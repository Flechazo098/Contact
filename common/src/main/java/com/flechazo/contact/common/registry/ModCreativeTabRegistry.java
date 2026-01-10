package com.flechazo.contact.common.registry;

import cc.sighs.oelib.registry.DeferredRegister;
import cc.sighs.oelib.registry.RegisterSupplier;
import com.flechazo.contact.Contact;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Contact.MOD_ID);

    public static final RegisterSupplier<CreativeModeTab> CONTACT_TAB = CREATIVE_TABS.register("tab", () ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.contact.tab"))
                    .icon(() -> new ItemStack(ItemRegistry.LETTER.get()))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ItemRegistry.LETTER.get());
                        entries.accept(ItemRegistry.ENVELOPE.get());
                        entries.accept(ItemRegistry.RED_PACKET.get());
                        entries.accept(ItemRegistry.RED_PACKET_ENVELOPE.get());
                        entries.accept(ItemRegistry.PARCEL.get());
                        entries.accept(ItemRegistry.ENDER_PARCEL.get());
                        entries.accept(ItemRegistry.WRAPPING_PAPER.get());
                        entries.accept(ItemRegistry.ENDER_WRAPPING_PAPER.get());

                        ItemRegistry.initPostcardStyles(displayContext, entries);

                        entries.accept(ItemRegistry.WHITE_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.ORANGE_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.MAGENTA_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.LIGHT_BLUE_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.YELLOW_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.LIME_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.PINK_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.GRAY_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.LIGHT_GRAY_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.CYAN_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.PURPLE_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.BLUE_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.BROWN_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.GREEN_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.RED_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.BLACK_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.CENTER_MAILBOX_ITEM.get());
                        entries.accept(ItemRegistry.RED_POSTBOX_ITEM.get());
                        entries.accept(ItemRegistry.GREEN_POSTBOX_ITEM.get());
                    })
                    .build()
    );
}