package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.SRPMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class SrpSensingMobEffect extends SrpMobEffect {
    public static final int LEGACY_VOMIT_COLOR = 0x726C41;
    public static final int LEGACY_SENSES_COLOR = 0x8E9ED7;
    public static final double LEGACY_VOMIT_FOLLOW_RANGE_MULTIPLIER = 0.9D;
    public static final double LEGACY_SENSES_FOLLOW_RANGE_MULTIPLIER = 0.1D;

    private final ResourceLocation modifierId;
    private final double multiplier;

    public SrpSensingMobEffect(MobEffectCategory category, int color, String modifierPath, double multiplier) {
        super(category, color);
        this.modifierId = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, modifierPath);
        this.multiplier = multiplier;
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        AttributeInstance attribute = attributeMap.getInstance(Attributes.FOLLOW_RANGE);
        if (attribute == null) {
            return;
        }
        attribute.removeModifier(this.modifierId);
        attribute.addPermanentModifier(new AttributeModifier(this.modifierId, this.multiplier * (amplifier + 1.0D), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        AttributeInstance attribute = attributeMap.getInstance(Attributes.FOLLOW_RANGE);
        if (attribute != null) {
            attribute.removeModifier(this.modifierId);
        }
    }
}
