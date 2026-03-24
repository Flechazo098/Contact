package com.flechazo.contact.common.tileentity;

import com.flechazo.contact.common.block.MailboxBlock;
import com.flechazo.contact.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.flechazo.contact.common.block.MailboxBlock.OPEN;
import static com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY;

public class MailboxBlockEntity extends BlockEntity {
    private boolean isOpened = false;
    private boolean needRefresh = false;
    private int refreshTicks = 20;
    private int checkToSendTicks = 0;
    private int angel = 0;

    public MailboxBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(MAILBOX_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        isOpened = nbt.getBoolean("IsOpened");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("IsOpened", isOpened);
    }

    public boolean checkToSend() {
        if (checkToSendTicks <= 0) {
            checkToSendTicks = 60;
            return false;
        } else {
            return true;
        }
    }


    public void refreshStatus() {
        if (!level.isClientSide) {
            {
                boolean now = !PlatformHelper.isMailboxEmpty(PlatformHelper.getMailboxOwner(level.dimension(), getBlockPos()));
                if (now != isOpened) {
                    needRefresh = true;
                    isOpened = now;
                    refresh();
                }
            }
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MailboxBlockEntity blockEntity) {
        if (!level.isClientSide) {
            if (blockEntity.refreshTicks >= 0) {
                blockEntity.refreshTicks--;
            }
            if (blockEntity.checkToSendTicks > 0) {
                blockEntity.checkToSendTicks--;
            }
            if (blockEntity.needRefresh || blockEntity.refreshTicks == 0) {
                blockEntity.refreshStatus();
                BlockState down = level.getBlockState(pos.below());
                if (down.getBlock() instanceof MailboxBlock && down.getValue(OPEN) != blockEntity.isOpened) {
                    level.setBlockAndUpdate(pos.below(), down.setValue(OPEN, blockEntity.isOpened));
                    blockEntity.needRefresh = false;
                } else {
                    blockEntity.needRefresh = false;
                    return;
                }

                if (state.getBlock() instanceof MailboxBlock) {
                    level.setBlockAndUpdate(pos, state.setValue(OPEN, blockEntity.isOpened));
                }
            }
        } else if (blockEntity.isOpened) {
            blockEntity.angel++;
            blockEntity.angel %= 40;
        }
    }

    private void refresh() {
        if (this.hasLevel() && !this.level.isClientSide) {
            ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(this);
            List<ServerPlayer> players = ((ServerLevel) this.level).getChunkSource().chunkMap.getPlayers(new ChunkPos(this.getBlockPos().getX() >> 4, this.getBlockPos().getZ() >> 4), false);
            for (ServerPlayer player : players) {
                player.connection.send(packet);
            }
        }
    }

    public boolean isOpened() {
        return isOpened;
    }

    public int getAngel() {
        return angel;
    }
}
