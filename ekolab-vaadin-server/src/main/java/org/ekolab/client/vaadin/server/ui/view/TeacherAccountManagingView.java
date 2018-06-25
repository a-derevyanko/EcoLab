package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Set;

@SpringView(name = TeacherAccountManagingView.NAME)
public class TeacherAccountManagingView extends GridLayout implements View {
    public static final String NAME = "student-account";

    private final Authentication currentUser;

    private final UserLabService userLabService;

    private final StudentInfoService studentInfoService;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Label userInitialsLabel = new Label("Surname Firstname Lastname");
    private final Label todayDate = new Label();
    private final NativeSelect<StudentGroup> groups = new NativeSelect<>();
    private final Button addGroupButton = new Button();
    private final Button removeGroupButton = new Button();
    private final HorizontalLayout buttons = new HorizontalLayout(addGroupButton, removeGroupButton);
    private final VerticalLayout groupsPanel = new VerticalLayout(groups, buttons);
    private final Grid<UserProfile> groupMembers = new Grid<>();

    @Autowired
    public TeacherAccountManagingView(Authentication currentUser,
                                      UserLabService userLabService, StudentInfoService studentInfoService, I18N i18N) {
        super(4, 5);
        this.currentUser = currentUser;
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

        addComponent(userInitialsLabel, 0, 0, 0, 0);
       /* addComponent(photo, 0, 1, 0, 1);
        addComponent(averagePointCount, 0, 3, 0, 3);
        addComponent(studentGroupLabel, 0, 4, 0, 4);

        addComponent(labStatisticsGrid, 1, 1, 3, 2);*/

        groups.addSelectionListener((SingleSelectionListener<StudentGroup>) event -> {
            Set<UserProfile> userProfiles = event.getSelectedItem().isPresent() ?
                    userLabService.getUserProfiles(event.getSelectedItem().orElseThrow(IllegalStateException::new).getName()) :
                    Collections.emptySet();
            groupMembers.setItems(userProfiles);
        });


       /* labStatisticsGrid.setSizeFull();
        labStatisticsGrid.addColumn(UserLabStatistics::getLabNumber).setCaption(i18N.get("profile-view.statistics.lab-number"));
        labStatisticsGrid.addColumn(UserLabStatistics::getTryCount).setCaption(i18N.get("profile-view.statistics.try-count"));
        labStatisticsGrid.addColumn(userLabStatistics -> userLabStatistics.getMark() + " (" + userLabStatistics.getPointCount() + ')').setCaption(i18N.get("profile-view.statistics.mark"));
        labStatisticsGrid.addColumn(UserLabStatistics::getLabDate).setCaption(i18N.get("profile-view.statistics.execution-date")).setRenderer(new LocalDateTimeRenderer());
        labStatisticsGrid.addColumn(UserLabStatistics::getTestDate).setCaption(i18N.get("profile-view.statistics.defend-date"));
        labStatisticsGrid.addComponentColumn(userLabStatistics -> {
            if (userLabStatistics.getTryCount() > 0) {
                @SuppressWarnings("unchecked")
                LabService<LabData<?>, ?> labService = (LabService<LabData<?>, ?>) labServices.stream().filter(s -> s.getLabNumber() == userLabStatistics.getLabNumber()).
                        findFirst().orElseThrow(IllegalStateException::new);

                @SuppressWarnings("unchecked")
                final LabData<?> data = labService.getCompletedLabByUser(currentUser.getName());

                if (data != null) {
                    Button button = new Button(VaadinIcons.DOWNLOAD);
                    button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
                    button.setCaptionAsHtml(true);
                    button.setDescription(i18N.get("profile-view.download-report", userLabStatistics.getLabNumber()));
                    button.setEnabled(false);

                    @SuppressWarnings("unchecked")
                    FileDownloader fileDownloader = new FileDownloader(
                            new DownloadStreamResource(() -> labService.createReport(data, UI.getCurrent().getLocale()), "report.pdf"));
                    fileDownloader.extend(button);
                    button.setEnabled(true);
                    return button;

                }
            }
            return null;
        }).setCaption(i18N.get("profile-view.statistics.report-download"));*/
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        groups.setItems(studentInfoService.getTeacherGroups(currentUser.getName()));

        UserProfile userProfile = userLabService.getUserProfile(currentUser.getName());

        userInitialsLabel.setValue(userProfile.getUserInfo().getLastName() + ' ' + userProfile.getUserInfo().getFirstName()
                + '\n' + userProfile.getUserInfo().getMiddleName());
    }
}
