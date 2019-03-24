package org.ecolab.client.vaadin.server;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.annotation.SpringComponent;
import javax.servlet.ServletException;
import org.ecolab.client.vaadin.server.security.VaadinSessionClientContextProvider;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.servlet.Vaadin4SpringServlet;

/**
 * Created by 777Al on 15.04.2017.
 */
@SpringComponent
public class VaadinServlet extends Vaadin4SpringServlet {
    private final MessageSource messageSource;
    private final EcoLabAuditService auditService;

    public VaadinServlet(MessageSource messageSource, EcoLabAuditService auditService) {
        this.messageSource = messageSource;
        this.auditService = auditService;
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        VaadinServletService servletService = super.createServletService(deploymentConfiguration);
        servletService.setSystemMessagesProvider((SystemMessagesProvider) systemMessagesInfo -> {
            CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();
            systemMessages.setSessionExpiredCaption(messageSource.getMessage("vaadin.session-expired", null, systemMessagesInfo.getLocale()));
            systemMessages.setSessionExpiredMessage(messageSource.getMessage("vaadin.server-connection-lost", null, systemMessagesInfo.getLocale()));
            systemMessages.setCommunicationErrorCaption(messageSource.getMessage("vaadin.session-expired", null, systemMessagesInfo.getLocale()));
            systemMessages.setCommunicationErrorMessage(messageSource.getMessage("vaadin.server-connection-lost", null, systemMessagesInfo.getLocale()));
            systemMessages.setAuthenticationErrorMessage(messageSource.getMessage("vaadin.server-connection-lost", null, systemMessagesInfo.getLocale()));
            return systemMessages;
        });
        return servletService;
    }

    @Override
    protected void servletInitialized() throws ServletException {
        getService().addSessionInitListener(this::onServletInit);
        getService().addSessionDestroyListener(this::onServletDestroy);
        super.servletInitialized();
    }

    private void onServletInit(SessionInitEvent sessionInitEvent) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            VaadinSessionClientContextProvider.INSTANCE.setAuthentication(authentication);

            auditService.log(EcoLabAuditEvent.ofType(EcoLabAuditEventType.LOGIN)
                    .attribute(EcoLabAuditEventAttribute.USER_NAME, authentication.getName()));
        }
    }

    private void onServletDestroy(SessionDestroyEvent sessionDestroyEvent) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            VaadinSessionClientContextProvider.INSTANCE.setAuthentication(authentication);

            EcoLabAuditEvent logoutAudit = EcoLabAuditEvent.ofType(EcoLabAuditEventType.LOGOUT);
            auditService.log(logoutAudit);
        }
    }
}
