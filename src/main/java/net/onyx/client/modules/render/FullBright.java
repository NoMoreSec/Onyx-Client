package net.onyx.client.modules.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.config.specials.Mode;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.interfaces.mixin.SimpleOptionAccessor;
import net.onyx.client.modules.Module;

public class FullBright extends Module {
    private Double normalGamma = 0d;
    Boolean hasSetGamma = false;

    public FullBright() {
        super("FullBright");
        
        this.setDescription("Allows you to see anywhere as if it was day.");

        this.addSetting(new Setting("Mode", new Mode("Gamma", "Potion")));

        this.setCategory("Render");
    }

    @Override
    public String listOption() {
        return this.getModeSetting("Mode").getStateName();
    }
    
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    private void restoreGamma() {
        if (!this.hasSetGamma) return;

        MinecraftClient client = OnyxClient.getClient();
        client.options.getGamma().setValue(this.normalGamma);
        this.hasSetGamma = false;
    }

    private void restoreEffect() {
        if (OnyxClient.me().hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            OnyxClient.me().removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);

        this.restoreGamma();
        this.restoreEffect();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                switch (this.getModeSetting("Mode").getStateName()) {
                    case "Potion": {
                        // Restore other mod's gamma
                        this.restoreGamma();

                        OnyxClient.me().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3, 1, true, true));

                        break;
                    }

                    case "Gamma": {
                        MinecraftClient client = OnyxClient.getClient();

                        if (!this.hasSetGamma) {
                            this.normalGamma = client.options.getGamma().getValue();
                            this.hasSetGamma = true;
        
                            this.restoreEffect();
                        }
        
                        SimpleOptionAccessor<Double> accessor = (SimpleOptionAccessor<Double>)(Object)(client.options.getGamma());
                        accessor.setUnsafeValue(16d);

                        break;
                    }
                }
            }
        }
    }
}
