package net.onyx.client.events.packet;

import net.minecraft.network.Packet;
import net.onyx.client.events.Event;

public class ReceivePacketEvent extends Event {
    public Packet<?> packet;
    public boolean cancel = false;

    public ReceivePacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
