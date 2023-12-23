package net.onyx.client.events.packet;

import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnGameStateChangeEvent extends Event {
    public GameStateChangeS2CPacket packet;
    public CallbackInfo ci;

    public OnGameStateChangeEvent(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        this.packet = packet;
        this.ci = ci;
    }
}
