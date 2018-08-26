package org.ecolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import org.ecolab.client.vaadin.server.dataprovider.UserInfoFilter;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.BaseUsersPanel;
import org.ecolab.client.vaadin.server.ui.windows.SimpleEditUserWindow;
import org.ecolab.client.vaadin.server.ui.windows.SimpleNewUserWindow;
import org.ecolab.client.vaadin.server.ui.windows.UserDataWindowSettings;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.UserInfoService;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@PrototypeScope
public class TeachersPanel extends BaseUsersPanel<UserDataWindowSettings, UserInfoFilter> {

    // ---------------------------- Графические компоненты --------------------

    public TeachersPanel(SimpleEditUserWindow userDataWindow, UserInfoService userInfoService, SimpleNewUserWindow newUserWindow, DataProvider<UserInfo, UserInfoFilter> userInfoDataProvider, I18N i18N) {
        super(userDataWindow, userInfoService, newUserWindow, userInfoDataProvider, i18N);
    }


    @Override
    public void init() throws Exception {
        super.init();
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
