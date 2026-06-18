package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModBlocks;
import com.dhanantry.scapeandrunparasites.init.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

public final class SrpBrewingEvents {
    private SrpBrewingEvents() {
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new DiseasedSpongeToDeadBloodRecipe());
    }

    private static final class DiseasedSpongeToDeadBloodRecipe implements IBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            if (input.isEmpty() || !input.is(Items.POTION)) {
                return false;
            }
            PotionContents contents = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return contents.is(Potions.WATER) || contents.is(Potions.AWKWARD);
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return !ingredient.isEmpty() && ingredient.is(ModBlocks.DISEASED_SPONGE.get().asItem());
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (!isInput(input) || !isIngredient(ingredient)) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(ModItems.DEADBLOOD_FLUID.get());
        }
    }
}
