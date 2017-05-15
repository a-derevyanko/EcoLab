package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabData<V extends LabVariant> implements DomainModel {
    @NotNull
    private V variant;

    @NotNull
    private String userLogin;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime saveDate;

    @NotNull
    private boolean completed;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public V getVariant() {
        return variant;
    }

    public void setVariant(V variant) {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabData)) return false;
        LabData<?> labData = (LabData<?>) o;
        return completed == labData.completed &&
                Objects.equals(variant, labData.variant) &&
                Objects.equals(userLogin, labData.userLogin) &&
                Objects.equals(startDate, labData.startDate) &&
                Objects.equals(saveDate, labData.saveDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variant, userLogin, startDate, saveDate, completed);
    }
}
