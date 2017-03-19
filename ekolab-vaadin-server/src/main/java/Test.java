import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.*;

/**
 * Created by Андрей on 18.03.2017.
 */
public class Test {
    public void f() {
        Wizard w = new Wizard();
        w.getBackButton().isDisableOnClick();
        w.setUriFragmentEnabled(true);
        w.addStep();
        w.addListener(new WizardProgressListener() {
            @Override
            public void activeStepChanged(WizardStepActivationEvent event) {

            }

            @Override
            public void stepSetChanged(WizardStepSetChangedEvent event) {

            }

            @Override
            public void wizardCompleted(WizardCompletedEvent event) {

            }

            @Override
            public void wizardCancelled(WizardCancelledEvent event) {

            }
        });
    }
}

