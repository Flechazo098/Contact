package com.flechazo.contact.fabric.capability;

import com.flechazo.contact.common.storage.PlayerMailboxData;
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

    public static MailboxSavedData load(CompoundTag tag) {
        PlayerMailboxData data = new PlayerMailboxData();
        data.readFromNBT(tag);
        return new MailboxSavedData(data);
    }

    public static MailboxSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                MailboxSavedData::load,
                MailboxSavedData::new,
                DATA_NAME
        );
    }

    public PlayerMailboxData getData() {
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        data.writeToNBT(tag);
        return tag;
    }

    public void markDirty() {
        setDirty();
    }
}