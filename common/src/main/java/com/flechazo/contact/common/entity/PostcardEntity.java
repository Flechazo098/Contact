package com.flechazo.contact.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import static com.flechazo.contact.common.entity.EntityTypeRegistry.POSTCARD;

public class PostcardEntity extends HangingEntity {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(PostcardEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> ROTATION = SynchedEntityData.defineId(PostcardEntity.class, EntityDataSerializers.INT);
    private boolean fixed;

    public PostcardEntity(EntityType<? extends HangingEntity> entityType, Level level)
    {
        super(entityType, level);
    }

    public PostcardEntity(Level level, BlockPos pos, Direction facing)
    {
        this(POSTCARD.get(), level, pos, facing);
    }

    public PostcardEntity(EntityType<? extends HangingEntity> type, Level world, BlockPos pos, Direction facing)
    {
        super(type, world, pos);
        this.setDirection(facing);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.0f;
    }


    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(ROTATION, 0);
    }

    @Override
    protected void setDirection(Direction direction) {
        Validate.notNull(direction);
        this.direction = direction;
        if (direction.getAxis().isHorizontal()) {
            this.setXRot(0.0f);
            this.setYRot(this.direction.get2DDataValue() * 90);
        } else {
            this.setXRot(-90 * direction.getAxisDirection().getStep());
            this.setYRot(0.0f);
        }
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        this.recalculateBoundingBox();
    }

    @Override
    protected void recalculateBoundingBox() {
        if (this.direction == null)
        {
            return;
        }
        double e = (double) this.pos.getX() + 0.5 - (double) this.direction.getStepX() * 0.46875;
        double f = (double) this.pos.getY() + 0.5 - (double) this.direction.getStepY() * 0.46875;
        double g = (double) this.pos.getZ() + 0.5 - (double) this.direction.getStepZ() * 0.46875;
        this.setPosRaw(e, f, g);
        double h = this.getWidth();
        double i = this.getHeight();
        double j = this.getWidth();
        Direction.Axis axis = this.direction.getAxis();
        switch (axis) {
            case X -> h = 1.0;
            case Y -> i = 1.0;
            case Z -> j = 1.0;
        }
        this.setBoundingBox(new AABB(e - (h /= 32.0), f - (i /= 32.0), g - (j /= 32.0), e + h, f + i, g + j));
    }

    @Override
    public boolean survives() {
        if (this.fixed) {
            return true;
        }
        if (!this.level().noCollision(this)) {
            return false;
        }
        BlockState blockState = this.level().getBlockState(this.pos.relative(this.direction.getOpposite()));
        if (!(blockState.isSolid() || this.direction.getAxis().isHorizontal() && DiodeBlock.isDiode(blockState))) {
            return false;
        }
        return this.level().getEntities(this, this.getBoundingBox(), HANGING_ENTITY).isEmpty();
    }

    @Override
    public void move(MoverType moverType, Vec3 movement) {
        if (!this.fixed) {
            super.move(moverType, movement);
        }
    }

    @Override
    public void push(double deltaX, double deltaY, double deltaZ) {
        if (!this.fixed) {
            super.push(deltaX, deltaY, deltaZ);
        }
    }

    @Override
    public float getPickRadius() {
        return 0.0f;
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.fixed) {
            if (source.typeHolder().is(DamageTypes.FELL_OUT_OF_WORLD) || source.isCreativePlayer()) {
                return super.hurt(source, amount);
            }
            return false;
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d = 16.0;
        return distance < (d *= 64.0 * ItemFrame.getViewScale()) * d;
    }

    @Override
    public void dropItem(@Nullable Entity entity) {
        this.playSound(this.getBreakSound(), 1.0f, 1.0f);
        this.dropPostcard(entity);
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.ITEM_FRAME_BREAK;
    }

    @Override
    public void playPlacementSound() {
        this.playSound(this.getPlaceSound(), 1.0f, 1.0f);
    }

    public SoundEvent getPlaceSound() {
        return SoundEvents.ITEM_FRAME_PLACE;
    }

    private void dropPostcard(@Nullable Entity entity) {
        if (this.fixed) {
            return;
        }
        ItemStack postcard = this.getPostcard();
        this.setHeldItemStack(ItemStack.EMPTY);
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            return;
        }
        if (entity instanceof Player player) {
            if (player.getAbilities().instabuild) {
                return;
            }
        }
        if (!postcard.isEmpty()) {
            postcard = postcard.copy();
            this.spawnAtLocation(postcard);
        }
    }

    public ItemStack getPostcard() {
        return this.getEntityData().get(ITEM_STACK);
    }

    public void setHeldItemStack(ItemStack value) {
        if (!value.isEmpty()) {
            value = value.copy();
            value.setCount(1);
        }
        this.setAsStackHolder(value);
        this.getEntityData().set(ITEM_STACK, value);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (data.equals(ITEM_STACK)) {
            this.setAsStackHolder(this.getPostcard());
        }
    }

    private void setAsStackHolder(ItemStack stack) {
        if (!stack.isEmpty() && stack.getEntityRepresentation() != this) {
            stack.setEntityRepresentation(this);
        }
        this.recalculateBoundingBox();
    }

    public int getRotation() {
        return this.getEntityData().get(ROTATION);
    }

    private void setRotation(int value) {
        this.getEntityData().set(ROTATION, value % 16);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.getPostcard().isEmpty()) {
            tag.put("Item", this.getPostcard().save(new CompoundTag()));
            tag.putByte("ItemRotation", (byte) this.getRotation());
        }
        tag.putByte("Facing", (byte) this.direction.get3DDataValue());
        tag.putBoolean("Invisible", this.isInvisible());
        tag.putBoolean("Fixed", this.fixed);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        CompoundTag compoundTag = tag.getCompound("Item");
        if (!compoundTag.isEmpty()) {
            ItemStack postcard = ItemStack.of(compoundTag);
            this.setHeldItemStack(postcard);
            this.setRotation(tag.getByte("ItemRotation"));
        }
        this.setDirection(Direction.from3DDataValue(tag.getByte("Facing")));
        this.setInvisible(tag.getBoolean("Invisible"));
        this.fixed = tag.getBoolean("Fixed");
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        boolean isEmpty = !this.getPostcard().isEmpty();
        if (this.fixed) {
            return InteractionResult.PASS;
        }
        if (this.level().isClientSide()) {
            return isEmpty ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else if (!isEmpty) {
            this.hurt(this.damageSources().playerAttack(player), 1);
        }
        this.playSound(this.getRotateItemSound(), 1.0f, 1.0f);
        if (getDirection().get3DDataValue() > 1) {
            this.setRotation((this.getRotation() + 1) % 3);
        } else {
            this.setRotation(this.getRotation() + 1);
        }

        return InteractionResult.CONSUME;
    }

    public SoundEvent getRotateItemSound() {
        return SoundEvents.ITEM_FRAME_ROTATE_ITEM;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    @Override
    public ItemStack getPickResult() {
        return this.getPostcard();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        Direction direction = this.getDirection();
        int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
        return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + this.getRotation() * 45 + i);
    }
}
