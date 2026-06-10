package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = SRPMain.MODID, value = Dist.CLIENT)
public final class ModClientEvents {
    private ModClientEvents() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GRUNT.get(), FlogRenderer::new);
        event.registerEntityRenderer(ModEntities.KIRIN.get(), KirinRenderer::new);
        event.registerEntityRenderer(ModEntities.MONARCH.get(), OrchRenderer::new);
        event.registerEntityRenderer(ModEntities.LODO.get(), LodoRenderer::new);
        event.registerEntityRenderer(ModEntities.MUDO.get(), MudoRenderer::new);
        event.registerEntityRenderer(ModEntities.NUUH.get(), NuuhRenderer::new);
        event.registerEntityRenderer(ModEntities.ATA.get(), AtaRenderer::new);
        event.registerEntityRenderer(ModEntities.WEBBALL.get(), WebballRenderer::new);
    }
}
