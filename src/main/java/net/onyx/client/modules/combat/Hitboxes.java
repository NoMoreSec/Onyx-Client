package net.onyx.client.modules.combat;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class Hitboxes extends DummyModule {

    public Hitboxes() {
        super("Hitboxes");

        this.addSetting(new Setting("Expansion Range", 3.0) {{
            this.setDescription("");
            this.setMin(6.0);
            this.setMin(0.0);
        }});
        this.addSetting(new Setting("Only Players", false) {{
            this.setDescription("");
        }});
        this.addSetting(new Setting("Only Sword", false) {{
            this.setDescription("");
        }});
    }

    public float getRange() {
        double range = this.getDoubleSetting("Expansion Range");
        return (float)range;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }
}
