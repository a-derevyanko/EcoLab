package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.WritableValue;
import org.apache.commons.lang3.RandomUtils;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.client.vaadin.server.ui.windows.LabTestFinishedWindow;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.common.Role;
import org.ecolab.server.model.content.LabTest;
import org.ecolab.server.model.content.LabTestHomeWorkQuestion;
import org.ecolab.server.model.content.LabTestQuestion;
import org.ecolab.server.model.content.LabTestQuestionVariant;
import org.ecolab.server.model.content.LabTestQuestionVariantWithAnswers;
import org.ecolab.server.model.content.LabTestResult;
import org.ecolab.server.service.api.content.LabService;
import org.ecolab.server.service.api.content.UserLabService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 777Al on 03.04.2017.
 */
@RolesAllowed({Role.ADMIN, Role.TEACHER, Role.STUDENT})
@PrototypeScope // При повторном входе тест должен быть другим
public abstract class LabTestWizard extends Wizard implements View {
    private final LabTestFinishedWindow labTestFinishedWindow;

    protected final UserLabService userLabService;
    protected final LabService<?, ?> labService;

    // ---------------------------- Графические компоненты --------------------

    protected LabTestWizard(I18N i18N, UserLabService userLabService, LabService<?, ?> labService, LabTestFinishedWindow labTestFinishedWindow) {
        super(i18N);
        this.userLabService = userLabService;
        this.labService = labService;
        this.labTestFinishedWindow = labTestFinishedWindow;
    }

    @PostConstruct
    public void init() throws Exception {
        super.init();
        getFinishButton().setCaption(i18N.get("test.check"));

        getFinishButton().setIcon(VaadinIcons.FLAG_CHECKERED, i18N.get("test.check"));

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
    public void wizardCompleted(WizardCompletedEvent event) {
        Map<LabTestQuestionVariant, Object> answers = new HashMap<>();
        List<WizardStep> steps = getSteps();
        for (int i = 0; i < steps.size(); i++) {
            BaseLabTestQuestionView step = (BaseLabTestQuestionView) steps.get(i);
            Object answer = step.getAnswer();
            if (answer == null) {
                ComponentErrorNotification.show(i18N.get("test.not-selected", i + 1));
                return;
            } else {
                answers.put(step.getQuestion(), answer);
            }
        }

        LabTestResult result = labService.checkLabTest(labService.getCompletedLabByUser(CurrentUser.getId()),
                answers, UI.getCurrent().getLocale());

        if (result.getCompleted()) {
            userLabService.setTestCompleted(labService.getLabNumber(), result.getMark(), result.getPointCount());
        }
        labTestFinishedWindow.show(new LabTestFinishedWindow.LabFinishedWindowSettings(result, answers.keySet()));
    }

    /**
     * Счётчик времени выполнения теста. На тест отводится 25 минут. По истечении времени отображается
     * специальное сообщение и все вопросы, на которые не был дан ответ засчитываются как неправильные.
     * todo реализовать метод
     */
    @Scheduled(fixedRate = 25 * 60 * 1000)
    protected void testExecutionCounter() {

    }

    private static class LabTestQuestionView extends BaseLabTestQuestionView<RadioButtonGroup<String>> {
        // ---------------------------- Графические компоненты --------------------
        public LabTestQuestionView(I18N i18N, LabTestQuestionVariantWithAnswers questionVariant) {
            super(new RadioButtonGroup<>(), i18N, questionVariant);

            List<String> answers = new ArrayList<>(questionVariant.getAnswers());
            Collections.shuffle(answers);

            component.setHtmlContentAllowed(true);
            component.setItems(answers);
            if (questionVariant.getImage() == null) {
                setAnswerComponent(component);
            } else {
                HorizontalLayout imageAndAnswer = new HorizontalLayout();
                imageAndAnswer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
                Image image = new Image(null, new ThemeResource(questionVariant.getImage()));
                imageAndAnswer.addComponent(image);
                imageAndAnswer.addComponent(component);
                setAnswerComponent(imageAndAnswer);
            }
        }
    }

    private static class LabHomeWorkTestQuestionView extends BaseLabTestQuestionView<HasValue<?>> {
        private final Property<?> answer;

        // ---------------------------- Графические компоненты --------------------

        public LabHomeWorkTestQuestionView(I18N i18N, LabTestHomeWorkQuestion questionVariant) {
            super(getComponent(questionVariant, i18N), i18N, questionVariant);

            if (component instanceof TextField) {
                setAnswerComponent(new HorizontalLayout(
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
                setAnswerComponent((Component) component);
                Binder<SimpleObjectProperty<Boolean>> binder = new Binder<>();
                binder.forField((RadioButtonGroup<Boolean>) component).bind(WritableValue::getValue, WritableValue::setValue);
                binder.setBean(new SimpleObjectProperty<>());
                binder.getBean().setValue(null);
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
                field.addStyleName(EcoLabTheme.TEXTFIELD_TINY);
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
            questionText.setWidth(600.0F, Unit.PIXELS);
            setSizeFull();
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            addComponent(questionText);
            setExpandRatio(questionText, 0.1F);
        }

        public Object getAnswer() {
            return component.getValue();
        }

        public LabTestQuestionVariant getQuestion() {
            return question;
        }

        @Override
        public String getCaption() {
            return "";
        }

        @Override
        public Component getContent() {
            return this;
        }

        @Override
        public boolean onAdvance() {
            return true;
        }

        @Override
        public boolean onBack() {
            return true;
        }

        protected void setAnswerComponent(Component component) {
            addComponent(component);
            setExpandRatio(component, 1.0F);
        }
    }
}
