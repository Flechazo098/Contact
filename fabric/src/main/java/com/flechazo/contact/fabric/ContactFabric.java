package com.flechazo.contact.fabric;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.handler.AddresseeSignInHandler;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.handler.WanderingTraderSaleHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionResult;

public final class ContactFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Contact.init();
        ServerTickEvents.START_SERVER_TICK.register(MailboxManager::onServerTick);
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            var result = WanderingTraderSaleHandler.interact(player, entity, hand);
            return switch (result) {
                case SUCCESS -> InteractionResult.SUCCESS;
                case FAIL -> InteractionResult.FAIL;
                default -> InteractionResult.PASS;
            };
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            AddresseeSignInHandler.onPlayerLoggedIn(handler.player);
        });
    }
}
