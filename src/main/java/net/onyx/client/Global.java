package net.onyx.client;

import net.minecraft.client.MinecraftClient;
import net.onyx.client.modules.Module;
import net.onyx.client.onyxevent.EventManager;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Global {

    MinecraftClient mc = MinecraftClient.getInstance();
    OnyxClient wc = OnyxClient.getInstance();
    Supplier<EventManager> events = wc::getEventManager;
    Supplier<List<Module>> modules = () -> wc.getModules().values().stream().toList();

    default <T extends Module> T getModule(Class<T> moduleClass) {
        return wc.getModule(moduleClass);
    }

    default <T extends Module> void executeModule(Class<T> moduleClass, Consumer<T> action) {
        T m = getModule(moduleClass);

        if (m != null) {
            action.accept(m);
        }
    }

    default <T extends Module, R> R getFrom(Class<T> moduleClass, Function<T, R> getter) {
        return wc.getFrom(moduleClass, getter);
    }

    default <T extends Module> boolean isEnabled(Class<T> moduleClass) {
        return wc.isEnabled(moduleClass);
    }
}
