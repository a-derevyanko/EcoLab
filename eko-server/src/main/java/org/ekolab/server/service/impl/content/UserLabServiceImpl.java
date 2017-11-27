package org.ekolab.server.service.impl.content;

import org.ekolab.server.dao.api.content.UserLabDao;
import org.ekolab.server.model.LabMode;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserLabServiceImpl implements UserLabService {
    private final UserLabDao dao;

    private final List<LabService<?, ?>> labServices;

    public UserLabServiceImpl(UserLabDao dao, List<LabService<?, ?>> labServices) {
        this.dao = dao;
        this.labServices = labServices;
    }

    @Override
    @Cacheable(value = "COMPLETED_TEST", key = "#userName")
    public Collection<Integer> getCompletedTests(String userName) {
        return dao.getCompletedTests(userName);
    }

    @Override
    public Map<Integer, LabMode> getCompletedLabs(String userName) {
        return dao.getCompletedLabs(userName);
    }

    @Override
    @CachePut(value = "COMPLETED_TEST", key = "#userName")
    public Collection<Integer> setTestCompleted(String userName, int labNumber) {
        dao.setTestCompleted(userName, labNumber);
        return dao.getCompletedTests(userName);
    }

    @Override
    public int removeAllOldLabs(LocalDateTime lastSaveDate) {
        AtomicInteger removedLabs = new AtomicInteger();
        labServices.forEach(labService -> removedLabs.addAndGet(labService.removeOldLabs(lastSaveDate)));
        return removedLabs.get();
    }
}
