package org.ekolab.client.vaadin.server.dataprovider;

import com.vaadin.data.provider.Query;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserInfo;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static org.ekolab.server.db.h2.public_.Tables.GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.GROUP_MEMBERS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_GROUPS;
import static org.ekolab.server.db.h2.public_.Tables.STUDY_TEAMS;
import static org.ekolab.server.db.h2.public_.Tables.USERS;

@Service
public class StudentInfoDataProvider extends BaseUserInfoDataProvider<StudentInfoFilter> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentInfoDataProvider.class);

    protected StudentInfoDataProvider(DSLContext dsl) {
        super(dsl);
    }


    /**
     * Возвращает данные пользователей, подходящих под фильтр
     * @param query фильтр
     * @return данные пользователей
     */
    @Override
    protected Stream<UserInfo> fetchFromBackEnd(Query<UserInfo, StudentInfoFilter> query) {
        Stream<UserInfo> stream = dsl.select(USERS.LOGIN, USERS.FIRST_NAME, USERS.MIDDLE_NAME, USERS.LAST_NAME, USERS.NOTE, GROUPS.GROUP_NAME).from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
                .where(getConditions(query)).limit(query.getLimit()).offset(query.getOffset()).fetch().map(USER_INFO_RECORD_MAPPER).stream();

        return query.getInMemorySorting() == null ? stream : stream.sorted(query.getInMemorySorting());
    }

    @Override
    protected int sizeInBackEnd(Query<UserInfo, StudentInfoFilter> query) {
        return dsl.selectCount().from(USERS)
                .join(GROUP_MEMBERS).on(GROUP_MEMBERS.USER_ID.eq(USERS.ID)).join(GROUPS).on(GROUP_MEMBERS.GROUP_ID.eq(GROUPS.ID))
                .where(getConditions(query)).fetchOneInto(Integer.class);
    }

    @Override
    protected List<Condition> getConditions(Query<UserInfo, StudentInfoFilter> query) {
        List<Condition> conditions = super.getConditions(query);

        if (query.getFilter().isPresent()) {
            StudentInfo filter = query.getFilter().get().getStudentInfoFilter();
            if (filter.getGroup() != null) {
                conditions.add(STUDY_GROUPS.ID.eq(filter.getGroup().getId()));
            }

            if (filter.getTeam() != null) {
                conditions.add(STUDY_TEAMS.ID.eq(filter.getTeam().getId()));
            }
        }
        return conditions;
    }
}
