package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.GotholEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GotholRenderer extends GeoEntityRenderer<GotholEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/gothol.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/gothol.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/gothol.png");

    public GotholRenderer(EntityRendererProvider.Context context) {
        super(context, new GotholModel());
        this.shadowRadius = 1.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(GotholEntity entity) {
        return TEXTURE;
    }

    private static final class GotholModel extends GeoModel<GotholEntity> {
        @Override
        public ResourceLocation getModelResource(GotholEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(GotholEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(GotholEntity entity) {
            return ANIMATION;
        }
    }
}
