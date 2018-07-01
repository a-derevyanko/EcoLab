package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.client.vaadin.server.ui.windows.AddTeacherGroupsWindow;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentGroupInfo;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Set;

@SpringView(name = TeacherGroupsManagingView.NAME)
public class TeacherGroupsManagingView extends GridLayout implements View {
    public static final String NAME = "teacher-account";

    private final Authentication currentUser;

    private final AddTeacherGroupsWindow addTeacherGroupsWindow;

    private final UserLabService userLabService;

    private final StudentInfoService studentInfoService;

    private final EkoLabNavigator navigator;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Label userInitialsLabel = new Label("Surname Firstname Lastname");
    private final Label todayDate = new Label();
    private final Grid<StudentGroupInfo> groups = new Grid<>();
    private final Button manageGroupButton = new Button("View group", event ->
            manageGroup(groups.getSelectedItems().iterator().next()));
    private final Button addGroupButton = new Button();
    private final Button removeGroupButton = new Button();
    private final HorizontalLayout buttons = new HorizontalLayout(manageGroupButton, addGroupButton, removeGroupButton);
    private final VerticalLayout groupsPanel = new VerticalLayout(groups, buttons);

    @Autowired
    public TeacherGroupsManagingView(Authentication currentUser,
                                     AddTeacherGroupsWindow addTeacherGroupsWindow, UserLabService userLabService, StudentInfoService studentInfoService, EkoLabNavigator navigator, I18N i18N) {
        super(2, 1);
        this.currentUser = currentUser;
        this.addTeacherGroupsWindow = addTeacherGroupsWindow;
        this.userLabService = userLabService;
        this.studentInfoService = studentInfoService;
        this.navigator = navigator;
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        View.super.init();
        setMargin(true);
        setSpacing(true);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        addComponent(userInitialsLabel, 0, 0, 0, 0);
        addComponent(groupsPanel, 1, 0, 1, 0);

        groups.setSelectionMode(Grid.SelectionMode.SINGLE);
        groups.addItemClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                manageGroup(event.getItem());
            }
        });

        groups.addSelectionListener(event ->
                manageGroupButton.setEnabled(!event.getAllSelectedItems().isEmpty()));

        manageGroupButton.setEnabled(false);
        addGroupButton.setCaption(i18N.get("student-data.add-group"));
        addGroupButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addGroupButton.addClickListener(event -> {
            Set<StudentGroupInfo> selected = groups.getSelectedItems();
            Set<StudentGroup> choosableGroups = studentInfoService.getStudentGroups();
            choosableGroups.removeAll(studentInfoService.getTeacherGroups(currentUser.getName()));
            addTeacherGroupsWindow.show(new AddTeacherGroupsWindow.AddTeacherGroupsWindowSettings(choosableGroups, studentGroups -> {
                studentGroups.forEach(group -> studentInfoService.addGroupToTeacher(currentUser.getName(), group));

                refreshTeacherGroups(selected);
            }));
        });

        removeGroupButton.setEnabled(false);
        removeGroupButton.setCaption(i18N.get("student-data.remove-group"));
        removeGroupButton.setStyleName(EkoLabTheme.BUTTON_DANGER);
        removeGroupButton.addClickListener(event -> {
            Set<StudentGroupInfo> selected = groups.getSelectedItems();
            if (!selected.isEmpty()) {
                selected.forEach(s -> studentInfoService.removeGroupFromTeacher(currentUser.getName(), s.getName()));
                refreshTeacherGroups(selected);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshTeacherGroups(null);
        UserProfile userProfile = userLabService.getUserProfile(currentUser.getName());

        userInitialsLabel.setValue(i18N.get("teacher-group-manage.initials", userProfile.getUserInfo().getLastName(),
                userProfile.getUserInfo().getFirstName(), userProfile.getUserInfo().getMiddleName()));
    }

    private void refreshTeacherGroups(Set<StudentGroupInfo> selected) {
        groups.setItems(studentInfoService.getTeacherGroupsInfo(currentUser.getName()));
        if (selected == null) {
            removeGroupButton.setEnabled(false);
        } else {
            selected.forEach(groups::select);
            removeGroupButton.setEnabled(true);
        }
    }

    private void manageGroup(StudentGroupInfo group) {
        navigator.redirectToView(GroupManagingView.NAME + '#' + GroupManagingView.GROUP_NAME  + '=' + group.getName());
    }
}
