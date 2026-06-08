package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class SrpParasiteMob extends Monster {
    protected SrpParasiteMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public abstract int getParasiteIDRegister();

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(isProtectedByTheSign(target) ? null : target);
    }

    protected boolean isParasiteAlly(Entity entity) {
        return entity instanceof SrpParasiteMob;
    }

    protected boolean canTargetLiving(LivingEntity target) {
        if (!canHarmLiving(target)) {
            return false;
        }
        return !(target instanceof Player player) || !player.isCreative();
    }

    protected boolean canHarmLiving(LivingEntity target) {
        return target != this && target != null && target.isAlive() && !isParasiteAlly(target) && !isProtectedByTheSign(target);
    }

    protected boolean isProtectedByTheSign(LivingEntity target) {
        return target instanceof Player && target.hasEffect(ModEffects.THE_SIGN);
    }
}
