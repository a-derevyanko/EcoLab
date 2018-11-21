package org.ecolab.server.service.api;

import org.ecolab.server.dev.LogExecutionTime;
import org.ecolab.server.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

/**
 * Created by 777Al on 22.05.2017.
 */
@Validated
public interface UserInfoService extends UserDetailsManager, GroupManager, UserDetailsService {
    /**
     * Возвращает данные пользователя
     * @param userId идентификатор пользователя
     * @return данные пользователя
     */
    UserInfo getUserInfo(long userId);
    /**
     * Изменяет данные пользователя
     * @param userInfo данные пользователя
     */
    UserInfo updateUserInfo(@Valid @NotNull UserInfo userInfo);

    /**
     * Сброс пароля пользователя
     * @param userName логин пользователя
     */
    void resetPassword(@NotNull String userName);

    /**
     * Создаёт данные пользователя.
     * Заполнять логин необязательно - логином назначается фамилия пользователя.
     * @param userInfo данные пользователя
     */
    UserInfo createUserInfo(@Valid @NotNull UserInfo userInfo);

    @LogExecutionTime(500)
    byte[] printUsersData(@Valid Stream<UserInfo> users);

    String createDefaultPassword(@NotNull String userLogin);
}
