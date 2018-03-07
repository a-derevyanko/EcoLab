package org.ekolab.server.service.api;

import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Created by 777Al on 22.05.2017.
 */
@Validated
public interface UserInfoService extends UserDetailsManager, GroupManager, UserDetailsService {
    /**
     * Возвращает данные пользователя
     * @param userName логин пользователя
     * @return данные пользователя
     */
    UserInfo getUserInfo(@NotNull String userName);
    /**
     * Изменяет данные пользователя
     * @param userInfo данные пользователя
     */
    UserInfo updateUserInfo(@Valid @NotNull UserInfo userInfo);

    /**
     * Создаёт данные пользователя.
     * Заполнять логин необязательно - логином назначается фамилия пользователя.
     * @param userInfo данные пользователя
     */
    @Valid
    UserInfo createUserInfo(@NotNull UserInfo userInfo);

    @LogExecutionTime(500)
    byte[] printUsersData(@Valid Stream<UserInfo> users, Locale locale);
}
