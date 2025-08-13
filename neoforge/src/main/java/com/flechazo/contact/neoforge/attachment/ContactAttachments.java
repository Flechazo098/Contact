package com.flechazo.contact.neoforge.attachment;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.storage.PlayerMailboxData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ContactAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Contact.MOD_ID);

    public static final Supplier<AttachmentType<PlayerMailboxData>> MAILBOX_DATA =
            ATTACHMENT_TYPES.register("mailbox_data", () ->
                    AttachmentType.builder(PlayerMailboxData::new)
                            .serialize(new IAttachmentSerializer<CompoundTag, PlayerMailboxData>() {
                                @Override
                                public PlayerMailboxData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
                                    return PlayerMailboxData.deserializeForAttachment(tag, provider);
                                }

                                @Override
                                public CompoundTag write(PlayerMailboxData attachment, HolderLookup.Provider provider) {
                                    return attachment.serializeForAttachment(provider);
                                }
                            })
                            .copyOnDeath()
                            .sync(
                                    (holder, player) -> holder instanceof ServerPlayer sp && sp.equals(player),
                                    PlayerMailboxData.STREAM_CODEC
                            )
                            .build()
            );
}