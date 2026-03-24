package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.common.entity.PostcardEntity;
import com.flechazo.contact.common.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PostcardItem extends NormalItem implements IMailItem {
    private final boolean isEnderType;

    public PostcardItem(String id, boolean isEnderType) {
        super(new ResourceLocation(Contact.MOD_ID, id),
                new Properties().stacksTo(1),
                null);
        this.isEnderType = isEnderType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack itemstack = user.getItemInHand(hand);
        if (level.isClientSide) {
            if (itemstack.getOrCreateTag().contains("Sender")) {
                ClientProxy.openPostcardToRead(itemstack);
            } else {
                ClientProxy.openPostcardToEdit(itemstack, user, hand);
            }
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos blockPos2 = blockPos.relative(direction);
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (player != null) {
            if (!player.isShiftKeyDown()) {
                return InteractionResult.PASS;
            }
            if (!this.canPlaceOn(player, direction, itemStack, blockPos2)) {
                return InteractionResult.FAIL;
            }
        }
        Level level = context.getLevel();
        PostcardEntity postcardEntity = new PostcardEntity(level, blockPos2, direction);
        if (postcardEntity.survives()) {
            if (!level.isClientSide) {
                postcardEntity.playPlacementSound();
                level.gameEvent(player, GameEvent.ENTITY_PLACE, postcardEntity.getPos());
                level.addFreshEntity(postcardEntity);
                postcardEntity.setHeldItemStack(itemStack.copy());
            }
            itemStack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.CONSUME;
    }

    protected boolean canPlaceOn(Player player, Direction side, ItemStack stack, BlockPos pos) {
        return !player.level().isOutsideBuildHeight(pos) && player.mayUseItemAt(pos, side, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getOrCreateTag().contains("Info")) {
            MutableComponent background = Component.translatable("tooltip.contact.postcard." + stack.getOrCreateTag().getCompound("Info").getString("ID")).withStyle(ChatFormatting.GRAY);
            tooltip.add(Component.translatable("tooltip.contact.postcard.background", background).withStyle(ChatFormatting.GRAY));
        }
        if (stack.getOrCreateTag().contains("CardID")) {
            ResourceLocation id = new ResourceLocation(stack.getOrCreateTag().getString("CardID"));
            MutableComponent background = Component.translatable("tooltip.postcard." + id.getNamespace() + "." + id.getPath()).withStyle(ChatFormatting.GRAY);
            tooltip.add(Component.translatable("tooltip.contact.postcard.background", background).withStyle(ChatFormatting.GRAY));
        }
        this.addSenderInfoTooltip(stack, level, tooltip, flag);
    }

    @Override
    public boolean isEnderType() {
        return isEnderType;
    }

    public static ItemStack getPostcard(ResourceLocation id, boolean isEnderType) {
        ItemStack postcard = new ItemStack(isEnderType ? ItemRegistry.ENDER_POSTCARD.get() : ItemRegistry.POSTCARD.get());
        CompoundTag nbt = new CompoundTag();
        nbt.putString("CardID", id.toString());
        postcard.setTag(nbt);
        return postcard;
    }

    public static ItemStack setText(ItemStack postcard, String text) {
        postcard.addTagElement("Text", StringTag.valueOf(text));
        return postcard;
    }

    public static String getText(ItemStack postcard) {
        if (postcard.hasTag()) {
            return postcard.getOrCreateTag().getString("Text");
        }
        return "";
    }
}