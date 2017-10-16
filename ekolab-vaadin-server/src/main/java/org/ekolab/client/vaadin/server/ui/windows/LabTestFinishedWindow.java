package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.server.model.content.LabTestQuestionVariant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class LabTestFinishedWindow extends BaseEkoLabWindow<LabTestFinishedWindow.LabFinishedWindowSettings> {

    // ---------------------------- Графические компоненты --------------------
    private final Accordion testResultAccordion = new Accordion();
    private final Image testCompleted = new Image();
    private final Image testNotCompleted = new Image();
    private final HorizontalLayout result = new HorizontalLayout(testResultAccordion, testCompleted, testNotCompleted);
    private final Button toMainMenuButton = new Button("Exit to main menu", VaadinIcons.EXIT);
    private final VerticalLayout buttons = new VerticalLayout(toMainMenuButton);
    private final VerticalLayout content = new VerticalLayout(result, buttons);

    @Autowired
    private I18N i18N;

    @Autowired
    private EkoLabNavigator navigator;

    @Autowired
    private ResourceService resourceService;

    @PostConstruct
    public void init() {
        setCaption(i18N.get("labwizard.lab-finished"));
        setContent(content);
        setHeight(80.0F, Unit.PERCENTAGE);
        setWidth(80.0F, Unit.PERCENTAGE);
        setModal(true);
        testCompleted.setWidth(300.0F, Unit.PIXELS);
        testNotCompleted.setWidth(300.0F, Unit.PIXELS);
        testNotCompleted.setSource(resourceService.getImage("content/", EkoLabTheme.TEST_NOT_COMPLETED));

        result.setSizeFull();

        result.setComponentAlignment(testCompleted, Alignment.MIDDLE_CENTER);
        result.setComponentAlignment(testNotCompleted, Alignment.MIDDLE_CENTER);
        testResultAccordion.setWidth(600.0F, Unit.PIXELS);
        content.setSizeFull();
        content.setStyleName(EkoLabTheme.PANEL_TEST_FINISHED);
        content.setMargin(true);
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        content.setExpandRatio(result, 10.0F);
        content.setExpandRatio(buttons, 1.0F);
        buttons.setComponentAlignment(toMainMenuButton, Alignment.BOTTOM_RIGHT);

        toMainMenuButton.setCaption(i18N.get("labwizard.lab-finished.go-to-main-menu"));

        toMainMenuButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        toMainMenuButton.addStyleName(EkoLabTheme.BUTTON_TINY);

        toMainMenuButton.addClickListener(event -> close());
        center();
    }

    @Override
    public void close() {
        super.close();
        navigator.redirectToView(LabChooserView.NAME);
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        if (settings.errors.isEmpty()) {
            testCompleted.setVisible(true);
        } else {
            testNotCompleted.setVisible(true);
        }

        settings.questions.stream().
                sorted(Comparator.comparingInt(LabTestQuestionVariant::getNumber)).
                forEach(questionVariant -> testResultAccordion.addTab(new Label(questionVariant.getQuestion(), ContentMode.HTML),
                            i18N.get("test.question", questionVariant.getNumber())).setIcon(VaadinIcons.CHECK));

        settings.errors.forEach(tabIndex -> testResultAccordion.getTab(tabIndex - 1).setIcon(VaadinIcons.CLOSE));
    }

    @Override
    protected void clear() {
        super.clear();
        testCompleted.setVisible(false);
        testNotCompleted.setVisible(false);
        testResultAccordion.removeAllComponents();
    }

    public static class LabFinishedWindowSettings implements WindowSettings {
        private final List<Integer> errors;
        private final Collection<LabTestQuestionVariant> questions;

        public LabFinishedWindowSettings(List<Integer> errors, Collection<LabTestQuestionVariant> questions) {
            this.errors = errors;
            this.questions = new ArrayList<>(questions);
        }
    }
}
