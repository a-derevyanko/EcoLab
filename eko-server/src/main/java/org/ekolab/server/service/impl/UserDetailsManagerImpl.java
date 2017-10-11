package org.ekolab.server.service.impl;

import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.AUTHORITIES;
import static org.ekolab.server.db.h2.public_.Tables.GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.GROUP_AUTHORITIES;
import static org.ekolab.server.db.h2.public_.Tables.GROUP_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.USERS;
import static org.ekolab.server.db.h2.public_.Tables.USER_AUTHORITIES;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Transactional
public class UserDetailsManagerImpl extends JdbcUserDetailsManager implements UserInfoService {
    private static final RecordMapper<Record, UserInfo> USER_INFO_RECORD_MAPPER = record -> {
        UserInfo info = new UserInfo();
        info.setLogin(record.get(USERS.LOGIN));
        info.setFirstName(record.get(USERS.FIRST_NAME));
        info.setMiddleName(record.get(USERS.MIDDLE_NAME));
        info.setLastName(record.get(USERS.LAST_NAME));
        info.setNote(record.get(USERS.NOTE));
        info.setGroup(UserGroup.valueOf(record.get(GROUPS.GROUP_NAME)));
        return info;
    };

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserCache userCache;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DSLContext dsl;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable("USER")
    public UserInfo getUserInfo(String userName) {
        return dsl.select(USERS.LOGIN, USERS.FIRST_NAME, USERS.MIDDLE_NAME, USERS.LAST_NAME, USERS.NOTE, GROUPS.GROUP_NAME).from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
        .where(USERS.LOGIN.eq(userName)).fetchOne().map(USER_INFO_RECORD_MAPPER);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
        super.changePassword(oldPassword, passwordEncoder.encode(newPassword));
    }

    @Override
    public void createUser(UserDetails userDetails) {
        User user = new User(userDetails.getUsername(),
                passwordEncoder.encode(userDetails.getPassword()), userDetails.getAuthorities());
        super.createUser(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = new User(userDetails.getUsername(),
                passwordEncoder.encode(userDetails.getPassword()), userDetails.getAuthorities());
        super.updateUser(user);
    }

    @Override
    @CachePut(cacheNames = "USER", key = "#userInfo.login")
    public UserInfo updateUserInfo(UserInfo userInfo) {
        dsl.update(USERS)
                .set(USERS.FIRST_NAME, userInfo.getFirstName())
                .set(USERS.MIDDLE_NAME, userInfo.getMiddleName())
                .set(USERS.LAST_NAME, userInfo.getLastName())
                .set(USERS.NOTE, userInfo.getNote()).where(USERS.LOGIN.eq(userInfo.getLogin())).execute();

        dsl.update(GROUP_MEMBERS).set(GROUP_MEMBERS.GROUP_ID,
                dsl.select(GROUPS.ID).from(GROUPS).where(GROUPS.GROUP_NAME.eq(userInfo.getGroup().name())))
                .where(GROUP_MEMBERS.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userInfo.getLogin())))).execute();
        return userInfo;
    }

    @Override
    @CachePut(cacheNames = "USER", key = "#userInfo.login")
    public UserInfo createUserInfo(UserInfo userInfo) {
        Long userID = dsl.insertInto(USERS)
                .set(USERS.FIRST_NAME, userInfo.getFirstName())
                .set(USERS.MIDDLE_NAME, userInfo.getMiddleName())
                .set(USERS.LAST_NAME, userInfo.getLastName())
                .set(USERS.NOTE, userInfo.getNote())
                .set(USERS.LOGIN, userInfo.getLogin())
                .set(USERS.ENABLED, true)
                .set(USERS.PASSWORD, passwordEncoder.encode(userInfo.getLogin()))
                .returning(USERS.ID).fetchOne().getId();

        dsl.insertInto(GROUP_MEMBERS)
                .set(GROUP_MEMBERS.GROUP_ID, dsl.select(GROUPS.ID).from(GROUPS).where(GROUPS.GROUP_NAME.eq(userInfo.getGroup().name())))
                .set(GROUP_MEMBERS.USER_ID, userID).execute();
        return userInfo;
    }
}
