package net.onyx.client.modules.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.AccessorUtils;

public class AutoInventoryTotem extends Module {


    public AutoInventoryTotem() {
        super("AutoInventoryTotem");

        this.setDescription("Auto puts a totem in offhand and totem slot");

        this.setCategory("Combat");

        this.addSetting(new Setting("TotemOnInv", false) {{
            this.setDescription("Totems when opening inv");
        }});

        this.addSetting(new Setting("HoverCurser", false) {{
            this.setDescription("Must have your curser hovered over a totem");
        }});

        this.addSetting(new Setting("TotemOnInv", false) {{
            this.setDescription("Switches to a totem when you open your inv");
        }});

        this.addSetting(new Setting("AutoSwitch", false) {{
            this.setDescription("Automatically switches to your totem slot");
        }});

        this.addSetting(new Setting("ForceTotem", false) {{
            this.setDescription("Replaces useless items");
        }});

        this.addSetting(new Setting("Delay", 0) {{
            this.setMax(20);
            this.setMin(0);
            this.setDescription("Delay for auto switch after opening inv");
        }});

        this.addSetting(new Setting("TotemSlot", 0) {{
            this.setMax(9);
            this.setMin(0);
            this.setDescription("Slot in which you want your totem to go to");
        }});

    }
    private int invClock = -1;


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        invClock = -1;

    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }


    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                PlayerInventory inv = OnyxClient.getClient().player.getInventory();
                if (OnyxClient.getClient().currentScreen != null && this .getBoolSetting("TotemOnInv"))
                    if (this.getBoolSetting("TotemOnInv")) {
                        inv.selectedSlot = this.getIntSetting("TotemSlot");
                    }
                if (this.getBoolSetting("HoverCurser")) {
                    if (!(OnyxClient.getClient().currentScreen instanceof InventoryScreen)) {
                        this.invClock = -1;
                        return;
                    }
                    if (this.invClock == -1)
                        this.invClock = this.getIntSetting("Delay");
                    if (this.invClock > 0) {
                        this.invClock--;
                        return;
                    }
                    if (this.getBoolSetting("AutoSwitch"))
                        inv.selectedSlot = this.getIntSetting("TotemSlot");
                    if (inv.offHand.get(0).getItem() != Items.TOTEM_OF_UNDYING) {
                        Screen screen = (MinecraftClient.getInstance().currentScreen);
                        HandledScreen<?> gui = (HandledScreen) screen;
                        Slot slot = AccessorUtils.getSlotUnderMouse(gui);
                        if (slot == null)
                            return;
                        int SlotUnderMouse = AccessorUtils.getSlotUnderMouse(gui).getIndex();
                        if (SlotUnderMouse > 35)
                            return;
                        if (SlotUnderMouse < 0)
                            return;
                        if (SlotUnderMouse == 40)
                            return;
                        if (inv.main.get(SlotUnderMouse).getItem() == Items.TOTEM_OF_UNDYING)
                            OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, SlotUnderMouse, 40, SlotActionType.SWAP, OnyxClient.getClient().player);
                        return;
                    }
                    ItemStack mainHand = inv.main.get(inv.selectedSlot);
                    if (mainHand.isEmpty() || this.getBoolSetting("ForceTotem") && mainHand.getItem() != Items.TOTEM_OF_UNDYING) {
                        Screen screen = (MinecraftClient.getInstance().currentScreen);
                        HandledScreen<?> gui = (HandledScreen) screen;
                        Slot slot = AccessorUtils.getSlotUnderMouse(gui);
                         if (slot == null)
                            return;
                        int SlotUnderMouse = AccessorUtils.getSlotUnderMouse(gui).getIndex();
                        if (SlotUnderMouse > 35)
                            return;
                        if (SlotUnderMouse < 0)
                            return;
                        if (SlotUnderMouse == 40)
                            return;
                        if (SlotUnderMouse == this.getIntSetting("TotemSlot"))
                            return;
                        if (inv.main.get(SlotUnderMouse).getItem() == Items.TOTEM_OF_UNDYING)
                            OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, SlotUnderMouse, inv.selectedSlot, SlotActionType.SWAP, OnyxClient.getClient().player);
                    }
                } else {
                    if (!(OnyxClient.getClient().currentScreen instanceof InventoryScreen)) {
                        invClock = -1;
                        return;
                    }
                    if (invClock == -1)
                        invClock = this.getIntSetting("Delay");
                    if (invClock > 0) {
                        invClock--;
                        return;
                    }
                    if (this.getBoolSetting("AutoSwitch"))
                        inv.selectedSlot = this.getIntSetting("TotemSlot");
                    if (inv.offHand.get(0).getItem() != Items.TOTEM_OF_UNDYING) {
                        int slot = findTotemSlot();
                        if (slot != -1) {
                            OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, slot, 40, SlotActionType.SWAP, OnyxClient.getClient().player);
                            return;
                        }
                    }
                    ItemStack mainHand = inv.main.get(inv.selectedSlot);
                    if (mainHand.isEmpty() ||
                            this.getBoolSetting("ForceTotem") && mainHand.getItem() != Items.TOTEM_OF_UNDYING) {
                        int slot = findTotemSlot();
                        if (slot != -1) {
                            OnyxClient.getClient().interactionManager.clickSlot(((InventoryScreen) OnyxClient.getClient().currentScreen).getScreenHandler().syncId, slot, inv.selectedSlot, SlotActionType.SWAP, OnyxClient.getClient().player);
                        }
                    }
                }
            }
        }
    }

            private int findTotemSlot()
            {
                PlayerInventory inv = OnyxClient.getClient().player.getInventory();
                for (int i = 9; i < 36; i++)
                {
                    if (inv.main.get(i).getItem() == Items.TOTEM_OF_UNDYING)
                        return i;
                }
                return -1;
            }
        }

