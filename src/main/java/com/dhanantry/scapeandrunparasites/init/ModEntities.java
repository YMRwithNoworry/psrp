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
import com.dhanantry.scapeandrunparasites.entity.monster.pure.AngedEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EsorEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.GanroEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.OmbooEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.OrchEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.ElviaEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.HaunterEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.LenciaEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.VestaEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.AngedballEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.ElviaBallEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.HommingballEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.LenciaBallEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.NadeBallEntity;
import com.dhanantry.scapeandrunparasites.entity.projectile.NadeEntity;
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
    public static final DeferredHolder<EntityType<?>, EntityType<GanroEntity>> GANRO = ENTITIES.register("warden", () ->
        EntityType.Builder.of(GanroEntity::new, MobCategory.MONSTER)
            .sized(GanroEntity.LEGACY_WIDTH, GanroEntity.LEGACY_HEIGHT)
            .eyeHeight(GanroEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":warden"));
    public static final DeferredHolder<EntityType<?>, EntityType<OmbooEntity>> OMBOO = ENTITIES.register("bomber_light", () ->
        EntityType.Builder.of(OmbooEntity::new, MobCategory.MONSTER)
            .sized(OmbooEntity.LEGACY_WIDTH, OmbooEntity.LEGACY_HEIGHT)
            .eyeHeight(OmbooEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":bomber_light"));
    public static final DeferredHolder<EntityType<?>, EntityType<EsorEntity>> ESOR = ENTITIES.register("bomber_heavy", () ->
        EntityType.Builder.of(EsorEntity::new, MobCategory.MONSTER)
            .sized(EsorEntity.LEGACY_WIDTH, EsorEntity.LEGACY_HEIGHT)
            .eyeHeight(EsorEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":bomber_heavy"));
    public static final DeferredHolder<EntityType<?>, EntityType<AngedEntity>> ANGED = ENTITIES.register("vigilante", () ->
        EntityType.Builder.of(AngedEntity::new, MobCategory.MONSTER)
            .sized(AngedEntity.LEGACY_WIDTH, AngedEntity.LEGACY_HEIGHT)
            .eyeHeight(AngedEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":vigilante"));
    public static final DeferredHolder<EntityType<?>, EntityType<HaunterEntity>> HAUNTER = ENTITIES.register("haunter", () ->
        EntityType.Builder.of(HaunterEntity::new, MobCategory.MONSTER)
            .sized(HaunterEntity.LEGACY_WIDTH, HaunterEntity.LEGACY_HEIGHT)
            .eyeHeight(HaunterEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":haunter"));
    public static final DeferredHolder<EntityType<?>, EntityType<VestaEntity>> VESTA = ENTITIES.register("carrier_colony", () ->
        EntityType.Builder.of(VestaEntity::new, MobCategory.MONSTER)
            .sized(VestaEntity.LEGACY_WIDTH, VestaEntity.LEGACY_HEIGHT)
            .eyeHeight(VestaEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":carrier_colony"));
    public static final DeferredHolder<EntityType<?>, EntityType<ElviaEntity>> ELVIA = ENTITIES.register("wraith", () ->
        EntityType.Builder.of(ElviaEntity::new, MobCategory.MONSTER)
            .sized(ElviaEntity.LEGACY_WIDTH, ElviaEntity.LEGACY_HEIGHT)
            .eyeHeight(ElviaEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":wraith"));
    public static final DeferredHolder<EntityType<?>, EntityType<LenciaEntity>> LENCIA = ENTITIES.register("bogle", () ->
        EntityType.Builder.of(LenciaEntity::new, MobCategory.MONSTER)
            .sized(LenciaEntity.LEGACY_WIDTH, LenciaEntity.LEGACY_HEIGHT)
            .eyeHeight(LenciaEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":bogle"));
    public static final DeferredHolder<EntityType<?>, EntityType<WebballEntity>> WEBBALL = ENTITIES.register("webball", () ->
        EntityType.Builder.<WebballEntity>of(WebballEntity::new, MobCategory.MISC)
            .sized(WebballEntity.LEGACY_WIDTH, WebballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":webball"));
    public static final DeferredHolder<EntityType<?>, EntityType<AngedballEntity>> ANGEDBALL = ENTITIES.register("ballball", () ->
        EntityType.Builder.<AngedballEntity>of(AngedballEntity::new, MobCategory.MISC)
            .sized(AngedballEntity.LEGACY_WIDTH, AngedballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":ballball"));
    public static final DeferredHolder<EntityType<?>, EntityType<SpineballEntity>> SPINEBALL = ENTITIES.register("spineball", () ->
        EntityType.Builder.<SpineballEntity>of(SpineballEntity::new, MobCategory.MISC)
            .sized(SpineballEntity.LEGACY_WIDTH, SpineballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":spineball"));
    public static final DeferredHolder<EntityType<?>, EntityType<HommingballEntity>> HOMMING = ENTITIES.register("homming", () ->
        EntityType.Builder.<HommingballEntity>of(HommingballEntity::new, MobCategory.MISC)
            .sized(HommingballEntity.LEGACY_WIDTH, HommingballEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":homming"));
    public static final DeferredHolder<EntityType<?>, EntityType<ElviaBallEntity>> ELVIABALL = ENTITIES.register("balltall", () ->
        EntityType.Builder.<ElviaBallEntity>of(ElviaBallEntity::new, MobCategory.MISC)
            .sized(ElviaBallEntity.LEGACY_WIDTH, ElviaBallEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":balltall"));
    public static final DeferredHolder<EntityType<?>, EntityType<LenciaBallEntity>> LENCIABALL = ENTITIES.register("ballmall", () ->
        EntityType.Builder.<LenciaBallEntity>of(LenciaBallEntity::new, MobCategory.MISC)
            .sized(LenciaBallEntity.LEGACY_WIDTH, LenciaBallEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":ballmall"));
    public static final DeferredHolder<EntityType<?>, EntityType<NadeBallEntity>> NADEBALL = ENTITIES.register("nadeball", () ->
        EntityType.Builder.<NadeBallEntity>of(NadeBallEntity::new, MobCategory.MISC)
            .sized(NadeBallEntity.LEGACY_WIDTH, NadeBallEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":nadeball"));
    public static final DeferredHolder<EntityType<?>, EntityType<NadeEntity>> NADE = ENTITIES.register("nade", () ->
        EntityType.Builder.<NadeEntity>of(NadeEntity::new, MobCategory.MISC)
            .sized(NadeEntity.LEGACY_WIDTH, NadeEntity.LEGACY_HEIGHT)
            .clientTrackingRange(8)
            .updateInterval(3)
            .build(SRPMain.MODID + ":nade"));

    private ModEntities() {
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
