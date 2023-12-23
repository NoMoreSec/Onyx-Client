package net.onyx.client.modules.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class MiddleClickXP extends Module {


    public MiddleClickXP() {
        super("AutoXP");

        this.setDescription("");

        this.setCategory("Combat");

        this.addSetting(new Setting("Cooldown", 0) {{
            this.setMin(0);
            this.setMax(20);
            this.setDescription("Speed at which you place XP");
        }});

    }

    private Integer cooldown = 0;

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.cooldown = 0;
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }




    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (OnyxClient.getClient().currentScreen != null) {
                    return;
                }
                if (OnyxClient.getClient().options.useKey.isPressed()) {
                    if (this.cooldown > 0) {
                        this.cooldown--;
                        break;
                    }
                    ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
                    if (!mainHandStack.isOf(Items.EXPERIENCE_BOTTLE)) {
                        return;
                    }
                    OnyxClient.getClient().interactionManager.interactItem(OnyxClient.getClient().player, Hand.MAIN_HAND);
                    OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                    this.cooldown = this.getIntSetting("Cooldown");

                }
            }
        }
    }
}

