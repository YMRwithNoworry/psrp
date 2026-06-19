package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.HebluEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.KirinEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.TonroEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.TonroEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.UnvoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.FerHorseEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.FerSheepEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.FerWolfEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.DorpaEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfBearEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfCowHeadEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfCowEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHorseHeadEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHorseEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHumanHeadEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfPigHeadEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfPigEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfSheepHeadEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfSheepEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfWolfHeadEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfWolfEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHumanEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.AtaEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.ButholEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.GotholEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.LodoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.MudoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.NuuhEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.RatholEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.AlafhaEntity;
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
import com.dhanantry.scapeandrunparasites.entity.projectile.AlafhaBallEntity;
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
    public static final DeferredHolder<EntityType<?>, EntityType<DorpaEntity>> DORPA = ENTITIES.register("sim_bigspider", () ->
        EntityType.Builder.of(DorpaEntity::new, MobCategory.MONSTER)
            .sized(DorpaEntity.LEGACY_WIDTH, DorpaEntity.LEGACY_HEIGHT)
            .eyeHeight(DorpaEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_bigspider"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfBearEntity>> INFBEAR = ENTITIES.register("sim_bear", () ->
        EntityType.Builder.of(InfBearEntity::new, MobCategory.MONSTER)
            .sized(InfBearEntity.LEGACY_WIDTH, InfBearEntity.LEGACY_HEIGHT)
            .eyeHeight(InfBearEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_bear"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfHumanEntity>> INFHUMAN = ENTITIES.register("sim_human", () ->
        EntityType.Builder.of(InfHumanEntity::new, MobCategory.MONSTER)
            .sized(InfHumanEntity.LEGACY_WIDTH, InfHumanEntity.LEGACY_HEIGHT)
            .eyeHeight(InfHumanEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_human"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfHumanHeadEntity>> INFHUMANHEAD = ENTITIES.register("sim_humanhead", () ->
        EntityType.Builder.of(InfHumanHeadEntity::new, MobCategory.MONSTER)
            .sized(InfHumanHeadEntity.LEGACY_WIDTH, InfHumanHeadEntity.LEGACY_HEIGHT)
            .eyeHeight(InfHumanHeadEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_humanhead"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfCowEntity>> INFCOW = ENTITIES.register("sim_cow", () ->
        EntityType.Builder.of(InfCowEntity::new, MobCategory.MONSTER)
            .sized(InfCowEntity.LEGACY_WIDTH, InfCowEntity.LEGACY_HEIGHT)
            .eyeHeight(InfCowEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_cow"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfCowHeadEntity>> INFCOWHEAD = ENTITIES.register("sim_cowhead", () ->
        EntityType.Builder.of(InfCowHeadEntity::new, MobCategory.MONSTER)
            .sized(InfCowHeadEntity.LEGACY_WIDTH, InfCowHeadEntity.LEGACY_HEIGHT)
            .eyeHeight(InfCowHeadEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_cowhead"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfPigEntity>> INFPIG = ENTITIES.register("sim_pig", () ->
        EntityType.Builder.of(InfPigEntity::new, MobCategory.MONSTER)
            .sized(InfPigEntity.LEGACY_WIDTH, InfPigEntity.LEGACY_HEIGHT)
            .eyeHeight(InfPigEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_pig"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfPigHeadEntity>> INFPIGHEAD = ENTITIES.register("sim_pighead", () ->
        EntityType.Builder.of(InfPigHeadEntity::new, MobCategory.MONSTER)
            .sized(InfPigHeadEntity.LEGACY_WIDTH, InfPigHeadEntity.LEGACY_HEIGHT)
            .eyeHeight(InfPigHeadEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_pighead"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfSheepEntity>> INFSHEEP = ENTITIES.register("sim_sheep", () ->
        EntityType.Builder.of(InfSheepEntity::new, MobCategory.MONSTER)
            .sized(InfSheepEntity.LEGACY_WIDTH, InfSheepEntity.LEGACY_HEIGHT)
            .eyeHeight(InfSheepEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_sheep"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfSheepHeadEntity>> INFSHEEPHEAD = ENTITIES.register("sim_sheephead", () ->
        EntityType.Builder.of(InfSheepHeadEntity::new, MobCategory.MONSTER)
            .sized(InfSheepHeadEntity.LEGACY_WIDTH, InfSheepHeadEntity.LEGACY_HEIGHT)
            .eyeHeight(InfSheepHeadEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_sheephead"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfWolfEntity>> INFWOLF = ENTITIES.register("sim_wolf", () ->
        EntityType.Builder.of(InfWolfEntity::new, MobCategory.MONSTER)
            .sized(InfWolfEntity.LEGACY_WIDTH, InfWolfEntity.LEGACY_HEIGHT)
            .eyeHeight(InfWolfEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_wolf"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfWolfHeadEntity>> INFWOLFHEAD = ENTITIES.register("sim_wolfhead", () ->
        EntityType.Builder.of(InfWolfHeadEntity::new, MobCategory.MONSTER)
            .sized(InfWolfHeadEntity.LEGACY_WIDTH, InfWolfHeadEntity.LEGACY_HEIGHT)
            .eyeHeight(InfWolfHeadEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_wolfhead"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfHorseEntity>> INFHORSE = ENTITIES.register("sim_horse", () ->
        EntityType.Builder.of(InfHorseEntity::new, MobCategory.MONSTER)
            .sized(InfHorseEntity.LEGACY_WIDTH, InfHorseEntity.LEGACY_HEIGHT)
            .eyeHeight(InfHorseEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_horse"));
    public static final DeferredHolder<EntityType<?>, EntityType<InfHorseHeadEntity>> INFHORSEHEAD = ENTITIES.register("sim_horsehead", () ->
        EntityType.Builder.of(InfHorseHeadEntity::new, MobCategory.MONSTER)
            .sized(InfHorseHeadEntity.LEGACY_WIDTH, InfHorseHeadEntity.LEGACY_HEIGHT)
            .eyeHeight(InfHorseHeadEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":sim_horsehead"));
    public static final DeferredHolder<EntityType<?>, EntityType<FerSheepEntity>> FERSHEEP = ENTITIES.register("fer_sheep", () ->
        EntityType.Builder.of(FerSheepEntity::new, MobCategory.MONSTER)
            .sized(FerSheepEntity.LEGACY_WIDTH, FerSheepEntity.LEGACY_HEIGHT)
            .eyeHeight(FerSheepEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":fer_sheep"));
    public static final DeferredHolder<EntityType<?>, EntityType<FerWolfEntity>> FERWOLF = ENTITIES.register("fer_wolf", () ->
        EntityType.Builder.of(FerWolfEntity::new, MobCategory.MONSTER)
            .sized(FerWolfEntity.LEGACY_WIDTH, FerWolfEntity.LEGACY_HEIGHT)
            .eyeHeight(FerWolfEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":fer_wolf"));
    public static final DeferredHolder<EntityType<?>, EntityType<FerHorseEntity>> FERHORSE = ENTITIES.register("fer_horse", () ->
        EntityType.Builder.of(FerHorseEntity::new, MobCategory.MONSTER)
            .sized(FerHorseEntity.LEGACY_WIDTH, FerHorseEntity.LEGACY_HEIGHT)
            .eyeHeight(FerHorseEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":fer_horse"));
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
    public static final DeferredHolder<EntityType<?>, EntityType<AlafhaEntity>> ALAFHA = ENTITIES.register("overseer", () ->
        EntityType.Builder.of(AlafhaEntity::new, MobCategory.MONSTER)
            .sized(AlafhaEntity.LEGACY_WIDTH, AlafhaEntity.LEGACY_HEIGHT)
            .eyeHeight(AlafhaEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(8)
            .build(SRPMain.MODID + ":overseer"));
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
    public static final DeferredHolder<EntityType<?>, EntityType<HebluEntity>> HEBLU = ENTITIES.register("draconite", () ->
        EntityType.Builder.of(HebluEntity::new, MobCategory.MONSTER)
            .sized(HebluEntity.LEGACY_WIDTH, HebluEntity.LEGACY_HEIGHT)
            .eyeHeight(HebluEntity.LEGACY_EYE_HEIGHT)
            .clientTrackingRange(10)
            .build(SRPMain.MODID + ":draconite"));
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
    public static final DeferredHolder<EntityType<?>, EntityType<AlafhaBallEntity>> ALAFHABALL = ENTITIES.register("salivaball", () ->
        EntityType.Builder.<AlafhaBallEntity>of(AlafhaBallEntity::new, MobCategory.MISC)
            .sized(AlafhaBallEntity.LEGACY_WIDTH, AlafhaBallEntity.LEGACY_HEIGHT)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(SRPMain.MODID + ":salivaball"));
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
