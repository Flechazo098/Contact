package com.flechazo.contact.common.storage;

import com.flechazo.contact.Contact;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class MailboxDataStorage extends SavedData {
    public final PlayerMailboxData PLAYERS_DATA = new PlayerMailboxData();

    public PlayerMailboxData getData() {
        return PLAYERS_DATA;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag compound = new CompoundTag();
        PLAYERS_DATA.writeToNBT(compound);
        return compound;
    }

    public static MailboxDataStorage readFromNbt(CompoundTag tag) {
        MailboxDataStorage serverState = new MailboxDataStorage();
        serverState.PLAYERS_DATA.readFromNBT(tag);
        return serverState;
    }

    public static MailboxDataStorage getMailboxData(MinecraftServer server) {
        DimensionDataStorage dimensionDataStorage = server.getLevel(Level.OVERWORLD).getDataStorage();
        return dimensionDataStorage.computeIfAbsent(MailboxDataStorage::readFromNbt, MailboxDataStorage::new, Contact.MOD_ID);
    }

}
