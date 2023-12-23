package net.onyx.client.events.render;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class GetSkinTextureEvent extends Event {
    public PlayerEntity player;
    public CallbackInfoReturnable<Identifier> cir;

    public GetSkinTextureEvent(PlayerEntity player, CallbackInfoReturnable<Identifier> cir) {
        this.player = player;
        this.cir = cir;
    }
}
