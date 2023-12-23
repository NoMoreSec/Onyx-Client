package net.onyx.client.modules.combat;

import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.client.OnAttackEntityEvent;
import net.onyx.client.modules.DummyModule;
import net.onyx.client.modules.Module;
import net.onyx.client.onyxevent.events.AttackEntityListener;

import static net.onyx.client.utils.WorldUtils.mc;

public class TotemHit extends Module {

    // SirBubble
    public TotemHit() {
        super("TotemHit");
        this.setDescription("Makes u do more kb while hitting with a totem");
        this.setCategory("Combat");
    }

    @Override
    public void activate() {
        this.addListen(OnAttackEntityEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnAttackEntityEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        if (event instanceof OnAttackEntityEvent attackEvent && mc.getNetworkHandler() != null && attackEvent.player.equals(mc.player) && mc.player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));

            mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(attackEvent.target, mc.player.isSneaking()));
            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
    }
}