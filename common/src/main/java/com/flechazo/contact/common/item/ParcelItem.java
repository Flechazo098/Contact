package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.common.registry.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ParcelItem extends NormalItem implements IMailItem, IPackageItem {
    private final boolean isEnderType;

    public ParcelItem(String id, boolean isEnderType) {
        super(new ResourceLocation(Contact.MOD_ID, id),
                new Properties().stacksTo(1),
                Contact.ITEM_GROUP);
        this.isEnderType = isEnderType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        IPackageItem.openPackage(this, user, hand);
        return InteractionResultHolder.success(ItemStack.EMPTY);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        PackageTooltipData data = IPackageItem.getTooltipData(this, stack);
        return data.contents().isEmpty() ? Optional.empty() : Optional.of(data);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        this.addSenderInfoTooltip(stack, level, tooltip, flag);
    }

    @Override
    public boolean isEnderType() {
        return isEnderType;
    }

    public static ItemStack getParcel(SimpleContainer contents, boolean isEnderType, String sender) {
        ItemStack parcel = new ItemStack(isEnderType ? ItemRegistry.ENDER_PARCEL.get() : ItemRegistry.PARCEL.get());
        parcel.getOrCreateTag().put("parcel", contents.createTag());
        if (!sender.isEmpty()) {
            parcel.getOrCreateTag().putString("Sender", sender);
        }
        return parcel;
    }

    @Override
    public int getCapacity() {
        return 4;
    }
}