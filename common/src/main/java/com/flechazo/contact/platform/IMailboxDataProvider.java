package com.flechazo.contact.platform;

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

public interface IMailboxDataProvider {
    Map<String, UUID> getNameToUUID();

    Map<UUID, SimpleContainer> getUuidToContents();

    List<MailToBeSent> getMailList();

    SimpleContainer getMailboxContents(UUID uuid);

    boolean isMailboxEmpty(UUID uuid);

    boolean isMailboxFull(UUID uuid);

    boolean addMailboxContents(UUID uuid, ItemStack parcelIn);

    void setMailboxContents(UUID uuid, SimpleContainer contents);

    void resetMailboxContents(UUID uuid);

    @Nullable
    UUID getMailboxOwner(ResourceKey<Level> level, BlockPos pos);

    @Nullable
    GlobalPos getMailboxPos(UUID uuid);

    void setMailboxData(UUID uuid, ResourceKey<Level> level, BlockPos pos);

    void removeMailboxData(GlobalPos pos);

    PlayerMailboxData data();
}