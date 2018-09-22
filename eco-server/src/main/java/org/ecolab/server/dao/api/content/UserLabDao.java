package org.ecolab.server.dao.api.content;

import org.ecolab.server.model.LabMode;
import org.ecolab.server.model.UserLabStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserLabDao {
    /**
     * Возвращает тесты, пройденные пользователем
     * @param user идентификатор пользователя
     * @return тесты, пройденные пользователем
     */
    Collection<Integer> getCompletedTests(long user);

    /**
     * Возвращает лабораторные работы, пройденные пользователем
     * @param user идентификатор пользователя
     * @return лабораторные работы, пройденные пользователем
     */
    Map<Integer, LabMode> getCompletedLabs(long user);

    /**
     * Устанавливает признак пройденного теста для лабораторной
     * @param user идентификатор пользователя
     * @param labNumber номер лабораторной
     * @param mark оценка
     * @param pointCount количество баллов
     */
    void setTestCompleted(long user, int labNumber, int mark, int pointCount);

    /**
     * Возвращает статистику по сдаче лабораторных пользователем
     * @param userId идентификатор пользователя
     * @return статистика
     */
    List<UserLabStatistics> getUserLabStatistics(long userId);
}
