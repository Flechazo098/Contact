package com.flechazo.contact.common.handler;

import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.data.PostcardDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

public final class WanderingTraderSaleHandler {
    public static InteractionResult interact(Player player, Entity entity, InteractionHand hand) {
        if (!player.level().isClientSide) {
            if (entity instanceof WanderingTrader trader) {
                if (!trader.getTags().contains("SellPostcard")) {
                    var postcards = PostcardDataManager.getPostcards();

                    int i = player.level().getRandom().nextInt(postcards.size());
                    trader.addTag("SellPostcard");
                    var id = postcards.keySet().toArray(new ResourceLocation[0])[i];
                    var style = PostcardDataManager.getPostcard(id);

                    int attempts = 0;
                    while (!style.trade().soldByTrader() && attempts < postcards.size()) {
                        i = player.level().getRandom().nextInt(postcards.size());
                        id = postcards.keySet().toArray(new ResourceLocation[0])[i];
                        style = PostcardDataManager.getPostcard(id);
                        attempts++;
                    }

                    if (style.trade().soldByTrader()) {
                        trader.getOffers().add(0, new MerchantOffer(
                                new ItemCost(style.trade().price().getItem(), style.trade().price().getCount()),
                                Optional.of(new ItemCost(Items.ENDER_PEARL)),
                                PostcardItem.getPostcard(id, true),
                                16, 10, 0.05F
                        ));
                        trader.getOffers().add(0, new MerchantOffer(
                                new ItemCost(style.trade().price().getItem(), style.trade().price().getCount()),
                                PostcardItem.getPostcard(id, false),
                                16, 10, 0.05F
                        ));
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}