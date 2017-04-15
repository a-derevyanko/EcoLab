package org.ekolab.client.vaadin.server;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinServletService;
import org.springframework.context.MessageSource;
import org.vaadin.spring.servlet.Vaadin4SpringServlet;

/**
 * Created by 777Al on 15.04.2017.
 */
public class CustomizedVaadinServlet extends Vaadin4SpringServlet {
    private final MessageSource messageSource;

    public CustomizedVaadinServlet(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        VaadinServletService servletService = super.createServletService(deploymentConfiguration);
        servletService.setSystemMessagesProvider((SystemMessagesProvider) systemMessagesInfo -> {
            CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();
            systemMessages.setSessionExpiredCaption(messageSource.getMessage("vaadin.session-expired", null, systemMessagesInfo.getLocale()));
            systemMessages.setSessionExpiredMessage(messageSource.getMessage("vaadin.server-connection-lost", null, systemMessagesInfo.getLocale()));
            systemMessages.setCommunicationErrorMessage(messageSource.getMessage("vaadin.server-connection-lost", null, systemMessagesInfo.getLocale()));
            systemMessages.setAuthenticationErrorMessage(messageSource.getMessage("vaadin.server-connection-lost", null, systemMessagesInfo.getLocale()));
            return systemMessages;
        });
        return servletService;
    }
}
