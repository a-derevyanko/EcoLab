package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
public abstract class UserDataWindow<T extends UserDataWindowSettings> extends BaseEkoLabWindow<T> {
    // ---------------------------- Графические компоненты --------------------
    protected final FormLayout content = new FormLayout();
    protected final Button save = new Button("Save", event -> save());
    protected final Button cancel = new Button("Cancel", event -> close());
    protected final TextField firstName = new TextField("First name");
    protected final TextField lastName = new TextField("Last name");
    protected final TextField middleName = new TextField("Middle name");
    protected final TextArea note = new TextArea("Note");
    protected final ComboBox<UserGroup> userGroup = new ComboBox<>("User group");


    protected final HorizontalLayout actions = new HorizontalLayout(save, cancel);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    protected final I18N i18N;

    protected final Binder<UserInfo> userInfoBinder;

    protected final UserInfoService userInfoService;

    protected UserDataWindow(I18N i18N, Binder<UserInfo> userInfoBinder, UserInfoService userInfoService) {
        this.i18N = i18N;
        this.userInfoBinder = userInfoBinder;
        this.userInfoService = userInfoService;
    }

    @PostConstruct
    public void init() {
        setContent(content);
        setCaption(i18N.get("user-data-window.title"));
        save.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        content.setSizeUndefined();
        setResizable(false);
        content.setMargin(true);

        actions.setSpacing(true);

        content.addComponents(lastName, firstName, middleName, note, userGroup, actions);
        userGroup.setItems(UserGroup.values());
        userGroup.setItemCaptionGenerator(i18N::get);
        userGroup.setTextInputAllowed(false);
        userGroup.setEmptySelectionAllowed(false);

        userInfoBinder.forField(lastName).bind("lastName");
        userInfoBinder.forField(firstName).bind("firstName");
        userInfoBinder.forField(middleName).bind("middleName");
        userInfoBinder.forField(note).bind("note");
        userInfoBinder.forField(userGroup).bind("group");

        lastName.setCaption(i18N.get("userdata.lastname"));
        firstName.setCaption(i18N.get("userdata.firstname"));
        middleName.setCaption(i18N.get("userdata.middlename"));
        note.setCaption(i18N.get("userdata.note"));
        userGroup.setCaption(i18N.get("userdata.group"));
        save.setCaption(i18N.get("savable.save"));
        cancel.setCaption(i18N.get("confirm.cancel"));

        lastName.setMaxLength(256);
        firstName.setMaxLength(256);
        middleName.setMaxLength(256);
        note.setMaxLength(256);

        center();
    }

    protected void save() {
        settings.getConsumer().accept(userInfoBinder.getBean());
        close();
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();

        userInfoBinder.setBean(settings.getUserInfo());
        lastName.focus();
    }
}
