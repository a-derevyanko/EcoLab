package org.ecolab.client.vaadin.server.ui.view;

import com.google.common.collect.Sets;
import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.data.ValueProvider;
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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.client.vaadin.server.ui.windows.ManageStudentWindow;
import org.ecolab.client.vaadin.server.ui.windows.NewStudentWindow;
import org.ecolab.client.vaadin.server.ui.windows.StudentDataWindowSettings;
import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.model.UserLabStatistics;
import org.ecolab.server.model.UserProfile;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.ecolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = GroupManagingView.NAME)
public class GroupManagingView extends HorizontalLayout implements View {
    public static final String NAME = "group-manage";

    public static final String GROUP_NAME = "name";

    private final UserLabService userLabService;

    private final EcoLabNavigator navigator;

    private final NewStudentWindow newStudentWindow;

    private final ManageStudentWindow editStudentWindow;

    private final UserInfoService userInfoService;

    private final StudentInfoService studentInfoService;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Label showLabs = new Label("Show labs: ");
    private final Grid<UserProfile> groupMembers = new Grid<>();
    private final Button backToGroups = new Button(VaadinIcons.BACKWARDS);
    private final Button editStudentButton = new Button(VaadinIcons.EDIT);
    private final Button newStudentButton = new Button(VaadinIcons.PLUS_CIRCLE);
    private final Button removeStudentButton = new Button(VaadinIcons.MINUS);
    private final Button printUserLogins = new Button("Print users", VaadinIcons.PRINT);
    private final HorizontalLayout checkBoxes = new HorizontalLayout(showLabs);
    private final VerticalLayout buttons = new VerticalLayout(printUserLogins, editStudentButton, newStudentButton,
            removeStudentButton, showLabs, checkBoxes, backToGroups);
    private final HorizontalLayout gridWithButtons = new HorizontalLayout(buttons, groupMembers);

    // ---------------------------- Данные экземпляра --------------------
    private StudentInfo studentInfo = new StudentInfo();

    @Autowired
    public GroupManagingView(UserLabService userLabService,
                             EcoLabNavigator navigator, NewStudentWindow newStudentWindow,
                             ManageStudentWindow editStudentWindow,
                             UserInfoService userInfoService, StudentInfoService studentInfoService,
                             I18N i18N) {
        this.userLabService = userLabService;
        this.navigator = navigator;
        this.newStudentWindow = newStudentWindow;
        this.editStudentWindow = editStudentWindow;
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

        addComponents(gridWithButtons);

        gridWithButtons.setSizeFull();
        gridWithButtons.setExpandRatio(buttons, 1.0F);
        gridWithButtons.setExpandRatio(groupMembers, 3.0F);

        showLabs.setStyleName(EcoLabTheme.LABEL_BOLD);
        showLabs.setValue(i18N.get("group-manage.group-members.show-labs"));
        checkBoxes.setSizeFull();

        editStudentButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        newStudentButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        removeStudentButton.setStyleName(EcoLabTheme.BUTTON_DANGER);

        backToGroups.addClickListener(event -> navigator.navigateTo(TeacherGroupsManagingView.NAME));
        backToGroups.setCaption(i18N.get("group-manage.group-members.back-to-groups"));
        backToGroups.setStyleName(EcoLabTheme.BUTTON_PRIMARY);

        printUserLogins.setWidth(300.0F, Unit.PIXELS);
        editStudentButton.setWidth(300.0F, Unit.PIXELS);
        newStudentButton.setWidth(300.0F, Unit.PIXELS);
        removeStudentButton.setWidth(300.0F, Unit.PIXELS);
        backToGroups.setWidth(300.0F, Unit.PIXELS);

        gridWithButtons.setSizeFull();
        groupMembers.setSizeFull();

        HeaderRow topHeader = groupMembers.prependHeaderRow();
        Grid.Column<UserProfile, String> initials = groupMembers.addColumn(userProfile ->
                userProfile.getUserInfo().getLastName() + ' ' +
                        userProfile.getUserInfo().getFirstName() + '\n' +
                        userProfile.getUserInfo().getMiddleName()
        ).setCaption(i18N.get("group-manage.group-members.student"));

        groupMembers.addComponentColumn(new ValueProvider<UserProfile, Component>() {
            @Override
            public Component apply(UserProfile userProfile) {
                return null;
            }
        }).setCaption(i18N.get("group-manage.group-members.student"));

        Grid.Column<UserProfile, String> team = groupMembers.addColumn(userProfile -> null ==
                userProfile.getStudentInfo().getTeam() ? i18N.get("group-manage.group-members.no-team") :
                userProfile.getStudentInfo().getTeam().getName()
        ).setCaption(i18N.get("group-manage.group-members.team"));

        Grid.Column<UserProfile, String> average = groupMembers.addColumn(s -> String.format("%d (%d)",
                s.getAverageMark(), s.getAveragePointCount())).setCaption(i18N.get("group-manage.group-members.average"));

        addLabColumns(1, i18N.get("group-manage.group-members.lab-1"), topHeader);
        addLabColumns(2, i18N.get("group-manage.group-members.lab-2"), topHeader);
        addLabColumns(3, i18N.get("group-manage.group-members.lab-3"), topHeader);

        groupMembers.addSelectionListener(event -> {
            if (event.getAllSelectedItems().isEmpty()) {
                editStudentButton.setEnabled(false);
                removeStudentButton.setEnabled(false);
            } else {
                editStudentButton.setEnabled(true);
                removeStudentButton.setEnabled(true);
            }
        });

        groupMembers.addItemClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                editUser(event.getItem());
            }
        });

        editStudentButton.setCaption(i18N.get("group-manage.group-members.edit-student"));
        editStudentButton.setEnabled(false);
        editStudentButton.addClickListener(event -> {
            editUser(groupMembers.getSelectedItems().iterator().next());
        });

        newStudentButton.setCaption(i18N.get("group-manage.group-members.add-student"));
        newStudentButton.addClickListener(event -> {
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setGroup(UserGroup.STUDENT);

            newStudentWindow.show(new StudentDataWindowSettings(newUserInfo, userInfo -> {
                Set<UserProfile> selected = groupMembers.getSelectedItems();
                refreshItems();
                selected.forEach(s -> groupMembers.getSelectionModel().select(s));
            }, studentInfo));
        });

        removeStudentButton.setCaption(i18N.get("group-manage.group-members.remove-student"));
        removeStudentButton.setEnabled(false);
        removeStudentButton.addClickListener(event ->
        {
            groupMembers.getSelectedItems().forEach(userProfile -> userInfoService.deleteUser(userProfile.getUserInfo().getLogin()));
            refreshItems();
        });

        groupMembers.setSortOrder(Collections.singletonList(new GridSortOrder<>(initials, SortDirection.ASCENDING)));

        printUserLogins.setCaption(i18N.get("admin-manage.users.print-table"));
        printUserLogins.setStyleName(EcoLabTheme.BUTTON_PRIMARY);

        new BrowserWindowOpener(new DownloadStreamResource(
                () -> {
                    Collection<UserProfile> userProfiles = groupMembers.getSelectedItems();
                    if (userProfiles.isEmpty()) {
                        userProfiles = ((ListDataProvider<UserProfile>) groupMembers.getDataProvider()).getItems();
                    }

                    return userInfoService.printUsersData(userProfiles.stream().map(UserProfile::getUserInfo));
                },
                "users.pdf"
        )).extend(printUserLogins);
        groupMembers.setFrozenColumnCount(2);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String group = event.getParameterMap().get(GROUP_NAME);
        studentInfo.setGroup(studentInfoService.getStudentGroupByName(group));
        refreshItems();
    }

    private void refreshItems() {
        Set<UserProfile> userProfiles = userLabService.getUserProfiles(studentInfo.getGroup().getName());
        groupMembers.setItems(userProfiles);
    }

    private void addLabColumns(final int labNumber, String caption, HeaderRow topHeader) {
        Grid.Column<UserProfile, VerticalLayout> execution = groupMembers.addComponentColumn(userProfile ->
        {
            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setMargin(false);
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            if (getUserLabStatistics(labNumber, userProfile).getLabDate() == null) {
                OnOffSwitch onOffSwitch = new OnOffSwitch();
                onOffSwitch.setSizeUndefined();
                layout.addComponent(onOffSwitch);
                setAllowLabButtonStyles(onOffSwitch, userProfile.getAllowedLabs().contains(labNumber));
                onOffSwitch.addValueChangeListener(event -> {
                    if (event.isUserOriginated()) {
                        Set<Integer> allowedLabs = Sets.newHashSet(userProfile.getAllowedLabs());
                        if (allowedLabs.contains(labNumber)) {
                            studentInfoService.changeLabAllowance(userProfile.getUserInfo(),
                                    false, labNumber);
                            allowedLabs.remove(labNumber);
                            setAllowLabButtonStyles(onOffSwitch, false);
                        } else {
                            studentInfoService.changeLabAllowance(userProfile.getUserInfo(),
                                    true, labNumber);
                            allowedLabs.add(labNumber);
                            setAllowLabButtonStyles(onOffSwitch, true);
                        }
                        userProfile.setAllowedLabs(allowedLabs);
                    }
                });
            } else {
                layout.addComponent(new Label(VaadinIcons.CHECK.getHtml(), ContentMode.HTML));
            }

            return layout;
        }).setCaption(i18N.get("group-manage.group-members.execution"));

        Grid.Column<UserProfile, VerticalLayout> defence = groupMembers.addComponentColumn(userProfile -> {
            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setMargin(false);
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            UserLabStatistics statistics = getUserLabStatistics(labNumber, userProfile);
            if (statistics.getLabDate() != null) {
                if (getUserLabStatistics(labNumber, userProfile).getMark() >= 3) {
                    layout.addComponent(new Label(VaadinIcons.CHECK.getHtml(), ContentMode.HTML));
                } else {
                    OnOffSwitch onOffSwitch = new OnOffSwitch();
                    onOffSwitch.setSizeUndefined();
                    layout.addComponent(onOffSwitch);
                    setAllowDefenceButtonStyles(onOffSwitch, userProfile.getAllowedDefence().contains(labNumber));
                    onOffSwitch.addValueChangeListener(event -> {
                        if (event.isUserOriginated()) {
                            Set<Integer> allowedLabs = Sets.newHashSet(userProfile.getAllowedDefence());
                            if (allowedLabs.contains(labNumber)) {
                                studentInfoService.changeDefenceAllowance(userProfile.getUserInfo(),
                                        false, labNumber);
                                allowedLabs.remove(labNumber);
                                setAllowDefenceButtonStyles(onOffSwitch, false);
                            } else {
                                studentInfoService.changeDefenceAllowance(userProfile.getUserInfo(),
                                        true, labNumber);
                                allowedLabs.add(labNumber);
                                setAllowDefenceButtonStyles(onOffSwitch, true);
                            }
                            userProfile.setAllowedDefence(allowedLabs);
                        }
                    });
                }
            }

            return layout;
        }).setCaption(i18N.get("group-manage.group-members.defence"));

        Grid.Column<UserProfile, String> mark = groupMembers.addColumn(userProfile -> {
            UserLabStatistics s = getUserLabStatistics(labNumber, userProfile);
            return s == null ? "" : String.format("%d (%d)", s.getMark(), s.getPointCount());
        }).setCaption(i18N.get("group-manage.group-members.mark"));

        topHeader.join(execution, defence, mark).setText(caption);

        CheckBox checkBox = new CheckBox("№ " + labNumber);
        checkBox.addValueChangeListener(event -> {
            boolean hide = !event.getValue();
            execution.setHidden(hide);
            defence.setHidden(hide);
            mark.setHidden(hide);
        });
        checkBox.setValue(true);
        checkBoxes.addComponent(checkBox);
    }

    private void setAllowLabButtonStyles(OnOffSwitch onOffSwitch, boolean labAllowed) {
        onOffSwitch.setDescription(labAllowed ? i18N.get("admin-manage.users.remove-allow") :
                i18N.get("admin-manage.users.allow"));
        onOffSwitch.setValue(labAllowed);
    }

    private void setAllowDefenceButtonStyles(OnOffSwitch onOffSwitch, boolean labAllowed) {
        onOffSwitch.setDescription(labAllowed ? i18N.get("admin-manage.users.remove-defence-allow") : i18N.get("admin-manage.users.allow-defence"));
        onOffSwitch.setValue(labAllowed);
    }

    private static UserLabStatistics getUserLabStatistics(int labNumber, UserProfile profile) {
        return profile.getStatistics().stream().filter(
                userLabStatistics -> userLabStatistics.getLabNumber() == labNumber).findAny().orElse(null);
    }

    /**
     * Редактирование пользователя
     *
     * @param user пользователь
     */
    private void editUser(UserProfile user) {
        editStudentWindow.show(new ManageStudentWindow.EditStudentWindowSettings(user.getUserInfo(), studentInfo -> {
            Set<UserProfile> selected = groupMembers.getSelectedItems();
            refreshItems();
            selected.forEach(s -> groupMembers.getSelectionModel().select(s));
        }, user.getStudentInfo(), user.getStatistics()));
    }
}
