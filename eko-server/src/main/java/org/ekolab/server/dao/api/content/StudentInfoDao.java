package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.StudentTeam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface StudentInfoDao extends Serializable {
    String getStudentGroup(String userName);

    StudentTeam getStudentTeam(String userName);

    List<String> getTeamMembers(Integer teamNumber);

    void updateStudentGroup(String userName, String group);

    void updateStudentTeam(String userName, Integer number);

    void addTeacherToStudent(String studentLogin, String teacherLogin);

    String getStudentTeacher(String studentLogin);

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
}
