package net.onyx.client.events.render;

import net.minecraft.entity.Entity;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class IsEntityGlowingEvent extends Event {
    public CallbackInfoReturnable<Boolean> cir;
    public Entity entity;

    public IsEntityGlowingEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        this.entity = entity;
        this.cir = cir;
    }
}
