package org.ekolab.client.vaadin.server.dataprovider;

import org.ekolab.server.model.UserInfo;

import java.util.Objects;

public class UserInfoFilter {
    private UserInfo userInfoFilter = new UserInfo();

    public UserInfo getUserInfoFilter() {
        return userInfoFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfoFilter)) return false;
        UserInfoFilter that = (UserInfoFilter) o;
        return Objects.equals(userInfoFilter, that.userInfoFilter);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userInfoFilter);
    }
}
