package com.dhanantry.scapeandrunparasites.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public final class IndeafMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0xFFDD00;

    public IndeafMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            entity.setDeltaMovement(0.0D, entity.getDeltaMovement().y, 0.0D);
            entity.xxa = 0.0F;
            entity.zza = 0.0F;
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
