package org.ekolab.server.service.api.content;

import org.ekolab.server.model.LabMode;
import org.ekolab.server.model.UserProfile;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface UserLabService {
    /**
     * Возвращает тесты, пройденные пользователем
     * @param userName имя пользователя
     * @return тесты, пройденные пользователем
     */
    Collection<Integer> getCompletedTests(String userName);

    /**
     * Возвращает лабораторные работы, пройденные пользователем
     * @param userName имя пользователя
     * @return лабораторные работы, пройденные пользователем
     */
    Map<Integer, LabMode> getCompletedLabs(String userName);

    /**
     * Устанавливает признак пройденного теста для лабораторной
     * @param userName имя пользователя, прошедшего тест
     * @param labNumber номер лабораторной
     * @param mark оценка
     * @param pointCount количество баллов
     */
    Collection<Integer> setTestCompleted(String userName, int labNumber, int mark, int pointCount);

    /**
     * Удаление всех лабораторных, которые были сохранены раньше, чем указанная дата
     * @param lastSaveDate дата, раньше которой удаляются все лабораторные
     * @return количество удалённых лабораторных
     */
    int removeAllOldLabs(LocalDateTime lastSaveDate);

    /**
     * Возвращает данные профиля пользователя
     * @param userName имя пользователя
     * @return профиль
     */
    UserProfile getUserProfile(String userName);

    /**
     * Возвращает данные пользоватей по группе
     * @param group группа
     * @return профили
     */
    Set<UserProfile> getUserProfiles(String group);
}
