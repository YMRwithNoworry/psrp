package com.dhanantry.scapeandrunparasites.item;

import com.dhanantry.scapeandrunparasites.init.SrpToolTiers;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import java.util.List;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;

public class LivingMeleeWeaponItem extends SwordItem {
    private final String legacyKey;
    private final boolean sentient;
    private final float attackSpeed;

    public LivingMeleeWeaponItem(String legacyKey, boolean sentient, float attackSpeed, Properties properties) {
        super(SrpToolTiers.LIVING, properties);
        this.legacyKey = legacyKey;
        this.sentient = sentient;
        this.attackSpeed = attackSpeed;
    }

    public static float defaultDamage(String legacyKey, boolean sentient) {
        return Math.max(0, switch (legacyKey.toLowerCase(Locale.ROOT)) {
            case "scythe" -> sentient ? 34 : 17;
            case "axe" -> sentient ? 32 : 16;
            case "sword" -> sentient ? 38 : 19;
            case "cleaver" -> sentient ? 36 : 18;
            case "maul" -> sentient ? 40 : 20;
            case "lance" -> sentient ? 30 : 15;
            default -> sentient ? 20 : 10;
        } - 1);
    }

    public String legacyKey() {
        return legacyKey;
    }

    public boolean isSentient() {
        return sentient;
    }

    public float attackSpeed() {
        return attackSpeed;
    }

    public float configuredAttackDamage() {
        return Math.max(0.0F, SrpConfig.weaponDamage(legacyKey, sentient) - 1.0F);
    }

    public double configuredReach() {
        return SrpConfig.weaponReach(legacyKey, sentient);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.srparasites.srpkills", SrpItemData.getInt(stack, SrpItemData.KILLS), SrpConfig.WEAPON_LIVING_SENTIENT_HP_NEEDED.get()));
        tooltip.add(Component.translatable("tooltip.srparasites.reach", String.format(Locale.ROOT, "%.1f", configuredReach())));
    }
}
