package org.ekolab.client.vaadin.server.ui.common;

import com.github.lotsabackscatter.blueimp.gallery.Gallery;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.LabTest;
import org.ekolab.server.model.content.LabTestQuestion;
import org.ekolab.server.service.api.content.LabService;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class LabTestStep extends Wizard implements LabWizardStep {
    protected final I18N i18N;
    protected final LabService<?> labService;

    // ---------------------------- Графические компоненты --------------------
    private final Gallery gallery = new Gallery();
    private final Button showGallery = new Button("Show presentation", VaadinIcons.PRESENTATION);

    protected LabTestStep(I18N i18N, LabService<?> labService) {
        this.i18N = i18N;
        this.labService = labService;
    }

    @Override
    @PostConstruct
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        addStyleName(EkoLabTheme.PANEL_WIZARD_PRESENTATION);

        LabTest test = labService.getLabTest(UI.getCurrent().getLocale());

        for (LabTestQuestion question : test.getQuestions()) {
            LabTestQuestion.LabTestQuestionVariant questionVariant = question.getVariants().get(RandomUtils.nextInt(0, question.getVariants().size()));
            addStep(new LabTestQuestionView(i18N, questionVariant));
        }
    }

    /**
     * Проверка результатов теста
     * @return признак того, что тест пройден удачно
     */
    @Override
    public boolean onAdvance() {
        return false;
    }
    /**
     * Проверка результатов теста
     */
    @Override
    public void finish() {
        Map<LabTestQuestion.LabTestQuestionVariant, String> answers = getSteps().stream().
                filter(s -> s instanceof LabTestQuestionView).
                map(LabTestQuestionView.class::cast).collect(Collectors.toMap(step ->
                step.questionVariant, LabTestQuestionView::getAnswer, (a, b) -> b));
        if (labService.checkLabTest(answers)) {
            super.finish();
        } else {
            ComponentErrorNotification.show(i18N.get("test.not-right"));
        }
    }

    private static class LabTestQuestionView extends GridLayout implements WizardStep {
        private final LabTestQuestion.LabTestQuestionVariant questionVariant;
        private final I18N i18N;

        // ---------------------------- Графические компоненты --------------------
        private final RadioButtonGroup<String> answersGroup = new RadioButtonGroup<>();

        public LabTestQuestionView(I18N i18N, LabTestQuestion.LabTestQuestionVariant questionVariant) {
            super(3, 6);
            this.questionVariant = questionVariant;
            this.i18N = i18N;

            List<String> answers = ListUtils.union(questionVariant.getWrongAnswers(), questionVariant.getRightAnswers());
            Collections.shuffle(answers);

            answersGroup.setItems(answers);
        }

        public String getAnswer() {
            return answersGroup.getSelectedItem().orElseThrow(IllegalStateException::new);
        }

        @Override
        public String getCaption() {
            return questionVariant.getQuestion();
        }

        @Override
        public Component getContent() {
            return this;
        }

        @Override
        public boolean onAdvance() {
            if (answersGroup.getSelectedItem().isPresent()) {
                return true;
            } else {
                ComponentErrorNotification.show(i18N.get("test.not-selected"));
                return false;
            }
        }

        @Override
        public boolean onBack() {
            return false;
        }
    }
}
