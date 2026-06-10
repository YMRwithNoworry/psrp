package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.AngedEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AngedRenderer extends GeoEntityRenderer<AngedEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/anged.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/anged.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/anged.png");
    private static final ResourceLocation TEXTURE_HEAVY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/angedh.png");

    public AngedRenderer(EntityRendererProvider.Context context) {
        super(context, new AngedModel());
        this.shadowRadius = 1.2F;
    }

    @Override
    public ResourceLocation getTextureLocation(AngedEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(AngedEntity entity) {
        return entity.getSkin() == AngedEntity.LEGACY_HEAVY_SKIN ? TEXTURE_HEAVY : TEXTURE;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        AngedEntity entity,
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
            float wave = 1.0F + (float) Math.sin((entity.tickCount + partialTick) * 100.0F) * pulse * 0.01F;
            float heavy = MthClamp(pulse) * MthClamp(pulse);
            heavy *= heavy;
            float horizontal = (1.0F + heavy * 0.4F) * wave;
            float vertical = (1.0F + heavy * 0.1F) / wave;
            poseStack.scale(horizontal, vertical, horizontal);
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    private static float MthClamp(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }

    private static final class AngedModel extends GeoModel<AngedEntity> {
        @Override
        public ResourceLocation getModelResource(AngedEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(AngedEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(AngedEntity entity) {
            return ANIMATION;
        }
    }
}
