package org.ecolab.server.dao.impl.content.lab2.experiment;

import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.content.lab2.experiment.Lab2ExperimentDao;
import org.ecolab.server.dao.impl.DaoUtils;
import org.ecolab.server.dao.impl.content.lab2.Lab2DaoImpl;
import org.ecolab.server.model.content.lab2.Frequency;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.ecolab.server.db.h2.public_.Tables.LAB2DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB2TEAM;
import static org.ecolab.server.db.h2.public_.Tables.LAB2_EXPERIMENT_LOG;
import static org.ecolab.server.db.h2.public_.Tables.LAB2_EXPERIMENT_LOG_SOUND_PRESSURE;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab2ExperimentDaoImpl extends Lab2DaoImpl<Lab2ExperimentLog> implements Lab2ExperimentDao {
    private static final RecordMapper<Record, List<Double>> LAB2_EXPERIMENT_LOG_SOUND_PRESSURE_RECORD_MAPPER = record -> {
        List<Double> values = new ArrayList<>();
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_315));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_63));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_125));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_250));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_500));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_1000));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_2000));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_4000));
        values.add(record.get(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_8000));
        return values;
    };

    private static final Lab2DataMapper<Lab2ExperimentLog> RECORD_MAPPER = new Lab2DataMapper<Lab2ExperimentLog>() {

        @Override
        public Lab2Data<Lab2ExperimentLog> map(Record record) {
            Lab2Data<Lab2ExperimentLog> data = super.map(record);
            data.getVariant().setId(record.get(LAB2_EXPERIMENT_LOG.ID));
            data.getVariant().setName(record.get(LAB2_EXPERIMENT_LOG.NAME));
            data.getVariant().setId(record.get(LAB2_EXPERIMENT_LOG.ID));
            data.getVariant().setBarometricPressure(record.get(LAB2_EXPERIMENT_LOG.BAROMETRIC_PRESSURE));
            data.getVariant().setIndoorsTemperature(record.get(LAB2_EXPERIMENT_LOG.INDOORS_TEMPERATURE));
            data.getVariant().setRoomSize(record.get(LAB2_EXPERIMENT_LOG.ROOM_SIZE));
            data.getVariant().setQuantityOfSingleTypeEquipment(record.get(LAB2_EXPERIMENT_LOG.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT));
            data.getVariant().setHemisphereRadius(record.get(LAB2_EXPERIMENT_LOG.HEMISPHERE_RADIUS));
            data.getVariant().setAverageSoundPressure(toDoubleList(record.get(LAB2_EXPERIMENT_LOG.AVERAGE_SOUND_PRESSURE)));
            data.getVariant().setEstimatedGeometricMeanFrequency(record.get(LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY) == null ?
                    null : Frequency.valueOf(record.get(LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY)));

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
        Lab2Data<Lab2ExperimentLog> data = dsl.select().from(LAB2DATA).join(LAB2_EXPERIMENT_LOG).on(LAB2_EXPERIMENT_LOG.ID.eq(LAB2DATA.ID)).
                where(LAB2DATA.ID.eq(dsl.select(LAB2TEAM.ID).where(LAB2TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName)))))
                        .and(LAB2DATA.COMPLETED.eq(completed)).
                orderBy(LAB2DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());

        if (data == null) {
            return null;
        } else {
            data.getVariant().setSoundPressure(dsl.selectFrom(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE)
                    .where(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.LAB_ID.eq(data.getId()))
                    .orderBy(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.ID)
                    .fetch(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE_RECORD_MAPPER));
        }
        fillLabUsers(data);
        return data;
    }

    @Override
    protected Lab2DataMapper<Lab2ExperimentLog> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(Lab2ExperimentLog variant) {
        List<Query> queries = new ArrayList<>();
        queries.add(dsl.insertInto(LAB2_EXPERIMENT_LOG,
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
                        variant.getName(),
                        variant.getBarometricPressure(),
                        variant.getIndoorsTemperature(),
                        variant.getRoomSize(),
                        variant.getQuantityOfSingleTypeEquipment(),
                        variant.getHemisphereRadius(),
                        variant.getEstimatedGeometricMeanFrequency() == null ? null : variant.getEstimatedGeometricMeanFrequency().value(),
                        toArray(variant.getAverageSoundPressure())
                ));

        List<List<Double>> soundPressure1 = variant.getSoundPressure();
        for (int i = 0; i < soundPressure1.size(); i++) {
            List<Double> soundPressure = soundPressure1.get(i);
            queries.add(dsl.insertInto(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.ID, Long.valueOf(i)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.LAB_ID, variant.getId()).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_315, soundPressure.get(0)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_63, soundPressure.get(1)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_125, soundPressure.get(2)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_250, soundPressure.get(3)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_500, soundPressure.get(4)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_1000, soundPressure.get(5)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_2000, soundPressure.get(6)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_4000, soundPressure.get(7)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_8000, soundPressure.get(8)));
        }
        dsl.batch(queries).execute();
    }

    @Override
    public void updateExperimentJournal(Lab2ExperimentLog experimentJournal) {
        List<Query> queries = new ArrayList<>();
        queries.add(dsl.update(LAB2_EXPERIMENT_LOG)
                .set(LAB2_EXPERIMENT_LOG.NAME, experimentJournal.getName())
                .set(LAB2_EXPERIMENT_LOG.BAROMETRIC_PRESSURE, experimentJournal.getBarometricPressure())
                .set(LAB2_EXPERIMENT_LOG.INDOORS_TEMPERATURE, experimentJournal.getIndoorsTemperature())
                .set(LAB2_EXPERIMENT_LOG.ROOM_SIZE, experimentJournal.getRoomSize())
                .set(LAB2_EXPERIMENT_LOG.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT, experimentJournal.getQuantityOfSingleTypeEquipment())
                .set(LAB2_EXPERIMENT_LOG.HEMISPHERE_RADIUS, experimentJournal.getHemisphereRadius())
                .set(LAB2_EXPERIMENT_LOG.AVERAGE_SOUND_PRESSURE, toArray(experimentJournal.getAverageSoundPressure()))
                .set(LAB2_EXPERIMENT_LOG.ESTIMATED_GEOMETRIC_MEAN_FREQUENCY, experimentJournal.getEstimatedGeometricMeanFrequency().value())
                .where(LAB2_EXPERIMENT_LOG.ID.eq(experimentJournal.getId())));

        queries.add(dsl.deleteFrom(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE)
                .where(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.LAB_ID.eq(experimentJournal.getId())));
        for (int i = 0; i < experimentJournal.getSoundPressure().size(); i++) {
            List<Double> soundPressure = experimentJournal.getSoundPressure().get(i);
            queries.add(dsl.insertInto(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.ID, Long.valueOf(i)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.LAB_ID, experimentJournal.getId()).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_315, soundPressure.get(0)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_63, soundPressure.get(1)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_125, soundPressure.get(2)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_250, soundPressure.get(3)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_500, soundPressure.get(4)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_1000, soundPressure.get(5)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_2000, soundPressure.get(6)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_4000, soundPressure.get(7)).
                    set(LAB2_EXPERIMENT_LOG_SOUND_PRESSURE.F_8000, soundPressure.get(8)));
        }
        dsl.batch(queries).execute();
    }
}
