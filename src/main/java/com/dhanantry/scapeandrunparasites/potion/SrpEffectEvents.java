package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public final class SrpEffectEvents {
    private SrpEffectEvents() {
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        MobEffectInstance viral = event.getEntity().getEffect(ModEffects.VIRAL);
        if (viral != null && SrpConfig.VIRAL_ENABLE.get()) {
            float amount = event.getAmount();
            float multiplier = (viral.getAmplifier() + 1.0F) * SrpConfig.VIRAL_AMOUNT.get().floatValue();
            event.setAmount(amount + amount * multiplier);
        }

        Entity attackerEntity = event.getSource().getEntity();
        if (attackerEntity instanceof LivingEntity attacker) {
            MobEffectInstance muscleOut = attacker.getEffect(ModEffects.MUSCLE_OUT);
            if (muscleOut != null) {
                float multiplier = (muscleOut.getAmplifier() + 1.0F) * SrpConfig.MUSCLE_OUT_DAMAGE_OUT.get().floatValue();
                event.setAmount(event.getAmount() * multiplier);
            }
        }
    }
}
