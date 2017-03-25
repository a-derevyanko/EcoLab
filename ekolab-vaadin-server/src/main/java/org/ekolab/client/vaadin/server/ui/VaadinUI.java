package org.ekolab.client.vaadin.server.ui;

import com.vaadin.annotations.PreserveOnRefresh;
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
@PreserveOnRefresh
public class VaadinUI extends UI {
    private static final long serialVersionUID = -2988327335267095955L;
    private static final Logger LOG = LoggerFactory.getLogger(VaadinUI.class);

    @Autowired
    private EkoLabNavigator navigator;

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Autowired
    private EkoLabToolBar toolBar;

    @Autowired
    private ExceptionNotification exceptionNotification;

    @Autowired
    private ViewContainerPanel viewContainer;

    // ----------------------------- Графические компоненты --------------------------------
    private final VerticalLayout root = new VerticalLayout();

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