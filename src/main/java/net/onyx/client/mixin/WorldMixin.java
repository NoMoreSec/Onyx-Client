package net.onyx.client.mixin;

import net.minecraft.world.World;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.GetRainGradientEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(at = @At("RETURN"), method = "getRainGradient(F)F", cancellable = true)
    private void onIsRaining(float delta, CallbackInfoReturnable<Float> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetRainGradientEvent(delta, cir));
    }
}
