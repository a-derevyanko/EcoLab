package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringView(name = AdminManagingView.NAME)
@Profile(Profiles.MODE.PROD)
public class AdminManagingView extends VerticalLayout implements View {
    public static final String NAME = "admin";

    @Autowired
    protected I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    protected final Grid teachers = new Grid();

    @Override
    public void init() throws Exception {
        View.super.init();
    }
}
