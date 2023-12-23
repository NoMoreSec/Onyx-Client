package net.onyx.client.modules.combat;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import org.lwjgl.glfw.GLFW;

public class PearlDoubleHand extends Module {
    private Integer totemSlot = 0;
    public PearlDoubleHand() {
        super("PearlDoubleHand");

        this.setDescription("After pearling it switches to your totem");

        this.setCategory("Combat");
        this.addSetting(new Setting("Totem Slot", 0) {{
            this.setMin(0);
            this.setMax(8);
        }});

    }
    @Override
    public void fireEvent(Event event) {
        if (GLFW.glfwGetMouseButton(OnyxClient.getClient().getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS){
            ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
            PlayerInventory inventory = OnyxClient.getClient().player.getInventory();
            if (mainHandStack.getItem() == Items.ENDER_PEARL) {
                this.totemSlot = this.getIntSetting("Totem Slot");
                inventory.selectedSlot = totemSlot;
            }
        }

    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }
}
