package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.ekolab.client.vaadin.server.dataprovider.UserInfoDataProvider;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@PrototypeScope
public class UsersPanel extends VerticalLayout implements UIComponent {

    @Autowired
    private final UserDataWindow userDataWindow;

    @Autowired
    private final UserInfoService userInfoService;

    @Autowired
    private final I18N i18N;

    private final UserInfoDataProvider userInfoDataProvider;

    /**
     * Фильтр по пользователям
     */
    private final UserInfo filter = new UserInfo();

    // ---------------------------- Графические компоненты --------------------
    protected final Grid<UserInfo> users = new Grid<>();
    protected final TextField findNameTextField = new TextField();
    protected final TextField findLastNameTextField = new TextField();
    protected final TextField findMiddleNameTextField = new TextField();
    protected final Button find = new Button(VaadinIcons.SEARCH);
    protected final Button clearFind = new Button(VaadinIcons.CLOSE);
    protected final Button addUser = new Button(VaadinIcons.PLUS_CIRCLE);
    protected final Button removeUser = new Button(VaadinIcons.MINUS_CIRCLE);
    protected final Button editUser = new Button(VaadinIcons.EDIT);
    protected final VerticalLayout buttonsPanel = new VerticalLayout(editUser, addUser, removeUser);
    protected final HorizontalLayout gridPanel = new HorizontalLayout(buttonsPanel, users);
    protected final HorizontalLayout findPanel = new HorizontalLayout(findLastNameTextField, findNameTextField, findMiddleNameTextField, find, clearFind);

    public UsersPanel(UserDataWindow userDataWindow, UserInfoService userInfoService, UserInfoDataProvider userInfoDataProvider, I18N i18N) {
        this.userDataWindow = userDataWindow;
        this.userInfoService = userInfoService;
        this.userInfoDataProvider = userInfoDataProvider;
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        addComponents(findPanel, gridPanel);

        findNameTextField.setPlaceholder(i18N.get("userdata.firstname"));
        findLastNameTextField.setPlaceholder(i18N.get("userdata.lastname"));
        findMiddleNameTextField.setPlaceholder(i18N.get("userdata.middlename"));

        findNameTextField.addValueChangeListener((HasValue.ValueChangeListener<String>) event -> enableFindButtons());
        findLastNameTextField.addValueChangeListener((HasValue.ValueChangeListener<String>) event -> enableFindButtons());
        findMiddleNameTextField.addValueChangeListener((HasValue.ValueChangeListener<String>) event -> enableFindButtons());

        find.setDescription(i18N.get("admin-manage.users.find"));
        find.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        find.addClickListener((Button.ClickListener) event -> {
            searchUsers();
            find.setEnabled(false);
        });

        clearFind.setDescription(i18N.get("admin-manage.users.clear-find"));
        clearFind.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        clearFind.addClickListener((Button.ClickListener) event -> {
            findNameTextField.clear();
            findLastNameTextField.clear();
            findMiddleNameTextField.clear();
            if (!find.isEnabled()) { // Если нажимали кнопку "Поиск"
                searchUsers();
            }
            enableFindButtons();
        });

        ConfigurableFilterDataProvider<UserInfo, Void, UserInfo> dataProvider = userInfoDataProvider.withConfigurableFilter();
        dataProvider.setFilter(filter);
        users.setSelectionMode(Grid.SelectionMode.SINGLE);

        users.setDataProvider(dataProvider);

        users.addSelectionListener((SelectionListener<UserInfo>) event -> editUser.setEnabled(event.getFirstSelectedItem().isPresent()));

        users.addColumn(UserInfo::getLogin, new HtmlRenderer()).setCaption(i18N.get("userdata.login")).setExpandRatio(1);
        users.addColumn(UserInfo::getFirstName, new HtmlRenderer()).setCaption(i18N.get("userdata.firstname")).setExpandRatio(1);
        users.addColumn(UserInfo::getMiddleName, new HtmlRenderer()).setCaption(i18N.get("userdata.middlename")).setExpandRatio(1);
        users.addColumn(UserInfo::getLastName).setCaption(i18N.get("userdata.lastname")).setExpandRatio(1);
        users.addColumn(UserInfo::getNote).setCaption(i18N.get("userdata.note")).setExpandRatio(4);

        addUser.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addUser.addClickListener((Button.ClickListener) event -> {
            userDataWindow.show(new UserDataWindow.UserDataWindowSettings(filter, userInfo -> {
                UserInfo savedUserInfo = userInfoService.createUserInfo(userInfo);
                dataProvider.refreshItem(savedUserInfo);
                users.select(savedUserInfo);
            }));
        });

        removeUser.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        removeUser.addClickListener((Button.ClickListener) event -> {
            users.getSelectedItems().forEach(userInfo -> userInfoService.deleteUser(userInfo.getLogin()));
            dataProvider.refreshAll();
        });

        editUser.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        editUser.setEnabled(false);
        editUser.addClickListener((Button.ClickListener) event -> users.getSelectedItems().forEach(userInfo -> userDataWindow.show(new UserDataWindow.UserDataWindowSettings(userInfo, userInfo1 -> {
            UserInfo updatedUserInfo = userInfoService.updateUserInfo(userInfo1);
            dataProvider.refreshItem(updatedUserInfo);
            users.select(updatedUserInfo);
        }))));

        dataProvider.setFilter(filter);
    }

    public void setUserGroup(UserGroup userGroup) {
        filter.setGroup(userGroup);
    }

    private void searchUsers() {
        filter.setFirstName(findNameTextField.getValue());
        filter.setLastName(findLastNameTextField.getValue());
        filter.setMiddleName(findMiddleNameTextField.getValue());
        users.getDataProvider().refreshAll();
    }

    private void enableFindButtons() {
        findNameTextField.isEmpty();
        findLastNameTextField.isEmpty();
        findMiddleNameTextField.isEmpty();

        find.setEnabled(!findNameTextField.isEmpty() ||
                !findLastNameTextField.isEmpty() || !findMiddleNameTextField.isEmpty());
        clearFind.setEnabled(!find.isEnabled());
    }
}
