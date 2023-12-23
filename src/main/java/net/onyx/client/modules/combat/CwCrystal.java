package net.onyx.client.modules.combat;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.interfaces.mixin.IMouse;
import net.onyx.client.modules.Module;
import net.onyx.client.onyxevent.events.ItemUseListener;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.CrystalUtils;
import org.lwjgl.glfw.GLFW;

public class CwCrystal extends Module implements ItemUseListener  {



    public CwCrystal() {
        super("CwCrystal");

        this.setDescription("Creds: Anker // Simple Crystal macro");

        this.setCategory("Combat");

        this.addSetting(new Setting("PlaceDelay", 0) {{
            this.setMin(0);
            this.setMax(20);
            this.setDescription("Speed at which you place crystals");
        }});

        this.addSetting(new Setting("BreakDelay", 0) {{
            this.setMin(0);
            this.setMax(20);
            this.setDescription("Speed at which you destroy crystals");
        }});

        this.addSetting(new Setting("StopOnKill", false) {{
            this.setDescription("Stops crystaling when a player dies nearby");
        }});

        this.addSetting(new Setting("SimulateCPS", false) {{
            this.setDescription("Shows a fake cps for mods like KronHUD etc");
        }});

    }

    private Integer placeInterval = 0;
    private Integer breakInterval = 0;



    private int crystalPlaceClock = 0;
    private int crystalBreakClock = 0;
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        crystalPlaceClock = 0;
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

            @Override
            public void fireEvent(Event event) {
                switch (event.getClass().getSimpleName()) {
                    case "ClientTickEvent": {
                        IMouse mouse = (IMouse) OnyxClient.getClient().mouse;
                        boolean dontPlaceCrystal = crystalPlaceClock != 0;
                        boolean dontBreakCrystal = crystalBreakClock != 0;
                        if (dontPlaceCrystal)
                            crystalPlaceClock--;
                        if (dontBreakCrystal)
                            crystalBreakClock--;
                        if (GLFW.glfwGetMouseButton(OnyxClient.getClient().getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS)
                            return;
                        ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
                        if (!mainHandStack.isOf(Items.END_CRYSTAL))
                            return;
                        if (this.getBoolSetting("StopOnKill") && isDeadBodyNearby())
                            return;

                        if (OnyxClient.getClient().crosshairTarget instanceof EntityHitResult hit) {
                            if (!dontBreakCrystal && hit.getEntity() instanceof EndCrystalEntity || hit.getEntity() instanceof MagmaCubeEntity || hit.getEntity() instanceof SlimeEntity) {
                                crystalBreakClock = breakInterval;
                                OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.getClient().player, hit.getEntity());
                                OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                                if (this.getBoolSetting("SimulateCPS")) {
                                    mouse.cwOnMouseButton(OnyxClient.getClient().getWindow().getHandle(), 0, 1, 0);
                                    mouse.cwOnMouseButton(OnyxClient.getClient().getWindow().getHandle(), 0, 0, 0);
                                }
                                OnyxClient.getInstance().getCrystalDataTracker().recordAttack(hit.getEntity());
                                this.breakInterval = this.getIntSetting("BreakDelay");
                            } 
                        }
                        if (OnyxClient.getClient().crosshairTarget instanceof BlockHitResult hit) {
                            BlockPos block = hit.getBlockPos();
                            if (!dontPlaceCrystal && CrystalUtils.canPlaceCrystalServer(block)) {
                                crystalPlaceClock = placeInterval;
                                ActionResult result = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, hit);
                                if (result.isAccepted() && result.shouldSwingHand())
                                    OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                                if (this.getBoolSetting("SimulateCPS")) {
                                    mouse.cwOnMouseButton(OnyxClient.getClient().getWindow().getHandle(), 1, 1, 0);
                                    mouse.cwOnMouseButton(OnyxClient.getClient().getWindow().getHandle(), 1, 0, 0);
                                }
                                this.placeInterval = this.getIntSetting("PlaceDelay");

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
                    if (mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos()))
                        event.cancel();
                }
            }
        }
