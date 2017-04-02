package org.ekolab.client.vaadin.server.ui;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.AlignmentInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.view.BaseView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static com.vaadin.ui.Alignment.MIDDLE_LEFT;

/**
 * При изменении VIEW меняются кнопки в тулбаре.
 */
@UIScope
@SpringComponent
public class EkoLabToolBar extends HorizontalLayout implements ViewChangeListener {
    @Autowired
    private I18N i18N;

    @Autowired
    private EkoLabNavigator navigator;

    // ---------------------- Данные экземпляра ----------------------------------

    // ---------------------- Графические компоненты -----------------------------
    private final HorizontalLayout leftButtonPanel = new HorizontalLayout();
    private final HorizontalLayout rightButtonPanel = new HorizontalLayout();

    @PostConstruct
    protected void init() {
        setWidth(100.0F, Unit.PERCENTAGE);
        setMargin(false);
        addComponents(leftButtonPanel, /*centerPanel,*/ rightButtonPanel);
        setComponentAlignment(leftButtonPanel, MIDDLE_LEFT);
        //setComponentAlignment(centerPanel, Alignment.MIDDLE_CENTER);
        setComponentAlignment(rightButtonPanel, Alignment.MIDDLE_RIGHT);
        setExpandRatio(leftButtonPanel, 1.0F);
        //setExpandRatio(centerPanel, 0.5F);
        setExpandRatio(rightButtonPanel, 1.0F);
    }

    /**
     * Устанавливает различные свойства, стили и лейбл кнопки.
     * @param button кнопка.
     * @param captionProperty ключ ресурса, содержащего лейбл кнопки.
     */
    public void addButton(Button button, String captionProperty, AlignmentInfo alignment) {
        button.setCaption(i18N.get(captionProperty));
        if (alignment == AlignmentInfo.LEFT) {
            leftButtonPanel.addComponent(button);
        } else if (alignment == AlignmentInfo.RIGHT) {
            rightButtonPanel.addComponent(button);
        } else {
            throw new IllegalArgumentException("Unknown alignment");
        }
    }

    // ------------------------------ Реализация обработчиков событий ----------------
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        ((BaseView) event.getNewView()).placeToolBarActions(this);
    }
}
