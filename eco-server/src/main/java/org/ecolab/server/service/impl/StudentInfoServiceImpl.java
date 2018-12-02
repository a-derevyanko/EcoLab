package org.ecolab.server.service.impl;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.common.UserInfoUtils;
import org.ecolab.server.dao.api.content.StudentInfoDao;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.StudentGroup;
import org.ecolab.server.model.StudentGroupInfo;
import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.StudentTeam;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.ecolab.server.CacheContext.STUDENT_INFO_CACHE;
import static org.ecolab.server.CacheContext.TEAM_MEMBERS_CACHE;

/**
 * Created by 777Al on 24.05.2017.
 */
@Service
public class StudentInfoServiceImpl implements StudentInfoService {
    private final UserInfoService userInfoService;

    private final EcoLabAuditService auditService;

    private final StudentInfoDao studentInfoDao;

    public StudentInfoServiceImpl(UserInfoService userInfoService, EcoLabAuditService auditService, StudentInfoDao studentInfoDao) {
        this.userInfoService = userInfoService;
        this.auditService = auditService;
        this.studentInfoDao = studentInfoDao;
    }

    @Override
    @Cacheable(cacheNames = STUDENT_INFO_CACHE, key = "#userId")
    @Nullable
    public StudentInfo getStudentInfo(long userId) {
        StudentGroup group = studentInfoDao.getStudentGroup(userId);
        if (group == null) {
            return null;
        } else {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setGroup(group);
            studentInfo.setTeam(studentInfoDao.getStudentTeam(userId));
            return studentInfo;
        }
    }

    @Override
    @Transactional
    @CachePut(cacheNames = STUDENT_INFO_CACHE, key = "#userInfo.id")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, allEntries = true, condition =  "#team != null")
    @Deprecated // todo подумать, нужен ли этот метод. кэш для getTeamMembers() плохо будет работаь
    public StudentInfo updateStudentInfo(UserInfo userInfo, StudentGroup group, StudentTeam team) {
        userInfoService.updateUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getId(), group);
        studentInfoDao.updateStudentTeam(userInfo.getId(), team);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = {STUDENT_INFO_CACHE}, key = "#userInfo.id")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, key = "#team.name", condition =  "#team != null")
    @NotNull
    public StudentInfo createStudentInfo(@NotNull UserInfo userInfo, @NotNull StudentGroup group,
                                         StudentTeam team) {
        if (userInfo.getId() == null) {
            userInfoService.createUserInfo(userInfo);
        }
        studentInfoDao.updateStudentGroup(userInfo.getId(), group);
        if (team != null) {
            studentInfoDao.updateStudentTeam(userInfo.getId(), team);
        }
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    public StudentGroup createStudentGroup(String name) {
        return studentInfoDao.createStudentGroup(name);
    }

    @Override
    @Transactional
    public StudentTeam createStudentTeam(String name, String group) {
        return studentInfoDao.createStudentTeam(name, group);
    }

    @Override
    @Transactional
    public void removeStudentGroup(String name) {
        studentInfoDao.removeStudentGroup(name);
    }

    @Override
    @Transactional
    public void removeStudentTeam(String name, String group) {
        studentInfoDao.removeStudentTeam(name, group);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = STUDENT_INFO_CACHE, allEntries = true)
    public void updateStudentGroup(StudentGroup group) {
        studentInfoDao.updateStudentGroup(group);
    }

    @Override
    public UserInfo getGroupTeacher(String group) {
        return userInfoService.getUserInfo(studentInfoDao.getGroupTeacher(group));
    }

    @Override
    public Set<Long> getTeamMembers(String name, String group) {
        return studentInfoDao.getTeamMembers(name, group);
    }

    @Override
    public Set<Long> getGroupMembers(String group) {
        return studentInfoDao.getGroupMembers(group);
    }

    @Override
    public Set<StudentGroup> getTeacherGroups(long teacherId) {
        return studentInfoDao.getTeacherGroups(teacherId);
    }

    @Override
    public Set<StudentGroupInfo> getTeacherGroupsInfo(long teacherId) {
        return studentInfoDao.getTeacherGroupsInfo(teacherId);
    }

    @Override
    public boolean isGroupExists(String group) {
        return studentInfoDao.isGroupExists(group);
    }

    @Override
    public boolean isTeamExists(StudentGroup group, String team) {
        return studentInfoDao.isTeamExists(group, team);
    }

    @Override
    @Transactional
    public void addGroupToTeacher(long teacherId, StudentGroup group) {
        studentInfoDao.addGroupToTeacher(teacherId, group);
    }

    @Override
    @Transactional
    public void removeGroupFromTeacher(long teacherId, String group) {
        studentInfoDao.removeGroupFromTeacher(teacherId, group);
    }

    @Override
    public Set<StudentGroup> getStudentGroups() {
        return studentInfoDao.getStudentGroups();
    }

    @Override
    public Set<StudentGroup> getGroupsAvailableForTeacher() {
        return studentInfoDao.getGroupsAvailableForTeacher();
    }

    @Override
    public StudentGroup getStudentGroupByName(String group) {
        return studentInfoDao.getStudentGroupByName(group);
    }

    @Override
    public Set<StudentTeam> getStudentTeams(String group) {
        return studentInfoDao.getStudentTeams(group);
    }

    @Override
    public Set<Integer> getAllowedLabs(long userId) {
        return studentInfoDao.getAllowedLabs(userId);
    }

    @Override
    public Set<Integer> getAllowedDefence(long userId) {
        return studentInfoDao.getAllowedDefence(userId);
    }

    @Override
    @Transactional
    public void changeLabAllowance(UserInfo user, boolean allow, int... labs) {
        studentInfoDao.changeLabAllowance(user.getId(), allow, labs);

        auditService.log(EcoLabAuditEvent.ofType(allow ? EcoLabAuditEventType.LAB_ALLOWED : EcoLabAuditEventType.LAB_DISALLOWED).
                forUser(user.getId()).
                attribute(EcoLabAuditEventAttribute.CONSUMER_NAME,
                        UserInfoUtils.getShortInitials(user)).
                attribute(EcoLabAuditEventAttribute.LAB_NUMBER, Arrays.stream(labs).mapToObj(String::valueOf).collect(Collectors.joining(", "))));
    }

    @Override
    @Transactional
    public void changeDefenceAllowance(UserInfo user, boolean allow, int... labs) {
        studentInfoDao.changeDefenceAllowance(user.getId(), allow, labs);

        auditService.log(EcoLabAuditEvent.ofType(allow ? EcoLabAuditEventType.LAB_DEFENCE_ALLOWED : EcoLabAuditEventType.LAB_DEFENCE_DISALLOWED).
                forUser(user.getId()).
                attribute(EcoLabAuditEventAttribute.CONSUMER_NAME,
                        UserInfoUtils.getShortInitials(user)).
                attribute(EcoLabAuditEventAttribute.LAB_NUMBER, Arrays.stream(labs).mapToObj(String::valueOf).collect(Collectors.joining(", "))));
    }
}
