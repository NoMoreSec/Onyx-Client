package net.onyx.client.events.io;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnMouseButtonEvent extends Event {
    public long window;

    public int button;
    public int action;
    public int mods;

    public CallbackInfo ci;

    public OnMouseButtonEvent(long window, int button, int action, int mods, CallbackInfo ci) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods   = mods;
        this.ci     = ci;
    }
}
