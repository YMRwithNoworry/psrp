package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public final class BleedMobEffect extends SrpMobEffect {
    public BleedMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(), entity.getZ(), 1, 0.15D, 0.3D, 0.15D, 0.0D);
            float damage = entity.getMaxHealth() * SrpConfig.BLEEDING_DAMAGE.get().floatValue();
            if (entity.getX() != entity.xo || entity.getZ() != entity.zo) {
                damage *= amplifier + 1.0F;
            }
            entity.hurt(entity.damageSources().generic(), Math.min(damage, SrpConfig.BLEEDING_DAMAGE_CAP.get().floatValue()));
        }
        return true;
    }
}
