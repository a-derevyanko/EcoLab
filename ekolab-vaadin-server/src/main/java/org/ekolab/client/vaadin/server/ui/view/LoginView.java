package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.ResourceService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = LoginView.NAME)
@Profile(Profiles.MODE.PROD)
public class LoginView extends VerticalLayout implements View {
    public static final String NAME = "login";

    @Autowired
    protected VaadinSharedSecurity vaadinSecurity;

    @Autowired
    protected I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    protected final HorizontalLayout fields = new HorizontalLayout();
    protected final VerticalLayout loginPanel = new VerticalLayout();
    protected final Label welcome = new Label("Welcome");
    protected final Button signin = new Button("Sign In", (ClickListener) event -> login());
    protected final CheckBox rememberMe = new CheckBox("Remember me");
    protected final TextField username = new TextField("Username");
    protected final PasswordField password = new PasswordField("Password");
    protected final Notification notification = new Notification("Welcome to EkoLab");

    @Override
    public void init() {
        View.super.init();
        setSizeFull();
        setCaption(i18N.get("login-view.title"));
        setMargin(false);
        setSpacing(false);
        addStyleName(EkoLabTheme.VIEW_LOGIN); //todo убрать при уходе

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        Responsive.makeResponsive(loginPanel);

        welcome.setValue(i18N.get("login-view.welcome"));
        welcome.setSizeUndefined();
        welcome.addStyleName(EkoLabTheme.LABEL_H4);
        welcome.addStyleName(EkoLabTheme.LABEL_COLORED);

        username.setCaption(i18N.get("login-view.username"));
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(EkoLabTheme.TEXTFIELD_INLINE_ICON);

        password.setCaption(i18N.get("login-view.password"));
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(EkoLabTheme.TEXTFIELD_INLINE_ICON);

        signin.setCaption(i18N.get("login-view.signin"));
        signin.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        rememberMe.setCaption(i18N.get("login-view.remember"));

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_CENTER);
        fields.setSpacing(true);

        loginPanel.addStyleName(EkoLabTheme.PANEL_LOGIN);

        Image logo = ResourceService.getImage(EkoLabTheme.IMAGE_TEXT_LOGO);
        logo.setWidth(30, Unit.EM);
        logo.setHeight(15, Unit.EM);

        loginPanel.addComponent(logo);
        loginPanel.addComponent(fields);
        loginPanel.addComponent(rememberMe);

        //addComponent(logo);
        addComponent(loginPanel);

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
