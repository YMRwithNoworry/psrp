const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");

const blockstateDir = path.join(root, "src/main/resources/assets/srparasites/blockstates");
const blockIds = fs.readdirSync(blockstateDir).filter(file => file.endsWith(".json")).map(file => file.replace(/\.json$/, "")).sort();
const source = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModBlocks.java");
const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");
const tabs = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModCreativeTabs.java");

function constantName(key) {
  let name = key.replace(/[^A-Za-z0-9]+/g, "_").replace(/^_+|_+$/g, "").toUpperCase();
  if (/^[0-9]/.test(name)) name = `BLOCK_${name}`;
  return name;
}

if (blockIds.length !== 263) throw new Error(`Expected 263 legacy blockstates, found ${blockIds.length}`);
for (const marker of [
  "DeferredRegister.createBlocks(SRPMain.MODID)",
  "DeferredRegister.createItems(SRPMain.MODID)",
  "CREATIVE_TAB_BLOCK_ITEMS",
  "BlockBehaviour.Properties.of().strength(1.0F, 3.0F)",
  "public static final DeferredBlock<LiquidBlock> DEADBLOOD = legacyFluid(\"deadblood\");",
  "new DeadBloodFluidBlock(ModFluids.DEADBLOOD_SOURCE.get(), legacyFluidProperties())",
  "new BlockItem(block.get(), new Item.Properties())"
]) {
  if (!source.includes(marker)) throw new Error(`ModBlocks missing marker: ${marker}`);
}
for (const id of blockIds) {
  const constant = constantName(id);
  const expected = id === "deadblood"
    ? `public static final DeferredBlock<LiquidBlock> ${constant} = legacyFluid("${id}");`
    : `public static final DeferredBlock<Block> ${constant} = legacyBlock("${id}");`;
  if (!source.includes(expected)) {
    throw new Error(`ModBlocks missing legacy block registration: ${id}`);
  }
  const blockstate = JSON.parse(read(`src/main/resources/assets/srparasites/blockstates/${id}.json`));
  if (!blockstate.variants || !blockstate.variants[""]) {
    throw new Error(`Blockstate is missing no-property fallback variant for baseline block: ${id}`);
  }
}
for (const key of ["alveoli", "parasitebush", "brusewood_door", "bruisewood_fence", "solid_alveoli_block", "trophy_void_orb"]) {
  if (!blockIds.includes(key)) throw new Error(`Missing expected legacy blockstate id: ${key}`);
  if (!source.includes(`legacyBlock("${key}")`)) throw new Error(`Missing expected legacy block registration: ${key}`);
}
if (!main.includes("import com.dhanantry.scapeandrunparasites.init.ModBlocks;")) throw new Error("SRPMain does not import ModBlocks");
if (!main.includes("ModBlocks.register(modEventBus);")) throw new Error("SRPMain does not register ModBlocks on the mod event bus");
if (!tabs.includes("ModBlocks.CREATIVE_TAB_BLOCK_ITEMS.forEach(item -> output.accept(item.get()))")) {
  throw new Error("Creative tab does not expose legacy block items");
}
console.log("Legacy block baseline registration verifier passed.");
