package net.onyx.client.modules.render;

import net.minecraft.world.chunk.BlockEntityTickInvoker;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.render.RenderWorldEvent;
import net.onyx.client.interfaces.mixin.IWorld;
import net.onyx.client.misc.Colour;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.RenderUtils;

import java.util.HashMap;
import java.util.List;

public class BlockESP extends Module {
    // Currently for ticker entities only!
    // TODO make it work for all blocks

    public BlockESP() {
        super("BlockESP");

        this.addSetting(new Setting("Blocks", new HashMap<String, Boolean>()));
    
        this.setDescription("Makes specific blocks visible through walls.");

        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(RenderWorldEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWorldEvent.class);
    }

    @SuppressWarnings("unchecked")
    private boolean shouldRender(BlockEntityTickInvoker ticker) {
        return ((HashMap<String, Boolean>)(this.getSetting("Blocks").value)).containsKey(ticker.getName());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldEvent": {
                RenderWorldEvent e = (RenderWorldEvent)event;

                Colour colour = OnyxClient.getInstance().config.storageColour;

                List<BlockEntityTickInvoker> tickers = ((IWorld)(OnyxClient.getClient().world)).getBlockEntityTickers();
                for (BlockEntityTickInvoker ticker : tickers) {
                    if (!this.shouldRender(ticker)) continue;
                    RenderUtils.renderBlockBox(e.mStack, ticker.getPos(), colour.r, colour.g, colour.b, colour.a);
                }

                break;
            }
        }
    }
}
