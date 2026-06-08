package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.KirinEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.OrchEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.WebballEntity;
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
    public static final DeferredHolder<EntityType<?>, EntityType<KirinEntity>> KIRIN = ENTITIES.register("kirin", () ->
        EntityType.Builder.of(KirinEntity::new, MobCategory.MONSTER)
            .sized(KirinEntity.LEGACY_WIDTH, KirinEntity.LEGACY_HEIGHT)
            .eyeHeight(KirinEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":kirin"));
    public static final DeferredHolder<EntityType<?>, EntityType<OrchEntity>> MONARCH = ENTITIES.register("monarch", () ->
        EntityType.Builder.of(OrchEntity::new, MobCategory.MONSTER)
            .sized(OrchEntity.LEGACY_WIDTH, OrchEntity.LEGACY_HEIGHT)
            .eyeHeight(OrchEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":monarch"));
    public static final DeferredHolder<EntityType<?>, EntityType<WebballEntity>> WEBBALL = ENTITIES.register("webball", () ->
        EntityType.Builder.<WebballEntity>of(WebballEntity::new, MobCategory.MISC)
            .sized(WebballEntity.LEGACY_WIDTH, WebballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":webball"));

    private ModEntities() {
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
