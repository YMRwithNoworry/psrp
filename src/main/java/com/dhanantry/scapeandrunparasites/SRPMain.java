package com.dhanantry.scapeandrunparasites;

import com.dhanantry.scapeandrunparasites.init.ModCreativeTabs;
import com.dhanantry.scapeandrunparasites.init.ModItems;
import com.dhanantry.scapeandrunparasites.item.SrpEquipmentEvents;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(SRPMain.MODID)
public final class SRPMain {
    public static final String MODID = "srparasites";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SRPMain(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(SrpEquipmentEvents.class);
        modContainer.registerConfig(ModConfig.Type.COMMON, SrpConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Loaded Scape and Run: Parasites NeoForge port slice for {}", MODID);
    }
}
