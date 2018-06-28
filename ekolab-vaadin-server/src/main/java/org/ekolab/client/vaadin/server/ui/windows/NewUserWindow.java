package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Created by 777Al on 20.04.2017.
 */
public abstract class NewUserWindow<T extends UserDataWindowSettings> extends UserDataWindow<T> {
    // ---------------------------- Графические компоненты --------------------

    // ------------------------------------ Данные экземпляра -------------------------------------
    private final UserSavedWindow userSavedWindow;

    public NewUserWindow(I18N i18N, Binder<UserInfo> userInfoBinder, UserInfoService userInfoService, UserSavedWindow userSavedWindow) {
        super(i18N, userInfoBinder, userInfoService);
        this.userSavedWindow = userSavedWindow;
    }

    @Override
    protected void save() {
        if (checkedBinders().stream().anyMatch(binder -> !binder.isValid())) {
            if (Page.getCurrent() != null) {
                ComponentErrorNotification.show(i18N.get("savable.save-exception-caption"), i18N.get("savable.save-exception"));
            }
        } else {
            UserInfo savedUserInfo = userInfoService.createUserInfo(userInfoBinder.getBean());
            userSavedWindow.show(new UserSavedWindow.UserSavedWindowSettings(savedUserInfo));
            super.save();
        }
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
