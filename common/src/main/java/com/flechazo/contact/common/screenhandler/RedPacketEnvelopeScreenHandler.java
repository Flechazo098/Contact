package com.flechazo.contact.common.screenhandler;

import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.item.IPackageItem;
import com.flechazo.contact.common.item.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static com.flechazo.contact.common.screenhandler.ScreenHandlerTypeRegistry.RED_PACKET_ENVELOPE_CONTAINER;

public class RedPacketEnvelopeScreenHandler extends PackageScreenHandler {
    public final static int CONTENT_COUNT = 1;
    public final SimpleContainer inputs = new SimpleContainer(CONTENT_COUNT);
    public String blessings = "";

    public RedPacketEnvelopeScreenHandler(int id, Container container) {
        super(RED_PACKET_ENVELOPE_CONTAINER.get(), id);
        addSlot(new Slot(inputs, 0, 40, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (stack.getItem() instanceof IPackageItem)
                    return false;
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                return !ContactCommonConfig.getBlacklistID().contains(id.toString()) && !ContactCommonConfig.getBlacklistID().contains(id.getPath());
            }
        });
        for (int i = 0; i < 3; ++i) {

            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(container, j + i * 9 + 9, 8 + j * 18, 67 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(container, i, 8 + i * 18, 125));
        }
    }

    @Override
    public SimpleContainer getContainer() {
        return inputs;
    }

    @Override
    public ItemStack getUnpackedItem() {
        return new ItemStack(ItemRegistry.RED_PACKET_ENVELOPE.get());
    }

    @Override
    public ItemStack getPackedItem() {
        ItemStack redPacket = new ItemStack(ItemRegistry.RED_PACKET.get());
        if (!blessings.isBlank()) {
            redPacket.getOrCreateTag().putString("blessing", blessings);
        }
        return redPacket;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public int getContainerCount() {
        return CONTENT_COUNT;
    }
}
