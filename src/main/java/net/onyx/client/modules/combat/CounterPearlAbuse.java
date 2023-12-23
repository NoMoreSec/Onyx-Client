package net.onyx.client.modules.combat;

import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.onyx.client.OnyxClient;
import net.onyx.client.components.plugins.impl.ServerClientRotation;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.ClientUtils;
import net.onyx.client.utils.InventoryUtils;
import net.onyx.client.utils.OldRotation;
import net.onyx.client.utils.PearlSimulator;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class CounterPearlAbuse extends Module {


    public CounterPearlAbuse() {
        super("CounterPearlAbuse(WIP)");

        this.setDescription("Counters people pearl abusing BY pearl abusing >;)");

        this.setCategory("Combat");


    }

    int id;

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);

    }


    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);

    }


    private final List<Triple<List<Vec3d>, Entity, BlockPos>> poses = new ArrayList<>();

    private final boolean bool = true;

    private Vec3d lastVec;

    private final ServerClientRotation rot = new ServerClientRotation();
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                //ALL TEST
                poses.clear();
                Entity e = PearlSimulator.summonProjectile(
                        OnyxClient.getClient().player, false, false, false);
                if (e != null) {
                    poses.add(PearlSimulator.simulate(e));
                }


                for (Entity lol : OnyxClient.getClient().world.getEntities()) {
                    if (lol instanceof ThrownEntity || lol instanceof PersistentProjectileEntity) {
                        if (!bool
                                && (lol instanceof EnderPearlEntity)) {
                            continue;
                        }


                        if (!Streams.stream(OnyxClient.getClient().world.getBlockCollisions(lol, lol.getBoundingBox())).allMatch(VoxelShape::isEmpty))
                            continue;

                        Triple<List<Vec3d>, Entity, BlockPos> p = PearlSimulator.simulate(lol);

                        if (p.getLeft().size() >= 2)
                            poses.add(p);
                    }
                }

                for (Triple<List<Vec3d>, Entity, BlockPos> t : poses) {
                    lastVec = !t.getLeft().isEmpty() ? t.getLeft().get(t.getLeft().size() - 1)
                            : OnyxClient.getClient().player.getEyePos();
                }

                for (PlayerEntity player : OnyxClient.getClient().world.getPlayers()) {
                    if (player == OnyxClient.getClient().player)
                        continue;

                    Entity proj = PearlSimulator.summonProjectile(
                            player, true, false, false);

                    if (proj != null) {
                        poses.add(PearlSimulator.simulate(proj));
                    }
                }

                ItemStack item = OnyxClient.getClient().player.getStackInHand(OnyxClient.getClient().player.getActiveHand());
                Item type = item.getItem();

                Vec3d oldLookPos = OldRotation.getClientLookVec();

                //Checks entities in the world
                for (Entity entity : OnyxClient.getClient().world.getEntities()) {
                    //if its a pearl GO GO GO
                    if (entity instanceof EnderPearlEntity pearl) {
                        // if any of these condition == true then return
                        if (pearl.isInLava() || pearl.isInsideWall() || pearl.isSubmergedInWater() || pearl.getY() < OnyxClient.getClient().player.getY())
                            continue;
                        if (item.isOf(Items.ENDER_PEARL)) return;
                        //if the pearl is within 5 blocks of the player GO GO GO
                        if (pearl.squaredDistanceTo(OnyxClient.me()) < 25) {
                            //gets the pearls landing pos
                            Vec3d pearlLandingPos = new Vec3d(lastVec.x, lastVec.y, lastVec.z);
                            //if the player can see the landingpos GO GO GO
                            if (ClientUtils.canSee(pearlLandingPos)) {
                                //if the landingpos is 5 blocks within the player GO GO GO
                                if (pearlLandingPos.squaredDistanceTo(OnyxClient.me().getPos()) < 25) {
                                    //do stuff
                                    ClientUtils.lookAtPos(pearlLandingPos);
                                    InventoryUtils.search(Items.ENDER_PEARL);
                                    OnyxClient.getClient().interactionManager.interactItem(OnyxClient.me(), Hand.MAIN_HAND);
                                    InventoryUtils.search(type);
                                    ClientUtils.lookAtPos(oldLookPos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}














