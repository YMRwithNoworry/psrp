package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.OrchEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OrchRenderer extends GeoEntityRenderer<OrchEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/orch.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/orch.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/orch.png");
    private static final ResourceLocation TEXTURE_WEAK = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/orchsp1.png");
    private static final ResourceLocation TEXTURE_HEAVY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/orchh.png");

    public OrchRenderer(EntityRendererProvider.Context context) {
        super(context, new OrchModel());
        this.shadowRadius = 1.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(OrchEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(OrchEntity entity) {
        return switch (entity.getSkin()) {
            case OrchEntity.LEGACY_VARIANT_SKIN_WEAK -> TEXTURE_WEAK;
            case OrchEntity.LEGACY_VARIANT_SKIN_HEAVY -> TEXTURE_HEAVY;
            default -> TEXTURE;
        };
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        OrchEntity entity,
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
            float horizontal = 1.0F + Math.min(pulse, 1.0F) * 0.04F;
            float vertical = 1.0F - Math.min(pulse, 1.0F) * 0.02F;
            poseStack.scale(horizontal, vertical, horizontal);
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class OrchModel extends GeoModel<OrchEntity> {
        @Override
        public ResourceLocation getModelResource(OrchEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(OrchEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(OrchEntity entity) {
            return ANIMATION;
        }
    }
}
