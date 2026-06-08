package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class DodSmokeTrailMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0x404040;
    public static final int LEGACY_GROUND_REMOVE_DURATION = 10;
    public static final int LEGACY_PARTICLE_COUNT = 6;
    public static final double LEGACY_PARTICLE_OFFSET = 0.15D;
    public static final double LEGACY_PARTICLE_SPEED = 0.02D;

    public DodSmokeTrailMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return true;
        }

        MobEffectInstance current = entity.getEffect(ModEffects.DOD_SMOKE_TRAIL);
        int duration = current == null ? 0 : current.getDuration();
        if (entity.onGround() && duration <= LEGACY_GROUND_REMOVE_DURATION) {
            entity.removeEffect(ModEffects.DOD_SMOKE_TRAIL);
            return true;
        }

        serverLevel.sendParticles(
            ParticleTypes.SMOKE,
            entity.getX(),
            entity.getY() + entity.getEyeHeight(),
            entity.getZ(),
            LEGACY_PARTICLE_COUNT,
            LEGACY_PARTICLE_OFFSET,
            LEGACY_PARTICLE_OFFSET,
            LEGACY_PARTICLE_OFFSET,
            LEGACY_PARTICLE_SPEED
        );
        return true;
    }
}
