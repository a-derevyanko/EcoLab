package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentTeam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface StudentInfoDao extends Serializable {
    StudentGroup getStudentGroup(String userName);

    StudentTeam getStudentTeam(String userName);

    List<String> getTeamMembers(String teamNumber, String group);

    void updateStudentGroup(String userName, StudentGroup group);

    void updateStudentTeam(String userName, StudentTeam studentTeam);

    void addTeacherToStudent(String studentLogin, String teacherLogin);

    String getStudentTeacher(String studentLogin);

    /**
     * Создаёт студенческую группу
     * @param name название
     */
    StudentGroup createStudentGroup(String name);

    /**
     * Создаёт студенческую бригаду
     * @param name номер
     * @param group группа
     */
    StudentTeam createStudentTeam(String name, String group);

    /**
     * Удаляет студенческую группу
     * @param name название
     */
    void removeStudentGroup(String name);

    /**
     * Удаляет студенческую бригаду
     * @param name номер
     */
    void removeStudentTeam(String name, String group);

    /**
     * Изменение студенческой группы
     * @param group группа
     */
    void updateStudentGroup(StudentGroup group);

    List<StudentGroup> getStudentGroups();

    List<StudentTeam> getStudentTeams(String group);
}
