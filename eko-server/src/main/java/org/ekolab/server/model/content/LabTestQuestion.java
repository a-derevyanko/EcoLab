package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.util.Objects;

public abstract class LabTestQuestion implements DomainModel {
    private String question;
    private String image;

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
        if (!(o instanceof LabTestQuestion)) return false;
        LabTestQuestion question1 = (LabTestQuestion) o;
        return Objects.equals(question, question1.question) &&
                Objects.equals(image, question1.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, image);
    }
}
