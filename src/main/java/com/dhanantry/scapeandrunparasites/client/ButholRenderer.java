package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.ButholEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ButholRenderer extends GeoEntityRenderer<ButholEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/buthol.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/buthol.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/buthol.png");
    private static final ResourceLocation TEXTURE_VARIANT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/butholone.png");

    public ButholRenderer(EntityRendererProvider.Context context) {
        super(context, new ButholModel());
        this.shadowRadius = 1.3F;
    }

    @Override
    public ResourceLocation getTextureLocation(ButholEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(ButholEntity entity) {
        return entity.getSkin() == ButholEntity.LEGACY_VARIANT_SKIN ? TEXTURE_VARIANT : TEXTURE;
    }

    private static final class ButholModel extends GeoModel<ButholEntity> {
        @Override
        public ResourceLocation getModelResource(ButholEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(ButholEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(ButholEntity entity) {
            return ANIMATION;
        }
    }
}
