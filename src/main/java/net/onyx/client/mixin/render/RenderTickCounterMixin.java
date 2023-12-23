package net.onyx.client.mixin.render;

import net.minecraft.client.render.RenderTickCounter;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.BeginRenderTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTickCounter.class)
public class RenderTickCounterMixin {
    @Inject(at = @At("HEAD"), method = "beginRenderTick(J)I", cancellable = true)
    private void onBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new BeginRenderTickEvent(timeMillis, ci));
    }
}
