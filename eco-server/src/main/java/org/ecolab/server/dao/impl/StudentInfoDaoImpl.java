package org.ecolab.server.dao.impl;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.content.StudentInfoDao;
import org.ecolab.server.model.StudentGroup;
import org.ecolab.server.model.StudentGroupInfo;
import org.ecolab.server.model.StudentTeam;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

import static org.ecolab.server.db.h2.public_.Tables.STUDY_GROUPS;
import static org.ecolab.server.db.h2.public_.Tables.STUDY_GROUP_MEMBERS;
import static org.ecolab.server.db.h2.public_.Tables.STUDY_GROUP_TEACHERS;
import static org.ecolab.server.db.h2.public_.Tables.STUDY_TEAMS;
import static org.ecolab.server.db.h2.public_.Tables.STUDY_TEAM_MEMBERS;
import static org.ecolab.server.db.h2.public_.Tables.USERS;
import static org.ecolab.server.db.h2.public_.Tables.USER_DEFENCE_ALLOWANCE;
import static org.ecolab.server.db.h2.public_.Tables.USER_LAB_ALLOWANCE;
import static org.ecolab.server.db.h2.public_.Tables.USER_LAB_HISTORY;
import static org.ecolab.server.db.h2.public_.Tables.USER_TEST_HISTORY;

/**
 * Created by 777Al on 24.05.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class StudentInfoDaoImpl implements StudentInfoDao {
    private static final String TEST_COUNT = "TEST_COUNT";
    private static final String STUDENT_COUNT = "STUDENT_COUNT";
    private static final String LAB_COUNT = "LAB_COUNT";
    private static final RecordMapper<Record, StudentGroupInfo> STUDENT_INFO_RECORD_MAPPER = record -> {
        var group = new StudentGroupInfo();
        group.setIndex(record.get(0, Integer.class));
        group.setName(record.get(1, String.class));
        group.setStudentCount(record.get(STUDENT_COUNT, Integer.class));
        group.setLabCount(record.get(LAB_COUNT, Integer.class));
        group.setDefenceCount(record.get(TEST_COUNT, Integer.class));
        return group;
    };
    private static final RecordMapper<Record, StudentGroup> STUDENT_GROUP_RECORD_MAPPER = record -> {
        var group = new StudentGroup();
        group.setId(record.get(STUDY_GROUPS.ID));
        group.setName(record.get(STUDY_GROUPS.NAME));
        return group;
    };

    private static final RecordMapper<Record, StudentTeam> STUDENT_TEAM_RECORD_MAPPER = record -> {
        var team = new StudentTeam();
        team.setId(record.get(STUDY_TEAMS.ID));
        team.setName(record.get(STUDY_TEAMS.NAME));
        return team;
    };

    private final DSLContext dsl;

    @Autowired
    public StudentInfoDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public StudentGroup getStudentGroup(String userName) {
        return dsl.select().from(STUDY_GROUPS).join(STUDY_GROUP_MEMBERS)
                .on(STUDY_GROUP_MEMBERS.GROUP_ID.eq(STUDY_GROUPS.ID)).join(USERS).on(USERS.ID.eq(STUDY_GROUP_MEMBERS.USER_ID))
                .where(USERS.LOGIN.eq(userName)).fetchOne(STUDENT_GROUP_RECORD_MAPPER);
    }

    @Override
    public StudentTeam getStudentTeam(String userName) {
        Record teamRecord = dsl.select(STUDY_TEAMS.ID, STUDY_TEAMS.NAME).from(STUDY_TEAMS).join(STUDY_TEAM_MEMBERS)
                .on(STUDY_TEAMS.ID.eq(STUDY_TEAM_MEMBERS.TEAM_ID)).join(USERS).on(STUDY_TEAM_MEMBERS.USER_ID.eq(USERS.ID))
                .where(USERS.LOGIN.eq(userName)).fetchOne();

        if (teamRecord == null) {
            return null;
        } else {
            var team = new StudentTeam();
            team.setName(teamRecord.get(STUDY_TEAMS.NAME));
            return team;
        }
    }

    @Override
    public Set<String> getTeamMembers(String teamNumber, String group) {
        return Sets.newHashSet(dsl.select(USERS.LOGIN).from(STUDY_TEAM_MEMBERS)
                .join(USERS).on(STUDY_TEAM_MEMBERS.USER_ID.eq(USERS.ID))
                .join(STUDY_TEAMS).on(STUDY_TEAM_MEMBERS.TEAM_ID.eq(STUDY_TEAMS.ID))
                .where(STUDY_TEAMS.NAME.eq(teamNumber)).and(STUDY_TEAMS.GROUP_ID
                        .eq(dsl.select(STUDY_GROUPS.ID).from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)))).
                        orderBy(USERS.LAST_NAME,USERS.FIRST_NAME, USERS.MIDDLE_NAME).
                        fetchInto(String.class));
    }

    @Override
    public Set<String> getGroupMembers(String group) {
        return Sets.newHashSet(dsl.select(USERS.LOGIN).from(USERS)
                .join(STUDY_GROUP_MEMBERS).on(STUDY_GROUP_MEMBERS.USER_ID.eq(USERS.ID))
                .join(STUDY_GROUPS).on(STUDY_GROUPS.ID.eq(STUDY_GROUP_MEMBERS.GROUP_ID))
                .where(STUDY_GROUPS.ID.eq(dsl.select(STUDY_GROUPS.ID).from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)))).
                        orderBy(USERS.LAST_NAME,USERS.FIRST_NAME, USERS.MIDDLE_NAME).
                        fetchInto(String.class));
    }

    @Override
    public Set<StudentGroup> getTeacherGroups(String teacher) {
        return Sets.newHashSet(dsl.select().from(STUDY_GROUPS).
                join(STUDY_GROUP_TEACHERS).on(STUDY_GROUP_TEACHERS.GROUP_ID.eq(STUDY_GROUPS.ID)).
                where(STUDY_GROUP_TEACHERS.TEACHER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, teacher))).
                orderBy(STUDY_GROUPS.NAME).
                fetch().map(STUDENT_GROUP_RECORD_MAPPER));
    }

    @Override
    public Set<StudentGroupInfo> getTeacherGroupsInfo(String teacher) {
        var nested = dsl.<Record>select(STUDY_GROUPS.NAME, DSL.count(STUDY_GROUP_MEMBERS.ID).as(STUDENT_COUNT),
                DSL.count(DSL.coalesce(USER_LAB_HISTORY.LAB_NUMBER)).as(LAB_COUNT),
                DSL.count(DSL.coalesce(USER_TEST_HISTORY.LAB_NUMBER)).as(TEST_COUNT)).from(STUDY_GROUPS).
                join(STUDY_GROUP_TEACHERS).on(STUDY_GROUP_TEACHERS.GROUP_ID.eq(STUDY_GROUPS.ID)).
                leftJoin(STUDY_GROUP_MEMBERS).on(STUDY_GROUP_MEMBERS.GROUP_ID.eq(STUDY_GROUPS.ID)).
                leftJoin(USER_TEST_HISTORY).on(USER_TEST_HISTORY.USER_ID.eq(STUDY_GROUP_MEMBERS.USER_ID)).
                leftJoin(USER_LAB_HISTORY).on(USER_LAB_HISTORY.USER_ID.eq(STUDY_GROUP_MEMBERS.USER_ID)).
                where(STUDY_GROUP_TEACHERS.TEACHER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, teacher))).
                groupBy(STUDY_GROUPS.NAME).
                orderBy(STUDY_GROUPS.NAME).
                asTable("T");

        return Sets.newLinkedHashSet(dsl.select(DSL.rownum()).select(nested.fields()).from(nested).
                fetch().map(STUDENT_INFO_RECORD_MAPPER));
    }

    @Override
    public boolean isGroupExists(String group) {
        return dsl.fetchExists(dsl.selectOne().from(STUDY_GROUPS)
                .where(STUDY_GROUPS.NAME.eq(group)));
    }

    @Override
    public boolean isTeamExists(StudentGroup group, String team) {
        return dsl.fetchExists(dsl.selectOne().from(STUDY_TEAMS)
                .where(STUDY_TEAMS.GROUP_ID.eq(group.getId()).and(STUDY_TEAMS.NAME.eq(team))));
    }

    @Override
    public void addGroupToTeacher(String teacher, StudentGroup group) {
        dsl.insertInto(STUDY_GROUP_TEACHERS, STUDY_GROUP_TEACHERS.TEACHER_ID, STUDY_GROUP_TEACHERS.GROUP_ID).
                values(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(teacher)).asField(),
                        DSL.val(group.getId())).execute();
    }

    @Override
    public void removeGroupFromTeacher(String teacher, String group) {
        dsl.deleteFrom(STUDY_GROUP_TEACHERS)
                .where(STUDY_GROUP_TEACHERS.TEACHER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(teacher)))
                        .and(STUDY_GROUP_TEACHERS.GROUP_ID.eq(dsl.select(STUDY_GROUPS.ID).from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)))))
                .execute();
    }

    @Override
    public void updateStudentGroup(String userName, StudentGroup group) {
        dsl.mergeInto(STUDY_GROUP_MEMBERS, STUDY_GROUP_MEMBERS.USER_ID, STUDY_GROUP_MEMBERS.GROUP_ID)
                .key(STUDY_GROUP_MEMBERS.USER_ID)
                .values(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName)).asField(),
                        dsl.select(STUDY_GROUPS.ID).from(STUDY_GROUPS).where(STUDY_GROUPS.ID.eq(group.getId())).asField())
                .execute();
    }

    @Override
    public void updateStudentTeam(String userName, StudentTeam studentTeam) {
        dsl.mergeInto(STUDY_TEAM_MEMBERS, STUDY_TEAM_MEMBERS.USER_ID, STUDY_TEAM_MEMBERS.TEAM_ID)
                .key(STUDY_TEAM_MEMBERS.USER_ID)
                .values(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName)).asField(),
                        dsl.select(STUDY_TEAMS.ID).from(STUDY_TEAMS).where(STUDY_TEAMS.ID.eq(studentTeam.getId())).asField())
                .execute();
    }

    @Override
    public String getGroupTeacher(String group) {
        return dsl.select(USERS.LOGIN).from(USERS).
                join(STUDY_GROUP_TEACHERS).on(STUDY_GROUP_TEACHERS.TEACHER_ID.eq(USERS.ID)).
                join(STUDY_GROUPS).on(STUDY_GROUP_TEACHERS.GROUP_ID.eq(STUDY_GROUPS.ID)).
                where(STUDY_GROUPS.NAME.eq(group)).
                fetchOneInto(String.class);
    }

    @Override
    public StudentGroup createStudentGroup(String name) {
        return dsl.insertInto(STUDY_GROUPS, STUDY_GROUPS.NAME).values(name).returning().fetchOne().map(STUDENT_GROUP_RECORD_MAPPER);
    }

    @Override
    public StudentTeam createStudentTeam(String name, String group) {
        return dsl.insertInto(STUDY_TEAMS, STUDY_TEAMS.NAME, STUDY_TEAMS.GROUP_ID).values(Arrays.asList(name, dsl.
                select(STUDY_GROUPS.ID).
                from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)).asField())).returning().fetchOne().map(STUDENT_TEAM_RECORD_MAPPER);
    }

    @Override
    public void removeStudentGroup(String name) {
        dsl.deleteFrom(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(name)).execute();
    }

    @Override
    public void removeStudentTeam(String name, String group) {
        dsl.deleteFrom(STUDY_TEAMS).where(STUDY_TEAMS.GROUP_ID.eq(dsl.
                select(STUDY_GROUPS.ID).
                from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group))).and(STUDY_TEAMS.NAME.eq(name))).execute();
    }

    @Override
    public void updateStudentGroup(StudentGroup group) {
        dsl.update(STUDY_GROUPS)
                .set(STUDY_GROUPS.NAME, group.getName()).where(STUDY_GROUPS.ID.eq(group.getId())).execute();
    }

    @Override
    public Set<StudentGroup> getStudentGroups() {
        return Sets.newHashSet(dsl.selectFrom(STUDY_GROUPS).orderBy(STUDY_GROUPS.NAME).fetch().map(STUDENT_GROUP_RECORD_MAPPER));
    }

    @Override
    public Set<StudentGroup> getGroupsAvailableForTeacher() {
        return Sets.newHashSet(dsl.selectFrom(STUDY_GROUPS).where(
                DSL.notExists(dsl.selectOne().from(STUDY_GROUP_TEACHERS).
                        where(STUDY_GROUP_TEACHERS.GROUP_ID.eq(STUDY_GROUPS.ID))))
                .orderBy(STUDY_GROUPS.NAME)
                .fetch().map(STUDENT_GROUP_RECORD_MAPPER));
    }

    @Override
    public StudentGroup getStudentGroupByName(String group) {
        return dsl.selectFrom(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)).fetchOne().map(STUDENT_GROUP_RECORD_MAPPER);
    }

    @Override
    public Set<StudentTeam> getStudentTeams(String group) {
        return Sets.newHashSet(dsl.selectFrom(STUDY_TEAMS).where(STUDY_TEAMS.GROUP_ID.eq(dsl.
                select(STUDY_GROUPS.ID).
                from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)))).fetch().map(STUDENT_TEAM_RECORD_MAPPER));
    }

    @Override
    public Set<Integer> getAllowedLabs(String userName) {
        return Sets.newHashSet(dsl.select(USER_LAB_ALLOWANCE.LAB_NUMBER).from(USER_LAB_ALLOWANCE).
                where(USER_LAB_ALLOWANCE.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetchInto(Integer.class));
    }

    @Override
    public Set<Integer> getAllowedDefence(String userName) {
        return Sets.newHashSet(dsl.select(USER_DEFENCE_ALLOWANCE.LAB_NUMBER).from(USER_DEFENCE_ALLOWANCE).
                where(USER_DEFENCE_ALLOWANCE.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetchInto(Integer.class));
    }

    @Override
    public void changeLabAllowance(String userName, boolean allow, int... labs) {
        if (allow) {
            long currentUserId =  DaoUtils.getFindUserIdSelect(dsl, userName).fetchOneInto(Long.class);
            var step = dsl.batch(dsl.insertInto(USER_LAB_ALLOWANCE, USER_LAB_ALLOWANCE.USER_ID, USER_LAB_ALLOWANCE.LAB_NUMBER).
                    values((Long) null, null));

            for (var lab : labs) {
                step = step.bind(currentUserId, lab);
            }
            step.execute();
        } else {
            dsl.deleteFrom(USER_LAB_ALLOWANCE).where(USER_LAB_ALLOWANCE.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))
                    .and(USER_LAB_ALLOWANCE.LAB_NUMBER.in(Ints.asList(labs)))).execute();
        }
    }

    @Override
    public void changeDefenceAllowance(String userName, boolean allow, int... labs) {
        if (allow) {
            long currentUserId =  DaoUtils.getFindUserIdSelect(dsl, userName).fetchOneInto(Long.class);
            var step = dsl.batch(dsl.insertInto(USER_DEFENCE_ALLOWANCE, USER_DEFENCE_ALLOWANCE.USER_ID, USER_DEFENCE_ALLOWANCE.LAB_NUMBER).
                    values((Long) null, null));

            for (var lab : labs) {
                step = step.bind(currentUserId, lab);
            }
            step.execute();
        } else {
            dsl.deleteFrom(USER_DEFENCE_ALLOWANCE).where(USER_DEFENCE_ALLOWANCE.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))
                    .and(USER_DEFENCE_ALLOWANCE.LAB_NUMBER.in(Ints.asList(labs)))).execute();
        }
    }
}
