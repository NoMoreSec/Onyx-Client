package net.onyx.client.modules.render;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.config.specials.Mode;
import net.onyx.client.events.Event;
import net.onyx.client.events.render.GetCapeTextureEvent;
import net.onyx.client.modules.Module;

public class Capes extends Module {


    public Capes() {
        super("Capes");

        this.setDescription("Renders a cape on the player");

        this.setCategory("Render");

        this.addSetting(new Setting("Mode", new Mode("Walksy", "Frwost", "WalksyClient", "KFC")));


    }

    private static boolean isEnabled;

    @Override
    public void activate() {
        this.addListen(GetCapeTextureEvent.class);
        isEnabled = true;
    }

    @Override
    public void deactivate() {
        this.removeListen(GetCapeTextureEvent.class);
        isEnabled = false;
    }

    public Identifier getTexture(PlayerEntity player) {
        if (isEnabled && (player == OnyxClient.getClient().player)) {
            return getIdentifier();
        } else {
            return null;
        }
    }


    public Identifier getIdentifier() {
        Identifier iden = new Identifier("walksy-client", "capes/walksyclient.png");
        if (this.getModeSetting("Mode").is("Frwost"))
        {
            iden = new Identifier("walksy-client", "capes/frwost.png");

        }

        else if (this.getModeSetting("Mode").is("WalksyClient"))

        {
            iden = new Identifier("walksy-client", "capes/walksyclient.png");

        }
        else if (this.getModeSetting("Mode").is("KFC"))

        {
            iden = new Identifier("walksy-client", "capes/kfc.png");
        }
        return iden;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetCapeTextureEvent": {
                GetCapeTextureEvent e = (GetCapeTextureEvent)(event);
                PlayerEntity player = OnyxClient.getClient().player;

                Identifier id = getTexture(((player)));
                if (id != null) e.cir.setReturnValue(id);
            }
        }
    }
}





