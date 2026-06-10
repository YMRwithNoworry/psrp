package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.VestaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VestaRenderer extends GeoEntityRenderer<VestaEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/vesta.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/vesta.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/vesta.png");
    private static final ResourceLocation TEXTURE_VARIANT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/vestare.png");

    public VestaRenderer(EntityRendererProvider.Context context) {
        super(context, new VestaModel());
        this.shadowRadius = VestaEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(VestaEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(VestaEntity entity) {
        return entity.getSkin() == VestaEntity.LEGACY_VARIANT_SKIN ? TEXTURE_VARIANT : TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        VestaEntity entity,
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

    private static final class VestaModel extends GeoModel<VestaEntity> {
        @Override
        public ResourceLocation getModelResource(VestaEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(VestaEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(VestaEntity entity) {
            return ANIMATION;
        }
    }
}
