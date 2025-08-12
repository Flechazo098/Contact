package com.flechazo.contact.common.block;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.inter.ISilveroakEntry;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.helper.VoxelShapeHelper;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PostboxBlock extends DoubleHorizontalBlock implements ISilveroakEntry {
    private static final Component CONTAINER_NAME = Component.translatable("container.contact.postbox");
    private final boolean isRed;
    public static final VoxelShape LOWER_SHAPE;
    public static final VoxelShape UPPER_SHAPE;

    public PostboxBlock(boolean isRed) {
        super(Properties.of().noOcclusion().sound(SoundType.STONE).strength(1.5F, 6.0F));
        this.isRed = isRed;
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    protected VoxelShape getLowerShape() {
        return LOWER_SHAPE;
    }

    @Override
    protected VoxelShape getUpperShape() {
        return UPPER_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (player instanceof ServerPlayer sp) {
                MenuRegistry.openExtendedMenu(sp,
                        getContainer(isRed),
                        buf -> buf.writeBoolean(isRed)
                );
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    public static MenuProvider getContainer(boolean isRed) {
        return new SimpleMenuProvider(
                (id, inventory, player) -> new PostboxScreenHandler(id, inventory, isRed),
                CONTAINER_NAME
        );
    }

    static {
        VoxelShape bottom = VoxelShapeHelper.createVoxelShape(1, 0, 1, 14, 9, 14);
        VoxelShape pillarBottom = VoxelShapeHelper.createVoxelShape(2, 9, 2, 12, 7, 12);
        VoxelShape pillarTop = VoxelShapeHelper.createVoxelShape(2, 0, 2, 12, 11, 12);
        VoxelShape topBottom = VoxelShapeHelper.createVoxelShape(0, 11, 0, 16, 3, 16);
        VoxelShape topTop = VoxelShapeHelper.createVoxelShape(3, 14, 3, 10, 2, 10);
        LOWER_SHAPE = Shapes.or(bottom, pillarBottom);
        UPPER_SHAPE = Shapes.or(pillarTop, topBottom, topTop);
    }

    @Override
    public ResourceLocation getRegistryID() {
        return Contact.getRL(isRed ? "red_postbox" : "green_postbox");
    }
}
