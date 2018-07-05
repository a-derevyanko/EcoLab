package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.components.grid.HeaderRow;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.model.UserLabStatistics;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringView(name = GroupManagingView.NAME)
public class GroupManagingView extends GridLayout implements View {
    public static final String NAME = "group-manage";

    public static final String GROUP_NAME = "name";

    private final UserLabService userLabService;

    private final StudentInfoService studentInfoService;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final Grid<UserProfile> groupMembers = new Grid<>();

    @Autowired
    public GroupManagingView(UserLabService userLabService, StudentInfoService studentInfoService, I18N i18N) {
        super(1, 2);
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

        addComponent(groupMembers, 0, 0, 0, 0);
        groupMembers.setSizeFull();

        HeaderRow topHeader = groupMembers.prependHeaderRow();
        Grid.Column<UserProfile, String> initials = groupMembers.addColumn(userProfile ->
                userProfile.getUserInfo().getLastName() + ' ' +
                        userProfile.getUserInfo().getFirstName() + '\n' +
                        userProfile.getUserInfo().getMiddleName()
        ).setCaption("FIO");

        //topHeader.join(initials).setText("FIO");
        addLabColumns(1, i18N.get("group-manage.group-members.lab-1"), topHeader);
        addLabColumns(2, i18N.get("group-manage.group-members.lab-2"), topHeader);
        addLabColumns(3, i18N.get("group-manage.group-members.lab-3"), topHeader);

        groupMembers.setSortOrder(Collections.singletonList(new GridSortOrder<>(initials, SortDirection.ASCENDING)));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String group = event.getParameterMap().get(GROUP_NAME);
        Set<UserProfile> userProfiles = userLabService.getUserProfiles(group);
        groupMembers.setItems(userProfiles);

        //todo
        UserProfile p = new UserProfile();
        p.setUserInfo(new UserInfo());
        p.getUserInfo().setFirstName("admin");
        p.getUserInfo().setMiddleName("admin");
        p.getUserInfo().setLastName("admin");

        List<UserLabStatistics> labStatistics = new ArrayList<>();
        UserLabStatistics s = new UserLabStatistics();
        s.setTryCount(8);
        s.setLabNumber(1);
        s.setMark(4);
        s.setPointCount(80);
        labStatistics.add(s);
        p.setStatistics(labStatistics);
        groupMembers.setItems(p);
    }

    private void addLabColumns(int labNumber, String caption, HeaderRow topHeader) {
        Grid.Column<UserProfile, Label> execution = groupMembers.addComponentColumn(userProfile ->
                new Label(getUserLabStatistics(labNumber, userProfile) == null ? VaadinIcons.MINUS_CIRCLE.getHtml() :
                        VaadinIcons.PLUS_CIRCLE.getHtml(), ContentMode.HTML)).setCaption(i18N.get("group-manage.group-members.execution"));

        Grid.Column<UserProfile, String> defence = groupMembers.addColumn(userProfile -> {
            UserLabStatistics s = getUserLabStatistics(labNumber, userProfile);
            return s == null ? "" : s.getTryCount() + "/10";
        }).setCaption(i18N.get("group-manage.group-members.defence"));

        Grid.Column<UserProfile, String> mark = groupMembers.addColumn(userProfile -> {
            UserLabStatistics s = getUserLabStatistics(labNumber, userProfile);
            return s == null ? "" : String.format("%d (%d)", s.getMark(),  s.getPointCount());
        }).setCaption(i18N.get("group-manage.group-members.mark"));

        topHeader.join(execution, defence, mark).setText(caption);
    }

    private static UserLabStatistics getUserLabStatistics(int labNumber, UserProfile profile) {
        return profile.getStatistics().stream().filter(
                userLabStatistics -> userLabStatistics.getLabNumber() == labNumber).findAny().orElse(null);
    }
}
