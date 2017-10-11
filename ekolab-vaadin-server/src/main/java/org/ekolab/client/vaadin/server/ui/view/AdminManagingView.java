package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.UsersPanel;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = AdminManagingView.NAME)
public class AdminManagingView extends VerticalLayout implements View {
    public static final String NAME = "admin";

    @Autowired
    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    @Autowired
    protected final UsersPanel adminsPanel;
    @Autowired
    protected final UsersPanel teachersPanel;
    @Autowired
    protected final UsersPanel studentsPanel;

    protected final TabSheet userTabs = new TabSheet();

    public AdminManagingView(I18N i18N, UsersPanel adminsPanel, UsersPanel teachersPanel, UsersPanel studentsPanel) {
        this.i18N = i18N;
        this.adminsPanel = adminsPanel;
        this.teachersPanel = teachersPanel;
        this.studentsPanel = studentsPanel;
    }


    @Override
    public void init() throws Exception {
        View.super.init();

        addComponent(userTabs);

        adminsPanel.setUserGroup(UserGroup.ADMIN);
        teachersPanel.setUserGroup(UserGroup.TEACHER);
        studentsPanel.setUserGroup(UserGroup.STUDENT);

        userTabs.addTab(adminsPanel, i18N.get(UserGroup.class.getSimpleName() + '.' + UserGroup.ADMIN.name()));
        userTabs.addTab(teachersPanel, i18N.get(UserGroup.class.getSimpleName() + '.' + UserGroup.TEACHER.name()));
        userTabs.addTab(studentsPanel, i18N.get(UserGroup.class.getSimpleName() + '.' + UserGroup.STUDENT.name()));
    }
}
