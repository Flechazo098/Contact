package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import com.flechazo.contact.client.overlay.RedPacketOverlay;

public class RedPacketItem extends NormalItem implements IMailItem, IPackageItem {
    public RedPacketItem() {
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "red_packet"),
                new Properties().stacksTo(1),
                Contact.ITEM_GROUP);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        var stack = user.getItemInHand(hand);
        String blessing = stack.get(ContactDataComponents.RED_PACKET_BLESSING.get());
        String sender = stack.get(ContactDataComponents.POSTCARD_SENDER.get());
        IPackageItem.openPackage(this, user, hand);
        if (level.isClientSide) {
            RedPacketOverlay.INSTANCE.showWithBlessing(blessing == null ? "" : blessing, sender == null ? "" : sender);
        }
        return InteractionResultHolder.success(ItemStack.EMPTY);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var data = IPackageItem.getTooltipData(this, stack);
        return data.contents().isEmpty() ? Optional.empty() : Optional.of(data);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        var blessing = stack.get(ContactDataComponents.RED_PACKET_BLESSING.get());
        if (blessing != null && !blessing.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.contact.red_packet.blessing", blessing).withStyle(ChatFormatting.GRAY));
        }
        this.addSenderInfoTooltip(stack, tooltipContext, tooltip, flag);
    }

    @Override
    public boolean isEnderType() {
        return false;
    }

    @Override
    public int getCapacity() {
        return 1;
    }
}
