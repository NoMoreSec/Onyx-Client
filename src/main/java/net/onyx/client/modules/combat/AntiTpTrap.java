package net.onyx.client.modules.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.client.JumpEvent;
import net.onyx.client.events.client.OpaqueCubeEvent;
import net.onyx.client.events.client.PlayerMoveEvent;
import net.onyx.client.modules.Module;

public class AntiTpTrap extends Module {





    public AntiTpTrap() {
        super("AntiTpTrap");

        this.setDescription("Allows you pearl out of anything");

        this.setCategory("Combat");

    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(JumpEvent.class);
        this.addListen(PlayerMoveEvent.class);
        this.addListen(OpaqueCubeEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(JumpEvent.class);
        this.removeListen(PlayerMoveEvent.class);
        this.removeListen(OpaqueCubeEvent.class);
    }




    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OpaqueCubeEvent": {
                ((OpaqueCubeEvent)event).ci.cancel();
                break;
            }
            case "PlayerMoveEvent": {
                if (!collidingBlocks())
                    return;
                ClientPlayerEntity player = OnyxClient.getClient().player;
                player.noClip = true;
                break;
            }
            case "ClientTickEvent": {
                if (!collidingBlocks())
                    return;

                ClientPlayerEntity player = OnyxClient.getClient().player;

                player.noClip = true;
                player.fallDistance = 0;
                player.setOnGround(true);

                player.getAbilities().flying = false;
                player.setVelocity(0, 0, 0);

                float speed = 0.02F;
                player.airStrafingSpeed = speed;

                if (OnyxClient.getClient().options.jumpKey.isPressed()) {
                    player.addVelocity(0, speed, 0);
                }
                if (OnyxClient.getClient().options.sneakKey.isPressed()) {
                    player.addVelocity(0, -speed, 0);
                }
                break;
            }
            case "JumpEvent": {
                if (!collidingBlocks())
                    return;
                ((JumpEvent)event).ci.cancel();
                break;
            }
        }
    }

    private boolean collidingBlocks()
    {
        ClientPlayerEntity player = OnyxClient.getClient().player;
        return
                wouldCollideAt(new BlockPos(player.getX() - (double)player.getWidth() * 0.35D, player.getY(), player.getZ() + (double)player.getWidth() * 0.35D)) ||
                        wouldCollideAt(new BlockPos(player.getX() - (double)player.getWidth() * 0.35D, player.getY(), player.getZ() - (double)player.getWidth() * 0.35D)) ||
                        wouldCollideAt(new BlockPos(player.getX() + (double)player.getWidth() * 0.35D, player.getY(),player.getZ() - (double)player.getWidth() * 0.35D)) ||
                        wouldCollideAt(new BlockPos(player.getX() + (double)player.getWidth() * 0.35D, player.getY(),player.getZ() + (double)player.getWidth() * 0.35D));
    }

    private boolean wouldCollideAt(BlockPos pos)
    {
        Box box = OnyxClient.getClient().player.getBoundingBox();
        Box box2 = (new Box(pos.getX(), box.minY, pos.getZ(), (double)pos.getX() + 1.0D, box.maxY, (double)pos.getZ() + 1.0D)).contract(1.0E-7D);
        return OnyxClient.getClient().world.canCollide(OnyxClient.getClient().player, box2);
    }
}



