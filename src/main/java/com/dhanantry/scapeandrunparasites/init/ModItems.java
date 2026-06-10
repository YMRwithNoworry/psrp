package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.item.LivingArmorItem;
import com.dhanantry.scapeandrunparasites.item.LivingBowItem;
import com.dhanantry.scapeandrunparasites.item.LivingMeleeWeaponItem;
import com.dhanantry.scapeandrunparasites.item.TheSignCharmItem;
import com.dhanantry.scapeandrunparasites.item.ThornshadeDecanterItem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SRPMain.MODID);
    public static final List<DeferredItem<? extends Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final DeferredItem<Item> ASSIMILATED_FLESH = basic("assimilated_flesh", 64);
    public static final DeferredItem<Item> BLOODY_BONE = basic("bloody_bone", 64);
    public static final DeferredItem<Item> BLOODY_ROD = basic("bloody_rod", 64);
    public static final DeferredItem<Item> BLOODY_IRON_INGOT = basic("bloody_iron_ingot", 64);
    public static final DeferredItem<Item> LIVING_CORE = basic("living_core", 1);
    public static final DeferredItem<Item> ORGAN_SYNTH = basic("organ_synth", 1);
    public static final DeferredItem<Item> HIVE_SCRAP = basic("hive_scrap", 64);
    public static final DeferredItem<Item> HIJACKED_DROP = basic("hijacked_drop", 64);
    public static final DeferredItem<Item> BECKON_DROP = basic("beckon_drop", 64);
    public static final DeferredItem<Item> DISPATCHER_DROP = basic("dispatcher_drop", 64);
    public static final DeferredItem<Item> ADA_ARACHNIDA_DROP = basic("ada_arachnida_drop", 64);
    public static final DeferredItem<Item> ADA_BOLSTER_DROP = basic("ada_bolster_drop", 64);
    public static final DeferredItem<Item> ADA_DEVOURER_DROP = basic("ada_devourer_drop", 64);
    public static final DeferredItem<Item> ADA_LONGARMS_DROP = basic("ada_longarms_drop", 64);
    public static final DeferredItem<Item> ADA_MANDUCATER_DROP = basic("ada_manducater_drop", 64);
    public static final DeferredItem<Item> ADA_REEKER_DROP = basic("ada_reeker_drop", 64);
    public static final DeferredItem<Item> ADA_SUMMONER_DROP = basic("ada_summoner_drop", 64);
    public static final DeferredItem<Item> ADA_VERMIN_DROP = basic("ada_vermin_drop", 64);
    public static final DeferredItem<Item> ADA_VISCERA_DROP = basic("ada_viscera_drop", 64);
    public static final DeferredItem<Item> ADA_YELLOWEYE_DROP = basic("ada_yelloweye_drop", 64);
    public static final DeferredItem<TheSignCharmItem> THE_SIGN_CHARM = register("the_sign_charm", () ->
        new TheSignCharmItem(new Item.Properties().stacksTo(TheSignCharmItem.LEGACY_STACK_SIZE)));
    public static final DeferredItem<ThornshadeDecanterItem> THORNSHADE_DECANTER = register("thornshade_decanter", () ->
        new ThornshadeDecanterItem(new Item.Properties().stacksTo(ThornshadeDecanterItem.LEGACY_STACK_SIZE)));

    public static final DeferredItem<SwordItem> HIJACKED_IRON_SWORD = register("hijacked_iron_sword", () ->
        new SwordItem(SrpToolTiers.HIJACKED_IRON, new Item.Properties().attributes(SwordItem.createAttributes(SrpToolTiers.HIJACKED_IRON, 3, -2.4F))));
    public static final DeferredItem<AxeItem> HIJACKED_IRON_AXE = register("hijacked_iron_axe", () ->
        new AxeItem(SrpToolTiers.HIJACKED_IRON, new Item.Properties().attributes(DiggerItem.createAttributes(SrpToolTiers.HIJACKED_IRON, 6.0F, -3.1F))));
    public static final DeferredItem<ShovelItem> HIJACKED_IRON_SHOVEL = register("hijacked_iron_shovel", () ->
        new ShovelItem(SrpToolTiers.HIJACKED_IRON, new Item.Properties().attributes(DiggerItem.createAttributes(SrpToolTiers.HIJACKED_IRON, 1.5F, -3.0F))));
    public static final DeferredItem<HoeItem> HIJACKED_IRON_HOE = register("hijacked_iron_hoe", () ->
        new HoeItem(SrpToolTiers.HIJACKED_IRON, new Item.Properties().attributes(DiggerItem.createAttributes(SrpToolTiers.HIJACKED_IRON, -2.0F, -1.0F))));
    public static final DeferredItem<PickaxeItem> HIJACKED_IRON_PICKAXE = register("hijacked_iron_pickaxe", () ->
        new PickaxeItem(SrpToolTiers.HIJACKED_IRON, new Item.Properties().attributes(DiggerItem.createAttributes(SrpToolTiers.HIJACKED_IRON, 1.0F, -2.8F))));

    public static final DeferredItem<ArmorItem> HIJACKED_IRON_HELMET = register("hijacked_iron_helmet", () ->
        new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<ArmorItem> HIJACKED_IRON_CHESTPIECE = register("hijacked_iron_chestpiece", () ->
        new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<ArmorItem> HIJACKED_IRON_LEGGINGS = register("hijacked_iron_leggings", () ->
        new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<ArmorItem> HIJACKED_IRON_BOOTS = register("hijacked_iron_boots", () ->
        new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_SCYTHE = livingWeapon("weapon_scythe", "scythe", false, -3.1F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_SCYTHE_SENTIENT = livingWeapon("weapon_scythe_sentient", "scythe", true, -3.3F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_AXE = livingWeapon("weapon_axe", "axe", false, -3.1F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_AXE_SENTIENT = livingWeapon("weapon_axe_sentient", "axe", true, -3.3F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_SWORD = livingWeapon("weapon_sword", "sword", false, -3.1F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_SWORD_SENTIENT = livingWeapon("weapon_sword_sentient", "sword", true, -3.3F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_CLEAVER = livingWeapon("weapon_cleaver", "cleaver", false, -3.1F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_CLEAVER_SENTIENT = livingWeapon("weapon_cleaver_sentient", "cleaver", true, -3.3F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_MAUL = livingWeapon("weapon_maul", "maul", false, -3.1F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_MAUL_SENTIENT = livingWeapon("weapon_maul_sentient", "maul", true, -3.3F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_LANCE = livingWeapon("weapon_lance", "lance", false, -3.1F);
    public static final DeferredItem<LivingMeleeWeaponItem> WEAPON_LANCE_SENTIENT = livingWeapon("weapon_lance_sentient", "lance", true, -3.3F);

    public static final DeferredItem<LivingBowItem> WEAPON_BOW = register("weapon_bow", () -> new LivingBowItem(false, new Item.Properties().durability(1000)));
    public static final DeferredItem<LivingBowItem> WEAPON_BOW_SENTIENT = register("weapon_bow_sentient", () -> new LivingBowItem(true, new Item.Properties().durability(1000)));

    public static final DeferredItem<LivingArmorItem> ARMOR_HELM = livingArmor("armor_helm", false, ArmorItem.Type.HELMET);
    public static final DeferredItem<LivingArmorItem> ARMOR_CHEST = livingArmor("armor_chest", false, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<LivingArmorItem> ARMOR_PANTS = livingArmor("armor_pants", false, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<LivingArmorItem> ARMOR_BOOTS = livingArmor("armor_boots", false, ArmorItem.Type.BOOTS);
    public static final DeferredItem<LivingArmorItem> ARMOR_HELM_SENTIENT = livingArmor("armor_helm_sentient", true, ArmorItem.Type.HELMET);
    public static final DeferredItem<LivingArmorItem> ARMOR_CHEST_SENTIENT = livingArmor("armor_chest_sentient", true, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<LivingArmorItem> ARMOR_PANTS_SENTIENT = livingArmor("armor_pants_sentient", true, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<LivingArmorItem> ARMOR_BOOTS_SENTIENT = livingArmor("armor_boots_sentient", true, ArmorItem.Type.BOOTS);
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_FLOG = register("itemmobspawner_flog", () ->
        new DeferredSpawnEggItem(ModEntities.GRUNT, 0x2A1714, 0xA62A2A, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_KIRIN = register("itemmobspawner_kirin", () ->
        new DeferredSpawnEggItem(ModEntities.KIRIN, 0x2B111E, 0x3157A5, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_ORCH = register("itemmobspawner_orch", () ->
        new DeferredSpawnEggItem(ModEntities.MONARCH, 0x313517, 0x313517, new Item.Properties()));

    // Legacy baseline items (from original SRPItems bytecode audit)
    public static final DeferredItem<Item> BOOK_OF_VENGEANCE = legacyItem("book_of_vengeance", 1);
    public static final DeferredItem<Item> THORNSHADE_BERRY = legacyItem("thornshade_berry", 64);
    public static final DeferredItem<Item> SRP_FIELD_GUIDE = legacyItem("srp_field_guide", 1);
    public static final DeferredItem<Item> PEARL = legacyItem("pearl", 64);
    public static final DeferredItem<Item> PHASE_REPORT = legacyItem("phase_report", 1);
    public static final DeferredItem<Item> VECTOR_MAP = legacyItem("vector_map", 1);
    public static final DeferredItem<Item> ITEMTAB = legacyItem("itemtab", 1);
    public static final DeferredItem<Item> ITEMASSIMILATE = legacyItem("itemassimilate", 1);
    public static final DeferredItem<Item> ITEMEVOLVE = legacyItem("itemevolve", 1);
    public static final DeferredItem<Item> ITEMDEVOLVE = legacyItem("itemdevolve", 1);
    public static final DeferredItem<Item> ITEMVARIANT = legacyItem("itemvariant", 1);
    public static final DeferredItem<Item> DRIED_TENDONS = legacyItem("dried_tendons", 1);
    public static final DeferredItem<Item> INFECTIOUS_BLADE_FRAGMENT = legacyItem("infectious_blade_fragment", 1);
    public static final DeferredItem<Item> VILE_SHELL = legacyItem("vile_shell", 1);
    public static final DeferredItem<Item> HARDENED_BONE_HANDLE = legacyItem("hardened_bone_handle", 1);
    public static final DeferredItem<Item> BONE = legacyItem("bone", 16);
    public static final DeferredItem<Item> SEMIORGANIC_INGOT = legacyItem("semiorganic_ingot", 64);
    public static final DeferredItem<Item> TISSUE_SPIKE = legacyItem("tissue_spike", 1);
    public static final DeferredItem<Item> ALVEOLAR_FLUID = legacyItem("alveolar_fluid", 1);
    public static final DeferredItem<Item> DEADBLOOD_FLUID = legacyItem("deadblood_fluid", 1);
    public static final DeferredItem<Item> ALVEOLIGROWTH = legacyItem("alveoligrowth", 64);
    public static final DeferredItem<Item> INFESTED_BONEMEAL = legacyItem("infested_bonemeal", 1);
    public static final DeferredItem<Item> BOUGH = legacyItem("bough", 1);
    public static final DeferredItem<Item> GREEK_FIRE = legacyItem("greek_fire", 16);
    public static final DeferredItem<Item> FALSE_APPLE = legacyItem("false_apple", 1);
    public static final DeferredItem<Item> FISHLIN = legacyItem("fishlin", 1);
    public static final DeferredItem<Item> SHRIMP = legacyItem("shrimp", 1);
    public static final DeferredItem<Item> VENKROL_BOOTS = legacyItem("venkrol_boots", 1);
    public static final DeferredItem<Item> ITEMTHROW = legacyItem("itemthrow", 16);
    public static final DeferredItem<Item> EVCLOCK = legacyItem("evclock", 1);
    public static final DeferredItem<Item> LEVELCLOCK = legacyItem("levelclock", 1);
    public static final DeferredItem<Item> NODECOMPASS = legacyItem("nodecompass", 1);
    public static final DeferredItem<Item> COLONYCOMPASS = legacyItem("colonycompass", 1);
    public static final DeferredItem<Item> ORIGINCOMPASS = legacyItem("origincompass", 1);
    public static final DeferredItem<Item> DISLODGEMENT_REPORT = legacyItem("dislodgement_report", 1);
    public static final DeferredItem<Item> DISCTHREE = legacyItem("discthree", 1);
    public static final DeferredItem<Item> DISCONE = legacyItem("discone", 1);
    public static final DeferredItem<Item> DISCTWO = legacyItem("disctwo", 1);
    public static final DeferredItem<Item> DISCFOUR = legacyItem("discfour", 1);
    public static final DeferredItem<Item> DISCFIVE = legacyItem("discfive", 1);
    public static final DeferredItem<Item> DISCSIX = legacyItem("discsix", 1);
    public static final DeferredItem<Item> LURECOMPONENT1 = legacyItem("lurecomponent1", 16);
    public static final DeferredItem<Item> LURECOMPONENT2 = legacyItem("lurecomponent2", 16);
    public static final DeferredItem<Item> LURECOMPONENT3 = legacyItem("lurecomponent3", 16);
    public static final DeferredItem<Item> LURECOMPONENT4 = legacyItem("lurecomponent4", 16);
    public static final DeferredItem<Item> LURECOMPONENT5 = legacyItem("lurecomponent5", 16);
    public static final DeferredItem<Item> LURECOMPONENT6 = legacyItem("lurecomponent6", 16);
    public static final DeferredItem<Item> LURECOMPONENT7 = legacyItem("lurecomponent7", 16);
    public static final DeferredItem<Item> LURECOMPONENT8 = legacyItem("lurecomponent8", 16);
    public static final DeferredItem<Item> LURECOMPONENT9 = legacyItem("lurecomponent9", 16);
    public static final DeferredItem<Item> LURECOMPONENT10 = legacyItem("lurecomponent10", 16);
    public static final DeferredItem<Item> MODULE_BASE = legacyItem("module_base", 1);
    public static final DeferredItem<Item> MODULE_INBORN = legacyItem("module_inborn", 1);
    public static final DeferredItem<Item> MODULE_ASSIMILATED = legacyItem("module_assimilated", 1);
    public static final DeferredItem<Item> MODULE_HIJACKED = legacyItem("module_hijacked", 1);
    public static final DeferredItem<Item> MODULE_FERAL = legacyItem("module_feral", 1);
    public static final DeferredItem<Item> MODULE_CRUDE = legacyItem("module_crude", 1);
    public static final DeferredItem<Item> MODULE_PRIMITIVE = legacyItem("module_primitive", 1);
    public static final DeferredItem<Item> MODULE_ADAPTED = legacyItem("module_adapted", 1);
    public static final DeferredItem<Item> MODULE_NEXUS = legacyItem("module_nexus", 1);
    public static final DeferredItem<Item> MODULE_DETERRENT = legacyItem("module_deterrent", 1);
    public static final DeferredItem<Item> MODULE_PURE = legacyItem("module_pure", 1);
    public static final DeferredItem<Item> MODULE_PREEMINENT = legacyItem("module_preeminent", 1);
    public static final DeferredItem<Item> MODULE_ANCIENT = legacyItem("module_ancient", 1);
    public static final DeferredItem<Item> MODULE_DESMOID = legacyItem("module_desmoid", 1);
    public static final DeferredItem<Item> MODULE_ESCHAR = legacyItem("module_eschar", 1);
    public static final DeferredItem<Item> MODULE_RESISTANCE = legacyItem("module_resistance", 1);
    public static final DeferredItem<Item> MODULE_IDEAL = legacyItem("module_ideal", 1);
    public static final DeferredItem<Item> MODULE_DISLODGEMENT = legacyItem("module_dislodgement", 1);
    public static final DeferredItem<Item> MODULE_ORIGIN = legacyItem("module_origin", 1);
    public static final DeferredItem<Item> MODULE_ASSIMARA = legacyItem("module_assimara", 1);
    public static final DeferredItem<Item> MODULE_DERIVED = legacyItem("module_derived", 1);
    public static final DeferredItem<Item> MODULE_VECTORS = legacyItem("module_vectors", 1);
    public static final DeferredItem<Item> MODULE_PHASE = legacyItem("module_phase", 1);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_DORPA = legacyItem("itemmobspawner_dorpa", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFSQUID = legacyItem("itemmobspawner_infsquid", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFBEAR = legacyItem("itemmobspawner_infbear", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFHUMAN = legacyItem("itemmobspawner_infhuman", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFHUMANHEAD = legacyItem("itemmobspawner_infhumanhead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFENDERMAN = legacyItem("itemmobspawner_infenderman", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFENDERMANHEAD = legacyItem("itemmobspawner_infendermanhead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFCOW = legacyItem("itemmobspawner_infcow", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFCOWHEAD = legacyItem("itemmobspawner_infcowhead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFSHEEP = legacyItem("itemmobspawner_infsheep", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFSHEEPHEAD = legacyItem("itemmobspawner_infsheephead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFWOLF = legacyItem("itemmobspawner_infwolf", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFWOLFHEAD = legacyItem("itemmobspawner_infwolfhead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFPIG = legacyItem("itemmobspawner_infpig", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFPIGHEAD = legacyItem("itemmobspawner_infpighead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFVILLAGER = legacyItem("itemmobspawner_infvillager", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFVILLAGERHEAD = legacyItem("itemmobspawner_infvillagerhead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INHOOS = legacyItem("itemmobspawner_inhoos", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INHOOM = legacyItem("itemmobspawner_inhoom", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFHORSE = legacyItem("itemmobspawner_infhorse", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFHORSEHEAD = legacyItem("itemmobspawner_infhorsehead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFPLAYER = legacyItem("itemmobspawner_infplayer", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFPLAYERHEAD = legacyItem("itemmobspawner_infplayerhead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFDRAGONE = legacyItem("itemmobspawner_infdragone", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_INFDRAGONEHEAD = legacyItem("itemmobspawner_infdragonehead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_QUAC = legacyItem("itemmobspawner_quac", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LEER = legacyItem("itemmobspawner_leer", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERBEAR = legacyItem("itemmobspawner_ferbear", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERHUMAN = legacyItem("itemmobspawner_ferhuman", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERCOW = legacyItem("itemmobspawner_fercow", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERENDERMAN = legacyItem("itemmobspawner_ferenderman", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERHORSE = legacyItem("itemmobspawner_ferhorse", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERPIG = legacyItem("itemmobspawner_ferpig", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERSHEEP = legacyItem("itemmobspawner_fersheep", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERVILLAGER = legacyItem("itemmobspawner_fervillager", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_FERWOLF = legacyItem("itemmobspawner_ferwolf", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MARCOW = legacyItem("itemmobspawner_marcow", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MARENDERMAN = legacyItem("itemmobspawner_marenderman", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MARVILLAGER = legacyItem("itemmobspawner_marvillager", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MARHUMAN = legacyItem("itemmobspawner_marhuman", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MARSHEEP = legacyItem("itemmobspawner_marsheep", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MARBEAR = legacyItem("itemmobspawner_marbear", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HIGOLEM = legacyItem("itemmobspawner_higolem", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HIBLAZE = legacyItem("itemmobspawner_hiblaze", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HISKELETON = legacyItem("itemmobspawner_hiskeleton", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HOST = legacyItem("itemmobspawner_host", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HOSTII = legacyItem("itemmobspawner_hostii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HEED = legacyItem("itemmobspawner_heed", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_CRUXA = legacyItem("itemmobspawner_cruxa", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MES = legacyItem("itemmobspawner_mes", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LESH = legacyItem("itemmobspawner_lesh", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_DONE = legacyItem("itemmobspawner_done", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_CRUXB = legacyItem("itemmobspawner_cruxb", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ABOBODIES = legacyItem("itemmobspawner_abobodies", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ABOHEAD = legacyItem("itemmobspawner_abohead", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_SHYCO = legacyItem("itemmobspawner_shyco", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_CANRA = legacyItem("itemmobspawner_canra", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_NOGLA = legacyItem("itemmobspawner_nogla", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HULL = legacyItem("itemmobspawner_hull", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_EMANA = legacyItem("itemmobspawner_emana", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_BANO = legacyItem("itemmobspawner_bano", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_WYMO = legacyItem("itemmobspawner_wymo", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_IKI = legacyItem("itemmobspawner_iki", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_RANRAC = legacyItem("itemmobspawner_ranrac", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LUM = legacyItem("itemmobspawner_lum", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_GIM = legacyItem("itemmobspawner_gim", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ZAA = legacyItem("itemmobspawner_zaa", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_SHYCOADAPTED = legacyItem("itemmobspawner_shycoadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_CANRAADAPTED = legacyItem("itemmobspawner_canraadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_NOGLAADAPTED = legacyItem("itemmobspawner_noglaadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HULLADAPTED = legacyItem("itemmobspawner_hulladapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_EMANAADAPTED = legacyItem("itemmobspawner_emanaadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_BANOADAPTED = legacyItem("itemmobspawner_banoadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_WYMOADAPTED = legacyItem("itemmobspawner_wymoadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_IKIADAPTED = legacyItem("itemmobspawner_ikiadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_RANRACADAPTED = legacyItem("itemmobspawner_ranracadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LUMADAPTED = legacyItem("itemmobspawner_lumadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_GIMADAPTED = legacyItem("itemmobspawner_gimadapted", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ZAAADAPTED = legacyItem("itemmobspawner_zaaadapted", 64);
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_LODO = register("itemmobspawner_lodo", () ->
        new DeferredSpawnEggItem(ModEntities.LODO, 0x7A4137, 0x2A1815, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_MUDO = register("itemmobspawner_mudo", () ->
        new DeferredSpawnEggItem(ModEntities.MUDO, 0x5A261D, 0x3D6B39, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_NUUH = register("itemmobspawner_nuuh", () ->
        new DeferredSpawnEggItem(ModEntities.NUUH, 0x3A201B, 0x9B3B2E, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_ATA = register("itemmobspawner_ata", () ->
        new DeferredSpawnEggItem(ModEntities.ATA, 0x271915, 0x6E8A2B, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_RATHOL = register("itemmobspawner_rathol", () ->
        new DeferredSpawnEggItem(ModEntities.RATHOL, 0x3D2C24, 0x7E6A47, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_GOTHOL = register("itemmobspawner_gothol", () ->
        new DeferredSpawnEggItem(ModEntities.GOTHOL, 0x352820, 0x6B4B34, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_BUTHOL = register("itemmobspawner_buthol", () ->
        new DeferredSpawnEggItem(ModEntities.BUTHOL, 0x362B22, 0x8B7B40, new Item.Properties()));
    public static final DeferredItem<Item> ITEMMOBSPAWNER_VENKROL = legacyItem("itemmobspawner_venkrol", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_VENKROLSII = legacyItem("itemmobspawner_venkrolsii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_VENKROLSIII = legacyItem("itemmobspawner_venkrolsiii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_VENKROLSIV = legacyItem("itemmobspawner_venkrolsiv", 64);
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_TONRO = register("itemmobspawner_tonro", () ->
        new DeferredSpawnEggItem(ModEntities.TONRO, 0x3B231B, 0x786044, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_UNVO = register("itemmobspawner_unvo", () ->
        new DeferredSpawnEggItem(ModEntities.UNVO, 0x273223, 0x7C8B57, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_NAK = register("itemmobspawner_nak", () ->
        new DeferredSpawnEggItem(ModEntities.NAK, 0x352A23, 0x6D3B30, new Item.Properties()));
    public static final DeferredItem<Item> ITEMMOBSPAWNER_DOD = legacyItem("itemmobspawner_dod", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_DODSII = legacyItem("itemmobspawner_dodsii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_DODSIII = legacyItem("itemmobspawner_dodsiii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_DODSIV = legacyItem("itemmobspawner_dodsiv", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LEEM = legacyItem("itemmobspawner_leem", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LEEMSII = legacyItem("itemmobspawner_leemsii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LEEMSIII = legacyItem("itemmobspawner_leemsiii", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LEEMSIV = legacyItem("itemmobspawner_leemsiv", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ALAFHA = legacyItem("itemmobspawner_alafha", 64);
    public static final DeferredItem<DeferredSpawnEggItem> ITEMMOBSPAWNER_GANRO = register("itemmobspawner_ganro", () ->
        new DeferredSpawnEggItem(ModEntities.GANRO, 0x2F241C, 0x8A6A3D, new Item.Properties()));
    public static final DeferredItem<Item> ITEMMOBSPAWNER_OMBOO = legacyItem("itemmobspawner_omboo", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ESOR = legacyItem("itemmobspawner_esor", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ANGED = legacyItem("itemmobspawner_anged", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_JINJO = legacyItem("itemmobspawner_jinjo", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_VESTA = legacyItem("itemmobspawner_vesta", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_PHEON = legacyItem("itemmobspawner_pheon", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_LENCIA = legacyItem("itemmobspawner_lencia", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ELVIA = legacyItem("itemmobspawner_elvia", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_HEBLU = legacyItem("itemmobspawner_heblu", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_ORONCO = legacyItem("itemmobspawner_oronco", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_TERLA = legacyItem("itemmobspawner_terla", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_POD = legacyItem("itemmobspawner_pod", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_MAR = legacyItem("itemmobspawner_mar", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_SOO = legacyItem("itemmobspawner_soo", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_TENN = legacyItem("itemmobspawner_tenn", 64);
    public static final DeferredItem<Item> ITEMMOBSPAWNER_VENKROLSV = legacyItem("itemmobspawner_venkrolsv", 64);

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static DeferredItem<Item> basic(String name, int stackSize) {
        return register(name, () -> new Item(new Item.Properties().stacksTo(stackSize)));
    }

    private static DeferredItem<Item> legacyItem(String name, int stackSize) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(stackSize)));
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<LivingMeleeWeaponItem> livingWeapon(String registryName, String legacyKey, boolean sentient, float attackSpeed) {
        return register(registryName, () -> new LivingMeleeWeaponItem(legacyKey, sentient, attackSpeed, new Item.Properties()
            .durability(1000)
            .attributes(SwordItem.createAttributes(SrpToolTiers.LIVING, LivingMeleeWeaponItem.defaultDamage(legacyKey, sentient), attackSpeed))));
    }

    private static DeferredItem<LivingArmorItem> livingArmor(String registryName, boolean sentient, ArmorItem.Type type) {
        return register(registryName, () -> new LivingArmorItem(sentient ? SrpArmorMaterials.SENTIENT : SrpArmorMaterials.LIVING, type,
            new Item.Properties().durability(type.getDurability(sentient ? 42 : 33)), sentient));
    }

    private static <T extends Item> DeferredItem<T> register(String name, Supplier<T> supplier) {
        DeferredItem<T> item = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }
}
