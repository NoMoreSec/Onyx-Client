package net.onyx.client.modules.combat;

import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.InventoryUtils;
import net.onyx.client.utils.WorldUtils;
import org.lwjgl.glfw.GLFW;

public class MiddleClickPearl extends Module {

    private boolean isMiddleClicking = false;
public MiddleClickPearl() {
        super("MiddleClickPearl");

        this.setDescription("i made this inside github");

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
    if (GLFW.glfwGetMouseButton(WorldUtils.mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS && !isMiddleClicking) {
        isMiddleClicking = true;
        InventoryUtils.selectItemFromHotbar(Items.ENDER_PEARL);
        OnyxClient.getClient().interactionManager.interactItem(OnyxClient.getClient().player, Hand.MAIN_HAND);
    }
    if (GLFW.glfwGetMouseButton(WorldUtils.mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_RELEASE && isMiddleClicking) {
        isMiddleClicking = false;
        }
      }
    }

