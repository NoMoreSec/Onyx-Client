package net.onyx.client.modules.utilities;

import net.minecraft.client.util.Window;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class UnfocusCPU extends Module {

    public UnfocusCPU() {
        super("UnfocusCPU", true);

        this.setDescription("Decreases game performance while the window is not focused.");

        this.addSetting(new Setting("MaxFPS", 15));
        this.setCategory("Utility");
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
                Window window = OnyxClient.getClient().getWindow();
                if (!OnyxClient.getClient().isWindowFocused()) {
                    window.setFramerateLimit(this.getIntSetting("MaxFPS"));
                } else {
                    window.setFramerateLimit(OnyxClient.getClient().options.getMaxFps().getValue());
                }
            }
        }
    }

}

