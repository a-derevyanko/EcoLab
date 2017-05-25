package org.ekolab.server.dao.api.content;

import java.io.Serializable;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface StudentInfoDao extends Serializable {
    String getStudentGroup(String userName);

    String getStudentTeam(String userName);

    void updateStudentGroup(String userName, String group);

    void updateStudentTeam(String userName, String team);

    /**
     * Создаёт студенческую группу
     * @param name название
     */
    void createStudentGroup(String name);

    /**
     * Создаёт студенческую бригаду
     * @param name название
     */
    void createStudentTeam(String name);

    /**
     * Переименовывает студенческую группу
     * @param name название
     * @param newName новое название
     */
    void renameStudentGroup(String name, String newName);

    /**
     * Переименовывает студенческую бригаду
     * @param name название
     * @param newName новое название
     */
    void renameStudentTeam(String name, String newName);
}
