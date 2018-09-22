package org.ecolab.client.vaadin.server.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.MenuBar;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.AdminManagingView;
import org.ecolab.client.vaadin.server.ui.view.LabChooserView;
import org.ecolab.client.vaadin.server.ui.view.StudentAccountManagingView;
import org.ecolab.client.vaadin.server.ui.view.TeacherGroupsManagingView;
import org.ecolab.client.vaadin.server.ui.windows.SimpleEditUserWindow;
import org.ecolab.client.vaadin.server.ui.windows.UserDataWindowSettings;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.common.Role;
import org.ecolab.server.common.UserInfoUtils;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.security.VaadinSecurity;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * При изменении VIEW меняются кнопки в тулбаре.
 */
@UIScope
@SpringComponent
public class EcoLabMenuBar extends MenuBar implements ViewChangeListener {
    // ---------------------- Данные экземпляра ----------------------------------
    private final I18N i18N;

    private final EcoLabNavigator navigator;

    private final VaadinSecurity vaadinSecurity;

    private final UserInfoService userDetailsManager;

    private final SimpleEditUserWindow userDataWindow;

    // ---------------------- Графические компоненты -----------------------------
    private MenuItem exitItem;
    private MenuItem userInfoItem;
    private MenuItem adminManagingItem;
    private MenuItem teacherManagingItem;
    private MenuItem studentManagingItem;
    private MenuItem labChooserItem;

    @Autowired
    public EcoLabMenuBar(I18N i18N, EcoLabNavigator navigator, VaadinSecurity vaadinSecurity, UserInfoService userDetailsManager,
                         SimpleEditUserWindow userDataWindow) {
        this.i18N = i18N;
        this.navigator = navigator;
        this.vaadinSecurity = vaadinSecurity;
        this.userDetailsManager = userDetailsManager;
        this.userDataWindow = userDataWindow;
    }

    @PostConstruct
    protected void init() {
        setWidth(100.0F, Unit.PERCENTAGE);
        setHeight(35.0F, Unit.PIXELS);
        setStyleName(EcoLabTheme.MENUBAR_BORDERLESS);
        exitItem = addItem(i18N.get("menubar.exit"), VaadinIcons.SIGN_OUT, (Command) selectedItem -> vaadinSecurity.logout());
        userInfoItem = addItem("", VaadinIcons.USER, (Command) selectedItem -> {
            userDataWindow.show(new UserDataWindowSettings(getUI().getCurrentUserInfo(), newUserInfo -> {
                getUI().setCurrentUserInfo(newUserInfo);
                updateUserInfoItem();
            }));
        });
        adminManagingItem = addItem(i18N.get("menubar.admin"), VaadinIcons.TOOLS, (Command) selectedItem -> navigator.redirectToView(AdminManagingView.NAME));
        studentManagingItem = addItem(i18N.get("menubar.student-account-managing"), VaadinIcons.USER_CARD, selectedItem -> navigator.redirectToView(StudentAccountManagingView.NAME));
        teacherManagingItem = addItem(i18N.get("menubar.teacher-account-managing"), VaadinIcons.USER_CARD, selectedItem -> navigator.redirectToView(TeacherGroupsManagingView.NAME));
        labChooserItem = addItem(i18N.get("menubar.labChooser"), VaadinIcons.ACADEMY_CAP, (Command) selectedItem -> navigator.redirectToView(LabChooserView.NAME));

        adminManagingItem.setVisible(false);
        teacherManagingItem.setVisible(false);
        studentManagingItem.setVisible(false);
    }

    // ------------------------------ Реализация обработчиков событий ----------------
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        if (vaadinSecurity.isAuthenticated()) {
            if (VaadinUI.getCurrent().getCurrentUserInfo() == null) {
                Authentication authentication = vaadinSecurity.getAuthentication();
                getUI().setCurrentUserInfo(userDetailsManager.getUserInfo(CurrentUser.getId()));
                updateUserInfoItem();

                Set<String> roles = UserInfoUtils.getRoles(authentication);

                if (roles.contains(Role.ADMIN)) {
                    adminManagingItem.setVisible(true);
                } else if (roles.contains(Role.TEACHER)) {
                    teacherManagingItem.setVisible(true);
                } else if (roles.contains(Role.STUDENT)) {
                    studentManagingItem.setVisible(true);
                }
            }
        } else {
            setVisible(false);
            getUI().setCurrentUserInfo(null);
            getUI().setCurrentStudentInfo(null);
        }
        return true;
    }

    @Override
    public VaadinUI getUI() {
        return VaadinUI.getCurrent();
    }

    private void updateUserInfoItem() {
        UserInfo userInfo = getUI().getCurrentUserInfo();
        userInfoItem.setText(UserInfoUtils.getShortInitials(userInfo));
    }
}
