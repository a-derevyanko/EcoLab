package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.LoginView;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = DemoLoginView.NAME)
@Profile(Profiles.MODE.DEV)
public class DemoLoginView extends LoginView {
    @Override
    public void init() throws Exception {
        super.init();
        notification.setDescription("<span>Приложение ЭкоЛаб работает в режиме демонстрации. вводить имя и пароль не требуется, просто нажмите кнопку <b>Войти</b>, чтобы продолжить.</span>");
    }

    @Override
    protected void login() {
        if (username.isEmpty() || password.isEmpty()) {
            username.setValue("admin");
            password.setValue("admin");
        }
        super.login();
    }
}
