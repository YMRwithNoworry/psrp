package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.EventHooks;

public class WebballEntity extends ThrowableItemProjectile {
    public static final int LEGACY_ENTITY_ID = 101;
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_BLIND_CHANCE = 0.3F;
    public static final int LEGACY_BLIND_TICKS = 60;
    public static final int LEGACY_BLIND_AMPLIFIER = 0;
    public static final int LEGACY_TIMEOUT_TICKS = 60;
    public static final int LEGACY_MIN_TIMEOUT_WEBS = 1;
    public static final int LEGACY_RANDOM_TIMEOUT_WEBS = 3;
    public static final byte LEGACY_TYPE_ONE = 1;
    public static final byte LEGACY_TYPE_TWO = 2;
    public static final byte LEGACY_TYPE_THREE = 3;
    private static final int[] LEGACY_WEB_OFFSETS = {-1, 0, 1};

    private byte webType = LEGACY_TYPE_ONE;

    public WebballEntity(EntityType<? extends WebballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public WebballEntity(Level level, LivingEntity owner, double xPower, double yPower, double zPower, byte webType) {
        super(ModEntities.WEBBALL.get(), owner, level);
        this.webType = webType;
        shoot(xPower, yPower, zPower, 1.6F, 0.0F);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.COBWEB;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount > LEGACY_TIMEOUT_TICKS) {
            setWebsAround();
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, getOwner()), 0.0F);
        if (entity instanceof Player player && !player.hasEffect(MobEffects.BLINDNESS) && this.random.nextFloat() < LEGACY_BLIND_CHANCE) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, LEGACY_BLIND_TICKS, LEGACY_BLIND_AMPLIFIER, false, true), this);
        }
        if (entity instanceof LivingEntity living) {
            placeWebAt(BlockPos.containing(living.getX(), Math.floor(living.getBoundingBox().minY), living.getZ()));
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        placeWebAt(result.getBlockPos().relative(result.getDirection()));
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.COBWEB));
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("WebType", this.webType);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WebType")) {
            this.webType = tag.getByte("WebType");
        }
    }

    public byte getWebType() {
        return this.webType;
    }

    private void setWebsAround() {
        int webs = LEGACY_MIN_TIMEOUT_WEBS + this.random.nextInt(LEGACY_RANDOM_TIMEOUT_WEBS);
        for (int i = 0; i < webs; i++) {
            int x = LEGACY_WEB_OFFSETS[this.random.nextInt(LEGACY_WEB_OFFSETS.length)];
            int y = LEGACY_WEB_OFFSETS[this.random.nextInt(LEGACY_WEB_OFFSETS.length)];
            int z = LEGACY_WEB_OFFSETS[this.random.nextInt(LEGACY_WEB_OFFSETS.length)];
            placeWebAt(BlockPos.containing(getX() + x, getY() + y, getZ() + z));
        }
    }

    private boolean placeWebAt(BlockPos pos) {
        if (!EventHooks.canEntityGrief(this.level(), this) || !this.level().isEmptyBlock(pos) || !mayInteract(this.level(), pos)) {
            return false;
        }
        return this.level().setBlock(pos, Blocks.COBWEB.defaultBlockState(), 3);
    }
}
