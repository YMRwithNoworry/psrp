package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.potion.BleedMobEffect;
import com.dhanantry.scapeandrunparasites.potion.ContaminationMobEffect;
import com.dhanantry.scapeandrunparasites.potion.CorrosiveMobEffect;
import com.dhanantry.scapeandrunparasites.potion.DodSmokeTrailMobEffect;
import com.dhanantry.scapeandrunparasites.potion.EffectNegMobEffect;
import com.dhanantry.scapeandrunparasites.potion.EffectPosMobEffect;
import com.dhanantry.scapeandrunparasites.potion.IndeafMobEffect;
import com.dhanantry.scapeandrunparasites.potion.NeedlerMobEffect;
import com.dhanantry.scapeandrunparasites.potion.OverheatingMobEffect;
import com.dhanantry.scapeandrunparasites.potion.RageMobEffect;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import com.dhanantry.scapeandrunparasites.potion.SrpSensingMobEffect;
import com.dhanantry.scapeandrunparasites.potion.TheSignMobEffect;
import com.dhanantry.scapeandrunparasites.potion.ThornshadeThornsMobEffect;
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
    public static final DeferredHolder<MobEffect, DodSmokeTrailMobEffect> DOD_SMOKE_TRAIL = EFFECTS.register("dod_smoke_trail", () ->
        new DodSmokeTrailMobEffect(MobEffectCategory.BENEFICIAL, DodSmokeTrailMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, MobEffect> FEAR = EFFECTS.register("fear", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x111114));
    public static final DeferredHolder<MobEffect, MobEffect> ANTIMALL = EFFECTS.register("antimall", () ->
        new SrpMobEffect(MobEffectCategory.HARMFUL, 0x88626C));
    public static final DeferredHolder<MobEffect, CorrosiveMobEffect> CORROSIVE = EFFECTS.register("corrosive", () ->
        new CorrosiveMobEffect(MobEffectCategory.HARMFUL, CorrosiveMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, SrpSensingMobEffect> VOMIT = EFFECTS.register("vomit", () ->
        new SrpSensingMobEffect(MobEffectCategory.BENEFICIAL, SrpSensingMobEffect.LEGACY_VOMIT_COLOR, "effect.vomit_follow_range", SrpSensingMobEffect.LEGACY_VOMIT_FOLLOW_RANGE_MULTIPLIER));
    public static final DeferredHolder<MobEffect, RageMobEffect> RAGE = EFFECTS.register("rage", () ->
        new RageMobEffect(MobEffectCategory.BENEFICIAL, RageMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, MobEffect> REPEL = EFFECTS.register("repel", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x43AC30));
    public static final DeferredHolder<MobEffect, SrpSensingMobEffect> SENSES = EFFECTS.register("senses", () ->
        new SrpSensingMobEffect(MobEffectCategory.BENEFICIAL, SrpSensingMobEffect.LEGACY_SENSES_COLOR, "effect.senses_follow_range", SrpSensingMobEffect.LEGACY_SENSES_FOLLOW_RANGE_MULTIPLIER));
    public static final DeferredHolder<MobEffect, MobEffect> DEBAR = EFFECTS.register("debar", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x9E134B));
    public static final DeferredHolder<MobEffect, MobEffect> LINK = EFFECTS.register("link", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0xFF7595));
    public static final DeferredHolder<MobEffect, MobEffect> PIVOT = EFFECTS.register("pivot", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0xFFB1C3));
    public static final DeferredHolder<MobEffect, MobEffect> JUGG = EFFECTS.register("jugg", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0xBDB885));
    public static final DeferredHolder<MobEffect, MobEffect> PARATE = EFFECTS.register("parate", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0xB35736));
    public static final DeferredHolder<MobEffect, MobEffect> PRIMITIVE = EFFECTS.register("primitive", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x8F4C45));
    public static final DeferredHolder<MobEffect, MobEffect> ADAPTED = EFFECTS.register("adapted", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x7F584E));
    public static final DeferredHolder<MobEffect, MobEffect> PURE = EFFECTS.register("pure", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x0DA532));
    public static final DeferredHolder<MobEffect, MobEffect> CRUDE = EFFECTS.register("crude", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x0DA532));
    public static final DeferredHolder<MobEffect, MobEffect> FERAL = EFFECTS.register("feral", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x993030));
    public static final DeferredHolder<MobEffect, MobEffect> NEXUS = EFFECTS.register("nexus", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x487848));
    public static final DeferredHolder<MobEffect, MobEffect> BRAINING = EFFECTS.register("braining", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x796E85));
    public static final DeferredHolder<MobEffect, MobEffect> NOVISION = EFFECTS.register("novision", () ->
        new SrpMobEffect(MobEffectCategory.BENEFICIAL, 0x182639));
    public static final DeferredHolder<MobEffect, IndeafMobEffect> INDEAF = EFFECTS.register("indeaf", () ->
        new IndeafMobEffect(MobEffectCategory.BENEFICIAL, IndeafMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, OverheatingMobEffect> OVERHEATING = EFFECTS.register("overheating", () ->
        new OverheatingMobEffect(MobEffectCategory.HARMFUL, OverheatingMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, ContaminationMobEffect> CONTAMINATION = EFFECTS.register("conta", () ->
        new ContaminationMobEffect(MobEffectCategory.HARMFUL, ContaminationMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, NeedlerMobEffect> NEEDLER = EFFECTS.register("needler", () ->
        new NeedlerMobEffect(MobEffectCategory.BENEFICIAL, NeedlerMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, MobEffect> MUSCLE_OUT = EFFECTS.register("muscleout", () ->
        new SrpMobEffect(MobEffectCategory.HARMFUL, 0xEC7F82));
    public static final DeferredHolder<MobEffect, EffectPosMobEffect> EFFECT_POS = EFFECTS.register("effectpos", () ->
        new EffectPosMobEffect(MobEffectCategory.HARMFUL, EffectPosMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, EffectNegMobEffect> EFFECT_NEG = EFFECTS.register("effectneg", () ->
        new EffectNegMobEffect(MobEffectCategory.HARMFUL, EffectNegMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, ThornshadeThornsMobEffect> THORNSHADE_THORNS = EFFECTS.register("thornshade_thorns", () ->
        new ThornshadeThornsMobEffect(MobEffectCategory.BENEFICIAL, ThornshadeThornsMobEffect.LEGACY_COLOR));
    public static final DeferredHolder<MobEffect, TheSignMobEffect> THE_SIGN = EFFECTS.register("the_sign", () ->
        new TheSignMobEffect(MobEffectCategory.BENEFICIAL, TheSignMobEffect.LEGACY_COLOR));

    private ModEffects() {
    }

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
