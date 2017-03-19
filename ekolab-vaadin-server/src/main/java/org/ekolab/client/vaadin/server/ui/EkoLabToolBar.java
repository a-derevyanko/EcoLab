package org.ekolab.client.vaadin.server.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.clock.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * При изменении VIEW меняются кнопки в тулбаре.
 */
@UIScope
@SpringComponent
public class EkoLabToolBar extends HorizontalLayout implements ViewChangeListener, ClickListener {
    @Autowired
    private final VaadinSecurity vaadinSecurity;

    @Autowired
    private final I18N i18N;

    @Autowired
    private final EkoLabNavigator navigator;

    // ---------------------- Данные экземпляра ----------------------------------

    // ---------------------- Графические компоненты -----------------------------
    private final Button settingsButton = new Button(VaadinIcons.COG);
    private final Button exitButton = new Button(VaadinIcons.CLOSE);
    private final Button editButton = new Button(VaadinIcons.PENCIL);
    private final Button management = new Button(VaadinIcons.COG);
    private final Clock clock = new Clock();
    private final HorizontalLayout leftButtonPanel = new HorizontalLayout();
    private final HorizontalLayout centerPanel = new HorizontalLayout(clock);
    private final HorizontalLayout rightButtonPanel = new HorizontalLayout(editButton, management, exitButton);

    @Autowired
    public EkoLabToolBar(VaadinSecurity vaadinSecurity, I18N i18N, EkoLabNavigator navigator) {
        this.vaadinSecurity = vaadinSecurity;
        this.i18N = i18N;
        this.navigator = navigator;
        setWidth(100.0F, Unit.PERCENTAGE);
        setMargin(false);
        prepareButton(settingsButton, "toolbar.settings");
        prepareButton(exitButton, "toolbar.exit");
        prepareButton(editButton, "toolbar.edit");
        prepareButton(management, "toolbar.management");
        clock.setWidth(400.0F, Unit.PIXELS);
        addComponents(leftButtonPanel, centerPanel, rightButtonPanel);
        setComponentAlignment(leftButtonPanel, Alignment.MIDDLE_LEFT);
        setComponentAlignment(centerPanel, Alignment.MIDDLE_CENTER);
        setComponentAlignment(rightButtonPanel, Alignment.MIDDLE_RIGHT);
        setExpandRatio(leftButtonPanel, 1.0F);
        setExpandRatio(centerPanel, 0.5F);
        setExpandRatio(rightButtonPanel, 1.0F);
    }

    /**
     * Устанавливает различные свойства, стили и лейбл кнопки.
     * @param button кнопка.
     * @param captionProperty ключ ресурса, содержащего лейбл кнопки.
     */
    private void prepareButton(Button button, String captionProperty) {
        button.addClickListener(this);
        button.setCaption(i18N.get(captionProperty));
    }

    // ------------------------------ Реализация обработчиков событий ----------------
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        boolean showButtons = true;//!navigator.isLoginView();
        leftButtonPanel.forEach(component -> component.setVisible(showButtons));
        rightButtonPanel.forEach(component -> component.setVisible(showButtons));
        //editButton.setVisible(navigator.isCurrentViewEditable());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        /*if (event.getButton() == settingsButton) {
            //TODO перемещаем на вид настроек
        } else if (event.getButton() == exitButton) {
            vaadinSecurity.logout();
        } else if (event.getButton() == editButton) {
            if (navigator.getCurrentEditableView().switchEditMode()) {
                editButton.setIcon(FontAwesome.SAVE);
                editButton.setCaption(i18N.get("toolbar.apply"));
            } else {
                editButton.setIcon(FontAwesome.PENCIL_SQUARE_O);
                editButton.setCaption(i18N.get("toolbar.edit"));
            }
        }*/
    }
}
