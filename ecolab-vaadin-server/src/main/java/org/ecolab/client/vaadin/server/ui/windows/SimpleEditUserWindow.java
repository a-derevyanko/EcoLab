package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.server.common.Role;
import org.ecolab.server.service.api.UserInfoService;
import org.springframework.security.core.Authentication;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class SimpleEditUserWindow extends EditUserWindow<UserDataWindowSettings> {
    private final Authentication currentUser;

    public SimpleEditUserWindow(I18N i18N, UserInfoService userInfoService, Authentication currentUser) {
        super(i18N, userInfoService);
        this.currentUser = currentUser;
    }

    @Override
    protected void beforeShow() {
        if (currentUser.getAuthorities().stream().anyMatch(grantedAuthority -> Role.STUDENT.equals(grantedAuthority.getAuthority()))) {
            lastName.setEnabled(false);
            middleName.setEnabled(false);
            firstName.setEnabled(false);
            userGroup.setEnabled(false);
        }
        super.beforeShow();
    }
}
