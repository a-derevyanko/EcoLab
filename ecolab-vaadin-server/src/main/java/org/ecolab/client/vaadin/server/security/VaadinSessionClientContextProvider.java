package org.ecolab.client.vaadin.server.security;

import com.vaadin.server.VaadinSession;
import org.ecolab.server.model.ClientContext;
import org.ecolab.server.model.ClientContextProvider;
import org.springframework.security.core.Authentication;

public class VaadinSessionClientContextProvider implements ClientContextProvider {
    public static final VaadinSessionClientContextProvider INSTANCE = new VaadinSessionClientContextProvider();
    @Override
    public ClientContext getClientContext() {
        VaadinSession session = VaadinSession.getCurrent();
        return session == null ? null : (ClientContext) session.getAttribute("client");
    }

    public void setAuthentication(Authentication authentication) {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("client", authentication.getDetails());
    }
}
