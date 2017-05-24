package org.ekolab.server.service.api;

import org.ekolab.server.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface UserInfoService extends UserDetailsManager, GroupManager, UserDetailsService {
    /**
     * Возвращает данные пользователя
     * @param userName логин пользователя
     * @return данные пользователя
     */
    UserInfo getUserInfo(String userName);
    /**
     * Сохраняет данные пользователя
     * @param userInfo данные пользователя
     */
    void saveUserInfo(UserInfo userInfo);
}
