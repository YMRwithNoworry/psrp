package com.dhanantry.scapeandrunparasites.util.config;

import java.util.Locale;
import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class SrpConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue WEAPON_LIVING_DURABILITY;
    public static final ModConfigSpec.IntValue WEAPON_LIVING_SENTIENT_HP_NEEDED;
    public static final ModConfigSpec.IntValue WEAPON_LIVING_SENTIENT_DAMAGE_NEEDED;
    public static final ModConfigSpec.BooleanValue WEAPON_CANCEL_PACKET;

    public static final ModConfigSpec.IntValue WEAPON_SCYTHE_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_SCYTHE_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_SCYTHE_SENTIENT_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_SCYTHE_SENTIENT_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_AXE_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_AXE_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_AXE_SENTIENT_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_AXE_SENTIENT_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_SWORD_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_SWORD_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_SWORD_SENTIENT_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_SWORD_SENTIENT_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_CLEAVER_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_CLEAVER_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_CLEAVER_SENTIENT_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_CLEAVER_SENTIENT_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_MAUL_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_MAUL_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_MAUL_SENTIENT_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_MAUL_SENTIENT_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_LANCE_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_LANCE_RANGE;
    public static final ModConfigSpec.IntValue WEAPON_LANCE_SENTIENT_DAMAGE;
    public static final ModConfigSpec.DoubleValue WEAPON_LANCE_SENTIENT_RANGE;

    public static final ModConfigSpec.IntValue WEAPON_BOW_DAMAGE;
    public static final ModConfigSpec.IntValue WEAPON_BOW_BONUS;
    public static final ModConfigSpec.IntValue WEAPON_BOW_DAMAGE_CAP;
    public static final ModConfigSpec.IntValue WEAPON_BOW_SENTIENT_DAMAGE;
    public static final ModConfigSpec.IntValue WEAPON_BOW_SENTIENT_BONUS;
    public static final ModConfigSpec.IntValue WEAPON_BOW_SENTIENT_DAMAGE_CAP;

    public static final ModConfigSpec.DoubleValue LIVING_POINT_REDUCTION;
    public static final ModConfigSpec.IntValue LIVING_DAMAGE_CAP;
    public static final ModConfigSpec.IntValue LIVING_POINT_CAP;
    public static final ModConfigSpec.DoubleValue SENTIENT_POINT_REDUCTION;
    public static final ModConfigSpec.IntValue SENTIENT_DAMAGE_CAP;
    public static final ModConfigSpec.IntValue SENTIENT_POINT_CAP;

    public static final ModConfigSpec.DoubleValue BLEEDING_DAMAGE;
    public static final ModConfigSpec.DoubleValue BLEEDING_DAMAGE_CAP;
    public static final ModConfigSpec.BooleanValue VIRAL_ENABLE;
    public static final ModConfigSpec.DoubleValue VIRAL_AMOUNT;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> STACKABLE_POTIONS_LIMIT;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.push("tools");
        WEAPON_LIVING_DURABILITY = BUILDER.comment("Legacy SRPConfig: Living Weapons Durability.").defineInRange("weapon_living_durability", 1000, 1, 10000);
        WEAPON_LIVING_SENTIENT_HP_NEEDED = BUILDER.comment("Legacy SRPConfig: Living Weapons HP Evolve.").defineInRange("weapon_livingSentient_HP_needed", 50000, 0, Integer.MAX_VALUE);
        WEAPON_LIVING_SENTIENT_DAMAGE_NEEDED = BUILDER.comment("Legacy SRPConfig: Living armor damage required to evolve.").defineInRange("weapon_livingSentient_DAMAGE_needed", 90000, 0, Integer.MAX_VALUE);
        WEAPON_CANCEL_PACKET = BUILDER.comment("Legacy SRPConfig: Weapon Packet Cancel; when true, living weapon reach is exposed as an attribute modifier.").define("weaponCancelPacket", false);

        WEAPON_SCYTHE_DAMAGE = damage("weapon_scythe_damage", 17);
        WEAPON_SCYTHE_RANGE = range("weapon_scythe_range", 4.0D);
        WEAPON_SCYTHE_SENTIENT_DAMAGE = damage("weapon_scythe_S_damage", 34);
        WEAPON_SCYTHE_SENTIENT_RANGE = range("weapon_scythe_S_range", 5.0D);
        WEAPON_AXE_DAMAGE = damage("weapon_axe_damage", 16);
        WEAPON_AXE_RANGE = range("weapon_axe_range", 4.0D);
        WEAPON_AXE_SENTIENT_DAMAGE = damage("weapon_axe_S_damage", 32);
        WEAPON_AXE_SENTIENT_RANGE = range("weapon_axe_S_range", 5.0D);
        WEAPON_SWORD_DAMAGE = damage("weapon_sword_damage", 19);
        WEAPON_SWORD_RANGE = range("weapon_sword_range", 4.5D);
        WEAPON_SWORD_SENTIENT_DAMAGE = damage("weapon_sword_S_damage", 38);
        WEAPON_SWORD_SENTIENT_RANGE = range("weapon_sword_S_range", 6.0D);
        WEAPON_CLEAVER_DAMAGE = damage("weapon_cleaver_damage", 18);
        WEAPON_CLEAVER_RANGE = range("weapon_cleaver_range", 4.0D);
        WEAPON_CLEAVER_SENTIENT_DAMAGE = damage("weapon_cleaver_S_damage", 36);
        WEAPON_CLEAVER_SENTIENT_RANGE = range("weapon_cleaver_S_range", 5.0D);
        WEAPON_MAUL_DAMAGE = damage("weapon_maul_damage", 20);
        WEAPON_MAUL_RANGE = range("weapon_maul_range", 4.0D);
        WEAPON_MAUL_SENTIENT_DAMAGE = damage("weapon_maul_S_damage", 40);
        WEAPON_MAUL_SENTIENT_RANGE = range("weapon_maul_S_range", 5.0D);
        WEAPON_LANCE_DAMAGE = damage("weapon_lance_damage", 15);
        WEAPON_LANCE_RANGE = range("weapon_lance_range", 5.0D);
        WEAPON_LANCE_SENTIENT_DAMAGE = damage("weapon_lance_S_damage", 30);
        WEAPON_LANCE_SENTIENT_RANGE = range("weapon_lance_S_range", 7.0D);
        WEAPON_BOW_DAMAGE = damage("weapon_bow_damage", 1);
        WEAPON_BOW_BONUS = damage("weapon_bow_bonus", 1);
        WEAPON_BOW_DAMAGE_CAP = damage("weapon_bow_damageCap", 2);
        WEAPON_BOW_SENTIENT_DAMAGE = damage("weapon_bow_sentient_damage", 1);
        WEAPON_BOW_SENTIENT_BONUS = damage("weapon_bow_sentient_bonus", 1);
        WEAPON_BOW_SENTIENT_DAMAGE_CAP = damage("weapon_bow_sentient_damageCap", 2);

        LIVING_POINT_REDUCTION = BUILDER.defineInRange("livingPointReduction", 0.0125D, 0.0D, 1000.0D);
        LIVING_DAMAGE_CAP = BUILDER.defineInRange("livingDamageCap", 4, 0, 1024);
        LIVING_POINT_CAP = BUILDER.defineInRange("livingPointCap", 18, 0, 1024);
        SENTIENT_POINT_REDUCTION = BUILDER.defineInRange("sentientPointReduction", 0.018D, 0.0D, 1000.0D);
        SENTIENT_DAMAGE_CAP = BUILDER.defineInRange("sentientDamageCap", 7, 0, 1024);
        SENTIENT_POINT_CAP = BUILDER.defineInRange("sentientPointCap", 13, 0, 1024);
        BUILDER.pop();

        BUILDER.push("status_effects");
        BLEEDING_DAMAGE = BUILDER.comment("Legacy SRPConfigSystems: Bleeding Damage. Damage fraction of the victim's max health.")
            .defineInRange("bleedingDamage", 0.06D, 0.0D, 1.0D);
        BLEEDING_DAMAGE_CAP = BUILDER.comment("Legacy SRPConfigSystems: Bleeding Damage Limit.")
            .defineInRange("bleedingDamageCap", 100.0D, 0.0D, 1000.0D);
        VIRAL_ENABLE = BUILDER.comment("Legacy SRPConfigSystems: enable Viral damage amplification.")
            .define("viralEnable", true);
        VIRAL_AMOUNT = BUILDER.comment("Legacy SRPConfigSystems: Viral Amount. Extra incoming damage per amplifier level.")
            .defineInRange("viralAmount", 0.5D, 0.0D, 1000.0D);
        STACKABLE_POTIONS_LIMIT = BUILDER.comment("Legacy SRPConfig: Limit Potion Amplifiers entries formatted as namespace:effect;limit.")
            .defineListAllowEmpty("stackablePotionsLimit", List.of(), value -> value instanceof String);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private SrpConfig() {
    }

    public static int weaponDamage(String legacyKey, boolean sentient) {
        return switch (legacyKey.toLowerCase(Locale.ROOT)) {
            case "scythe" -> sentient ? WEAPON_SCYTHE_SENTIENT_DAMAGE.get() : WEAPON_SCYTHE_DAMAGE.get();
            case "axe" -> sentient ? WEAPON_AXE_SENTIENT_DAMAGE.get() : WEAPON_AXE_DAMAGE.get();
            case "sword" -> sentient ? WEAPON_SWORD_SENTIENT_DAMAGE.get() : WEAPON_SWORD_DAMAGE.get();
            case "cleaver" -> sentient ? WEAPON_CLEAVER_SENTIENT_DAMAGE.get() : WEAPON_CLEAVER_DAMAGE.get();
            case "maul" -> sentient ? WEAPON_MAUL_SENTIENT_DAMAGE.get() : WEAPON_MAUL_DAMAGE.get();
            case "lance" -> sentient ? WEAPON_LANCE_SENTIENT_DAMAGE.get() : WEAPON_LANCE_DAMAGE.get();
            default -> 1;
        };
    }

    public static double weaponReach(String legacyKey, boolean sentient) {
        return switch (legacyKey.toLowerCase(Locale.ROOT)) {
            case "scythe" -> sentient ? WEAPON_SCYTHE_SENTIENT_RANGE.get() : WEAPON_SCYTHE_RANGE.get();
            case "axe" -> sentient ? WEAPON_AXE_SENTIENT_RANGE.get() : WEAPON_AXE_RANGE.get();
            case "sword" -> sentient ? WEAPON_SWORD_SENTIENT_RANGE.get() : WEAPON_SWORD_RANGE.get();
            case "cleaver" -> sentient ? WEAPON_CLEAVER_SENTIENT_RANGE.get() : WEAPON_CLEAVER_RANGE.get();
            case "maul" -> sentient ? WEAPON_MAUL_SENTIENT_RANGE.get() : WEAPON_MAUL_RANGE.get();
            case "lance" -> sentient ? WEAPON_LANCE_SENTIENT_RANGE.get() : WEAPON_LANCE_RANGE.get();
            default -> 0.0D;
        };
    }

    private static ModConfigSpec.IntValue damage(String key, int defaultValue) {
        return BUILDER.defineInRange(key, defaultValue, 0, 1024);
    }

    private static ModConfigSpec.DoubleValue range(String key, double defaultValue) {
        return BUILDER.defineInRange(key, defaultValue, 0.0D, 64.0D);
    }
}
