package org.ekolab.client.vaadin.server.ui.customcomponents.virtualkeyboard;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.shared.Registration;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// This is the server-side UI component that provides public API 
// for VirtualKeyboard
@JavaScript({"virtualkeyboard.js", "keyboards.js"})
@StyleSheet("keyboard.css")
public class VirtualKeyboard extends AbstractJavaScriptComponent {
    @FunctionalInterface
    public interface KeyListener {
        void keyPress(KeyEvent event);
    }

    public static class KeyEvent extends Event {
        private final String key;

        public KeyEvent(Component source, String key) {
            super(source);
            this.key = key;
        }

        public String getKeyChar() {
            return key;
        }
    }

    @FunctionalInterface
    public interface KeyClickRpc extends ServerRpc {
        void onKeyClick(String s);
    }

    private final Map<AbstractTextField, Registration> focusListeners = new HashMap<>();

    private Window keyboardWindow;
    private final boolean isFloatingWindow;
    private AbstractTextField focusedTextField;

    public VirtualKeyboard(boolean isFloatingWindow) {
        this.isFloatingWindow = isFloatingWindow;
        registerRpc((KeyClickRpc) s -> {
            if (focusedTextField != null) {
                int curPos = focusedTextField.getCursorPosition();
                String oldText = focusedTextField.getValue();

                if (Objects.equals("\b", s) && curPos > 0) {
                    focusedTextField.setValue(oldText.substring(0, curPos - 1) + oldText.substring(curPos, oldText.length()));
                    focusedTextField.setCursorPosition(curPos - 1);
                } else if (Objects.equals("\n", s) && (focusedTextField instanceof TextField || focusedTextField instanceof PasswordField)) {
                    //focusedTextField.setCursorPosition(curPos);
                } else {
                    focusedTextField.setValue(oldText.substring(0, curPos) + s + oldText.substring(curPos, oldText.length()));
                    focusedTextField.setCursorPosition(curPos + 1);
                }
                focusedTextField.focus();
            }
        });
    }


    public void attachComponent(final AbstractTextField component) {
        focusListeners.put(component, component.addFocusListener((FocusListener) event -> {
            focusedTextField = (AbstractTextField) event.getComponent();
            if(isFloatingWindow) {
                if(keyboardWindow == null) {
                    keyboardWindow = getWindow();
                }

                if (keyboardWindow.getParent() == null) {
                    component.getUI().addWindow(keyboardWindow);
                }
                keyboardWindow.setVisible(true);
            }
        }));
    }

    public void detachComponent(final AbstractTextField component) {
        if (focusListeners.containsKey(component)) {
            focusListeners.remove(component).remove();
        }
    }

    public Window getWindow() {
        if (!isFloatingWindow) {
            return null;
        }

        if (keyboardWindow == null) {
            keyboardWindow = new Window();
            keyboardWindow.setCaption(/*i18N.get(*/"Virtual Keyboard"/*, null, Locale.getDefault())*/);
            keyboardWindow.setVisible(false);
            keyboardWindow.setContent(this);
            keyboardWindow.setResizable(false);
        }
        return keyboardWindow;
    }
}
