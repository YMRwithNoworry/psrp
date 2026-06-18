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
  "event.getBuilder().addRecipe(new DiseasedSpongeToDeadBloodRecipe())",
  "implements IBrewingRecipe",
  "input.is(Items.POTION)",
  "DataComponents.POTION_CONTENTS",
  "PotionContents.EMPTY",
  "contents.is(Potions.WATER) || contents.is(Potions.AWKWARD)",
  "ingredient.is(ModBlocks.DISEASED_SPONGE.get().asItem())",
  "new ItemStack(ModItems.DEADBLOOD_FLUID.get())",
  "ItemStack.EMPTY"
]) {
  if (!brewing.includes(marker)) throw new Error(`SrpBrewingEvents missing legacy recipe marker: ${marker}`);
}

for (const marker of [
  "DISEASED_SPONGE = legacyBlock(\"diseased_sponge\")",
  "DEADBLOOD_FLUID = register(\"deadblood_fluid\""
]) {
  const source = marker.includes("DISEASED") ? blocks : items;
  if (!source.includes(marker)) throw new Error(`Registry source missing brewing dependency marker: ${marker}`);
}

for (const marker of [
  "Recipe$SpongeToDeadbloodRecipe",
  "srparasites:diseased_sponge",
  "srparasites:deadblood_fluid",
  "minecraft:water",
  "minecraft:awkward",
  "Water Bottle or Awkward Potion"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing deadblood brewing evidence marker: ${marker}`);
}

console.log("Dead blood brewing verifier passed with legacy SpongeToDeadbloodRecipe markers.");
