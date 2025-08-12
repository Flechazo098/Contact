package com.flechazo.contact.common.handler;

import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.common.storage.MailboxDataManager;
import com.flechazo.contact.common.storage.PlayerMailboxData;
import com.flechazo.contact.common.tileentity.MailboxBlockEntity;
import com.flechazo.contact.platform.PlatformHelper;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.UUID;

public final class MailboxManager {
    private static final List<MailToBeSent> READY_TO_REMOVE = Lists.newArrayList();
    private static int updateTick = 0;

    public static void onServerTick(MinecraftServer server) {
        IMailboxDataProvider data = MailboxDataManager.getData(server);
        updateTick = ++updateTick % 20;
        if (updateTick == 0) {
            for (MailToBeSent mail : data.getMailList()) {
                mail.tick(20);
                if (mail.isReady()) {
                    UUID uuid = mail.getUUID();
                    if (data.addMailboxContents(uuid, mail.getContents())) {
                        Player player = server.getPlayerList().getPlayer(uuid);
                        if (player != null) {
                            player.displayClientMessage(Component.translatable("message.contact.mailbox.new_mail"), false);
                        }
                        updateState(uuid, data.data());
                        READY_TO_REMOVE.add(mail);
                    }
                }
            }
            if (!READY_TO_REMOVE.isEmpty()) {
                data.getMailList().removeAll(READY_TO_REMOVE);
                READY_TO_REMOVE.clear();
            }
        }
    }

    public static void updateState(UUID uuid, PlayerMailboxData data) {
        GlobalPos posData = data.getMailboxPos(uuid);
        if (posData != null) {
            updateState(PlatformHelper.getCurrentServer().getLevel(posData.dimension()), posData.pos());
        }
    }

    @SuppressWarnings("deprecation")
    public static void updateState(Level level, BlockPos pos) {
        if (level != null) {
            if (level.hasChunkAt(pos)) {
                BlockEntity te = level.getBlockEntity(pos);
                if (te instanceof MailboxBlockEntity) {
                    ((MailboxBlockEntity) te).refreshStatus();
                }
            }
        }
    }

    public static int getDeliveryTicks(ResourceKey<Level> fromLevel, BlockPos fromPos, ResourceKey<Level> toLevel, BlockPos toPos) {
        int time = 0;
        if (fromLevel != toLevel) {
            time += ContactCommonConfig.getTicksToAnotherWorld();
        }
        int distance = Math.abs(fromPos.getX() - toPos.getX()) + Math.abs(fromPos.getZ() - toPos.getZ());
        if (distance > 9000) distance = 9000;
        time += ContactCommonConfig.getPostalSpeed() * distance;
        return time;
    }

    public static int getDeliveryTicks(GlobalPos fromPos, GlobalPos toPos) {
        return getDeliveryTicks(fromPos.dimension(), fromPos.pos(), toPos.dimension(), toPos.pos());
    }
}
