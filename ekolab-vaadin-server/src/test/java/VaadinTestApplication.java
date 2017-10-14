import org.ekolab.client.vaadin.server.VaadinApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VaadinTestApplication extends VaadinApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinTestApplication.class);

    public static void main(String... args) throws Exception {
        new VaadinTestApplication().run(args);
    }
}