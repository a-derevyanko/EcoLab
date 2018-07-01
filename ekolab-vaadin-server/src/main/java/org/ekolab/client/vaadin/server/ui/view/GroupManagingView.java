package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

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
        super(2, 1);
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
        String group = event.getParameterMap().get(GROUP_NAME);
        Set<UserProfile> userProfiles = userLabService.getUserProfiles(group);
        groupMembers.setItems(userProfiles);
    }
}
