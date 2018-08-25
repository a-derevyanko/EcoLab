package org.ecolab.client.vaadin.server.ui.windows;


import org.apache.commons.lang3.SerializationUtils;
import org.ecolab.server.model.UserInfo;

import java.util.function.Consumer;

public class UserDataWindowSettings implements WindowSettings {
    private final UserInfo userInfo;
    private final Consumer<UserInfo> consumer;

    public UserDataWindowSettings(UserInfo userInfo, Consumer<UserInfo> consumer) {
        this.userInfo = SerializationUtils.clone(userInfo);
        this.consumer = consumer;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Consumer<UserInfo> getConsumer() {
        return consumer;
    }
}