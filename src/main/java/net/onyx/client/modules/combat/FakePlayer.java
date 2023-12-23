package net.onyx.client.modules.combat;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class FakePlayer extends Module {


    public FakePlayer() {
        super("FakePlayer");

        this.setDescription("Spawns a fake player");

        this.setCategory("Combat");

        this.addSetting(new Setting("FakePlayerName", "Walksy") {{
            this.setDescription("");
        }});

    }
    int id;
    @Override
    public void activate() {
        if (OnyxClient.getClient().world == null) {
            return;
        }
        OtherClientPlayerEntity player = new OtherClientPlayerEntity(OnyxClient.getClient().world, new GameProfile(null, this.getStringSetting("FakePlayerName")), null);
        Vec3d pos = OnyxClient.getClient().player.getPos();
        player.updateTrackedPosition(pos.x,pos.y,pos.z);
        player.updatePositionAndAngles(pos.x, pos.y, pos.z, OnyxClient.getClient().player.getYaw(), OnyxClient.getClient().player.getPitch());
        player.resetPosition();
        OnyxClient.getClient().world.addPlayer(player.getId(), player);
        id = player.getId();
    }


    @Override
    public void deactivate() {
        OnyxClient.getClient().world.removeEntity(id, Entity.RemovalReason.DISCARDED);
        this.removeListen(ClientTickEvent.class);
    }




    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {


            }
        }
    }
}

