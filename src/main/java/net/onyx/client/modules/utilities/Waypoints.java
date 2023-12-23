package net.onyx.client.modules.utilities;

import net.onyx.client.components.systems.WaypointSystem;
import net.onyx.client.events.Event;
import net.onyx.client.events.render.OnRenderEvent;
import net.onyx.client.events.render.RenderWorldViewBobbingEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.RenderUtils;

public class Waypoints extends Module {
    public WaypointSystem waypoints = new WaypointSystem();

    public Waypoints() {
        super("Waypoints");

        this.setDescription("Renders where waypoints are in the world.");
        
        this.setCategory("Utility");
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(RenderWorldViewBobbingEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(RenderWorldViewBobbingEvent.class);
    }

    private boolean isActive = false;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldViewBobbingEvent": {
                if (!this.isActive) return;

                // Stabilise the tracers
                RenderWorldViewBobbingEvent e = (RenderWorldViewBobbingEvent)event;
                e.cancel = true;

                break;
            }
            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;

                this.isActive = false;
                for (String name : this.waypoints.getWaypoints()) {
                    WaypointSystem.Waypoint waypoint = this.waypoints.getWaypoint(name);
                    if (!waypoint.enabled) continue;

                    RenderUtils.drawTracer(e.mStack, waypoint.pos, e.tickDelta);
                    this.isActive = true;
                }

                break;
            }
        }
    }
}
