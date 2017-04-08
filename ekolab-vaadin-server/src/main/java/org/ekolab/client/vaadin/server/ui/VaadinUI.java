package org.ekolab.client.vaadin.server.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

import java.util.Locale;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringUI
@Title("EkoLab")
@Widgetset("AppWidgetset")
@Theme(EkoLabTheme.THEME_NAME)
@PreserveOnRefresh
public class VaadinUI extends UI {
    private static final long serialVersionUID = -2988327335267095955L;
    private static final Logger LOG = LoggerFactory.getLogger(VaadinUI.class);

    @Autowired
    private EkoLabNavigator navigator;

    @Autowired
    private EkoLabMenuBar menuBar;

    @Autowired
    private ExceptionNotification exceptionNotification;

    @Autowired
    private ViewContainerPanel viewContainer;

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Autowired
    private I18N i18N;

    // ----------------------------- Графические компоненты --------------------------------
    private final VerticalLayout root = new VerticalLayout();

    @Override
    protected void init(VaadinRequest request) {
        setLocale(Locale.getDefault());
        setErrorHandler(event -> exceptionNotification.show(Page.getCurrent(), event.getThrowable()));
        setContent(viewContainer);
        addStyleName(EkoLabTheme.UI_WITH_MENU);

        /*root.setSizeFull();
        root.addComponents(menuBar, viewContainer);

        root.setExpandRatio(menuBar, 0.1f);
        root.setExpandRatio(viewContainer, 2.0f);
*/
        navigator.addViewChangeListener(menuBar);

        Responsive.makeResponsive(this);

        VaadinService.getCurrent().setSystemMessagesProvider((SystemMessagesProvider) systemMessagesInfo -> {
            // todo переопределить системные значения
            Locale locale = systemMessagesInfo.getLocale();
            CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();

            // =====================================================================
            // Vaadin.SessionExpired
            /*String message = i18N.get("Vaadin.SessionExpired.URL", null, locale);
            systemMessages.setSessionExpiredURL(message);*/

            // .... and you can get the other messages from resource file .....

            return systemMessages;
        });
    }

    @Override
    public String toString() {
        return "VaadinUI{" +
                "navigator=" + navigator +
                ", vaadinSecurity=" + vaadinSecurity +
                ", menuBar=" + menuBar +
                ", exceptionNotification=" + exceptionNotification +
                ", root=" + root +
                ", viewContainer=" + viewContainer +
                 '}';
    }
}