package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.StudentTeam;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class EditStudentWindow extends EditUserWindow<StudentDataWindowSettings> {
    private final Binder<StudentInfo> studentInfoBinder;
    // ---------------------------- Графические компоненты --------------------
    private final ComboBox<StudentGroup> studentGroupComboBox = new ComboBox<>();
    private final ComboBox<StudentTeam> studentTeamComboBox = new ComboBox<>();

    public EditStudentWindow(I18N i18N, Binder<UserInfo> userInfoBinder,
                             UserInfoService userInfoService, Binder<StudentInfo> studentInfoBinder) {
        super(i18N, userInfoBinder, userInfoService);
        this.studentInfoBinder = studentInfoBinder;
    }

    // ------------------------------------ Данные экземпляра -------------------------------------------

    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    protected void save() {
        settings.getStudentInfoConsumer().accept(studentInfoBinder.getBean());
        super.save();
    }
}
