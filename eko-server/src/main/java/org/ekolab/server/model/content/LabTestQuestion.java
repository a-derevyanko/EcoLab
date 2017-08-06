package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class LabTestQuestion implements DomainModel {
    private final List<LabTestQuestionVariant> variants;

    public LabTestQuestion(List<LabTestQuestionVariant> variants) {
        this.variants = variants;
    }

    public List<LabTestQuestionVariant> getVariants() {
        return variants;
    }

    public static class LabTestQuestionVariant implements DomainModel {
        private final String question;
        private final Image image;
        private final List<String> wrongAnswers;
        private final List<String> rightAnswers;

        public LabTestQuestionVariant(String question, Image image, List<String> wrongAnswers, List<String> rightAnswers) {
            this.question = question;
            this.image = image;
            this.wrongAnswers = wrongAnswers;
            this.rightAnswers = rightAnswers;
        }

        public String getQuestion() {
            return question;
        }

        public Image getImage() {
            return image;
        }

        public List<String> getWrongAnswers() {
            return wrongAnswers;
        }

        public List<String> getRightAnswers() {
            return rightAnswers;
        }
    }
}
