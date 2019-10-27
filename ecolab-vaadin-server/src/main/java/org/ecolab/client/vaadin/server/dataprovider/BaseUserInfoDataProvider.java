package org.ecolab.client.vaadin.server.dataprovider;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.model.UserInfo;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.ecolab.server.db.h2.public_.Tables.GROUPS;
import static org.ecolab.server.db.h2.public_.Tables.USERS;

public abstract class BaseUserInfoDataProvider<T extends UserInfoFilter> extends AbstractBackEndDataProvider<UserInfo, T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseUserInfoDataProvider.class);

    protected static final RecordMapper<Record, UserInfo> USER_INFO_RECORD_MAPPER = record -> {
        var info = new UserInfo();
        info.setLogin(record.get(USERS.LOGIN));
        info.setFirstName(record.get(USERS.FIRST_NAME));
        info.setMiddleName(record.get(USERS.MIDDLE_NAME));
        info.setLastName(record.get(USERS.LAST_NAME));
        info.setNote(record.get(USERS.NOTE));
        info.setGroup(UserGroup.valueOf(record.get(GROUPS.GROUP_NAME)));
        return info;
    };

    protected final DSLContext dsl;

    protected BaseUserInfoDataProvider(DSLContext dsl) {
        this.dsl = dsl;
    }

    protected List<Condition> getConditions(Query<UserInfo, T> query) {
        List<Condition> conditions = new ArrayList<>();

        if (query.getFilter().isPresent()) {
            var filter = query.getFilter().get().getUserInfoFilter();
            if (!StringUtils.isEmpty(filter.getLogin())) {
                conditions.add(USERS.LOGIN.lower().contains(filter.getLogin().toLowerCase()));
            }

            if (!StringUtils.isEmpty(filter.getFirstName())) {
                conditions.add(USERS.FIRST_NAME.lower().contains(filter.getFirstName().toLowerCase()));
            }

            if (!StringUtils.isEmpty(filter.getMiddleName())) {
                conditions.add(USERS.MIDDLE_NAME.lower().contains(filter.getMiddleName().toLowerCase()));
            }

            if (!StringUtils.isEmpty(filter.getLastName())) {
                conditions.add(USERS.LAST_NAME.lower().contains(filter.getLastName().toLowerCase()));
            }

            if (filter.getGroup() != null) {
                conditions.add(GROUPS.GROUP_NAME.eq(filter.getGroup().name()));
            }
        }
        return conditions;
    }

    @Override
    public Object getId(UserInfo item) {
        return item.getLogin();
    }
}
