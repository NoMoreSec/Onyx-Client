package net.onyx.client.modules.render;

import net.onyx.client.config.settings.Setting;
import net.onyx.client.modules.DummyModule;

public class OptimalObiHighlight extends DummyModule {
    public OptimalObiHighlight() {
        super("OptimalObiHighlight");

        this.setDescription("");

        this.addSetting(new ColorSetting("Red", 0){{
            this.setMax(255);
            this.setMin(0);
        }});

        this.addSetting(new ColorSetting("Green", 0){{
            this.setMax(255);
            this.setMin(0);
        }});
        this.addSetting(new ColorSetting("Blue", 0){{
            this.setMax(255);
            this.setMin(0);
        }});

        this.setCategory("Render");
    }

    public static boolean enabled = false;



    @Override
    public void activate() {
        enabled = true;
    }

    @Override
    public void deactivate() {
        enabled = false;
    }

    private class ColorSetting extends Setting {
        public ColorSetting(String name, Object value) {
            super(name, value);

            this.setCategory("Color");
        }

        @Override
        public boolean shouldShow() {
            return isEnabled();
        }
    }
}
