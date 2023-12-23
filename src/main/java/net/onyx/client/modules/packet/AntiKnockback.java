package net.onyx.client.modules.packet;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class AntiKnockback extends DummyModule {

    public AntiKnockback() {
        super("AntiKnockback");

        this.addSetting(new Setting("Multiplier X", 0.0) {{
            this.setDescription("");
            this.setMax(3.0);
            this.setMin(0.0);
        }});
        this.addSetting(new Setting("Multiplier Y", 0.0) {{
            this.setDescription("");
            this.setMax(3.0);
            this.setMin(0.0);
        }});
        this.addSetting(new Setting("Multiplier Z", 0.0) {{
            this.setDescription("");
            this.setMax(3.0);
            this.setMin(0.0);
        }});
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }
}
