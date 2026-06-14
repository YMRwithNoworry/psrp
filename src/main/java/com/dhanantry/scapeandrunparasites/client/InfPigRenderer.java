package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfPigEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfPigRenderer extends GeoEntityRenderer<InfPigEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/inf_pig.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/inf_pig.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/pig.png");

    public InfPigRenderer(EntityRendererProvider.Context context) {
        super(context, new InfPigModel());
        this.shadowRadius = InfPigEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(InfPigEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        InfPigEntity entity,
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
        float aSize = entity.getSelfeFlashIntensity2();
        float wave = 1.0F + Mth.sin(pulse * 100.0F) * pulse * 0.01F;
        float clampedPulse = Mth.clamp(pulse, 0.0F, 1.0F);
        clampedPulse *= clampedPulse;
        clampedPulse *= clampedPulse;
        float horizontal = (1.0F + clampedPulse * 0.4F) * wave;
        float vertical = (1.0F + clampedPulse * 0.1F) / wave;
        poseStack.scale(horizontal, aSize * vertical, horizontal);
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class InfPigModel extends GeoModel<InfPigEntity> {
        @Override
        public ResourceLocation getModelResource(InfPigEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(InfPigEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(InfPigEntity entity) {
            return ANIMATION;
        }
    }
}
