package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.init.ModFluids;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = SRPMain.MODID, value = Dist.CLIENT)
public final class DeadBloodClientEvents {
    public static final float LEGACY_FOG_RED = 0.08F;
    public static final float LEGACY_FOG_GREEN = 0.2F;
    public static final float LEGACY_FOG_BLUE = 0.07F;
    public static final float LEGACY_FOG_NEAR = 0.0F;
    public static final float LEGACY_FOG_FAR = 2.5F;
    public static final float LEGACY_OVERLAY_ALPHA = 0.45F;
    public static final double LEGACY_SWIM_ACCELERATION = 0.045D;
    public static final double LEGACY_MAX_UPWARD_SPEED = 0.12D;
    private static final ResourceLocation FLOW_OVERLAY = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "textures/blocks/deadblood_flowing.png");

    private DeadBloodClientEvents() {
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (!isEyeInDeadBlood(event.getCamera())) {
            return;
        }
        event.setRed(LEGACY_FOG_RED);
        event.setGreen(LEGACY_FOG_GREEN);
        event.setBlue(LEGACY_FOG_BLUE);
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (!isEyeInDeadBlood(event.getCamera())) {
            return;
        }
        event.setNearPlaneDistance(LEGACY_FOG_NEAR);
        event.setFarPlaneDistance(LEGACY_FOG_FAR);
        event.setFogShape(FogShape.SPHERE);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockScreenEffect(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.WATER && isEyeInDeadBlood(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !isEyeInDeadBlood(minecraft.player)) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, LEGACY_OVERLAY_ALPHA);
        guiGraphics.blit(FLOW_OVERLAY, 0, 0, 0, 0.0F, 0.0F, width, height, width, height);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !isEyeInDeadBlood(minecraft.player) || !minecraft.options.keyJump.isDown()) {
            return;
        }
        double y = Math.min(minecraft.player.getDeltaMovement().y + LEGACY_SWIM_ACCELERATION, LEGACY_MAX_UPWARD_SPEED);
        minecraft.player.setDeltaMovement(minecraft.player.getDeltaMovement().x, y, minecraft.player.getDeltaMovement().z);
    }

    private static boolean isEyeInDeadBlood(Camera camera) {
        return camera.getEntity() != null && isEyeInDeadBlood(camera.getEntity());
    }

    private static boolean isEyeInDeadBlood(Entity entity) {
        Level level = entity.level();
        BlockPos eyePos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
        FluidState eyeState = level.getFluidState(eyePos);
        if (ModFluids.isDeadBlood(eyeState)) {
            return true;
        }
        BlockPos justBelowEye = BlockPos.containing(entity.getX(), entity.getEyeY() - 0.0625D, entity.getZ());
        return ModFluids.isDeadBlood(level.getFluidState(justBelowEye));
    }
}
