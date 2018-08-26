package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.UserInfoService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
public abstract class EditUserWindow<T extends UserDataWindowSettings> extends UserDataWindow<T> {
    // ---------------------------- Графические компоненты --------------------
    private final TextField login = new TextField("Login");
    protected final Label changePassword = new Label("New password");
    protected final PasswordField oldPassword = new PasswordField("Old password");
    protected final PasswordField newPassword = new PasswordField("New password");

    protected EditUserWindow(I18N i18N, UserInfoService userInfoService) {
        super(i18N, userInfoService);
    }

    // ------------------------------------ Данные экземпляра -------------------------------------------

    @PostConstruct
    public void init() {
        super.init();
        content.addComponent(login, 0);
        content.addComponent(changePassword, 6);
        content.addComponent(oldPassword, 7);
        content.addComponent(newPassword, 8);

        content.addComponents(login, lastName, firstName, middleName, note, userGroup, changePassword, oldPassword, newPassword, actions);
        userGroup.setItems(UserGroup.values());
        userGroup.setItemCaptionGenerator(i18N::get);
        userGroup.setTextInputAllowed(false);
        userGroup.setEmptySelectionAllowed(false);

        userInfoBinder.forField(login).bind(UserInfo::getLogin, UserInfo::setLogin);

        login.setCaption(i18N.get("userdata.login"));
        changePassword.setValue(i18N.get("user-data-window.password-changing"));
        oldPassword.setCaption(i18N.get("user-data-window.old-password"));
        newPassword.setCaption(i18N.get("user-data-window.new-password"));

        login.setMaxLength(256);
        oldPassword.setMaxLength(256);
        newPassword.setMaxLength(256);

        login.setEnabled(false);
        center();
    }

    @Override
    protected void save() {
        if (StringUtils.hasText(oldPassword.getValue())) {
            try {
                userInfoService.changePassword(oldPassword.getValue(), newPassword.getValue());
            } catch (AuthenticationException ex) {
                ComponentErrorNotification.show(i18N.get("user-data-window.change-password-error"), i18N.get("user-data-window.wrong-old-password"));
                return;
            }
        }
        userInfoService.updateUserInfo(userInfoBinder.getBean());
        super.save();
        Notification.show(i18N.get("user-data-window.saved"), Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    protected void clear() {
        super.clear();
        oldPassword.clear();
        newPassword.clear();
    }
}
