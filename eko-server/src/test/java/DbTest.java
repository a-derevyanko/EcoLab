import org.ekolab.server.DslConfiguration;
import org.ekolab.server.ServerApplication;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.CommandLineArgs;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlSuite;

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
