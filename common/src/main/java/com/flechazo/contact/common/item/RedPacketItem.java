package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.item.PackageTooltipData;
import net.minecraft.ChatFormatting;
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

public class RedPacketItem extends NormalItem implements IMailItem, IPackageItem {
    public RedPacketItem() {
        super(new ResourceLocation(Contact.MOD_ID, "red_packet"),
                new Properties().stacksTo(1),
                Contact.ITEM_GROUP);
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
        if (stack.getOrCreateTag().contains("blessing")) {
            tooltip.add(Component.translatable("tooltip.contact.red_packet.blessing",
                    stack.getOrCreateTag().getString("blessing")).withStyle(ChatFormatting.GRAY));
        }
        this.addSenderInfoTooltip(stack, level, tooltip, flag);
    }

    @Override
    public boolean isEnderType() {
        return false;
    }

    public static ItemStack getRedPacket(SimpleContainer contents, String blessings, String sender) {
        ItemStack letter = new ItemStack(ItemRegistry.RED_PACKET.get());
        letter.getOrCreateTag().put("parcel", contents.createTag());
        letter.getOrCreateTag().putString("blessings", blessings);
        if (!sender.isEmpty()) {
            letter.getOrCreateTag().putString("Sender", sender);
        }
        return letter;
    }

    @Override
    public int getCapacity() {
        return 1;
    }
}