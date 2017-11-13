package org.ekolab.server.dao.api.content;

import java.time.LocalDateTime;
import java.util.Collection;

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
    Collection<Integer> getCompletedLabs(String userName);

    /**
     * Устанавливает признак пройденного теста для лабораторной
     * @param userName имя пользователя, прошедшего тест
     * @param labNumber номер лабораторной
     */
    void setTestCompleted(String userName, int labNumber);

    /**
     * Удаление всех лабораторных, которые были сохранены раньше, чем указанная дата
     * @param lastSaveDate дата, раньше которой удаляются все лабораторные
     * @return количество удалённых лабораторных
     */
    int removeAllOldLabs(LocalDateTime lastSaveDate);
}
