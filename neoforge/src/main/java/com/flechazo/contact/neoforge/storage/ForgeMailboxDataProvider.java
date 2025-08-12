package com.flechazo.contact.neoforge.storage;

import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.common.storage.PlayerMailboxData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ForgeMailboxDataProvider(PlayerMailboxData data) implements IMailboxDataProvider {

    // 实现所有接口方法，与 Fabric 版本相同
    @Override
    public Map<String, UUID> getNameToUUID() {
        return data.nameToUUID;
    }

    @Override
    public Map<UUID, SimpleContainer> getUuidToContents() {
        return data.uuidToContents;
    }

    @Override
    public List<MailToBeSent> getMailList() {
        return data.mailList;
    }

    @Override
    public SimpleContainer getMailboxContents(UUID uuid) {
        return data.getMailboxContents(uuid);
    }

    @Override
    public boolean isMailboxEmpty(UUID uuid) {
        return data.isMailboxEmpty(uuid);
    }

    @Override
    public boolean isMailboxFull(UUID uuid) {
        return data.isMailboxFull(uuid);
    }

    @Override
    public boolean addMailboxContents(UUID uuid, ItemStack parcelIn) {
        return data.addMailboxContents(uuid, parcelIn);
    }

    @Override
    public void setMailboxContents(UUID uuid, SimpleContainer contents) {
        data.setMailboxContents(uuid, contents);
    }

    @Override
    public void resetMailboxContents(UUID uuid) {
        data.resetMailboxContents(uuid);
    }

    @Override
    @Nullable
    public UUID getMailboxOwner(ResourceKey<Level> level, BlockPos pos) {
        return data.getMailboxOwner(level, pos);
    }

    @Override
    @Nullable
    public GlobalPos getMailboxPos(UUID uuid) {
        return data.getMailboxPos(uuid);
    }

    @Override
    public void setMailboxData(UUID uuid, ResourceKey<Level> level, BlockPos pos) {
        data.setMailboxData(uuid, level, pos);
    }

    @Override
    public void removeMailboxData(GlobalPos pos) {
        data.removeMailboxData(pos);
    }
}