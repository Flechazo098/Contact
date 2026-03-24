package com.flechazo.contact.common.screenhandler;

import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.item.IPackageItem;
import com.flechazo.contact.common.registry.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static com.flechazo.contact.common.registry.ScreenHandlerTypeRegistry.WRAPPING_PAPER_CONTAINER;

public class WrappingPaperScreenHandler extends PackageScreenHandler {
    public final static int CONTENT_COUNT = 4;
    public final SimpleContainer inputs = new SimpleContainer(CONTENT_COUNT);
    private final boolean isEnder;

    public WrappingPaperScreenHandler(int id, Container container, boolean isEnder) {
        super(WRAPPING_PAPER_CONTAINER.get(), id);
        this.isEnder = isEnder;
        for (int i = 0; i < CONTENT_COUNT; i++) {
            addSlot(new Slot(inputs, i, 35 + 20 * i, 32) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if (stack.getItem() instanceof IPackageItem)
                        return false;
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                    return !ContactCommonConfig.getBlacklistID().contains(id.toString()) && !ContactCommonConfig.getBlacklistID().contains(id.getPath());
                }
            });
        }
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
        return isEnder ? new ItemStack(ItemRegistry.ENDER_WRAPPING_PAPER.get()) : new ItemStack(ItemRegistry.WRAPPING_PAPER.get());
    }

    @Override
    public ItemStack getPackedItem() {
        return isEnder ? new ItemStack(ItemRegistry.ENDER_PARCEL.get()) : new ItemStack(ItemRegistry.PARCEL.get());
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
