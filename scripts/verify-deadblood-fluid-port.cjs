const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const json = (file) => JSON.parse(read(file));

const fluids = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModFluids.java");
const blocks = read("src/main/java/com/dhanantry/scapeandrunparasites/block/DeadBloodFluidBlock.java");
const bottleEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/fluid/DeadBloodEvents.java");
const client = read("src/main/java/com/dhanantry/scapeandrunparasites/client/DeadBloodClientEvents.java");
const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
const deadBloodItem = read("src/main/java/com/dhanantry/scapeandrunparasites/item/DeadBloodFluidItem.java");
const alveolarItem = read("src/main/java/com/dhanantry/scapeandrunparasites/item/AlveolarFluidItem.java");
const audit = read("docs/DEADBLOOD_FLUID_AUDIT.md");

for (const marker of [
  "descriptionId(\"fluid.deadblood\")",
  ".density(3000)",
  ".viscosity(1500)",
  ".temperature(310)",
  "DEADBLOOD_SOURCE",
  "DEADBLOOD_FLOWING",
  ".block(() -> ModBlocks.DEADBLOOD.get())",
  ".bucket(() -> ModItems.DEADBLOOD_FLUID.get())"
]) {
  if (!fluids.includes(marker)) throw new Error(`ModFluids missing legacy marker: ${marker}`);
}

for (const marker of [
  "LEGACY_PARASITE_HEAL = 1.0F",
  "LEGACY_HORIZONTAL_DRAG = 0.85D",
  "LEGACY_FALLING_DRAG = 0.92D",
  "LEGACY_BASE_DAMAGE = 0.1F",
  "LEGACY_HEAD_CORROSIVE_DURATION = 100",
  "LEGACY_HEAD_VIRAL_DURATION = 200",
  "LEGACY_REAPPLY_THRESHOLD = 20",
  "parasite.heal(LEGACY_PARASITE_HEAL)",
  "hurtWithViralScaling(living, LEGACY_BASE_DAMAGE)",
  "refreshIfLow(living, ModEffects.CORROSIVE, LEGACY_HEAD_CORROSIVE_DURATION, 0)",
  "refreshIfLow(living, ModEffects.VIRAL, LEGACY_HEAD_VIRAL_DURATION, 1)"
]) {
  if (!blocks.includes(marker)) throw new Error(`DeadBloodFluidBlock missing legacy marker: ${marker}`);
}

for (const marker of [
  "Items.GLASS_BOTTLE",
  "ModFluids.isDeadBlood(level.getFluidState(blockHit.getBlockPos()))",
  "new ItemStack(ModItems.DEADBLOOD_FLUID.get())",
  "SoundEvents.BOTTLE_FILL",
  "SoundSource.PLAYERS",
  "0.9F, 1.0F"
]) {
  if (!bottleEvents.includes(marker)) throw new Error(`DeadBloodEvents missing bottle marker: ${marker}`);
}

for (const marker of [
  "LEGACY_FOG_RED = 0.08F",
  "LEGACY_FOG_GREEN = 0.2F",
  "LEGACY_FOG_BLUE = 0.07F",
  "LEGACY_FOG_FAR = 2.5F",
  "LEGACY_OVERLAY_ALPHA = 0.45F",
  "LEGACY_SWIM_ACCELERATION = 0.045D",
  "LEGACY_MAX_UPWARD_SPEED = 0.12D",
  "RenderBlockScreenEffectEvent.OverlayType.WATER",
  "ViewportEvent.ComputeFogColor",
  "ViewportEvent.RenderFog",
  "RenderGuiEvent.Post",
  "ClientTickEvent.Post",
  "textures/blocks/deadblood_flowing.png"
]) {
  if (!client.includes(marker)) throw new Error(`DeadBloodClientEvents missing client marker: ${marker}`);
}

for (const marker of [
  "ALVEOLAR_FLUID = register(\"alveolar_fluid\"",
  "DEADBLOOD_FLUID = register(\"deadblood_fluid\""
]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing fluid item marker: ${marker}`);
}

for (const [name, source, markers] of [
  ["DeadBloodFluidItem", deadBloodItem, ["USE_DURATION_TICKS = 32", "EFFECT_DURATION_TICKS = 600", "LEGACY_STACK_SIZE = 64", "new MobEffectInstance(ModEffects.VIRAL, EFFECT_DURATION_TICKS, 1)", "UseAnim.DRINK", "Items.GLASS_BOTTLE"]],
  ["AlveolarFluidItem", alveolarItem, ["USE_DURATION_TICKS = 32", "EFFECT_DURATION_TICKS = 600", "LEGACY_STACK_SIZE = 1", "MobEffects.MOVEMENT_SPEED", "MobEffects.DIG_SPEED", "new MobEffectInstance(ModEffects.VIRAL, EFFECT_DURATION_TICKS, 2)", "UseAnim.DRINK", "Items.GLASS_BOTTLE"]]
]) {
  for (const marker of markers) {
    if (!source.includes(marker)) throw new Error(`${name} missing marker: ${marker}`);
  }
}

for (const marker of [
  "https://scape-and-run-parasites.fandom.com/wiki/Dead_Blood",
  "https://scape-and-run-parasites.fandom.com/wiki/Dead_Blood_Fluid",
  "https://scape-and-run-parasites.fandom.com/wiki/Alveoli",
  "Water/lava conversion remains a documented gap"
]) {
  if (!audit.includes(marker)) throw new Error(`audit missing wiki/evidence marker: ${marker}`);
}

const blockstate = json("src/main/resources/assets/srparasites/blockstates/deadblood.json");
if (blockstate.variants[""].custom.fluid !== "deadblood") throw new Error("deadblood blockstate does not preserve the legacy fluid id");
if (json("src/main/resources/assets/srparasites/models/item/deadblood_fluid.json").textures.layer0 !== "srparasites:items/deadblood_bottle") {
  throw new Error("deadblood bottle item model points at the wrong legacy texture");
}
if (json("src/main/resources/assets/srparasites/models/item/alveolar_fluid.json").textures.layer0 !== "srparasites:items/alveolar_fluid") {
  throw new Error("alveolar item model points at the wrong legacy texture");
}

console.log("Dead blood and alveolar fluid verifier passed with legacy and wiki behavior markers.");
