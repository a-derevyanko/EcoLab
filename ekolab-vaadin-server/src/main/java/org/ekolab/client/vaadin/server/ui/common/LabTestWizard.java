package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.*;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.property.*;
import javafx.beans.value.WritableValue;
import org.apache.commons.lang3.RandomUtils;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.common.Role;
import org.ekolab.server.model.content.*;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 777Al on 03.04.2017.
 */
@RolesAllowed(Role.STUDENT)
public abstract class LabTestWizard extends Wizard implements View {
    @Autowired
    private Authentication currentUser;

    protected final I18N i18N;
    protected final LabService<?> labService;

    // ---------------------------- Графические компоненты --------------------

    protected LabTestWizard(I18N i18N, LabService<?> labService) {
        this.i18N = i18N;
        this.labService = labService;
    }

    @PostConstruct
    public void init() throws IOException {
        setSizeFull();
        mainLayout.setSizeFull();
        getHeader().setVisible(false);
        getCancelButton().setVisible(false);

        addStyleName(EkoLabTheme.PANEL_WIZARD);
        getFinishButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getFinishButton().addStyleName(EkoLabTheme.BUTTON_TINY);
        getNextButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getNextButton().addStyleName(EkoLabTheme.BUTTON_TINY);
        getBackButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getBackButton().addStyleName(EkoLabTheme.BUTTON_TINY);

        getBackButton().setCaption(i18N.get("labwizard.back"));
        getNextButton().setCaption(i18N.get("labwizard.next"));
        getFinishButton().setCaption(i18N.get("labwizard.finish"));

        getFinishButton().setIcon(VaadinIcons.FLAG_CHECKERED, i18N.get("labwizard.finish"));
        getNextButton().setIcon(VaadinIcons.ARROW_FORWARD, i18N.get("labwizard.next"));
        getBackButton().setIcon(VaadinIcons.ARROW_BACKWARD, i18N.get("labwizard.back"));

        footer.removeComponent(getCancelButton());

        LabTest test = labService.getLabTest(UI.getCurrent().getLocale());

        for (LabTestQuestion question : test.getQuestions()) {
            LabTestQuestionVariant questionVariant = question.getVariants().get(RandomUtils.nextInt(0, question.getVariants().size()));
            if (questionVariant instanceof LabTestQuestionVariantWithAnswers) {
                addStep(new LabTestQuestionView(i18N, (LabTestQuestionVariantWithAnswers) questionVariant));
            } else {
                addStep(new LabHomeWorkTestQuestionView(i18N, (LabTestHomeWorkQuestion) questionVariant));
            }
        }
    }

    /**
     * Проверка результатов теста
     */
    @Override
    public void finish() {
        Map<LabTestQuestionVariant, Object> answers = getSteps().stream().
                filter(s -> s instanceof BaseLabTestQuestionView<?>).
                map(BaseLabTestQuestionView.class::cast).collect(Collectors.
                toMap(BaseLabTestQuestionView::getQuestion, BaseLabTestQuestionView::getAnswer, (a, b) -> b));
        int errors = labService.checkLabTest(labService.getLastUncompletedLabByUser(currentUser.getName()), answers);
        if (errors == 0) {
            labService.setTestCompleted(currentUser.getName());
            super.finish();
        } else {
            ComponentErrorNotification.show(i18N.get("test.not-right", errors));
        }
    }

    private static class LabTestQuestionView extends BaseLabTestQuestionView<RadioButtonGroup<String>> {
        // ---------------------------- Графические компоненты --------------------

        public LabTestQuestionView(I18N i18N, LabTestQuestionVariantWithAnswers questionVariant) {
            super(new RadioButtonGroup<>(), i18N, questionVariant);

            List<String> answers = new ArrayList<>(questionVariant.getAnswers());
            Collections.shuffle(answers);

            component.setHtmlContentAllowed(true);
            component.setItems(answers);
            if (questionVariant.getImage() != null) {
                Image image = new Image(null, new ThemeResource(questionVariant.getImage()));
                addComponent(image);
                setExpandRatio(image, 1.0F);
            }
            addComponent(component);
        }
    }

    private static class LabHomeWorkTestQuestionView extends BaseLabTestQuestionView<HasValue<?>> {
        private final Property<?> answer;

        // ---------------------------- Графические компоненты --------------------

        public LabHomeWorkTestQuestionView(I18N i18N, LabTestHomeWorkQuestion questionVariant) {
            super(getComponent(questionVariant, i18N), i18N, questionVariant);

            if (component instanceof TextField) {
                addComponent(new HorizontalLayout(
                        new Label(i18N.get("test.solution")),
                        (Component) component,
                        new Label(questionVariant.getDimension(), ContentMode.HTML)));

                if (Integer.class.isAssignableFrom(questionVariant.getValueType())) {
                    Binder<SimpleIntegerProperty> binder = new Binder<>();
                    binder.forField((TextField) component).withConverter(new StringToIntegerConverter(""))
                    .bind(IntegerExpression::getValue, WritableValue::setValue);
                    binder.setBean(new SimpleIntegerProperty());
                    answer = binder.getBean();
                } else if (Double.class.isAssignableFrom(questionVariant.getValueType())) {
                    Binder<SimpleDoubleProperty> binder = new Binder<>();
                    binder.forField((TextField) component).withConverter(new StringToDoubleConverter(""))
                            .bind(DoubleExpression::getValue, WritableValue::setValue);
                    binder.setBean(new SimpleDoubleProperty());
                    answer = binder.getBean();
                }else if (String.class.isAssignableFrom(questionVariant.getValueType())) {
                    Binder<SimpleStringProperty> binder = new Binder<>();
                    binder.forField((TextField) component)
                            .bind(WritableValue::getValue, WritableValue::setValue);
                    binder.setBean(new SimpleStringProperty());
                    answer = binder.getBean();
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                addComponent((Component) component);
                Binder<SimpleBooleanProperty> binder = new Binder<>();
                binder.forField((RadioButtonGroup<Boolean>) component).bind(WritableValue::getValue, WritableValue::setValue);
                binder.setBean(new SimpleBooleanProperty());
                answer = binder.getBean();
            }
        }

        @Override
        public Object getAnswer() {
            return answer.getValue();
        }

        private static HasValue<?> getComponent(LabTestHomeWorkQuestion questionVariant, I18N i18N) {
            if (questionVariant.getValueType().equals(Boolean.class)) {
                RadioButtonGroup<Boolean> answersGroup = new RadioButtonGroup<>(null, Arrays.asList(Boolean.FALSE, Boolean.TRUE));
                answersGroup.setItemCaptionGenerator(item -> Boolean.TRUE.equals(item) ? i18N.get("labwizard.yes-value") : i18N.get("labwizard.no-value"));
                return answersGroup;
            } else if (questionVariant.getValueType() == String.class || Number.class.isAssignableFrom(questionVariant.getValueType())) {
                TextField field = new TextField();
                field.addStyleName(EkoLabTheme.TEXTFIELD_TINY);
                return field;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static abstract class BaseLabTestQuestionView<C extends HasValue<?>> extends VerticalLayout implements WizardStep {
        protected final LabTestQuestionVariant question;
        protected final I18N i18N;

        // ---------------------------- Графические компоненты --------------------
        protected final C component;
        protected final Label questionText = new Label(null, ContentMode.HTML);

        public BaseLabTestQuestionView(C component, I18N i18N, LabTestQuestionVariant question) {
            this.question = question;
            this.i18N = i18N;
            this.component = component;
            questionText.setValue(question.getQuestion());
            setSizeFull();
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            addComponent(questionText);
        }

        public Object getAnswer() {
            return component.getValue();
        }

        public LabTestQuestionVariant getQuestion() {
            return question;
        }

        @Override
        public String getCaption() {
            return question.getQuestion();
        }

        @Override
        public Component getContent() {
            return this;
        }

        @Override
        public boolean onAdvance() {
            if (component.isEmpty()) {
                ComponentErrorNotification.show(i18N.get("test.not-selected"));
                return false;
            } else {
                return true;
            }
        }

        @Override
        public boolean onBack() {
            return true;
        }
    }
}
