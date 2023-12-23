package net.onyx.client.modules.combat;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class NoSwing extends DummyModule {

    public static boolean enabled = false;

    public NoSwing() {
        super("NoSwing");

        this.setDescription("NoSwing");

        this.setCategory("Combat");

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
