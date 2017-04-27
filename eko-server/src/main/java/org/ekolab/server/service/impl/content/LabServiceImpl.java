package org.ekolab.server.service.impl.content;

import org.ekolab.server.dao.api.LabDao;
import org.ekolab.server.model.Calculated;
import org.ekolab.server.model.LabData;
import org.ekolab.server.service.api.ReportsService;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by 777Al on 26.04.2017.
 */
public abstract class LabServiceImpl<T extends LabData> implements LabService<T> {
    @Autowired
    protected ReportsService reportsService;

    private final LabDao<T> labDao;

    protected LabServiceImpl(LabDao<T> labDao) {
        this.labDao = labDao;
    }

    @Override
    public boolean isFieldCalculated(Field field) {
        return field.getAnnotation(Calculated.class) != null;
    }

    @Override
    public T getLastLabByUser(String userName) {
        return loadCalculatedFields(labDao.getLastLabByUser(userName));
    }

    @Override
    public List<T> getAllLabsByUser(String userName) {
        List<T> labs = labDao.getAllLabsByUser(userName);
        labs.forEach(this::loadCalculatedFields);
        return labs;
    }

    @Override
    public T saveLab(T labData) {
        return labDao.saveLab(labData);
    }

    @Override
    public T updateLab(T labData) {
        return labDao.updateLab(labData);
    }

    @Override
    public int removeLabsByUser(String userName) {
        return labDao.removeLabsByUser(userName);
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return labDao.removeOldLabs(lastSaveDate);
    }

    protected abstract T loadCalculatedFields(T labData);

    protected byte[] createReport(String templatePath, Map<String, Object> data) {
        return reportsService.fillReport(reportsService.compileReport(templatePath), data);
    }
}
