package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.potion.BleedMobEffect;
import com.dhanantry.scapeandrunparasites.potion.CorrosiveMobEffect;
import com.dhanantry.scapeandrunparasites.potion.EffectNegMobEffect;
import com.dhanantry.scapeandrunparasites.potion.EffectPosMobEffect;
import com.dhanantry.scapeandrunparasites.potion.IndeafMobEffect;
import com.dhanantry.scapeandrunparasites.potion.RageMobEffect;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import com.dhanantry.scapeandrunparasites.potion.SrpSensingMobEffect;
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
    public static final DeferredHolder<MobEffect, CorrosiveMobEffect> CORROSIVE = EFFECTS.register("corrosive", () ->
        new CorrosiveMobEffect(MobEffectCategory.HARMFUL, CorrosiveMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, SrpSensingMobEffect> VOMIT = EFFECTS.register("vomit", () ->
        new SrpSensingMobEffect(MobEffectCategory.BENEFICIAL, SrpSensingMobEffect.LEGACY_VOMIT_COLOR, "effect.vomit_follow_range", SrpSensingMobEffect.LEGACY_VOMIT_FOLLOW_RANGE_MULTIPLIER));
    public static final DeferredHolder<MobEffect, RageMobEffect> RAGE = EFFECTS.register("rage", () ->
        new RageMobEffect(MobEffectCategory.BENEFICIAL, RageMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, SrpSensingMobEffect> SENSES = EFFECTS.register("senses", () ->
        new SrpSensingMobEffect(MobEffectCategory.BENEFICIAL, SrpSensingMobEffect.LEGACY_SENSES_COLOR, "effect.senses_follow_range", SrpSensingMobEffect.LEGACY_SENSES_FOLLOW_RANGE_MULTIPLIER));
    public static final DeferredHolder<MobEffect, IndeafMobEffect> INDEAF = EFFECTS.register("indeaf", () ->
        new IndeafMobEffect(MobEffectCategory.BENEFICIAL, IndeafMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, EffectPosMobEffect> EFFECT_POS = EFFECTS.register("effectpos", () ->
        new EffectPosMobEffect(MobEffectCategory.HARMFUL, EffectPosMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, EffectNegMobEffect> EFFECT_NEG = EFFECTS.register("effectneg", () ->
        new EffectNegMobEffect(MobEffectCategory.HARMFUL, EffectNegMobEffect.LEGACY_COLOR));

    private ModEffects() {
    }

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
