package net.onyx.client.modules.utilities;

import net.minecraft.client.MinecraftClient;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.interfaces.mixin.IClient;
import net.onyx.client.modules.Module;

public class FastUse extends Module {

    public FastUse() {
        super("FastUse");

        this.setCategory("Utility");

        this.addSetting(new Setting("TickDelay", 0));

        this.setDescription("Allows you to use items at light speed.");
    }

    @Override
    public void activate() {
        this.resetDelay();
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private Integer delayTick = 0;
    private void resetDelay() {
        this.delayTick = 0;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                MinecraftClient client = OnyxClient.getClient();

                if (!client.options.useKey.isPressed()) {
                    this.resetDelay();
                    break;
                }

                // Ignore if we are still on a delay
                if (this.delayTick > 0) {
                    this.delayTick--;
                    break;
                }

                IClient clientAccessor = (IClient) client;
                clientAccessor.performItemUse();
                this.delayTick = this.getIntSetting("TickDelay");

                break;
            }
        }
    }


}

