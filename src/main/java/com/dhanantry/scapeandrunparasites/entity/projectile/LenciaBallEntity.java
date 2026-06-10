package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class LenciaBallEntity extends ThrowableItemProjectile {
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_DAMAGE = 70.0F;
    public static final float LEGACY_EXPLOSION_RADIUS = 10.0F;
    public static final float LEGACY_SHOOT_VELOCITY = 1.6F;

    public LenciaBallEntity(EntityType<? extends LenciaBallEntity> entityType, Level level) {
        super(entityType, level);
    }

    public LenciaBallEntity(Level level, LivingEntity owner, double xPower, double yPower, double zPower) {
        super(ModEntities.LENCIABALL.get(), owner, level);
        shoot(xPower, yPower, zPower, LEGACY_SHOOT_VELOCITY, 0.0F);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (!this.level().isClientSide && entity instanceof LivingEntity living) {
            if (living instanceof SrpParasiteMob && !(living instanceof NakEntity)) {
                discard();
                return;
            }
            living.hurt(this.damageSources().thrown(this, getOwner()), LEGACY_DAMAGE);
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().explode(this, getX(), getY(), getZ(), LEGACY_EXPLOSION_RADIUS, false, Level.ExplosionInteraction.NONE);
            this.level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }
}
