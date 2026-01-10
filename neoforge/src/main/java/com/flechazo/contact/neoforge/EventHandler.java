package com.flechazo.contact.neoforge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.handler.AddresseeSignInHandler;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.handler.WanderingTraderSaleHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Contact.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        MailboxManager.onServerTick(event.getServer());
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