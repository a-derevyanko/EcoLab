package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.ecolab.client.vaadin.server.dataprovider.UserInfoFilter;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.UIComponent;
import org.ecolab.client.vaadin.server.ui.windows.NewUserWindow;
import org.ecolab.client.vaadin.server.ui.windows.SimpleEditUserWindow;
import org.ecolab.client.vaadin.server.ui.windows.UserDataWindowSettings;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.UserInfoService;

public abstract class BaseUsersPanel<S extends  UserDataWindowSettings, T extends UserInfoFilter> extends VerticalLayout implements UIComponent {

    private final SimpleEditUserWindow editUserWindow;

    private final NewUserWindow<S> newUserWindow;

    protected final UserInfoService userInfoService;

    protected final I18N i18N;

    private final DataProvider<UserInfo, T> userInfoDataProvider;

    /**
     * Фильтр по пользователям
     */
    protected T filter;

    // ---------------------------- Графические компоненты --------------------
    protected final Grid<UserInfo> users = new Grid<>();
    protected final TextField findNameTextField = new TextField();
    protected final TextField findLastNameTextField = new TextField();
    protected final TextField findMiddleNameTextField = new TextField();
    protected final Button find = new Button(VaadinIcons.SEARCH);
    protected final Button clearFind = new Button(VaadinIcons.CLOSE);
    protected final Button addUser = new Button("Add user", VaadinIcons.PLUS_CIRCLE);
    protected final Button removeUser = new Button("Remove user", VaadinIcons.MINUS_CIRCLE);
    protected final Button editUser = new Button("Edit user", VaadinIcons.EDIT);
    protected final Button printUserLogins = new Button("Print users", VaadinIcons.PRINT);
    protected final VerticalLayout buttonsPanel = new VerticalLayout(editUser, addUser, removeUser, printUserLogins);
    protected final HorizontalLayout gridPanel = new HorizontalLayout(buttonsPanel, users);
    protected final HorizontalLayout findPanel = new HorizontalLayout(findLastNameTextField, findNameTextField, findMiddleNameTextField, find, clearFind);

    protected BaseUsersPanel(SimpleEditUserWindow userDataWindow, UserInfoService userInfoService, NewUserWindow<S> newUserWindow, DataProvider<UserInfo, T> userInfoDataProvider, I18N i18N) {
        this.editUserWindow = userDataWindow;
        this.newUserWindow = newUserWindow;
        this.userInfoService = userInfoService;
        this.userInfoDataProvider = userInfoDataProvider;
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        UIComponent.super.init();
        setMargin(true);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponents(findPanel, gridPanel);

        gridPanel.setSizeFull();
        gridPanel.setExpandRatio(buttonsPanel, 1.0F);
        gridPanel.setExpandRatio(users, 3.0F);

        findNameTextField.setPlaceholder(i18N.get("userdata.firstname"));
        findLastNameTextField.setPlaceholder(i18N.get("userdata.lastname"));
        findMiddleNameTextField.setPlaceholder(i18N.get("userdata.middlename"));

        find.setDescription(i18N.get("admin-manage.users.find"));
        find.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        find.addClickListener((Button.ClickListener) event -> {
            searchUsers();
        });

        clearFind.setDescription(i18N.get("admin-manage.users.clear-find"));
        clearFind.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        clearFind.addClickListener((Button.ClickListener) event -> {
            findNameTextField.clear();
            findLastNameTextField.clear();
            findMiddleNameTextField.clear();
            searchUsers();
        });

        findPanel.setSpacing(true);
        findPanel.setMargin(true);

        ConfigurableFilterDataProvider<UserInfo, Void, T> dataProvider = userInfoDataProvider.withConfigurableFilter();
        filter = createFilter();
        dataProvider.setFilter(filter);
        users.setSizeFull();
        users.setSelectionMode(Grid.SelectionMode.SINGLE);

        users.setDataProvider(dataProvider);

        users.addSelectionListener((SelectionListener<UserInfo>) event -> {
            boolean hasSelection = event.getFirstSelectedItem().isPresent();
            editUser.setEnabled(hasSelection);
            removeUser.setEnabled(hasSelection);
        });

        users.addColumn(UserInfo::getLogin, new HtmlRenderer()).setCaption(i18N.get("userdata.login")).setExpandRatio(1);
        users.addColumn(UserInfo::getFirstName, new HtmlRenderer()).setCaption(i18N.get("userdata.firstname")).setExpandRatio(1);
        users.addColumn(UserInfo::getMiddleName, new HtmlRenderer()).setCaption(i18N.get("userdata.middlename")).setExpandRatio(1);
        users.addColumn(UserInfo::getLastName).setCaption(i18N.get("userdata.lastname")).setExpandRatio(1);
        users.addColumn(UserInfo::getNote).setCaption(i18N.get("userdata.note")).setExpandRatio(4);

        addUser.setCaption(i18N.get("admin-manage.users.add-user"));
        addUser.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        addUser.setWidth(300.0F, Unit.PIXELS);
        addUser.addClickListener(event ->
                newUserWindow.show(createSettings(dataProvider)));

        removeUser.setCaption(i18N.get("admin-manage.users.remove-user"));
        removeUser.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        removeUser.setEnabled(false);
        removeUser.setWidth(300.0F, Unit.PIXELS);
        removeUser.addClickListener((Button.ClickListener) event -> {
            users.getSelectedItems().forEach(userInfo -> userInfoService.deleteUser(userInfo.getLogin()));
            dataProvider.refreshAll();
        });

        editUser.setCaption(i18N.get("admin-manage.users.edit-user"));
        editUser.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        editUser.setEnabled(false);
        editUser.setWidth(300.0F, Unit.PIXELS);
        editUser.addClickListener((Button.ClickListener) event -> users.getSelectedItems().
                forEach(userInfo -> editUserWindow.show(createSettingsForEdit(userInfo, dataProvider))));

        printUserLogins.setCaption(i18N.get("admin-manage.users.print-table"));
        printUserLogins.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        printUserLogins.setWidth(300.0F, Unit.PIXELS);

        dataProvider.setFilter(filter);

        new BrowserWindowOpener(new DownloadStreamResource(
                () -> userInfoService.printUsersData(dataProvider.fetch(new Query<>(null)), UI.getCurrent().getLocale()),
                "users.pdf")).extend(printUserLogins);
    }

    protected abstract T createFilter();

    protected abstract S createSettings(ConfigurableFilterDataProvider<UserInfo, Void, T> dataProvider);

    protected abstract S createSettingsForEdit(UserInfo userInfo, ConfigurableFilterDataProvider<UserInfo, Void, T> dataProvider);

    public void setUserGroup(UserGroup userGroup) {
        filter.getUserInfoFilter().setGroup(userGroup);
    }

    protected void searchUsers() {
        filter.getUserInfoFilter().setFirstName(findNameTextField.getValue());
        filter.getUserInfoFilter().setLastName(findLastNameTextField.getValue());
        filter.getUserInfoFilter().setMiddleName(findMiddleNameTextField.getValue());
        users.getDataProvider().refreshAll();
    }
}
