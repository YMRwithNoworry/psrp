package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public final class SrpEffectEvents {
    private SrpEffectEvents() {
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!SrpConfig.VIRAL_ENABLE.get()) {
            return;
        }
        MobEffectInstance viral = event.getEntity().getEffect(ModEffects.VIRAL);
        if (viral != null) {
            float amount = event.getAmount();
            float multiplier = (viral.getAmplifier() + 1.0F) * SrpConfig.VIRAL_AMOUNT.get().floatValue();
            event.setAmount(amount + amount * multiplier);
        }
    }
}
