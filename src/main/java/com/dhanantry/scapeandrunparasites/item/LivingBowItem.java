package com.dhanantry.scapeandrunparasites.item;

import com.dhanantry.scapeandrunparasites.init.ModItems;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class LivingBowItem extends BowItem {
    private static final ThreadLocal<Integer> DRAW_TICKS = new ThreadLocal<>();
    private static final int LEGACY_TIPPED_ARROW_EFFECT_TICKS = 200;
    private static final int LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER = 0;

    private final boolean sentient;

    public LivingBowItem(boolean sentient, Properties properties) {
        super(properties);
        this.sentient = sentient;
    }

    public boolean isSentient() {
        return sentient;
    }

    public Item nextItem() {
        return sentient ? null : ModItems.WEAPON_BOW_SENTIENT.get();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        DRAW_TICKS.set(Math.max(0, getUseDuration(stack, entity) - timeLeft));
        try {
            super.releaseUsing(stack, level, entity, timeLeft);
        } finally {
            DRAW_TICKS.remove();
        }
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        AbstractArrow customized = super.customArrow(arrow, projectileStack, weaponStack);
        if (projectileStack.getItem() instanceof TippedArrowItem && customized instanceof Arrow tippedArrow) {
            tippedArrow.addEffect(new MobEffectInstance(ModEffects.BLEED, LEGACY_TIPPED_ARROW_EFFECT_TICKS, LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER, false, true));
            tippedArrow.addEffect(new MobEffectInstance(ModEffects.DOD_SMOKE_TRAIL, LEGACY_TIPPED_ARROW_EFFECT_TICKS, LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER, false, true));
        }
        return customized;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
        if (projectile instanceof AbstractArrow arrow) {
            int damage = sentient ? SrpConfig.WEAPON_BOW_SENTIENT_DAMAGE.get() : SrpConfig.WEAPON_BOW_DAMAGE.get();
            int bonus = sentient ? SrpConfig.WEAPON_BOW_SENTIENT_BONUS.get() : SrpConfig.WEAPON_BOW_BONUS.get();
            int cap = sentient ? SrpConfig.WEAPON_BOW_SENTIENT_DAMAGE_CAP.get() : SrpConfig.WEAPON_BOW_DAMAGE_CAP.get();
            double multiplier = Math.min((double) damage * cap, drawSeconds() * (double) bonus);
            arrow.setBaseDamage(arrow.getBaseDamage() * multiplier + damage);
        }
    }

    private static int drawSeconds() {
        Integer drawTicks = DRAW_TICKS.get();
        return drawTicks == null ? 0 : drawTicks / 20;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.srparasites.srpkills", SrpItemData.getInt(stack, SrpItemData.KILLS), SrpConfig.WEAPON_LIVING_SENTIENT_HP_NEEDED.get()));
    }
}
