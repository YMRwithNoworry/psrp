package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.AtaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AtaRenderer extends GeoEntityRenderer<AtaEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/ata.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/ata.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/gnat.png");

    public AtaRenderer(EntityRendererProvider.Context context) {
        super(context, new AtaModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(AtaEntity entity) {
        return TEXTURE;
    }

    private static final class AtaModel extends GeoModel<AtaEntity> {
        @Override
        public ResourceLocation getModelResource(AtaEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(AtaEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(AtaEntity entity) {
            return ANIMATION;
        }
    }
}
