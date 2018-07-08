package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.AddableComboBox;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.StudentTeam;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class NewStudentWindow extends NewUserWindow<StudentDataWindowSettings> {
    // ---------------------------- Графические компоненты --------------------
    private final AddableComboBox<StudentGroup> studentGroupComboBox = new AddableComboBox<>();
    private final AddableComboBox<StudentTeam> studentTeamComboBox = new AddableComboBox<>();

    // ------------------------------------ Данные экземпляра -------------------------------------
    private final Binder<StudentInfo> studentInfoBinder = new BeanValidationBinder<>(StudentInfo.class);
    private final StudentInfoService studentInfoService;
    private final NewLabeledItemWindow<StudentGroup> groupNewLabeledItemWindow;
    private final NewLabeledItemWindow<StudentTeam> teamNewLabeledItemWindow;

    public NewStudentWindow(I18N i18N,UserInfoService userInfoService, UserSavedWindow userSavedWindow, StudentInfoService studentInfoService, NewLabeledItemWindow<StudentGroup> groupNewLabeledItemWindow, NewLabeledItemWindow<StudentTeam> teamNewLabeledItemWindow) {
        super(i18N,  userInfoService, userSavedWindow);
        this.studentInfoService = studentInfoService;
        this.groupNewLabeledItemWindow = groupNewLabeledItemWindow;
        this.teamNewLabeledItemWindow = teamNewLabeledItemWindow;
    }

    @PostConstruct
    @Override
    public void init() {
        super.init();
        content.addComponent(studentGroupComboBox, 5);
        content.addComponent(studentTeamComboBox, 6);

        studentGroupComboBox.setCaption(i18N.get("student-data.group"));
        studentTeamComboBox.setCaption(i18N.get("student-data.team"));

        studentTeamComboBox.setEnabled(false);

        studentGroupComboBox.initSettings(i18N.get("student-data.add-group"),
                studentInfoService::getStudentGroups,
                StudentGroup::getName,
                event -> {
                    StudentGroup group = event.getValue();
                    if (group == null) {
                        studentTeamComboBox.clear();
                        studentTeamComboBox.setEnabled(false);
                    } else {
                        studentTeamComboBox.refreshItems();
                        studentTeamComboBox.setEnabled(true);
                    }
                },
                studentInfoService::isGroupExists,
                studentInfoService::createStudentGroup,
                groupNewLabeledItemWindow);
        studentTeamComboBox.initSettings(i18N.get("student-data.add-team"),
                () -> studentInfoService.getStudentTeams(studentGroupComboBox.getValue().getName()),
                StudentTeam::getName,
                null,
                v -> studentInfoService.isTeamExists(studentGroupComboBox.getValue(), v),
                s -> studentInfoService.createStudentTeam(s, studentGroupComboBox.getValue().getName()),
                teamNewLabeledItemWindow);

        studentInfoBinder.forField(studentGroupComboBox).bind("group");
        studentInfoBinder.forField(studentTeamComboBox).bind("team");
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        studentGroupComboBox.setItems(studentInfoService.getStudentGroups());
        if (settings.getStudentInfo().getGroup() != null) {
            studentTeamComboBox.setItems(studentInfoService.getStudentTeams(settings.getStudentInfo().getGroup().getName()));
            studentGroupComboBox.setValue(settings.getStudentInfo().getGroup());
            studentTeamComboBox.setValue(settings.getStudentInfo().getTeam());
        } else {
            studentGroupComboBox.setValue(null);
        }
    }

    @Override
    protected UserInfo saveUserInfo() {
        UserInfo userInfo = super.saveUserInfo();
        studentInfoService.createStudentInfo(userInfo, studentGroupComboBox.getValue(), studentTeamComboBox.getValue());
        return userInfo;
    }

    @Override
    protected Collection<Binder<?>> checkedBinders() {
        return Arrays.asList(studentInfoBinder, userInfoBinder);
    }
}
