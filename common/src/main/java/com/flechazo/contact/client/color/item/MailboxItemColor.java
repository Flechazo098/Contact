package com.flechazo.contact.client.color.item;

import com.flechazo.contact.common.block.MailboxBlock;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class MailboxItemColor implements ItemColor {
    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        Block block = Block.byItem(itemStack.getItem());
        if (block instanceof MailboxBlock) {
            if (tintIndex <= 1) {
                return ((MailboxBlock) block).boxColor.getMapColor().col;
            } else if (tintIndex == 2) {
                return DyeColor.RED.getMapColor().col;
            }
        }
        return -1;
    }
}
