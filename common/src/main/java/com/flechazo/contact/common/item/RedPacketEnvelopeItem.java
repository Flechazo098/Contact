package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class RedPacketEnvelopeItem extends NormalItem {
    private static final Component CONTAINER_NAME = Component.translatable("container.contact.red_packet_envelope");

    public RedPacketEnvelopeItem() {
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "red_packet_envelope"), Contact.ITEM_GROUP);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (!level.isClientSide) {
            user.openMenu(getContainer());
            if (!user.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.consume(itemStack);
    }

    public static MenuProvider getContainer() {
        return new SimpleMenuProvider((id, inventory, player) -> new RedPacketEnvelopeScreenHandler(id, inventory), CONTAINER_NAME);
    }
}
