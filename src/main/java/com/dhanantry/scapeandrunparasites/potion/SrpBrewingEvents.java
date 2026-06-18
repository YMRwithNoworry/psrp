package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.init.ModBlocks;
import com.dhanantry.scapeandrunparasites.init.ModItems;
import com.dhanantry.scapeandrunparasites.init.ModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
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
        event.getBuilder().addRecipe(new AlveolarFluidToFearPotionRecipe());
        event.getBuilder().addRecipe(new FearPotionToSplashRecipe());
        event.getBuilder().addRecipe(new FearSplashToLingeringRecipe());
        event.getBuilder().addRecipe(new DiseasedSpongeToDeadBloodRecipe());
        event.getBuilder().addRecipe(new ThornshadeBerryToDecanterRecipe());
    }

    private static boolean isPotion(ItemStack input, Holder<Potion> potion) {
        if (input.isEmpty() || !input.is(Items.POTION)) {
            return false;
        }
        PotionContents contents = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return contents.is(potion);
    }

    private static boolean isSplashPotion(ItemStack input, Holder<Potion> potion) {
        if (input.isEmpty() || !input.is(Items.SPLASH_POTION)) {
            return false;
        }
        PotionContents contents = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return contents.is(potion);
    }

    private static boolean isWaterOrAwkwardPotion(ItemStack input) {
        if (input.isEmpty() || !input.is(Items.POTION)) {
            return false;
        }
        PotionContents contents = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return contents.is(Potions.WATER) || contents.is(Potions.AWKWARD);
    }

    private static final class AlveolarFluidToFearPotionRecipe implements IBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            return !input.isEmpty() && input.is(ModItems.ALVEOLAR_FLUID.get());
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return !ingredient.isEmpty() && ingredient.is(Items.FLINT);
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (!isInput(input) || !isIngredient(ingredient)) {
                return ItemStack.EMPTY;
            }
            return PotionContents.createItemStack(Items.POTION, ModPotions.FEAR);
        }
    }

    private static final class FearPotionToSplashRecipe implements IBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            return isPotion(input, ModPotions.FEAR);
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return !ingredient.isEmpty() && ingredient.is(Items.GUNPOWDER);
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (!isInput(input) || !isIngredient(ingredient)) {
                return ItemStack.EMPTY;
            }
            return PotionContents.createItemStack(Items.SPLASH_POTION, ModPotions.FEAR);
        }
    }

    private static final class FearSplashToLingeringRecipe implements IBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            return isSplashPotion(input, ModPotions.FEAR);
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return !ingredient.isEmpty() && ingredient.is(Items.DRAGON_BREATH);
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (!isInput(input) || !isIngredient(ingredient)) {
                return ItemStack.EMPTY;
            }
            return PotionContents.createItemStack(Items.LINGERING_POTION, ModPotions.FEAR);
        }
    }

    private static final class DiseasedSpongeToDeadBloodRecipe implements IBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            return isWaterOrAwkwardPotion(input);
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

    private static final class ThornshadeBerryToDecanterRecipe implements IBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            return isWaterOrAwkwardPotion(input);
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return !ingredient.isEmpty() && ingredient.is(ModItems.THORNSHADE_BERRY.get());
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (!isInput(input) || !isIngredient(ingredient)) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(ModItems.THORNSHADE_DECANTER.get());
        }
    }
}
