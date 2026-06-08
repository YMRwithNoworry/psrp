package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.potion.BleedMobEffect;
import com.dhanantry.scapeandrunparasites.potion.RageMobEffect;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SRPMain.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> VIRAL = EFFECTS.register("viral", () ->
        new SrpMobEffect(MobEffectCategory.HARMFUL, 0x136334));
    public static final DeferredHolder<MobEffect, BleedMobEffect> BLEED = EFFECTS.register("bleed", () ->
        new BleedMobEffect(MobEffectCategory.HARMFUL, 0x5E0806));
    public static final DeferredHolder<MobEffect, RageMobEffect> RAGE = EFFECTS.register("rage", () ->
        new RageMobEffect(MobEffectCategory.BENEFICIAL, RageMobEffect.LEGACY_COLOR));

    private ModEffects() {
    }

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
