package org.ekolab.server.service.impl;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.*;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Transactional
public class UserDetailsManagerImpl extends JdbcUserDetailsManager {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserCache userCache;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DSLContext dsl;


    @PostConstruct
    public void init() {
        setAuthenticationManager(authenticationManager);
        setJdbcTemplate(jdbcTemplate);
        setUserCache(userCache);
        setEnableGroups(true);
        setCreateUserSql(dsl.insertInto(USERS, USERS.LOGIN, USERS.PASSWORD, USERS.ENABLED).
                values("", "", true).getSQL());
        setDeleteUserSql(dsl.deleteFrom(USERS).where(USERS.LOGIN.eq("")).getSQL());
        setUpdateUserSql(dsl.update(USERS).set(USERS.PASSWORD, "").set(USERS.ENABLED, true).getSQL());
        setCreateAuthoritySql(
                dsl.insertInto(USER_AUTHORITIES)
                        .set(USER_AUTHORITIES.USER_ID, dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq("")))
                        .set(USER_AUTHORITIES.AUTHORITY_ID, dsl.select(AUTHORITIES.ID).from(AUTHORITIES).where(AUTHORITIES.AUTHORITY_NAME.eq(""))).
                        getSQL());
        setChangePasswordSql(dsl.update(USERS).set(USERS.PASSWORD, "").where(USERS.LOGIN.eq("")).getSQL());
        setUsersByUsernameQuery(dsl.select(USERS.LOGIN, USERS.PASSWORD, USERS.ENABLED).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        setAuthoritiesByUsernameQuery(dsl.select(USERS.LOGIN, AUTHORITIES.AUTHORITY_NAME).
                from(USER_AUTHORITIES).join(USERS).on(USERS.ID.eq(USER_AUTHORITIES.USER_ID)).join(AUTHORITIES).on(AUTHORITIES.ID.eq(USER_AUTHORITIES.AUTHORITY_ID)).where(USERS.LOGIN.eq("")).getSQL());
        setUserExistsSql(dsl.select(USERS.LOGIN).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        setFindAllGroupsSql(dsl.select(GROUPS.GROUP_NAME).from(GROUPS).getSQL());
        setDeleteUserAuthoritiesSql(dsl.deleteFrom(USER_AUTHORITIES).where(
                USER_AUTHORITIES.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq("")))).getSQL());
        setGroupAuthoritiesByUsernameQuery(dsl.select(GROUPS.ID, GROUPS.GROUP_NAME, AUTHORITIES.AUTHORITY_NAME)
                .from(GROUPS.join(GROUP_AUTHORITIES).on(GROUP_AUTHORITIES.GROUP_ID.eq(GROUPS.ID)).join(AUTHORITIES).on(AUTHORITIES.ID.eq(GROUP_AUTHORITIES.AUTHORITY_ID)).join(GROUP_MEMBERS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID)))
                .where(GROUP_MEMBERS.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq("")).asField())).getSQL());
        setFindUsersInGroupSql(dsl.select(USERS.LOGIN).from(USERS).join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID))
                .join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID)).where(GROUPS.GROUP_NAME.eq("")).getSQL());
        setInsertGroupSql(dsl.insertInto(GROUPS, GROUPS.GROUP_NAME).values("").getSQL());
        setFindGroupIdSql(dsl.select(GROUPS.ID).from(GROUPS).where(GROUPS.GROUP_NAME.eq("")).getSQL());
        setInsertGroupAuthoritySql(dsl.insertInto(GROUP_AUTHORITIES)
                .set(GROUP_AUTHORITIES.GROUP_ID, dsl.select(GROUPS.ID).from(GROUPS).where(GROUPS.GROUP_NAME.eq("")))
                .set(GROUP_AUTHORITIES.AUTHORITY_ID, dsl.select(AUTHORITIES.ID).from(AUTHORITIES).where(AUTHORITIES.AUTHORITY_NAME.eq(""))).getSQL());
        setDeleteGroupSql(dsl.deleteFrom(GROUPS).where(GROUPS.ID.eq(1L)).getSQL());
        setDeleteGroupAuthoritiesSql(dsl.deleteFrom(GROUP_AUTHORITIES).where(GROUPS.ID.eq(1L)).getSQL());
        setDeleteGroupMembersSql(dsl.deleteFrom(GROUP_MEMBERS).where(GROUPS.ID.eq(1L)).getSQL());
        setRenameGroupSql(dsl.update(GROUPS).set(GROUPS.GROUP_NAME, "").where(GROUPS.GROUP_NAME.eq("")).getSQL());
        setInsertGroupSql(dsl.insertInto(GROUP_MEMBERS, GROUP_MEMBERS.GROUP_ID, GROUP_MEMBERS.USER_ID)
                .select(dsl.select(DSL.val(1L), USERS.ID).from(USERS).where(USERS.LOGIN.eq(""))).getSQL());
        setDeleteGroupMemberSql(dsl.deleteFrom(GROUP_MEMBERS).where(GROUPS.ID.eq(1L)
                .and(GROUP_MEMBERS.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(""))))).getSQL());
        setDeleteGroupAuthoritySql(dsl.deleteFrom(GROUP_AUTHORITIES).where(GROUPS.ID.eq(1L)
                .and(GROUP_AUTHORITIES.AUTHORITY_ID.eq(dsl.select(AUTHORITIES.ID).from(AUTHORITIES).where(AUTHORITIES.AUTHORITY_NAME.eq(""))))).getSQL());
        setGroupAuthoritiesSql(dsl.select(GROUPS.ID, GROUPS.GROUP_NAME, AUTHORITIES.AUTHORITY_NAME).
                from(GROUPS).join(GROUP_AUTHORITIES).on(GROUPS.ID.eq(GROUP_AUTHORITIES.GROUP_ID)).join(AUTHORITIES).on(AUTHORITIES.ID.eq(GROUP_AUTHORITIES.AUTHORITY_ID)).where(GROUPS.GROUP_NAME.eq("")).getSQL());

        super.initDao();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected List<UserDetails> loadUsersByUsername(String username) {
        return dsl.fetch(getUsersByUsernameQuery(), username).map(record -> {
            String username1 = record.getValue(0, String.class);
            String password = record.getValue(1, String.class);
            boolean enabled = record.getValue(2, Boolean.class);
            return new User(username1, password, enabled, true, true, true,
                    AuthorityUtils.NO_AUTHORITIES);
        });
    }
}
