package net.onyx.client.events.client;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PlayerChatEvent extends Event {
    public CallbackInfo ci;
    public String message;

    public PlayerChatEvent(String message, CallbackInfo ci) {
        this.ci = ci;
        this.message = message;
    }
}
