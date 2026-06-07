package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class SrpParasiteMob extends Monster {
    protected SrpParasiteMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public abstract int getParasiteIDRegister();

    protected boolean isParasiteAlly(Entity entity) {
        return entity instanceof SrpParasiteMob;
    }
}
