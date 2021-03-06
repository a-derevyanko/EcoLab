package org.ecolab.server.service.api;

import org.ecolab.server.model.StudentGroup;
import org.ecolab.server.model.StudentGroupInfo;
import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.StudentTeam;
import org.ecolab.server.model.UserInfo;

import java.io.Serializable;
import java.util.Set;

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
     * @return данные студента
     */
    StudentInfo createStudentInfo(UserInfo userInfo, StudentGroup group, StudentTeam team);

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

    /**
     *
     * @param group
     * @return
     */
    UserInfo getGroupTeacher(String group);

    Set<String> getTeamMembers(String teamNumber, String group);

    Set<String> getGroupMembers(String group);

    Set<StudentGroup> getTeacherGroups(String teacher);

    Set<StudentGroupInfo> getTeacherGroupsInfo(String teacher);

    boolean isGroupExists(String group);

    boolean isTeamExists(StudentGroup group, String team);

    void addGroupToTeacher(String teacher, StudentGroup group);

    void removeGroupFromTeacher(String teacher, String group);

    Set<StudentGroup> getStudentGroups();

    Set<StudentGroup> getGroupsAvailableForTeacher();

    StudentGroup getStudentGroupByName(String group);

    Set<StudentTeam> getStudentTeams(String group);

    /**
     * Возвращает номера лабораторных, к которым есть доступ
     * @param userName имя пользователя
     * @return номера лабораторных, к которым есть доступ
     */
    Set<Integer> getAllowedLabs(String userName);

    /**
     * Возвращает номера защит, к которым есть доступ
     * @param userName имя пользователя
     * @return номера лабораторных, к которым есть доступ
     */
    Set<Integer> getAllowedDefence(String userName);

    /**
     * Допустить пользователя к лабораторной
     * @param userName пользователь
     * @param labs лабораторные, к которым меняется допуск
     * @param allow допустить/снять допуск
     */
    void changeLabAllowance(String userName, boolean allow, int... labs);

    /**
     * Допустить пользователя к защите лабораторной
     * @param userName пользователь
     * @param labs лабораторные, к которым меняется допуск
     * @param allow допустить/снять допуск
     */
    void changeDefenceAllowance(String userName, boolean allow, int... labs);
}
