package net.onyx.client.modules.utilities;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class NoBreakDelay extends DummyModule {

    public NoBreakDelay() {
        super("NoBreakDelay");

        this.addSetting(new Setting("Break Delay", 0) {{
            this.setDescription("");
            this.setMax(5);
            this.setMin(0);
        }});
    }

    @Override
    public void activate() {
        super.activate();
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }
}
