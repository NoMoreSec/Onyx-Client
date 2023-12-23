package net.onyx.client.modules.utilities;

import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.screen.DeathEvent;
import net.onyx.client.modules.Module;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn");

        this.setDescription("Automatically respawns the player.");
        this.setCategory("Utility");
    }

    @Override
    public void activate() {
        this.addListen(DeathEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(DeathEvent.class);
    }
    
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "DeathEvent": {
                OnyxClient.me().requestRespawn();
                break;
            }
        }
    }
}
