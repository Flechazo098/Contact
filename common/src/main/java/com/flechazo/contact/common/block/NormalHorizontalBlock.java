package com.flechazo.contact.common.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class NormalHorizontalBlock extends HorizontalDirectionalBlock {
    public NormalHorizontalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (ctx.getPlayer() != null) {
            return defaultBlockState().setValue(FACING, ctx.getPlayer().getDirection().getOpposite());
        }
        return defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}