package org.ecolab.server.dao.impl;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang3.ClassUtils;
import org.ecolab.server.dao.api.SettingsDao;
import org.ecolab.server.db.h2.public_.tables.records.CommonSettingsRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import static org.ecolab.server.db.h2.public_.Tables.COMMON_SETTINGS;

@Service
public class SettingsDaoImpl implements SettingsDao {
    private final DSLContext dsl;

    public SettingsDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public <T> T getSetting(String valueName) {
        CommonSettingsRecord r = dsl.selectFrom(COMMON_SETTINGS).where(COMMON_SETTINGS.NAME.eq(valueName)).fetchOne();

        String value = r.getValue();
        try {
            Class valueClass = r.getType().equals("string") ? String.class : ClassUtils.primitiveToWrapper(ClassUtils.getClass(ClassLoader.getSystemClassLoader(),
                    r.getType(), true));

            Object val;
            if (valueClass == Boolean.class) {
                val = Boolean.parseBoolean(value);
            } else if (valueClass == String.class) {
                val = value;
            } else {
                val = NumberUtils.parseNumber(value, valueClass);
            }
            return (T) val;
        } catch (ClassNotFoundException e) {
            throw new UnhandledException(e);
        }
    }
}
