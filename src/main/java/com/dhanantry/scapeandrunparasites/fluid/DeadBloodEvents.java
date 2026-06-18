package com.dhanantry.scapeandrunparasites.fluid;

import com.dhanantry.scapeandrunparasites.init.ModFluids;
import com.dhanantry.scapeandrunparasites.init.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class DeadBloodEvents {
    private DeadBloodEvents() {
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.GLASS_BOTTLE)) {
            return;
        }

        Player player = event.getEntity();
        Level level = event.getLevel();
        HitResult hit = player.pick(player.blockInteractionRange(), 1.0F, true);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockHitResult blockHit = (BlockHitResult) hit;
        if (!ModFluids.isDeadBlood(level.getFluidState(blockHit.getBlockPos()))) {
            return;
        }

        if (!level.isClientSide) {
            fillDeadBloodBottle(player, stack);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 0.9F, 1.0F);
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }

    private static void fillDeadBloodBottle(Player player, ItemStack emptyBottleStack) {
        ItemStack deadBlood = new ItemStack(ModItems.DEADBLOOD_FLUID.get());
        if (!player.getAbilities().instabuild) {
            emptyBottleStack.shrink(1);
        }

        if (emptyBottleStack.isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, deadBlood);
        } else if (!player.getInventory().add(deadBlood)) {
            player.drop(deadBlood, false);
        }
    }
}
