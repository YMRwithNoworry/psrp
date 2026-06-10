package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.KirinEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.TonroEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.UnvoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.AtaEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.ButholEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.GotholEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.LodoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.MudoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.NuuhEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.RatholEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.OrchEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.SpineballEntity;
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
    public static final DeferredHolder<EntityType<?>, EntityType<LodoEntity>> LODO = ENTITIES.register("lodo", () ->
        EntityType.Builder.of(LodoEntity::new, MobCategory.MONSTER)
            .sized(LodoEntity.LEGACY_WIDTH, LodoEntity.LEGACY_HEIGHT)
            .eyeHeight(LodoEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":lodo"));
    public static final DeferredHolder<EntityType<?>, EntityType<MudoEntity>> MUDO = ENTITIES.register("mudo", () ->
        EntityType.Builder.of(MudoEntity::new, MobCategory.MONSTER)
            .sized(MudoEntity.LEGACY_WIDTH, MudoEntity.LEGACY_HEIGHT)
            .eyeHeight(MudoEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":mudo"));
    public static final DeferredHolder<EntityType<?>, EntityType<NuuhEntity>> NUUH = ENTITIES.register("nuuh", () ->
        EntityType.Builder.of(NuuhEntity::new, MobCategory.MONSTER)
            .sized(NuuhEntity.LEGACY_WIDTH, NuuhEntity.LEGACY_HEIGHT)
            .eyeHeight(NuuhEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":nuuh"));
    public static final DeferredHolder<EntityType<?>, EntityType<AtaEntity>> ATA = ENTITIES.register("gnat", () ->
        EntityType.Builder.of(AtaEntity::new, MobCategory.MONSTER)
            .sized(AtaEntity.LEGACY_WIDTH, AtaEntity.LEGACY_HEIGHT)
            .eyeHeight(AtaEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":gnat"));
    public static final DeferredHolder<EntityType<?>, EntityType<RatholEntity>> RATHOL = ENTITIES.register("carrier_heavy", () ->
        EntityType.Builder.of(RatholEntity::new, MobCategory.MONSTER)
            .sized(RatholEntity.LEGACY_WIDTH, RatholEntity.LEGACY_HEIGHT)
            .eyeHeight(RatholEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":carrier_heavy"));
    public static final DeferredHolder<EntityType<?>, EntityType<GotholEntity>> GOTHOL = ENTITIES.register("carrier_light", () ->
        EntityType.Builder.of(GotholEntity::new, MobCategory.MONSTER)
            .sized(GotholEntity.LEGACY_WIDTH, GotholEntity.LEGACY_HEIGHT)
            .eyeHeight(GotholEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":carrier_light"));
    public static final DeferredHolder<EntityType<?>, EntityType<ButholEntity>> BUTHOL = ENTITIES.register("carrier_flying", () ->
        EntityType.Builder.of(ButholEntity::new, MobCategory.MONSTER)
            .sized(ButholEntity.LEGACY_WIDTH, ButholEntity.LEGACY_HEIGHT)
            .eyeHeight(ButholEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":carrier_flying"));
    public static final DeferredHolder<EntityType<?>, EntityType<TonroEntity>> TONRO = ENTITIES.register("kyphosis", () ->
        EntityType.Builder.of(TonroEntity::new, MobCategory.MONSTER)
            .sized(TonroEntity.LEGACY_WIDTH, TonroEntity.LEGACY_HEIGHT)
            .eyeHeight(TonroEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":kyphosis"));
    public static final DeferredHolder<EntityType<?>, EntityType<UnvoEntity>> UNVO = ENTITIES.register("sentry", () ->
        EntityType.Builder.of(UnvoEntity::new, MobCategory.MONSTER)
            .sized(UnvoEntity.LEGACY_WIDTH, UnvoEntity.LEGACY_HEIGHT)
            .eyeHeight(UnvoEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sentry"));
    public static final DeferredHolder<EntityType<?>, EntityType<NakEntity>> NAK = ENTITIES.register("seizer", () ->
        EntityType.Builder.of(NakEntity::new, MobCategory.MONSTER)
            .sized(NakEntity.LEGACY_WIDTH, NakEntity.LEGACY_HEIGHT)
            .eyeHeight(NakEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":seizer"));
    public static final DeferredHolder<EntityType<?>, EntityType<WebballEntity>> WEBBALL = ENTITIES.register("webball", () ->
        EntityType.Builder.<WebballEntity>of(WebballEntity::new, MobCategory.MISC)
            .sized(WebballEntity.LEGACY_WIDTH, WebballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":webball"));
    public static final DeferredHolder<EntityType<?>, EntityType<SpineballEntity>> SPINEBALL = ENTITIES.register("spineball", () ->
        EntityType.Builder.<SpineballEntity>of(SpineballEntity::new, MobCategory.MISC)
            .sized(SpineballEntity.LEGACY_WIDTH, SpineballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":spineball"));

    private ModEntities() {
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
