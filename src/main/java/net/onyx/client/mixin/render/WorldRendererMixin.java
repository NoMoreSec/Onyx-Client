package net.onyx.client.mixin.render;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.RenderEntityEvent;
import net.onyx.client.events.render.RenderWorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method="renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    public void renderEntity(net.minecraft.entity.Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack mStack, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new RenderEntityEvent(entity, cameraX, cameraY, cameraZ, tickDelta, mStack, vertexConsumers, ci));
    }

    @Inject(at = @At("RETURN"), method="render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", cancellable = true)
    public void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager arg4, Matrix4f arg5, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new RenderWorldEvent(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, arg4, arg5, ci));
    }
}
