package com.flechazo.contact.fabric.data;

import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.common.storage.PlayerMailboxData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FabricMailboxDataProvider(ServerLevel level) implements IMailboxDataProvider {

    private MailboxSavedData getSavedData() {
        return MailboxSavedData.get(level);
    }

    private PlayerMailboxData getData() {
        return getSavedData().getData();
    }

    @Override
    public PlayerMailboxData data() {
        return getData();
    }

    @Override
    public Map<String, UUID> getNameToUUID() {
        return getData().nameToUUID;
    }

    @Override
    public Map<UUID, SimpleContainer> getUuidToContents() {
        return getData().uuidToContents;
    }

    @Override
    public List<MailToBeSent> getMailList() {
        return getData().mailList;
    }

    @Override
    public SimpleContainer getMailboxContents(UUID uuid) {
        return getData().getMailboxContents(uuid);
    }

    @Override
    public boolean isMailboxEmpty(UUID uuid) {
        return getData().isMailboxEmpty(uuid);
    }

    @Override
    public boolean isMailboxFull(UUID uuid) {
        return getData().isMailboxFull(uuid);
    }

    @Override
    public boolean addMailboxContents(UUID uuid, ItemStack parcelIn) {
        boolean result = getData().addMailboxContents(uuid, parcelIn);
        if (result) {
            getSavedData().markDirty();
        }
        return result;
    }

    @Override
    public void setMailboxContents(UUID uuid, SimpleContainer contents) {
        getData().setMailboxContents(uuid, contents);
        getSavedData().markDirty();
    }

    @Override
    public void resetMailboxContents(UUID uuid) {
        getData().resetMailboxContents(uuid);
        getSavedData().markDirty();
    }

    @Override
    @Nullable
    public UUID getMailboxOwner(ResourceKey<Level> level, BlockPos pos) {
        return getData().getMailboxOwner(level, pos);
    }

    @Override
    @Nullable
    public GlobalPos getMailboxPos(UUID uuid) {
        return getData().getMailboxPos(uuid);
    }

    @Override
    public void setMailboxData(UUID uuid, ResourceKey<Level> level, BlockPos pos) {
        getData().setMailboxData(uuid, level, pos);
        getSavedData().markDirty();
    }

    @Override
    public void removeMailboxData(GlobalPos pos) {
        getData().removeMailboxData(pos);
        getSavedData().markDirty();
    }
}