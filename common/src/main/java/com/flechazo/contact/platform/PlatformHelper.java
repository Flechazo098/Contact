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
import java.util.ServiceLoader;
import java.util.UUID;

public final class PlatformHelper {

    private static final IMailboxDataProvider IMPL;

    static {
        IMPL = ServiceLoader.load(IMailboxDataProvider.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No IMailboxDataProvider implementation found"));
    }

    private PlatformHelper() {
    }

    public static Map<String, UUID> getNameToUUID() {
        return IMPL.getNameToUUID();
    }

    public static Map<UUID, SimpleContainer> getUuidToContents() {
        return IMPL.getUuidToContents();
    }

    public static List<MailToBeSent> getMailList() {
        return IMPL.getMailList();
    }

    public static SimpleContainer getMailboxContents(UUID uuid) {
        return IMPL.getMailboxContents(uuid);
    }

    public static boolean isMailboxEmpty(UUID uuid) {
        return IMPL.isMailboxEmpty(uuid);
    }

    public static boolean isMailboxFull(UUID uuid) {
        return IMPL.isMailboxFull(uuid);
    }

    public static boolean addMailboxContents(UUID uuid, ItemStack parcelIn) {
        return IMPL.addMailboxContents(uuid, parcelIn);
    }

    public static void setMailboxContents(UUID uuid, SimpleContainer contents) {
        IMPL.setMailboxContents(uuid, contents);
    }

    public static void resetMailboxContents(UUID uuid) {
        IMPL.resetMailboxContents(uuid);
    }

    @Nullable
    public static UUID getMailboxOwner(ResourceKey<Level> level, BlockPos pos) {
        return IMPL.getMailboxOwner(level, pos);
    }

    @Nullable
    public static GlobalPos getMailboxPos(UUID uuid) {
        return IMPL.getMailboxPos(uuid);
    }

    public static void setMailboxData(UUID uuid, ResourceKey<Level> level, BlockPos pos) {
        IMPL.setMailboxData(uuid, level, pos);
    }

    public static void removeMailboxData(GlobalPos pos) {
        IMPL.removeMailboxData(pos);
    }

    public static PlayerMailboxData data() {
        return IMPL.data();
    }
}