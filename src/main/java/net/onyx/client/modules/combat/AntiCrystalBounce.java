package net.onyx.client.modules.combat;

import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.DummyModule;

public class AntiCrystalBounce extends DummyModule {


    public AntiCrystalBounce() {
        super("AntiCrystalBounce");
        this.setDescription("Stops crystals bouncing in your hotbar");
        this.setCategory("Combat");

    }

    public static boolean enabled = false;


    @Override
    public void activate() {
        enabled = true;
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        enabled = false;
        this.removeListen(ClientTickEvent.class);
    }
}

