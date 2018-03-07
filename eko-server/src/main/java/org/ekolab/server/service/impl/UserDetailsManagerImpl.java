package org.ekolab.server.service.impl;

import com.ibm.icu.text.Transliterator;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import org.apache.commons.lang3.time.FastDateFormat;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.UserInfoService;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
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
    private static final Transliterator TRANSLITERATOR = Transliterator.getInstance("Any-Latin; Lower; Latin-ASCII");

    private static final RecordMapper<Record, UserInfo> USER_INFO_RECORD_MAPPER = record -> {
        UserInfo info = new UserInfo();
        info.setId(record.get(USERS.ID));
        info.setLogin(record.get(USERS.LOGIN));
        info.setFirstName(record.get(USERS.FIRST_NAME));
        info.setMiddleName(record.get(USERS.MIDDLE_NAME));
        info.setLastName(record.get(USERS.LAST_NAME));
        info.setNote(record.get(USERS.NOTE));
        info.setGroup(UserGroup.valueOf(record.get(GROUPS.GROUP_NAME)));
        return info;
    };

    private final UserCache userCache;

    private final JdbcTemplate jdbcTemplate;

    private final DSLContext dsl;

    private final PasswordEncoder passwordEncoder;

    private final ReportService reportService;

    private final MessageSource messageSource;

    @Autowired
    public UserDetailsManagerImpl(UserCache userCache, JdbcTemplate jdbcTemplate, DSLContext dsl, PasswordEncoder passwordEncoder, ReportService reportService, MessageSource messageSource) {
        this.userCache = userCache;
        this.jdbcTemplate = jdbcTemplate;
        this.dsl = dsl;
        this.passwordEncoder = passwordEncoder;
        this.reportService = reportService;
        this.messageSource = messageSource;
    }

    @PostConstruct
    public void init() {
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
        return dsl.select(USERS.ID, USERS.LOGIN, USERS.FIRST_NAME, USERS.MIDDLE_NAME, USERS.LAST_NAME, USERS.NOTE, GROUPS.GROUP_NAME).from(USERS)
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

    /**
     * Создаёт данные пользователя.
     * Заполнять логин необязательно - логином назначается фамилия пользователя.
     * При выборе логина выполняется проверка, что такого логине не существует.
     * @param userInfo данные пользователя
     */
    @Override
    @CachePut(cacheNames = "USER", key = "#result.login")
    public UserInfo createUserInfo(UserInfo userInfo) {
        if (userInfo.getLogin() == null) {
            String newLogin = TRANSLITERATOR.transform(userInfo.getLastName());
            List<String> sameUsers = dsl.select(USERS.LOGIN).from(USERS).where(USERS.LOGIN.startsWith(newLogin)).fetchInto(String.class);

            if (sameUsers.contains(newLogin)) {
                for (int i = 1; i < Integer.MAX_VALUE; i++) {
                    if (!sameUsers.contains(newLogin + i)) {
                        newLogin = newLogin + i;
                        break;
                    }
                }
            }

            userInfo.setLogin(newLogin);
        }

        userInfo.setId(dsl.insertInto(USERS)
                .set(USERS.FIRST_NAME, userInfo.getFirstName())
                .set(USERS.MIDDLE_NAME, userInfo.getMiddleName())
                .set(USERS.LAST_NAME, userInfo.getLastName())
                .set(USERS.NOTE, userInfo.getNote())
                .set(USERS.LOGIN, userInfo.getLogin())
                .set(USERS.ENABLED, true)
                .set(USERS.PASSWORD, passwordEncoder.encode(userInfo.getLogin()))
                .returning(USERS.ID).fetchOne().getId());

        dsl.insertInto(GROUP_MEMBERS)
                .set(GROUP_MEMBERS.GROUP_ID, dsl.select(GROUPS.ID).from(GROUPS).where(GROUPS.GROUP_NAME.eq(userInfo.getGroup().name())))
                .set(GROUP_MEMBERS.USER_ID, userInfo.getId()).execute();
        return userInfo;
    }

    @LogExecutionTime(500)
    @Override
    public byte[] printUsersData(Stream<UserInfo> users, Locale locale) {
        DRDataSource dataSource = new DRDataSource("firstName", "lastName", "login", "password");

        users.forEach(value -> dataSource.add(value.getFirstName(), value.getLastName(), value.getLogin()));

        JasperReportBuilder builder = report()
                .setTemplate(reportService.getReportTemplate(locale)).
                        title(reportService.createTitleComponent(
                                messageSource.getMessage("report.user.title", null, locale) + " (" +
                                        FastDateFormat.getInstance("dd.MM.yyyy HH:mm").format(new Date()) +')'));

        TextColumnBuilder<String> firstNameColumn = col.column(messageSource.
                getMessage("report.user.firstName", null, locale), "firstName", type.stringType());
        TextColumnBuilder<String> lastNameColumn = col.column(messageSource.
                getMessage("report.user.lastName", null, locale), "lastName", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        TextColumnBuilder<String> loginColumn = col.column(messageSource.
                getMessage("report.user.login", null, locale), "login", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        TextColumnBuilder<String> passwordColumn = col.column(messageSource.
                getMessage("report.user.password", null, locale), "login", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        return reportService.printReport(
                builder.columns(firstNameColumn, lastNameColumn, loginColumn, passwordColumn)
                        .setDataSource(dataSource));
    }
}
