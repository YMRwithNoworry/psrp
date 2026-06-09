package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.LodoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LodoRenderer extends GeoEntityRenderer<LodoEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/lodo.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/lodo.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/lodo.png");

    public LodoRenderer(EntityRendererProvider.Context context) {
        super(context, new LodoModel());
        this.shadowRadius = 0.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(LodoEntity entity) {
        return TEXTURE;
    }

    private static final class LodoModel extends GeoModel<LodoEntity> {
        @Override
        public ResourceLocation getModelResource(LodoEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(LodoEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(LodoEntity entity) {
            return ANIMATION;
        }
    }
}
