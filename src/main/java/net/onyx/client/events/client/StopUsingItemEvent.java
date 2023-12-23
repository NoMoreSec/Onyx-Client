package net.onyx.client.events.client;

import net.minecraft.entity.player.PlayerEntity;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StopUsingItemEvent extends Event {
    public PlayerEntity playerEntity;
    public CallbackInfo ci;
    
    public StopUsingItemEvent(PlayerEntity playerEntity, CallbackInfo ci) {
        this.playerEntity = playerEntity;
        this.ci = ci;
    }
}
