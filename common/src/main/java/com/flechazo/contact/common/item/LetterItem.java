package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.common.component.ContactDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LetterItem extends NormalItem implements IMailItem, IPackageItem {
    public LetterItem() {
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "letter"),
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
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        this.addSenderInfoTooltip(stack, tooltipContext, tooltip, flag);
    }

    @Override
    public boolean isEnderType() {
        return false;
    }

    public static ItemStack getLetter(SimpleContainer contents, String sender) {
        ItemStack letter = new ItemStack(ItemRegistry.LETTER.get());
        letter.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(contents.getItems()));
        if (!sender.isEmpty()) {
            letter.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);
        }
        return letter;
    }

    @Override
    public int getCapacity() {
        return 1;
    }
}