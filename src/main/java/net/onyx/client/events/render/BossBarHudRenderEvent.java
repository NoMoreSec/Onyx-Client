package net.onyx.client.events.render;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class BossBarHudRenderEvent extends Event {
    public CallbackInfo ci;

    public BossBarHudRenderEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
