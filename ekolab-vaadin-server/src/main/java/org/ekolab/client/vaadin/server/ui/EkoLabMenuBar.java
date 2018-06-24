package org.ekolab.client.vaadin.server.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.MenuBar;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.development.DevUtils;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.AdminManagingView;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.client.vaadin.server.ui.view.StudentAccountManagingView;
import org.ekolab.client.vaadin.server.ui.windows.SimpleEditUserWindow;
import org.ekolab.client.vaadin.server.ui.windows.UserDataWindowSettings;
import org.ekolab.server.common.Role;
import org.ekolab.server.common.UserInfoUtils;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.vaadin.spring.security.VaadinSecurity;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * При изменении VIEW меняются кнопки в тулбаре.
 */
@UIScope
@SpringComponent
public class EkoLabMenuBar extends MenuBar implements ViewChangeListener {
    // ---------------------- Данные экземпляра ----------------------------------
    private final I18N i18N;

    private final EkoLabNavigator navigator;

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
    public EkoLabMenuBar(I18N i18N, EkoLabNavigator navigator, VaadinSecurity vaadinSecurity, UserInfoService userDetailsManager,
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
        setStyleName(EkoLabTheme.MENUBAR_BORDERLESS);
        exitItem = addItem(i18N.get("menubar.exit"), VaadinIcons.SIGN_OUT, (Command) selectedItem -> vaadinSecurity.logout());
        userInfoItem = addItem("", VaadinIcons.USER, (Command) selectedItem -> {
            userDataWindow.show(new UserDataWindowSettings(getUI().getCurrentUserInfo(), newUserInfo -> {
                getUI().setCurrentUserInfo(newUserInfo);
                updateUserInfoItem();
            }));
        });
        adminManagingItem = addItem(i18N.get("menubar.admin"), VaadinIcons.TOOLS, (Command) selectedItem -> navigator.redirectToView(AdminManagingView.NAME));
        studentManagingItem = addItem(i18N.get("menubar.student-account-managing"), VaadinIcons.USER_CARD, selectedItem -> navigator.redirectToView(StudentAccountManagingView.NAME));
        teacherManagingItem = addItem(i18N.get("menubar.teacher-account-managing"), VaadinIcons.USER_CARD, selectedItem -> navigator.redirectToView(StudentAccountManagingView.NAME));
        labChooserItem = addItem(i18N.get("menubar.labChooser"), VaadinIcons.ACADEMY_CAP, (Command) selectedItem -> navigator.redirectToView(LabChooserView.NAME));

        adminManagingItem.setVisible(false);
        teacherManagingItem.setVisible(false);
        studentManagingItem.setVisible(false);
        labChooserItem.setVisible(false);

        if (!DevUtils.isProductionVersion()) {
            exitItem.setEnabled(false);
            userInfoItem.setEnabled(false);
        }
    }

    // ------------------------------ Реализация обработчиков событий ----------------
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        if (vaadinSecurity.isAuthenticated()) {
            if (VaadinUI.getCurrent().getCurrentUserInfo() == null) {
                Authentication authentication = vaadinSecurity.getAuthentication();
                getUI().setCurrentUserInfo(userDetailsManager.getUserInfo(authentication.getName()));
                updateUserInfoItem();

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority authority : authorities) {
                    switch (authority.getAuthority()) {
                        case Role.ADMIN:
                            adminManagingItem.setVisible(true);
                            break;
                        case Role.TEACHER:
                            teacherManagingItem.setVisible(true);
                            break;
                        case Role.STUDENT:
                            studentManagingItem.setVisible(true);
                            labChooserItem.setVisible(true);
                            break;
                    }
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
