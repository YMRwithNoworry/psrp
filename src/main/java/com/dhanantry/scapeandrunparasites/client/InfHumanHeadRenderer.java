package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHumanHeadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfHumanHeadRenderer extends GeoEntityRenderer<InfHumanHeadEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/inf_human_head.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/inf_human_head.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanh.png");
    private static final ResourceLocation TEXTURE_1 = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanh1.png");
    private static final ResourceLocation TEXTURE_2 = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanh2.png");
    private static final ResourceLocation TEXTURE_NOCTURN = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanhnocturn.png");

    public InfHumanHeadRenderer(EntityRendererProvider.Context context) {
        super(context, new InfHumanHeadModel());
        this.shadowRadius = InfHumanHeadEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(InfHumanHeadEntity entity) {
        return textureForSkin(entity);
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        InfHumanHeadEntity entity,
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

    private static ResourceLocation textureForSkin(InfHumanHeadEntity entity) {
        return switch (entity.getSkin()) {
            case 1 -> TEXTURE_1;
            case InfHumanHeadEntity.LEGACY_NOCTURN_SKIN -> TEXTURE_NOCTURN;
            default -> TEXTURE;
        };
    }

    private static final class InfHumanHeadModel extends GeoModel<InfHumanHeadEntity> {
        @Override
        public ResourceLocation getModelResource(InfHumanHeadEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(InfHumanHeadEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(InfHumanHeadEntity entity) {
            return ANIMATION;
        }
    }
}
