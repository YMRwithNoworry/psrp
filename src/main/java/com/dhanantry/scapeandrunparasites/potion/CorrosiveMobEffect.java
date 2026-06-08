package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class CorrosiveMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0x7A605A;
    public static final int LEGACY_DEFAULT_DAMAGE_VALUE = 3;
    public static final double LEGACY_DEFAULT_DURABILITY_THRESHOLD = 0.1D;

    private static final EquipmentSlot[] LEGACY_ARMOR_SLOTS = {
        EquipmentSlot.HEAD,
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS,
        EquipmentSlot.FEET
    };

    public CorrosiveMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            for (EquipmentSlot slot : LEGACY_ARMOR_SLOTS) {
                corrodeArmor(entity, slot);
            }
        }
        return true;
    }

    private static void corrodeArmor(LivingEntity entity, EquipmentSlot slot) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return;
        }

        int remainingDurability = stack.getMaxDamage() - stack.getDamageValue();
        double threshold = stack.getMaxDamage() * SrpConfig.CORROSIVE_DURABILITY_THRESHOLD.get();
        if (threshold < remainingDurability) {
            stack.hurtAndBreak(SrpConfig.CORROSIVE_DAMAGE_VALUE.get(), entity, slot);
        }
    }
}
