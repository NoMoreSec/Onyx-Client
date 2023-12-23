package net.onyx.client.components.plugins;
import net.onyx.client.events.Event;
import net.onyx.client.modules.Module;

public interface ModulePlugin {
    void addListeners(Module parentModule);
    void removeListeners(Module parentModule);
    boolean fireEvent(Event event);
}
