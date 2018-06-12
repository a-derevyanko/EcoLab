package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.LabMode;
import org.ekolab.server.model.UserLabStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserLabDao {
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
    void setTestCompleted(String userName, int labNumber, int mark, int pointCount);

    /**
     * Возвращает статистику по сдаче лабораторных пользователем
     * @param userName имя пользователя
     * @return статистика
     */
    List<UserLabStatistics> getUserLabStatistics(String userName);
}
