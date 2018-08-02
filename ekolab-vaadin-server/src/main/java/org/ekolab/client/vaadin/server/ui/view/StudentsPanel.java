package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.ekolab.client.vaadin.server.dataprovider.StudentInfoDataProvider;
import org.ekolab.client.vaadin.server.dataprovider.StudentInfoFilter;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.BaseUsersPanel;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.windows.EditStudentWindow;
import org.ekolab.client.vaadin.server.ui.windows.NewLabeledItemWindow;
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
    private final NewLabeledItemWindow<StudentGroup> newStudentGroupWindow;
    private final NewLabeledItemWindow<StudentTeam> newStudentTeamWindow;

    // ---------------------------- Графические компоненты --------------------
    private final Label studentGroupLabel = new Label();
    private final ComboBox<StudentGroup> studentGroupComboBox = new ComboBox<>();
    private final Button addStudentGroupButton = new Button(VaadinIcons.PLUS_CIRCLE);
    private final Button removeStudentGroupButton = new Button(VaadinIcons.MINUS_CIRCLE);
    private final Label studentTeamLabel = new Label();
    private final ComboBox<StudentTeam> studentTeamComboBox = new ComboBox<>();
    private final Button addStudentTeamButton = new Button(VaadinIcons.PLUS_CIRCLE);
    private final Button removeStudentTeamButton = new Button(VaadinIcons.MINUS_CIRCLE);
    private final GridLayout filters = new GridLayout(14, 1);

    public StudentsPanel(EditStudentWindow userDataWindow, UserInfoService userInfoService, NewStudentWindow newUserWindow,
                         StudentInfoDataProvider userInfoDataProvider, I18N i18N, StudentInfoService studentInfoService, NewLabeledItemWindow<StudentGroup> newStudentGroupWindow, NewLabeledItemWindow<StudentTeam> newStudentTeamWindow) {
        super(userDataWindow, userInfoService, newUserWindow, userInfoDataProvider, i18N);
        this.studentInfoService = studentInfoService;
        this.newStudentGroupWindow = newStudentGroupWindow;
        this.newStudentTeamWindow = newStudentTeamWindow;
    }

    @Override
    public void init() throws Exception {
        super.init();
        addComponent(filters, 0);
        filters.setSpacing(true);
        filters.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        filters.addComponent(studentGroupLabel, 0, 0);
        filters.addComponent(studentGroupComboBox, 1, 0, 3, 0);
        filters.addComponent(addStudentGroupButton, 4, 0);
        filters.addComponent(removeStudentGroupButton, 5, 0);

       filters.addComponent(studentTeamLabel, 7, 0);
       filters.addComponent(studentTeamComboBox, 8, 0, 11, 0);
       filters.addComponent(addStudentTeamButton, 12, 0);
       filters.addComponent(removeStudentTeamButton, 13, 0);

        addStudentGroupButton.setSizeFull();
        removeStudentGroupButton.setSizeFull();
        addStudentGroupButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addStudentGroupButton.setDescription(i18N.get("student-data.add-group"));
        addStudentGroupButton.addClickListener(event -> newStudentGroupWindow.show(new NewLabeledItemWindow.NewItemWindowSettings<>(
                group -> {
                    studentGroupComboBox.setSelectedItem(group);
                    studentTeamComboBox.setItems(studentInfoService.getStudentTeams(group.getName()));
                    }, studentInfoService::isGroupExists,
                studentInfoService::createStudentGroup)));
        removeStudentGroupButton.setStyleName(EkoLabTheme.BUTTON_DANGER);
        removeStudentGroupButton.setEnabled(false);
        removeStudentGroupButton.setDescription(i18N.get("student-data.remove-group"));
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

        addStudentTeamButton.setSizeFull();
        removeStudentTeamButton.setSizeFull();
        removeStudentTeamButton.setDescription(i18N.get("student-data.remove-team"));
        addStudentTeamButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addStudentTeamButton.setEnabled(false);
        addStudentTeamButton.setDescription(i18N.get("student-data.add-team"));
        addStudentTeamButton.addClickListener(event -> newStudentTeamWindow.show(new NewLabeledItemWindow.NewItemWindowSettings<>(
                team -> {
                    String group = studentGroupComboBox.getSelectedItem().orElseThrow(IllegalStateException::new).getName();
                    studentTeamComboBox.setItems(studentInfoService.getStudentTeams(group));
                    studentTeamComboBox.setSelectedItem(team);
                }, o -> {
                    StudentGroup group = studentGroupComboBox.getSelectedItem().orElseThrow(IllegalStateException::new);
                    return studentInfoService.isTeamExists(group, o);
                },
                s -> {
                    String group = studentGroupComboBox.getSelectedItem().orElseThrow(IllegalStateException::new).getName();
                    return studentInfoService.createStudentTeam(s, group);
                })));
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
        }, filter.getStudentInfoFilter());
    }

    @Override
    protected StudentDataWindowSettings createSettingsForEdit(UserInfo userInfo, ConfigurableFilterDataProvider<UserInfo, Void, StudentInfoFilter> dataProvider) {
        return new StudentDataWindowSettings(userInfo, dataProvider::refreshItem,
                filter.getStudentInfoFilter());
    }

    @Override
    protected void searchUsers() {
        filter.getStudentInfoFilter().setGroup(studentGroupComboBox.getValue());
        filter.getStudentInfoFilter().setTeam(studentTeamComboBox.getValue());
        super.searchUsers();
    }
}
