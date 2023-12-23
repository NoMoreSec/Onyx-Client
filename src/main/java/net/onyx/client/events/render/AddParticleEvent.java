package net.onyx.client.events.render;

import net.minecraft.client.particle.Particle;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AddParticleEvent extends Event {
    public CallbackInfoReturnable<Particle> ci;

    public AddParticleEvent(CallbackInfoReturnable<Particle> ci) {
        this.ci = ci;
    }
}
