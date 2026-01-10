package com.flechazo.contact.common.block;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.inter.ISilveroakEntry;
import com.flechazo.contact.platform.PlatformHelper;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class CenterMailboxBlock extends NormalHorizontalBlock implements ISilveroakEntry {
    public CenterMailboxBlock() {
        super(Properties.of().sound(SoundType.METAL));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (ContactCommonConfig.isEnableCenterMailbox()) {
                if (PlatformHelper.getMailboxPos(player.getUUID()) == null) {
                    var contents = PlatformHelper.getMailboxContents(player.getUUID());
                    boolean isEmpty = true;
                    for (int i = 0; i < contents.getContainerSize(); ++i) {
                        if (!contents.getItem(i).isEmpty()) {
                            player.getInventory().placeItemBackInInventory(contents.getItem(i));
                            isEmpty = false;
                        }
                    }

                    PlatformHelper.resetMailboxContents(player.getUUID());
                    if (!isEmpty) {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.pick_up"), true);
                    } else {
                        player.displayClientMessage(Component.translatable("message.contact.mailbox.empty"), true);
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    player.displayClientMessage(Component.translatable("message.contact.mailbox.deny"), true);
                }
            } else {
                player.displayClientMessage(Component.translatable("message.contact.mailbox.disabled"), true);
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Lists.newArrayList(new ItemStack(this));
    }

    @Override
    public ResourceLocation getRegistryID() {
        return Contact.getRL("center_mailbox");
    }
}
