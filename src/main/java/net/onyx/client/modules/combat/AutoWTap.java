package net.onyx.client.modules.combat;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.config.specials.Mode;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.client.OnAttackEntityEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.Input;

public class AutoWTap extends Module {
    private final Double lastValid = OnyxClient.getCurrentTime();


    public AutoWTap() {
        super("AutoWTap");

        this.setDescription("Auto W taps for you");

        this.addSetting(new Setting("Mode", new Mode("Hold", "SprintReset")));


        this.setCategory("Combat");

    }

    @Override
    public String listOption() {
        return "Packet";
    }
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(OnAttackEntityEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(OnAttackEntityEvent.class);
    }


    private int has;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (this.getModeSetting("Mode").is("Hold")) {
                    if (!OnyxClient.getClient().options.attackKey.isPressed()) {
                        has = 0;
                    }

                        if (OnyxClient.getClient().options.forwardKey.isPressed()) {
                        if (!OnyxClient.getClient().options.attackKey.isPressed()) {
                            unpress();
                        }
                    }
                    HitResult hit = OnyxClient.getClient().crosshairTarget;
                    if (hit.getType() != HitResult.Type.ENTITY)
                        return;
                    Entity target = ((EntityHitResult) hit).getEntity();
                    if (!(target instanceof PlayerEntity)) {
                        unpress();
                        return;
                    }
                    if (OnyxClient.getClient().options.forwardKey.isPressed()) {
                        if (OnyxClient.getClient().options.attackKey.isPressed()) {
                            setPressed(OnyxClient.getClient().options.backKey, true);
                        } else if (!OnyxClient.getClient().options.attackKey.isPressed()) unpress();
                    }
                }
                if (this.getModeSetting("Mode").is("SprintReset")) {
                    HitResult hit = OnyxClient.getClient().crosshairTarget;
                    if (hit.getType() != HitResult.Type.ENTITY)
                        return;
                    Entity target = ((EntityHitResult) hit).getEntity();
                    if (!(target instanceof PlayerEntity)) {
                        return;
                    }
                    if (has == 1)
                        return;
                    if (OnyxClient.getClient().options.attackKey.isPressed()) {
                        setPressed(OnyxClient.getClient().options.backKey, true);
                        unpress();
                        has++;
                    }
                }
                    break;
            }
        }
    }





    private void unpress() {
        setPressed(OnyxClient.getClient().options.backKey, false);
    }

    private void setPressed(KeyBinding key, boolean pressed) {
        key.setPressed(pressed);
        Input.setKeyState(key, pressed);
    }
}

