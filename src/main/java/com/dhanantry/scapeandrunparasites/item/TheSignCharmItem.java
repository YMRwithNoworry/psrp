package com.dhanantry.scapeandrunparasites.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class TheSignCharmItem extends Item {
    public static final int LEGACY_STACK_SIZE = 1;

    public TheSignCharmItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.srparasites.the_sign_charm.red").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("tooltip.srparasites.the_sign_charm.white").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("tooltip.srparasites.the_sign_charm.gray").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
