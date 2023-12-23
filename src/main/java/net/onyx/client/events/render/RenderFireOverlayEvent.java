package net.onyx.client.events.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderFireOverlayEvent extends Event {
    public MinecraftClient client;
    public MatrixStack mStack;
    public CallbackInfo ci;

    public RenderFireOverlayEvent(MinecraftClient client, MatrixStack mStack, CallbackInfo ci) {
        this.client = client;
        this.mStack = mStack;
        this.ci = ci;
    }
}
