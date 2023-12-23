package net.onyx.client.events.client;

import net.minecraft.text.Text;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnDisconnectedEvent extends Event {
    public Text reason;
    public CallbackInfo ci;

    public OnDisconnectedEvent(Text reason, CallbackInfo ci) {
        this.reason = reason;
        this.ci = ci;
    }
}
