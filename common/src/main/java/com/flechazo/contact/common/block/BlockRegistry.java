package com.flechazo.contact.common.block;

import com.flechazo.contact.Contact;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static com.flechazo.contact.client.ClientProxy.registerCutoutRenderLayer;

public final class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Contact.MOD_ID, Registries.BLOCK);


    public static final RegistrySupplier<Block> WHITE_MAILBOX = register("white_mailbox", () -> new MailboxBlock(DyeColor.WHITE));
    public static final RegistrySupplier<Block> ORANGE_MAILBOX = register("orange_mailbox", () -> new MailboxBlock(DyeColor.ORANGE));
    public static final RegistrySupplier<Block> MAGENTA_MAILBOX = register("magenta_mailbox", () -> new MailboxBlock(DyeColor.MAGENTA));
    public static final RegistrySupplier<Block> LIGHT_BLUE_MAILBOX = register("light_blue_mailbox", () -> new MailboxBlock(DyeColor.LIGHT_BLUE));
    public static final RegistrySupplier<Block> YELLOW_MAILBOX = register("yellow_mailbox", () -> new MailboxBlock(DyeColor.YELLOW));
    public static final RegistrySupplier<Block> LIME_MAILBOX = register("lime_mailbox", () -> new MailboxBlock(DyeColor.LIME));
    public static final RegistrySupplier<Block> PINK_MAILBOX = register("pink_mailbox", () -> new MailboxBlock(DyeColor.PINK));
    public static final RegistrySupplier<Block> GRAY_MAILBOX = register("gray_mailbox", () -> new MailboxBlock(DyeColor.GRAY));
    public static final RegistrySupplier<Block> LIGHT_GRAY_MAILBOX = register("light_gray_mailbox", () -> new MailboxBlock(DyeColor.LIGHT_GRAY));
    public static final RegistrySupplier<Block> CYAN_MAILBOX = register("cyan_mailbox", () -> new MailboxBlock(DyeColor.CYAN));
    public static final RegistrySupplier<Block> PURPLE_MAILBOX = register("purple_mailbox", () -> new MailboxBlock(DyeColor.PURPLE));
    public static final RegistrySupplier<Block> BLUE_MAILBOX = register("blue_mailbox", () -> new MailboxBlock(DyeColor.BLUE));
    public static final RegistrySupplier<Block> BROWN_MAILBOX = register("brown_mailbox", () -> new MailboxBlock(DyeColor.BROWN));
    public static final RegistrySupplier<Block> GREEN_MAILBOX = register("green_mailbox", () -> new MailboxBlock(DyeColor.GREEN));
    public static final RegistrySupplier<Block> RED_MAILBOX = register("red_mailbox", () -> new MailboxBlock(DyeColor.RED));
    public static final RegistrySupplier<Block> BLACK_MAILBOX = register("black_mailbox", () -> new MailboxBlock(DyeColor.BLACK));

    public static final RegistrySupplier<Block> CENTER_MAILBOX = register("center_mailbox", CenterMailboxBlock::new);
    public static final RegistrySupplier<Block> RED_POSTBOX = register("red_postbox", () -> new PostboxBlock(true));
    public static final RegistrySupplier<Block> GREEN_POSTBOX = register("green_postbox", () -> new PostboxBlock(false));

    private static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block) {
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
}