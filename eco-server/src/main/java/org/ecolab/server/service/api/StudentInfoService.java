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
     * @param userId идентификатор пользователя
     * @return данные студента
     */
    StudentInfo getStudentInfo(long userId);

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

    Set<Long> getTeamMembers(String teamNumber, String group);

    Set<Long> getGroupMembers(String group);

    Set<StudentGroup> getTeacherGroups(long teacherId);

    Set<StudentGroupInfo> getTeacherGroupsInfo(long teacherId);

    boolean isGroupExists(String group);

    boolean isTeamExists(StudentGroup group, String team);

    void addGroupToTeacher(long teacherId, StudentGroup group);

    void removeGroupFromTeacher(long teacherId, String group);

    Set<StudentGroup> getStudentGroups();

    Set<StudentGroup> getGroupsAvailableForTeacher();

    StudentGroup getStudentGroupByName(String group);

    Set<StudentTeam> getStudentTeams(String group);

    /**
     * Возвращает номера лабораторных, к которым есть доступ
     * @param userId идентификатор пользователя
     * @return номера лабораторных, к которым есть доступ
     */
    Set<Integer> getAllowedLabs(long userId);

    /**
     * Возвращает номера защит, к которым есть доступ
     * @param userId идентификатор пользователя
     * @return номера лабораторных, к которым есть доступ
     */
    Set<Integer> getAllowedDefence(long userId);

    /**
     * Допустить пользователя к лабораторной
     * @param userId идентификатор пользователя
     * @param labs лабораторные, к которым меняется допуск
     * @param allow допустить/снять допуск
     */
    void changeLabAllowance(long userId, boolean allow, int... labs);

    /**
     * Допустить пользователя к защите лабораторной
     * @param userId идентификатор пользователя
     * @param labs лабораторные, к которым меняется допуск
     * @param allow допустить/снять допуск
     */
    void changeDefenceAllowance(long userId, boolean allow, int... labs);
}
