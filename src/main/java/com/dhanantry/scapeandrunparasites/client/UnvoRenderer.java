package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.UnvoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class UnvoRenderer extends GeoEntityRenderer<UnvoEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/unvo.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/unvo.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/unvo.png");

    public UnvoRenderer(EntityRendererProvider.Context context) {
        super(context, new UnvoModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(UnvoEntity entity) {
        return TEXTURE;
    }

    private static final class UnvoModel extends GeoModel<UnvoEntity> {
        @Override
        public ResourceLocation getModelResource(UnvoEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(UnvoEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(UnvoEntity entity) {
            return ANIMATION;
        }
    }
}
