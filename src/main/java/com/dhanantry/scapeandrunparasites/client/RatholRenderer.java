package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.RatholEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RatholRenderer extends GeoEntityRenderer<RatholEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/rathol.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/rathol.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/rathol.png");
    private static final ResourceLocation TEXTURE_VARIANT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/ratholone.png");

    public RatholRenderer(EntityRendererProvider.Context context) {
        super(context, new RatholModel());
        this.shadowRadius = 1.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(RatholEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(RatholEntity entity) {
        return entity.getSkin() == RatholEntity.LEGACY_VARIANT_SKIN ? TEXTURE_VARIANT : TEXTURE;
    }

    private static final class RatholModel extends GeoModel<RatholEntity> {
        @Override
        public ResourceLocation getModelResource(RatholEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(RatholEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(RatholEntity entity) {
            return ANIMATION;
        }
    }
}
