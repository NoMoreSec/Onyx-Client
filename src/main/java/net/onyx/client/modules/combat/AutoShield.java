package net.onyx.client.modules.combat;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

import java.util.function.Predicate;

public class AutoShield extends Module {
    private final Double lastValid = OnyxClient.getCurrentTime();


    public AutoShield() {
        super("AutoShield");

        this.setDescription("..");

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

