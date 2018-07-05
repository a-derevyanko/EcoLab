package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.StudentGroup;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.function.Consumer;

@SpringComponent
@UIScope
public class AddTeacherGroupsWindow extends BaseEkoLabWindow<AddTeacherGroupsWindow.AddTeacherGroupsWindowSettings> {
    // ---------------------------- Графические компоненты --------------------
    protected final FormLayout content = new FormLayout();
    protected final Button ok = new Button("Ok", event -> save());
    protected final Button cancel = new Button("Cancel", event -> close());
    protected final Grid<StudentGroup> groups = new Grid<>();

    protected final HorizontalLayout actions = new HorizontalLayout(ok, cancel);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    protected final I18N i18N;

    protected AddTeacherGroupsWindow(I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    public void init() {
        setContent(content);
        setCaption(i18N.get("teacher-view.add-groups"));
        ok.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        content.setSizeUndefined();
        setResizable(false);
        content.setMargin(true);

        actions.setSpacing(true);

        content.addComponents(groups, actions);
        groups.addColumn(StudentGroup::getName).setCaption(i18N.get("teacher-view.add-groups.name"));
        groups.setSelectionMode(Grid.SelectionMode.MULTI);

        cancel.setCaption(i18N.get("confirm.cancel"));

        center();
    }

    protected void save() {
        settings.selectedGroups.accept(groups.getSelectedItems());
        close();
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();

        groups.setItems(settings.chooserGroups);
    }

    public static class AddTeacherGroupsWindowSettings implements WindowSettings {
        private final Set<StudentGroup> chooserGroups;
        private final Consumer<Set<StudentGroup>> selectedGroups;

        public AddTeacherGroupsWindowSettings(Set<StudentGroup> chooserGroups, Consumer<Set<StudentGroup>> selectedGroups) {
            this.chooserGroups = chooserGroups;
            this.selectedGroups = selectedGroups;
        }
    }
}
