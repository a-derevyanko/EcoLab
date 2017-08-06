package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.util.Collection;

public class LabTest implements DomainModel {
    private final Collection<LabTestQuestion> questions;

    public LabTest(Collection<LabTestQuestion> questions) {
        this.questions = questions;
    }

    public Collection<LabTestQuestion> getQuestions() {
        return questions;
    }
}
