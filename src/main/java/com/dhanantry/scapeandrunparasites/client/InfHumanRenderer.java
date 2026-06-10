package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.InfHumanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfHumanRenderer extends GeoEntityRenderer<InfHumanEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/inf_human.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/inf_human.animation.json");
    private static final ResourceLocation TEXTURE_DEFAULT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/human.png");
    private static final ResourceLocation TEXTURE_ALT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/human1.png");
    private static final ResourceLocation TEXTURE_EATEN = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humaneaten.png");
    private static final ResourceLocation TEXTURE_FLOOD = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanflood.png");
    private static final ResourceLocation TEXTURE_FROZEN = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanfrozen.png");
    private static final ResourceLocation TEXTURE_KIM = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/humanforge.png");
    private static final String LEGACY_KIM_NAME = "Kim";

    public InfHumanRenderer(EntityRendererProvider.Context context) {
        super(context, new InfHumanModel());
        this.shadowRadius = InfHumanEntity.LEGACY_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(InfHumanEntity entity) {
        return textureFor(entity);
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        InfHumanEntity entity,
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

    private static ResourceLocation textureFor(InfHumanEntity entity) {
        if (entity.hasCustomName() && LEGACY_KIM_NAME.equalsIgnoreCase(entity.getName().getString())) {
            return TEXTURE_KIM;
        }
        return switch (entity.getSkin()) {
            case 1 -> TEXTURE_ALT;
            case 2 -> TEXTURE_EATEN;
            case 3 -> TEXTURE_FLOOD;
            case 120 -> TEXTURE_FROZEN;
            default -> TEXTURE_DEFAULT;
        };
    }

    private static final class InfHumanModel extends GeoModel<InfHumanEntity> {
        @Override
        public ResourceLocation getModelResource(InfHumanEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(InfHumanEntity entity) {
            return textureFor(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(InfHumanEntity entity) {
            return ANIMATION;
        }
    }
}
