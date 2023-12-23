package net.onyx.client.modules.packet;

import net.minecraft.client.MinecraftClient;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class Sprint extends Module {

    public Sprint() {
        super("AutoSprint");
        this.setDescription("Makes you sprint whenever you move.");
        this.setCategory("Packet");
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                MinecraftClient client = OnyxClient.getClient();
                client.options.sprintKey.setPressed(true);
                break;
            }
        }
    }


}
