package com.dhanantry.scapeandrunparasites.client;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = SRPMain.MODID, value = Dist.CLIENT)
public final class IndeafClientEvents {
    private IndeafClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTickPre(ClientTickEvent.Pre event) {
        releaseMovementKeys();
    }

    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        releaseMovementKeys();
    }

    private static void releaseMovementKeys() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.player.hasEffect(ModEffects.INDEAF)) {
            return;
        }

        release(minecraft.options.keyUp);
        release(minecraft.options.keyDown);
        release(minecraft.options.keyLeft);
        release(minecraft.options.keyRight);
        release(minecraft.options.keyJump);
    }

    private static void release(KeyMapping key) {
        key.setDown(false);
    }
}
