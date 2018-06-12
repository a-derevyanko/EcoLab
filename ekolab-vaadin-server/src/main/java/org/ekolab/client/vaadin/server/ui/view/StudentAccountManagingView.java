package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.model.UserLabStatistics;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.io.ByteArrayInputStream;

@SpringView(name = StudentAccountManagingView.NAME)
public class StudentAccountManagingView extends GridLayout implements View {
    public static final String NAME = "student-account";

    private final Authentication currentUser;

    private final UserLabService userLabService;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Label userInitialsLabel = new Label("Surname Firstname Lastname");
    private final Image photo = new Image();
    private final Label averagePointCount = new Label("Average point count:\nX (XX)");
    private final Label studentGroupLabel = new Label("GROUP");
    private final Grid<UserLabStatistics> labStatisticsGrid = new Grid<>();
    private final Button lab1ReportButton = new Button(VaadinIcons.DOWNLOAD);
    private final Button lab2ReportButton = new Button(VaadinIcons.DOWNLOAD);
    private final Button lab3ReportButton = new Button(VaadinIcons.DOWNLOAD);

    @Autowired
    public StudentAccountManagingView(Authentication currentUser,
                                      UserLabService userLabService, I18N i18N) {
        super(4, 5);
        this.currentUser = currentUser;
        this.userLabService = userLabService;
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        View.super.init();
        setMargin(false);
        setSpacing(false);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        photo.setSizeFull();
        photo.setWidth(400.0F, Unit.PIXELS);

        labStatisticsGrid.addColumn(UserLabStatistics::getLabNumber).setCaption(i18N.get("profile-view.statistics.lab-number"));
        labStatisticsGrid.addColumn(UserLabStatistics::getTryCount).setCaption(i18N.get("profile-view.statistics.try-count"));
        labStatisticsGrid.addColumn(userLabStatistics -> userLabStatistics.getMark() + " (" + userLabStatistics.getPointCount() + ')').setCaption(i18N.get("profile-view.statistics.mark"));
        labStatisticsGrid.addColumn(UserLabStatistics::getLabDate).setCaption(i18N.get("profile-view.statistics.execution-date"));
        labStatisticsGrid.addColumn(UserLabStatistics::getTestDate).setCaption(i18N.get("profile-view.statistics.defend-date"));

        //addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        UserProfile userProfile = userLabService.getUserProfile(currentUser.getName());

        userInitialsLabel.setValue(userProfile.getUserInfo().getLastName() + ' ' + userProfile.getUserInfo().getFirstName()
                + '\n' + userProfile.getUserInfo().getMiddleName());
        photo.setSource(new StreamResource(() -> new ByteArrayInputStream(userProfile.getPicture()), null));

        averagePointCount.setValue(i18N.get("profile-view.average-point", Math.round(userProfile.getAverageMark()),
                Math.round(userProfile.getAveragePointCount())));

        if (userProfile.getStudentInfo() != null && userProfile.getStudentInfo().getGroup() != null) {
            studentGroupLabel.setValue(i18N.get("profile-view.group", userProfile.getStudentInfo().getGroup().getName()));
        }
        labStatisticsGrid.setItems(userProfile.getStatistics());
    }

}
