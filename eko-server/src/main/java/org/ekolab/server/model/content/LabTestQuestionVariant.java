package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class LabTestQuestionVariant implements DomainModel {
    private String question;
    private Image image;
    private List<String> answers;
    private int rightAnswer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabTestQuestionVariant)) return false;
        LabTestQuestionVariant that = (LabTestQuestionVariant) o;
        return rightAnswer == that.rightAnswer &&
                Objects.equals(question, that.question) &&
                Objects.equals(image, that.image) &&
                Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, image, answers, rightAnswer);
    }
}
