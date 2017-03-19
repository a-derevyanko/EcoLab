package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.virtualkeyboard.VirtualKeyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = LoginView.NAME)
public class LoginView extends VerticalLayout implements View {
    public static final String NAME = "login";

    @Autowired
    protected final VaadinSharedSecurity vaadinSecurity;

    @Autowired
    protected final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    protected final HorizontalLayout fields = new HorizontalLayout();
    protected final VerticalLayout loginPanel = new VerticalLayout();
    protected final Label welcome = new Label("Welcome");
    protected final Label title = new Label("QuickTickets Dashboard");
    protected final Button signin = new Button("Sign In", (ClickListener) event -> login());
    protected final CheckBox rememberMe = new CheckBox("Remember me");
    protected final VirtualKeyboard keyboard = new VirtualKeyboard(false);
    protected final TextField username = new TextField("Username");
    protected final PasswordField password = new PasswordField("Password");
    protected final Notification notification = new Notification("Welcome to Dashboard Demo");

    @Autowired
    public LoginView(VaadinSharedSecurity vaadinSecurity, I18N i18N) {
        this.vaadinSecurity = vaadinSecurity;
        this.i18N = i18N;
        setCaption(i18N.get("login-view.title"));
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);

        welcome.setValue(i18N.get("login-view.welcome"));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);

        title.setValue(i18N.get("login-view.title"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_LIGHT);

        username.setCaption(i18N.get("login-view.username"));
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password.setCaption(i18N.get("login-view.password"));
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        keyboard.attachComponent(username);
        keyboard.attachComponent(password);

        signin.setCaption(i18N.get("login-view.signin"));
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        rememberMe.setCaption(i18N.get("login-view.remember"));

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        fields.setSpacing(true);

        loginPanel.addComponent(welcome);
        loginPanel.addComponent(fields);
        loginPanel.addComponent(rememberMe);

        addComponent(title);
        addComponent(loginPanel);
        addComponent(keyboard);

        setExpandRatio(title, 1.0F);
        setExpandRatio(loginPanel, 1.0F);
        setExpandRatio(keyboard, 3.0F);

        notification.setCaption(i18N.get("login-view.ekolab-welcome"));
        notification.setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        notification.show(Page.getCurrent());
        username.focus();
    }

    protected void login() {
        try {
            vaadinSecurity.login(username.getValue(), password.getValue(), !rememberMe.isEmpty());
        } catch (AuthenticationException e) {
            // отобразить понятное исключение
            e.printStackTrace();
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }
}
