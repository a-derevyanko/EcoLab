package org.ecolab.client.vaadin.server.ui.windows;


import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.UserInfo;

import java.util.function.Consumer;

public class StudentDataWindowSettings extends UserDataWindowSettings {
    private final StudentInfo studentInfo;

    public StudentDataWindowSettings(UserInfo userInfo, Consumer<UserInfo> userInfoConsumer, StudentInfo studentInfo) {
        super(userInfo, userInfoConsumer);
        this.studentInfo = studentInfo;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }
}