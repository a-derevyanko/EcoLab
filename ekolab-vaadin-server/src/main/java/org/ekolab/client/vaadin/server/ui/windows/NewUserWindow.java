package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class NewUserWindow extends UserDataWindow {
    // ---------------------------- Графические компоненты --------------------

    // ------------------------------------ Данные экземпляра -------------------------------------
    private final UserSavedWindow userSavedWindow;

    public NewUserWindow(I18N i18N, Binder<UserInfo> userInfoBinder, UserInfoService userInfoService, UserSavedWindow userSavedWindow) {
        super(i18N, userInfoBinder, userInfoService);
        this.userSavedWindow = userSavedWindow;
    }

    @Override
    protected void save() {
        UserInfo savedUserInfo = userInfoService.createUserInfo(userInfoBinder.getBean());
        userSavedWindow.show(new UserSavedWindow.UserSavedWindowSettings(savedUserInfo));
        super.save();
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        userGroup.setEnabled(settings.getUserInfo().getGroup() == null);
    }
}
