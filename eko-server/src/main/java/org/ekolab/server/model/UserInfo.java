package org.ekolab.server.model;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Created by 777Al on 22.05.2017.
 */
public class UserInfo implements DomainModel {
    @Size(max = 256)
    private String login;

    @Size(max = 256)
    private String firstName;

    @Size(max = 256)
    private String middleName;

    @Size(max = 256)
    private String lastName;

    @Size(max = 256)
    private String note;

    @Valid
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


}
