package org.ekolab.server.service.api;

import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserInfo;

import java.io.Serializable;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface StudentInfoService extends Serializable {
    /**
     * Возвращает признак того, что аутентифицированный пользователь является студентом
     * @param userInfo аутентифицированный пользователь
     * @return признак того, что аутентифицированный пользователь является студентом
     */
    boolean isStudent(UserInfo userInfo);

    /**
     * Возвращает данные студента
     * @param userName логин студента
     * @return данные студента
     */
    StudentInfo getStudentInfo(String userName);

    /**
     * Изменяет данные студента
     * @param userInfo данные пользователя
     * @param studentInfo данные студента
     */
    void updateStudentInfo(UserInfo userInfo, StudentInfo studentInfo);

    /**
     * Создаёт нового студента
     * @param userInfo данные пользователя
     * @param studentInfo данные студента
     */
    void createStudentInfo(UserInfo userInfo, StudentInfo studentInfo);

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
