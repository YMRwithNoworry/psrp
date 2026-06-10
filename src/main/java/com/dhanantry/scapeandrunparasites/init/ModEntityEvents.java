package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.entity.monster.derived.KirinEntity;
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
        event.put(ModEntities.LODO.get(), LodoEntity.createAttributes().build());
        event.put(ModEntities.MUDO.get(), MudoEntity.createAttributes().build());
        event.put(ModEntities.NUUH.get(), NuuhEntity.createAttributes().build());
        event.put(ModEntities.ATA.get(), AtaEntity.createAttributes().build());
        event.put(ModEntities.RATHOL.get(), RatholEntity.createAttributes().build());
        event.put(ModEntities.GOTHOL.get(), GotholEntity.createAttributes().build());
        event.put(ModEntities.BUTHOL.get(), ButholEntity.createAttributes().build());
        event.put(ModEntities.TONRO.get(), TonroEntity.createAttributes().build());
        event.put(ModEntities.UNVO.get(), UnvoEntity.createAttributes().build());
    }
}
