import org.ekolab.client.vaadin.server.VaadinApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VaadinTestApplication extends VaadinApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinTestApplication.class);

    public static void main(String... args) {
        ApplicationContext ctx = VaadinApplication.run(args);
        LOGGER.info("-------------------------------------------------------");
        LOGGER.info("|     Vaadin Server metrics for test instance         |");
        LOGGER.info("|                  Beans count: {}                   |", ctx.getBeanDefinitionCount());
        LOGGER.info("-------------------------------------------------------");
    }
}