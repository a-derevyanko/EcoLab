package org.ecolab.server.model.content;

import org.ecolab.server.model.DomainModel;

import java.util.Collection;
import java.util.Objects;

public class LabTest implements DomainModel {
    private Collection<LabTestQuestion> questions;

    public Collection<LabTestQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<LabTestQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabTest)) return false;
        var test = (LabTest) o;
        return Objects.equals(questions, test.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions);
    }
}
