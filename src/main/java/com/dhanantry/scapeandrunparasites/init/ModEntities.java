package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SRPMain.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FlogEntity>> GRUNT = ENTITIES.register("grunt", () ->
        EntityType.Builder.of(FlogEntity::new, MobCategory.MONSTER)
            .sized(0.7666F, 1.95F)
            .eyeHeight(1.73F)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":grunt"));

    private ModEntities() {
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
