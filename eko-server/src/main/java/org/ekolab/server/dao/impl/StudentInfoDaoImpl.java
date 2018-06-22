package org.ekolab.server.dao.impl;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentTeam;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.STUDY_GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_GROUP_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_TEACHERS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_TEAMS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_TEAM_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.USERS;

/**
 * Created by 777Al on 24.05.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class StudentInfoDaoImpl implements StudentInfoDao {
    private static final RecordMapper<Record, StudentGroup> STUDENT_GROUP_RECORD_MAPPER = record -> {
        StudentGroup group = new StudentGroup();
        group.setId(record.get(STUDY_GROUPS.ID));
        group.setName(record.get(STUDY_GROUPS.NAME));
        return group;
    };

    private static final RecordMapper<Record, StudentTeam> STUDENT_TEAM_RECORD_MAPPER = record -> {
        StudentTeam team = new StudentTeam();
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
            StudentTeam team = new StudentTeam();
            team.setName(teamRecord.get(STUDY_TEAMS.NAME));
            return team;
        }
    }

    @Override
    public List<String> getTeamMembers(String teamNumber, String group) {
        return dsl.select(USERS.LOGIN).from(STUDY_TEAM_MEMBERS)
                .join(USERS).on(STUDY_TEAM_MEMBERS.USER_ID.eq(USERS.ID))
                .join(STUDY_TEAMS).on(STUDY_TEAM_MEMBERS.TEAM_ID.eq(STUDY_TEAMS.ID))
                .where(STUDY_TEAMS.NAME.eq(teamNumber)).and(STUDY_TEAMS.GROUP_ID
                        .eq(dsl.select(STUDY_GROUPS.ID).from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)))).
                        fetchInto(String.class);
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
    public void addTeacherToStudent(String studentLogin, String teacherLogin) {
        dsl.insertInto(STUDY_TEACHERS).
                set(STUDY_TEACHERS.STUDENT_ID, dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(studentLogin))).
                set(STUDY_TEACHERS.TEACHER_ID, dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(teacherLogin)));
    }

    @Override
    public String getStudentTeacher(String studentLogin) {
        return dsl.select(USERS.LOGIN).from(USERS).where(USERS.ID.eq(dsl.select(STUDY_TEACHERS.ID).from(STUDY_TEACHERS))).
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
        dsl.deleteFrom(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(name));
    }

    @Override
    public void removeStudentTeam(String name, String group) {
        dsl.deleteFrom(STUDY_TEAMS).where(STUDY_TEAMS.GROUP_ID.eq(dsl.
                select(STUDY_GROUPS.ID).
                from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group))).and(STUDY_TEAMS.NAME.eq(name)));
    }

    @Override
    public void updateStudentGroup(StudentGroup group) {
        dsl.update(STUDY_GROUPS)
                .set(STUDY_GROUPS.NAME, group.getName()).where(STUDY_GROUPS.ID.eq(group.getId())).execute();
    }

    @Override
    public List<StudentGroup> getStudentGroups() {
        return dsl.selectFrom(STUDY_GROUPS).fetch().map(STUDENT_GROUP_RECORD_MAPPER);
    }

    @Override
    public List<StudentTeam> getStudentTeams(String group) {
        return dsl.selectFrom(STUDY_TEAMS).where(STUDY_TEAMS.GROUP_ID.eq(dsl.
                select(STUDY_GROUPS.ID).
                from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)))).fetch().map(STUDENT_TEAM_RECORD_MAPPER);
    }
}
