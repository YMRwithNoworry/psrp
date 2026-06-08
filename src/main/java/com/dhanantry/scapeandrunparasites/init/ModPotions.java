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

    public static final DeferredHolder<Potion, Potion> CORROSIVE = register("corro", "srparasites:corrosive", ModEffects.CORROSIVE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> VIRAL = register("vira", "srparasites:viral", ModEffects.VIRAL, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> VOMIT = register("vomit", "srparasites:vomit", ModEffects.VOMIT, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> RAGE = register("rage", "srparasites:rage", ModEffects.RAGE, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> SENSES = register("senses", "srparasites:senses", ModEffects.SENSES, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> INDEAF = register("indeaf", "srparasites:indeaf", ModEffects.INDEAF, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> OVERHEATING = register("overheating", "srparasites:overheating", ModEffects.OVERHEATING, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> CONTAMINATION = register("conta", "srparasites:conta", ModEffects.CONTAMINATION, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> EFFECT_POS = register("effectpos", "srparasites:effectpos", ModEffects.EFFECT_POS, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> EFFECT_NEG = register("effectneg", "srparasites:effectneg", ModEffects.EFFECT_NEG, LEGACY_DEFAULT_DURATION);
    public static final DeferredHolder<Potion, Potion> THORNSHADE_THORNS = register("thornshade_thorns", "srparasites:thornshade_thorns", ModEffects.THORNSHADE_THORNS, LEGACY_THORNSHADE_THORNS_DURATION);

    private ModPotions() {
    }

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    private static DeferredHolder<Potion, Potion> register(String id, String legacyName, Holder<MobEffect> effect, int duration) {
        return POTIONS.register(id, () -> new Potion(legacyName, new MobEffectInstance(effect, duration)));
    }
}
