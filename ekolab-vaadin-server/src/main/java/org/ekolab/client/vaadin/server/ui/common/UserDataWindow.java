package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class UserDataWindow extends Window {
    // ---------------------------- Графические компоненты --------------------
    private final FormLayout content = new FormLayout();
    private final Button save = new Button("Save", this::save);
    private final Button cancel = new Button("Cancel", event -> close());
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final TextField middleName = new TextField("Middle name");
    private final TextArea note = new TextArea("Note");
    private final ComboBox<UserGroup> userGroup = new ComboBox<>("User group");

    private final Label changePassword = new Label("New password");
    private final PasswordField oldPassword = new PasswordField("Old password");
    private final PasswordField newPassword = new PasswordField("New password");

    private final HorizontalLayout actions = new HorizontalLayout(save, cancel);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    @Autowired
    private I18N i18N;

    @Autowired
    private Binder<UserInfo> userInfoBinder;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ExceptionNotification exceptionNotification;

    private Consumer<UserInfo> userInfoConsumer;

    @PostConstruct
    public void init() {
        setContent(content);
        setCaption(i18N.get("user-data-window.title"));
        save.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
        content.setSizeUndefined();
        setResizable(false);
        content.setMargin(true);

        actions.setSpacing(true);

        content.addComponents(lastName, firstName, middleName, note, userGroup, changePassword, oldPassword, newPassword, actions);
        userInfoBinder.forField(lastName).bind(UserInfo::getLastName, UserInfo::setLastName);
        userInfoBinder.forField(firstName).bind(UserInfo::getFirstName, UserInfo::setFirstName);
        userInfoBinder.forField(middleName).bind(UserInfo::getMiddleName, UserInfo::setMiddleName);
        userInfoBinder.forField(note).bind(UserInfo::getNote, UserInfo::setNote);

        lastName.setCaption(i18N.get("userdata.lastname"));
        firstName.setCaption(i18N.get("userdata.firstname"));
        middleName.setCaption(i18N.get("userdata.middlename"));
        changePassword.setValue(i18N.get("user-data-window.password-changing"));
        oldPassword.setCaption(i18N.get("user-data-window.old-password"));
        newPassword.setCaption(i18N.get("user-data-window.new-password"));
        note.setCaption(i18N.get("userdata.note"));
        userGroup.setCaption(i18N.get("userdata.group"));
        save.setCaption(i18N.get("savable.save"));
        cancel.setCaption(i18N.get("confirm.cancel"));

        lastName.setMaxLength(256);
        firstName.setMaxLength(256);
        middleName.setMaxLength(256);
        oldPassword.setMaxLength(256);
        newPassword.setMaxLength(256);
        note.setMaxLength(256);

        userGroup.setItems(UserGroup.values());
        userGroup.setItemCaptionGenerator((elem) -> i18N.get(elem.getDeclaringClass().getSimpleName().toLowerCase() + '.' + elem.name()));
        userGroup.setTextInputAllowed(false);
        userGroup.setEmptySelectionAllowed(false);
        userInfoBinder.forField(userGroup).bind(UserInfo::getGroup, UserInfo::setGroup);
        center();
    }

    public void save(Button.ClickEvent event) {
        try {
            if (StringUtils.hasText(oldPassword.getValue())) {
                try {
                    userInfoService.changePassword(oldPassword.getValue(), newPassword.getValue());
                } catch (AuthenticationException ex) {
                    ComponentErrorNotification.show(i18N.get("user-data-window.change-password-error"), i18N.get("user-data-window.wrong-old-password"));
                    return;
                }
            }
            UserInfo info = new UserInfo();
            userInfoBinder.writeBean(info);
            userInfoService.saveUserInfo(info);
            userInfoConsumer.accept(info);
            close();
            Notification.show(i18N.get("user-data-window.saved"), Notification.Type.TRAY_NOTIFICATION);
        } catch (ValidationException e) {
            ComponentErrorNotification.show(i18N.get("savable.save-exception"), e.getFieldValidationErrors());
        }
    }

    public void show(UserInfo userInfo, Consumer<UserInfo> consumer) {
        if (!UI.getCurrent().getWindows().contains(this)) {
            oldPassword.clear();
            newPassword.clear();

            userInfoBinder.readBean(userInfo);
            userInfoConsumer = consumer;
            setVisible(true);
            firstName.focus();
            UI.getCurrent().addWindow(this);
        }
    }
}
