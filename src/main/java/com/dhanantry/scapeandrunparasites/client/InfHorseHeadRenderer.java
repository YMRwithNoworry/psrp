package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHorseHeadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfHorseHeadRenderer extends GeoEntityRenderer<InfHorseHeadEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/inf_horse_head.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/inf_horse_head.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/horseh.png");

    public InfHorseHeadRenderer(EntityRendererProvider.Context context) {
        super(context, new InfHorseHeadModel());
        this.shadowRadius = InfHorseHeadEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(InfHorseHeadEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        InfHorseHeadEntity entity,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        int renderColor
    ) {
        poseStack.scale(1.0F, 1.0F, 1.0F);
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static final class InfHorseHeadModel extends GeoModel<InfHorseHeadEntity> {
        @Override
        public ResourceLocation getModelResource(InfHorseHeadEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(InfHorseHeadEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(InfHorseHeadEntity entity) {
            return ANIMATION;
        }
    }
}
