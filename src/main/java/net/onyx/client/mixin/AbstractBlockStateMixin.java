package net.onyx.client.mixin;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.GetAmbientOcclusionLightLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(at = @At("RETURN"),
        method = {
            "getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"
        },
        cancellable = true
    )
    private void onGetAmbientOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetAmbientOcclusionLightLevelEvent(blockView, blockPos, cir));
    }
}
