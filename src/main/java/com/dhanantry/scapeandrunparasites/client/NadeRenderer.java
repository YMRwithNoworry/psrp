package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.projectile.NadeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NadeRenderer extends GeoEntityRenderer<NadeEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/nade.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/nade.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/nade.png");

    public NadeRenderer(EntityRendererProvider.Context context) {
        super(context, new NadeModel());
    }

    @Override
    public ResourceLocation getTextureLocation(NadeEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        NadeEntity entity,
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
        float wave = 1.0F + (float) Math.sin(pulse * 100.0F) * pulse * 0.01F;
        float heavy = Math.max(0.0F, Math.min(1.1F, pulse));
        heavy *= heavy;
        heavy *= heavy;
        float horizontal = entity.getBbWidth() * 1.4F + (1.0F + heavy * 0.4F) * wave;
        float vertical = entity.getBbHeight() * 1.25F + (1.0F + heavy * 0.1F) / wave;
        poseStack.scale(horizontal, vertical, horizontal);
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class NadeModel extends GeoModel<NadeEntity> {
        @Override
        public ResourceLocation getModelResource(NadeEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(NadeEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(NadeEntity entity) {
            return ANIMATION;
        }
    }
}
