package org.ekolab.client.vaadin.server.ui.view;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import org.apache.batik.util.CSSConstants;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.client.vaadin.server.ui.windows.NewStudentWindow;
import org.ekolab.client.vaadin.server.ui.windows.StudentDataWindowSettings;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.model.UserLabStatistics;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringView(name = GroupManagingView.NAME)
public class GroupManagingView extends GridLayout implements View {
    public static final String NAME = "group-manage";

    public static final String GROUP_NAME = "name";

    private final UserLabService userLabService;

    private final EkoLabNavigator navigator;

    private final NewStudentWindow newStudentWindow;

    private final UserInfoService userInfoService;

    private final StudentInfoService studentInfoService;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Grid<UserProfile> groupMembers = new Grid<>();
    private final Button backToGroups = new Button(VaadinIcons.BACKWARDS);
    private final Button newStudentButton = new Button(VaadinIcons.PLUS_CIRCLE);
    private final Button removeStudentButton = new Button(VaadinIcons.MINUS);
    private final Button printUserLogins = new Button("Print users", VaadinIcons.PRINT);
    private final HorizontalLayout buttons = new HorizontalLayout(printUserLogins, newStudentButton, removeStudentButton);

    // ---------------------------- Данные экземпляра --------------------
    private StudentGroup studentGroup;

    @Autowired
    public GroupManagingView(UserLabService userLabService, EkoLabNavigator navigator, NewStudentWindow newStudentWindow, UserInfoService userInfoService, StudentInfoService studentInfoService, I18N i18N) {
        super(4, 4);
        this.userLabService = userLabService;
        this.navigator = navigator;
        this.newStudentWindow = newStudentWindow;
        this.userInfoService = userInfoService;
        this.studentInfoService = studentInfoService;
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        View.super.init();
        setMargin(true);
        setSpacing(true);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        addComponent(groupMembers, 0, 0, 3, 2);
        addComponent(backToGroups, 0, 3, 0, 3);
        addComponent(buttons, 2, 3, 3, 3);

        newStudentButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        removeStudentButton.setStyleName(EkoLabTheme.BUTTON_DANGER);

        backToGroups.addClickListener( event -> navigator.navigateTo(TeacherGroupsManagingView.NAME));
        backToGroups.setCaption(i18N.get("group-manage.group-members.back-to-groups"));
        backToGroups.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        setComponentAlignment(backToGroups, Alignment.MIDDLE_LEFT);
        setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

        groupMembers.setSizeFull();

        groupMembers.setBodyRowHeight(80.0);
        HeaderRow topHeader = groupMembers.prependHeaderRow();
        Grid.Column<UserProfile, String> initials = groupMembers.addColumn(userProfile ->
                userProfile.getUserInfo().getLastName() + ' ' +
                        userProfile.getUserInfo().getFirstName() + '\n' +
                        userProfile.getUserInfo().getMiddleName()
        ).setCaption(i18N.get("group-manage.group-members.student"));

        addLabColumns(1, i18N.get("group-manage.group-members.lab-1"), topHeader);
        addLabColumns(2, i18N.get("group-manage.group-members.lab-2"), topHeader);
        addLabColumns(3, i18N.get("group-manage.group-members.lab-3"), topHeader);

        groupMembers.addSelectionListener(event -> {
            if (event.getAllSelectedItems().isEmpty()) {
                removeStudentButton.setEnabled(false);
            } else {
                removeStudentButton.setEnabled(true);
            }
        });

        newStudentButton.setCaption(i18N.get("group-manage.group-members.add-student"));
        newStudentButton.addClickListener(event -> {
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setGroup(UserGroup.STUDENT);

            StudentInfo newStudentInfo = new StudentInfo();
            newStudentInfo.setGroup(studentGroup);
            newStudentWindow.show(new StudentDataWindowSettings(newUserInfo, userInfo -> {
                Set<UserProfile> selected = groupMembers.getSelectedItems();
                refreshItems();
                selected.forEach(s -> groupMembers.getSelectionModel().select(s));
            }, newStudentInfo));
        });

        removeStudentButton.setCaption(i18N.get("group-manage.group-members.remove-student"));
        removeStudentButton.setEnabled(false);
        removeStudentButton.addClickListener(event ->
                groupMembers.getSelectedItems().forEach(userProfile -> userInfoService.deleteUser(userProfile.getUserInfo().getLogin())));

        groupMembers.setSortOrder(Collections.singletonList(new GridSortOrder<>(initials, SortDirection.ASCENDING)));

        printUserLogins.setCaption(i18N.get("admin-manage.users.print-table"));
        printUserLogins.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        new BrowserWindowOpener(new DownloadStreamResource(
                () -> {
                    Collection<UserProfile> userProfiles = groupMembers.getSelectedItems();
                    if (userProfiles.isEmpty()) {
                        userProfiles = ((ListDataProvider<UserProfile>) groupMembers.getDataProvider()).getItems();
                    }

                    return userInfoService.printUsersData(userProfiles.stream().map(UserProfile::getUserInfo), UI.getCurrent().getLocale());
                },
                "users.pdf"
        )).extend(printUserLogins);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String group = event.getParameterMap().get(GROUP_NAME);
        studentGroup = studentInfoService.getStudentGroupByName(group);
        refreshItems();
    }

    private void refreshItems() {
        Set<UserProfile> userProfiles = userLabService.getUserProfiles(studentGroup.getName());

        //todo
        UserProfile p = new UserProfile();
        p.setUserInfo(new UserInfo());
        p.getUserInfo().setFirstName("admin");
        p.getUserInfo().setMiddleName("admin");
        p.getUserInfo().setLastName("admin");
        p.setAllowedLabs(ImmutableSet.of(1, 2));

        List<UserLabStatistics> labStatistics = new ArrayList<>();
        UserLabStatistics s = new UserLabStatistics();
        s.setTryCount(8);
        s.setLabNumber(1);
        s.setMark(4);
        s.setPointCount(80);
        labStatistics.add(s);
        p.setStatistics(labStatistics);

        userProfiles.add(p);
        groupMembers.setItems(userProfiles);

    }

    private void addLabColumns(int labNumber, String caption, HeaderRow topHeader) {
        Grid.Column<UserProfile, VerticalLayout> execution = groupMembers.addComponentColumn(userProfile ->
        {
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Button button = new Button();
            Button.ClickListener listener = event -> {
                Set<Integer> allowedLabs = Sets.newHashSet(userProfile.getAllowedLabs());
                if (allowedLabs.contains(labNumber)) {
                    studentInfoService.changeLabAllowance(userProfile.getUserInfo().getLogin(),
                            false, labNumber);
                    allowedLabs.remove(labNumber);
                    setAllowButtonStyles(button, false);
                } else {
                    studentInfoService.changeLabAllowance(userProfile.getUserInfo().getLogin(),
                            true, labNumber);
                    allowedLabs.add(labNumber);
                    setAllowButtonStyles(button, true);
                }
                userProfile.setAllowedLabs(allowedLabs);
            };
            button.addClickListener(listener);

            setAllowButtonStyles(button, userProfile.getAllowedLabs().contains(labNumber));
            layout.addComponent(button);
            return layout;
        }).setCaption(i18N.get("group-manage.group-members.execution"));

        Grid.Column<UserProfile, VerticalLayout> defence = groupMembers.addComponentColumn(userProfile -> {
            UserLabStatistics s = getUserLabStatistics(labNumber, userProfile);
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(false);
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            VaadinIcons icon = s == null || s.getMark() < 3 ? VaadinIcons.MINUS : VaadinIcons.PLUS;
            layout.addComponent(new Label("<span style=\'color: " + (icon == VaadinIcons.PLUS ? CSSConstants.CSS_GREEN_VALUE : CSSConstants.CSS_RED_VALUE)
                    + " !important;\'> " + icon.getHtml()  + "</span>", ContentMode.HTML));

            boolean isLabAllowed = userProfile.getAllowedLabs().contains(labNumber);

            if (isLabAllowed && s != null) { // проверка допуска
                layout.addComponent(new Label("\n(Попыток " + s.getTryCount() + ')'));
            }
            return layout;
        }).setCaption(i18N.get("group-manage.group-members.defence"));

        Grid.Column<UserProfile, String> mark = groupMembers.addColumn(userProfile -> {
            UserLabStatistics s = getUserLabStatistics(labNumber, userProfile);
            return s == null ? "" : String.format("%d (%d)", s.getMark(),  s.getPointCount());
        }).setCaption(i18N.get("group-manage.group-members.mark"));

        topHeader.join(execution, defence, mark).setText(caption);
    }

    private void setAllowButtonStyles(Button button, boolean labAllowed) {
        if (labAllowed) {
            button.setIcon(VaadinIcons.MINUS_CIRCLE);
            button.setStyleName(EkoLabTheme.BUTTON_DANGER);
            button.setDescription(i18N.get("admin-manage.users.remove-allow"));
        } else {
            button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
            button.setIcon(VaadinIcons.PLUS_CIRCLE);
            button.setDescription(i18N.get("admin-manage.users.allow"));
        }
    }

    private static UserLabStatistics getUserLabStatistics(int labNumber, UserProfile profile) {
        return profile.getStatistics().stream().filter(
                userLabStatistics -> userLabStatistics.getLabNumber() == labNumber).findAny().orElse(null);
    }
}
