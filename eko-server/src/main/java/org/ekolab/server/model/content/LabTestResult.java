package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import java.util.List;

public class LabTestResult implements DomainModel {
    private List<Integer> errors;
    private Integer pointCount;
    private Boolean completed;
    private Byte mark;

    public List<Integer> getErrors() {
        return errors;
    }

    public void setErrors(List<Integer> errors) {
        this.errors = errors;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Byte getMark() {
        return mark;
    }

    public void setMark(Byte mark) {
        this.mark = mark;
    }
}
