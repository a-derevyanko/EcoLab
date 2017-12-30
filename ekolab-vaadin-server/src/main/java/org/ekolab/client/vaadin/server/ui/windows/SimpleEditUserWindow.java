package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class SimpleEditUserWindow extends EditUserWindow<UserDataWindowSettings> {
    public SimpleEditUserWindow(I18N i18N, Binder<UserInfo> userInfoBinder, UserInfoService userInfoService) {
        super(i18N, userInfoBinder, userInfoService);
    }
}
