package com.dhanantry.scapeandrunparasites.potion;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class EffectNegMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0x6FACB4;

    public EffectNegMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            List<MobEffectInstance> activeEffects = new ArrayList<>(entity.getActiveEffects());
            for (MobEffectInstance effect : activeEffects) {
                if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                    SrpMobEffect.applyStackEffect(effect.getEffect(), entity, 20, amplifier);
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
