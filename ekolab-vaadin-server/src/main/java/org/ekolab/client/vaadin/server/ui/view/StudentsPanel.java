package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
    private final Label studentGroupLabel = new Label();
    private final ComboBox<StudentGroup> studentGroupComboBox = new ComboBox<>();
    private final Label studentTeamLabel = new Label();
    private final ComboBox<StudentTeam> studentTeamComboBox = new ComboBox<>();
    private final HorizontalLayout studentFilters = new HorizontalLayout(studentGroupLabel, studentGroupComboBox, studentTeamLabel, studentTeamComboBox);

    public StudentsPanel(EditStudentWindow userDataWindow, UserInfoService userInfoService, NewStudentWindow newUserWindow,
                         StudentInfoDataProvider userInfoDataProvider, I18N i18N, StudentInfoService studentInfoService) {
        super(userDataWindow, userInfoService, newUserWindow, userInfoDataProvider, i18N);
        this.studentInfoService = studentInfoService;
    }

    @Override
    public void init() throws Exception {
        super.init();
        addComponent(studentFilters, 0);
        studentGroupLabel.setValue(i18N.get("studentData.group"));
        studentGroupComboBox.setItems(studentInfoService.getStudentGroups());
        studentGroupComboBox.setItemCaptionGenerator(StudentGroup::getName);
        studentGroupComboBox.addValueChangeListener(e -> searchUsers());

        studentTeamLabel.setValue(i18N.get("studentData.team"));
        studentTeamComboBox.setItems(studentInfoService.getStudentTeams());
        studentTeamComboBox.setItemCaptionGenerator(studentTeam -> String.valueOf(studentTeam.getNumber()));
        studentTeamComboBox.addValueChangeListener(e -> searchUsers());
    }

    @Override
    protected StudentInfoFilter createFilter() {
        return new StudentInfoFilter();
    }

    @Override
    protected StudentDataWindowSettings createSettings(ConfigurableFilterDataProvider<UserInfo, Void, StudentInfoFilter> dataProvider) {
        return new StudentDataWindowSettings(filter.getUserInfoFilter(), userInfo -> {
            dataProvider.refreshAll();

            if (dataProvider.size(new Query<>(null)) > 0) {
                users.select(userInfo);
            }
        }, filter.getStudentInfoFilter(), studentInfo -> studentInfo = null);
    }

    @Override
    protected StudentDataWindowSettings createSettingsForEdit(UserInfo userInfo, ConfigurableFilterDataProvider<UserInfo, Void, StudentInfoFilter> dataProvider) {
        return new StudentDataWindowSettings(userInfo, dataProvider::refreshItem,
                filter.getStudentInfoFilter(), studentInfo -> studentInfo = null);
    }

    @Override
    protected void searchUsers() {
        filter.getStudentInfoFilter().setGroup(studentGroupComboBox.getValue());
        filter.getStudentInfoFilter().setTeam(studentTeamComboBox.getValue());
        super.searchUsers();
    }
}
