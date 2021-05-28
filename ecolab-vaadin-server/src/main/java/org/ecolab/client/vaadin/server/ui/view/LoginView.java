package org.ecolab.client.vaadin.server.ui.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.client.vaadin.server.ui.common.UIUtils;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.server.common.Profiles;
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

    @Autowired
    protected ResourceService resourceService;

    // ---------------------------- Графические компоненты --------------------
    protected final HorizontalLayout fields = new HorizontalLayout();
    protected final VerticalLayout loginPanel = new VerticalLayout();
    protected final Image logo = new Image();
    protected final Label welcome = new Label("Welcome");
    protected final Button signin = new Button("Sign In", (ClickListener) event -> login());
    protected final CheckBox rememberMe = new CheckBox("Remember me");
    protected final TextField username = new TextField("Username");
    protected final PasswordField password = new PasswordField("Password");
    protected Notification currentNotification;

    @Override
    public void init() throws Exception {
        View.super.init();
        setSizeFull();
        setCaption(i18N.get("login-view.title"));
        setMargin(false);
        setSpacing(false);
        addStyleName(EcoLabTheme.VIEW_LOGIN); //todo убрать при уходе

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        Responsive.makeResponsive(loginPanel);

        welcome.setValue(i18N.get("login-view.welcome"));
        welcome.setSizeUndefined();
        welcome.addStyleName(EcoLabTheme.LABEL_H4);
        welcome.addStyleName(EcoLabTheme.LABEL_COLORED);

        username.setCaption(i18N.get("login-view.username"));
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(EcoLabTheme.TEXTFIELD_INLINE_ICON);

        password.setCaption(i18N.get("login-view.password"));
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(EcoLabTheme.TEXTFIELD_INLINE_ICON);

        signin.setCaption(i18N.get("login-view.signin"));
        signin.addStyleName(EcoLabTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        rememberMe.setCaption(i18N.get("login-view.remember"));

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_CENTER);
        fields.setSpacing(true);

        loginPanel.addStyleName(EcoLabTheme.PANEL_LOGIN);

        logo.setSource(resourceService.getImage(EcoLabTheme.IMAGE_TEXT_LOGO));
        logo.setWidth(30, Unit.EM);
        logo.setHeight(15, Unit.EM);

        loginPanel.addComponent(logo);
        loginPanel.addComponent(fields);
        loginPanel.addComponent(rememberMe);

        addComponent(loginPanel);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        if (currentNotification != null) {
            //currentNotification.close(); todo
        }
        currentNotification = UIUtils.showNotification(i18N.get("login-view.ecolab-welcome"), i18N.get("login-view.ecolab-welcome-demo"));
        username.focus();
    }

    protected void login() {
        //currentNotification.close(); todo
        if (username.isEmpty() || password.isEmpty()) {
            currentNotification = UIUtils.showNotification(i18N.get("login-view.ecolab-welcome-login-error"),
                    i18N.get("login-view.ecolab-welcome-login-empty"));
        } else {
            try {
                vaadinSecurity.login(username.getValue(), password.getValue(), !rememberMe.isEmpty());
            } catch (AuthenticationException e) {
                currentNotification = UIUtils.showNotification(i18N.get("login-view.ecolab-welcome-login-error"),
                        i18N.get("login-view.ecolab-welcome-login-data-error"));
            } catch (Exception e) {
                throw new SecurityException(e);
            }
        }
    }

}
