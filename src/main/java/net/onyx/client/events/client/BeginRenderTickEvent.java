package net.onyx.client.events.client;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BeginRenderTickEvent extends Event {
    public long timeMillis;
    public CallbackInfoReturnable<Integer> ci;

    public BeginRenderTickEvent(long timeMillis, CallbackInfoReturnable<Integer> ci) {
        this.timeMillis = timeMillis;
        this.ci = ci;
    }
}
