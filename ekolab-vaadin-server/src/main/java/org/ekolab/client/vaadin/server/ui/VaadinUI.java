package org.ekolab.client.vaadin.server.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
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
@Theme(ValoTheme.THEME_NAME)
public class VaadinUI extends UI {
    private static final long serialVersionUID = -2988327335267095955L;
    private static final Logger LOG = LoggerFactory.getLogger(VaadinUI.class);

    @Autowired
    private final EkoLabNavigator navigator;

    @Autowired
    private final VaadinSecurity vaadinSecurity;

    @Autowired
    private final EkoLabToolBar toolBar;

    @Autowired
    private final ExceptionNotification exceptionNotification;

    @Autowired
    private final ViewContainerPanel viewContainer;

    // ----------------------------- Графические компоненты --------------------------------
    private final VerticalLayout root = new VerticalLayout();

    @Autowired
    public VaadinUI(EkoLabNavigator navigator, VaadinSecurity vaadinSecurity, EkoLabToolBar toolBar, ExceptionNotification exceptionNotification, ViewContainerPanel viewContainer) {
        this.navigator = navigator;
        this.vaadinSecurity = vaadinSecurity;
        this.toolBar = toolBar;
        this.exceptionNotification = exceptionNotification;
        this.viewContainer = viewContainer;
    }

    @Override
    protected void init(VaadinRequest request) {
        setLocale(Locale.getDefault());
        setErrorHandler((ErrorHandler) event -> {
            exceptionNotification.show(Page.getCurrent());
            LOG.error(event.getThrowable().getLocalizedMessage(), ExceptionUtils.getRootCause(event.getThrowable()));
        });
        setContent(root);

        root.setSizeFull();
        root.addComponents(toolBar, viewContainer);

        root.setExpandRatio(toolBar, 0.1f);
        root.setExpandRatio(viewContainer, 2.0f);

        navigator.addViewChangeListener(toolBar);

        Responsive.makeResponsive(this);
    }

    @Override
    public String toString() {
        return "VaadinUI{" +
                "navigator=" + navigator +
                ", vaadinSecurity=" + vaadinSecurity +
                ", toolBar=" + toolBar +
                ", exceptionNotification=" + exceptionNotification +
                ", root=" + root +
                ", viewContainer=" + viewContainer +
                 '}';
    }
}