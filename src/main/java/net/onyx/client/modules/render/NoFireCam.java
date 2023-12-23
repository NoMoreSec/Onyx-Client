package net.onyx.client.modules.render;

import net.onyx.client.events.Event;
import net.onyx.client.events.render.RenderFireOverlayEvent;
import net.onyx.client.modules.Module;

public class NoFireCam extends Module {
    public NoFireCam() {
        super("NoFireCam");

        this.setDescription("Disables the annoying fire overlay to allow you to see a bit better.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(RenderFireOverlayEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderFireOverlayEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderFireOverlayEvent": {
                ((RenderFireOverlayEvent)event).ci.cancel();
                break;
            }
        }
    }
}
