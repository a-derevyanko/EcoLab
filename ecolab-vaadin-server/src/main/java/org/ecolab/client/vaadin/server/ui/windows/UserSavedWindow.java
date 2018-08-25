package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.server.common.UserInfoUtils;
import org.ecolab.server.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class UserSavedWindow extends BaseEcoLabWindow<UserSavedWindow.UserSavedWindowSettings> {
    // ---------------------------- Графические компоненты --------------------
    private final Label userSaved = new Label("User added");
    private final Label login = new Label("Login");
    private final Label password = new Label("Password");
    private final Button ok = new Button("OK");

    private final VerticalLayout content = new VerticalLayout(userSaved, login, password, ok);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    private final I18N i18N;

    @Autowired
    public UserSavedWindow(I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    public void init() {
        setContent(content);
        setCaption(i18N.get("user-saved-window.user-added"));
        setResizable(false);
        content.setMargin(true);

        login.setEnabled(false);
        login.setCaption(i18N.get("login-view.username"));
        password.setEnabled(false);
        password.setCaption(i18N.get("login-view.password"));

        ok.addClickListener((Button.ClickListener) event -> close());

        center();
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();

        userSaved.setValue(i18N.get("user-saved-window.user-saved", UserInfoUtils.getShortInitials(settings.userInfo)));
        login.setValue(settings.userInfo.getLogin());
        password.setValue(settings.userInfo.getLogin());
        ok.focus();
    }

    public static class UserSavedWindowSettings implements WindowSettings {
        private final UserInfo userInfo;

        public UserSavedWindowSettings(UserInfo userInfo) {
            this.userInfo = userInfo;
        }
    }
}
