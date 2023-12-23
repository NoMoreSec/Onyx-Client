package net.onyx.client.events.packet;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PreMovementPacketEvent extends Event {
    public CallbackInfo ci;

    public PreMovementPacketEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
