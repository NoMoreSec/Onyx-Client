package net.onyx.client.mixin.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.onyx.client.OnyxClient;
import net.onyx.client.modules.render.OptimalObiHighlight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public MixinPlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    //Code refactored from https://github.com/uku3lig/hitrange/blob/1.19.4/src/main/java/net/uku3lig/hitrange/mixin/MixinPlayerEntityRenderer.java ❤

    private final float height = 0.0f;

    private OptimalObiHighlight optimalObiHighlight() {
        return (OptimalObiHighlight) OnyxClient.getInstance().getModules().get("optimalobihighlight");
    }


    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void render(AbstractClientPlayerEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (OptimalObiHighlight.enabled) {
            matrices.push();

            VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getLines());
            Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
            Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

            float halfSize = 3f; // 3 blocks wide


            // Define the four corners of the square
            vertices.vertex(positionMatrix, -halfSize, height, -halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();
            vertices.vertex(positionMatrix, halfSize, height, -halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();

            vertices.vertex(positionMatrix, halfSize, height, -halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();
            vertices.vertex(positionMatrix, halfSize, height, halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();

            vertices.vertex(positionMatrix, halfSize, height, halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();
            vertices.vertex(positionMatrix, -halfSize, height, halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();

            vertices.vertex(positionMatrix, -halfSize, height, halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();
            vertices.vertex(positionMatrix, -halfSize, height, -halfSize).color(optimalObiHighlight().getIntSetting("Red"), optimalObiHighlight().getIntSetting("Green"), optimalObiHighlight().getIntSetting("Blue"), 255).normal(normalMatrix, 0.0f, 0.0f, 0.0f).next();

            matrices.pop();
        }
    }
}

