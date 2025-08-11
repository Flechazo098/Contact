package com.flechazo.contact.common.storage;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class MailToBeSent {
    private final UUID uuid;
    private final SimpleContainer contents;
    private long ticks;

    public MailToBeSent(CompoundTag tag) {
        uuid = UUID.fromString(tag.getString("MailUUID"));
        ticks = tag.getInt("MailTicks");
        contents = new SimpleContainer(1);
        contents.fromTag(tag.getList("MailContents", Tag.TAG_COMPOUND));
    }

    public MailToBeSent(UUID uuid, ItemStack contents, long ticks) {
        this.uuid = uuid;
        this.contents = new SimpleContainer(1);
        this.contents.setItem(0, contents.copy());
        this.ticks = ticks;
    }

    public ItemStack getContents() {
        return contents.getItem(0).copy();
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isReady() {
        return ticks <= 0;
    }

    public void tick(int tick) {
        if (ticks > 0) ticks -= tick;
    }

    public CompoundTag writeToNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("MailUUID", uuid.toString());
        nbt.putLong("MailTicks", ticks);
        nbt.put("MailContents", contents.createTag());
        return nbt;
    }
}
