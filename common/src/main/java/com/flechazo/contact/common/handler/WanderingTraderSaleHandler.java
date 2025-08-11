package com.flechazo.contact.common.handler;

import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.resourse.PostcardDataManager;
import com.flechazo.contact.resourse.PostcardStyle;
import dev.architectury.event.EventResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public final class WanderingTraderSaleHandler {
    public static EventResult interact(Player player, Entity entity, InteractionHand hand) {
        if (!player.level().isClientSide) {
            if (entity instanceof WanderingTrader trader) {
                if (!trader.getTags().contains("SellPostcard")) {
                    var postcards = PostcardDataManager.getPostcards();

                    int i = player.level().getRandom().nextInt(postcards.size());
                    trader.addTag("SellPostcard");
                    ResourceLocation id = postcards.keySet().toArray(new ResourceLocation[0])[i];
                    PostcardStyle style = PostcardDataManager.getPostcard(id);

                    int attempts = 0;
                    while (!style.trade().soldByTrader() && attempts < postcards.size()) {
                        i = player.level().getRandom().nextInt(postcards.size());
                        id = postcards.keySet().toArray(new ResourceLocation[0])[i];
                        style = PostcardDataManager.getPostcard(id);
                        attempts++;
                    }

                    if (style.trade().soldByTrader()) {
                        trader.getOffers().add(0, new MerchantOffer(style.trade().price(), new ItemStack(Items.ENDER_PEARL), PostcardItem.getPostcard(id, true), 16, 10, 0.05F));
                        trader.getOffers().add(0, new MerchantOffer(style.trade().price(), PostcardItem.getPostcard(id, false), 16, 10, 0.05F));
                    }
                }
            }
        }
        return EventResult.pass();
    }
}