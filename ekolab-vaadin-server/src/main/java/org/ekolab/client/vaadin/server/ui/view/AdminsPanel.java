package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import org.ekolab.client.vaadin.server.dataprovider.UserInfoFilter;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.BaseUsersPanel;
import org.ekolab.client.vaadin.server.ui.windows.SimpleEditUserWindow;
import org.ekolab.client.vaadin.server.ui.windows.SimpleNewUserWindow;
import org.ekolab.client.vaadin.server.ui.windows.UserDataWindowSettings;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@PrototypeScope
public class AdminsPanel extends BaseUsersPanel<UserDataWindowSettings, UserInfoFilter> {

    // ---------------------------- Графические компоненты --------------------

    public AdminsPanel(SimpleEditUserWindow userDataWindow, UserInfoService userInfoService, SimpleNewUserWindow newUserWindow, DataProvider<UserInfo, UserInfoFilter> userInfoDataProvider, I18N i18N) {
        super(userDataWindow, userInfoService, newUserWindow, userInfoDataProvider, i18N);
    }

    @Override
    protected UserInfoFilter createFilter() {
        return new UserInfoFilter();
    }

    @Override
    protected UserDataWindowSettings createSettings(ConfigurableFilterDataProvider<UserInfo, Void, UserInfoFilter> dataProvider) {
        return new UserDataWindowSettings(filter.getUserInfoFilter(), userInfo -> {
            dataProvider.refreshAll();
            users.select(userInfo);
        });
    }

    @Override
    protected UserDataWindowSettings createSettingsForEdit(UserInfo userInfo, ConfigurableFilterDataProvider<UserInfo, Void, UserInfoFilter> dataProvider) {
        return new UserDataWindowSettings(userInfo, dataProvider::refreshItem);
    }
}
