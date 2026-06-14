package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfPigHeadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfPigHeadRenderer extends GeoEntityRenderer<InfPigHeadEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/inf_pig_head.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/inf_pig_head.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/pigh.png");

    public InfPigHeadRenderer(EntityRendererProvider.Context context) {
        super(context, new InfPigHeadModel());
        this.shadowRadius = InfPigHeadEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(InfPigHeadEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        InfPigHeadEntity entity,
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
        float wave = 1.0F + Mth.sin(pulse * 100.0F) * pulse * 0.01F;
        float clampedPulse = Mth.clamp(pulse, 0.0F, 1.0F);
        clampedPulse *= clampedPulse;
        clampedPulse *= clampedPulse;
        float horizontal = (1.0F + clampedPulse * 0.4F) * wave;
        float vertical = (1.0F + clampedPulse * 0.1F) / wave;
        poseStack.scale(horizontal, vertical, horizontal);
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class InfPigHeadModel extends GeoModel<InfPigHeadEntity> {
        @Override
        public ResourceLocation getModelResource(InfPigHeadEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(InfPigHeadEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(InfPigHeadEntity entity) {
            return ANIMATION;
        }
    }
}
