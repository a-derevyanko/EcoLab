package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.model.UserLabStatistics;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.ecolab.server.service.api.UserInfoService;
import org.ecolab.server.service.api.content.LabService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class ManageStudentWindow extends EditUserWindow<ManageStudentWindow.EditStudentWindowSettings> {

    private final List<LabService<?, ?>> labServices;

    // ---------------------------- Графические компоненты --------------------
    private final Button resetPassword = new Button(VaadinIcons.PASSWORD);
    private final MenuBar menu = new MenuBar();
    private final MenuBar.MenuItem rootMenuItem = menu.addItem("", VaadinIcons.MENU, null);

    // ------------------------------------ Данные экземпляра -------------------------------------
    protected ManageStudentWindow(I18N i18N, UserInfoService userInfoService, List<LabService<?, ?>> labServices) {
        super(i18N, userInfoService);
        this.labServices = labServices;
    }

    @PostConstruct
    @Override
    public void init() {
        super.init();
        userGroup.setEnabled(false);
        content.addComponent(resetPassword, 7);
        content.addComponent(menu, 8);

        resetPassword.addClickListener(event -> userInfoService.resetPassword(settings.getUserInfo().getLogin()));
        resetPassword.setCaption(i18N.get("group-manage.group-members.actions-reset-password"));

        userGroup.setVisible(false);
        changePassword.setVisible(false);
        oldPassword.setVisible(false);
        newPassword.setVisible(false);

        menu.setCaption(i18N.get("group-manage.group-members.actions"));
        rootMenuItem.setStyleName(EcoLabTheme.BUTTON_QUIET);
        for (var labNumber = 1; labNumber <= 3; labNumber++) {
            var downloadItem = rootMenuItem.addItem(i18N.get("group-manage.group-members.actions-lab-report",
                    labNumber),
                    VaadinIcons.DOWNLOAD, null);
            var finalLabNumber = labNumber;
            new BrowserWindowOpener(new DownloadStreamResource(
                    () -> {
                        var service = getLabService(finalLabNumber);

                        var data = service.getCompletedLabByUser(settings.getUserInfo().getLogin());

                        return service.createReport(data, UI.getCurrent().getLocale());
                    },
                    String.format("report-lab-%s.pdf", labNumber)
            )).extend(downloadItem);
        }
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();

        menu.setVisible(false);

        for (var menuItem : rootMenuItem.getChildren()) {
            menuItem.setVisible(false);
        }

        settings.statistics.stream().filter(userLabStatistics -> userLabStatistics.getTryCount() > 0).
                forEach(userLabStatistics -> {
                    rootMenuItem.getChildren().get(userLabStatistics.getLabNumber()).setVisible(true);
                    menu.setVisible(true);
                });

    }

    @SuppressWarnings("unchecked")
    private <V extends LabVariant, T extends LabData<V>> LabService<T, V> getLabService(int labNumber) {
        return (LabService<T, V>) labServices.stream().
                filter(labService -> labService.getLabNumber() == labNumber).findAny().
                orElseThrow(IllegalStateException::new);
    }

    public static class EditStudentWindowSettings extends StudentDataWindowSettings {
        private final List<UserLabStatistics> statistics;

        public EditStudentWindowSettings(UserInfo userInfo, Consumer<UserInfo> userInfoConsumer,
                                         StudentInfo studentInfo, List<UserLabStatistics> statistics) {
            super(userInfo, userInfoConsumer, studentInfo);
            this.statistics = statistics;
        }
    }
}
