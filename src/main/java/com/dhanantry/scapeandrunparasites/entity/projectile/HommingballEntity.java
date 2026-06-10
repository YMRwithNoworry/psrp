package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HommingballEntity extends ThrowableItemProjectile {
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_SHOOT_VELOCITY = 1.2F;
    public static final float LEGACY_DAMAGE = 15.0F;
    public static final double LEGACY_HOMING_ACCELERATION = 0.12D;
    public static final int LEGACY_LIFETIME = 120;

    private UUID targetUuid;
    private int targetId = -1;
    private float damage = LEGACY_DAMAGE;

    public HommingballEntity(EntityType<? extends HommingballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public HommingballEntity(Level level, LivingEntity owner, LivingEntity target, float damage) {
        super(ModEntities.HOMMING.get(), owner, level);
        setTarget(target);
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            LivingEntity target = getTarget();
            if (target != null && target.isAlive()) {
                Vec3 desired = target.getEyePosition().subtract(position()).normalize().scale(LEGACY_HOMING_ACCELERATION);
                setDeltaMovement(getDeltaMovement().scale(0.92D).add(desired));
                hasImpulse = true;
            }
            if (this.tickCount > LEGACY_LIFETIME) {
                discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (!this.level().isClientSide && entity instanceof LivingEntity living) {
            if (living instanceof SrpParasiteMob) {
                discard();
                return;
            }
            living.hurt(this.damageSources().thrown(this, getOwner()), this.damage);
        }
        super.onHitEntity(result);
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
            ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SLIME_BALL));
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
        tag.putFloat("SrpDamage", this.damage);
        if (this.targetUuid != null) {
            tag.putUUID("SrpTargetUuid", this.targetUuid);
        }
        tag.putInt("SrpTargetId", this.targetId);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpDamage")) {
            this.damage = tag.getFloat("SrpDamage");
        }
        if (tag.hasUUID("SrpTargetUuid")) {
            this.targetUuid = tag.getUUID("SrpTargetUuid");
        }
        if (tag.contains("SrpTargetId")) {
            this.targetId = tag.getInt("SrpTargetId");
        }
    }

    private void setTarget(LivingEntity target) {
        if (target != null) {
            this.targetUuid = target.getUUID();
            this.targetId = target.getId();
        }
    }

    private LivingEntity getTarget() {
        if (this.level() instanceof ServerLevel serverLevel && this.targetUuid != null) {
            Entity entity = serverLevel.getEntity(this.targetUuid);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        if (this.targetId >= 0) {
            Entity entity = this.level().getEntity(this.targetId);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return Optional.ofNullable(getOwner())
            .filter(LivingEntity.class::isInstance)
            .map(LivingEntity.class::cast)
            .map(LivingEntity::getLastHurtMob)
            .orElse(null);
    }
}
