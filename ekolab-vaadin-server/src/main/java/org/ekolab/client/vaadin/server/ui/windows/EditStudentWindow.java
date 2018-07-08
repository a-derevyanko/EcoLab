package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class EditStudentWindow extends EditUserWindow<StudentDataWindowSettings> {
    // ---------------------------- Графические компоненты --------------------

    public EditStudentWindow(I18N i18N, UserInfoService userInfoService) {
        super(i18N,  userInfoService);
    }

    // ------------------------------------ Данные экземпляра -------------------------------------------

    @PostConstruct
    public void init() {
        super.init();
    }
}
