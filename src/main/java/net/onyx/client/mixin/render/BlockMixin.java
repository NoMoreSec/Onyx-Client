package net.onyx.client.mixin.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.GetVelocityMultiplierEvent;
import net.onyx.client.events.render.ShouldDrawBlockSideEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin implements ItemConvertible {
	@Inject(at = {@At("RETURN")}, 
        method = {
            "shouldDrawSide(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;)Z"
        },
		cancellable = true
    )
	private static void onShouldDrawSide(BlockState state, BlockView blockView, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new ShouldDrawBlockSideEvent(state, blockView, pos, side, blockPos, cir));
	}

    @Inject(at = @At("HEAD"), method = "getVelocityMultiplier()F", cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetVelocityMultiplierEvent(cir));
    }
}