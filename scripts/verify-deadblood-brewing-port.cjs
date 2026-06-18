const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");

const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");
const brewing = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpBrewingEvents.java");
const blocks = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModBlocks.java");
const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");

for (const marker of [
  "import com.dhanantry.scapeandrunparasites.potion.SrpBrewingEvents;",
  "NeoForge.EVENT_BUS.register(SrpBrewingEvents.class);"
]) {
  if (!main.includes(marker)) throw new Error(`SRPMain missing brewing event registration marker: ${marker}`);
}

for (const marker of [
  "RegisterBrewingRecipesEvent",
  "event.getBuilder().addRecipe(new AlveolarFluidToFearPotionRecipe())",
  "event.getBuilder().addRecipe(new FearPotionToSplashRecipe())",
  "event.getBuilder().addRecipe(new FearSplashToLingeringRecipe())",
  "event.getBuilder().addRecipe(new DiseasedSpongeToDeadBloodRecipe())",
  "event.getBuilder().addRecipe(new ThornshadeBerryToDecanterRecipe())",
  "implements IBrewingRecipe",
  "input.is(ModItems.ALVEOLAR_FLUID.get())",
  "ingredient.is(Items.FLINT)",
  "PotionContents.createItemStack(Items.POTION, ModPotions.FEAR)",
  "isPotion(input, ModPotions.FEAR)",
  "ingredient.is(Items.GUNPOWDER)",
  "PotionContents.createItemStack(Items.SPLASH_POTION, ModPotions.FEAR)",
  "isSplashPotion(input, ModPotions.FEAR)",
  "ingredient.is(Items.DRAGON_BREATH)",
  "PotionContents.createItemStack(Items.LINGERING_POTION, ModPotions.FEAR)",
  "input.is(Items.POTION)",
  "DataComponents.POTION_CONTENTS",
  "PotionContents.EMPTY",
  "contents.is(Potions.WATER) || contents.is(Potions.AWKWARD)",
  "ingredient.is(ModBlocks.DISEASED_SPONGE.get().asItem())",
  "new ItemStack(ModItems.DEADBLOOD_FLUID.get())",
  "ingredient.is(ModItems.THORNSHADE_BERRY.get())",
  "new ItemStack(ModItems.THORNSHADE_DECANTER.get())",
  "ItemStack.EMPTY"
]) {
  if (!brewing.includes(marker)) throw new Error(`SrpBrewingEvents missing legacy recipe marker: ${marker}`);
}

for (const marker of [
  "ALVEOLAR_FLUID = register(\"alveolar_fluid\"",
  "DISEASED_SPONGE = legacyBlock(\"diseased_sponge\")",
  "DEADBLOOD_FLUID = register(\"deadblood_fluid\"",
  "THORNSHADE_BERRY = legacyItem(\"thornshade_berry\"",
  "THORNSHADE_DECANTER = register(\"thornshade_decanter\""
]) {
  const source = marker.includes("DISEASED") ? blocks : items;
  if (!source.includes(marker)) throw new Error(`Registry source missing brewing dependency marker: ${marker}`);
}

for (const marker of [
  "Recipe$BaseToFearRecipe",
  "Recipe$FearToSplashRecipe",
  "Recipe$FearSplashToLingeringRecipe",
  "Recipe$SpongeToDeadbloodRecipe",
  "Recipe$BerryToThornshadeDecanterRecipe",
  "srparasites:alveolar_fluid",
  "srparasites:fear",
  "srparasites:diseased_sponge",
  "srparasites:deadblood_fluid",
  "srparasites:thornshade_berry",
  "srparasites:thornshade_decanter",
  "minecraft:water",
  "minecraft:awkward",
  "Water Bottle or Awkward Potion",
  "flint produces a normal",
  "dragon breath produces lingering Fear"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing deadblood brewing evidence marker: ${marker}`);
}

console.log("SRP custom brewing verifier passed with legacy Recipe markers.");
