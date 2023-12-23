package net.onyx.client.modules.render;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class Ambience extends Module {


    public Ambience() {
        super("Ambience");

        this.setDescription("Change color of curtain things");

        this.setCategory("Render");

        this.addSetting(new Setting("Sky", false) {{
            this.setDescription("Change sky color");
        }});
        this.addSetting(new Setting("NetherSky", false) {{
            this.setDescription("Change sky color");
        }});
        this.addSetting(new Setting("EndSky", false) {{
            this.setDescription("Change sky color");
        }});
        this.addSetting(new Setting("CustomCloud", false) {{
            this.setDescription("Change cloud color");
        }});




        this.addSetting(new SkySetting("SkyRed", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of red in the sky");
        }});

        this.addSetting(new SkySetting("SkyGreen", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of green in the sky");
        }});

        this.addSetting(new SkySetting("SkyBlue", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of blue in the sky");
        }});

//nether

        this.addSetting(new NetherSky("NetherSkyRed", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of red in the sky");
        }});

        this.addSetting(new NetherSky("NetherSkyGreen", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of green in the sky");
        }});

        this.addSetting(new NetherSky("NetherSkyBlue", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of blue in the sky");
        }});

//end

        this.addSetting(new EndSky("EndSkyRed", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of red in the sky");
        }});

        this.addSetting(new EndSky("EndSkyGreen", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of green in the sky");
        }});

        this.addSetting(new EndSky("EndSkyBlue", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of blue in the sky");
        }});

//cloud

        this.addSetting(new CustomCloud("CloudRed", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of red in the cloud");
        }});

        this.addSetting(new CustomCloud("CloudGreen", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of green in the cloud");
        }});

        this.addSetting(new CustomCloud("CloudBlue", 0) {{
            this.setMax(255);
            this.setMin(0);
            this.setDescription("The amount of blue in the cloud");
        }});

    }


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        reload();
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        reload();
    }

public static boolean shouldSky;
    public static int skyRed;
    public static int skyGreen;
    public static int skyBlue;

    public static boolean shouldNetherSky;
    public static int netherSkyRed;
    public static int netherSkyGreen;
    public static int netherSkyBlue;


    public static boolean shouldEndSky;
    public static int endSkyRed;
    public static int endSkyGreen;
    public static int endSkyBlue;

    public static boolean shouldCloud;
    public static int cloudRed;
    public static int cloudGreen;
    public static int cloudBlue;



    private void reload()
    {
        if (OnyxClient.getClient().worldRenderer != null && isEnabled()) OnyxClient.getClient().worldRenderer.reload();
    }

    public static class Custom extends DimensionEffects {
        public Custom() {
            super(Float.NaN, true, DimensionEffects.SkyType.END, true, false);
        }

        @Override
        public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
            return color.multiply(0.15000000596046448D);
        }

        @Override
        public boolean useThickFog(int camX, int camY) {
            return false;
        }

        @Override
        public float[] getFogColorOverride(float skyAngle, float tickDelta) {
            return null;
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                shouldSky = this.getBoolSetting("Sky");
                shouldNetherSky = this.getBoolSetting("NetherSky");
                shouldEndSky = this.getBoolSetting("EndSky");
                shouldCloud = this.getBoolSetting("CustomCloud");
            }
            if (shouldSky) {
                skyRed = this.getIntSetting("SkyRed");
                skyGreen = this.getIntSetting("SkyGreen");
                skyBlue = this.getIntSetting("SkyBlue");
            }
            if (shouldNetherSky) {
                netherSkyRed = this.getIntSetting("NetherSkyRed");
                netherSkyGreen = this.getIntSetting("NetherSkyGreen");
                netherSkyBlue = this.getIntSetting("NetherSkyBlue");
            }
            if (shouldEndSky) {
                endSkyRed = this.getIntSetting("EndSkyRed");
                endSkyGreen = this.getIntSetting("EndSkyGreen");
                endSkyBlue = this.getIntSetting("EndSkyBlue");
            }
            if (shouldCloud) {
                cloudRed = this.getIntSetting("CloudRed");
                cloudGreen = this.getIntSetting("CloudGreen");
                cloudBlue = this.getIntSetting("CloudBlue");
            }
        }
    }



    private class SkySetting extends Setting {
        public SkySetting(String name, Object value) {
            super(name, value);

            this.setCategory("Sky");
        }

        @Override
        public boolean shouldShow() {
            return getBoolSetting("Sky");
        }
    }

    private class NetherSky extends Setting {
        public NetherSky(String name, Object value) {
            super(name, value);

            this.setCategory("NetherSky");
        }

        @Override
        public boolean shouldShow() {
            return getBoolSetting("NetherSky");
        }
    }


    private class EndSky extends Setting {
        public EndSky(String name, Object value) {
            super(name, value);

            this.setCategory("EndSky");
        }

        @Override
        public boolean shouldShow() {
            return getBoolSetting("EndSky");
        }
    }


    private class CustomCloud extends Setting {
        public CustomCloud(String name, Object value) {
            super(name, value);

            this.setCategory("CloudColor");
        }

        @Override
        public boolean shouldShow() {
            return getBoolSetting("CustomCloud");
        }
    }
}


