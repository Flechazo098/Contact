package com.flechazo.contact.common.tileentity;

import cc.sighs.oelib.registry.DeferredRegister;
import cc.sighs.oelib.registry.RegisterSupplier;
import com.flechazo.contact.Contact;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static com.flechazo.contact.common.registry.BlockRegistry.*;

public final class BlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Contact.MOD_ID);
    public static final RegisterSupplier<BlockEntityType<MailboxBlockEntity>> MAILBOX_BLOCK_ENTITY =
            register("mailbox", () ->
                    BlockEntityType.Builder.of(MailboxBlockEntity::new,
                                    ORANGE_MAILBOX.get(), MAGENTA_MAILBOX.get(), LIGHT_BLUE_MAILBOX.get(), YELLOW_MAILBOX.get(),
                                    LIME_MAILBOX.get(), PINK_MAILBOX.get(), GRAY_MAILBOX.get(), LIGHT_GRAY_MAILBOX.get(),
                                    CYAN_MAILBOX.get(), PURPLE_MAILBOX.get(), BLUE_MAILBOX.get(), BROWN_MAILBOX.get(),
                                    GREEN_MAILBOX.get(), RED_MAILBOX.get(), BLACK_MAILBOX.get(), WHITE_MAILBOX.get())
                            .build(null));

    private static <T extends BlockEntity> RegisterSupplier<BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> blockEntityType) {
        return BLOCK_ENTITY_TYPES.register(name, blockEntityType);
    }
}