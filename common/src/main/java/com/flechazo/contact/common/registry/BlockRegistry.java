package com.flechazo.contact.common.registry;

import cc.sighs.oelib.registry.DeferredRegister;
import cc.sighs.oelib.registry.RegisterSupplier;
import cc.sighs.oelib.registry.extra.ColorRegister;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.block.CenterMailboxBlock;
import com.flechazo.contact.common.block.MailboxBlock;
import com.flechazo.contact.common.block.PostboxBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.flechazo.contact.ContactClient.BLOCK_MAILBOX_COLOR;
import static com.flechazo.contact.util.ClientUtil.registerCutoutRenderLayer;

public final class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Contact.MOD_ID);


    public static final RegisterSupplier<Block> WHITE_MAILBOX = register("white_mailbox", () -> new MailboxBlock(DyeColor.WHITE));
    public static final RegisterSupplier<Block> ORANGE_MAILBOX = register("orange_mailbox", () -> new MailboxBlock(DyeColor.ORANGE));
    public static final RegisterSupplier<Block> MAGENTA_MAILBOX = register("magenta_mailbox", () -> new MailboxBlock(DyeColor.MAGENTA));
    public static final RegisterSupplier<Block> LIGHT_BLUE_MAILBOX = register("light_blue_mailbox", () -> new MailboxBlock(DyeColor.LIGHT_BLUE));
    public static final RegisterSupplier<Block> YELLOW_MAILBOX = register("yellow_mailbox", () -> new MailboxBlock(DyeColor.YELLOW));
    public static final RegisterSupplier<Block> LIME_MAILBOX = register("lime_mailbox", () -> new MailboxBlock(DyeColor.LIME));
    public static final RegisterSupplier<Block> PINK_MAILBOX = register("pink_mailbox", () -> new MailboxBlock(DyeColor.PINK));
    public static final RegisterSupplier<Block> GRAY_MAILBOX = register("gray_mailbox", () -> new MailboxBlock(DyeColor.GRAY));
    public static final RegisterSupplier<Block> LIGHT_GRAY_MAILBOX = register("light_gray_mailbox", () -> new MailboxBlock(DyeColor.LIGHT_GRAY));
    public static final RegisterSupplier<Block> CYAN_MAILBOX = register("cyan_mailbox", () -> new MailboxBlock(DyeColor.CYAN));
    public static final RegisterSupplier<Block> PURPLE_MAILBOX = register("purple_mailbox", () -> new MailboxBlock(DyeColor.PURPLE));
    public static final RegisterSupplier<Block> BLUE_MAILBOX = register("blue_mailbox", () -> new MailboxBlock(DyeColor.BLUE));
    public static final RegisterSupplier<Block> BROWN_MAILBOX = register("brown_mailbox", () -> new MailboxBlock(DyeColor.BROWN));
    public static final RegisterSupplier<Block> GREEN_MAILBOX = register("green_mailbox", () -> new MailboxBlock(DyeColor.GREEN));
    public static final RegisterSupplier<Block> RED_MAILBOX = register("red_mailbox", () -> new MailboxBlock(DyeColor.RED));
    public static final RegisterSupplier<Block> BLACK_MAILBOX = register("black_mailbox", () -> new MailboxBlock(DyeColor.BLACK));

    public static final RegisterSupplier<Block> CENTER_MAILBOX = register("center_mailbox", CenterMailboxBlock::new);
    public static final RegisterSupplier<Block> RED_POSTBOX = register("red_postbox", () -> new PostboxBlock(true));
    public static final RegisterSupplier<Block> GREEN_POSTBOX = register("green_postbox", () -> new PostboxBlock(false));

    private static <T extends Block> RegisterSupplier<T> register(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
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

    public static void registerColors() {
        @SuppressWarnings("unchecked")
        Supplier<? extends Block>[] coloredMailboxes = Stream.of(
                ORANGE_MAILBOX, MAGENTA_MAILBOX, LIGHT_BLUE_MAILBOX, YELLOW_MAILBOX,
                LIME_MAILBOX, PINK_MAILBOX, GRAY_MAILBOX, LIGHT_GRAY_MAILBOX,
                CYAN_MAILBOX, PURPLE_MAILBOX, BLUE_MAILBOX, BROWN_MAILBOX,
                GREEN_MAILBOX, RED_MAILBOX, BLACK_MAILBOX, WHITE_MAILBOX
        ).toArray(Supplier[]::new);

        ColorRegister.registerColorBlocks(BLOCK_MAILBOX_COLOR, coloredMailboxes);
    }
}