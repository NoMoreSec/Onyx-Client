package net.onyx.client.events.client;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class GetVelocityMultiplierEvent extends Event {
    public CallbackInfoReturnable<Float> cir;
    
    public GetVelocityMultiplierEvent(CallbackInfoReturnable<Float> cir) {
        this.cir = cir;
    }
}
