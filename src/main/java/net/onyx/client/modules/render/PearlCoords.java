package net.onyx.client.modules.render;

import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.render.RenderWorldEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.PearlSimulator;
import net.onyx.client.utils.QuadColor;
import net.onyx.client.utils.RenderUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class PearlCoords extends Module {


    public PearlCoords() {
        super("PearlPredict");

        this.setDescription("");

        this.setCategory("Render");






        this.addSetting(new Setting("FlyingPearls", true) {{
        }});

        this.addSetting(new Setting("OtherPlayers", true) {{
        }});

        this.addSetting(new Setting("ChatMessage", true) {{
        }});



    }


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(RenderWorldEvent.class);

    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(RenderWorldEvent.class);
    }

    private final List<Triple<List<Vec3d>, Entity, BlockPos>> poses = new ArrayList<>();

    private boolean sendChatMessage;

    private int ticks;
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                poses.clear();
                ticks++;
                if (ticks > 60) {
                    ticks = 0;
                    sendChatMessage = false;
                }

                Entity entity = PearlSimulator.summonProjectile(
                        OnyxClient.getClient().player, false, false, false);

                if (entity != null) {
                    poses.add(PearlSimulator.simulate(entity));
                }

                    for (Entity e : OnyxClient.getClient().world.getEntities()) {
                        if (e instanceof ThrownEntity || e instanceof PersistentProjectileEntity) {
                            if (!this.getBoolSetting("FlyingPearls")
                                    && (e instanceof EnderPearlEntity)) {
                                continue;
                            }


                            if (!Streams.stream(OnyxClient.getClient().world.getBlockCollisions(e, e.getBoundingBox())).allMatch(VoxelShape::isEmpty))
                                continue;

                            Triple<List<Vec3d>, Entity, BlockPos> p = PearlSimulator.simulate(e);

                            if (p.getLeft().size() >= 2)
                                poses.add(p);
                        }
                    }


                if (this.getBoolSetting("OtherPlayers")) {
                    for (PlayerEntity e : OnyxClient.getClient().world.getPlayers()) {
                        if (e == OnyxClient.getClient().player)
                            continue;

                        Entity proj = PearlSimulator.summonProjectile(
                                e, true, false, false);

                        if (proj != null) {
                            poses.add(PearlSimulator.simulate(proj));
                        }
                    }

                }
                break;
            }
            case "RenderWorldEvent": {
                int[] col = {255, 0, 130};
                int opacity = 255;

                for (Triple<List<Vec3d>, Entity, BlockPos> t : poses) {
                        if (t.getLeft().size() >= 2) {
                            //for (int i = 1; i < t.getLeft().size(); i++) {
                            //    RenderUtils.drawLine(
                            //            t.getLeft().get(i - 1).x, t.getLeft().get(i - 1).y, t.getLeft().get(i - 1).z,
                            //            t.getLeft().get(i).x, t.getLeft().get(i).y, t.getLeft().get(i).z,
                            //            LineColor.single(col[0], col[1], col[2], opacity),
                            //            1f);
                            //}
                        }



                    VoxelShape hitbox = t.getMiddle() != null ? VoxelShapes.cuboid(t.getMiddle().getBoundingBox())
                            : t.getRight() != null ? OnyxClient.getClient().world.getBlockState(t.getRight()).getCollisionShape(OnyxClient.getClient().world, t.getRight()).offset(t.getRight().getX(), t.getRight().getY(), t.getRight().getZ())
                            : null;
                    Vec3d lastVec = !t.getLeft().isEmpty() ? t.getLeft().get(t.getLeft().size() - 1)
                            : OnyxClient.getClient().player.getEyePos();


                    if (hitbox != null) {
                        //RenderUtils.drawLine(lastVec.x + 0.25, lastVec.y, lastVec.z, lastVec.x - 0.25, lastVec.y, lastVec.z, LineColor.single(col[0], col[1], col[2], 255), 1.75f);
                        //RenderUtils.drawLine(lastVec.x, lastVec.y + 0.25, lastVec.z, lastVec.x, lastVec.y - 0.25, lastVec.z, LineColor.single(col[0], col[1], col[2], 255), 1.75f);
                        //RenderUtils.drawLine(lastVec.x, lastVec.y, lastVec.z + 0.25, lastVec.x, lastVec.y, lastVec.z - 0.25, LineColor.single(col[0], col[1], col[2], 255), 1.75f);
                        if (this.getBoolSetting("ChatMessage")) {
                            if (!sendChatMessage) {
                                displayMessage("The pearl will land at" + " X:" + Math.round(lastVec.x) + " Y:" + Math.round(lastVec.y) + " Z:" + Math.round(lastVec.x));
                                sendChatMessage = true;
                            }
                        }

                        for (Box box : hitbox.getBoundingBoxes()) {
                            RenderUtils.drawBoxOutline(box, QuadColor.single(col[0], col[1], col[2], 190), 1f);
                        }
                    }
                }
                break;
            }
        }
    }
}





