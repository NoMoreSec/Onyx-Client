package net.onyx.client.events.io;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnKeyEvent extends Event {
    public long window;
    public int key;
    public int scancode;
    public int action;
    public int modifiers;
    public CallbackInfo ci;

    public OnKeyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.modifiers = modifiers;
        this.ci = ci;
    }
}
