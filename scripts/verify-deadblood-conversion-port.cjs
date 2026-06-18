const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const json = (file) => JSON.parse(read(file));

const fluidBlock = read("src/main/java/com/dhanantry/scapeandrunparasites/block/DeadBloodFluidBlock.java");
const stainBlock = read("src/main/java/com/dhanantry/scapeandrunparasites/block/ParasiteStainBlock.java");
const rubbleBlock = read("src/main/java/com/dhanantry/scapeandrunparasites/block/ParasiteRubbleBlock.java");
const blocks = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModBlocks.java");
const audit = read("docs/DEADBLOOD_FLUID_AUDIT.md");

for (const marker of [
  "protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)",
  "protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)",
  "convertNeighboringLiquids(level, pos)",
  "for (Direction direction : Direction.values())",
  "fluidState.is(FluidTags.WATER)",
  "ModBlocks.PARASITESTAIN.get().mudState()",
  "fluidState.is(FluidTags.LAVA)",
  "ModBlocks.PARASITERUBBLE.get().obsidianState()"
]) {
  if (!fluidBlock.includes(marker)) throw new Error(`DeadBloodFluidBlock missing conversion marker: ${marker}`);
}

for (const [name, source, markers] of [
  ["ParasiteStainBlock", stainBlock, ["EnumProperty<Variant> VARIANT", "registerDefaultState(stateDefinition.any().setValue(VARIANT, Variant.DIRT))", "mudState()", "Variant.MUD", "SACKFLESH"]],
  ["ParasiteRubbleBlock", rubbleBlock, ["EnumProperty<Variant> VARIANT", "registerDefaultState(stateDefinition.any().setValue(VARIANT, Variant.FLESH))", "obsidianState()", "Variant.OBSIDIAN", "WEATHFSS"]]
]) {
  for (const marker of markers) {
    if (!source.includes(marker)) throw new Error(`${name} missing variant marker: ${marker}`);
  }
}

for (const marker of [
  "public static final DeferredBlock<ParasiteStainBlock> PARASITESTAIN = parasiteStainBlock(\"parasitestain\");",
  "public static final DeferredBlock<ParasiteRubbleBlock> PARASITERUBBLE = parasiteRubbleBlock(\"parasiterubble\");",
  "new ParasiteStainBlock(BlockBehaviour.Properties.of().strength(1.0F, 3.0F))",
  "new ParasiteRubbleBlock(BlockBehaviour.Properties.of().strength(1.0F, 3.0F))"
]) {
  if (!blocks.includes(marker)) throw new Error(`ModBlocks missing conversion target marker: ${marker}`);
}

const stainState = json("src/main/resources/assets/srparasites/blockstates/parasitestain.json");
const rubbleState = json("src/main/resources/assets/srparasites/blockstates/parasiterubble.json");
if (!stainState.variants["variant=mud"]) throw new Error("parasitestain blockstate is missing the runtime mud variant");
if (!rubbleState.variants["variant=obsidian"]) throw new Error("parasiterubble blockstate is missing the runtime obsidian variant");
if (!JSON.stringify(stainState.variants["variant=mud"]).includes("srparasites:block/parasitestain_mud")) {
  throw new Error("parasitestain mud variant does not point at the migrated Visceral Mud model");
}
if (!JSON.stringify(rubbleState.variants["variant=obsidian"]).includes("srparasites:block/parasiterubble_obsidian")) {
  throw new Error("parasiterubble obsidian variant does not point at the migrated Bleeding Obsidian model");
}

for (const marker of [
  "BlockFluid",
  "BlockParasiteStain$EnumType",
  "BlockParasiteRubble$EnumType",
  "Water/lava conversion is now implemented",
  "Visceral Mud",
  "Bleeding Obsidian"
]) {
  if (!audit.includes(marker)) throw new Error(`audit missing conversion marker: ${marker}`);
}

console.log("Dead Blood water/lava conversion verifier passed with wiki, bytecode, code, and resource markers.");
