package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.EkoLabVaadinProperties;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.client.vaadin.server.ui.common.UsersPanel;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.common.Role;
import org.ekolab.server.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = AdminManagingView.NAME)
@RolesAllowed(Role.ADMIN)
public class AdminManagingView extends VerticalLayout implements View {
    public static final String NAME = "admin";

    @Autowired
    private final I18N i18N;

    @Autowired
    private final EkoLabVaadinProperties systemProperties;
    // ---------------------------- Графические компоненты --------------------
    private final TextField autoSaveRate = new TextField();

    private final FormLayout properties = new FormLayout(autoSaveRate);

    private final HorizontalLayout systemPropertiesPanel = new HorizontalLayout(properties);

    private final Binder<EkoLabVaadinProperties.Lab> binder = new Binder<>();

    @Autowired
    protected final UsersPanel adminsPanel;
    @Autowired
    protected final UsersPanel teachersPanel;
    @Autowired
    protected final UsersPanel studentsPanel;

    protected final TabSheet userTabs = new TabSheet();

    public AdminManagingView(I18N i18N, EkoLabVaadinProperties systemProperties, UsersPanel adminsPanel, UsersPanel teachersPanel, UsersPanel studentsPanel) {
        this.i18N = i18N;
        this.systemProperties = systemProperties;
        this.adminsPanel = adminsPanel;
        this.teachersPanel = teachersPanel;
        this.studentsPanel = studentsPanel;
    }


    @Override
    public void init() throws Exception {
        View.super.init();

        addComponent(userTabs);

        autoSaveRate.setCaption(i18N.get("autoSaveRate"));

        Converter<String, Integer> converter = UIUtils.getStringConverter(Integer.class, i18N);
        binder.forField(autoSaveRate).withConverter(converter).
                bind(EkoLabVaadinProperties.Lab::getAutoSaveRate, EkoLabVaadinProperties.Lab::setAutoSaveRate);

        adminsPanel.setUserGroup(UserGroup.ADMIN);
        teachersPanel.setUserGroup(UserGroup.TEACHER);
        studentsPanel.setUserGroup(UserGroup.STUDENT);

        userTabs.addTab(adminsPanel, i18N.get(UserGroup.ADMIN));
        userTabs.addTab(teachersPanel, i18N.get(UserGroup.TEACHER));
        userTabs.addTab(studentsPanel, i18N.get(UserGroup.STUDENT));
        userTabs.addTab(systemPropertiesPanel, i18N.get("admin-manage.system.properties"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        binder.readBean(systemProperties.getLab());
    }
}
