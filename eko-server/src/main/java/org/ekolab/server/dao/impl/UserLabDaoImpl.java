package org.ekolab.server.dao.impl;

import org.ekolab.server.dao.api.content.UserLabDao;
import org.ekolab.server.model.LabMode;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.ekolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB1_EXPERIMENT_LOG;
import static org.ekolab.server.db.h2.public_.Tables.LAB1_RANDOM_VARIANT;
import static org.ekolab.server.db.h2.public_.Tables.LAB3DATA;
import static org.ekolab.server.db.h2.public_.Tables.USER_TEST_HISTORY;

@Service
public class UserLabDaoImpl implements UserLabDao {
    private final DSLContext dsl;

    public UserLabDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Collection<Integer> getCompletedTests(String userName) {
        return dsl.select(USER_TEST_HISTORY.LAB_NUMBER).from(USER_TEST_HISTORY).where(USER_TEST_HISTORY.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetchInto(Integer.class);
    }

    @Override
    public Map<Integer, LabMode> getCompletedLabs(String userName) {
        return dsl.select(DSL.val(1), DSL.val(LabMode.EXPERIMENT.name())).from(LAB1DATA).join(LAB1_EXPERIMENT_LOG).on(LAB1DATA.ID.eq(LAB1_EXPERIMENT_LOG.ID)).
                where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB1DATA.COMPLETED.eq(true))
                .union(dsl.select(DSL.val(1), DSL.val(LabMode.RANDOM.name())).from(LAB1DATA).join(LAB1_RANDOM_VARIANT).on(LAB1DATA.ID.eq(LAB1_RANDOM_VARIANT.ID)).
                        where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB1DATA.COMPLETED.eq(true)))
        /*.union(dsl.select(DSL.val(2)).from(LAB2DATA).
                where(LAB2DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB2DATA.COMPLETED.eq(true)))*/
        .union(dsl.select(DSL.val(3), DSL.val(LabMode.NONE.name())).from(LAB3DATA).
                where(LAB3DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB3DATA.COMPLETED.eq(true))).
                        fetch().stream().collect(Collectors.toMap(Record2::value1, r -> LabMode.valueOf(r.value2())));

        /*
        Collection<Integer> completed = new ArrayList<>();
        if (dsl.fetchExists(dsl.select().from(LAB1DATA).
                where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB1DATA.COMPLETED.eq(true)))) {
            completed.add(1);
        }
        //todo
        if (dsl.fetchExists(dsl.select().from(LAB2DATA).
                where(LAB2DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB2DATA.COMPLETED.eq(true)))) {
            completed.add(2);
        }
        if (dsl.fetchExists(dsl.select().from(LAB3DATA).
                where(LAB3DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB3DATA.COMPLETED.eq(true)))) {
            completed.add(3);
        }
        return completed;*/
    }

    @Override
    public void setTestCompleted(String userName, int labNumber) {
        dsl.insertInto(USER_TEST_HISTORY, USER_TEST_HISTORY.LAB_NUMBER, USER_TEST_HISTORY.USER_ID).values(Arrays.asList(labNumber, DaoUtils.getFindUserIdSelect(dsl, userName)));
    }
}
