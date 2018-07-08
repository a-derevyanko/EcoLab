package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
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
    private final Button addNewGroup = new Button(VaadinIcons.PLUS_CIRCLE);
    private final Label existsLabel = new Label("Exists");
    private final Grid<StudentGroup> groups = new Grid<>();

    private final GridLayout content = new GridLayout(2, 3);

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

        content.setSizeFull();
        content.setSpacing(true);
        content.addComponent(groups, 0, 0,1,1);
        content.addComponent(ok, 0, 2,0,2);
        content.addComponent(addNewGroup, 1, 2,1,2);

        content.setComponentAlignment(addNewGroup, Alignment.MIDDLE_RIGHT);

        ok.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        content.setSizeUndefined();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setResizable(false);
        content.setMargin(true);

        existsLabel.setValue(i18N.get("teacher-view.add-groups.existing"));
        groups.addColumn(StudentGroup::getName).setCaption(i18N.get("teacher-view.add-groups.name"));
        groups.setSelectionMode(Grid.SelectionMode.MULTI);

        addNewGroup.setCaption(i18N.get("teacher-view.add-groups.create-new"));
        addNewGroup.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        addNewGroup.addClickListener(event -> newNamedEntityWindow.show(new NewNamedEntityWindow.NamedEntityWindowSettings(
                i18N.get("student-data.add-group"),
                s -> {
                    Set<StudentGroup> selected = groups.getSelectedItems();
                    selected.add(studentInfoService.createStudentGroup(s));
                    refreshItems();
                    selected.forEach(group -> groups.getSelectionModel().select(group));
                }
        )));
        center();
    }

    protected void save() {
        if (!groups.getSelectedItems().isEmpty()) {
            groups.getSelectedItems().forEach(group -> studentInfoService.addGroupToTeacher(settings.teacher, group));
            settings.onSave.run();
        }
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
