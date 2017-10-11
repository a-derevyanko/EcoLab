package org.ekolab.client.vaadin.server.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.UserDataWindow;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.AdminManagingView;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.common.Role;
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
    @Autowired
    private I18N i18N;

    @Autowired
    private EkoLabNavigator navigator;

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Autowired
    private UserInfoService userDetailsManager;

    @Autowired
    private UserDataWindow userDataWindow;

    // ---------------------- Данные экземпляра ----------------------------------

    // ---------------------- Графические компоненты -----------------------------
    private final HorizontalLayout leftButtonPanel = new HorizontalLayout();
    private final HorizontalLayout rightButtonPanel = new HorizontalLayout();
    private MenuItem exitItem;
    private MenuItem userInfoItem;
    private MenuItem adminManagingItem;
    private MenuItem teacherManagingItem;
    private MenuItem labChooserItem;

    @PostConstruct
    protected void init() {
        setWidth(100.0F, Unit.PERCENTAGE);
        setHeight(35.0F, Unit.PIXELS);
        setStyleName(EkoLabTheme.MENUBAR_BORDERLESS);
        exitItem = addItem(i18N.get("menubar.exit"), VaadinIcons.SIGN_OUT, (Command) selectedItem -> vaadinSecurity.logout());
        userInfoItem = addItem("", VaadinIcons.USER, (Command) selectedItem -> {
            userDataWindow.show(new UserDataWindow.UserDataWindowSettings(getUI().getCurrentUserInfo(), newUserInfo -> {
                getUI().setCurrentUserInfo(newUserInfo);
                updateUserInfoItem();
            }));
        });
        adminManagingItem = addItem(i18N.get("menubar.admin"), VaadinIcons.TOOLS, (Command) selectedItem -> navigator.redirectToView(AdminManagingView.NAME));
        teacherManagingItem = addItem(i18N.get("menubar.teacher"), VaadinIcons.INSTITUTION, (Command) selectedItem -> vaadinSecurity.logout());
        labChooserItem = addItem(i18N.get("menubar.labChooser"), VaadinIcons.ACADEMY_CAP, (Command) selectedItem -> navigator.redirectToView(LabChooserView.NAME));

        adminManagingItem.setVisible(false);
        teacherManagingItem.setVisible(false);
        labChooserItem.setVisible(false);
    }

    // ------------------------------ Реализация обработчиков событий ----------------
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        if (vaadinSecurity.isAuthenticated()) {
            Authentication authentication = vaadinSecurity.getAuthentication();
            if (userInfoItem.getText().isEmpty()) {
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
                            labChooserItem.setVisible(true);
                            break;
                    }
                }
            }
            ((View) event.getNewView()).placeMenuBarActions(this);
        } else {
            setVisible(false);
            getUI().setCurrentUserInfo(null);
            getUI().setCurrentStudentInfo(null);
        }
    }

    @Override
    public VaadinUI getUI() {
        return (VaadinUI) super.getUI();
    }

    private void updateUserInfoItem() {
        UserInfo userInfo = getUI().getCurrentUserInfo();
        userInfoItem.setText(userInfo.getLastName() + ' ' + userInfo.getFirstName() + ' ' + userInfo.getMiddleName());
    }
}
