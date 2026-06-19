package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.FerSheepEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FerSheepRenderer extends GeoEntityRenderer<FerSheepEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/inf_sheep.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/inf_sheep.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/fersheep.png");

    public FerSheepRenderer(EntityRendererProvider.Context context) {
        super(context, new FerSheepModel());
        this.shadowRadius = FerSheepEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(FerSheepEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        FerSheepEntity entity,
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

    private static final class FerSheepModel extends GeoModel<FerSheepEntity> {
        @Override
        public ResourceLocation getModelResource(FerSheepEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(FerSheepEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(FerSheepEntity entity) {
            return ANIMATION;
        }
    }
}