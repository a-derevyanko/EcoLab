package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.UserLabStatistics;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.io.ByteArrayInputStream;

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
        addComponent(photo, 0, 1, 0, 1);
        addComponent(averagePointCount, 0, 3, 0, 3);
        addComponent(studentGroupLabel, 0, 4, 0, 4);

        addComponent(labStatisticsGrid, 1, 1, 3, 2);

        groups.addSelectionListener(new SingleSelectionListener<StudentGroup>() {
            @Override
            public void selectionChange(SingleSelectionEvent<StudentGroup> event) {
                studentInfoService.ge
            }
        });


        labStatisticsGrid.setSizeFull();
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
        }).setCaption(i18N.get("profile-view.statistics.report-download"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        groups.setItems(studentInfoService.getStudentGroups());

        UserProfile userProfile = userLabService.getUserProfile(currentUser.getName());

        userInitialsLabel.setValue(userProfile.getUserInfo().getLastName() + ' ' + userProfile.getUserInfo().getFirstName()
                + '\n' + userProfile.getUserInfo().getMiddleName());
        photo.setSource(new StreamResource(() -> new ByteArrayInputStream(userProfile.getPicture()), "profile.svg"));

        averagePointCount.setValue(i18N.get("profile-view.average-point", Math.round(userProfile.getAverageMark()),
                Math.round(userProfile.getAveragePointCount())));

        if (userProfile.getStudentInfo() != null && userProfile.getStudentInfo().getGroup() != null) {
            studentGroupLabel.setVisible(true);
            studentGroupLabel.setValue(i18N.get("profile-view.group", userProfile.getStudentInfo().getGroup().getName()));
        } else {
            studentGroupLabel.setVisible(false);
        }
        labStatisticsGrid.setItems(userProfile.getStatistics());
    }
}
