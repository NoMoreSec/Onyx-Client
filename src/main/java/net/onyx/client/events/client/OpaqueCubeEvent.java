package net.onyx.client.events.client;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OpaqueCubeEvent extends Event {
    public CallbackInfo ci;

    public OpaqueCubeEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
