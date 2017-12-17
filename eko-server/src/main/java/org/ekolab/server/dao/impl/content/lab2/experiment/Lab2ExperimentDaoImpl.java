package org.ekolab.server.dao.impl.content.lab2.experiment;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab2.experiment.Lab2ExperimentDao;
import org.ekolab.server.dao.impl.DaoUtils;
import org.ekolab.server.dao.impl.content.lab2.Lab2DaoImpl;
import org.ekolab.server.model.content.lab2.EstimatedGeometricMeanFrequency;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.ObjectType;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.LAB2DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB2_EXPERIMENT_LOG;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab2ExperimentDaoImpl extends Lab2DaoImpl<Lab2ExperimentLog> implements Lab2ExperimentDao {
    private static final Lab2DataMapper<Lab2ExperimentLog> RECORD_MAPPER = new Lab2DataMapper<Lab2ExperimentLog>() {

        @Override
        public Lab2Data<Lab2ExperimentLog> map(Record record) {
            Lab2Data<Lab2ExperimentLog> data = super.map(record);
            data.getVariant().setId(record.get(LAB2_EXPERIMENT_LOG.ID));
            data.getVariant().setName(record.get(LAB2_EXPERIMENT_LOG.NAME) == null ? null : ObjectType.valueOf(record.get(LAB2_EXPERIMENT_LOG.NAME)));
            data.getVariant().setId(record.get(LAB2_EXPERIMENT_LOG.ID));
            data.getVariant().setBarometricPressure(record.get(LAB2_EXPERIMENT_LOG.BAROMETRIC_PRESSURE));
            data.getVariant().setIndoorsTemperature(record.get(LAB2_EXPERIMENT_LOG.INDOORS_TEMPERATURE));
            data.getVariant().setRoomSize(record.get(LAB2_EXPERIMENT_LOG.ROOM_SIZE));
            data.getVariant().setQuantityOfSingleTypeEquipment(record.get(LAB2_EXPERIMENT_LOG.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT));
            data.getVariant().setHemisphereRadius(record.get(LAB2_EXPERIMENT_LOG.HEMISPHERE_RADIUS));
            data.getVariant().setAverageSoundPressure(toList(record.get(LAB2_EXPERIMENT_LOG.AVERAGE_SOUND_PRESSURE)));
            data.getVariant().setEstimatedGeometricMeanFrequency(record.get(LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY) == null ?
                    null : EstimatedGeometricMeanFrequency.valueOf(record.get(LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY)));

            return data;
        }

        @Override
        protected Lab2ExperimentLog createVariant() {
            return new Lab2ExperimentLog();
        }
    };

    public Lab2ExperimentDaoImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public Lab2Data<Lab2ExperimentLog> getLastLabByUser(String userName, boolean completed) {
        return dsl.select().from(LAB2DATA).join(LAB2_EXPERIMENT_LOG).on(LAB2_EXPERIMENT_LOG.ID.eq(LAB2DATA.ID)).
                where(LAB2DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB2DATA.COMPLETED.eq(completed)).
                orderBy(LAB2DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());
    }

    @Override
    public List<Lab2Data<Lab2ExperimentLog>> getAllLabsByUser(String userName) {
        return dsl.select().from(LAB2DATA).join(LAB2_EXPERIMENT_LOG).on(LAB2_EXPERIMENT_LOG.ID.eq(LAB2DATA.ID))
                .where(LAB2DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetch(getLabMapper());
    }

    @Override
    protected Lab2DataMapper<Lab2ExperimentLog> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(Lab2ExperimentLog variant) {
        dsl.insertInto(LAB2_EXPERIMENT_LOG,
                LAB2_EXPERIMENT_LOG.ID,
                LAB2_EXPERIMENT_LOG.NAME,
                LAB2_EXPERIMENT_LOG.BAROMETRIC_PRESSURE,
                LAB2_EXPERIMENT_LOG.INDOORS_TEMPERATURE,
                LAB2_EXPERIMENT_LOG.ROOM_SIZE,
                LAB2_EXPERIMENT_LOG.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT,
                LAB2_EXPERIMENT_LOG.HEMISPHERE_RADIUS,
                LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY,
                LAB2_EXPERIMENT_LOG.AVERAGE_SOUND_PRESSURE).
                values(
                        variant.getId(),
                        variant.getName() == null ? null : variant.getName().name(),
                        variant.getBarometricPressure(),
                        variant.getIndoorsTemperature(),
                        variant.getRoomSize(),
                        variant.getQuantityOfSingleTypeEquipment(),
                        variant.getHemisphereRadius(),
                        variant.getEstimatedGeometricMeanFrequency() == null ? null : variant.getEstimatedGeometricMeanFrequency().value(),
                        toArray(variant.getAverageSoundPressure())
                ).execute();
    }

    @Override
    public void updateExperimentJournal(Lab2ExperimentLog experimentJournal) {
        dsl.update(LAB2_EXPERIMENT_LOG)
                .set(LAB2_EXPERIMENT_LOG.NAME, experimentJournal.getName() == null ? null : experimentJournal.getName().name())
                .set(LAB2_EXPERIMENT_LOG.BAROMETRIC_PRESSURE, experimentJournal.getBarometricPressure())
                .set(LAB2_EXPERIMENT_LOG.INDOORS_TEMPERATURE, experimentJournal.getIndoorsTemperature())
                .set(LAB2_EXPERIMENT_LOG.ROOM_SIZE, experimentJournal.getRoomSize())
                .set(LAB2_EXPERIMENT_LOG.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT, experimentJournal.getQuantityOfSingleTypeEquipment())
                .set(LAB2_EXPERIMENT_LOG.HEMISPHERE_RADIUS, experimentJournal.getHemisphereRadius())
                .set(LAB2_EXPERIMENT_LOG.AVERAGE_SOUND_PRESSURE, toArray(experimentJournal.getAverageSoundPressure()))
                .set(LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY, experimentJournal.getEstimatedGeometricMeanFrequency().value())
                .where(LAB2_EXPERIMENT_LOG.ID.eq(experimentJournal.getId()))
                .execute();
    }
}
