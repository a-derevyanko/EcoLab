package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TextField;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.server.model.DomainModel;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class NewLabeledItemWindow<T extends DomainModel> extends NewItemWindow<String, T> {
    // ---------------------------- Графические компоненты --------------------
    protected final TextField label = new TextField();

    public NewLabeledItemWindow(I18N i18N) {
        super(i18N);
    }

    @PostConstruct
    @Override
    protected void init() {
        super.init();
        content.addComponent(label, 0);
    }

    @Override
    protected String getValue() {
        return label.getValue();
    }

    @Override
    protected void beforeShow() {
        label.clear();
    }
}
