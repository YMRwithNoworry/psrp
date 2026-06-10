package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.entity.monster.derived.KirinEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.HebluEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.TonroEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.UnvoEntity;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.DorpaEntity;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public final class ModEntityEvents {
    private ModEntityEvents() {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GRUNT.get(), FlogEntity.createAttributes().build());
        event.put(ModEntities.KIRIN.get(), KirinEntity.createAttributes().build());
        event.put(ModEntities.MONARCH.get(), OrchEntity.createAttributes().build());
        event.put(ModEntities.DORPA.get(), DorpaEntity.createAttributes().build());
        event.put(ModEntities.LODO.get(), LodoEntity.createAttributes().build());
        event.put(ModEntities.MUDO.get(), MudoEntity.createAttributes().build());
        event.put(ModEntities.NUUH.get(), NuuhEntity.createAttributes().build());
        event.put(ModEntities.ATA.get(), AtaEntity.createAttributes().build());
        event.put(ModEntities.RATHOL.get(), RatholEntity.createAttributes().build());
        event.put(ModEntities.GOTHOL.get(), GotholEntity.createAttributes().build());
        event.put(ModEntities.BUTHOL.get(), ButholEntity.createAttributes().build());
        event.put(ModEntities.TONRO.get(), TonroEntity.createAttributes().build());
        event.put(ModEntities.UNVO.get(), UnvoEntity.createAttributes().build());
        event.put(ModEntities.NAK.get(), NakEntity.createAttributes().build());
        event.put(ModEntities.GANRO.get(), GanroEntity.createAttributes().build());
        event.put(ModEntities.OMBOO.get(), OmbooEntity.createAttributes().build());
        event.put(ModEntities.ESOR.get(), EsorEntity.createAttributes().build());
        event.put(ModEntities.ANGED.get(), AngedEntity.createAttributes().build());
        event.put(ModEntities.ALAFHA.get(), AlafhaEntity.createAttributes().build());
        event.put(ModEntities.HAUNTER.get(), HaunterEntity.createAttributes().build());
        event.put(ModEntities.VESTA.get(), VestaEntity.createAttributes().build());
        event.put(ModEntities.ELVIA.get(), ElviaEntity.createAttributes().build());
        event.put(ModEntities.LENCIA.get(), LenciaEntity.createAttributes().build());
        event.put(ModEntities.HEBLU.get(), HebluEntity.createAttributes().build());
    }
}
