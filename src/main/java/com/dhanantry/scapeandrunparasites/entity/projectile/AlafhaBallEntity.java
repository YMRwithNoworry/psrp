package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.entity.monster.derived.HebluEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class AlafhaBallEntity extends ThrowableItemProjectile {
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_DAMAGE = 30.0F;
    public static final double LEGACY_HIT_AOE = 3.0D;
    public static final int LEGACY_DLER_TICKS = 300;
    public static final int LEGACY_DLER_CLOUD_TICKS = 360;
    public static final int LEGACY_COTH_TICKS = 300;
    public static final int LEGACY_EFFECT_AMPLIFIER = 0;
    public static final float LEGACY_CLOUD_RADIUS = 2.0F;
    public static final float LEGACY_CLOUD_RADIUS_HEBLU = 5.0F;
    public static final float LEGACY_CLOUD_RADIUS_ON_USE = -0.5F;
    public static final int LEGACY_CLOUD_WAIT_TICKS = 30;
    public static final int LEGACY_CLOUD_DURATION_TICKS = 60;
    public static final float LEGACY_SHOOT_VELOCITY = 1.6F;

    public AlafhaBallEntity(EntityType<? extends AlafhaBallEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AlafhaBallEntity(Level level, LivingEntity owner, double xPower, double yPower, double zPower) {
        super(ModEntities.ALAFHABALL.get(), owner, level);
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
            damageArea();
            spawnToxicCloud();
            playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
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
                this.level().addParticle(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void damageArea() {
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(LEGACY_HIT_AOE), this::canHarm)) {
            living.addEffect(new MobEffectInstance(ModEffects.NEEDLER, LEGACY_DLER_TICKS, LEGACY_EFFECT_AMPLIFIER, false, false), this);
            living.hurt(this.damageSources().thrown(this, getOwner()), LEGACY_DAMAGE);
        }
    }

    private boolean canHarm(LivingEntity living) {
        return living.isAlive() && !(living instanceof SrpParasiteMob);
    }

    private void spawnToxicCloud() {
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), getX(), getY(), getZ());
        Entity owner = getOwner();
        cloud.setOwner(owner instanceof LivingEntity living ? living : null);
        cloud.setDuration(LEGACY_CLOUD_DURATION_TICKS);
        cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
        if (owner instanceof HebluEntity) {
            cloud.setRadius(LEGACY_CLOUD_RADIUS_HEBLU);
            cloud.addEffect(new MobEffectInstance(ModEffects.VOMIT, LEGACY_COTH_TICKS, LEGACY_EFFECT_AMPLIFIER, false, false));
            cloud.addEffect(new MobEffectInstance(ModEffects.VIRAL, LEGACY_COTH_TICKS, LEGACY_EFFECT_AMPLIFIER, false, false));
        } else {
            cloud.setRadius(LEGACY_CLOUD_RADIUS);
            cloud.setRadiusOnUse(LEGACY_CLOUD_RADIUS_ON_USE);
            cloud.setWaitTime(LEGACY_CLOUD_WAIT_TICKS);
            cloud.addEffect(new MobEffectInstance(ModEffects.NEEDLER, LEGACY_DLER_CLOUD_TICKS, LEGACY_EFFECT_AMPLIFIER, false, false));
        }
        cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
        this.level().addFreshEntity(cloud);
    }
}
