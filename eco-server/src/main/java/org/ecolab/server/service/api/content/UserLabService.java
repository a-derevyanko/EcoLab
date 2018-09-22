package org.ecolab.server.service.api.content;

import org.ecolab.server.model.LabMode;
import org.ecolab.server.model.UserProfile;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface UserLabService {
    /**
     * Возвращает тесты, пройденные пользователем
     * @return тесты, пройденные пользователем
     */
    Collection<Integer> getCompletedTests();

    /**
     * Возвращает лабораторные работы, пройденные пользователем
     * @return лабораторные работы, пройденные пользователем
     */
    Map<Integer, LabMode> getCompletedLabs();

    /**
     * Устанавливает признак пройденного теста для лабораторной
     * @param labNumber номер лабораторной
     * @param mark оценка
     * @param pointCount количество баллов
     */
    Collection<Integer> setTestCompleted(int labNumber, int mark, int pointCount);

    /**
     * Удаление всех лабораторных, которые были сохранены раньше, чем указанная дата
     * @param lastSaveDate дата, раньше которой удаляются все лабораторные
     * @return количество удалённых лабораторных
     */
    int removeAllOldLabs(LocalDateTime lastSaveDate);

    /**
     * Возвращает данные профиля пользователя
     * @param userId идентификатор пользователя
     * @return профиль
     */
    UserProfile getUserProfile(long userId);

    /**
     * Возвращает данные пользоватей по группе
     * @param group группа
     * @return профили
     */
    Set<UserProfile> getUserProfiles(String group);
}
