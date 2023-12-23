package net.onyx.client.utils;

import net.onyx.client.onyxevent.CancellableEvent;
import net.onyx.client.onyxevent.Event;
import net.onyx.client.onyxevent.EventManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public enum MixinUtils {
    ;

    public static void fireEvent(Event<?> event) {
        EventManager.fire(event);
    }

    public static void fireCancellable(CancellableEvent<?> event, CallbackInfo ci) {
        EventManager.fire(event);
        if (event.isCancelled() && ci.isCancellable())
            ci.cancel();
    }
}
