package com.dhanantry.scapeandrunparasites.item;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public final class DeadBloodFluidItem extends Item {
    public static final int USE_DURATION_TICKS = 32;
    public static final int EFFECT_DURATION_TICKS = 600;
    public static final int LEGACY_STACK_SIZE = 64;

    public DeadBloodFluidItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(ModEffects.VIRAL, EFFECT_DURATION_TICKS, 1));
        }
        return consumeBottle(stack, livingEntity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION_TICKS;
    }

    private static ItemStack consumeBottle(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (stack.isEmpty()) {
                    return bottle;
                }
                if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
            }
            return stack;
        }

        stack.shrink(1);
        return stack;
    }
}
