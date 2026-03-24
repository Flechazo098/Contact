package com.flechazo.contact.forge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.handler.AddresseeSignInHandler;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.handler.WanderingTraderSaleHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Contact.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MailboxManager.onServerTick(event.getServer());
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        var result = WanderingTraderSaleHandler.interact(
                event.getEntity(),
                event.getTarget(),
                event.getHand()
        );

        if (result == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            AddresseeSignInHandler.onPlayerLoggedIn(serverPlayer);
        }
    }
}