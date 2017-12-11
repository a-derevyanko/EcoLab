package org.ekolab.server.dao.impl.content.lab2.random;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab2.random.Lab2RandomDao;
import org.ekolab.server.dao.impl.DaoUtils;
import org.ekolab.server.dao.impl.content.lab2.Lab2DaoImpl;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.ObjectType;
import org.ekolab.server.model.content.lab2.random.Lab2RandomVariant;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.LAB2DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB2_RANDOM_VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab2RandomDaoImpl extends Lab2DaoImpl<Lab2RandomVariant> implements Lab2RandomDao {
    private static final Lab2DataMapper<Lab2RandomVariant> RECORD_MAPPER = new Lab2DataMapper<Lab2RandomVariant>() {

        @Override
        public Lab2Data<Lab2RandomVariant> map(Record record) {
            Lab2Data<Lab2RandomVariant> data = super.map(record);
            data.getVariant().setId(record.get(LAB2_RANDOM_VARIANT.ID));
            data.getVariant().setName(record.get(LAB2_RANDOM_VARIANT.NAME) == null ? null : ObjectType.valueOf(record.get(LAB2_RANDOM_VARIANT.NAME)));
            data.getVariant().setBarometricPressure(record.get(LAB2_RANDOM_VARIANT.BAROMETRIC_PRESSURE));
            data.getVariant().setIndoorsTemperature(record.get(LAB2_RANDOM_VARIANT.INDOORS_TEMPERATURE));
            data.getVariant().setRoomSize(record.get(LAB2_RANDOM_VARIANT.ROOM_SIZE));
            data.getVariant().setQuantityOfSingleTypeEquipment(record.get(LAB2_RANDOM_VARIANT.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT));
            data.getVariant().setAverageSoundPressure(toList(record.get(LAB2_RANDOM_VARIANT.AVERAGE_SOUND_PRESSURE)));
            return data;
        }

        @Override
        protected Lab2RandomVariant createVariant() {
            return new Lab2RandomVariant();
        }
    };

    public Lab2RandomDaoImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public Lab2Data<Lab2RandomVariant> getLastLabByUser(String userName, boolean completed) {
        return dsl.select().from(LAB2DATA).join(LAB2_RANDOM_VARIANT).on(LAB2_RANDOM_VARIANT.ID.eq(LAB2DATA.ID)).
                where(LAB2DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB2DATA.COMPLETED.eq(completed)).
                orderBy(LAB2DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());
    }

    @Override
    public List<Lab2Data<Lab2RandomVariant>> getAllLabsByUser(String userName) {
        return dsl.select().from(LAB2DATA).join(LAB2_RANDOM_VARIANT).on(LAB2_RANDOM_VARIANT.ID.eq(LAB2DATA.ID))
                .where(LAB2DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetch(getLabMapper());
    }

    @Override
    protected Lab2DataMapper<Lab2RandomVariant> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(Lab2RandomVariant variant) {
        dsl.insertInto(LAB2_RANDOM_VARIANT,
                LAB2_RANDOM_VARIANT.ID,
                LAB2_RANDOM_VARIANT.NAME,
                LAB2_RANDOM_VARIANT.BAROMETRIC_PRESSURE,
                LAB2_RANDOM_VARIANT.INDOORS_TEMPERATURE,
                LAB2_RANDOM_VARIANT.ROOM_SIZE,
                LAB2_RANDOM_VARIANT.QUANTITY_OF_SINGLE_TYPE_EQUIPMENT,
                LAB2_RANDOM_VARIANT.AVERAGE_SOUND_PRESSURE).
                values(
                        variant.getId(),
                        variant.getName() == null ? null : variant.getName().name(),
                        variant.getBarometricPressure(),
                        variant.getIndoorsTemperature(),
                        variant.getRoomSize(),
                        variant.getQuantityOfSingleTypeEquipment(),
                        toArray(variant.getAverageSoundPressure())
                ).execute();
    }
}
