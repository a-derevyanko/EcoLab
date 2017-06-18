package electronvaadin.server;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import electronvaadin.app.DesktopUI;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/desktop")
@VaadinServletConfiguration(productionMode = false, ui = DesktopUI.class)
public class AppServlet extends VaadinServlet implements BootstrapListener {
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener((SessionInitListener) event ->
                event.getSession().addBootstrapListener(this)
        );
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
    }

    /**
     * Obtain electron UI API end point
     */
    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Element head = response.getDocument().getElementsByTag("head").get(0);
        Element script = response.getDocument().createElement("script");
        script.attr("type", "text/javascript");
        script.text("let {remote} = require('electron');\n" +
                    "window.callElectronUiApi = remote.getGlobal(\"callElectronUiApi\");");
        head.appendChild(script);
    }
}