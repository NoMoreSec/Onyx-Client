package net.onyx.client.modules.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.render.InGameHudRenderEvent;
import net.onyx.client.misc.Colour;
import net.onyx.client.misc.GUIPos;
import net.onyx.client.modules.Module;

import java.util.*;

public class ModList extends Module {

    //TODO
    private GUIPos getPosition() {
        return GUIPos.fromInt( 1, GUIPos.Type.TOP_LEFT);
    }

    private interface ColouringMode {
        int getColour(int cur, int total);
    }

    private static class DefaultColouring implements ColouringMode {
        protected List<Integer> flat;

        public DefaultColouring() {
            this.flat = Arrays.asList(0xffffffff);
        }

        @Override
        public int getColour(int cur, int total) {
            int index = (int)((float)flat.size() * ((float)cur/(float)total));
            return this.flat.get(index);
        }
    }
    private static class TransColouring extends DefaultColouring {
        public TransColouring() {
            this.flat = Arrays.asList(0xff55cdfc,
                    0xfff7a8b8,
                    0xffffffff,
                    0xfff7a8b8,
                    0xff55cdfc);
        }
    }
    private static class LGBTColouring extends DefaultColouring {
        public LGBTColouring() {
            this.flat = Arrays.asList(0xffe40303,
                    0xffff8c00,
                    0xffffed00,
                    0xff008026,
                    0xff004dff,
                    0xff750787);
        }
    }
    private static class RGBColouring implements ColouringMode {
        public RGBColouring() {
            // Generate all of the steps, it is hard work but at least we only need to do it once.
            this.generateSteps();

            // We need to run a tick so we can generate at least one set.
            this.tick();
        }

        private Module getModList() {
            return OnyxClient.getInstance().getModules().get("modlist");
        }

        private int getSpeed() {
            return 1;
        }

        // This is used to say where the head of the function is (i.e. 0)
        private int head = 0;
        private void generateSteps() {
            // The 763 is the period of the "step" function (i.e. the thing generating all of the colours).
            this.steps = new Colour[763];

            // This is just from an RGB thing I found on a site this https://codepen.io/Codepixl/pen/ogWWaK
            // Thanks <3

            // The "steps" function
            int r = 255, g = 0, b = 0;
            for (int i = 0; i < this.steps.length; i++) {
                if (r > 0 && b == 0){
                    r--;
                    g++;
                }
                if (g > 0 && r == 0){
                    g--;
                    b++;
                }
                if (b > 0 && g == 0){
                    r++;
                    b--;
                }

                // Place the new colour that we generated in the steps array.
                this.steps[i] = new Colour(r, g, b, 255);
            }
        }
        
        // This is used to store all of the possible colours
        private Colour[] steps;
        
        // This is used to store or of the possible colours for all of the modules for the active tick.
        private List<Colour> set;

        // This will prepare all of the colours so we can tick over them all at a constant rate (i.e. client TPS)
        private void tick() {
            set = new ArrayList<Colour>();
            int totalModules = OnyxClient.getInstance().getModules().keySet().size();
            int speed = this.getSpeed();

            // Loop through them all as if we are wanting them their and then.
            for (int cur = 0; cur < totalModules; cur++) {
                int len = this.steps.length;

                // If the cur == 0 then that must mean that it is the head.
                if (cur == 0) {
                    // This adds one to head
                    head += speed;

                    // Technically this does mean that head will infinitely increase, however for it to be a problem the user needs to be on MC for a super long time, which all it would mean is that the int would wrap round to the negatives.
                    // ...which would cause an error as it would be looking for a negative index in the set array... so I guess I will fix this...
                    // So let's check for that
                    head = head < 0 ? speed : head; 
                }
                
                // Get the element behind the head.
                int i = head - cur*speed;

                // If it is less than zero then loop it around the array, else then just leave it be.
                i = (i < 0 ? len - i : i);

                // Add the step to the set
                // ...making sure that i is less than len.
                set.add(this.steps[i % len]);
            }
        }

        // Return a colour for a given module.
        @Override
        public int getColour(int cur, int total) {
            // Get the colour that would have been generated for this index
            Colour c = this.set.get(cur);

            // Return it as an integer
            return c.toARGB();
        }
    }

    private HashMap<String, ColouringMode> colouringModes;

    public ModList() {
        super("ModList", true);
        
        this.setDescription("Displays all of your enabled mods");
        this.modListDisplay = false;
        this.addSetting(new Setting("Scale", 1.0f));
        this.setCategory("HUD");
    }



    @Override
    public void activate() {
        // Setup colouring modes
        colouringModes = new HashMap<String, ColouringMode>() {{
            put("default",  new DefaultColouring());
            put("trans",    new TransColouring());
            put("rgb",      new RGBColouring());
            put("lgbt",     new LGBTColouring());
        }};

        this.addListen(InGameHudRenderEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    public static final Identifier WATERMARK_TEXTURE = new Identifier("walksy-client", "textures/misc/watermark.png");

    public static final int BACKGROUND_WIDTH = 425;
    public static final int BACKGROUND_HEIGHT = 170;
    public static void render(MatrixStack matrixStack, Double scale, double x, double y) {
        matrixStack.push();
        matrixStack.translate(x, y, 0);

        RenderSystem.setShaderTexture(0, WATERMARK_TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        // Scaling
        int width  = (int)(BACKGROUND_WIDTH * scale);
        int height = (int)(BACKGROUND_HEIGHT * scale);

        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, width, height, width, height);

        matrixStack.pop();
    }

    public static void render(MatrixStack matrixStack, Double scale) {
        // Scaling
        int width = (int)(BACKGROUND_WIDTH * scale);
        int height = (int)(BACKGROUND_HEIGHT * scale);

        int x = 0;
        int y = 0;

        matrixStack.translate(x, y, 0);

        RenderSystem.setShaderTexture(0, WATERMARK_TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, width, height, width, height);

        matrixStack.translate(-x, -y, 0);
    }


    @Override
    public void fireEvent(Event event) {
        // Hide with F3
        if (OnyxClient.getClient().options.debugEnabled) return;

        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {


                break;
            }
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                TextRenderer textRenderer = OnyxClient.getInstance().textRenderer;
                List<Module> enabledMods = new ArrayList<Module>();
                render(e.mStack, 1.7 / 6);
                
                for (String moduleName : OnyxClient.getInstance().getModules().keySet()) {
                    Module module = OnyxClient.getInstance().getModules().get(moduleName);

                    if (!module.shouldDisplayInModList()) continue;
                    
                    enabledMods.add(module);
                }

                // Sort the enabledMods list by the module name
                Collections.sort(enabledMods, (c1, c2) -> {
                    return c2.getTextWidth(textRenderer) - c1.getTextWidth(textRenderer);
                });

                float scale = (float)this.getSetting("Scale").value;

                e.mStack.push();
                e.mStack.scale(scale, scale, 0);

                int display = 0;

                // Positioning stuff
                GUIPos pos = getPosition();

                // Window sizes
                Window window = OnyxClient.getClient().getWindow();
                int MAX_WIDTH  = window.getScaledWidth();
                int MAX_HEIGHT = window.getScaledHeight();

                for (Module module : enabledMods) {
                    // Positioning based stuff
                    float actualWScale = pos.isRight() ? scale : 1;
                    float actualHScale = pos.isBottom() ? scale : 1;

                    // Do some magic
                    float deltaWidth = ((float)MAX_WIDTH * (actualWScale - 1))/actualWScale;
                    float deltaHeight = ((float)MAX_HEIGHT * (actualHScale - 1))/actualHScale;

                    int x = pos.isRight() ? MAX_WIDTH - (int)((float)module.getTextWidth(textRenderer)) - (int)deltaWidth : 0;

                    int y = 45 + 10*(display + (pos.isBottom() ? 1 : 0)) + (int)deltaHeight;
                    y = pos.isBottom() ? MAX_HEIGHT - y : y;

                    int purpleColor = 0xFF8405EB;
                    x = textRenderer.drawWithShadow(e.mStack, module.getName(), x, y, purpleColor); // COLOR HERE

                    if (module.hasListOption()) {
                        // The +2 is just for the space
                        textRenderer.drawWithShadow(e.mStack, String.format("[%s]", module.listOption()), x + 2, y, 0xFF4605d6); //COLOR HERE
                    }

                    display++;
                }

                e.mStack.pop();
            }
        } 
    }
}
