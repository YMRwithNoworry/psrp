package com.dhanantry.scapeandrunparasites.item;

import com.dhanantry.scapeandrunparasites.init.ModItems;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class LivingBowItem extends BowItem {
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
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
        if (projectile instanceof AbstractArrow arrow) {
            int damage = sentient ? SrpConfig.WEAPON_BOW_SENTIENT_DAMAGE.get() : SrpConfig.WEAPON_BOW_DAMAGE.get();
            int cap = sentient ? SrpConfig.WEAPON_BOW_SENTIENT_DAMAGE_CAP.get() : SrpConfig.WEAPON_BOW_DAMAGE_CAP.get();
            arrow.setBaseDamage(Math.min(cap, arrow.getBaseDamage() + damage));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.srparasites.srpkills", SrpItemData.getInt(stack, SrpItemData.KILLS), SrpConfig.WEAPON_LIVING_SENTIENT_HP_NEEDED.get()));
    }
}
