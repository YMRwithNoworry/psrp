package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModPotions {
    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, SRPMain.MODID);

    public static final int LEGACY_DEFAULT_DURATION = 2400;
    public static final int LEGACY_THORNSHADE_THORNS_DURATION = 60;

    public static final DeferredHolder<Potion, Potion> FEAR = register("fear", "srparasites:fear", ModEffects.FEAR, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> ANTIMALL = register("res", "srparasites:antimall", ModEffects.ANTIMALL, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> CORROSIVE = register("corro", "srparasites:corrosive", ModEffects.CORROSIVE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> VIRAL = register("vira", "srparasites:viral", ModEffects.VIRAL, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> VOMIT = register("vomit", "srparasites:vomit", ModEffects.VOMIT, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> RAGE = register("rage", "srparasites:rage", ModEffects.RAGE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> REPEL = register("repel", "srparasites:repel", ModEffects.REPEL, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> SENSES = register("senses", "srparasites:senses", ModEffects.SENSES, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> DEBAR = register("debar", "srparasites:debar", ModEffects.DEBAR, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> LINK = register("link", "srparasites:link", ModEffects.LINK, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> PIVOT = register("pivot", "srparasites:pivot", ModEffects.PIVOT, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> JUGG = register("jugg", "srparasites:jugg", ModEffects.JUGG, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> PARATE = register("parate", "srparasites:parate", ModEffects.PARATE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> PRIMITIVE = register("primitive", "srparasites:primitive", ModEffects.PRIMITIVE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> ADAPTED = register("adapted", "srparasites:adapted", ModEffects.ADAPTED, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> PURE = register("pure", "srparasites:pure", ModEffects.PURE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> CRUDE = register("crude", "srparasites:crude", ModEffects.CRUDE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> FERAL = register("feral", "srparasites:feral", ModEffects.FERAL, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> NEXUS = register("nexus", "srparasites:nexus", ModEffects.NEXUS, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> BRAINING = register("braining", "srparasites:braining", ModEffects.BRAINING, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> NOVISION = register("novision", "srparasites:novision", ModEffects.NOVISION, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> INDEAF = register("indeaf", "srparasites:indeaf", ModEffects.INDEAF, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> OVERHEATING = register("overheating", "srparasites:overheating", ModEffects.OVERHEATING, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> CONTAMINATION = register("conta", "srparasites:conta", ModEffects.CONTAMINATION, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> MUSCLE_OUT = register("muscleout", "srparasites:muscleout", ModEffects.MUSCLE_OUT, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> EFFECT_POS = register("effectpos", "srparasites:effectpos", ModEffects.EFFECT_POS, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> EFFECT_NEG = register("effectneg", "srparasites:effectneg", ModEffects.EFFECT_NEG, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> THORNSHADE_THORNS = register("thornshade_thorns", "srparasites:thornshade_thorns", ModEffects.THORNSHADE_THORNS, LEGACY_THORNSHADE_THORNS_DURATION);
    public static final DeferredHolder<Potion, Potion> THE_SIGN = register("the_sign", "srparasites:the_sign", ModEffects.THE_SIGN, LEGACY_DEFAULT_DURATION);

    private ModPotions() {
    }

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    private static DeferredHolder<Potion, Potion> register(String id, String legacyName, Holder<MobEffect> effect, int duration) {
        return POTIONS.register(id, () -> new Potion(legacyName, new MobEffectInstance(effect, duration)));
    }
}
