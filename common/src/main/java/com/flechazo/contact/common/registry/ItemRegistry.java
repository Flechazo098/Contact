package com.flechazo.contact.common.registry;

import cc.sighs.oelib.registry.DeferredRegister;
import cc.sighs.oelib.registry.RegisterSupplier;
import cc.sighs.oelib.registry.extra.ColorRegister;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.inter.ISilveroakEntry;
import com.flechazo.contact.common.item.*;
import com.flechazo.contact.data.PostcardDataManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.flechazo.contact.Contact.ITEM_GROUP;
import static com.flechazo.contact.ContactClient.ITEM_MAILBOX_COLOR;
import static com.flechazo.contact.common.item.PostcardItem.getPostcard;
import static com.flechazo.contact.common.registry.BlockRegistry.*;

public final class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Contact.MOD_ID);
    public static final RegisterSupplier<Item> LETTER = register("letter", LetterItem::new);
    public static final RegisterSupplier<Item> ENVELOPE = register("envelope", EnvelopeItem::new);

    public static final RegisterSupplier<Item> RED_PACKET = register("red_packet", RedPacketItem::new);
    public static final RegisterSupplier<Item> RED_PACKET_ENVELOPE = register("red_packet_envelope", RedPacketEnvelopeItem::new);

    public static final RegisterSupplier<Item> PARCEL = register("parcel", () -> new ParcelItem("parcel", false));
    public static final RegisterSupplier<Item> ENDER_PARCEL = register("ender_parcel", () -> new ParcelItem("ender_parcel", true));

    public static final RegisterSupplier<Item> POSTCARD = register("postcard", () -> new PostcardItem("postcard", false));
    public static final RegisterSupplier<Item> ENDER_POSTCARD = register("ender_postcard", () -> new PostcardItem("ender_postcard", true));

    public static final RegisterSupplier<Item> WRAPPING_PAPER = register("wrapping_paper", () -> new WrappingPaperItem("wrapping_paper"));
    public static final RegisterSupplier<Item> ENDER_WRAPPING_PAPER = register("ender_wrapping_paper", () -> new WrappingPaperItem("ender_wrapping_paper"));

    public static final RegisterSupplier<BlockItem> WHITE_MAILBOX_ITEM = register("white_mailbox", () -> createBlockItem(WHITE_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> ORANGE_MAILBOX_ITEM = register("orange_mailbox", () -> createBlockItem(ORANGE_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> MAGENTA_MAILBOX_ITEM = register("magenta_mailbox", () -> createBlockItem(MAGENTA_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> LIGHT_BLUE_MAILBOX_ITEM = register("light_blue_mailbox", () -> createBlockItem(LIGHT_BLUE_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> YELLOW_MAILBOX_ITEM = register("yellow_mailbox", () -> createBlockItem(YELLOW_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> LIME_MAILBOX_ITEM = register("lime_mailbox", () -> createBlockItem(LIME_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> PINK_MAILBOX_ITEM = register("pink_mailbox", () -> createBlockItem(PINK_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> GRAY_MAILBOX_ITEM = register("gray_mailbox", () -> createBlockItem(GRAY_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> LIGHT_GRAY_MAILBOX_ITEM = register("light_gray_mailbox", () -> createBlockItem(LIGHT_GRAY_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> CYAN_MAILBOX_ITEM = register("cyan_mailbox", () -> createBlockItem(CYAN_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> PURPLE_MAILBOX_ITEM = register("purple_mailbox", () -> createBlockItem(PURPLE_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> BLUE_MAILBOX_ITEM = register("blue_mailbox", () -> createBlockItem(BLUE_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> BROWN_MAILBOX_ITEM = register("brown_mailbox", () -> createBlockItem(BROWN_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> GREEN_MAILBOX_ITEM = register("green_mailbox", () -> createBlockItem(GREEN_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> RED_MAILBOX_ITEM = register("red_mailbox", () -> createBlockItem(RED_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> BLACK_MAILBOX_ITEM = register("black_mailbox", () -> createBlockItem(BLACK_MAILBOX.get()));

    public static final RegisterSupplier<BlockItem> CENTER_MAILBOX_ITEM = register("center_mailbox", () -> createBlockItem(CENTER_MAILBOX.get()));
    public static final RegisterSupplier<BlockItem> RED_POSTBOX_ITEM = register("red_postbox", () -> createBlockItem(RED_POSTBOX.get()));
    public static final RegisterSupplier<BlockItem> GREEN_POSTBOX_ITEM = register("green_postbox", () -> createBlockItem(GREEN_POSTBOX.get()));

    private static <T extends Item> RegisterSupplier<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static BlockItem createBlockItem(Block block) {
        if (block instanceof ISilveroakEntry b) {
            return new NormalBlockItem(block, b.getRegistryID(), ITEM_GROUP);
        } else return new BlockItem(block, new Item.Properties());
    }

    public static void initPostcardStyles(CreativeModeTab.ItemDisplayParameters displayContext, CreativeModeTab.Output entries) {
        for (ResourceLocation id : PostcardDataManager.getPostcards().keySet()) {
            entries.accept(getPostcard(id, false));
        }
        for (ResourceLocation id : PostcardDataManager.getPostcards().keySet()) {
            entries.accept(getPostcard(id, true));
        }
    }

    public static void registerColors() {
        @SuppressWarnings("unchecked")
        Supplier<? extends Item>[] coloredMailboxes = Stream.of(
                ORANGE_MAILBOX, MAGENTA_MAILBOX, LIGHT_BLUE_MAILBOX, YELLOW_MAILBOX,
                LIME_MAILBOX, PINK_MAILBOX, GRAY_MAILBOX, LIGHT_GRAY_MAILBOX,
                CYAN_MAILBOX, PURPLE_MAILBOX, BLUE_MAILBOX, BROWN_MAILBOX,
                GREEN_MAILBOX, RED_MAILBOX, BLACK_MAILBOX, WHITE_MAILBOX
        ).toArray(Supplier[]::new);

        ColorRegister.registerColorItems(ITEM_MAILBOX_COLOR, coloredMailboxes);
    }
}