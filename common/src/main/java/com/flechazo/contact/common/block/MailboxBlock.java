package com.flechazo.contact.common.block;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.handler.AdvancementManager;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.inter.ISilveroakEntry;
import com.flechazo.contact.common.item.IMailItem;
import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.common.storage.MailboxDataManager;
import com.flechazo.contact.common.tileentity.MailboxBlockEntity;
import com.flechazo.contact.helper.VoxelShapeHelper;
import com.flechazo.contact.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
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
import java.util.UUID;

import static com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY;

public class MailboxBlock extends DoubleHorizontalBlock implements EntityBlock, ISilveroakEntry {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public final DyeColor boxColor;
    public final DyeColor flagColor;
    public static final VoxelShape LOWER_SHAPE;
    public static final VoxelShape UPPER_SHAPE_NORTH;
    public static final VoxelShape UPPER_SHAPE_EAST;

    public MailboxBlock(DyeColor boxColor, DyeColor flagColor) {
        super(Properties.of().mapColor(boxColor).noOcclusion().sound(SoundType.STONE).strength(1.5F, 6.0F));
        this.boxColor = boxColor;
        this.flagColor = flagColor;
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false));
    }

    public MailboxBlock(DyeColor boxColor) {
        this(boxColor, DyeColor.RED);
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
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide) {
            IMailboxDataProvider data = MailboxDataManager.getData(level);
            BlockPos topPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos : pos.above();
            UUID mailboxOwner = data.getMailboxOwner(level.dimension(), topPos);
            if (player.isShiftKeyDown()) {
                // 检查邮箱主人，没有的话，录入
                if (mailboxOwner == null) {
                    if (data.getMailboxPos(player.getUUID()) == null) {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.binding"), true);
                    } else {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.switch"), true);
                    }
                    data.setMailboxData(player.getUUID(), level.dimension(), topPos);
                    MailboxManager.updateState(level, topPos);
                    AdvancementManager.givePlayerAdvancement(level.getServer(), (ServerPlayer) player, new ResourceLocation("contact:root"));
                    return InteractionResult.SUCCESS;
                }
            }
            if (Objects.equals(mailboxOwner, player.getUUID())) {
                // 获取邮件
                SimpleContainer contents = data.getMailboxContents(mailboxOwner);
                boolean isEmpty = true;
                for (int i = 0; i < contents.getContainerSize(); ++i) {
                    ItemStack parcel = contents.getItem(i);
                    if (!parcel.isEmpty()) {
                        if (parcel.getItem() instanceof PostcardItem) {
                            AdvancementManager.givePlayerAdvancement(level.getServer(), (ServerPlayer) player, new ResourceLocation("contact:receive_postcard"));
                        }
                        if (parcel.getOrCreateTag().contains("AnotherWorld")) {
                            AdvancementManager.givePlayerAdvancement(level.getServer(), (ServerPlayer) player, new ResourceLocation("contact:from_another_world"));
                        }

                        player.getInventory().placeItemBackInInventory(parcel);
                        isEmpty = false;
                    }
                }
                // 腾空邮件列表
                data.resetMailboxContents(mailboxOwner);
                if (!isEmpty) {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.pick_up"), true);
                } else {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.empty"), true);
                }
                MailboxManager.updateState(level, topPos);
                return InteractionResult.SUCCESS;
            } else if (player.getItemInHand(handIn).getItem() instanceof IMailItem) {
                if (mailboxOwner != null) {
                    ItemStack held = player.getItemInHand(handIn).copy();
                    if (!held.getOrCreateTag().contains("Sender")) {
                        // 不是主人的话，如果有包裹和明信片，那么塞进去
                        if (!data.isMailboxFull(mailboxOwner)) {
                            if (level.getBlockEntity(topPos) instanceof MailboxBlockEntity mailbox && mailbox.checkToSend()) {
                                held.getOrCreateTag().putString("Sender", player.getName().getString());
                                data.addMailboxContents(mailboxOwner, held);
                                player.setItemInHand(handIn, ItemStack.EMPTY);
                                player.displayClientMessage(Component.translatable("message.contact.mailbox.deliver"), true);
                                AdvancementManager.givePlayerAdvancement(player.getServer(), (ServerPlayer) player, new ResourceLocation("contact:send_in_person"));
                                MailboxManager.updateState(level, topPos);
                            } else {
                                player.displayClientMessage(Component.translatable("message.contact.mailbox.check"), true);
                            }
                        } else {
                            player.displayClientMessage(Component.translatable("message.contact.mailbox.full"), true);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.used"), true);
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.no_owner"), true);
                }
                return InteractionResult.SUCCESS;
            } else if (mailboxOwner != null) {
                PlatformHelper.getCurrentServer().getProfileCache().get(mailboxOwner).ifPresent(gameProfile ->
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.others", gameProfile.getName()), true));
                return InteractionResult.SUCCESS;
            }
            player.displayClientMessage(Component.translatable("message.contact.mailbox.no_owner_tips"), true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (!level.isClientSide) {
            IMailboxDataProvider data = MailboxDataManager.getData(level);
            BlockPos topPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos : pos.above();
            data.removeMailboxData(GlobalPos.of(level.dimension(), topPos));
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MailboxBlockEntity::tick);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityTicker<? super E> entityTicker) {
        return MAILBOX_BLOCK_ENTITY.get() == blockEntityType ? (BlockEntityTicker<A>) entityTicker : null;
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

    static {
        LOWER_SHAPE = VoxelShapeHelper.createVoxelShape(7, 0, 7, 2, 16, 2);
        UPPER_SHAPE_NORTH = VoxelShapeHelper.createVoxelShape(3, 0, 1, 10, 9, 14);
        UPPER_SHAPE_EAST = VoxelShapeHelper.createVoxelShape(1, 0, 3, 14, 9, 10);
    }

    @Override
    public ResourceLocation getRegistryID() {
        return Contact.getRL(boxColor.getName() + "_mailbox");
    }
}
