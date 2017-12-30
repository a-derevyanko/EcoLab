package org.ekolab.server.service.api;

import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.StudentTeam;
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
     * @param team бригада студента
     * @return изменённые данные студента
     */
    StudentInfo updateStudentInfo(UserInfo userInfo, StudentGroup group, StudentTeam team);

    /**
     * Создаёт нового студента
     * @param userInfo данные пользователя
     * @param group группа студента
     * @param team бригада студента
     * @param teacherName преподаватель
     * @return данные студента
     */
    StudentInfo createStudentInfo(UserInfo userInfo, StudentGroup group, StudentTeam team, String teacherName);

    /**
     * Создаёт студенческую группу
     * @param name название
     */
    StudentGroup createStudentGroup(String name);

    /**
     * Создаёт студенческую бригаду
     * @param number номер
     */
    StudentTeam createStudentTeam(Integer number);

    /**
     * Изменение студенческой группы
     * @param group группа
     */
    void updateStudentGroup(StudentGroup group);

    /**
     *
     * @param studentLogin
     * @return
     */
    String getStudentTeacher(String studentLogin);

    List<String> getTeamMembers(Integer teamNumber);

    List<StudentGroup> getStudentGroups();

    List<StudentTeam> getStudentTeams();
}
