package net.onyx.client.modules;

import net.onyx.client.OnyxClient;
import net.onyx.client.modules.chat.CommandAutoFill;
import net.onyx.client.modules.chat.InfChat;
import net.onyx.client.modules.combat.*;
import net.onyx.client.modules.hud.*;
import net.onyx.client.modules.packet.*;
import net.onyx.client.modules.render.*;
import net.onyx.client.modules.utilities.*;

public class ModuleAdder {

    public ModuleAdder(boolean multithreaded) {
        if (multithreaded) {
            Thread instanceThread = new Thread(this::addInstanceModules);
            Thread classThread = new Thread(this::addClassModules);

            instanceThread.start();
            classThread.start();

            try {
                instanceThread.join();
                classThread.join();
            } catch (InterruptedException e) {
                OnyxClient.log("Failed to launch the modules in multithreaded mode: " + e.getMessage());
            }
        } else {
            addInstanceModules();
            addClassModules();
        }
    }

    @Deprecated
    public <T extends Module> void addModule(Class<T> module) {
        OnyxClient.addModule(module);
    }

    public void addModule(Module module) {
        OnyxClient.addModule(module);
    }

    public void addInstanceModules() {
        addModule(new MiddleClickPearl());
        addModule(new EntityESP());
        addModule(new NoWeather());
        addModule(new NoBoss());
        addModule(new XRay());
        addModule(new TapeMeasure());
        addModule(new ModList());
        addModule(new NoHurtCam());
        addModule(new FullBright());
        addModule(new AutoRespawn());
        addModule(new NoFireCam());
        addModule(new Waypoints());
        addModule(new ItemRenderTweaks());
        addModule(new BlockESP());
        addModule(new ShulkerPeak());
        addModule(new PacketLimiter());
        addModule(new SpeedHack());
        addModule(new NoSubmerge());
        addModule(new Watermark());
        addModule(new FreeCam());
        addModule(new AntiInvisible());
        addModule(new NoRespondAlert());
        addModule(new Binds());
        addModule(new TotemPopCount());
        addModule(new InfChat());
        addModule(new SignSearch());
        addModule(new AntiResourcePack());
        addModule(new HClip());
        addModule(new NoSlow());
        addModule(new AntiKick());
        addModule(new CommandAutoFill());
        addModule(new EntityOwner());
        addModule(new Aimbot());
        addModule(new Hitmarker());
        addModule(new PearlDoubleHand());
        addModule(new ArmourDisplay());
        addModule(new Blink());
        addModule(new AutoAnchorBreak());
        addModule(new AntiDoubleTap());
        addModule(new ClickGUI());
        addModule(new FakePlayer());
        addModule(new AntiShield());
        addModule(new TriggerBot());
        addModule(new CwCrystal());
        addModule(new MarlowCrystal());
        addModule(new AnchorMacro());
        addModule(new SafeAnchor());
        addModule(new AutoLoot());
        addModule(new MiddleClickXP());
        addModule(new AutoInventoryTotem());
        addModule(new Ambience());
        addModule(new CPvpItemRefresh());
        addModule(new NameTags());
        addModule(new AntiTpTrap());
        addModule(new AutoDtap());
        addModule(new ShowDamageTick());
        addModule(new Capes());
        addModule(new SelfDestruct());
        addModule(new AutoWTap());
        addModule(new CrystalPlaceCheck());
        addModule(new PearlCoords());
        addModule(new MarlowOptimizer());
        addModule(new RecordPackets());
        addModule(new Sprint());
        addModule(new AutoTotem());
        addModule(new SkeletonESP());
        addModule(new Criticals());
        addModule(new NoDesyncPlace());
        //addModule(new ElytraAI());
        addModule(new OptimalObiHighlight());
        //addModule(new WebhookDispatcher());
        addModule(new DiscordRPC());
        addModule(new Trails());
        addModule(new AntiCrystalBounce());
        addModule(new GhostObsidian());
        addModule(new GhostAnchor());
        addModule(new CounterPearlAbuse());
        addModule(new SilentAutoPearl());
        addModule(new Tracers());
        addModule(new FemboyMod());
        addModule(new NoItemRender());
        addModule(new NoParticles());
        addModule(new NoPortal());
        addModule(new AutoShield());
        addModule(new UnfocusCPU());
        addModule(new FastUse());
        addModule(new NoSwing());
        addModule(new BreakDelayerRemover());
        addModule(new NoBreak());
        addModule(new MidClickPearl());
        addModule(new PearlPhase());
        addModule(new ObiHightlight());
        addModule(new AutoPearl());
        addModule(new Reach());
        addModule(new Hitboxes());
        addModule(new AntiKnockback());
        addModule(new NoBreakDelay());
        addModule(new SlowHandSwing());
    }

    public void addClassModules() {
        addModule(TotemHit.class);
    }
}