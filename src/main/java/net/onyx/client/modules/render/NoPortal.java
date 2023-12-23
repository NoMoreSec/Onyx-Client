package net.onyx.client.modules.render;

import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.render.RenderPortalOverlayEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.ClientUtils;

public class NoPortal extends Module {
    public NoPortal() {
        super("NoPortal");

        this.addSetting(new Setting("NoOverlay", true));
        this.addSetting(new Setting("NoNausea", true));
        this.addSetting(new Setting("AllowTyping", true));

        this.setDescription("Allows portal effects to be toggled.");

        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(RenderPortalOverlayEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderPortalOverlayEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (!ClientUtils.isInNetherPortal() || !(boolean)this.getSetting("NoNausea").value) break;

                OnyxClient.me().lastNauseaStrength = 0;
                OnyxClient.me().nextNauseaStrength = 0.00001f;

                break;
            }
            case "RenderPortalOverlayEvent": {
                RenderPortalOverlayEvent e = (RenderPortalOverlayEvent)event;
                if ((boolean)this.getSetting("NoOverlay").value) {
                    e.ci.cancel();
                }

                break;
            }
        }
    }


    }
