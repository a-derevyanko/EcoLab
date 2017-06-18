package electronvaadin.app;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonString;
import org.ekolab.client.vaadin.server.ui.VaadinUI;

import static com.vaadin.shared.ui.ContentMode.PREFORMATTED;

@PreserveOnRefresh
@Theme(ValoTheme.THEME_NAME)
public class DesktopUI extends VaadinUI {
    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
        initElectronApi();
    }

    private void initElectronApi() {
        JavaScript js = getPage().getJavaScript();
        js.addFunction("appMenuItemTriggered", arguments -> {
            if (arguments.length() == 1 && arguments.get(0) instanceof JsonString) {
                String menuId = arguments.get(0).asString();
                if ("About".equals(menuId)) {
                    onMenuAbout();
                } else if ("Exit".equals(menuId)) {
                    onWindowExit();
                }
            }
        });
        js.addFunction("appWindowExit", arguments -> onWindowExit());
    }

    private void callElectronUiApi(String[] args) {
        JsonArray paramsArray = Json.createArray();
        int i = 0;
        for (String arg : args) {
            paramsArray.set(i, Json.create(arg));
            i++;
        }
        getPage().getJavaScript().execute("callElectronUiApi(" + paramsArray.toJson() + ")");
    }

    private void onMenuAbout() {
        Window helpWindow = new Window();
        helpWindow.setCaption("About");
        helpWindow.setModal(true);
        helpWindow.setResizable(false);

        helpWindow.setSizeUndefined();

        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.setMargin(true);
        content.setSpacing(true);

        Label aboutLabel = new Label("Electron+Vaadin Demo\nAuthor: Derevyanko ANdrey");
        aboutLabel.setContentMode(PREFORMATTED);
        aboutLabel.setSizeUndefined();

        content.addComponent(aboutLabel);

        Button okBtn = new Button("Ok", VaadinIcons.CHECK);
        okBtn.focus();
        okBtn.addClickListener(event -> helpWindow.close());

        content.addComponent(okBtn);
        content.setComponentAlignment(okBtn, Alignment.MIDDLE_CENTER);

        helpWindow.setContent(content);

        getUI().addWindow(helpWindow);
    }

    private void onWindowExit() {
        if (!getUI().getWindows().isEmpty()) {
            // it seems that confirmation window is already shown
            return;
        }

        Window confirmationWindow = new Window();
        confirmationWindow.setResizable(false);
        confirmationWindow.setModal(true);
        confirmationWindow.setCaption("Exit confirmation");
        confirmationWindow.setSizeUndefined();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidthUndefined();

        Label confirmationText = new Label("Are you sure?");
        confirmationText.setSizeUndefined();
        layout.addComponent(confirmationText);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        Button yesBtn = new Button("Yes", VaadinIcons.SIGN_OUT);
        yesBtn.focus();
        yesBtn.addClickListener(event -> {
            confirmationWindow.close();
            callElectronUiApi(new String[]{"exit"});
        });
        buttonsLayout.addComponent(yesBtn);

        Button noBtn = new Button("No", VaadinIcons.CLOSE);
        noBtn.addClickListener(event -> confirmationWindow.close());
        buttonsLayout.addComponent(noBtn);

        layout.addComponent(buttonsLayout);

        confirmationWindow.setContent(layout);

        getUI().addWindow(confirmationWindow);
    }
}