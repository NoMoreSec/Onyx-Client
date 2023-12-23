package net.onyx.client.components.plugins.impl;

import net.minecraft.util.math.Vec3d;
import net.onyx.client.components.plugins.ModulePlugin;
import net.onyx.client.events.Event;
import net.onyx.client.events.packet.PostMovementPacketEvent;
import net.onyx.client.events.packet.PreMovementPacketEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.ClientUtils;
import net.onyx.client.utils.OldRotation;

public class ServerClientRotation implements ModulePlugin {
    public void handlePreMotion() {
        this.client = ClientUtils.getRotation();
        ClientUtils.applyRotation(this.server);
    }
    public void handlePostMotion() {
        ClientUtils.applyRotation(this.client);

        active = false;
    }

    @Override
    public void addListeners(Module parentModule) {
        parentModule.addListen(PreMovementPacketEvent.class);
        parentModule.addListen(PostMovementPacketEvent.class);
    }

    @Override
    public void removeListeners(Module parentModule) {
        parentModule.removeListen(PreMovementPacketEvent.class);
        parentModule.removeListen(PostMovementPacketEvent.class);
    }

    private OldRotation.Rotation server;
    private OldRotation.Rotation client;

    private boolean active = false;


    public void setServer(OldRotation.Rotation rotation) {
        this.server = rotation;
    }

    public void setClient(OldRotation.Rotation rotation) {
        this.client = rotation;
    }

    public void lookAtPosServer(Vec3d pos) {
        this.active = true;

        this.server = OldRotation.getRequiredRotation(pos);
    }
    public void lookAtPosClient(Vec3d pos) {
        ClientUtils.lookAtPos(pos);
    }

    public OldRotation.Rotation getServerRotation() {
        return this.active ? this.server : ClientUtils.getRotation();
    }

    @Override
    public boolean fireEvent(Event event) {
        if (!active) return false;

        switch (event.getClass().getSimpleName()) {
            case "PostMotionEvent": {
                this.handlePostMotion();
                return true;
            }

            case "PreMotionEvent": {
                this.handlePreMotion();
                return true;
            }
        }

        return false;
    }
}
