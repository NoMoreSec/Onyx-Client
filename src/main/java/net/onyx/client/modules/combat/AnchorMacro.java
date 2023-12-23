package net.onyx.client.modules.combat;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.InventoryUtils;
import org.lwjgl.glfw.GLFW;

public class AnchorMacro extends Module {


    public AnchorMacro() {
        super("AnchorMacro");

        this.setDescription("Anchor Macro");

        this.setCategory("Combat");

        this.addSetting(new Setting("ChargeOnly", false) {{
            this.setDescription("Only charges the anchor");
        }});

        //this.addSetting(new Setting("SafeAnchor", false) {{
        //    this.setDescription("(1.19.4 ONLY)");
        //}});

        this.addSetting(new Setting("ItemSwap", 1) {{
            this.setMax(10);
            this.setMin(0);
            this.setDescription("Item to swap to after exploding the anchor");
        }});

        this.addSetting(new Setting("Cooldown", 0) {{
            this.setMax(20);
            this.setMin(0);
            this.setDescription("Cooldown between blowing up anchors");
        }});

    }
    private boolean hasAnchored;
    private boolean hasCharged;
    private int clock;



    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }


    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (GLFW.glfwGetMouseButton(OnyxClient.getClient().getWindow().getHandle(), 1) != 1) {
                    return;
                }
                if (OnyxClient.getClient().player.isUsingItem()) {
                    return;
                }
                if (this.hasAnchored) {
                    if (this.clock != 0) {
                        --this.clock;
                        return;
                    }
                    this.clock = this.getIntSetting("Cooldown");
                    this.hasAnchored = false;
                }
                final HitResult cr = OnyxClient.getClient().crosshairTarget;
                if (cr instanceof BlockHitResult hit) {
                    final BlockPos pos = hit.getBlockPos();
                    if (BlockUtils.isAnchorUncharged(pos)) {
                        if (OnyxClient.getClient().player.isHolding(Items.GLOWSTONE)) {
                            final ActionResult actionResult = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, hit);
                            if (actionResult.isAccepted() && ActionResult.CONSUME.shouldSwingHand()) {
                                OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                            }
                            return;
                        }
                        InventoryUtils.selectItemFromHotbar(Items.GLOWSTONE);
                        final ActionResult actionResult = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, hit);
                        if (actionResult.isAccepted() && ActionResult.CONSUME.shouldSwingHand()) {
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                                }
                    } else if (BlockUtils.isAnchorCharged(pos) && !this.getBoolSetting("ChargeOnly")) {
                        final PlayerInventory inv = OnyxClient.getClient().player.getInventory();
                        inv.selectedSlot = this.getIntSetting("ItemSwap");
                        final ActionResult actionResult2 = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, hit);
                        if (actionResult2.isAccepted() && ActionResult.CONSUME.shouldSwingHand()) {
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                        }
                        this.hasAnchored = true;
                    }
                }
            }
        }
    }
}

