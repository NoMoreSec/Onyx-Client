package net.onyx.client.modules.packet;

import net.onyx.client.events.Event;
import net.onyx.client.events.packet.SendPacketEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.ChatUtils;

public class RecordPackets extends Module {
    public RecordPackets() {
        super("RecordPackets");

        this.setDescription("Sends packets in chat");
        this.setCategory("Packet");
    }

    @Override
    public void activate() {
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "SendPacketEvent": {
                SendPacketEvent e = ((SendPacketEvent) event);
                ChatUtils.displayMessage("Packet sent: " + e.getPacket().getClass().getSimpleName());
                break;
            }
        }
    }
}
