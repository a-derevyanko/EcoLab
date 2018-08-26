package org.ecolab.server.model.content;

import org.ecolab.server.model.DomainModel;

import java.util.List;

public class LabTestQuestion implements DomainModel {
    private List<LabTestQuestionVariant> variants;

    private String title;

    private int questionNumber;

    private int pointCount;

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

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }
}
