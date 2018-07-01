package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentGroupInfo;
import org.ekolab.server.model.StudentTeam;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by 777Al on 22.05.2017.
 */
public interface StudentInfoDao extends Serializable {
    StudentGroup getStudentGroup(String userName);

    StudentTeam getStudentTeam(String userName);

    Set<String> getTeamMembers(String teamNumber, String group);

    Set<String> getGroupMembers(String group);

    Set<StudentGroup> getTeacherGroups(String teacher);

    Set<StudentGroupInfo> getTeacherGroupsInfo(String teacher);

    boolean isGroupExists(String group);

    boolean isTeamExists(StudentGroup group, String team);

    void addGroupToTeacher(String teacher, StudentGroup group);

    void removeGroupFromTeacher(String teacher, String group);

    void updateStudentGroup(String userName, StudentGroup group);

    void updateStudentTeam(String userName, StudentTeam studentTeam);

    String getGroupTeacher(String group);

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

    Set<StudentGroup> getStudentGroups();

    Set<StudentTeam> getStudentTeams(String group);
}
