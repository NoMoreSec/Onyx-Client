package net.onyx.client.events.render;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BossBarHudSkyEffectsEvent extends Event {
    public CallbackInfoReturnable<Boolean> cir;

    public BossBarHudSkyEffectsEvent(CallbackInfoReturnable<Boolean> cir) {
        this.cir = cir;
    }
}
