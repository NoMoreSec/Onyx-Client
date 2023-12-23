package net.onyx.client.modules.utilities;

import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.client.OnClientCloseEvent;
import net.onyx.client.modules.DummyModule;
import net.onyx.client.modules.utilities.discordrpcutils.DiscordIPC;
import net.onyx.client.modules.utilities.discordrpcutils.RichPresence;

public class DiscordRPC extends DummyModule {


    public DiscordRPC() {
        super("DiscordRPC", false);

        this.setDescription("Connects to your discord");

        this.setCategory("Utility");

    }

    private static final RichPresence rpc = new RichPresence();
    String largeText = "%s %s".formatted("Onyx Client", "v4");

    @Override
    public void activate() {
        DiscordIPC.start(1139809688674897922L, null);

        rpc.setStart(System.currentTimeMillis() / 1000L);
        rpc.setLargeImage("onyx", largeText);
        rpc.setDetails("Using Onyx.ss v4");

        DiscordIPC.setActivity(rpc);
        this.addListen(OnClientCloseEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        DiscordIPC.stop();
        this.removeListen(OnClientCloseEvent.class);
        this.removeListen(ClientTickEvent.class);

    }
}





