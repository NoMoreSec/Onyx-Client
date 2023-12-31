package net.onyx.client.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.render.OnRenderEvent;
import net.onyx.client.events.render.RenderWorldViewBobbingEvent;
import net.onyx.client.interfaces.mixin.IEntity;
import net.onyx.client.interfaces.mixin.IWorld;
import net.onyx.client.misc.Colour;
import net.onyx.client.misc.EntityFlags;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.MathsUtils;
import net.onyx.client.utils.RenderUtils;

import java.util.HashMap;
import java.util.List;

public class Tracers extends Module {

    public Tracers() {
        super("Tracers");

        this.addSetting(new Setting("Player", true));
        this.addSetting(new Setting("Mob", true));
        this.addSetting(new Setting("Item", true));
        this.addSetting(new Setting("OtherEntities", true));
        this.addSetting(new Setting("Invisible", false));

        // TODO add block search
        // TODO add entity search mode

        // Block/Ticker search
        this.addSetting(new Setting("Block", false));
        this.addSetting(new Setting("Blocks", new HashMap<String, Boolean>()));

        // Rendering
        this.addSetting(new Setting("Transparency", 1f));

        this.setDescription("Draws tracers to specified targets.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(RenderWorldViewBobbingEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(RenderWorldViewBobbingEvent.class);
    }

    public boolean shouldDrawTracer(Entity ent) {
        boolean drawPlayer        = (boolean)this.getSetting("Player").value;
        boolean drawMob           = (boolean)this.getSetting("Mob").value;
        boolean drawItem          = (boolean)this.getSetting("Item").value;
        boolean drawOtherEntities = (boolean)this.getSetting("OtherEntities").value;
        boolean drawInvisible     = (boolean)this.getSetting("Invisible").value;

        IEntity iEnt = (IEntity)ent;

        boolean isMob       = ent instanceof MobEntity;
        boolean isPlayer    = ent instanceof PlayerEntity;
        boolean isItem      = ent instanceof ItemEntity;
        boolean isInvisible = iEnt.getEntFlag(EntityFlags.INVISIBLE_FLAG_INDEX);

        return (
                (drawPlayer && isPlayer)
                        ||  (drawMob && isMob)
                        ||  (drawItem && isItem)
                        ||  (drawInvisible && isInvisible)
                        ||  (drawOtherEntities && !isMob && !isPlayer && !isItem)
        );
    }

    @SuppressWarnings("unchecked")
    public boolean shouldDrawTracer(BlockEntityTickInvoker block) {
        HashMap<String, Boolean> phrases = (HashMap<String, Boolean>)this.getSetting("Blocks").value;

        return phrases.containsKey(block.getName());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldViewBobbingEvent": {
                RenderWorldViewBobbingEvent e = (RenderWorldViewBobbingEvent)event;
                e.cancel = true;


                break;
            }

            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;
                float transparency = (Float)this.getSetting("Transparency").value;

                // Render entity/player tracers
                Iterable<Entity> ents = OnyxClient.getClient().world.getEntities();
                for (Entity entity : ents) {
                    if (!this.shouldDrawTracer(entity)) continue;

                    // No render myself.
                    if (entity instanceof PlayerEntity && (PlayerEntity)entity == OnyxClient.me()) {
                        continue;
                    }

                    // TODO add different colours for different entities
                    Colour c = OnyxClient.getInstance().config.entityColour;

                    // Render tracers
                    RenderUtils.drawTracer(e.mStack, MathsUtils.getLerpedCentre(entity, e.tickDelta), e.tickDelta, c.r, c.g, c.b, c.a*transparency);
                }

                if ((boolean)this.getSetting("Block").value) {
                    // Render blocks etc.
                    List<BlockEntityTickInvoker> tickers = ((IWorld)(OnyxClient.getClient().world)).getBlockEntityTickers();
                    for (BlockEntityTickInvoker ticker : tickers) {
                        if (!this.shouldDrawTracer(ticker)) continue;

                        // This is only storage colouring for now but whatever.
                        // TODO what if it isn't storage!?
                        Colour c = OnyxClient.getInstance().config.storageColour;

                        RenderUtils.drawTracer(e.mStack, BlockUtils.blockPos(ticker.getPos()).add(0.5, 0.5, 0.5), e.tickDelta, c.r, c.g, c.b, c.a * transparency);
                    }
                }

                break;
            }
        }
    }


    }

