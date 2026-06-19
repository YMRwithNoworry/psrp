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
        event.registerEntityRenderer(ModEntities.DORPA.get(), DorpaRenderer::new);
        event.registerEntityRenderer(ModEntities.INFBEAR.get(), InfBearRenderer::new);
        event.registerEntityRenderer(ModEntities.INFHUMAN.get(), InfHumanRenderer::new);
        event.registerEntityRenderer(ModEntities.INFHUMANHEAD.get(), InfHumanHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.INFCOW.get(), InfCowRenderer::new);
        event.registerEntityRenderer(ModEntities.INFCOWHEAD.get(), InfCowHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.INFPIG.get(), InfPigRenderer::new);
        event.registerEntityRenderer(ModEntities.INFPIGHEAD.get(), InfPigHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.INFSHEEP.get(), InfSheepRenderer::new);
        event.registerEntityRenderer(ModEntities.INFSHEEPHEAD.get(), InfSheepHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.INFWOLF.get(), InfWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.INFWOLFHEAD.get(), InfWolfHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.INFHORSE.get(), InfHorseRenderer::new);
        event.registerEntityRenderer(ModEntities.INFHORSEHEAD.get(), InfHorseHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.FERSHEEP.get(), FerSheepRenderer::new);
        event.registerEntityRenderer(ModEntities.FERWOLF.get(), FerWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.FERHORSE.get(), FerHorseRenderer::new);
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
        event.registerEntityRenderer(ModEntities.ALAFHA.get(), AlafhaRenderer::new);
        event.registerEntityRenderer(ModEntities.HAUNTER.get(), HaunterRenderer::new);
        event.registerEntityRenderer(ModEntities.VESTA.get(), VestaRenderer::new);
        event.registerEntityRenderer(ModEntities.ELVIA.get(), ElviaRenderer::new);
        event.registerEntityRenderer(ModEntities.LENCIA.get(), LenciaRenderer::new);
        event.registerEntityRenderer(ModEntities.HEBLU.get(), HebluRenderer::new);
        event.registerEntityRenderer(ModEntities.WEBBALL.get(), WebballRenderer::new);
        event.registerEntityRenderer(ModEntities.ANGEDBALL.get(), AngedballRenderer::new);
        event.registerEntityRenderer(ModEntities.SPINEBALL.get(), SpineballRenderer::new);
        event.registerEntityRenderer(ModEntities.HOMMING.get(), HommingballRenderer::new);
        event.registerEntityRenderer(ModEntities.ELVIABALL.get(), ElviaBallRenderer::new);
        event.registerEntityRenderer(ModEntities.LENCIABALL.get(), LenciaBallRenderer::new);
        event.registerEntityRenderer(ModEntities.ALAFHABALL.get(), AlafhaBallRenderer::new);
        event.registerEntityRenderer(ModEntities.NADEBALL.get(), NadeBallRenderer::new);
        event.registerEntityRenderer(ModEntities.NADE.get(), NadeRenderer::new);
    }
}
