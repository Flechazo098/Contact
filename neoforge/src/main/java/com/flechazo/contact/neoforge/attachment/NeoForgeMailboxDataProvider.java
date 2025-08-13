package com.flechazo.contact.neoforge.attachment;

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

public record NeoForgeMailboxDataProvider(ServerLevel level) implements IMailboxDataProvider {

    private PlayerMailboxData getData() {
        return level.getData(ContactAttachments.MAILBOX_DATA);
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
        return getData().addMailboxContents(uuid, parcelIn);
    }

    @Override
    public void setMailboxContents(UUID uuid, SimpleContainer contents) {
        getData().setMailboxContents(uuid, contents);
    }

    @Override
    public void resetMailboxContents(UUID uuid) {
        getData().resetMailboxContents(uuid);
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
    }

    @Override
    public void removeMailboxData(GlobalPos pos) {
        getData().removeMailboxData(pos);
    }
}