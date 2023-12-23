package net.onyx.client.events.packet;

import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnWorldTimeUpdateEvent extends Event {
    public CallbackInfo ci;
    public WorldTimeUpdateS2CPacket packet;

    public OnWorldTimeUpdateEvent(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        this.ci = ci;
        this.packet = packet;
    }
}
