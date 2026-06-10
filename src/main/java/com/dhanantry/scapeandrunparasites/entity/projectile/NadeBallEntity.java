package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class NadeBallEntity extends ThrowableItemProjectile {
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final int LEGACY_FUSE_TICKS = 4;
    public static final int LEGACY_DURATION_TICKS = 60;
    public static final float LEGACY_SHOOT_VELOCITY = 1.6F;

    private int fuse = LEGACY_FUSE_TICKS;
    private int duration = LEGACY_DURATION_TICKS;

    public NadeBallEntity(EntityType<? extends NadeBallEntity> entityType, Level level) {
        super(entityType, level);
    }

    public NadeBallEntity(Level level, LivingEntity owner, double xPower, double yPower, double zPower, int fuse, int duration) {
        super(ModEntities.NADEBALL.get(), owner, level);
        this.fuse = fuse;
        this.duration = duration;
        shoot(xPower, yPower, zPower, LEGACY_SHOOT_VELOCITY, 0.0F);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            Entity owner = getOwner();
            if (owner instanceof LivingEntity living && living.isAlive()) {
                NadeEntity nade = new NadeEntity(this.level(), living, this.fuse, this.duration);
                nade.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
                this.level().addFreshEntity(nade);
            }
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
}
