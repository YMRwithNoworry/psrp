package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.FlogEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FlogRenderer extends MobRenderer<FlogEntity, SpiderModel<FlogEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flog.png");
    private static final ResourceLocation TEXTURE_VIRULENT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flogv.png");
    private static final ResourceLocation TEXTURE_BLEEDING = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flogb.png");
    private static final ResourceLocation TEXTURE_HEAVY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/flogh.png");

    public FlogRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(FlogEntity entity) {
        return switch (entity.getSkin()) {
            case 5 -> TEXTURE_VIRULENT;
            case 6 -> TEXTURE_BLEEDING;
            case 7 -> TEXTURE_HEAVY;
            default -> TEXTURE;
        };
    }

    @Override
    protected void scale(FlogEntity entity, PoseStack poseStack, float partialTickTime) {
        float pulse = entity.getAttackTimer();
        if (pulse > 0.0F) {
            float horizontal = 1.0F + Math.min(pulse, 1.5F) * 0.06F;
            float vertical = 1.0F - Math.min(pulse, 1.5F) * 0.025F;
            poseStack.scale(horizontal, vertical, horizontal);
        }
    }
}
