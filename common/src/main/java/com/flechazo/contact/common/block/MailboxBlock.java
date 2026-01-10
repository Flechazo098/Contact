package com.flechazo.contact.common.block;

import cc.sighs.oelib.data.DataManager;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.handler.AdvancementManager;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.inter.ISilveroakEntry;
import com.flechazo.contact.common.item.IMailItem;
import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.common.tileentity.MailboxBlockEntity;
import com.flechazo.contact.helper.VoxelShapeHelper;
import com.flechazo.contact.platform.PlatformHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY;

public class MailboxBlock extends DoubleHorizontalBlock implements EntityBlock, ISilveroakEntry {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final MapCodec<MailboxBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    DyeColor.CODEC.fieldOf("box_color").forGetter(block -> block.boxColor),
                    DyeColor.CODEC.optionalFieldOf("flag_color", DyeColor.RED).forGetter(block -> block.flagColor)
            ).apply(instance, MailboxBlock::new)
    );
    public static final VoxelShape LOWER_SHAPE;
    public static final VoxelShape UPPER_SHAPE_NORTH;
    public static final VoxelShape UPPER_SHAPE_EAST;

    static {
        LOWER_SHAPE = VoxelShapeHelper.createVoxelShape(7, 0, 7, 2, 16, 2);
        UPPER_SHAPE_NORTH = VoxelShapeHelper.createVoxelShape(3, 0, 1, 10, 9, 14);
        UPPER_SHAPE_EAST = VoxelShapeHelper.createVoxelShape(1, 0, 3, 14, 9, 10);
    }

    public final DyeColor boxColor;
    public final DyeColor flagColor;

    public MailboxBlock(DyeColor boxColor, DyeColor flagColor) {
        super(Properties.of().mapColor(boxColor).noOcclusion().sound(SoundType.STONE).strength(1.5F, 6.0F));
        this.boxColor = boxColor;
        this.flagColor = flagColor;
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false));
    }

    public MailboxBlock(DyeColor boxColor) {
        this(boxColor, DyeColor.RED);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityTicker<? super E> entityTicker) {
        return MAILBOX_BLOCK_ENTITY.get() == blockEntityType ? (BlockEntityTicker<A>) entityTicker : null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
            return getLowerShape();
        } else {
            return switch (state.getValue(FACING)) {
                case EAST, WEST -> UPPER_SHAPE_EAST;
                default -> UPPER_SHAPE_NORTH;
            };
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    protected VoxelShape getLowerShape() {
        return LOWER_SHAPE;
    }

    @Override
    protected VoxelShape getUpperShape() {
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            var topPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos : pos.above();
            var mailboxOwner = PlatformHelper.getMailboxOwner(level.dimension(), topPos);

            if (player.isShiftKeyDown()) {
                // 检查邮箱主人，没有的话，录入
                if (mailboxOwner == null) {
                    if (PlatformHelper.getMailboxPos(player.getUUID()) == null) {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.binding"), true);
                    } else {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.switch"), true);
                    }
                    PlatformHelper.setMailboxData(player.getUUID(), level.dimension(), topPos);
                    MailboxManager.updateState(level, topPos);
                    AdvancementManager.givePlayerAdvancement(level.getServer(), (ServerPlayer) player, ResourceLocation.parse("contact:root"));
                    return InteractionResult.SUCCESS;
                }
            }

            if (Objects.equals(mailboxOwner, player.getUUID())) {
                // 获取邮件
                var contents = PlatformHelper.getMailboxContents(mailboxOwner);
                boolean isEmpty = true;
                for (int i = 0; i < contents.getContainerSize(); ++i) {
                    var parcel = contents.getItem(i);
                    if (!parcel.isEmpty()) {
                        if (parcel.getItem() instanceof PostcardItem) {
                            AdvancementManager.givePlayerAdvancement(level.getServer(), (ServerPlayer) player, ResourceLocation.parse("contact:receive_postcard"));
                        }
                        if (parcel.has(ContactDataComponents.ANOTHER_WORLD.get())) {
                            AdvancementManager.givePlayerAdvancement(level.getServer(), (ServerPlayer) player, ResourceLocation.parse("contact:from_another_world"));
                        }
                        player.getInventory().placeItemBackInInventory(parcel);
                        isEmpty = false;
                    }
                }
                PlatformHelper.resetMailboxContents(mailboxOwner);
                if (!isEmpty) {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.pick_up"), true);
                } else {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.empty"), true);
                }
                MailboxManager.updateState(level, topPos);
                return InteractionResult.SUCCESS;
            } else if (mailboxOwner != null) {
                DataManager.getServer().getProfileCache().get(mailboxOwner).ifPresent(gameProfile ->
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.others", gameProfile.getName()), true));
                return InteractionResult.SUCCESS;
            }

            player.displayClientMessage(Component.translatable("message.contact.mailbox.no_owner_tips"), true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && itemStack.getItem() instanceof IMailItem) {
            var topPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos : pos.above();
            var mailboxOwner = PlatformHelper.getMailboxOwner(level.dimension(), topPos);

            if (mailboxOwner != null) {
                var held = itemStack.copy();
                if (!held.has(ContactDataComponents.POSTCARD_SENDER.get())) {
                    if (!PlatformHelper.isMailboxFull(mailboxOwner)) {
                        if (level.getBlockEntity(topPos) instanceof MailboxBlockEntity mailbox && mailbox.checkToSend()) {
                            held.set(ContactDataComponents.POSTCARD_SENDER.get(), player.getName().getString());
                            PlatformHelper.addMailboxContents(mailboxOwner, held);
                            itemStack.shrink(1);
                            player.displayClientMessage(Component.translatable("message.contact.mailbox.deliver"), true);
                            AdvancementManager.givePlayerAdvancement(player.getServer(), (ServerPlayer) player, ResourceLocation.parse("contact:send_in_person"));
                            MailboxManager.updateState(level, topPos);
                            return ItemInteractionResult.SUCCESS;
                        } else {
                            player.displayClientMessage(Component.translatable("message.contact.mailbox.check"), true);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.full"), true);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.used"), true);
                }
            } else {
                player.displayClientMessage(Component.translatable("message.contact.mailbox.no_owner"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (!level.isClientSide) {
            var topPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos : pos.above();
            PlatformHelper.removeMailboxData(GlobalPos.of(level.dimension(), topPos));
        }
        return state;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MailboxBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MAILBOX_BLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    public ResourceLocation getRegistryID() {
        return Contact.getRL(boxColor.getName() + "_mailbox");
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
