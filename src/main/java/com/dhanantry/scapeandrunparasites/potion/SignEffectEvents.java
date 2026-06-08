package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class SignEffectEvents {
    public static final int LEGACY_DURATION_TICKS = 40;

    private SignEffectEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || !hasSignCharmAnywhere(player)) {
            return;
        }
        player.addEffect(new MobEffectInstance(ModEffects.THE_SIGN, LEGACY_DURATION_TICKS, 0, false, false));
    }

    private static boolean hasSignCharmAnywhere(Player player) {
        Inventory inventory = player.getInventory();
        return containsSignCharm(inventory.items) || containsSignCharm(inventory.offhand) || containsSignCharm(inventory.armor);
    }

    private static boolean containsSignCharm(Iterable<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty() && stack.is(ModItems.THE_SIGN_CHARM.get())) {
                return true;
            }
        }
        return false;
    }
}
