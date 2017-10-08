package org.ekolab.server.service.api;

import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface StudentInfoService extends Serializable {
    /**
     * Возвращает данные студента
     * @param userName логин студента
     * @return данные студента
     */
    StudentInfo getStudentInfo(String userName);

    /**
     * Изменяет данные студента
     * @param userInfo данные пользователя
     * @param group группа студента
     * @param number бригада студента
     * @return изменённые данные студента
     */
    StudentInfo updateStudentInfo(UserInfo userInfo, String group, Integer number);

    /**
     * Создаёт нового студента
     * @param userInfo данные пользователя
     * @param group группа студента
     * @param number бригада студента
     * @param teacherName преподаватель
     * @return данные студента
     */
    StudentInfo createStudentInfo(UserInfo userInfo, String group, Integer number, String teacherName);

    /**
     * Создаёт студенческую группу
     * @param name название
     */
    void createStudentGroup(String name);

    /**
     * Создаёт студенческую бригаду
     * @param number номер
     */
    void createStudentTeam(Integer number);

    /**
     * Переименовывает студенческую группу
     * @param name название
     * @param newName новое название
     */
    void renameStudentGroup(String name, String newName);

    /**
     *
     * @param studentLogin
     * @return
     */
    String getStudentTeacher(String studentLogin);

    List<String> getTeamMembers(Integer teamNumber);
}
