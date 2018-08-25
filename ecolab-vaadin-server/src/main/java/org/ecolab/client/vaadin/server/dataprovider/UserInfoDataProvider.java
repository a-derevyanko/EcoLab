package org.ecolab.client.vaadin.server.dataprovider;

import com.vaadin.data.provider.Query;
import org.ecolab.server.model.UserInfo;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static org.ecolab.server.db.h2.public_.Tables.GROUPS;
import static org.ecolab.server.db.h2.public_.Tables.GROUP_MEMBERS;
import static org.ecolab.server.db.h2.public_.Tables.USERS;

@Service
public class UserInfoDataProvider extends BaseUserInfoDataProvider<UserInfoFilter> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoDataProvider.class);

    protected UserInfoDataProvider(DSLContext dsl) {
        super(dsl);
    }

    /**
     * Возвращает данные пользователей, подходящих под фильтр
     * @param query фильтр
     * @return данные пользователей
     */
    @Override
    protected Stream<UserInfo> fetchFromBackEnd(Query<UserInfo, UserInfoFilter> query) {
        Stream<UserInfo> stream = dsl.select(USERS.LOGIN, USERS.FIRST_NAME, USERS.MIDDLE_NAME, USERS.LAST_NAME, USERS.NOTE, GROUPS.GROUP_NAME).from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
                .where(getConditions(query)).limit(query.getLimit()).offset(query.getOffset()).fetch().map(USER_INFO_RECORD_MAPPER).stream();

        return query.getInMemorySorting() == null ? stream : stream.sorted(query.getInMemorySorting());
    }

    @Override
    protected int sizeInBackEnd(Query<UserInfo, UserInfoFilter> query) {
        return dsl.selectCount().from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
                .where(getConditions(query)).fetchOneInto(Integer.class);
    }

}
