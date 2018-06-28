package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.client.vaadin.server.ui.windows.AddTeacherGroupsWindow;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@SpringView(name = TeacherAccountManagingView.NAME)
public class TeacherAccountManagingView extends GridLayout implements View {
    public static final String NAME = "teacher-account";

    private final Authentication currentUser;

    private final AddTeacherGroupsWindow addTeacherGroupsWindow;

    private final UserLabService userLabService;

    private final StudentInfoService studentInfoService;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Label userInitialsLabel = new Label("Surname Firstname Lastname");
    private final Label todayDate = new Label();
    private final NativeSelect<StudentGroup> groups = new NativeSelect<>(null);
    private final Button addGroupButton = new Button();
    private final Button removeGroupButton = new Button();
    private final HorizontalLayout buttons = new HorizontalLayout(addGroupButton, removeGroupButton);
    private final VerticalLayout groupsPanel = new VerticalLayout(groups, buttons);
    private final Grid<UserProfile> groupMembers = new Grid<>();

    @Autowired
    public TeacherAccountManagingView(Authentication currentUser,
                                      AddTeacherGroupsWindow addTeacherGroupsWindow, UserLabService userLabService, StudentInfoService studentInfoService, I18N i18N) {
        super(2, 1);
        this.currentUser = currentUser;
        this.addTeacherGroupsWindow = addTeacherGroupsWindow;
        this.userLabService = userLabService;
        this.studentInfoService = studentInfoService;
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        View.super.init();
        setMargin(true);
        setSpacing(true);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        addComponent(groupsPanel, 0, 0, 0, 0);

        addGroupButton.addClickListener(event -> {
            Optional<StudentGroup> selected = groups.getSelectedItem();
            Set<StudentGroup> choosableGroups = studentInfoService.getStudentGroups();
            choosableGroups.removeAll(studentInfoService.getTeacherGroups(currentUser.getName()));
            addTeacherGroupsWindow.show(new AddTeacherGroupsWindow.AddTeacherGroupsWindowSettings(choosableGroups, studentGroups -> {
                studentGroups.forEach(group -> studentInfoService.addGroupToTeacher(currentUser.getName(), group));

                refreshTeacherGroups(selected.orElse(null));
            }));
        });

        removeGroupButton.setEnabled(false);
        removeGroupButton.addClickListener(event -> {
            StudentGroup selectedGroup = groups.getSelectedItem().orElseThrow(IllegalArgumentException::new);
            studentInfoService.removeGroupFromTeacher(currentUser.getName(), selectedGroup);
            refreshTeacherGroups(null);
        });
        groups.addSelectionListener(event -> {
            if (event.getSelectedItem().isPresent()) {
                refreshTeacherGroups(event.getSelectedItem().orElseThrow(IllegalStateException::new));
            } else {
                groupMembers.setItems(Collections.emptyList());
                removeGroupButton.setEnabled(false);
            }
        });

        groups.setItemCaptionGenerator(StudentGroup::getName);
        
        groupMembers.addComponentColumn(new ValueProvider<UserProfile, Component>() {
            @Override
            public Component apply(UserProfile userProfile) {
                return null;
            }
        });

        groupMembers.addColumn(new ValueProvider<UserProfile, String>() {
            @Override
            public String apply(UserProfile userProfile) {
                return null;
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshTeacherGroups(null);
        UserProfile userProfile = userLabService.getUserProfile(currentUser.getName());

        userInitialsLabel.setValue(userProfile.getUserInfo().getLastName() + ' ' + userProfile.getUserInfo().getFirstName()
                + '\n' + userProfile.getUserInfo().getMiddleName());
    }

    private void refreshTeacherGroups(StudentGroup selected) {
        groups.setItems(studentInfoService.getTeacherGroups(currentUser.getName()));
        if (selected == null) {
            removeGroupButton.setEnabled(false);
        } else {
            groups.setSelectedItem(selected);
            groupMembers.setItems(userLabService.getUserProfiles(selected));
            removeGroupButton.setEnabled(true);
        }
    }
}
