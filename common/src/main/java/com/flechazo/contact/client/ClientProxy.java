package com.flechazo.contact.client;

import cc.sighs.oelib.registry.extra.RenderTypeRegister;
import com.flechazo.contact.client.gui.screen.NewMailToast;
import com.flechazo.contact.client.gui.screen.PostcardEditScreen;
import com.flechazo.contact.client.gui.screen.PostcardReadScreen;
import com.flechazo.contact.common.config.ContactClientConfig;
import com.flechazo.contact.data.PostcardStyle;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;

public class ClientProxy {
    private static final Map<PostcardStyle, RenderType> CARD_RENDER_LAYERS = Maps.newHashMap();
    private static final Map<PostcardStyle, RenderType> POSTMARK_RENDER_LAYERS = Maps.newHashMap();


    public static void openPostcardToEdit(ItemStack itemstack, Player playerIn, InteractionHand handIn) {
        Minecraft.getInstance().setScreen(new PostcardEditScreen(itemstack, playerIn, handIn));
    }

    public static void openPostcardToRead(ItemStack itemstack) {
        Minecraft.getInstance().setScreen(new PostcardReadScreen(itemstack));
    }

    public static void notifyNewMail(Minecraft client) {
        if (ContactClientConfig.isShowNewMailToast()) {
            client.execute(() -> client.getToasts().addToast(new NewMailToast()));
        }
    }

    public static void registerCutoutRenderLayer(Supplier<Block> block) {
        RenderTypeRegister.registerBlocks(RenderType.cutout(), block);
    }

    public static RenderType getPostcardCardRenderLayer(PostcardStyle style) {
        RenderType renderLayer = CARD_RENDER_LAYERS.get(style);
        if (renderLayer == null) {
            renderLayer = RenderType.text(style.getCardTexture());
            CARD_RENDER_LAYERS.put(style, renderLayer);
            return renderLayer;
        }
        return renderLayer;
    }

    public static RenderType getPostcardPostmarkRenderLayer(PostcardStyle style) {
        RenderType renderLayer = POSTMARK_RENDER_LAYERS.get(style);
        if (renderLayer == null) {
            renderLayer = RenderType.text(style.getPostmarkTexture());
            POSTMARK_RENDER_LAYERS.put(style, renderLayer);
            return renderLayer;
        }
        return renderLayer;
    }
}
