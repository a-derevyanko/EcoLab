package org.ekolab.server.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabData {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabData labData = (LabData) o;

        if (completed != labData.completed) return false;
        if (!userLogin.equals(labData.userLogin)) return false;
        if (!startDate.equals(labData.startDate)) return false;
        return saveDate.equals(labData.saveDate);
    }

    @Override
    public int hashCode() {
        int result = userLogin.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + saveDate.hashCode();
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }
}
