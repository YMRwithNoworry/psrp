package com.dhanantry.scapeandrunparasites.potion;

import net.minecraft.world.effect.MobEffectCategory;

public final class ThornshadeThornsMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0x421F7E;
    public static final int LEGACY_POTION_DURATION = 60;

    public ThornshadeThornsMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }
}
