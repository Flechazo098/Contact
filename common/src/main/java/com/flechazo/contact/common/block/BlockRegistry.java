package com.flechazo.contact.common.block;

import com.flechazo.contact.common.registry.RegistryManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import static com.flechazo.contact.client.ClientProxy.registerCutoutRenderLayer;

public final class BlockRegistry {
    public static final RegistrySupplier<Block> WHITE_MAILBOX = RegistryManager.BLOCKS.register("white_mailbox", () -> new MailboxBlock(DyeColor.WHITE));
    public static final RegistrySupplier<Block> ORANGE_MAILBOX = RegistryManager.BLOCKS.register("orange_mailbox", () -> new MailboxBlock(DyeColor.ORANGE));
    public static final RegistrySupplier<Block> MAGENTA_MAILBOX = RegistryManager.BLOCKS.register("magenta_mailbox", () -> new MailboxBlock(DyeColor.MAGENTA));
    public static final RegistrySupplier<Block> LIGHT_BLUE_MAILBOX = RegistryManager.BLOCKS.register("light_blue_mailbox", () -> new MailboxBlock(DyeColor.LIGHT_BLUE));
    public static final RegistrySupplier<Block> YELLOW_MAILBOX = RegistryManager.BLOCKS.register("yellow_mailbox", () -> new MailboxBlock(DyeColor.YELLOW));
    public static final RegistrySupplier<Block> LIME_MAILBOX = RegistryManager.BLOCKS.register("lime_mailbox", () -> new MailboxBlock(DyeColor.LIME));
    public static final RegistrySupplier<Block> PINK_MAILBOX = RegistryManager.BLOCKS.register("pink_mailbox", () -> new MailboxBlock(DyeColor.PINK));
    public static final RegistrySupplier<Block> GRAY_MAILBOX = RegistryManager.BLOCKS.register("gray_mailbox", () -> new MailboxBlock(DyeColor.GRAY));
    public static final RegistrySupplier<Block> LIGHT_GRAY_MAILBOX = RegistryManager.BLOCKS.register("light_gray_mailbox", () -> new MailboxBlock(DyeColor.LIGHT_GRAY));
    public static final RegistrySupplier<Block> CYAN_MAILBOX = RegistryManager.BLOCKS.register("cyan_mailbox", () -> new MailboxBlock(DyeColor.CYAN));
    public static final RegistrySupplier<Block> PURPLE_MAILBOX = RegistryManager.BLOCKS.register("purple_mailbox", () -> new MailboxBlock(DyeColor.PURPLE));
    public static final RegistrySupplier<Block> BLUE_MAILBOX = RegistryManager.BLOCKS.register("blue_mailbox", () -> new MailboxBlock(DyeColor.BLUE));
    public static final RegistrySupplier<Block> BROWN_MAILBOX = RegistryManager.BLOCKS.register("brown_mailbox", () -> new MailboxBlock(DyeColor.BROWN));
    public static final RegistrySupplier<Block> GREEN_MAILBOX = RegistryManager.BLOCKS.register("green_mailbox", () -> new MailboxBlock(DyeColor.GREEN));
    public static final RegistrySupplier<Block> RED_MAILBOX = RegistryManager.BLOCKS.register("red_mailbox", () -> new MailboxBlock(DyeColor.RED));
    public static final RegistrySupplier<Block> BLACK_MAILBOX = RegistryManager.BLOCKS.register("black_mailbox", () -> new MailboxBlock(DyeColor.BLACK));

    public static final RegistrySupplier<Block> CENTER_MAILBOX = RegistryManager.BLOCKS.register("center_mailbox", CenterMailboxBlock::new);
    public static final RegistrySupplier<Block> RED_POSTBOX = RegistryManager.BLOCKS.register("red_postbox", () -> new PostboxBlock(true));
    public static final RegistrySupplier<Block> GREEN_POSTBOX = RegistryManager.BLOCKS.register("green_postbox", () -> new PostboxBlock(false));

    public static void initBlocks() {
    }

    public static void registerRenderLayer() {
        registerCutoutRenderLayer(WHITE_MAILBOX);
        registerCutoutRenderLayer(ORANGE_MAILBOX);
        registerCutoutRenderLayer(MAGENTA_MAILBOX);
        registerCutoutRenderLayer(LIGHT_BLUE_MAILBOX);
        registerCutoutRenderLayer(YELLOW_MAILBOX);
        registerCutoutRenderLayer(LIME_MAILBOX);
        registerCutoutRenderLayer(PINK_MAILBOX);
        registerCutoutRenderLayer(GRAY_MAILBOX);
        registerCutoutRenderLayer(LIGHT_GRAY_MAILBOX);
        registerCutoutRenderLayer(CYAN_MAILBOX);
        registerCutoutRenderLayer(PURPLE_MAILBOX);
        registerCutoutRenderLayer(BLUE_MAILBOX);
        registerCutoutRenderLayer(BROWN_MAILBOX);
        registerCutoutRenderLayer(GREEN_MAILBOX);
        registerCutoutRenderLayer(RED_MAILBOX);
        registerCutoutRenderLayer(BLACK_MAILBOX);
    }
}