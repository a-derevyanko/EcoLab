import org.ekolab.server.DslConfiguration;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * Created by 777Al on 16.03.2017.
 */

@SpringBootTest(classes = {DslConfiguration.class})
@EnableAutoConfiguration
@ActiveProfiles("test")
public class DbTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private DSLContext dsl;

    @Test
    public void testSave() {
        dsl.select();
    }
}
