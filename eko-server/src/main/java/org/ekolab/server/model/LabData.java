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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabData labData = (LabData) o;

        if (userLogin != null ? !userLogin.equals(labData.userLogin) : labData.userLogin != null) return false;
        if (startDate != null ? !startDate.equals(labData.startDate) : labData.startDate != null) return false;
        return saveDate != null ? saveDate.equals(labData.saveDate) : labData.saveDate == null;
    }

    @Override
    public int hashCode() {
        int result = userLogin != null ? userLogin.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (saveDate != null ? saveDate.hashCode() : 0);
        return result;
    }
}
