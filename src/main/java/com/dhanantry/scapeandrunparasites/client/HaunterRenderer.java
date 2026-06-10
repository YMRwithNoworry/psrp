package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.HaunterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HaunterRenderer extends GeoEntityRenderer<HaunterEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/pheon.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/pheon.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/pheon.png");
    private static final ResourceLocation TEXTURE_SPECIAL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/pheonsp1.png");

    public HaunterRenderer(EntityRendererProvider.Context context) {
        super(context, new HaunterModel());
        this.shadowRadius = HaunterEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(HaunterEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(HaunterEntity entity) {
        return entity.getSkin() == HaunterEntity.LEGACY_VARIANT_SKIN ? TEXTURE_SPECIAL : TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        HaunterEntity entity,
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
            float wave = 1.0F + (float) Math.sin(pulse * 100.0F) * pulse * 0.01F;
            float heavy = Math.max(0.0F, Math.min(1.0F, pulse));
            heavy *= heavy;
            heavy *= heavy;
            float horizontal = (1.0F + heavy * 0.4F) * wave;
            float vertical = (1.0F + heavy * 0.1F) / wave;
            poseStack.scale(horizontal, vertical, horizontal);
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class HaunterModel extends GeoModel<HaunterEntity> {
        @Override
        public ResourceLocation getModelResource(HaunterEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(HaunterEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(HaunterEntity entity) {
            return ANIMATION;
        }
    }
}
