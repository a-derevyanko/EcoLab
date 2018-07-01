package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.ekolab.client.vaadin.server.dataprovider.StudentInfoDataProvider;
import org.ekolab.client.vaadin.server.dataprovider.StudentInfoFilter;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.BaseUsersPanel;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.windows.EditStudentWindow;
import org.ekolab.client.vaadin.server.ui.windows.NewNamedEntityWindow;
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
    private final NewNamedEntityWindow newNamedEntityWindow;

    // ---------------------------- Графические компоненты --------------------
    private final Label studentGroupLabel = new Label();
    private final ComboBox<StudentGroup> studentGroupComboBox = new ComboBox<>();
    private final Button addStudentGroupButton = new Button("Add", VaadinIcons.PLUS_CIRCLE);
    private final Button removeStudentGroupButton = new Button(VaadinIcons.MINUS_CIRCLE);
    private final Label studentTeamLabel = new Label();
    private final ComboBox<StudentTeam> studentTeamComboBox = new ComboBox<>();
    private final Button addStudentTeamButton = new Button("Add", VaadinIcons.PLUS_CIRCLE);
    private final Button removeStudentTeamButton = new Button(VaadinIcons.MINUS_CIRCLE);
    private final GridLayout groupFilters = new GridLayout(4, 2);
    private final GridLayout teamFilters = new GridLayout(4, 2);
    private final HorizontalLayout studentFilters = new HorizontalLayout(groupFilters, teamFilters);

    public StudentsPanel(EditStudentWindow userDataWindow, UserInfoService userInfoService, NewStudentWindow newUserWindow,
                         StudentInfoDataProvider userInfoDataProvider, I18N i18N, StudentInfoService studentInfoService,
                         NewNamedEntityWindow newNamedEntityWindow) {
        super(userDataWindow, userInfoService, newUserWindow, userInfoDataProvider, i18N);
        this.studentInfoService = studentInfoService;
        this.newNamedEntityWindow = newNamedEntityWindow;
    }

    @Override
    public void init() throws Exception {
        super.init();
        addComponent(studentFilters, 0);
        groupFilters.setSpacing(true);
        groupFilters.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        groupFilters.addComponent(studentGroupLabel, 0, 0);
        groupFilters.addComponent(studentGroupComboBox, 1, 0, 3, 0);
        groupFilters.addComponent(addStudentGroupButton, 0, 1, 1, 1);
        groupFilters.addComponent(removeStudentGroupButton, 2, 1, 3, 1);

        addStudentGroupButton.setSizeFull();
        removeStudentGroupButton.setSizeFull();
        addStudentGroupButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addStudentGroupButton.setCaption(i18N.get("student-data.add-group"));
        addStudentGroupButton.addClickListener(event -> newNamedEntityWindow.show(new NewNamedEntityWindow.NamedEntityWindowSettings(
                i18N.get("student-data.add-group"),
                s -> {
                    StudentGroup group = studentInfoService.createStudentGroup(s);
                    studentGroupComboBox.setSelectedItem(group);
                    studentTeamComboBox.setItems(studentInfoService.getStudentTeams(s));
                }
        )));
        removeStudentGroupButton.setStyleName(EkoLabTheme.BUTTON_DANGER);
        removeStudentGroupButton.setEnabled(false);
        studentGroupLabel.setValue(i18N.get("student-data.group"));
        studentGroupComboBox.setItems(studentInfoService.getStudentGroups());
        studentGroupComboBox.setTextInputAllowed(false);
        studentGroupComboBox.setItemCaptionGenerator(StudentGroup::getName);
        studentGroupComboBox.addValueChangeListener(e -> {
            if (e.getValue() == null) {
                removeStudentGroupButton.setEnabled(false);
                studentTeamComboBox.setEnabled(false);
                studentTeamComboBox.clear();
                addStudentTeamButton.setEnabled(false);
            } else {
                removeStudentGroupButton.setEnabled(true);
                studentTeamComboBox.setEnabled(true);
                addStudentTeamButton.setEnabled(true);
                studentTeamComboBox.setItems(studentInfoService.getStudentTeams(e.getValue().getName()));
            }
            searchUsers();
        });

        teamFilters.setSpacing(true);
        teamFilters.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        teamFilters.addComponent(studentTeamLabel, 0, 0);
        teamFilters.addComponent(studentTeamComboBox, 1, 0, 3, 0);
        teamFilters.addComponent(addStudentTeamButton, 0, 1, 1, 1);
        teamFilters.addComponent(removeStudentTeamButton, 2, 1, 3, 1);

        addStudentTeamButton.setSizeFull();
        removeStudentTeamButton.setSizeFull();
        addStudentTeamButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addStudentTeamButton.setEnabled(false);
        addStudentTeamButton.setCaption(i18N.get("student-data.add-team"));
        addStudentTeamButton.addClickListener(event -> newNamedEntityWindow.show(new NewNamedEntityWindow.NamedEntityWindowSettings(
                i18N.get("student-data.add-team"),
                s -> {
                    String group = studentGroupComboBox.getSelectedItem().orElseThrow(IllegalStateException::new).getName();
                    StudentTeam team = studentInfoService.createStudentTeam(s, group);
                    studentTeamComboBox.setItems(studentInfoService.getStudentTeams(group));
                    studentTeamComboBox.setSelectedItem(team);
                }
        )));
        removeStudentTeamButton.setStyleName(EkoLabTheme.BUTTON_DANGER);
        removeStudentTeamButton.setEnabled(false);
        removeStudentTeamButton.addClickListener(event -> studentInfoService.removeStudentTeam(studentTeamComboBox.
                        getSelectedItem().orElseThrow(IllegalStateException::new).getName(),
                studentGroupComboBox.getSelectedItem().orElseThrow(IllegalStateException::new).getName()));
        studentTeamLabel.setValue(i18N.get("student-data.team"));
        studentTeamComboBox.setTextInputAllowed(false);
        studentTeamComboBox.setItemCaptionGenerator(studentTeam -> String.valueOf(studentTeam.getName()));
        studentTeamComboBox.addValueChangeListener(e -> {
            removeStudentTeamButton.setEnabled(e.getValue() != null);
            searchUsers();
        });
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
