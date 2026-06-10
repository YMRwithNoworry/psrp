package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.GanroEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GanroRenderer extends GeoEntityRenderer<GanroEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/ganro.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/ganro.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/ganro.png");
    private static final ResourceLocation TEXTURE_HEAVY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/ganroh.png");

    public GanroRenderer(EntityRendererProvider.Context context) {
        super(context, new GanroModel());
        this.shadowRadius = 1.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(GanroEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(GanroEntity entity) {
        return entity.getSkin() == GanroEntity.LEGACY_HEAVY_SKIN ? TEXTURE_HEAVY : TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        GanroEntity entity,
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
            float wave = 1.0F + MthSin(entity.tickCount + partialTick, pulse) * 0.01F;
            float heavy = pulse * pulse * pulse * pulse;
            float horizontal = (1.0F + heavy * 0.4F) * wave;
            float vertical = (1.0F + heavy * 0.1F) / wave;
            poseStack.scale(horizontal, vertical, horizontal);
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static float MthSin(float age, float pulse) {
        return (float) Math.sin(age * 100.0F) * pulse;
    }

    private static final class GanroModel extends GeoModel<GanroEntity> {
        @Override
        public ResourceLocation getModelResource(GanroEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(GanroEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(GanroEntity entity) {
            return ANIMATION;
        }
    }
}
