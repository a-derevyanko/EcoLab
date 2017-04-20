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
}
