package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
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

public class AngedballEntity extends ThrowableItemProjectile {
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_DAMAGE = 27.0F;
    public static final float LEGACY_CLOUD_RADIUS = 2.5F;
    public static final float LEGACY_CLOUD_RADIUS_ON_USE = -0.5F;
    public static final int LEGACY_CLOUD_WAIT_TICKS = 10;
    public static final int LEGACY_CLOUD_DURATION_TICKS = 100;
    public static final int LEGACY_POISON_TICKS = 300;
    public static final int LEGACY_CORROSIVE_TICKS = 100;
    public static final int LEGACY_EFFECT_AMPLIFIER = 0;
    public static final float LEGACY_SHOOT_VELOCITY = 1.6F;

    public AngedballEntity(EntityType<? extends AngedballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AngedballEntity(Level level, LivingEntity owner, double xPower, double yPower, double zPower) {
        super(ModEntities.ANGEDBALL.get(), owner, level);
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
            spawnToxicCloud();
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

    private void spawnToxicCloud() {
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), getX(), getY(), getZ());
        cloud.setOwner(getOwner() instanceof LivingEntity living ? living : null);
        cloud.setRadius(LEGACY_CLOUD_RADIUS);
        cloud.setRadiusOnUse(LEGACY_CLOUD_RADIUS_ON_USE);
        cloud.setWaitTime(LEGACY_CLOUD_WAIT_TICKS);
        cloud.setDuration(LEGACY_CLOUD_DURATION_TICKS);
        cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
        cloud.addEffect(new MobEffectInstance(MobEffects.POISON, LEGACY_POISON_TICKS, LEGACY_EFFECT_AMPLIFIER, false, false));
        cloud.addEffect(new MobEffectInstance(ModEffects.CORROSIVE, LEGACY_CORROSIVE_TICKS, LEGACY_EFFECT_AMPLIFIER, false, false));
        this.level().addFreshEntity(cloud);
    }
}
