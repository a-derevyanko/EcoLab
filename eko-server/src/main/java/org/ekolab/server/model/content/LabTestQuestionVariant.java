package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.util.Objects;

public abstract class LabTestQuestionVariant implements DomainModel {
    private int number;
    private String question;
    private String image;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabTestQuestionVariant)) return false;
        LabTestQuestionVariant that = (LabTestQuestionVariant) o;
        return number == that.number &&
                Objects.equals(question, that.question) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, question, image);
    }
}
