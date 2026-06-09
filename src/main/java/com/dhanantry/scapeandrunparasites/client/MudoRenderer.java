package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.MudoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MudoRenderer extends GeoEntityRenderer<MudoEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "geo/entity/mudo.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "animations/entity/mudo.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/mudo.png");
    private static final ResourceLocation TEXTURE_VIRAL = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/mudov.png");
    private static final ResourceLocation TEXTURE_BLEEDING = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/entity/monster/mudob.png");

    public MudoRenderer(EntityRendererProvider.Context context) {
        super(context, new MudoModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(MudoEntity entity) {
        return textureForSkin(entity);
    }

    private static ResourceLocation textureForSkin(MudoEntity entity) {
        return switch (entity.getSkin()) {
            case MudoEntity.LEGACY_VARIANT_SKIN_VIRAL -> TEXTURE_VIRAL;
            case MudoEntity.LEGACY_VARIANT_SKIN_BLEEDING -> TEXTURE_BLEEDING;
            default -> TEXTURE;
        };
    }

    private static final class MudoModel extends GeoModel<MudoEntity> {
        @Override
        public ResourceLocation getModelResource(MudoEntity entity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(MudoEntity entity) {
            return textureForSkin(entity);
        }

        @Override
        public ResourceLocation getAnimationResource(MudoEntity entity) {
            return ANIMATION;
        }
    }
}
