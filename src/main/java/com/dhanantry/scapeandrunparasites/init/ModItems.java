package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.item.LivingArmorItem;
import com.dhanantry.scapeandrunparasites.item.LivingBowItem;
import com.dhanantry.scapeandrunparasites.item.LivingMeleeWeaponItem;
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

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static DeferredItem<Item> basic(String name, int stackSize) {
        return register(name, () -> new Item(new Item.Properties().stacksTo(stackSize)));
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
