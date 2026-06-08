package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public final class ContaminationMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0x9DF100;
    public static final int LEGACY_SELF_DAMAGE_INTERVAL = 40;
    public static final int LEGACY_SPREAD_XZ_RANGE = 4;
    public static final int LEGACY_SPREAD_Y_RANGE = 3;

    public ContaminationMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity.tickCount % LEGACY_SELF_DAMAGE_INTERVAL == 0) {
            if (entity.getHealth() > 1.0F) {
                entity.hurt(entity.damageSources().magic(), 1.0F);
            }

            MobEffectInstance current = entity.getEffect(ModEffects.CONTAMINATION);
            int duration = current == null ? 0 : current.getDuration();
            AABB spreadBox = new AABB(
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                entity.getX() + 1.0D,
                entity.getY() + 1.0D,
                entity.getZ() + 1.0D
            ).inflate(LEGACY_SPREAD_XZ_RANGE, LEGACY_SPREAD_Y_RANGE, LEGACY_SPREAD_XZ_RANGE);
            for (LivingEntity nearby : entity.level().getEntitiesOfClass(LivingEntity.class, spreadBox)) {
                SrpMobEffect.applyStackEffect(ModEffects.CONTAMINATION, nearby, duration, amplifier);
            }
        }
        return true;
    }
}
