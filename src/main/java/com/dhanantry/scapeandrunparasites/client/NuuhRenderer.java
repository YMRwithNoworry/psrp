package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.NuuhEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NuuhRenderer extends GeoEntityRenderer<NuuhEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/nuuh.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/nuuh.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/nuuh.png");
    private static final ResourceLocation TEXTURE_VIRAL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/nuuhv.png");
    private static final ResourceLocation TEXTURE_BLEEDING = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/nuuhb.png");

    public NuuhRenderer(EntityRendererProvider.Context context) {
        super(context, new NuuhModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(NuuhEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(NuuhEntity entity) {
        return switch (entity.getSkin()) {
            case NuuhEntity.LEGACY_VARIANT_SKIN_VIRAL -> TEXTURE_VIRAL;
            case NuuhEntity.LEGACY_VARIANT_SKIN_BLEEDING -> TEXTURE_BLEEDING;
            default -> TEXTURE;
        };
    }

    private static final class NuuhModel extends GeoModel<NuuhEntity> {
        @Override
        public ResourceLocation getModelResource(NuuhEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(NuuhEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(NuuhEntity entity) {
            return ANIMATION;
        }
    }
}
