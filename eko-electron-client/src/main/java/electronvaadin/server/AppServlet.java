package electronvaadin.server;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import electronvaadin.app.MainUI;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * @author Yuriy Artamonov
 */
@WebServlet(value = "/desktop")
@VaadinServletConfiguration(productionMode = false, ui = MainUI.class)
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

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        // Obtain electron UI API end point
        Element head = response.getDocument().getElementsByTag("head").get(0);
        Element script = response.getDocument().createElement("script");
        script.attr("type", "text/javascript");
        script.text("let {remote} = require('electron');\n" +
                    "window.callElectronUiApi = remote.getGlobal(\"callElectronUiApi\");");
        head.appendChild(script);
    }
}