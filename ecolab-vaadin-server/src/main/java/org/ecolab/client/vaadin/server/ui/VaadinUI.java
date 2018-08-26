package org.ecolab.client.vaadin.server.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

import java.util.Locale;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringUI
@Title(EcoLabTheme.THEME_NAME)
@Widgetset("AppWidgetset")
@Theme(EcoLabTheme.THEME_NAME)
@PreserveOnRefresh
@Profile({Profiles.MODE.PROD, Profiles.MODE.DEMO})
public class VaadinUI extends UI {
    private static final long serialVersionUID = -2988327335267095955L;
    private static final Logger LOG = LoggerFactory.getLogger(VaadinUI.class);

    private final EcoLabNavigator navigator;

    private final EcoLabMenuBar menuBar;

    private final ExceptionNotification exceptionNotification;

    private final ViewContainerPanel viewContainer;

    protected final VaadinSharedSecurity vaadinSecurity;

    private final I18N i18N;

    // ----------------------------- Графические компоненты --------------------------------
    private final VerticalLayout root = new VerticalLayout();

    // ----------------------------- Данные экземпляра -------------------------------------
    private UserInfo currentUserInfo;

    private StudentInfo currentStudentInfo;

    @Autowired
    public VaadinUI(EcoLabNavigator navigator, EcoLabMenuBar menuBar, ExceptionNotification exceptionNotification, ViewContainerPanel viewContainer, VaadinSharedSecurity vaadinSecurity, I18N i18N) {
        this.navigator = navigator;
        this.menuBar = menuBar;
        this.exceptionNotification = exceptionNotification;
        this.viewContainer = viewContainer;
        this.vaadinSecurity = vaadinSecurity;
        this.i18N = i18N;
    }

    @Override
    protected void init(VaadinRequest request) {
        setLocale(Locale.getDefault());
        setErrorHandler(event -> exceptionNotification.show(Page.getCurrent(), event.getThrowable()));

        getLoadingIndicatorConfiguration().setThirdDelay(50000);

        root.setSizeFull();
        root.setMargin(false);
        root.setSpacing(false);
        root.addComponents(menuBar, viewContainer);
        setContent(root);
        viewContainer.setStyleName(EcoLabTheme.UI_WITH_MENU);

        root.setExpandRatio(viewContainer, 1.0f);

        navigator.addViewChangeListener(menuBar);

        Responsive.makeResponsive(this);

        UI.getCurrent().getReconnectDialogConfiguration().setDialogText(i18N.get("vaadin.server-connection-lost"));
        UI.getCurrent().getReconnectDialogConfiguration().setDialogTextGaveUp(i18N.get("vaadin.server-connection-lost-gave-up"));
    }

    public void setCurrentUserInfo(UserInfo currentUserInfo) {
        this.currentUserInfo = currentUserInfo;
    }

    public void setCurrentStudentInfo(StudentInfo currentStudentInfo) {
        this.currentStudentInfo = currentStudentInfo;
    }

    public UserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }

    public StudentInfo getCurrentStudentInfo() {
        return currentStudentInfo;
    }

    public static VaadinUI getCurrent() {
        return (VaadinUI) UI.getCurrent();
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