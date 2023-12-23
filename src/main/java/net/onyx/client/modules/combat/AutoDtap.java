package net.onyx.client.modules.combat;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.onyxevent.events.ItemUseListener;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.CrystalUtils;
import net.onyx.client.utils.RotationUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class AutoDtap extends Module implements ItemUseListener  {


    public AutoDtap() {
        super("AutoDtap");

        this.setDescription("Uses damage ticks to justify when to crystal");

        this.setCategory("Combat");


        this.addSetting(new Setting("BreakDelay", 0) {{
            this.setMin(0);
            this.setMax(20);
            this.setDescription("Speed at which you destroy crystals");
        }});

        this.addSetting(new Setting("DamageTick", 8) {{
            this.setMin(0);
            this.setMax(10);
            this.setDescription("At which damage tick to activate on 1 being the start");
        }});

        this.addSetting(new Setting("StopOnKill", false) {{
            this.setDescription("Stops crystaling when a player dies nearby");
        }});

    }

    private Integer breakInterval = 0;
    private int crystaled;

    private final String lastTargetUuid = null;

    private int crystalBreakClock = 0;
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        crystalBreakClock = 0;
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }


    private boolean isDeadBodyNearby()
    {
        return OnyxClient.getClient().world.getPlayers().parallelStream()
                .filter(e -> OnyxClient.getClient().player != e)
                .filter(e -> e.squaredDistanceTo(OnyxClient.getClient().player) < 36)
                .anyMatch(LivingEntity::isDead);
    }

    private boolean canEntityBeHit() {
        return OnyxClient.getClient().world.getPlayers().parallelStream()
                .filter(e -> OnyxClient.getClient().player != e)
                .filter(e -> e.squaredDistanceTo(OnyxClient.getClient().player) < 25)
                .anyMatch(e -> e.hurtTime != 0);
    }

    private Robot robot;


    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                HitResult hitEntity = OnyxClient.getClient().crosshairTarget;
                if (hitEntity.getType() == HitResult.Type.ENTITY) {
                    Entity target = ((EntityHitResult) hitEntity).getEntity();
                    if ((target instanceof PlayerEntity))
                        crystaled = 0;
                    if (crystaled == 2) {
                        crystaled = 0;
                    }
                }
                boolean dontBreakCrystal = crystalBreakClock != 0;
                if (dontBreakCrystal)
                    crystalBreakClock--;
                if (GLFW.glfwGetMouseButton(OnyxClient.getClient().getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS)
                    return;
                ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
                if (!mainHandStack.isOf(Items.END_CRYSTAL))
                    return;
                if (this.getBoolSetting("StopOnKill") && isDeadBodyNearby())
                    return;
                Vec3d camPos = OnyxClient.getClient().player.getEyePos();
                BlockHitResult blockHit = OnyxClient.getClient().world.raycast(new RaycastContext(camPos, camPos.add(RotationUtils.getClientLookVec().multiply(4.5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, OnyxClient.getClient().player));
                    if (!OnyxClient.getClient().options.useKey.isPressed()) {
                        crystaled = 0;
                    }
                    if (crystaled == 1 && !canEntityBeHit()) {
                        return;
                    }
                    if (OnyxClient.getClient().crosshairTarget instanceof EntityHitResult hit) {
                        if (!dontBreakCrystal && hit.getEntity() instanceof EndCrystalEntity crystal) {
                            crystalBreakClock = breakInterval;
                            OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.getClient().player, crystal);
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                            OnyxClient.getInstance().getCrystalDataTracker().recordAttack(crystal);
                            this.breakInterval = this.getIntSetting("BreakDelay");
                            crystaled++;
                        } else if (!dontBreakCrystal && hit.getEntity() instanceof SlimeEntity slime) {
                            crystalBreakClock = breakInterval;
                            OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.getClient().player, slime);
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                            OnyxClient.getInstance().getCrystalDataTracker().recordAttack(slime);
                            this.breakInterval = this.getIntSetting("BreakDelay");
                            crystaled++;
                        } else if (!dontBreakCrystal && hit.getEntity() instanceof MagmaCubeEntity magma) {
                            crystalBreakClock = breakInterval;
                            OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.getClient().player, magma);
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                            OnyxClient.getInstance().getCrystalDataTracker().recordAttack(magma);
                            this.breakInterval = this.getIntSetting("BreakDelay");
                            crystaled++;
                        }
                    }
                    if (BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos()) || BlockUtils.isBlock(Blocks.BEDROCK, blockHit.getBlockPos())) {
                        if (CrystalUtils.canPlaceCrystalServer(blockHit.getBlockPos())) {
                            ActionResult result = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, blockHit);
                            if (result.isAccepted() && result.shouldSwingHand())
                                OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                        }
                    }
                }
            }
        }




    @Override
    public void onItemUse(ItemUseEvent event)
    {
        ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
        if (OnyxClient.getClient().crosshairTarget.getType() == HitResult.Type.BLOCK)
        {
            BlockHitResult hit = (BlockHitResult) OnyxClient.getClient().crosshairTarget;
            if (mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos()) || mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.BEDROCK, hit.getBlockPos()))
                event.cancel();
        }
    }
}

