package org.ekolab.server.dao.impl;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.ekolab.server.model.StudentTeam;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.*;

/**
 * Created by 777Al on 24.05.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class StudentInfoDaoImpl implements StudentInfoDao {
    @Autowired
    private DSLContext dsl;

    @Override
    public String getStudentGroup(String userName) {
        Record1<String> group = dsl.select(STUDY_GROUPS.NAME).from(STUDY_GROUPS).join(STUDY_GROUP_MEMBERS)
                .on(STUDY_GROUP_MEMBERS.GROUP_ID.eq(STUDY_GROUPS.ID)).join(USERS).on(USERS.ID.eq(STUDY_GROUP_MEMBERS.USER_ID))
                .where(USERS.LOGIN.eq(userName)).fetchOne();
        return group == null ? null : group.value1();
    }

    @Override
    public StudentTeam getStudentTeam(String userName) {
        Record teamRecord = dsl.select(STUDY_TEAMS.ID, STUDY_TEAMS.NUMBER).from(STUDY_TEAMS).join(STUDY_TEAM_MEMBERS)
                .on(STUDY_TEAMS.ID.eq(STUDY_TEAM_MEMBERS.TEAM_ID)).join(USERS).on(STUDY_TEAM_MEMBERS.USER_ID.eq(USERS.ID))
                .where(USERS.LOGIN.eq(userName)).fetchOne();

        if (teamRecord == null) {
            return null;
        } else {
            StudentTeam team = new StudentTeam();
            team.setNumber(teamRecord.get(STUDY_TEAMS.NUMBER));
            return team;
        }
    }

    @Override
    public List<String> getTeamMembers(Integer teamNumber) {
        return dsl.select(USERS.LOGIN).from(STUDY_TEAM_MEMBERS)
                .join(USERS).on(STUDY_TEAM_MEMBERS.USER_ID.eq(USERS.ID))
                .join(STUDY_TEAMS).on(STUDY_TEAM_MEMBERS.TEAM_ID.eq(STUDY_TEAMS.ID)).where(STUDY_TEAMS.NUMBER.eq(teamNumber)).
                        fetchInto(String.class);
    }

    @Override
    public void updateStudentGroup(String userName, String group) {
        dsl.mergeInto(STUDY_GROUP_MEMBERS, STUDY_GROUP_MEMBERS.USER_ID, STUDY_GROUP_MEMBERS.GROUP_ID)
                .key(STUDY_GROUP_MEMBERS.USER_ID)
                .values(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName)).asField(),
                        dsl.select(STUDY_GROUPS.ID).from(STUDY_GROUPS).where(STUDY_GROUPS.NAME.eq(group)).asField())
                .execute();
    }

    @Override
    public void updateStudentTeam(String userName, Integer number) {
        dsl.mergeInto(STUDY_TEAM_MEMBERS, STUDY_TEAM_MEMBERS.USER_ID, STUDY_TEAM_MEMBERS.TEAM_ID)
                .key(STUDY_TEAM_MEMBERS.USER_ID)
                .values(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName)).asField(),
                        dsl.select(STUDY_TEAMS.ID).from(STUDY_TEAMS).where(STUDY_TEAMS.NUMBER.eq(number)).asField())
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
    public void createStudentGroup(String name) {
        dsl.insertInto(STUDY_GROUPS, STUDY_GROUPS.NAME).values(name).execute();
    }

    @Override
    public void createStudentTeam(Integer number) {
        dsl.insertInto(STUDY_TEAMS, STUDY_TEAMS.NUMBER).values(number).execute();
    }

    @Override
    public void renameStudentGroup(String name, String newName) {
        dsl.update(STUDY_GROUPS)
                .set(STUDY_GROUPS.NAME, newName).where(STUDY_GROUPS.NAME.eq(name)).execute();
    }
}
