package net.onyx.client.events.render;

import net.minecraft.client.util.math.MatrixStack;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class BobViewWhenHurtEvent extends Event {
    public MatrixStack mStack;
    public float f;
    public CallbackInfo ci;

    public BobViewWhenHurtEvent(MatrixStack mStack, float f, CallbackInfo ci) {
        this.mStack = mStack;
        this.f = f;
        this.ci = ci;
    }
}
