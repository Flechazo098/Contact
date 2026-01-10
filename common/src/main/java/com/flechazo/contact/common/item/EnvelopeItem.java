package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.EnvelopeScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnvelopeItem extends NormalItem {
    private static final Component CONTAINER_NAME = Component.translatable("container.contact.envelope");

    public EnvelopeItem() {
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "envelope"), Contact.ITEM_GROUP);
    }

    public static MenuProvider getContainer() {
        return new SimpleMenuProvider((id, inventory, player) -> new EnvelopeScreenHandler(id, inventory), CONTAINER_NAME);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        var itemStack = user.getItemInHand(hand);
        if (!level.isClientSide) {
            user.openMenu(getContainer());
            if (!user.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.consume(itemStack);
    }
}
