package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public final class ModEntityEvents {
    private ModEntityEvents() {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GRUNT.get(), FlogEntity.createAttributes().build());
    }
}
