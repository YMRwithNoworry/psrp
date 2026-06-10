package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.derived.HebluEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HebluRenderer extends GeoEntityRenderer<HebluEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/heblu.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/heblu.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/heblu.png");

    public HebluRenderer(EntityRendererProvider.Context context) {
        super(context, new HebluModel());
        this.shadowRadius = HebluEntity.LEGACY_SHADOW_RADIUS;
    }

    private static final class HebluModel extends GeoModel<HebluEntity> {
        @Override
        public ResourceLocation getModelResource(HebluEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(HebluEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(HebluEntity entity) {
            return ANIMATION;
        }
    }
}
