package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.server.service.api.UserInfoService;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class SimpleNewUserWindow extends NewUserWindow<UserDataWindowSettings> {
    public SimpleNewUserWindow(I18N i18N, UserInfoService userInfoService, UserSavedWindow userSavedWindow) {
        super(i18N, userInfoService, userSavedWindow);
    }
}
