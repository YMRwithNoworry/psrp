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
        event.registerEntityRenderer(ModEntities.RATHOL.get(), RatholRenderer::new);
        event.registerEntityRenderer(ModEntities.GOTHOL.get(), GotholRenderer::new);
        event.registerEntityRenderer(ModEntities.BUTHOL.get(), ButholRenderer::new);
        event.registerEntityRenderer(ModEntities.TONRO.get(), TonroRenderer::new);
        event.registerEntityRenderer(ModEntities.UNVO.get(), UnvoRenderer::new);
        event.registerEntityRenderer(ModEntities.NAK.get(), NakRenderer::new);
        event.registerEntityRenderer(ModEntities.GANRO.get(), GanroRenderer::new);
        event.registerEntityRenderer(ModEntities.OMBOO.get(), OmbooRenderer::new);
        event.registerEntityRenderer(ModEntities.ESOR.get(), EsorRenderer::new);
        event.registerEntityRenderer(ModEntities.ANGED.get(), AngedRenderer::new);
        event.registerEntityRenderer(ModEntities.HAUNTER.get(), HaunterRenderer::new);
        event.registerEntityRenderer(ModEntities.VESTA.get(), VestaRenderer::new);
        event.registerEntityRenderer(ModEntities.ELVIA.get(), ElviaRenderer::new);
        event.registerEntityRenderer(ModEntities.WEBBALL.get(), WebballRenderer::new);
        event.registerEntityRenderer(ModEntities.ANGEDBALL.get(), AngedballRenderer::new);
        event.registerEntityRenderer(ModEntities.SPINEBALL.get(), SpineballRenderer::new);
        event.registerEntityRenderer(ModEntities.HOMMING.get(), HommingballRenderer::new);
        event.registerEntityRenderer(ModEntities.ELVIABALL.get(), ElviaBallRenderer::new);
    }
}
