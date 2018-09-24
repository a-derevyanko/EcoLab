package org.ecolab.client.vaadin.server.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.model.UserLabStatistics;
import org.ecolab.server.model.UserProfile;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.service.api.content.LabService;
import org.ecolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;

@SpringView(name = StudentAccountManagingView.NAME)
public class StudentAccountManagingView extends GridLayout implements View {
    public static final String NAME = "student-account";

    private final UserLabService userLabService;

    private final I18N i18N;

    private final List<LabService<?, ?>> labServices;

    // ---------------------------- Графические компоненты --------------------
    private final Label userInitialsLabel = new Label("Surname Firstname Lastname");
    private final Image photo = new Image();
    private final Label averagePointCount = new Label("Average point count:\nX (XX)");
    private final Label studentGroupLabel = new Label("GROUP");
    private final Grid<UserLabStatistics> labStatisticsGrid = new Grid<>();

    @Autowired
    public StudentAccountManagingView(UserLabService userLabService, I18N i18N, List<LabService<?, ?>> labServices) {
        super(4, 5);
        this.userLabService = userLabService;
        this.i18N = i18N;
        this.labServices = labServices;
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
        photo.setSizeFull();
        photo.setHeight(200.0F, Unit.PIXELS);

        labStatisticsGrid.setSizeFull();
        labStatisticsGrid.setRowHeight(40.0);
        labStatisticsGrid.addColumn(UserLabStatistics::getLabNumber).setCaption(i18N.get("profile-view.statistics.lab-number"));
        labStatisticsGrid.addColumn(UserLabStatistics::getTryCount).setCaption(i18N.get("profile-view.statistics.try-count"));
        labStatisticsGrid.addColumn(userLabStatistics -> userLabStatistics.getMark() + " (" + userLabStatistics.getPointCount() + ')').setCaption(i18N.get("profile-view.statistics.mark"));
        labStatisticsGrid.addColumn(UserLabStatistics::getLabDate).setCaption(i18N.get("profile-view.statistics.execution-date")).setRenderer(new LocalDateTimeRenderer());
        labStatisticsGrid.addColumn(UserLabStatistics::getTestDate).setCaption(i18N.get("profile-view.statistics.defend-date")).setRenderer(new LocalDateTimeRenderer());
        labStatisticsGrid.addComponentColumn(userLabStatistics -> {
                @SuppressWarnings("unchecked")
                LabService<LabData<?>, ?> labService = (LabService<LabData<?>, ?>) labServices.stream().filter(s -> s.getLabNumber() == userLabStatistics.getLabNumber()).
                        findFirst().orElseThrow(IllegalStateException::new);

                @SuppressWarnings("unchecked")
                final LabData<?> data = labService.getCompletedLabByUser(CurrentUser.getId());

                if (data != null) {
                    Button button = new Button(VaadinIcons.DOWNLOAD);
                    VerticalLayout layout = new VerticalLayout(button);
                    layout.setSpacing(true);
                    layout.setMargin(false);
                    layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
                    button.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
                    button.setCaptionAsHtml(true);
                    button.setDescription(i18N.get("profile-view.download-report", userLabStatistics.getLabNumber()));

                    @SuppressWarnings("unchecked")
                    FileDownloader fileDownloader = new FileDownloader(
                            new DownloadStreamResource(() -> labService.createReport(data), "report.pdf"));
                    fileDownloader.extend(button);
                    return layout;
                }
            return null;
        }).setCaption(i18N.get("profile-view.statistics.report-download"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        UserProfile userProfile = userLabService.getUserProfile(CurrentUser.getId());

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
