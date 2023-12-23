package net.onyx.client.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Hand;
import net.minecraft.util.Language;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.onyx.client.OnyxClient;
import net.onyx.client.interfaces.mixin.IClientWorld;
import net.onyx.client.interfaces.mixin.IEntity;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ClientUtils {

    public static double frameTime;
    public static boolean isInStorage() {
        Screen screen = OnyxClient.getClient().currentScreen;
        if (screen == null) return false;

        switch (screen.getClass().getSimpleName()) {
            case "ShulkerBoxScreen":
            case "GenericContainerScreen": return true;
            default: return false;
        }
    
    }

    public static boolean hasElytraEquipt() {
        ItemStack chestSlot = OnyxClient.me().getEquippedStack(EquipmentSlot.CHEST);
		return (chestSlot.getItem() == Items.ELYTRA);
    }

    public static void applyRotation(OldRotation.Rotation rot) {
        OnyxClient.me().setYaw((float)rot.yaw);
        OnyxClient.me().setPitch((float)rot.pitch);
    }

    public static void lookAtPos(Vec3d pos) {
        applyRotation(
                OldRotation.getRequiredRotation(pos)
        );
    }

    public static void hitEntity(Entity target) {
        OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.me(), target);
        OnyxClient.me().swingHand(Hand.MAIN_HAND);
    }

    public static OldRotation.Rotation getRotation() {
        return new OldRotation.Rotation(
                OnyxClient.me().getYaw(),
                OnyxClient.me().getPitch()
        );
    }

    public static void sendPos(double x, double y, double z, boolean onGround) {
        OnyxClient.me().networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround)
        );
    }


    public static Vec2f getRotationTo(Box box) {
        PlayerEntity player = OnyxClient.getClient().player;
        if (player == null) {
            return Vec2f.ZERO;
        }

        Vec3d eyePos = RotationUtils.getEyesPos();

        if (player.getBoundingBox().intersects(box)) {
            return getRotationTo(eyePos, box.getCenter());
        }

        double x = MathHelper.clamp(eyePos.x, box.minX, box.maxX);
        double y = MathHelper.clamp(eyePos.y, box.minY, box.maxY);
        double z = MathHelper.clamp(eyePos.z, box.minZ, box.maxZ);

        return getRotationTo(eyePos, new Vec3d(x, y, z));
    }

    public static Vec2f getRotationTo(Vec3d posTo) {
        PlayerEntity player = OnyxClient.getClient().player;
        return player != null ? getRotationTo(RotationUtils.getEyesPos(), posTo) : Vec2f.ZERO;
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        return getRotationFromVec(posTo.subtract(posFrom));
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double lengthXZ = Math.hypot(vec.x, vec.z);
        double yaw = normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        double pitch = normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, lengthXZ)));

        return new Vec2f((float) yaw, (float) pitch);
    }

    public static double normalizeAngle(double angle) {
        angle %= 360.0;

        if (angle >= 180.0) {
            angle -= 360.0;
        }

        if (angle < -180.0) {
            angle += 360.0;
        }

        return angle;
    }

    public static float normalizeAngle(float angle) {
        angle %= 360.0f;

        if (angle >= 180.0f) {
            angle -= 360.0f;
        }

        if (angle < -180.0f) {
            angle += 360.0f;
        }

        return angle;
    }


    public static void sendPos(Vec3d pos, boolean onGround) {
        sendPos(pos.x, pos.y, pos.z, onGround);
    }

    public static Boolean inGame() {
        return OnyxClient.me() != null && OnyxClient.getClient().getNetworkHandler() != null;
    }

    public static Boolean isInNetherPortal() {
        IEntity me = (IEntity) OnyxClient.me();
        
        return me.getInNetherPortal();
    }

    public static Boolean isThirdperson() {
        return OnyxClient.getClient().gameRenderer.getCamera().isThirdPerson();
    }

    public static void openChatScreen(String text) {
        if (!inGame()) return;
        OnyxClient.getClient().setScreen(new ChatScreen(text));
    }

    public static void clearChatHistory() {
        if (!inGame()) return;
        ChatHud hud = new ChatHud(OnyxClient.getClient());
        hud.clear(true);
    }

    public static void openChatScreen() {
        openChatScreen("");
    }

    public static ItemStack getHandlerSlot(int i) {
        return OnyxClient.me().currentScreenHandler.getSlot(i).getStack();
    }

    public static Integer getPing() {
        ClientPlayNetworkHandler lv = OnyxClient.me().networkHandler;
        PlayerListEntry entry = lv.getPlayerListEntry(OnyxClient.me().getUuid());
        
        if (entry == null) return 0;

        return entry.getLatency();
    }

    public static String getGameModeName() {
        if (OnyxClient.me().isSpectator()) return "Spectator";
        if (OnyxClient.me().isCreative()) return "Creative";

        return "Survival";
    }

    public static Vec3d getControlVelocity(Entity ent, Double speed, Boolean allowFlight) {
        // Initialize as still, or somewhat still.
        Vec3d velocity = new Vec3d(0, allowFlight ? 0 : ent.getVelocity().getY(), 0);

        // We only need these two velocities since the other you can calculate just by multiplying these out by -1 :P
        Vec3d forward = MathsUtils.getForwardVelocity(ent);
        Vec3d right   = MathsUtils.getRightVelocity(ent);

        // Forward + Back
        if (OnyxClient.me().input.pressingForward) velocity = velocity.add(forward.multiply(new Vec3d(speed, 0, speed)));
        if (OnyxClient.me().input.pressingBack)    velocity = velocity.add(forward.multiply(new Vec3d(-speed, 0, -speed)));

        // Right + Left
        if (OnyxClient.me().input.pressingRight) velocity = velocity.add(right.multiply(new Vec3d(speed, 0, speed)));
        if (OnyxClient.me().input.pressingLeft)  velocity = velocity.add(right.multiply(new Vec3d(-speed, 0, -speed)));

        if (allowFlight) {
            // Up + Down
            if (OnyxClient.me().input.jumping)  velocity = velocity.add(0, speed, 0);
            if (OnyxClient.me().input.sneaking) velocity = velocity.add(0, -speed, 0);
        }

        // Set the velocity
        return velocity;
    }

    public static void entitySpeedControl(Entity ent, Double speed, Boolean allowFlight) {
        // Set the velocity
        ent.setVelocity(getControlVelocity(ent, speed, allowFlight));
    }

    public static Vec2f getMousePosition() {
        MinecraftClient client = OnyxClient.getClient();
        Mouse mouse = client.mouse;
        Window window = client.getWindow();

        if (mouse.isCursorLocked()) return new Vec2f(window.getScaledWidth()/2, window.getScaledHeight()/2);

        double scaleFactor = window.getScaleFactor();
        int posX = (int)(mouse.getX()/scaleFactor);
        int posY = (int)(mouse.getY()/scaleFactor);

        return new Vec2f(posX, posY);
    }

    public static String getTextString(Text text) {
        if (text instanceof TranslatableTextContent transText) {
            Language language = Language.getInstance();

            String str = language.get(transText.getKey());
            return str.equals("%s") ? transText.getKey() : str;
        }

        return text.getString();
    }

    /**
     * Checks to see if an entity can see a given position
     * @param
     */
    public static boolean canSee(Entity ent, Vec3d pos, float tickDelta) {
        return ent.world.raycast(
            new RaycastContext(
                ent.getCameraPosVec(tickDelta), // My position
                pos, // The position to check
                RaycastContext.ShapeType.COLLIDER, // The shape type
                RaycastContext.FluidHandling.NONE, // The fluid handling
                ent // The entity that is doing the checking
            )
        ).getType() == HitResult.Type.MISS;
    }


    /**
     * Checks to see if an entity can see a given position
     * @param
     */
    public static boolean canSee(Vec3d pos) {
        return canSee(OnyxClient.me(), pos, 1);
    }

    public static boolean canSeeBlock(BlockPos blockPos) {
        return canSee(OnyxClient.me(), Vec3d.ofCenter(blockPos), 1);
    }

    public static void disconnect(Screen prev) {
        MinecraftClient client = OnyxClient.getClient();
        
        client.world.disconnect();
        client.setScreen(prev);
    }

    public static void openInventory() {
        OnyxClient.getClient().setScreen(new InventoryScreen(OnyxClient.me()));
    }

    public static void refreshInventory() {
        MinecraftClient client = OnyxClient.getClient();

        Boolean wasMouseLocked = client.mouse.isCursorLocked();

        ClientUtils.openInventory();
        client.currentScreen = null;
        
        if (wasMouseLocked) client.mouse.lockCursor();
    }

    /**
     * 
     * @return the username of the player
     */
    public static String getUsername() {
        return OnyxClient.me().getName().getString();
    }

    private static final HashMap<UUID, String> usernames = new HashMap<>();
    private static class UsernameResponse {
        String name;
    }
    public static String getPlayerUsername(UUID uuid) {
        if (usernames.containsKey(uuid)) return usernames.get(uuid);

        PlayerEntity player = OnyxClient.getClient().world.getPlayerByUuid(uuid);
        if (player != null) {
            String name = player.getEntityName();

            usernames.put(uuid, name);
            return name;
        }

        usernames.put(uuid, "N/A");

        // Pain.
        Thread thread = new Thread(() -> {
            String data = WebUtils.getJSON("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names", 60000);

            Gson gson = new Gson();
            ArrayList<UsernameResponse> d = gson.fromJson(data, new TypeToken<ArrayList<UsernameResponse>>() {}.getType());

            for (UsernameResponse u : d) {
                usernames.put(uuid, u.name);
                return;
            }
        });
        thread.start();

        return usernames.get(uuid);
    }

    public static GameMode getGameMode(PlayerEntity player) {
        PlayerListEntry playerListEntry = OnyxClient.getClient().getNetworkHandler().getPlayerListEntry(player.getGameProfile().getId());

        // Check for null and return the default gamemode
        if (playerListEntry == null) {
            return GameMode.SURVIVAL;
        }

        return playerListEntry.getGameMode();
    }

    public static GameMode getGameMode() {
        // Get the localplayer in the tab list
        return getGameMode(OnyxClient.me());
    }

    public static int getHealth(LivingEntity ent) {
        return (int)Math.ceil(ent.getHealth());
    }

    public static String getKeyCodeName(int keyCode, int scanCode) {
        return getTextString(InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText());
    }

    public static String getKeyCodeName(int keyCode) {
        return getKeyCodeName(keyCode, GLFW.GLFW_KEY_UNKNOWN);
    }

    /**
     * Gets a class and gets the last part of the class name.
     * <p>This is horrible and should be replaced ASAP!</p>
     * @param classId The class to get the name of
     * @return The last part of the class name
     */
    private static String getNameFromClassId(String classId) {
        String[] parts = classId.split("\\.");

        if (parts.length == 0) {
            return "";
        }

        return parts[parts.length - 1];
    }

    /**
     * Gets an entity type as a string
     * @param entity The entity to get the type of
     * @return The entity type
     */
    public static String getEntityType(Entity entity) {
        return getNameFromClassId(entity.getType().getTranslationKey());
    }

    /**
     * Gets an effect type as a string
     * @param effect The effect to get the type of
     * @return The effect type
     */
    public static String getEffectType(StatusEffectInstance effect) {
        return getNameFromClassId(effect.getTranslationKey());
    }

    /**
     * Gets the PendingUpdateManager
     * @param world The world to get the manager from
     * @return The PendingUpdateManager
     */
    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        IClientWorld iWorld = (IClientWorld)world;

        return iWorld.obtainPendingUpdateManager();
    }

    /**
     * Opens, increments and closes the PendingUpdateManager for a given world
     * @param world The world to open the manager for
     * @return The new sequence number
     */
    public static int incrementPendingUpdateManager(ClientWorld world) {
        PendingUpdateManager manager = getUpdateManager(world);

        int current = manager.getSequence();
        manager.close();

        return current;
    }
}
