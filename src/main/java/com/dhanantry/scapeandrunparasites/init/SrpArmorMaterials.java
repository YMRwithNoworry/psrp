package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public final class SrpArmorMaterials {
    public static final Holder<ArmorMaterial> LIVING = Holder.direct(material("living", 18, 3.0F, 0.0F, Map.of(
        ArmorItem.Type.BOOTS, 3,
        ArmorItem.Type.LEGGINGS, 6,
        ArmorItem.Type.CHESTPLATE, 8,
        ArmorItem.Type.HELMET, 3
    )));

    public static final Holder<ArmorMaterial> SENTIENT = Holder.direct(material("sentient", 22, 4.0F, 0.05F, Map.of(
        ArmorItem.Type.BOOTS, 4,
        ArmorItem.Type.LEGGINGS, 7,
        ArmorItem.Type.CHESTPLATE, 9,
        ArmorItem.Type.HELMET, 4
    )));

    private SrpArmorMaterials() {
    }

    private static ArmorMaterial material(String name, int enchantment, float toughness, float knockbackResistance, Map<ArmorItem.Type, Integer> defenseValues) {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.putAll(defenseValues);
        return new ArmorMaterial(
            defense,
            enchantment,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ModItems.LIVING_CORE.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, name))),
            toughness,
            knockbackResistance
        );
    }
}
