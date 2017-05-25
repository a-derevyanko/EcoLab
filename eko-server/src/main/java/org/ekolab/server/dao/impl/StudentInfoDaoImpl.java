package org.ekolab.server.dao.impl;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static org.ekolab.server.db.h2.public_.Tables.STUDY_GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_GROUP_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_TEAMS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_TEAM_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.USERS;

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
    public String getStudentTeam(String userName) {
        Record1<String> team = dsl.select(STUDY_TEAMS.NAME).from(STUDY_TEAMS).join(STUDY_TEAM_MEMBERS)
                .on(STUDY_TEAMS.ID.eq(STUDY_TEAM_MEMBERS.TEAM_ID)).join(USERS).on(STUDY_TEAM_MEMBERS.USER_ID.eq(USERS.ID))
                .where(USERS.LOGIN.eq(userName)).fetchOne();
        return team == null ? null : team.value1();
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
    public void updateStudentTeam(String userName, String team) {
        dsl.mergeInto(STUDY_TEAM_MEMBERS, STUDY_TEAM_MEMBERS.USER_ID, STUDY_TEAM_MEMBERS.TEAM_ID)
                .key(STUDY_TEAM_MEMBERS.USER_ID)
                .values(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName)).asField(),
                        dsl.select(STUDY_TEAMS.ID).from(STUDY_TEAMS).where(STUDY_TEAMS.NAME.eq(team)).asField())
                .execute();
    }

    @Override
    public void createStudentGroup(String name) {
        dsl.insertInto(STUDY_GROUPS, STUDY_GROUPS.NAME).values(name).execute();
    }

    @Override
    public void createStudentTeam(String name) {
        dsl.insertInto(STUDY_TEAMS, STUDY_TEAMS.NAME).values(name).execute();
    }

    @Override
    public void renameStudentGroup(String name, String newName) {
        dsl.update(STUDY_GROUPS)
                .set(STUDY_GROUPS.NAME, newName).where(STUDY_GROUPS.NAME.eq(name)).execute();
    }

    @Override
    public void renameStudentTeam(String name, String newName) {
        dsl.update(STUDY_TEAMS)
                .set(STUDY_TEAMS.NAME, newName).where(STUDY_TEAMS.NAME.eq(name)).execute();
    }
}
