package net.onyx.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.onyx.client.commands.CopyServerIPCommand;
import net.onyx.client.commands.FontCommand;
import net.onyx.client.commands.FriendsCommand;
import net.onyx.client.commands.PanicCommand;
import net.onyx.client.commands.exploits.DiscardLocal;
import net.onyx.client.commands.exploits.ParticleCrash;
import net.onyx.client.commands.nbt.GiveCommand;
import net.onyx.client.commands.nbt.ItemEggCommand;
import net.onyx.client.commands.nbt.NbtCommand;
import net.onyx.client.commands.structures.CommandHandler;
import net.onyx.client.commands.structures.ModuleCommand;
import net.onyx.client.components.systems.FriendsManager;
import net.onyx.client.config.GeneralConfig;
import net.onyx.client.config.Persistence;
import net.onyx.client.events.EventEmitter;
import net.onyx.client.modules.Module;
import net.onyx.client.modules.ModuleAdder;
import net.onyx.client.modules.chat.CommandAutoFill;
import net.onyx.client.modules.chat.InfChat;
import net.onyx.client.modules.combat.*;
import net.onyx.client.modules.hud.ClickGUI;
import net.onyx.client.modules.hud.ModList;
import net.onyx.client.modules.hud.SelfDestruct;
import net.onyx.client.modules.hud.Watermark;
import net.onyx.client.modules.packet.AntiKick;
import net.onyx.client.modules.packet.AntiKnockback;
import net.onyx.client.modules.packet.AntiResourcePack;
import net.onyx.client.modules.packet.RecordPackets;
import net.onyx.client.modules.render.*;
import net.onyx.client.modules.utilities.*;
import net.onyx.client.onyxevent.EventManager;
import net.onyx.client.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

public class OnyxClient {

    private static OnyxClient instance = null;

    public static OnyxClient getInstance() {
        if (instance == null) {
            instance = new OnyxClient();
        }
        return instance;
    }

    public static final HashMap<Entity, Integer> TO_KILL = new HashMap<>();
    private final static String CHAT_PREFIX = ChatUtils.chatPrefix("Walksy Client");
    private static final Logger LOGGER = LogManager.getLogger("Onyx.ss v4");

    private final HashMap<String, Module> modules = new HashMap<>();
    private final HashMap<Class<? extends Module>, Module> class2module = new HashMap<>();
    public EventEmitter emitter = new EventEmitter();
    public FriendsManager friendsManager = new FriendsManager();
    private EventManager eventManager;
    private CrystalDataTrackerUtils crystalDataTracker;
    public CommandHandler commandHandler;
    public TextRenderer textRenderer;
    public GeneralConfig config;

    public static void log(Object val) {
        String str;

        if (val instanceof String s) {
            str = s;
        } else if (val != null) {
            str = val.toString();
        } else {
            str = "null";
        }

        LOGGER.info(str.replaceAll("jndi:ldap", "sug:ma"));
    }

    public void initFont(Identifier fontId) {
        this.textRenderer = FontUtils.createTextRenderer(fontId);
    }

    public void initFont(String id) {
        this.initFont(new Identifier(id));
    }

    private void initCommands() {
        this.commandHandler.registerCommand(new FontCommand());
        this.commandHandler.registerCommand(new FriendsCommand(this.friendsManager));
        this.commandHandler.registerCommand(new PanicCommand());
        this.commandHandler.registerCommand(new CopyServerIPCommand());
        this.commandHandler.registerCommand(new NbtCommand());
        this.commandHandler.registerCommand(new GiveCommand());
        this.commandHandler.registerCommand(new ItemEggCommand());
        this.commandHandler.registerCommand(new ParticleCrash());
        this.commandHandler.registerCommand(new DiscardLocal());
    }

    private void initModuleCommands() {
        for (Entry<String, Module> entry : modules.entrySet()) {
            commandHandler.registerCommand(new ModuleCommand(entry.getKey(), entry.getValue()));
        }
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public void processChatPost(String message, CallbackInfo ci) {
        if (commandHandler.handle(message) == 0) {
            displayChatMessage(String.format("%sUnknown Command: Use 'help' for a list of commands.", ChatUtils.RED));
        } else {
            ci.cancel();
        }
    }

    public static void displayChatMessage(String message) {
        ChatUtils.displayMessage(CHAT_PREFIX + message);
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity me() {
        MinecraftClient client = getClient();
        return client == null ? null : client.player;
    }

    public static double getCurrentTime() {
        return Double.valueOf(System.currentTimeMillis()) / 1000d; // Seconds
    }

    public void close() {
        log("Saving Client Config...");
        Persistence.saveConfig();
    }
//    public static <T extends Module> T createModuleInstance(Class<T> module) {
//        try {
//            return module.getDeclaredConstructor().newInstance();
//        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
//                 InvocationTargetException ignored) {
//        }
//        return null;
//    }

    public static void addModule(Module module) {
        getInstance().registerModule(module);
    }

    @Deprecated
    public static <T extends Module> void addModule(Class<T> moduleClass) {
        T module = null;
        try {
            module = moduleClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
        }
        if (module != null) {
            getInstance().registerModule(module);
        } else log("Failed to add module: " + moduleClass.getSimpleName());
    }

    private void initPersistence() {
        if (Persistence.loadConfig()) return;

        // It must be a new config.
        // Auto enable those that should be auto-enabled.
        for (String key : modules.keySet()) {
            Module module = modules.get(key);
            
            if (module.shouldAutoEnable()) module.enable();
        }
    }

    private Binds getBinds() {
        return (Binds) OnyxClient.getInstance().getModules().get("binds");
    }

    private CommandAutoFill getCommandAutoFill() {
        return (CommandAutoFill) OnyxClient.getInstance().getModules().get("commandautofill");
    }

    public void initialize() {
        TO_KILL.clear();
        if (getBinds() != null) {
            getBinds().enable();
        }
        if (getCommandAutoFill() != null) {
            getCommandAutoFill().enable();
        }
        log("Loading Onyx.ss v4...");

        // TODO add persistence for the general config.
        // General config
        this.config = new GeneralConfig();

        // Set up the chat command system
        this.commandHandler = new CommandHandler(ThirdPartyUtils.isOtherClientLoaded() ? config.alterativeCommandPrefix : config.commandPrefix);
        new ModuleAdder(true);

        // Load the persistence
        this.initPersistence();

        // Register commands
        this.initCommands();
        this.initModuleCommands();
        this.eventManager = new EventManager();
        this.crystalDataTracker = new CrystalDataTrackerUtils();

        // Load all the block ids
        BlockUtils.initIdList();

        // Generate textRenderer
        this.initFont(config.font);

        // Done!
        log("Onyx.ss v4 Loaded!");
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public CrystalDataTrackerUtils getCrystalDataTracker() {
            return crystalDataTracker;
    }

    /**
     * Registers a given module
     * @param module The module to register
     * @return if the module was registered
     */
    public boolean registerModule(Module module) {
        String key = module.getName().toLowerCase();

        // Check if the module is already registered.
        if (modules.containsKey(key)) {
            log("Module " + key + " is already registered!");
            return false;
        }

        // Add the module
        this.modules.put(key, module);
        this.class2module.put(module.getClass(), module);

        return true;
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T) class2module.get(moduleClass);
    }

    public <T extends Module, R> R getFrom(Class<T> moduleClass, Function<T, R> getter) {
        T m = getModule(moduleClass);

        if (m != null) {
            return getter.apply(m);
        }
        return null;
    }

    public <T extends Module> boolean isEnabled(Class<T> moduleClass) {
        return getFrom(moduleClass, Module::isEnabled);
    }
}
