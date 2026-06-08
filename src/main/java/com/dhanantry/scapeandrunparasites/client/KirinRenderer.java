package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.KirinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KirinRenderer extends GeoEntityRenderer<KirinEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/kirin.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/kirin.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/kirin.png");

    public KirinRenderer(EntityRendererProvider.Context context) {
        super(context, new KirinModel());
        this.shadowRadius = 1.3F;
    }

    private static final class KirinModel extends GeoModel<KirinEntity> {
        @Override
        public ResourceLocation getModelResource(KirinEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(KirinEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(KirinEntity entity) {
            return ANIMATION;
        }
    }
}
