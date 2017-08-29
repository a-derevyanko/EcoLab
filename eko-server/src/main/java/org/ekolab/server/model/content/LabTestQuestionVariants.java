package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.util.List;

public class LabTestQuestionVariants implements DomainModel {
    private List<LabTestQuestion> variants;

    private String title;

    public List<LabTestQuestion> getVariants() {
        return variants;
    }

    public void setVariants(List<LabTestQuestion> variants) {
        this.variants = variants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
