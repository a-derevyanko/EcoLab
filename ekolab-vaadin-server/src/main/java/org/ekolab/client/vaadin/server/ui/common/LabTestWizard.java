package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.apache.commons.lang3.RandomUtils;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.common.Authorize;
import org.ekolab.server.model.content.*;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 777Al on 03.04.2017.
 */
@PreAuthorize(Authorize.HasAuthorities.STUDENT)
public abstract class LabTestWizard extends Wizard implements View {
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
        getFinishButton().setVisible(false);

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
        footer.removeComponent(getBackButton());

        LabTest test = labService.getLabTest(UI.getCurrent().getLocale());

        for (LabTestQuestionVariants question : test.getQuestions()) {
            LabTestQuestion questionVariant = question.getVariants().get(RandomUtils.nextInt(0, question.getVariants().size()));
            if (questionVariant instanceof LabTestQuestionVariant) {
                addStep(new LabTestQuestionView(i18N, (LabTestQuestionVariant) questionVariant));
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
        Map<LabTestQuestion, Object> answers = getSteps().stream().
                filter(s -> s instanceof BaseLabTestQuestionView<?>).
                map(BaseLabTestQuestionView.class::cast).collect(Collectors.
                toMap(BaseLabTestQuestionView::getQuestion, BaseLabTestQuestionView::getAnswer, (a, b) -> b));
        if (labService.checkLabTest(answers)) {
            super.finish();
        } else {
            ComponentErrorNotification.show(i18N.get("test.not-right"));
        }
    }

    private static class LabTestQuestionView extends BaseLabTestQuestionView<RadioButtonGroup<String>> {
        // ---------------------------- Графические компоненты --------------------
        private final Image image = new Image();
        private final Label question = new Label();

        public LabTestQuestionView(I18N i18N, LabTestQuestionVariant questionVariant) {
            super(1, 3, new RadioButtonGroup<>(), i18N, questionVariant);

            List<String> answers = new ArrayList<>(questionVariant.getAnswers());
            Collections.shuffle(answers);

            component.setItems(answers);
            question.setValue(questionVariant.getQuestion());
            addComponent(question, 0, 0); //todo
            if (questionVariant.getImage() != null) {
                image.setSource(new ThemeResource(questionVariant.getImage()));
            }
            addComponent(image, 0, 1);
            addComponent(component, 0, 2); //todo
        }
    }

    private static class LabHomeWorkTestQuestionView extends BaseLabTestQuestionView<HasValue<?>> {
        // ---------------------------- Графические компоненты --------------------

        public LabHomeWorkTestQuestionView(I18N i18N, LabTestHomeWorkQuestion questionVariant) {
            super(3, 6, getComponent(questionVariant, i18N), i18N, questionVariant);
        }

        private static HasValue<?> getComponent(LabTestHomeWorkQuestion questionVariant, I18N i18N) {
            if (questionVariant.getValueType().equals(Boolean.class)) {
                RadioButtonGroup<Boolean> answersGroup = new RadioButtonGroup<>(null, Arrays.asList(Boolean.FALSE, Boolean.TRUE));
                answersGroup.setItemCaptionGenerator(item -> Boolean.TRUE.equals(item) ? i18N.get("labwizard.yes-value") : i18N.get("labwizard.no-value"));
                return answersGroup;
            } else if (questionVariant.getValueType() == String.class || Number.class.isAssignableFrom(questionVariant.getValueType())) {
                TextField field = new TextField();
                new Binder<String>().forField(field).withConverter(UIUtils.getStringConverter(questionVariant.getValueType(), null, i18N));
                field.addStyleName(EkoLabTheme.TEXTFIELD_TINY);
                return field;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static abstract class BaseLabTestQuestionView<C extends HasValue<?>> extends GridLayout implements WizardStep {
        protected final LabTestQuestion question;
        protected final I18N i18N;

        // ---------------------------- Графические компоненты --------------------
        protected final C component;

        public BaseLabTestQuestionView(int columns, int rows, C component, I18N i18N, LabTestQuestion question) {
            super(columns, rows);
            this.question = question;
            this.i18N = i18N;
            this.component = component;
        }

        public Object getAnswer() {
            return component.getValue();
        }

        public LabTestQuestion getQuestion() {
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
            return false;
        }
    }
}
