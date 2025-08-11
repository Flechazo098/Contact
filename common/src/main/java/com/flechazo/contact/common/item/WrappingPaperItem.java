package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.WrappingPaperScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WrappingPaperItem extends NormalItem {
    private static final Component CONTAINER_NAME = Component.translatable("container.contact.wrapping_paper");

    public WrappingPaperItem(String id) {
        super(new ResourceLocation(Contact.MOD_ID, id), Contact.ITEM_GROUP);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (!world.isClientSide) {
            user.openMenu(getContainer(itemStack.getItem() == ItemRegistry.ENDER_WRAPPING_PAPER));
            if (!user.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.consume(itemStack);
    }

    public static MenuProvider getContainer(boolean isEnder) {
        return new SimpleMenuProvider((id, inventory, player) -> new WrappingPaperScreenHandler(id, inventory, isEnder), CONTAINER_NAME);
    }
}
