package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlogRenderer extends GeoEntityRenderer<FlogEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/flog.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/flog.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flog.png");
    private static final ResourceLocation TEXTURE_VIRULENT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flogv.png");
    private static final ResourceLocation TEXTURE_BLEEDING = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flogb.png");
    private static final ResourceLocation TEXTURE_HEAVY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flogh.png");

    public FlogRenderer(EntityRendererProvider.Context context) {
        super(context, new FlogModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(FlogEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(FlogEntity entity) {
        return switch (entity.getSkin()) {
            case 5 -> TEXTURE_VIRULENT;
            case 6 -> TEXTURE_BLEEDING;
            case 7 -> TEXTURE_HEAVY;
            default -> TEXTURE;
        };
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        FlogEntity entity,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        int renderColor
    ) {
        float pulse = entity.getAttackTimer();
        if (pulse > 0.0F) {
            float horizontal = 1.0F + Math.min(pulse, 1.5F) * 0.06F;
            float vertical = 1.0F - Math.min(pulse, 1.5F) * 0.025F;
            poseStack.scale(horizontal, vertical, horizontal);
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class FlogModel extends GeoModel<FlogEntity> {
        @Override
        public ResourceLocation getModelResource(FlogEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(FlogEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(FlogEntity entity) {
            return ANIMATION;
        }
    }
}
