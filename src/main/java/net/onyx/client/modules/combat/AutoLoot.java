package net.onyx.client.modules.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.AccessorUtils;
import net.onyx.client.utils.InventoryUtils;

import java.util.List;

public class AutoLoot extends Module {


    public AutoLoot() {
        super("AutoLoot");

        this.setDescription("Auto drops tots or pearls to get loot from ground");

        this.setCategory("Combat");

        this.addSetting(new Setting("HoverCurser", false) {{
            this.setDescription("Only works when your curser is hovered over the totems / pearls");
        }});

        this.addSetting(new Setting("TotemFirst", false) {{
            this.setDescription("Swaps to a totem when inv is opened");
        }});

        this.addSetting(new Setting("DropInterval", 0) {{
            this.setMax(20);
            this.setMin(0);
            this.setDescription("How fast to drop items from your inv");
        }});

        this.addSetting(new Setting("PearlsToKeep", 0) {{
            this.setMax(36);
            this.setMin(0);
            this.setDescription("The amount of pearls to keep // In stack");
        }});

        this.addSetting(new Setting("PotionsToKeep", 0) {{
            this.setMax(36);
            this.setMin(0);
            this.setDescription("The amount of PotionsToKeep to keep");
        }});

        this.addSetting(new Setting("TotemsToKeep", 0) {{
            this.setMax(36);
            this.setMin(0);
            this.setDescription("The amount of totems to keep // In stack");
        }});
    }
    private int dropClock = 0;


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        dropClock = 0;
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }




    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (this.getBoolSetting("HoverCurser")) {
                    if (!looting())
                        return;
                    PlayerInventory inv = OnyxClient.getClient().player.getInventory();
                    if (this.dropClock != 0) {
                        this.dropClock--;
                        return;
                    }
                    if (!(OnyxClient.getClient().currentScreen instanceof InventoryScreen))
                        return;
                    Screen screen = (MinecraftClient.getInstance()).currentScreen;
                    HandledScreen<?> gui = (HandledScreen) screen;
                    Slot slot = AccessorUtils.getSlotUnderMouse(gui);
                    if (slot == null)
                        return;
                    int SlotUnderMouse = AccessorUtils.getSlotUnderMouse(gui).getIndex();
                    if (SlotUnderMouse > 35)
                        return;
                    if (SlotUnderMouse < 9)
                        return;
                    if (inv.main.get(SlotUnderMouse).getItem() == Items.TOTEM_OF_UNDYING) {
                        if (InventoryUtils.countItem(Items.TOTEM_OF_UNDYING) <= this.getIntSetting("TotemsToKeep"))
                            return;
                        OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, SlotUnderMouse, 1, SlotActionType.THROW, OnyxClient.getClient().player);
                        this.dropClock = this.getIntSetting("DropInterval");
                        return;
                    }
                    if (inv.main.get(SlotUnderMouse).getItem() == Items.POTION) {
                        if (InventoryUtils.countItem(Items.TOTEM_OF_UNDYING) <= this.getIntSetting("PotionsToKeep"))
                            return;
                        OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, SlotUnderMouse, 1, SlotActionType.THROW, OnyxClient.getClient().player);
                        this.dropClock = this.getIntSetting("DropInterval");
                        return;
                    }
                    if (inv.main.get(SlotUnderMouse).getItem() == Items.ENDER_PEARL) {
                        int minPearls = this.getIntSetting("PearlsToKeep") * 16;
                        if (InventoryUtils.countItem(Items.ENDER_PEARL) <= minPearls)
                            return;
                        OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, SlotUnderMouse, 1, SlotActionType.THROW, OnyxClient.getClient().player);
                        this.dropClock = this.getIntSetting("DropInterval");
                        return;
                    }
                }

                if (this.getBoolSetting("HoverCurser")) {
                    return;
                }

                if (dropClock != 0)
                {
                    dropClock--;
                    return;
                }
                if (!(OnyxClient.getClient().currentScreen instanceof InventoryScreen))
                    return;
                if (!looting())
                    return;
                int slot = findSlot();
                if (slot == -1)
                    return;
                dropClock = this.getIntSetting("DropInterval");
                OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, slot, 1, SlotActionType.THROW, OnyxClient.getClient().player);
                }
            }
        }

    private boolean looting()
    {
        List<Entity> collidedEntities = OnyxClient.getClient().world.getOtherEntities(OnyxClient.getClient().player, OnyxClient.getClient().player.getBoundingBox().expand(1, 0.5, 1).expand(1.0E-7D));
        for (Entity e : collidedEntities)
        {

            if (e instanceof ItemEntity itemStack)
            {
                Item item = itemStack.getStack().getItem();
                if (item != Items.TOTEM_OF_UNDYING && item != Items.ENDER_PEARL)
                {
                    if (item == Items.END_CRYSTAL ||
                            item == Items.RESPAWN_ANCHOR ||
                            item == Items.GOLDEN_APPLE)
                        return true;
                    if (item instanceof ToolItem toolItem)
                    {
                        if (toolItem.getMaterial() == ToolMaterials.NETHERITE ||
                                toolItem.getMaterial() == ToolMaterials.DIAMOND)
                            return true;
                    }
                    if (item instanceof ArmorItem armorItem)
                    {
                        if (armorItem.getMaterial() == ArmorMaterials.NETHERITE ||
                                armorItem.getMaterial() == ArmorMaterials.DIAMOND)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private int findSlot()
    {
        if (this.getBoolSetting("TotemFirst"))
        {
            int totemSlot = findTotemSlot();
            if (totemSlot == -1)
                return findPearlSlot();
            return totemSlot;
        }
        int pearlSlot = findPearlSlot();
        if (pearlSlot == -1)
            return findTotemSlot();
        return pearlSlot;
    }

    private int findPearlSlot()
    {
        PlayerInventory inv = OnyxClient.getClient().player.getInventory();
        int pearlCount = InventoryUtils.countItem(Items.ENDER_PEARL);
        int fewestPearlSlot = -1;
        for (int i = 9; i < 36; i++)
        {
            ItemStack itemStack = inv.main.get(i);
            if (itemStack.getItem() == Items.ENDER_PEARL)
            {
                if (fewestPearlSlot == -1 ||
                        itemStack.getCount() < inv.main.get(fewestPearlSlot).getCount())
                {
                    fewestPearlSlot = i;
                }
            }
        }
        int minPearls = this.getIntSetting("PearlsToKeep") * 16;
        if (fewestPearlSlot == -1)
            return -1;
        if (pearlCount - inv.main.get(fewestPearlSlot).getCount() >= minPearls)
        {
            return fewestPearlSlot;
        }
        return -1;
    }

    private int findTotemSlot()
    {
        PlayerInventory inv = OnyxClient.getClient().player.getInventory();
        int totemCount = InventoryUtils.countItem(Items.TOTEM_OF_UNDYING);
        if (totemCount <= this.getIntSetting("TotemsToKeep"))
            return -1;
        for (int i = 9; i < 36; i++)
        {
            ItemStack itemStack = inv.main.get(i);
            if (itemStack.getItem() == Items.TOTEM_OF_UNDYING)
            {
                return i;
            }
        }
        return -1;
    }

    }


