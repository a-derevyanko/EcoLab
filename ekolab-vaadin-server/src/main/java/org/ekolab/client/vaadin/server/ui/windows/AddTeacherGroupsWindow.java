package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.service.api.StudentInfoService;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringComponent
@UIScope
public class AddTeacherGroupsWindow extends BaseEkoLabWindow<AddTeacherGroupsWindow.AddTeacherGroupsWindowSettings> {
    // ---------------------------- Графические компоненты --------------------
    private final Button ok = new Button("OK", event -> save());
    private final Button addNewGroup = new Button("New");
    private final Label existsLabel = new Label("Exists");
    private final Grid<StudentGroup> groups = new Grid<>();

    private final HorizontalLayout actions = new HorizontalLayout(ok, addNewGroup);
    private final VerticalLayout content = new VerticalLayout(existsLabel,groups, actions);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    private final I18N i18N;
    private final NewNamedEntityWindow newNamedEntityWindow;
    private final StudentInfoService studentInfoService;

    private AddTeacherGroupsWindow(I18N i18N, NewNamedEntityWindow newNamedEntityWindow, StudentInfoService studentInfoService) {
        this.i18N = i18N;
        this.newNamedEntityWindow = newNamedEntityWindow;
        this.studentInfoService = studentInfoService;
    }

    @PostConstruct
    public void init() {
        setContent(content);
        setCaption(i18N.get("teacher-view.add-groups"));
        ok.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        content.setSizeUndefined();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setResizable(false);
        content.setMargin(true);

        actions.setSpacing(true);

        existsLabel.setValue(i18N.get("teacher-view.add-groups.existing"));
        groups.addColumn(StudentGroup::getName).setCaption(i18N.get("teacher-view.add-groups.name"));
        groups.setSelectionMode(Grid.SelectionMode.MULTI);

        addNewGroup.addClickListener(event -> newNamedEntityWindow.show(new NewNamedEntityWindow.NamedEntityWindowSettings(
                i18N.get("student-data.add-group"),
                s -> {
                    StudentGroup group = studentInfoService.createStudentGroup(s);
                    refreshItems();
                    groups.getSelectionModel().select(group);
                }
        )));
        center();
    }

    protected void save() {
        groups.getSelectedItems().forEach(group -> studentInfoService.addGroupToTeacher(settings.teacher, group));
        settings.onSave.run();
        close();
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        refreshItems();
    }

    private void refreshItems() {
        Set<StudentGroup> studentGroups = studentInfoService.getStudentGroups();
        studentGroups.removeAll(studentInfoService.getTeacherGroups(settings.teacher));
        groups.setItems(studentGroups);
    }

    public static class AddTeacherGroupsWindowSettings implements WindowSettings {
        private final String teacher;
        private final Runnable onSave;

        public AddTeacherGroupsWindowSettings(String teacher, Runnable onSave) {
            this.teacher = teacher;
            this.onSave = onSave;
        }
    }
}
