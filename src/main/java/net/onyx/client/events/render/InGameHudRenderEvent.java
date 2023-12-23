package net.onyx.client.events.render;

import net.minecraft.client.util.math.MatrixStack;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class InGameHudRenderEvent extends Event {
    public MatrixStack mStack;
    public float tickDelta;
    public CallbackInfo ci;

    public InGameHudRenderEvent(MatrixStack mStack, float tickDelta, CallbackInfo ci) {
        this.mStack = mStack;
        this.tickDelta = tickDelta;
        this.ci = ci;
    }
}
