package org.ecolab.client.vaadin.server.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.EcoLabVaadinProperties;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ecolab.client.vaadin.server.ui.common.UIUtils;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.server.common.Role;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.service.api.AdminService;

import javax.annotation.security.RolesAllowed;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = AdminManagingView.NAME)
@RolesAllowed(Role.ADMIN)
public class AdminManagingView extends VerticalLayout implements View {
    public static final String NAME = "admin";

    private final I18N i18N;

    private final AdminService adminService;

    private final EcoLabVaadinProperties systemProperties;

    private final AdminsPanel adminsPanel;

    private final TeachersPanel teachersPanel;

    private final StudentsPanel studentsPanel;

    // ---------------------------- Графические компоненты --------------------
    private final TextField autoSaveRate = new TextField();

    private final Button backUpButton = new Button(VaadinIcons.DOWNLOAD_ALT);

    private final FormLayout properties = new FormLayout(autoSaveRate, backUpButton);

    private final HorizontalLayout systemPropertiesPanel = new HorizontalLayout(properties);

    private final Binder<EcoLabVaadinProperties.Lab> binder = new Binder<>();

    protected final TabSheet userTabs = new TabSheet();

    public AdminManagingView(I18N i18N, AdminService adminService, EcoLabVaadinProperties systemProperties, AdminsPanel adminsPanel, TeachersPanel teachersPanel, StudentsPanel studentsPanel) {
        this.i18N = i18N;
        this.adminService = adminService;
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

        var converter = UIUtils.getStringConverter(Integer.class, i18N);
        binder.forField(autoSaveRate).withConverter(converter).
                bind(EcoLabVaadinProperties.Lab::getAutoSaveRate, EcoLabVaadinProperties.Lab::setAutoSaveRate);

        adminsPanel.setUserGroup(UserGroup.ADMIN);
        teachersPanel.setUserGroup(UserGroup.TEACHER);
        studentsPanel.setUserGroup(UserGroup.STUDENT);

        userTabs.addTab(teachersPanel, i18N.get(UserGroup.TEACHER));
        userTabs.addTab(studentsPanel, i18N.get(UserGroup.STUDENT));
        userTabs.addTab(adminsPanel, i18N.get(UserGroup.ADMIN));
        userTabs.addTab(systemPropertiesPanel, i18N.get("admin-manage.system.properties"));

        backUpButton.setCaption(i18N.get("backUp"));

        var fileDownloader = new FileDownloader(
                new DownloadStreamResource(adminService::getBackup, "backup.zip"));
        fileDownloader.extend(backUpButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        binder.readBean(systemProperties.getLab());
    }
}
