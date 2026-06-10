package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.OmbooEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OmbooRenderer extends GeoEntityRenderer<OmbooEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/omboo.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/omboo.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/omboo.png");
    private static final ResourceLocation TEXTURE_HEAVY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/ombooh.png");

    public OmbooRenderer(EntityRendererProvider.Context context) {
        super(context, new OmbooModel());
        this.shadowRadius = 1.3F;
    }

    @Override
    public ResourceLocation getTextureLocation(OmbooEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(OmbooEntity entity) {
        return entity.getSkin() == OmbooEntity.LEGACY_HEAVY_SKIN ? TEXTURE_HEAVY : TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        OmbooEntity entity,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        int renderColor
    ) {
        float pulse = entity.getSelfeFlashIntensity(partialTick);
        if (pulse > 0.0F) {
            float scale = 1.0F + (float) Math.sin((entity.tickCount + partialTick) * 0.6F) * 0.03F * pulse;
            poseStack.scale(scale, 1.0F / scale, scale);
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class OmbooModel extends GeoModel<OmbooEntity> {
        @Override
        public ResourceLocation getModelResource(OmbooEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(OmbooEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(OmbooEntity entity) {
            return ANIMATION;
        }
    }
}
