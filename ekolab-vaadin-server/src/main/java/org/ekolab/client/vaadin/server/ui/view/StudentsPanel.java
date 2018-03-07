package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import org.ekolab.client.vaadin.server.dataprovider.StudentInfoDataProvider;
import org.ekolab.client.vaadin.server.dataprovider.StudentInfoFilter;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.BaseUsersPanel;
import org.ekolab.client.vaadin.server.ui.windows.EditStudentWindow;
import org.ekolab.client.vaadin.server.ui.windows.NewStudentWindow;
import org.ekolab.client.vaadin.server.ui.windows.StudentDataWindowSettings;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentTeam;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@PrototypeScope
public class StudentsPanel extends BaseUsersPanel<StudentDataWindowSettings, StudentInfoFilter> {
    private final StudentInfoService studentInfoService;

    // ---------------------------- Графические компоненты --------------------
    private final ComboBox<StudentGroup> studentGroupComboBox = new ComboBox<>();
    private final ComboBox<StudentTeam> studentTeamComboBox = new ComboBox<>();

    public StudentsPanel(EditStudentWindow userDataWindow, UserInfoService userInfoService, NewStudentWindow newUserWindow,
                         StudentInfoDataProvider userInfoDataProvider, I18N i18N, StudentInfoService studentInfoService) {
        super(userDataWindow, userInfoService, newUserWindow, userInfoDataProvider, i18N);
        this.studentInfoService = studentInfoService;
    }

    @Override
    public void init() throws Exception {
        super.init();
        addComponent(studentGroupComboBox, 0);
        addComponent(studentTeamComboBox, 1);
        studentGroupComboBox.setItems(studentInfoService.getStudentGroups());
        studentGroupComboBox.setItemCaptionGenerator(StudentGroup::getName);

        studentTeamComboBox.setItems(studentInfoService.getStudentTeams());
        studentTeamComboBox.setItemCaptionGenerator(studentTeam -> String.valueOf(studentTeam.getNumber()));
    }

    @Override
    protected StudentInfoFilter createFilter() {
        return new StudentInfoFilter();
    }

    @Override
    protected StudentDataWindowSettings createSettings(ConfigurableFilterDataProvider<UserInfo, Void, StudentInfoFilter> dataProvider) {
        return new StudentDataWindowSettings(filter.getUserInfoFilter(), userInfo -> {
            dataProvider.refreshAll();
            users.select(userInfo);
        }, filter.getStudentInfoFilter(), studentInfo -> studentInfo = null);
    }

    @Override
    protected StudentDataWindowSettings createSettingsForEdit(UserInfo userInfo, ConfigurableFilterDataProvider<UserInfo, Void, StudentInfoFilter> dataProvider) {
        return new StudentDataWindowSettings(userInfo, dataProvider::refreshItem,
                filter.getStudentInfoFilter(), studentInfo -> studentInfo = null);
    }
}
