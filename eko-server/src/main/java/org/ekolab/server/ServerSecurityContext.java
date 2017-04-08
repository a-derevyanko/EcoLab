package org.ekolab.server;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.*;

import static org.ekolab.server.db.h2.public_.Tables.AUTHORITIES;
import static org.ekolab.server.db.h2.public_.Tables.GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.GROUP_AUTHORITIES;
import static org.ekolab.server.db.h2.public_.Tables.GROUP_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.USERS;

/**
 * Created by Андрей on 19.11.2016.
 */
@SpringBootConfiguration
public class ServerSecurityContext {
    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    @Lazy
    public AuthenticationProvider authenticationProvider(UserCache userCache,
                                                         PasswordEncoder passwordEncoder,
                                                         UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserCache(userCache);
        return authenticationProvider;
    }

    @Bean
    @Lazy
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
                                                 PersistentTokenRepository tokenRepository) {
        return new PersistentTokenBasedRememberMeServices(AbstractRememberMeServices.DEFAULT_PARAMETER,
                userDetailsService, tokenRepository);
    }

    @Bean
    @Lazy
    public UserDetailsManager userDetailsManager(AuthenticationManager authenticationManager,
                                                 UserCache userCache, JdbcTemplate jdbcTemplate, DSLContext dsl) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setAuthenticationManager(authenticationManager);
        userDetailsManager.setJdbcTemplate(jdbcTemplate);
        userDetailsManager.setUserCache(userCache);
        userDetailsManager.setCreateUserSql(dsl.insertInto(USERS, USERS.LOGIN, USERS.PASSWORD, USERS.ENABLED).
                values("", "", true).getSQL());
        userDetailsManager.setDeleteUserSql(dsl.deleteFrom(USERS).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setUpdateUserSql(dsl.update(USERS).set(USERS.PASSWORD, "").set(USERS.ENABLED, true).getSQL());
        userDetailsManager.setCreateAuthoritySql(
                dsl.insertInto(AUTHORITIES, AUTHORITIES.USER_ID, AUTHORITIES.AUTHORITY).
                        select(dsl.select(USERS.ID, DSL.val("").as(AUTHORITIES.AUTHORITY)).from(USERS).where(USERS.LOGIN.eq(""))).getSQL());
        userDetailsManager.setChangePasswordSql(dsl.update(USERS).set(USERS.PASSWORD, "").where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setUsersByUsernameQuery(dsl.select(USERS.LOGIN, USERS.PASSWORD, USERS.ENABLED).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setAuthoritiesByUsernameQuery(dsl.select(USERS.LOGIN, AUTHORITIES.AUTHORITY).
                from(AUTHORITIES).join(USERS).on(USERS.ID.eq(AUTHORITIES.USER_ID)).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setUserExistsSql(dsl.select(USERS.LOGIN).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setFindAllGroupsSql(dsl.select(GROUPS.GROUP_NAME).from(GROUPS).getSQL());
        userDetailsManager.setDeleteUserAuthoritiesSql(dsl.deleteFrom(AUTHORITIES).where(
                AUTHORITIES.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq("")))).getSQL());
        userDetailsManager.setGroupAuthoritiesByUsernameQuery(dsl.select(GROUPS.ID, GROUPS.GROUP_NAME, GROUP_AUTHORITIES.AUTHORITY)
                .from(GROUPS.join(GROUP_AUTHORITIES).on(GROUP_AUTHORITIES.GROUP_ID.eq(GROUPS.ID)).join(GROUP_MEMBERS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID)))
                .where(GROUP_MEMBERS.USER_ID.eq(dsl.select(USERS.LOGIN).from(USERS).where(USERS.LOGIN.eq("")).asField())).getSQL());
        userDetailsManager.setFindUsersInGroupSql(dsl.select(USERS.LOGIN).from(USERS).join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID))
                .join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID)).where(GROUPS.GROUP_NAME.eq("")).getSQL());
        userDetailsManager.setInsertGroupSql(dsl.insertInto(GROUPS, GROUPS.GROUP_NAME).values("").getSQL());
        userDetailsManager.setFindGroupIdSql(dsl.select(GROUPS.ID).from(GROUPS).where(GROUPS.GROUP_NAME.eq("")).getSQL());
        userDetailsManager.setInsertGroupAuthoritySql(dsl.insertInto(GROUP_AUTHORITIES, GROUP_AUTHORITIES.GROUP_ID, GROUP_AUTHORITIES.AUTHORITY).
                values(1L, "").getSQL());
        userDetailsManager.setDeleteGroupSql(dsl.deleteFrom(GROUPS).where(GROUPS.ID.eq(1L)).getSQL());
        userDetailsManager.setDeleteGroupAuthoritiesSql(dsl.deleteFrom(GROUP_AUTHORITIES).where(GROUPS.ID.eq(1L)).getSQL());
        userDetailsManager.setDeleteGroupMembersSql(dsl.deleteFrom(GROUP_MEMBERS).where(GROUPS.ID.eq(1L)).getSQL());
        userDetailsManager.setRenameGroupSql(dsl.update(GROUPS).set(GROUPS.GROUP_NAME, "").where(GROUPS.GROUP_NAME.eq("")).getSQL());
        userDetailsManager.setInsertGroupSql(dsl.insertInto(GROUP_MEMBERS, GROUP_MEMBERS.GROUP_ID, GROUP_MEMBERS.USER_ID)
                .select(dsl.select(DSL.val(1L), USERS.ID).from(USERS).where(USERS.LOGIN.eq(""))).getSQL());
        userDetailsManager.setDeleteGroupMemberSql(dsl.deleteFrom(GROUP_MEMBERS).where(GROUPS.ID.eq(1L)
                .and(GROUP_MEMBERS.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(""))))).getSQL());
        userDetailsManager.setDeleteGroupAuthoritySql(dsl.deleteFrom(GROUP_AUTHORITIES).where(GROUPS.ID.eq(1L)
                .and(GROUP_AUTHORITIES.AUTHORITY.eq(""))).getSQL());
        userDetailsManager.setGroupAuthoritiesSql(dsl.select(GROUPS.ID, GROUPS.GROUP_NAME, GROUP_AUTHORITIES.AUTHORITY).
                from(GROUPS).join(GROUP_AUTHORITIES).on(GROUPS.ID.eq(GROUP_AUTHORITIES.GROUP_ID)).where(GROUPS.GROUP_NAME.eq("")).getSQL());
        return userDetailsManager;
    }
}
