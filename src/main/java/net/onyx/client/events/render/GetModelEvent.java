package net.onyx.client.events.render;

import net.minecraft.entity.player.PlayerEntity;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class GetModelEvent extends Event {
    public PlayerEntity player;
    public CallbackInfoReturnable<String> cir;

    public GetModelEvent(PlayerEntity player, CallbackInfoReturnable<String> cir) {
        this.player = player;
        this.cir = cir;
    }
}
