package com.flechazo.contact.common.item;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.entity.PostcardEntity;
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
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, id),
                new Properties().stacksTo(1),
                null);
        this.isEnderType = isEnderType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack itemstack = user.getItemInHand(hand);
        if (level.isClientSide) {
            if (itemstack.has(ContactDataComponents.POSTCARD_SENDER.get())) {
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
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation styleId = stack.get(ContactDataComponents.POSTCARD_STYLE_ID.get());
        if (styleId != null) {
            MutableComponent background = Component.translatable("tooltip.postcard." + styleId.getNamespace() + "." + styleId.getPath()).withStyle(ChatFormatting.GRAY);
            tooltip.add(Component.translatable("tooltip.contact.postcard.background", background).withStyle(ChatFormatting.GRAY));
        }
        this.addSenderInfoTooltip(stack, tooltipContext, tooltip, flag);
    }

    @Override
    public boolean isEnderType() {
        return isEnderType;
    }


    public static ItemStack getPostcard(ResourceLocation id, boolean isEnderType) {
        ItemStack postcard = new ItemStack(isEnderType ? ItemRegistry.ENDER_POSTCARD.get() : ItemRegistry.POSTCARD.get());
        postcard.set(ContactDataComponents.POSTCARD_STYLE_ID.get(), id);
        return postcard;
    }

    public static ItemStack setText(ItemStack postcard, String text) {
        postcard.set(ContactDataComponents.POSTCARD_TEXT.get(), text);
        return postcard;
    }

    public static String getText(ItemStack postcard) {
        return postcard.getOrDefault(ContactDataComponents.POSTCARD_TEXT.get(), "");
    }
}