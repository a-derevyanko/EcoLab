package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@UIScope
@PrototypeScope
public class ResourceWindow extends BaseEcoLabWindow<ResourceWindow.ResourceWindowSettings> {
    private final VerticalLayout content = new VerticalLayout();

    @Override
    protected void init() {
        setCaptionAsHtml(true);
        setContent(content);
        setHeight(85.0F, Unit.PERCENTAGE);
        setWidth(65.0F, Unit.PERCENTAGE);
        content.setSizeFull();
        center();
    }

    /**
     * Действия, выполняемые перед отображением окна
     */
    protected void beforeShow() {
        setCaption(settings.caption);
        setModal(settings.isModal);
        content.addComponent(settings.infoResource);
        settings.infoResource.setSizeFull();
    };

    /**
     * Очистка компонента
     */
    protected void clear() {
        content.removeAllComponents();
    };

    public static class ResourceWindowSettings implements WindowSettings {
        private final String caption;
        private final BrowserFrame infoResource;
        private final boolean isModal;

        public ResourceWindowSettings(String caption, BrowserFrame infoResource, boolean isModal) {
            this.caption = caption;
            this.infoResource = infoResource;
            this.isModal = isModal;
        }
    }
}
