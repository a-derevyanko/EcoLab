package org.ekolab.server;

import org.jooq.DSLContext;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.*;

import static org.ekolab.server.db.h2.public_.Tables.AUTHORITIES;
import static org.ekolab.server.db.h2.public_.Tables.USERS;

/**
 * Created by Андрей on 19.11.2016.
 */
@SpringBootConfiguration
public class ServerSecurityContext {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(JdbcTemplate jdbcTemplate) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setJdbcTemplate(jdbcTemplate);
        //todo переопределить sql выражения
        return tokenRepository;
    }

    @Bean
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
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
                                                 PersistentTokenRepository tokenRepository) {
        return new PersistentTokenBasedRememberMeServices(AbstractRememberMeServices.DEFAULT_PARAMETER,
                userDetailsService, tokenRepository);
    }

    @Bean
    public UserDetailsService userDetailsService(UserCache userCache, JdbcTemplate jdbcTemplate, DSLContext dsl) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setJdbcTemplate(jdbcTemplate);
        userDetailsManager.setUserCache(userCache);
        userDetailsManager.setCreateUserSql(dsl.insertInto(USERS, USERS.LOGIN, USERS.FIRST_NAME, USERS.MIDDLE_NAME,
                USERS.LAST_NAME, USERS.NOTE, USERS.PASSWORD, USERS.ENABLED).
                values("", "", "", "", "", "", true).getSQL());
        userDetailsManager.setDeleteUserSql(dsl.deleteFrom(USERS).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setUpdateUserSql(dsl.update(USERS).set(USERS.PASSWORD, "").set(USERS.ENABLED, true).getSQL());
        String s = dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq("")).getSQL();
        userDetailsManager.setCreateAuthoritySql(dsl.insertInto(AUTHORITIES, AUTHORITIES.USER_ID, AUTHORITIES.AUTHORITY).
                values(USERS.ID.cast(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq("")).getSQL(), "").getSQL());
        userDetailsManager.setChangePasswordSql(dsl.update(USERS).set(USERS.PASSWORD, "").where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setUsersByUsernameQuery(dsl.select(USERS.LOGIN, USERS.PASSWORD, USERS.ENABLED).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setAuthoritiesByUsernameQuery(dsl.select(USERS.LOGIN, AUTHORITIES.AUTHORITY).
                from(AUTHORITIES).join(USERS).on(USERS.ID.eq(AUTHORITIES.USER_ID)).where(USERS.LOGIN.eq("")).getSQL());
        userDetailsManager.setUserExistsSql(dsl.select(USERS.LOGIN).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        //userDetailsManager.setFindAllGroupsSql(dsl.select(LOGIN).from(USERS).where(USERS.LOGIN.eq("")).getSQL());
        return userDetailsManager;
    }

    public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority "
            + "from groups g, group_members gm, group_authorities ga "
            + "where gm.username = ? " + "and g.id = ga.group_id "
            + "and g.id = gm.group_id";


    public static final String DEF_DELETE_USER_AUTHORITIES_SQL = "delete from authorities where username = ?";

    // GroupManager SQL
    public static final String DEF_FIND_GROUPS_SQL = "select group_name from groups";
    public static final String DEF_FIND_USERS_IN_GROUP_SQL = "select username from group_members gm, groups g "
            + "where gm.group_id = g.id" + " and g.group_name = ?";
    public static final String DEF_INSERT_GROUP_SQL = "insert into groups (group_name) values (?)";
    public static final String DEF_FIND_GROUP_ID_SQL = "select id from groups where group_name = ?";
    public static final String DEF_INSERT_GROUP_AUTHORITY_SQL = "insert into group_authorities (group_id, authority) values (?,?)";
    public static final String DEF_DELETE_GROUP_SQL = "delete from groups where id = ?";
    public static final String DEF_DELETE_GROUP_AUTHORITIES_SQL = "delete from group_authorities where group_id = ?";
    public static final String DEF_DELETE_GROUP_MEMBERS_SQL = "delete from group_members where group_id = ?";
    public static final String DEF_RENAME_GROUP_SQL = "update groups set group_name = ? where group_name = ?";
    public static final String DEF_INSERT_GROUP_MEMBER_SQL = "insert into group_members (group_id, username) values (?,?)";
    public static final String DEF_DELETE_GROUP_MEMBER_SQL = "delete from group_members where group_id = ? and username = ?";
    public static final String DEF_GROUP_AUTHORITIES_QUERY_SQL = "select g.id, g.group_name, ga.authority "
            + "from groups g, group_authorities ga "
            + "where g.group_name = ? "
            + "and g.id = ga.group_id ";
    public static final String DEF_DELETE_GROUP_AUTHORITY_SQL = "delete from group_authorities where group_id = ? and authority = ?";

}
