package org.ekolab.client.vaadin.server;

import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.vaadin.spring.servlet.Vaadin4SpringServlet;

/**
 * Created by 777Al on 15.04.2017.
 */
@SpringComponent
public class VaadinServlet extends Vaadin4SpringServlet {
    @Autowired
    private MessageSource messageSource;

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
