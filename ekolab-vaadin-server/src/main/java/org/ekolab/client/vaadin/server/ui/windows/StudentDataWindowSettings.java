package org.ekolab.client.vaadin.server.ui.windows;


import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserInfo;

import java.util.function.Consumer;

public class StudentDataWindowSettings extends UserDataWindowSettings {
    private final StudentInfo studentInfo;
    private final Consumer<StudentInfo> studentInfoConsumer;

    public StudentDataWindowSettings(UserInfo userInfo, Consumer<UserInfo> userInfoConsumer, StudentInfo studentInfo,
                                     Consumer<StudentInfo> studentInfoConsumer) {
        super(userInfo, userInfoConsumer);
        this.studentInfo = studentInfo;
        this.studentInfoConsumer = studentInfoConsumer;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public Consumer<StudentInfo> getStudentInfoConsumer() {
        return studentInfoConsumer;
    }
}