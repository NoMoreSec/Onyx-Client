package net.onyx.client.events.client;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class GetClientModNameEvent extends Event {
    public CallbackInfoReturnable<String> cir;

    public GetClientModNameEvent(CallbackInfoReturnable<String> cir) {
        this.cir = cir;
    }
}
