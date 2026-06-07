package com.dhanantry.scapeandrunparasites.item;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public final class SrpEquipmentEvents {
    private static final ResourceLocation REACH_ID = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "living_weapon_reach");

    private SrpEquipmentEvents() {
    }

    @SubscribeEvent
    public static void onItemAttributes(ItemAttributeModifierEvent event) {
        Item item = event.getItemStack().getItem();
        if (item instanceof LivingMeleeWeaponItem weapon) {
            event.replaceModifier(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, weapon.configuredAttackDamage(), AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            );
            event.replaceModifier(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, weapon.attackSpeed(), AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            );
            if (SrpConfig.WEAPON_CANCEL_PACKET.get()) {
                event.replaceModifier(
                    Attributes.ENTITY_INTERACTION_RANGE,
                    new AttributeModifier(REACH_ID, weapon.configuredReach(), AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
                );
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity living) {
            ItemStack weapon = living.getMainHandItem();
            if (weapon.getItem() instanceof LivingMeleeWeaponItem || weapon.getItem() instanceof LivingBowItem) {
                SrpItemData.addInt(weapon, SrpItemData.KILLS, Math.max(1, (int) event.getEntity().getMaxHealth()));
                tryUpgradeWeaponStack(living, weapon);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        int damage = Math.max(1, (int) Math.ceil(event.getNewDamage()));
        for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
            ItemStack armor = entity.getItemBySlot(slot);
            if (armor.getItem() instanceof LivingArmorItem livingArmor) {
                SrpItemData.addInt(armor, SrpItemData.HITS, damage);
                tryUpgradeArmorStack(entity, armor, livingArmor);
            }
        }
    }

    public static void tryUpgradeArmorStack(Entity entity, ItemStack stack, LivingArmorItem armor) {
        Item next = armor.nextItem();
        if (armor.isSentient() || next == null || SrpItemData.getInt(stack, SrpItemData.HITS) < SrpConfig.WEAPON_LIVING_SENTIENT_DAMAGE_NEEDED.get()) {
            return;
        }
        if (entity instanceof LivingEntity living) {
            ItemStack upgraded = new ItemStack(next, stack.getCount());
            upgraded.applyComponents(stack.getComponentsPatch());
            living.setItemSlot(armor.getEquipmentSlot(), upgraded);
        }
    }

    private static void tryUpgradeWeaponStack(LivingEntity living, ItemStack stack) {
        Item next = null;
        if (stack.getItem() instanceof LivingMeleeWeaponItem weapon && !weapon.isSentient()) {
            next = weaponNext(stack);
        } else if (stack.getItem() instanceof LivingBowItem bow && !bow.isSentient()) {
            next = bow.nextItem();
        }
        if (next == null || SrpItemData.getInt(stack, SrpItemData.KILLS) < SrpConfig.WEAPON_LIVING_SENTIENT_HP_NEEDED.get()) {
            return;
        }
        ItemStack upgraded = new ItemStack(next, stack.getCount());
        upgraded.applyComponents(stack.getComponentsPatch());
        if (living instanceof Player player) {
            player.setItemSlot(EquipmentSlot.MAINHAND, upgraded);
        } else {
            living.setItemSlot(EquipmentSlot.MAINHAND, upgraded);
        }
    }

    private static Item weaponNext(ItemStack stack) {
        String path = stack.getItemHolder().getRegisteredName();
        return switch (path) {
            case "srparasites:weapon_scythe" -> com.dhanantry.scapeandrunparasites.init.ModItems.WEAPON_SCYTHE_SENTIENT.get();
            case "srparasites:weapon_axe" -> com.dhanantry.scapeandrunparasites.init.ModItems.WEAPON_AXE_SENTIENT.get();
            case "srparasites:weapon_sword" -> com.dhanantry.scapeandrunparasites.init.ModItems.WEAPON_SWORD_SENTIENT.get();
            case "srparasites:weapon_cleaver" -> com.dhanantry.scapeandrunparasites.init.ModItems.WEAPON_CLEAVER_SENTIENT.get();
            case "srparasites:weapon_maul" -> com.dhanantry.scapeandrunparasites.init.ModItems.WEAPON_MAUL_SENTIENT.get();
            case "srparasites:weapon_lance" -> com.dhanantry.scapeandrunparasites.init.ModItems.WEAPON_LANCE_SENTIENT.get();
            default -> null;
        };
    }
}
