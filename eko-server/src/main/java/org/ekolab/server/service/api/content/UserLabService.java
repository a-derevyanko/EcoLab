package org.ekolab.server.service.api.content;

import java.util.Collection;

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
    Collection<Integer> getCompletedLabs(String userName);

    /**
     * Устанавливает признак пройденного теста для лабораторной
     * @param userName имя пользователя, прошедшего тест
     * @param labNumber номер лабораторной
     */
    Collection<Integer> setTestCompleted(String userName, int labNumber);
}
