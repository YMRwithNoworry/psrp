package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.NakEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NakRenderer extends GeoEntityRenderer<NakEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/nak.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/nak.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/nak.png");

    public NakRenderer(EntityRendererProvider.Context context) {
        super(context, new NakModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public ResourceLocation getTextureLocation(NakEntity entity) {
        return TEXTURE;
    }

    private static final class NakModel extends GeoModel<NakEntity> {
        @Override
        public ResourceLocation getModelResource(NakEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(NakEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(NakEntity entity) {
            return ANIMATION;
        }
    }
}
