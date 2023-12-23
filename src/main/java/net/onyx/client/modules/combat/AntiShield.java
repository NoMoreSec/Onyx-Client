package net.onyx.client.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

import java.util.function.Predicate;

public class AntiShield extends Module {

    public AntiShield() {
        super("AntiShield");

        this.setDescription("Bybasses shields allowing you to still hit them");

        //this.addSetting(new Setting("MustHit", false) {{
        //    this.setDescription("Have to hit the player to disable the shield");
        //}});

        this.setCategory("Combat");

    }


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
                ItemStack mainHand = OnyxClient.getClient().player.getMainHandStack();
                HitResult hit = OnyxClient.getClient().crosshairTarget;
                if (hit.getType() != HitResult.Type.ENTITY)
                    return;
                Entity target = ((EntityHitResult) hit).getEntity();
                if (!(target instanceof PlayerEntity))
                    return;
                if (!((PlayerEntity) target).isUsingItem()) {
                    return;
                }
                if ((mainHand.isOf(Items.DIAMOND_SWORD) || mainHand.isOf(Items.NETHERITE_SWORD))) {
                    if (((PlayerEntity) target).getOffHandStack().getItem() instanceof ShieldItem || ((PlayerEntity) target).getMainHandStack().getItem() instanceof ShieldItem) {
                        selectItemFromHotbar(Items.NETHERITE_AXE);
                        selectItemFromHotbar(Items.DIAMOND_AXE);
                    }
                }

                if ((mainHand.isOf(Items.NETHERITE_AXE) || mainHand.isOf(Items.DIAMOND_AXE))) {
                    if (((PlayerEntity) target).getOffHandStack().getItem() instanceof ShieldItem || ((PlayerEntity) target).getMainHandStack().getItem() instanceof ShieldItem) {
                        if (!OnyxClient.getClient().options.attackKey.isPressed())
                            return;
                        selectItemFromHotbar(Items.NETHERITE_SWORD);
                        if (((PlayerEntity) target).canTakeDamage()) {
                            OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.getClient().player, target);
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }



    public boolean selectItemFromHotbar(Predicate<Item> item) {
        PlayerInventory inv = OnyxClient.getClient().player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            inv.selectedSlot = i;
            return true;
        }
        return false;
    }
    public boolean selectItemFromHotbar (Item item){
        return this.selectItemFromHotbar((Item i) -> i == item);
    }
}

