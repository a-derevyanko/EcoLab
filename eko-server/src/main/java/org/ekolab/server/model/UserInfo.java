package org.ekolab.server.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by 777Al on 22.05.2017.
 */
public class UserInfo extends IdentifiedDomainModel {
    @Size(max = 256)
    private String login;

    @Size(min = 1, max = 256)
    @NotNull
    private String firstName;

    @Size(max = 256)
    @NotNull
    private String middleName;

    @Size(min = 1, max = 256)
    @NotNull
    private String lastName;

    @Size(max = 256)
    private String note;

    @Valid
    @NotNull
    private UserGroup group;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(login, userInfo.login) &&
                Objects.equals(firstName, userInfo.firstName) &&
                Objects.equals(middleName, userInfo.middleName) &&
                Objects.equals(lastName, userInfo.lastName) &&
                Objects.equals(note, userInfo.note) &&
                group == userInfo.group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, firstName, middleName, lastName, note, group);
    }
}
