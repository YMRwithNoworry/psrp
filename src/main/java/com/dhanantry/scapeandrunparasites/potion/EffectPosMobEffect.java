package com.dhanantry.scapeandrunparasites.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class EffectPosMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0xB890C8;

    public EffectPosMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            for (MobEffectInstance effect : entity.getActiveEffects()) {
                if (effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL) {
                    entity.hurt(entity.damageSources().magic(), 0.5F * (effect.getAmplifier() + 1.0F));
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
