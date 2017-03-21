package org.ekolab.server.db.test;

import org.jooq.impl.DSL;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.ekolab.server.db.h2.information_schema.Tables.TABLES;

/**
 * Created by 777Al on 21.03.2017.
 */
public class DslTest {
    @Test
    public void testInformationSchema() throws Exception{
        Class.forName("org.h2.Driver");

        Assert.assertEquals(
                Arrays.asList("COLUMNS", "TABLES"),
                DSL.using("jdbc:h2:~/test-gradle", "sa", "")
                        .select(TABLES.TABLE_NAME)
                        .from(TABLES)
                        .where(TABLES.TABLE_NAME.in("COLUMNS", "TABLES"))
                        .and(TABLES.TABLE_SCHEMA.eq("INFORMATION_SCHEMA"))
                        .fetch(TABLES.TABLE_NAME)
        );
    }
}
