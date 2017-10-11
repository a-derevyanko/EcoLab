package org.ekolab.client.vaadin.server.dataprovider;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.ekolab.server.db.h2.public_.Tables.GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.GROUP_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.USERS;

@Service
public class UserInfoDataProvider extends AbstractBackEndDataProvider<UserInfo, UserInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoDataProvider.class);

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
    private final DSLContext dsl;

    public UserInfoDataProvider(DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Возвращает данные пользователей, подходящих под фильтр
     * @param query фильтр
     * @return данные пользователей
     */
    @Override
    protected Stream<UserInfo> fetchFromBackEnd(Query<UserInfo, UserInfo> query) {
        return dsl.select(USERS.LOGIN, USERS.FIRST_NAME, USERS.MIDDLE_NAME, USERS.LAST_NAME, USERS.NOTE, GROUPS.GROUP_NAME).from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
                .where(getConditions(query)).limit(query.getLimit()).offset(query.getOffset()).fetch().map(USER_INFO_RECORD_MAPPER).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<UserInfo, UserInfo> query) {
        return dsl.selectCount().from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
                .where(getConditions(query)).fetchOneInto(Integer.class);
    }

    private List<Condition> getConditions(Query<UserInfo, UserInfo> query) {
        List<Condition> conditions = new ArrayList<>();

        if (query.getFilter().isPresent()) {
            UserInfo filter = query.getFilter().get();
            if (!StringUtils.isEmpty(filter.getLogin())) {
                conditions.add(USERS.LOGIN.likeIgnoreCase(filter.getLogin()));
            }

            if (!StringUtils.isEmpty(filter.getFirstName())) {
                conditions.add(USERS.FIRST_NAME.likeIgnoreCase(filter.getFirstName()));
            }

            if (!StringUtils.isEmpty(filter.getMiddleName())) {
                conditions.add(USERS.MIDDLE_NAME.likeIgnoreCase(filter.getMiddleName()));
            }

            if (!StringUtils.isEmpty(filter.getLastName())) {
                conditions.add(USERS.LAST_NAME.likeIgnoreCase(filter.getLastName()));
            }

            if (filter.getGroup() != null) {
                conditions.add(GROUPS.GROUP_NAME.eq(filter.getGroup().name()));
            }
        }
        return conditions;
    }
}
