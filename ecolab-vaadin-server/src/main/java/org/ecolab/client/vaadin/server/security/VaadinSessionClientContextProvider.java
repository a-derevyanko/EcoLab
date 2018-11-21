package org.ecolab.client.vaadin.server.security;

import com.vaadin.server.VaadinSession;
import org.ecolab.server.model.ClientContext;
import org.ecolab.server.model.ClientContextProvider;

public class VaadinSessionClientContextProvider implements ClientContextProvider {
    public static final ClientContextProvider INSTANCE = new VaadinSessionClientContextProvider();
    @Override
    public ClientContext getClientContext() {
        VaadinSession session = VaadinSession.getCurrent();
        return session == null ? null : (ClientContext) session.getAttribute("client");
    }
}
