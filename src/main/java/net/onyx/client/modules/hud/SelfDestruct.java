package net.onyx.client.modules.hud;

import net.onyx.client.OnyxClient;
import net.onyx.client.modules.DummyModule;
import net.onyx.client.modules.Module;

public class SelfDestruct extends DummyModule {


    public SelfDestruct() {
        super("SelfDestruct");
        this.setDescription("SelfDestruct");

        this.setCategory("HUD");
    }



    @Override
    public void activate() {
        OnyxClient.getClient().setScreen(null);
        for (Module module : OnyxClient.getInstance().getModules().values()) {
            if (module.isEnabled()) module.disable();
        }
        try {
            System.gc();
            System.runFinalization();
            System.gc();
            Thread.sleep(100L);
            System.gc();
            System.runFinalization();
            Thread.sleep(200L);
            System.gc();
            System.runFinalization();
            Thread.sleep(300L);
            System.gc();
            System.runFinalization();
        } catch (InterruptedException e) {
            OnyxClient.displayChatMessage("Self Destruct Failed.");
            throw new RuntimeException(e);
        }
        System.gc();
            }


    @Override
    public void deactivate() {
    }
}
