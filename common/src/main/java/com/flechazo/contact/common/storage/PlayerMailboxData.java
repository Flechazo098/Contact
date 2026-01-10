package com.flechazo.contact.common.storage;

import cc.sighs.oelib.data.DataManager;
import com.flechazo.contact.common.tileentity.MailboxBlockEntity;
import com.flechazo.contact.network.ActionMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerMailboxData {
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMailboxData> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> {
                CompoundTag tag = new CompoundTag();
                data.writeToNBT(tag, buf.registryAccess());
                ByteBufCodecs.COMPOUND_TAG.encode(buf, tag);

                buf.writeInt(data.nameToUUID.size());
                data.nameToUUID.forEach((name, uuid) -> {
                    ByteBufCodecs.STRING_UTF8.encode(buf, name);
                    ByteBufCodecs.STRING_UTF8.encode(buf, uuid.toString());
                });
            },
            (buf) -> {
                PlayerMailboxData data = new PlayerMailboxData();
                var tag = ByteBufCodecs.COMPOUND_TAG.decode(buf);
                data.readFromNBT(tag, buf.registryAccess());

                int nameMapSize = buf.readInt();
                for (int i = 0; i < nameMapSize; i++) {
                    var name = ByteBufCodecs.STRING_UTF8.decode(buf);
                    var uuidStr = ByteBufCodecs.STRING_UTF8.decode(buf);
                    data.nameToUUID.put(name, UUID.fromString(uuidStr));
                }

                return data;
            }
    );
    public final Map<String, UUID> nameToUUID = Maps.newTreeMap();
    public final Map<UUID, SimpleContainer> uuidToContents = Maps.newHashMap();
    public final List<MailToBeSent> mailList = Lists.newArrayList();
    private final Map<UUID, GlobalPos> uuidToLocation = Maps.newHashMap();
    private final Map<GlobalPos, UUID> locationToPlayer = Maps.newHashMap();

    public static PlayerMailboxData deserializeForAttachment(CompoundTag tag, HolderLookup.Provider provider) {
        PlayerMailboxData data = new PlayerMailboxData();
        data.readFromNBT(tag, provider);
        return data;
    }

    public CompoundTag serializeForAttachment(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        writeToNBT(tag, provider);
        return tag;
    }

    public SimpleContainer getMailboxContents(UUID uuid) {
        return uuidToContents.getOrDefault(uuid, new SimpleContainer(24));
    }

    public boolean isMailboxEmpty(UUID uuid) {
        var contents = uuidToContents.get(uuid);
        if (contents == null) {
            return true;
        } else {
            for (int i = 0; i < contents.getContainerSize(); ++i) {
                if (!contents.getItem(i).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isMailboxFull(UUID uuid) {
        var contents = uuidToContents.get(uuid);
        if (contents == null) {
            return false;
        } else {
            for (int i = 0; i < contents.getContainerSize(); ++i) {
                if (contents.getItem(i).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean addMailboxContents(UUID uuid, ItemStack parcelIn) {
        var mailbox = getMailboxContents(uuid);
        if (!isMailboxFull(uuid)) {
            for (int i = 0; i < mailbox.getContainerSize(); ++i) {
                if (mailbox.getItem(i).isEmpty()) {
                    mailbox.setItem(i, parcelIn);
                    setMailboxContents(uuid, mailbox);
                    var player = DataManager.getServer().getPlayerList().getPlayer(uuid);
                    if (player != null) {
                        ActionMessage packet = new ActionMessage(0, "");
                        packet.sendTo(player);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void setMailboxContents(UUID uuid, SimpleContainer contents) {
        uuidToContents.put(uuid, contents);
    }

    public void resetMailboxContents(UUID uuid) {
        setMailboxContents(uuid, new SimpleContainer(24));
    }

    @Nullable
    public UUID getMailboxOwner(ResourceKey<Level> level, BlockPos pos) {
        return locationToPlayer.get(GlobalPos.of(level, pos));
    }

    @Nullable
    public GlobalPos getMailboxPos(UUID uuid) {
        return uuidToLocation.get(uuid);
    }

    @SuppressWarnings("deprecation")
    public void setMailboxData(UUID uuid, ResourceKey<Level> level, BlockPos pos) {
        var newPos = GlobalPos.of(level, pos);
        var oldPos = uuidToLocation.get(uuid);

        if (oldPos != null) {
            locationToPlayer.remove(oldPos);
            var oldLevel = DataManager.getServer().getLevel(oldPos.dimension());
            if (oldLevel != null && oldLevel.hasChunkAt(oldPos.pos())) {
                var oldTE = oldLevel.getBlockEntity(oldPos.pos());
                if (oldTE instanceof MailboxBlockEntity) {
                    ((MailboxBlockEntity) oldTE).refreshStatus();
                }
            }
        }
        uuidToLocation.put(uuid, newPos);
        locationToPlayer.put(newPos, uuid);

        var newWorld = DataManager.getServer().getLevel(level);
        if (newWorld != null && newWorld.hasChunkAt(newPos.pos())) {
            var newTE = newWorld.getBlockEntity(newPos.pos());
            if (newTE instanceof MailboxBlockEntity) {
                ((MailboxBlockEntity) newTE).refreshStatus();
            }
        }
    }

    public void removeMailboxData(GlobalPos pos) {
        var uuid = locationToPlayer.remove(pos);
        if (uuid != null) {
            var mailboxPos = uuidToLocation.get(uuid);
            if (Objects.equals(mailboxPos, pos)) {
                uuidToLocation.remove(uuid);
            }
        }
    }


    public CompoundTag writeToNBT(CompoundTag tag, HolderLookup.Provider provider) {
        int n = uuidToContents.size();
        tag.putInt("MapDataSize", n);
        int i = 0;
        for (UUID uuid : uuidToContents.keySet()) {
            CompoundTag compoundTag = new CompoundTag();

            compoundTag.putString("UUID", uuid.toString());
            compoundTag.put("Contents", uuidToContents.getOrDefault(uuid, new SimpleContainer(24)).createTag(provider));

            var globalPos = uuidToLocation.get(uuid);
            if (globalPos != null) {
                ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, globalPos.dimension().location()).resultOrPartial(LogManager.getLogger()::error).ifPresent(world -> compoundTag.put("MailboxDimension", world));
                compoundTag.putInt("MailboxX", globalPos.pos().getX());
                compoundTag.putInt("MailboxY", globalPos.pos().getY());
                compoundTag.putInt("MailboxZ", globalPos.pos().getZ());
            }
            tag.put("MapData" + i, compoundTag);
            i++;
        }

        tag.putInt("MailListSize", mailList.size());
        for (i = 0; i < mailList.size(); i++) {
            tag.put("MailListData" + i, mailList.get(i).writeToNBT(provider));
        }

        tag.putInt("NameMapSize", nameToUUID.size());
        i = 0;
        for (String name : nameToUUID.keySet()) {
            tag.putString("NameMap" + i, name);
            tag.putString("NameMapUUID" + i, nameToUUID.get(name).toString());
            i++;
        }

        return tag;
    }

    public void readFromNBT(CompoundTag tag, HolderLookup.Provider provider) {
        uuidToContents.clear();
        uuidToLocation.clear();
        locationToPlayer.clear();
        mailList.clear();
        nameToUUID.clear();

        int n = tag.getInt("MapDataSize");
        for (int i = 0; i < n; i++) {
            var compoundTag = tag.getCompound("MapData" + i);
            var uuid = UUID.fromString(compoundTag.getString("UUID"));
            SimpleContainer contents = new SimpleContainer(24);
            contents.fromTag(compoundTag.getList("Contents", Tag.TAG_COMPOUND), provider);
            uuidToContents.put(uuid, contents);

            if (compoundTag.contains("MailboxDimension")) {
                var mailboxPos = new BlockPos(compoundTag.getInt("MailboxX"), compoundTag.getInt("MailboxY"), compoundTag.getInt("MailboxZ"));
                var mailboxWorld = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("MailboxDimension")).resultOrPartial(LogManager.getLogger()::error).orElse(Level.OVERWORLD);
                var globalPos = GlobalPos.of(mailboxWorld, mailboxPos);
                uuidToLocation.put(uuid, globalPos);
                locationToPlayer.put(globalPos, uuid);
            }
        }

        n = tag.getInt("MailListSize");
        for (int i = 0; i < n; i++) {
            var compoundTag = tag.getCompound("MailListData" + i);
            MailToBeSent mail = new MailToBeSent(compoundTag, provider);
            mailList.add(mail);
        }

        n = tag.getInt("NameMapSize");
        for (int i = 0; i < n; i++) {
            var name = tag.getString("NameMap" + i);
            var uuid = UUID.fromString(tag.getString("NameMapUUID" + i));
            nameToUUID.put(name, uuid);
        }
    }
}