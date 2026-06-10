package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.TonroEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TonroRenderer extends GeoEntityRenderer<TonroEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/tonro.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/tonro.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/tonro.png");

    public TonroRenderer(EntityRendererProvider.Context context) {
        super(context, new TonroModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public ResourceLocation getTextureLocation(TonroEntity entity) {
        return TEXTURE;
    }

    private static final class TonroModel extends GeoModel<TonroEntity> {
        @Override
        public ResourceLocation getModelResource(TonroEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(TonroEntity entity) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(TonroEntity entity) {
            return ANIMATION;
        }
    }
}
