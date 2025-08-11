package com.flechazo.contact.client.color.block;

import com.flechazo.contact.common.block.MailboxBlock;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class MailboxBlockColor implements BlockColor {
    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        Block block = state.getBlock();
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
