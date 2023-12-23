package net.onyx.client.events.packet;

import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnResourcePackSendEvent extends Event {
    public ResourcePackSendS2CPacket packet;
    public CallbackInfo ci;
    
    public OnResourcePackSendEvent(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        this.packet = packet;
        this.ci = ci;
    }
}
