package com.flechazo.contact.fabric.capability;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.storage.PlayerMailboxData;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class MailboxDataComponent implements AutoSyncedComponent {
    public static final ComponentKey<MailboxDataComponent> KEY = ComponentRegistry.getOrCreate(
            new ResourceLocation(Contact.MOD_ID, "mailbox_data"), MailboxDataComponent.class);
    
    private final PlayerMailboxData data = new PlayerMailboxData();
    
    public PlayerMailboxData getData() {
        return data;
    }
    
    @Override
    public void readFromNbt(CompoundTag tag) {
        data.readFromNBT(tag);
    }
    
    @Override
    public void writeToNbt(CompoundTag tag) {
        data.writeToNBT(tag);
    }

}