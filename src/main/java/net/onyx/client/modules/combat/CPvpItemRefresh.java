package net.onyx.client.modules.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.mixin.client.ItemStackAccessor;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.InventoryUtils;
import net.onyx.client.utils.SlotUtils;

public class CPvpItemRefresh extends Module {



    private final ItemStack[] items = new ItemStack[10];


    public CPvpItemRefresh() {
        super("CPvpItemRefresh");

        this.setDescription("Automatically refreshes items in your hotbar");

        this.setCategory("Combat");

        this.addSetting(new Setting("CpvpItems", false) {{
            this.setDescription("Shows the available items to refresh");
        }});

        //Items
        this.addSetting(new CpvpItems("Crystals", false) {{
        }});
        this.addSetting(new CpvpItems("Pearls", false) {{
        }});
        this.addSetting(new CpvpItems("Obsidian", false) {{
        }});
        this.addSetting(new CpvpItems("Gapples", false) {{
        }});
        this.addSetting(new CpvpItems("Anchors", false) {{
        }});
        this.addSetting(new CpvpItems("GlowStone", false) {{
        }});
        this.addSetting(new CpvpItems("EXP", false) {{
        }});




        this.addSetting(new Setting("MinItemStack", 0) {{
            this.setMax(64);
            this.setMin(0);
            this.setDescription("The threshold of items left this actives at");
        }});

        this.addSetting(new Setting("SearchHotbar", false) {{
            this.setDescription("Checks hotbar for that item too");
        }});

        for (int i = 0; i < items.length; i++) items[i] = new ItemStack(Items.AIR);



    }

    @Override
    public void activate() {
        fillItems();
        this.addListen(ClientTickEvent.class);
        prevHadOpenScreen = OnyxClient.getClient().currentScreen != null;

    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private boolean prevHadOpenScreen;
    private int tickDelayLeft;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                //return statements
                ItemStack mainHand = OnyxClient.getClient().player.getMainHandStack();
                if (mainHand.isOf(Items.EXPERIENCE_BOTTLE) && this.getBoolSetting("EXP")
                        || mainHand.isOf(Items.GOLDEN_APPLE) && this.getBoolSetting("Gapples")
                        || mainHand.isOf(Items.OBSIDIAN) && this.getBoolSetting("Obsidian")
                        || mainHand.isOf(Items.GLOWSTONE) && this.getBoolSetting("GlowStone")
                        || mainHand.isOf(Items.ENDER_PEARL) && this.getBoolSetting("Pearls")
                        || mainHand.isOf(Items.RESPAWN_ANCHOR) && this.getBoolSetting("Anchors")
                        || mainHand.isOf(Items.END_CRYSTAL) && this.getBoolSetting("Crystals")) {
                    if (OnyxClient.getClient().currentScreen == null && prevHadOpenScreen) {
                        fillItems();
                    }

                    prevHadOpenScreen = OnyxClient.getClient().currentScreen != null;
                    if (OnyxClient.getClient().player.currentScreenHandler.getStacks().size() != 46 || OnyxClient.getClient().currentScreen != null)
                        return;

                    if (tickDelayLeft <= 0) {
                        tickDelayLeft = 0;

                        // Hotbar
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = OnyxClient.getClient().player.getInventory().getStack(i);
                            checkSlot(i, stack);
                        }
                    } else {
                        tickDelayLeft--;
                    }
                }
            }
        }
    }




    private void checkSlot(int slot, ItemStack stack) {
        ItemStack prevStack = getItem(slot);

        // Stackable items 1
        if (!stack.isEmpty() && stack.isStackable()) {
            if (stack.getCount() <= this.getIntSetting("MinItemStack")) {
                addSlots(slot, findItem(stack, slot, this.getIntSetting("MinItemStack") - stack.getCount() + 1));
            }
        }

        if (stack.isEmpty() && !prevStack.isEmpty()) {
            // Stackable items 2
            if (prevStack.isStackable()) {
                addSlots(slot, findItem(prevStack, slot, this.getIntSetting("MinItemStack") - stack.getCount() + 1));
            }
        }

        setItem(slot, stack);
    }


    private int findItem(ItemStack itemStack, int excludedSlot, int goodEnoughCount) {
        int slot = -1;
        int count = 0;

        for (int i = OnyxClient.getClient().player.getInventory().size() - 2; i >= (this.getBoolSetting("SearchHotbar") ? 0 : 9); i--) {
            ItemStack stack = OnyxClient.getClient().player.getInventory().getStack(i);

            if (i != excludedSlot && stack.getItem() == itemStack.getItem() && ItemStack.areNbtEqual(itemStack, stack)) {
                if (stack.getCount() > count) {
                    slot = i;
                    count = stack.getCount();

                    if (count >= goodEnoughCount) break;
                }
            }
        }

        return slot;
    }

    private void addSlots(int to, int from) {
        InventoryUtils.move().from(from).to(to);
    }

    private void fillItems() {
        for (int i = 0; i < 9; i++) {
            setItem(i, OnyxClient.getClient().player.getInventory().getStack(i));
        }

        setItem(SlotUtils.OFFHAND, OnyxClient.getClient().player.getOffHandStack());
    }

    private ItemStack getItem(int slot) {
        if (slot == SlotUtils.OFFHAND) slot = 9;

        return items[slot];
    }

    private void setItem(int slot, ItemStack stack) {
        if (slot == SlotUtils.OFFHAND) slot = 9;

        ItemStack s = items[slot];
        ((ItemStackAccessor) (Object) s).setItem(stack.getItem());
        s.setCount(stack.getCount());
        s.setNbt(stack.getNbt());
        ((ItemStackAccessor) (Object) s).setEmpty(stack.isEmpty());
    }


    private class CpvpItems extends Setting {
        public CpvpItems(String name, Object value) {
            super(name, value);

            this.setCategory("CpvpItems");
        }

        @Override
        public boolean shouldShow() {
            return getBoolSetting("CpvpItems");
        }
    }
}



