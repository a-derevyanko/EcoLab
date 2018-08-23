package org.ekolab.client.vaadin.server.ui.view;

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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
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
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.service.api.SettingsService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringView(name = GroupManagingView.NAME)
public class GroupManagingView extends HorizontalLayout implements View {
    public static final String NAME = "group-manage";

    public static final String GROUP_NAME = "name";

    private final UserLabService userLabService;

    private final List<LabService<?, ?>> labServices;

    private final EkoLabNavigator navigator;

    private final NewStudentWindow newStudentWindow;

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
    private StudentGroup studentGroup;

    @Autowired
    public GroupManagingView(UserLabService userLabService, List<LabService<?, ?>> labServices, EkoLabNavigator navigator, NewStudentWindow newStudentWindow, UserInfoService userInfoService, StudentInfoService studentInfoService, SettingsService settingsService, I18N i18N) {
        this.userLabService = userLabService;
        this.labServices = labServices;
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

        addComponents(gridWithButtons);

        gridWithButtons.setSizeFull();
        gridWithButtons.setExpandRatio(buttons, 1.0F);
        gridWithButtons.setExpandRatio(groupMembers, 3.0F);

        showLabs.setStyleName(EkoLabTheme.LABEL_BOLD);
        showLabs.setValue(i18N.get("group-manage.group-members.show-labs"));
        checkBoxes.setSizeFull();

        editStudentButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        newStudentButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        removeStudentButton.setStyleName(EkoLabTheme.BUTTON_DANGER);

        backToGroups.addClickListener( event -> navigator.navigateTo(TeacherGroupsManagingView.NAME));
        backToGroups.setCaption(i18N.get("group-manage.group-members.back-to-groups"));
        backToGroups.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

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

        Grid.Column<UserProfile, String> team = groupMembers.addColumn(userProfile -> null ==
                userProfile.getStudentInfo().getTeam() ? "" :
                userProfile.getStudentInfo().getTeam().getName()
        ).setCaption(i18N.get("group-manage.group-members.team"));

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
        groupMembers.setFrozenColumnCount(2);
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
       /* UserProfile p = new UserProfile();
        p.setUserInfo(new UserInfo());
        p.getUserInfo().setFirstName("admin");
        p.getUserInfo().setMiddleName("admin");
        p.getUserInfo().setLastName("admin");
        p.setAllowedLabs(ImmutableSet.of(1, 2));

        StudentInfo studentInfo = new StudentInfo();
        p.setStudentInfo(studentInfo);

        List<UserLabStatistics> labStatistics = new ArrayList<>();
        UserLabStatistics s = new UserLabStatistics();
        s.setTryCount(8);
        s.setLabNumber(1);
        s.setMark(4);
        s.setPointCount(80);
        labStatistics.add(s);
        p.setStatistics(labStatistics);

        userProfiles.add(p);*/
        groupMembers.setItems(userProfiles);

    }

    private void addLabColumns(int labNumber, String caption, HeaderRow topHeader) {
        Grid.Column<UserProfile, VerticalLayout> execution = groupMembers.addComponentColumn(userProfile ->
        {
            Button button = new Button();
            VerticalLayout layout = new VerticalLayout(button);
            layout.setSpacing(true);
            layout.setMargin(false);
            layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
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
            button.setSizeUndefined();

            setAllowButtonStyles(button, userProfile.getAllowedLabs().contains(labNumber));
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

        Grid.Column<UserProfile, VerticalLayout> report = groupMembers.addComponentColumn(userProfile -> {
            UserLabStatistics s = getUserLabStatistics(labNumber, userProfile);
            Button button = new Button(VaadinIcons.DOWNLOAD);
            VerticalLayout layout = new VerticalLayout(button);
            layout.setSpacing(true);
            layout.setMargin(false);
            layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
            button.setSizeUndefined();

            if (s.getTryCount() == 0) {
                button.setEnabled(false);
            } else {
                new BrowserWindowOpener(new DownloadStreamResource(
                        () -> {
                            LabService<LabData<?>, ?> service = (LabService<LabData<?>, ?>) labServices.stream().
                                    filter(labService -> labService.getLabNumber() == labNumber).findAny().
                                    orElseThrow(IllegalStateException::new);

                            LabData<?> data = service.getCompletedLabByUser(userProfile.getUserInfo().getLogin());


                            return service.createReport(data, UI.getCurrent().getLocale());
                        },
                        String.format("report-lab-%s.pdf", labNumber)
                )).extend(button);
            }
            return layout;
        }).setCaption(i18N.get("group-manage.group-members.report"));

        topHeader.join(execution, defence, mark, report).setText(caption);

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

    private void setAllowButtonStyles(Button button, boolean labAllowed) {
        if (labAllowed) {
            button.setIcon(VaadinIcons.MINUS_CIRCLE);
            //button.setStyleName(EkoLabTheme.BUTTON_DANGER);
            button.setDescription(i18N.get("admin-manage.users.remove-allow"));
        } else {
            //button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
            button.setIcon(VaadinIcons.PLUS_CIRCLE);
            button.setDescription(i18N.get("admin-manage.users.allow"));
        }
    }

    private static UserLabStatistics getUserLabStatistics(int labNumber, UserProfile profile) {
        return profile.getStatistics().stream().filter(
                userLabStatistics -> userLabStatistics.getLabNumber() == labNumber).findAny().orElse(null);
    }

    /**
     * Редактирование пользователя
     * @param user пользователь
     */
    private void editUser(UserProfile user) {
        newStudentWindow.show(new StudentDataWindowSettings(user.getUserInfo(), studentInfo -> {
            Set<UserProfile> selected = groupMembers.getSelectedItems();
            refreshItems();
            selected.forEach(s -> groupMembers.getSelectionModel().select(s));
        }, user.getStudentInfo()));
    }
}
