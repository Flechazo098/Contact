package com.flechazo.contact.fabric.data;

import com.flechazo.contact.common.storage.PlayerMailboxData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class MailboxSavedData extends SavedData {
    private static final String DATA_NAME = "contact_mailbox_data";
    private final PlayerMailboxData data;

    public MailboxSavedData() {
        this.data = new PlayerMailboxData();
    }

    public MailboxSavedData(PlayerMailboxData data) {
        this.data = data;
    }

    public static MailboxSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        PlayerMailboxData data = new PlayerMailboxData();
        data.readFromNBT(tag, provider);
        return new MailboxSavedData(data);
    }

    public static MailboxSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        MailboxSavedData::new,
                        MailboxSavedData::load,
                        null
                ),
                DATA_NAME
        );
    }

    public PlayerMailboxData getData() {
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        data.writeToNBT(tag, provider);
        return tag;
    }

    public void markDirty() {
        setDirty();
    }
}