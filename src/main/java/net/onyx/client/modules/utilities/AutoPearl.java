package net.onyx.client.modules.utilities;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class AutoPearl extends DummyModule {

    public static boolean enabled = false;

    public AutoPearl() {
        super("AutoPearl");

        this.setDescription("NoSwing");

        this.setCategory("Utility");

        this.addSetting(new Setting("Client", true) {{
            this.setDescription("");
        }});

        this.addSetting(new Setting("Server", true) {{
            this.setDescription("");
        }});
    }


    @Override
    public void activate() {
        enabled = true;
    }

    @Override
    public void deactivate() {
        enabled = false;
    }
}
