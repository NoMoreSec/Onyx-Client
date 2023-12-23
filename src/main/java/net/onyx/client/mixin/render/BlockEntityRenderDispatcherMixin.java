package net.onyx.client.mixin.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.BlockEntityRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    public void onRender(BlockEntity blockEntity, float tickDelta, MatrixStack matrix, VertexConsumerProvider arg3, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new BlockEntityRenderEvent(blockEntity, tickDelta, matrix, arg3, ci));
    }
}
