package org.ecolab.client.vaadin.server.ui.view;

import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.server.model.UserInfo;

/**
 * Калькулятор
 */
public class CalcManageView extends VerticalLayout implements View {
    protected final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    protected final Grid<UserInfo> calculations = new Grid<>();
    protected final Button newCalc = new Button("New calculation", VaadinIcons.PLUS_CIRCLE);
    protected final Button removeUser = new Button("Remove user", VaadinIcons.MINUS_CIRCLE);
    protected final Button editUser = new Button("Edit user", VaadinIcons.EDIT);
    protected final Button printUserLogins = new Button("Print calculations", VaadinIcons.PRINT);
    protected final VerticalLayout buttonsPanel = new VerticalLayout(editUser, newCalc, removeUser, printUserLogins);
    protected final HorizontalLayout gridPanel = new HorizontalLayout(buttonsPanel, calculations);

    public CalcManageView(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public void init() throws Exception {
        View.super.init();
        setMargin(true);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponents(gridPanel);

        gridPanel.setSizeFull();
        gridPanel.setExpandRatio(buttonsPanel, 1.0F);
        gridPanel.setExpandRatio(calculations, 3.0F);

        calculations.setSizeFull();
        calculations.setSelectionMode(Grid.SelectionMode.SINGLE);

        calculations.addSelectionListener((SelectionListener<UserInfo>) event -> {
            boolean hasSelection = event.getFirstSelectedItem().isPresent();
            editUser.setEnabled(hasSelection);
            removeUser.setEnabled(hasSelection);
        });

        calculations.addColumn(UserInfo::getLogin, new HtmlRenderer()).setCaption(i18N.get("userdata.login")).setExpandRatio(1);
        calculations.addColumn(UserInfo::getFirstName, new HtmlRenderer()).setCaption(i18N.get("userdata.firstname")).setExpandRatio(1);
        calculations.addColumn(UserInfo::getMiddleName, new HtmlRenderer()).setCaption(i18N.get("userdata.middlename")).setExpandRatio(1);
        calculations.addColumn(UserInfo::getLastName).setCaption(i18N.get("userdata.lastname")).setExpandRatio(1);
        calculations.addColumn(UserInfo::getNote).setCaption(i18N.get("userdata.note")).setExpandRatio(4);

        newCalc.setCaption(i18N.get("admin-manage.users.add-user"));
        newCalc.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        newCalc.setWidth(300.0F, Unit.PIXELS);
        newCalc.addClickListener(event ->
                {}
        );

        removeUser.setCaption(i18N.get("admin-manage.users.remove-user"));
        removeUser.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        removeUser.setEnabled(false);
        removeUser.setWidth(300.0F, Unit.PIXELS);
        removeUser.addClickListener((Button.ClickListener) event -> {

        });

        editUser.setCaption(i18N.get("admin-manage.users.edit-user"));
        editUser.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        editUser.setEnabled(false);
        editUser.setWidth(300.0F, Unit.PIXELS);
        editUser.addClickListener((Button.ClickListener) event -> {});

        printUserLogins.setCaption(i18N.get("admin-manage.users.print-table"));
        printUserLogins.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        printUserLogins.setWidth(300.0F, Unit.PIXELS);

    }
    
}
