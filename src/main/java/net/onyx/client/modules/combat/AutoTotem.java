package net.onyx.client.modules.combat;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.ClientUtils;
import net.onyx.client.utils.InteractionUtils;

import java.util.ArrayList;
import java.util.List;

public class AutoTotem extends Module {

    public AutoTotem() {
        super("AutoTotem");

        this.setDescription("Automatically places a totem into your off hand");
        this.setCategory("Combat");
    }

    @Override
    public String listOption() {
        int total = 0;

        total += this.getAllTotemSlots().size();
        total += this.hasTotemEquip() ? 1 : 0;

        return Integer.toString(total);
    }

    private List<Integer> getAllTotemSlots() {
        return this.getTotemSlots(OnyxClient.me().getInventory().size());
    }

    private List<Integer> getTotemSlots(int total) {
        List<Integer> totems = new ArrayList<Integer>();

        PlayerInventory inv = OnyxClient.me().getInventory();

        for (int i = 9; i < PlayerInventory.MAIN_SIZE && total > 0; i++) {
            if (this.isTotem(inv.main.get(i))) {
                total--;
                totems.add(i);
            }
        }

        for (int i = 0; i < 9 && total > 0; i++) {
            if (this.isTotem(inv.main.get(i))) {
                total--;
                totems.add(i + 36);
            }
        }

        return totems;
    }

    private boolean isTotem(ItemStack item) {
        return item.isOf(Items.TOTEM_OF_UNDYING);
    }

    private ItemStack getOffHandStack() {
        return OnyxClient.me().getStackInHand(Hand.OFF_HAND);
    }

    private boolean hasTotemEquip() {
        ItemStack item = this.getOffHandStack();

        return this.isTotem(item);
    }

    private int getTotemSlot() {
        List<Integer> slots = this.getTotemSlots(1);

        return slots.size() == 0 ? -1 : slots.get(0);
    }

    private void equipTotem(int origin) {
        boolean wasHandEmpty = this.getOffHandStack().isEmpty();

        InteractionUtils.pickupItem(origin);

        InteractionUtils.pickupItem(45);

        if (!wasHandEmpty) {
            InteractionUtils.pickupItem(origin);
        };
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
                if (this.hasTotemEquip()) break;
                if (ClientUtils.isInStorage()) break;

                int totemSlot = this.getTotemSlot();

                if (totemSlot == -1) break;

                this.equipTotem(totemSlot);

                break;
            }
        }
    }

}
