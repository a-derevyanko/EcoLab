package org.ecolab.server.dao.impl;

import org.ecolab.server.dao.api.content.UserLabDao;
import org.ecolab.server.model.LabMode;
import org.ecolab.server.model.UserLabStatistics;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record6;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.ecolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB1TEAM;
import static org.ecolab.server.db.h2.public_.Tables.LAB1_EXPERIMENT_LOG;
import static org.ecolab.server.db.h2.public_.Tables.LAB1_RANDOM_VARIANT;
import static org.ecolab.server.db.h2.public_.Tables.LAB2DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB2TEAM;
import static org.ecolab.server.db.h2.public_.Tables.LAB2_RANDOM_VARIANT;
import static org.ecolab.server.db.h2.public_.Tables.LAB3DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB3TEAM;
import static org.ecolab.server.db.h2.public_.Tables.USER_DEFENCE_ALLOWANCE;
import static org.ecolab.server.db.h2.public_.Tables.USER_TEST_HISTORY;
import static org.ecolab.server.db.h2.public_.tables.Lab2ExperimentLog.LAB2_EXPERIMENT_LOG;

@Service
public class UserLabDaoImpl implements UserLabDao {
  private static final RecordMapper<Record6<Integer, Integer, LocalDateTime, LocalDateTime, Integer, Integer>, UserLabStatistics> USER_LAB_STATISTICS_RECORD_MAPPER = record -> {
    UserLabStatistics statistics = new UserLabStatistics();
    statistics.setLabNumber(record.value1());
    statistics.setTryCount(record.value2() == null ? 0 : record.value2());
    statistics.setLabDate(record.value3());
    statistics.setTestDate(record.value4());
    statistics.setMark(record.value5() == null ? 0 : record.value5());
    statistics.setPointCount(record.value6() == null ? 0 : record.value6());
    return statistics;
  };

  private final DSLContext dsl;

    public UserLabDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Collection<Integer> getCompletedTests(long user) {
        return dsl.select(USER_TEST_HISTORY.LAB_NUMBER).from(USER_TEST_HISTORY).where(USER_TEST_HISTORY.USER_ID.eq(user)).fetchInto(Integer.class);
    }

    @Override
    //todo оптимизировать запрос
    public Map<Integer, LabMode> getCompletedLabs(long user) {
      return dsl.select(DSL.val(1), DSL.val(LabMode.EXPERIMENT.name())).from(LAB1DATA).join(LAB1_EXPERIMENT_LOG).on(LAB1DATA.ID.eq(LAB1_EXPERIMENT_LOG.ID)).
              where(LAB1DATA.ID.eq(dsl.select(LAB1TEAM.ID)
                      .from(LAB1TEAM).where(LAB1TEAM.USER_ID.eq(user)))).and(LAB1DATA.COMPLETED.eq(true))
              .union(dsl.select(DSL.val(1), DSL.val(LabMode.RANDOM.name())).from(LAB1DATA).join(LAB1_RANDOM_VARIANT).on(LAB1DATA.ID.eq(LAB1_RANDOM_VARIANT.ID)).
                      where(LAB1DATA.ID.eq(dsl.select(LAB1TEAM.ID)
                              .from(LAB1TEAM).where(LAB1TEAM.USER_ID.eq(user)))).and(LAB1DATA.COMPLETED.eq(true)))
              .union(dsl.select(DSL.val(2), DSL.val(LabMode.EXPERIMENT.name())).from(LAB2DATA).join(LAB2_EXPERIMENT_LOG).on(LAB2DATA.ID.eq(LAB2_EXPERIMENT_LOG.ID)).
                      where(LAB2DATA.ID.eq(dsl.select(LAB2TEAM.ID)
                              .from(LAB2TEAM).where(LAB2TEAM.USER_ID.eq(user)))).and(LAB2DATA.COMPLETED.eq(true))
                      .union(dsl.select(DSL.val(2), DSL.val(LabMode.RANDOM.name())).from(LAB2DATA).join(LAB2_RANDOM_VARIANT).on(LAB2DATA.ID.eq(LAB2_RANDOM_VARIANT.ID)).
                              where(LAB2DATA.ID.eq(dsl.select(LAB2TEAM.ID)
                                      .from(LAB2TEAM).where(LAB2TEAM.USER_ID.eq(user)))).and(LAB2DATA.COMPLETED.eq(true))))
              .union(dsl.select(DSL.val(3), DSL.val(LabMode.NONE.name())).from(LAB3DATA).
                      where(LAB3DATA.ID.eq(dsl.select(LAB3TEAM.ID)
                              .from(LAB3TEAM).where(LAB3TEAM.USER_ID.eq(user)))).and(LAB3DATA.COMPLETED.eq(true))).
                      fetch().stream().collect(Collectors.toMap(Record2::value1, r -> LabMode.valueOf(r.value2())));
    }

    @Override
    public void setTestCompleted(long user, int labNumber, int mark, int pointCount) {
        dsl.insertInto(USER_TEST_HISTORY, USER_TEST_HISTORY.LAB_NUMBER,
                USER_TEST_HISTORY.LAST_MODIFY_DATE, USER_TEST_HISTORY.USER_ID,
                USER_TEST_HISTORY.MARK, USER_TEST_HISTORY.POINT_COUNT)
                .values(Arrays.asList(labNumber, OffsetDateTime.now(),
                        user, mark, pointCount)).execute();
        dsl.deleteFrom(USER_DEFENCE_ALLOWANCE).where(USER_DEFENCE_ALLOWANCE.LAB_NUMBER.eq(labNumber)
                .and(USER_DEFENCE_ALLOWANCE.USER_ID.eq(user))).execute();
    }

    @Override
    public List<UserLabStatistics> getUserLabStatistics(String userName) {
      return dsl.select(DSL.val(1),
              DSL.count(USER_TEST_HISTORY.ID), DSL.max(LAB1DATA.SAVE_DATE), DSL.max(USER_TEST_HISTORY.LAST_MODIFY_DATE),
                      DSL.max(USER_TEST_HISTORY.MARK),
                              DSL.max(USER_TEST_HISTORY.POINT_COUNT))
              .from(LAB1DATA)
              .leftJoin(LAB1TEAM).on(LAB1DATA.ID.eq(LAB1DATA.ID))
              .join(USER_TEST_HISTORY).on(USER_TEST_HISTORY.USER_ID.eq(LAB1TEAM.USER_ID).and(USER_TEST_HISTORY.LAB_NUMBER.eq(1))).
                      where(LAB1DATA.COMPLETED.isTrue().and(LAB1TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))))
              .union(dsl.select(DSL.val(2),
                      DSL.count(USER_TEST_HISTORY.ID), DSL.max(LAB2DATA.SAVE_DATE), DSL.max(USER_TEST_HISTORY.LAST_MODIFY_DATE),
                      DSL.max(USER_TEST_HISTORY.MARK),
                      DSL.max(USER_TEST_HISTORY.POINT_COUNT))
                      .from(LAB2DATA)
                      .leftJoin(LAB2TEAM).on(LAB2DATA.ID.eq(LAB2DATA.ID))
                      .join(USER_TEST_HISTORY).on(USER_TEST_HISTORY.USER_ID.eq(LAB2TEAM.USER_ID).and(USER_TEST_HISTORY.LAB_NUMBER.eq(2))).
                              where(LAB2DATA.COMPLETED.isTrue().and(LAB2TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName)))))
              .union(dsl.select(DSL.val(3),
                      DSL.count(USER_TEST_HISTORY.ID), DSL.max(LAB3DATA.SAVE_DATE), DSL.max(USER_TEST_HISTORY.LAST_MODIFY_DATE),
                      DSL.max(USER_TEST_HISTORY.MARK),
                      DSL.max(USER_TEST_HISTORY.POINT_COUNT))
                      .from(LAB3DATA)
                      .leftJoin(LAB3TEAM).on(LAB3DATA.ID.eq(LAB3DATA.ID))
                      .join(USER_TEST_HISTORY).on(USER_TEST_HISTORY.USER_ID.eq(LAB3TEAM.USER_ID).and(USER_TEST_HISTORY.LAB_NUMBER.eq(3))).
                              where(LAB3DATA.COMPLETED.isTrue().and(LAB3TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName)))))
              .orderBy(1)
              .fetch(USER_LAB_STATISTICS_RECORD_MAPPER);
    }
}
