package com.dhanantry.scapeandrunparasites.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public final class OverheatingMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0xFF8706;

    public OverheatingMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity.tickCount % 20 == 0) {
            entity.igniteForSeconds(2.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
