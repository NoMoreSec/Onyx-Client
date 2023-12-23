package net.onyx.client.events.packet;

import net.minecraft.network.Packet;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SendPacketEvent extends Event {
    public CallbackInfo ci;
    public Packet<?> packet;

    public SendPacketEvent(Packet<?> packet, CallbackInfo ci) {
        this.ci = ci;
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
