package net.onyx.client.mixin.client;

import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.AddParticleEmitterEvent;
import net.onyx.client.events.render.AddParticleEvent;
import net.onyx.client.interfaces.mixin.IParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements IParticleManager {
    @Inject(at = @At("HEAD"), method = "addEmitter", cancellable = true)
    private void onAddEmitter(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new AddParticleEmitterEvent(ci));
    }

    @Inject(at = @At("HEAD"), method = "addParticle", cancellable = true)
    private void onAddParticle(CallbackInfoReturnable<Particle> ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new AddParticleEvent(ci));
    }

    @Shadow private Queue<EmitterParticle> newEmitterParticles;
    @Shadow private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Override
    public void clearAll() {
        this.newEmitterParticles.clear();
        this.particles.clear();
    }
}
