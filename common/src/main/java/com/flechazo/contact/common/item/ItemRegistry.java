package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.inter.ISilveroakEntry;
import com.flechazo.contact.resourse.PostcardDataManager;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static com.flechazo.contact.Contact.ITEM_GROUP;
import static com.flechazo.contact.common.block.BlockRegistry.*;
import static com.flechazo.contact.common.item.PostcardItem.getPostcard;

public final class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Contact.MOD_ID, Registries.ITEM);
    public static final RegistrySupplier<Item> LETTER = register("letter", LetterItem::new);
    public static final RegistrySupplier<Item> ENVELOPE = register("envelope", EnvelopeItem::new);

    public static final RegistrySupplier<Item> RED_PACKET = register("red_packet", RedPacketItem::new);
    public static final RegistrySupplier<Item> RED_PACKET_ENVELOPE = register("red_packet_envelope", RedPacketEnvelopeItem::new);

    public static final RegistrySupplier<Item> PARCEL = register("parcel", () -> new ParcelItem("parcel", false));
    public static final RegistrySupplier<Item> ENDER_PARCEL = register("ender_parcel", () -> new ParcelItem("ender_parcel", true));

    public static final RegistrySupplier<Item> POSTCARD = register("postcard", () -> new PostcardItem("postcard", false));
    public static final RegistrySupplier<Item> ENDER_POSTCARD = register("ender_postcard", () -> new PostcardItem("ender_postcard", true));

    public static final RegistrySupplier<Item> WRAPPING_PAPER = register("wrapping_paper", () -> new WrappingPaperItem("wrapping_paper"));
    public static final RegistrySupplier<Item> ENDER_WRAPPING_PAPER = register("ender_wrapping_paper", () -> new WrappingPaperItem("ender_wrapping_paper"));

    public static final RegistrySupplier<BlockItem> WHITE_MAILBOX_ITEM = register("white_mailbox", () -> createBlockItem(WHITE_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> ORANGE_MAILBOX_ITEM = register("orange_mailbox", () -> createBlockItem(ORANGE_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> MAGENTA_MAILBOX_ITEM = register("magenta_mailbox", () -> createBlockItem(MAGENTA_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> LIGHT_BLUE_MAILBOX_ITEM = register("light_blue_mailbox", () -> createBlockItem(LIGHT_BLUE_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> YELLOW_MAILBOX_ITEM = register("yellow_mailbox", () -> createBlockItem(YELLOW_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> LIME_MAILBOX_ITEM = register("lime_mailbox", () -> createBlockItem(LIME_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> PINK_MAILBOX_ITEM = register("pink_mailbox", () -> createBlockItem(PINK_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> GRAY_MAILBOX_ITEM = register("gray_mailbox", () -> createBlockItem(GRAY_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> LIGHT_GRAY_MAILBOX_ITEM = register("light_gray_mailbox", () -> createBlockItem(LIGHT_GRAY_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> CYAN_MAILBOX_ITEM = register("cyan_mailbox", () -> createBlockItem(CYAN_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> PURPLE_MAILBOX_ITEM = register("purple_mailbox", () -> createBlockItem(PURPLE_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> BLUE_MAILBOX_ITEM = register("blue_mailbox", () -> createBlockItem(BLUE_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> BROWN_MAILBOX_ITEM = register("brown_mailbox", () -> createBlockItem(BROWN_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> GREEN_MAILBOX_ITEM = register("green_mailbox", () -> createBlockItem(GREEN_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> RED_MAILBOX_ITEM = register("red_mailbox", () -> createBlockItem(RED_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> BLACK_MAILBOX_ITEM = register("black_mailbox", () -> createBlockItem(BLACK_MAILBOX.get()));

    public static final RegistrySupplier<BlockItem> CENTER_MAILBOX_ITEM = register("center_mailbox", () -> createBlockItem(CENTER_MAILBOX.get()));
    public static final RegistrySupplier<BlockItem> RED_POSTBOX_ITEM = register("red_postbox", () -> createBlockItem(RED_POSTBOX.get()));
    public static final RegistrySupplier<BlockItem> GREEN_POSTBOX_ITEM = register("green_postbox", () -> createBlockItem(GREEN_POSTBOX.get()));

    private static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static BlockItem createBlockItem(Block block) {
        if (block instanceof ISilveroakEntry b) {
            return new NormalBlockItem(block, b.getRegistryID(), ITEM_GROUP);
        } else return new BlockItem(block, new Item.Properties());
    }

    public static void initItems() {
    }

    public static void initPostcardStyles(CreativeModeTab.ItemDisplayParameters displayContext, CreativeModeTab.Output entries) {
        for (ResourceLocation id : PostcardDataManager.getPostcards().keySet()) {
            entries.accept(getPostcard(id, false));
        }
        for (ResourceLocation id : PostcardDataManager.getPostcards().keySet()) {
            entries.accept(getPostcard(id, true));
        }
    }
}