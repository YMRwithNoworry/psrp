package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class RageMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0xF84343;
    public static final double LEGACY_DEFAULT_DAMAGE_MULTIPLIER = 0.1D;
    public static final double LEGACY_DEFAULT_SPEED_MULTIPLIER = 0.1D;
    public static final ResourceLocation SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "effect.rage_speed");
    public static final ResourceLocation DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "effect.rage_damage");

    public RageMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        addOrReplaceRageModifier(
            attributeMap.getInstance(Attributes.MOVEMENT_SPEED),
            SPEED_MODIFIER_ID,
            SrpConfig.RAGE_SPEED.get() * (amplifier + 1.0D)
        );
        addOrReplaceRageModifier(
            attributeMap.getInstance(Attributes.ATTACK_DAMAGE),
            DAMAGE_MODIFIER_ID,
            SrpConfig.RAGE_DAMAGE.get() * (amplifier + 1.0D)
        );
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        removeRageModifier(attributeMap.getInstance(Attributes.MOVEMENT_SPEED), SPEED_MODIFIER_ID);
        removeRageModifier(attributeMap.getInstance(Attributes.ATTACK_DAMAGE), DAMAGE_MODIFIER_ID);
    }

    private static void addOrReplaceRageModifier(AttributeInstance attribute, ResourceLocation id, double amount) {
        if (attribute == null) {
            return;
        }
        attribute.removeModifier(id);
        attribute.addPermanentModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    private static void removeRageModifier(AttributeInstance attribute, ResourceLocation id) {
        if (attribute != null) {
            attribute.removeModifier(id);
        }
    }
}
