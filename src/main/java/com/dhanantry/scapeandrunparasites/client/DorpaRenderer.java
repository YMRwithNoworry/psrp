package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.DorpaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DorpaRenderer extends GeoEntityRenderer<DorpaEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/dorpa.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/dorpa.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/dorpa.png");
    private static final ResourceLocation TEXTURE_VARIANT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/dorpa2.png");

    public DorpaRenderer(EntityRendererProvider.Context context) {
        super(context, new DorpaModel());
        this.shadowRadius = DorpaEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(DorpaEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(DorpaEntity entity) {
        return entity.getSkin() == DorpaEntity.LEGACY_VARIANT_SKIN ? TEXTURE_VARIANT : TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        DorpaEntity entity,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        int renderColor
    ) {
        poseStack.scale(0.78F, 0.78F, 0.78F);
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class DorpaModel extends GeoModel<DorpaEntity> {
        @Override
        public ResourceLocation getModelResource(DorpaEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(DorpaEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(DorpaEntity entity) {
            return ANIMATION;
        }
    }
}
