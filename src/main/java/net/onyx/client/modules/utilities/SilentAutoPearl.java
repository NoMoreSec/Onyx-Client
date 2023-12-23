package net.onyx.client.modules.utilities;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class SilentAutoPearl extends DummyModule {

    public static boolean enabled = false;

    public SilentAutoPearl() {
        super("SilentAutoPearl");

        this.setDescription("SilentAutoPearl");

        this.setCategory("Utility");

        this.addSetting(new Setting("Sword", true) {{
            this.setDescription("");
        }});
        this.addSetting(new Setting("Totem", true) {{
            this.setDescription("");
        }});this.addSetting(new Setting("Anything", true) {{
            this.setDescription("");
        }});this.addSetting(new Setting("Throw Pearl", true) {{
            this.setDescription("");
        }});this.addSetting(new Setting("Equip Item After", true) {{
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
