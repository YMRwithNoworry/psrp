package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;

public class SrpMobEffect extends MobEffect {
    public SrpMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        cures.clear();
    }

    public static void applyStackEffect(Holder<MobEffect> effect, LivingEntity target, int duration, int amplifier) {
        if (amplifier + 1 >= 256 || amplifier - 1 <= -256) {
            return;
        }

        MobEffectInstance current = target.getEffect(effect);
        if (current == null) {
            target.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
            return;
        }

        int stackedDuration = current.getDuration() + 40 <= duration ? duration : current.getDuration() + 10;
        String effectId = BuiltInRegistries.MOB_EFFECT.getKey(effect.value()).toString();
        for (String limitEntry : SrpConfig.STACKABLE_POTIONS_LIMIT.get()) {
            String[] parts = limitEntry.split(";");
            if (parts.length != 2 || !parts[0].equals(effectId)) {
                continue;
            }

            int limit = Integer.parseInt(parts[1]);
            if (amplifier > limit || current.getAmplifier() + 1 > limit) {
                target.addEffect(new MobEffectInstance(effect, stackedDuration, limit, false, false));
                return;
            }
            break;
        }

        if (current.getAmplifier() < amplifier) {
            target.addEffect(new MobEffectInstance(effect, stackedDuration, amplifier, false, false));
            return;
        }

        int nextAmplifier = current.getAmplifier();
        nextAmplifier += nextAmplifier < 0 ? -1 : 1;
        target.addEffect(new MobEffectInstance(effect, stackedDuration, nextAmplifier, false, false));
    }
}
