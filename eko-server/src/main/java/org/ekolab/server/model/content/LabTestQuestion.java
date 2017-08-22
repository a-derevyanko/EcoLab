package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.util.List;

public class LabTestQuestion implements DomainModel {
    private List<LabTestQuestionVariant> variants;

    private String title;

    public List<LabTestQuestionVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<LabTestQuestionVariant> variants) {
        this.variants = variants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
