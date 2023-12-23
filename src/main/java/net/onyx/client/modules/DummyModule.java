package net.onyx.client.modules;

import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;

/**
 * Dummy module for making modules be used more as flags than actual modules.
 */
public class DummyModule extends Module {

    public DummyModule(String name, boolean autoEnable) {
        super(name, autoEnable);
    }

    public DummyModule(String name) {
        super(name);
    }

    @Override
    public final void fireEvent(Event event) {
        OnyxClient.log(this.getName() + " fired event " + event.getClass().getSimpleName() + " when it shouldn't have! This is a bug!");
    }

    // These can be overridden but by default should do nothing

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }
    
}
