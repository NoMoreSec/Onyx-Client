package net.onyx.client.events.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderNametagEvent extends Event {

    private final LivingEntity entity;
    private final MatrixStack matrices;
    private final VertexConsumerProvider vertexConsumers;
    public CallbackInfo ci;
    public boolean cancel = false;

    public RenderNametagEvent(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        this.entity = entity;
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
        this.ci = ci;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public VertexConsumerProvider getVertexConsumers() {
        return vertexConsumers;
    }
}
