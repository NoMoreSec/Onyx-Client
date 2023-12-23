package net.onyx.client.events.render;

import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderPortalOverlayEvent extends Event {
    public float nauseaStrength;
    public CallbackInfo ci;

    public RenderPortalOverlayEvent(float nauseaStrength, CallbackInfo ci) {
        this.nauseaStrength = nauseaStrength;
        this.ci = ci;
    }
}
