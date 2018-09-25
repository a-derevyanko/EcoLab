package org.ecolab.server.service.impl.content;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.UnhandledException;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.dao.api.content.UserLabDao;
import org.ecolab.server.model.LabMode;
import org.ecolab.server.model.UserLabStatistics;
import org.ecolab.server.model.UserProfile;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.ecolab.server.service.api.content.LabService;
import org.ecolab.server.service.api.content.UserLabService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UserLabServiceImpl implements UserLabService {
    private final UserInfoService userInfoService;
    private final StudentInfoService studentInfoService;

    private final UserLabDao dao;

    private final List<LabService<?, ?>> labServices;

    public UserLabServiceImpl(UserInfoService userInfoService, StudentInfoService studentInfoService, UserLabDao dao, List<LabService<?, ?>> labServices) {
        this.userInfoService = userInfoService;
        this.studentInfoService = studentInfoService;
        this.dao = dao;
        this.labServices = labServices;
    }

    @Override
    @Cacheable(value = "COMPLETED_TEST", key = "T(org.ecolab.server.common.CurrentUser).getId()")
    public Collection<Integer> getCompletedTests() {
        return dao.getCompletedTests(CurrentUser.getId());
    }

    @Override
    public Map<Integer, LabMode> getCompletedLabs() {
        return dao.getCompletedLabs(CurrentUser.getId());
    }

    @Override
    @CachePut(value = "COMPLETED_TEST", key = "T(org.ecolab.server.common.CurrentUser).getId()")
    @Transactional
    public Collection<Integer> setTestCompleted(int labNumber, int mark, int pointCount) {
        dao.setTestCompleted(CurrentUser.getId(), labNumber, mark, pointCount);
        return dao.getCompletedTests(CurrentUser.getId());
    }

    @Override
    @Transactional
    public int removeAllOldLabs(LocalDateTime lastSaveDate) {
        AtomicInteger removedLabs = new AtomicInteger();
        labServices.forEach(labService -> removedLabs.addAndGet(labService.removeOldLabs(lastSaveDate)));
        return removedLabs.get();
    }

    @Override
    public UserProfile getUserProfile(long userId) {
        UserProfile profile = new UserProfile();
        profile.setStatistics(dao.getUserLabStatistics(userId));
        profile.setUserInfo(userInfoService.getUserInfo(userId));
        profile.setAllowedLabs(studentInfoService.getAllowedLabs(userId));
        profile.setAllowedDefence(studentInfoService.getAllowedDefence(userId));
        profile.setStudentInfo(studentInfoService.getStudentInfo(userId));
        profile.setAverageMark(profile.getStatistics().stream().filter(s -> s.getMark() != 0).mapToDouble(UserLabStatistics::getMark).average().orElse(0.0));
        profile.setAveragePointCount(profile.getStatistics().stream().filter(s -> s.getPointCount() != 0).mapToDouble(UserLabStatistics::getPointCount).average().orElse(0.0));

        try {
            if (profile.getAverageMark() < 3) {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/1.svg")));
            } else if (profile.getAverageMark() < 4) {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/2.svg")));
            } else if (profile.getAverageMark() < 5) {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/3.svg")));
            } else {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/4.svg")));
            }
        } catch (IOException ex) {
            throw new UnhandledException(ex);
        }
        return profile;
    }

    @Override
    public Set<UserProfile> getUserProfiles(String group) {
        return studentInfoService.getGroupMembers(group).stream().map(this::getUserProfile).collect(Collectors.toSet());
    }
}
