package net.onyx.client.events.client;

import net.minecraft.entity.Entity;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class GetEntitiesEvent extends Event {
    public CallbackInfoReturnable<Iterable<Entity>> cir;
    
    public GetEntitiesEvent(CallbackInfoReturnable<Iterable<Entity>> cir) {
        this.cir = cir;
    }
}
