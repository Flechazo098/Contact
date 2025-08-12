package com.flechazo.contact.forge.storage;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.storage.PlayerMailboxData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Contact.MOD_ID)
public class MailboxDataCapability {
    public static final Capability<PlayerMailboxData> MAILBOX_DATA = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation ID = new ResourceLocation(Contact.MOD_ID, "mailbox_data");
    
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().dimension() == Level.OVERWORLD) {
            event.addCapability(ID, new MailboxDataProvider());
        }
    }
    
    public static class MailboxDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final PlayerMailboxData data = new PlayerMailboxData();
        private final LazyOptional<PlayerMailboxData> optional = LazyOptional.of(() -> data);
        
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == MAILBOX_DATA ? optional.cast() : LazyOptional.empty();
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            data.writeToNBT(tag);
            return tag;
        }
        
        @Override
        public void deserializeNBT(CompoundTag nbt) {
            data.readFromNBT(nbt);
        }
    }
}