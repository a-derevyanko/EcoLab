package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.UserInfoService;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by 777Al on 20.04.2017.
 */
public abstract class NewUserWindow<T extends UserDataWindowSettings> extends UserDataWindow<T> {
    // ---------------------------- Графические компоненты --------------------

    // ------------------------------------ Данные экземпляра -------------------------------------
    private final UserSavedWindow userSavedWindow;

    protected NewUserWindow(I18N i18N, UserInfoService userInfoService, UserSavedWindow userSavedWindow) {
        super(i18N, userInfoService);
        this.userSavedWindow = userSavedWindow;
    }

    @Override
    protected void save() {
        if (checkedBinders().stream().anyMatch(binder -> !binder.isValid())) {
            if (Page.getCurrent() != null) {
                ComponentErrorNotification.show(i18N.get("savable.save-exception-caption"), i18N.get("savable.save-exception"));
            }
        } else {
            var savedUserInfo = saveUserInfo();
            userSavedWindow.show(new UserSavedWindow.UserSavedWindowSettings(savedUserInfo,
                    userInfoService.createDefaultPassword(savedUserInfo.getLogin())));
            super.save();
        }
    }

    protected UserInfo saveUserInfo() {
        return userInfoBinder.getBean().getId() == null ?
                userInfoService.createUserInfo(userInfoBinder.getBean()) :
                userInfoService.updateUserInfo(userInfoBinder.getBean());
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        userGroup.setEnabled(settings.getUserInfo().getGroup() == null);
    }

    protected Collection<Binder<?>> checkedBinders() {
        return Collections.singleton(userInfoBinder);
    }
}
