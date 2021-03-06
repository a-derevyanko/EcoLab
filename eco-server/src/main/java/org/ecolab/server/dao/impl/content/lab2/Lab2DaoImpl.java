package org.ecolab.server.dao.impl.content.lab2;

import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.content.lab2.Lab2Dao;
import org.ecolab.server.dao.impl.DaoUtils;
import org.ecolab.server.dao.impl.content.LabDaoImpl;
import org.ecolab.server.db.h2.public_.tables.records.Lab2dataRecord;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.Lab2Variant;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Profile;


import static org.ecolab.server.db.h2.public_.Tables.LAB2DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB2TEAM;
import static org.ecolab.server.db.h2.public_.Tables.LAB3TEAM;
import static org.ecolab.server.db.h2.public_.Tables.USERS;
import static org.ecolab.server.db.h2.public_.Tables.USER_LAB_ALLOWANCE;

/**
 * Created by 777Al on 19.04.2017.
 */
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public abstract class Lab2DaoImpl<V extends Lab2Variant> extends LabDaoImpl<Lab2Data<V>> implements Lab2Dao<V> {
    public Lab2DaoImpl(DSLContext dsl) {
        super(dsl);
    }

    protected abstract static class Lab2DataMapper<V extends Lab2Variant>  implements RecordMapper<Record, Lab2Data<V>> {
        @Override
        public Lab2Data<V> map(Record record) {
            var data = new Lab2Data<V>();
            data.setId(record.get(LAB2DATA.ID));
            data.setStartDate(record.get(LAB2DATA.START_DATE));
            data.setSaveDate(record.get(LAB2DATA.SAVE_DATE));
            data.setCompleted(record.get(LAB2DATA.COMPLETED));
            data.setBarometricPressure(record.get(LAB2DATA.BAROMETRIC_PRESSURE));
            data.setIndoorsTemperature(record.get(LAB2DATA.INDOORS_TEMPERATURE));
            data.setRoomSize(record.get(LAB2DATA.ROOM_SIZE));
            data.setQuantityOfSingleTypeEquipment(record.get(LAB2DATA.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT));
            data.setHemisphereRadius(record.get(LAB2DATA.HEMISPHERE_RADIUS));
            data.setAverageSoundPressure(toDoubleList(record.get(LAB2DATA.AVERAGE_SOUND_PRESSURE)));
            data.setCorrectionFactor(record.get(LAB2DATA.CORRECTION_FACTOR));
            data.setSoundPressureMeasuringSurface(record.get(LAB2DATA.SOUND_PRESSURE_MEASURING_SURFACE));
            data.setHemisphereSurface(record.get(LAB2DATA.HEMISPHERE_SURFACE));
            data.setMeasuringFactor(record.get(LAB2DATA.MEASURING_FACTOR));
            data.setSoundPowerLevel(record.get(LAB2DATA.SOUND_POWER_LEVEL));
            data.setRoomConstant1000(record.get(LAB2DATA.ROOM_CONSTANT_1000));
            data.setFrequencyCoefficient(record.get(LAB2DATA.FREQUENCY_COEFFICIENT));
            data.setRoomConstant(record.get(LAB2DATA.ROOM_CONSTANT));
            data.setReflectedSoundPower(record.get(LAB2DATA.REFLECTED_SOUND_POWER));

            var variant = createVariant();
            data.setVariant(variant);

            return data;
        }

        protected abstract V createVariant();
    }

    @Override
    public void saveLab(Lab2Data<V> data) {
        var record = dsl.insertInto(LAB2DATA,
                LAB2DATA.START_DATE,
                LAB2DATA.SAVE_DATE,
                LAB2DATA.COMPLETED,
                LAB2DATA.BAROMETRIC_PRESSURE,
                LAB2DATA.INDOORS_TEMPERATURE,
                LAB2DATA.ROOM_SIZE,
                LAB2DATA.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT,
                LAB2DATA.HEMISPHERE_RADIUS,
                LAB2DATA.AVERAGE_SOUND_PRESSURE,
                LAB2DATA.CORRECTION_FACTOR,
                LAB2DATA.SOUND_PRESSURE_MEASURING_SURFACE,
                LAB2DATA.HEMISPHERE_SURFACE,
                LAB2DATA.MEASURING_FACTOR,
                LAB2DATA.SOUND_POWER_LEVEL,
                LAB2DATA.ROOM_CONSTANT_1000,
                LAB2DATA.FREQUENCY_COEFFICIENT,
                LAB2DATA.ROOM_CONSTANT,
                LAB2DATA.REFLECTED_SOUND_POWER).
                values(
                        Arrays.asList(
                                data.getStartDate(),
                                data.getSaveDate(),
                                data.isCompleted(),
                                data.getBarometricPressure(),
                                data.getIndoorsTemperature(),
                                data.getRoomSize(),
                                data.getQuantityOfSingleTypeEquipment(),
                                data.getHemisphereRadius(),
                                data.getAverageSoundPressure(),
                                data.getCorrectionFactor(),
                                data.getSoundPressureMeasuringSurface(),
                                data.getHemisphereSurface(),
                                data.getMeasuringFactor(),
                                data.getSoundPowerLevel(),
                                data.getRoomConstant1000(),
                                data.getFrequencyCoefficient(),
                                data.getRoomConstant(),
                                data.getReflectedSoundPower())
                ).returning(LAB2DATA.ID, LAB2DATA.START_DATE, LAB2DATA.SAVE_DATE)
                .fetchOne();

        data.setId(record.getId());
        data.setSaveDate(record.getSaveDate());
        data.setStartDate(record.getStartDate());

        data.getVariant().setId(data.getId());
        saveVariant(data.getVariant());
        saveLabUsers(data);
    }

    @Override
    public int updateLab(Lab2Data<V> data) {
        return dsl.update(LAB2DATA)
                .set(LAB2DATA.START_DATE, data.getStartDate())
                .set(LAB2DATA.SAVE_DATE, data.getSaveDate())
                .set(LAB2DATA.COMPLETED, data.isCompleted())
                .set(LAB2DATA.BAROMETRIC_PRESSURE, data.getBarometricPressure())
                .set(LAB2DATA.INDOORS_TEMPERATURE, data.getIndoorsTemperature())
                .set(LAB2DATA.ROOM_SIZE, data.getRoomSize())
                .set(LAB2DATA.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT, data.getQuantityOfSingleTypeEquipment())
                .set(LAB2DATA.HEMISPHERE_RADIUS, data.getHemisphereRadius())
                .set(LAB2DATA.AVERAGE_SOUND_PRESSURE, toArray(data.getAverageSoundPressure()))
                .set(LAB2DATA.CORRECTION_FACTOR, data.getCorrectionFactor())
                .set(LAB2DATA.SOUND_PRESSURE_MEASURING_SURFACE, data.getSoundPressureMeasuringSurface())
                .set(LAB2DATA.HEMISPHERE_SURFACE, data.getHemisphereSurface())
                .set(LAB2DATA.MEASURING_FACTOR, data.getMeasuringFactor())
                .set(LAB2DATA.SOUND_POWER_LEVEL, data.getSoundPowerLevel())
                .set(LAB2DATA.ROOM_CONSTANT_1000, data.getRoomConstant1000())
                .set(LAB2DATA.FREQUENCY_COEFFICIENT, data.getFrequencyCoefficient())
                .set(LAB2DATA.ROOM_CONSTANT, data.getRoomConstant())
                .set(LAB2DATA.REFLECTED_SOUND_POWER, data.getReflectedSoundPower())
                .where(LAB2DATA.ID.eq(data.getId()))
                .execute();
    }

    @Override
    public int removeLabsByUser(String userName) {
        return dsl.deleteFrom(LAB2DATA).where(LAB2DATA.ID.in(dsl.select(LAB3TEAM.ID).from(LAB3TEAM).where(LAB3TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))))).execute();
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return dsl.deleteFrom(LAB2DATA).where(LAB2DATA.SAVE_DATE.greaterThan(lastSaveDate)).execute();
    }

    @Override
    protected int getLabNumber() {
        return 2;
    }

    @Override
    public void setLabCompleted(Lab2Data<V> data) {
        dsl.update(LAB2DATA).set(LAB2DATA.COMPLETED, true).where(LAB2DATA.ID.eq(data.getId())).execute();
        dsl.deleteFrom(USER_LAB_ALLOWANCE).where(USER_LAB_ALLOWANCE.LAB_NUMBER.eq(getLabNumber())
                .and(USER_LAB_ALLOWANCE.USER_ID.in(dsl.select(LAB2TEAM.USER_ID).from(LAB2TEAM).where(LAB2TEAM.ID.eq(data.getId()))))).execute();
    }

    protected static Object[] toArray(List<?> list) {
        return list == null ? null : list.toArray(new Object[0]);
    }

    protected static List<Double> toDoubleList(Object[] array) {
        //todo
        return array == null ? null : Arrays.stream(array).map(o -> Double.valueOf(o.toString())).collect(Collectors.toList());
    }

    protected abstract Lab2DataMapper<V> getLabMapper();

    protected abstract void saveVariant(V variant);

    @Override
    protected void fillLabUsers(Lab2Data data) {
        data.setUsers(Sets.newHashSet(dsl.select(USERS.LOGIN).from(USERS).join(LAB2TEAM).on(USERS.ID.eq(LAB2TEAM.USER_ID)).
                where(LAB2TEAM.ID.eq(data.getId())).fetchInto(String.class)));
    }

    @Override
    protected void saveLabUsers(Lab2Data data) {
        dsl.insertInto(LAB2TEAM, LAB2TEAM.ID, LAB2TEAM.USER_ID).
                select(dsl.select(DSL.val(data.getId()), USERS.ID).from(USERS).where(USERS.LOGIN.in(data.getUsers()))).execute();
    }
}
