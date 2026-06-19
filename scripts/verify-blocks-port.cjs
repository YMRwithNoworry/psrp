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

function readJson(file) {
  return JSON.parse(read(file));
}

function collectLegacyProperties(blockstate) {
  const props = new Map();
  const add = (name, value) => {
    if (!props.has(name)) props.set(name, new Set());
    props.get(name).add(String(value));
  };

  for (const key of Object.keys(blockstate.srparasites_legacy_variants || {})) {
    if (["", "normal", "inventory", "fluid", "stage"].includes(key)) continue;
    for (const part of key.split(",")) {
      const split = part.indexOf("=");
      if (split > 0) add(part.slice(0, split), part.slice(split + 1));
    }
  }

  function walkWhen(when) {
    if (!when || typeof when !== "object") return;
    for (const [key, raw] of Object.entries(when)) {
      if ((key === "OR" || key === "AND") && Array.isArray(raw)) {
        raw.forEach(walkWhen);
      } else {
        add(key, typeof raw === "boolean" ? String(raw) : raw);
      }
    }
  }
  for (const entry of blockstate.srparasites_legacy_multipart || []) walkWhen(entry.when);
  return props;
}

function hasAll(set, values) {
  return values.every(value => set.has(value));
}

function expectedFactory(id, blockstate) {
  const props = collectLegacyProperties(blockstate);
  const names = new Set(props.keys());
  if (blockstate.srparasites_legacy_variants && blockstate.srparasites_legacy_variants.fluid) return "legacyFluid";
  if (hasAll(names, ["facing", "half", "hinge", "open", "powered"])) return "legacyDoor";
  if (id.endsWith("_trapdoor") && hasAll(names, ["facing", "half", "open"]) && !names.has("hinge")) return "legacyTrapDoor";
  if (hasAll(names, ["facing", "half", "shape"])) return "legacyStairs";
  if (id.endsWith("_slab") && names.has("half") && (!props.has("variant") || [...props.get("variant")].every(value => value === "default"))) return "legacySlab";
  if (id.endsWith("_fence") && hasAll(names, ["north", "east", "south", "west"]) && !names.has("up")) return "legacyFence";
  if (id.endsWith("_pane") && hasAll(names, ["north", "east", "south", "west"]) && !names.has("up")) return "legacyPane";
  if (id.endsWith("_wall") && hasAll(names, ["north", "east", "south", "west", "up"])) return "legacyWall";
  if (names.size === 1 && names.has("axis") && !props.get("axis").has("none")) return "legacyPillar";
  return "legacyBlock";
}

function factoryBlockType(factory) {
  if (factory === "legacyFluid") return "LiquidBlock";
  if (["legacyDoor", "legacyTrapDoor", "legacyStairs", "legacySlab", "legacyFence", "legacyPane", "legacyWall", "legacyPillar", "legacyBlock"].includes(factory)) return "Block";
  return "Block";
}

function assertVariant(blockstate, id, key) {
  if (!blockstate.variants || !blockstate.variants[key]) {
    throw new Error(`Blockstate ${id} missing variant: ${key}`);
  }
}

function assertMultipart(blockstate, id) {
  if (!Array.isArray(blockstate.multipart) || blockstate.multipart.length === 0) {
    throw new Error(`Blockstate ${id} missing multipart model definitions`);
  }
}

function verifyModernBlockstate(id, blockstate, factory) {
  if (factory === "legacyBlock" || factory === "legacyFluid") {
    assertVariant(blockstate, id, "");
    return;
  }
  if (factory === "legacyDoor") {
    assertVariant(blockstate, id, "facing=east,half=lower,hinge=left,open=false,powered=false");
    assertVariant(blockstate, id, "facing=north,half=upper,hinge=right,open=true,powered=true");
    return;
  }
  if (factory === "legacyTrapDoor") {
    assertVariant(blockstate, id, "facing=east,half=top,open=false");
    assertVariant(blockstate, id, "facing=north,half=bottom,open=true");
    return;
  }
  if (factory === "legacyStairs") {
    assertVariant(blockstate, id, "facing=east,half=bottom,shape=straight");
    assertVariant(blockstate, id, "facing=north,half=top,shape=inner_left");
    return;
  }
  if (factory === "legacySlab") {
    assertVariant(blockstate, id, "type=bottom");
    assertVariant(blockstate, id, "type=top");
    assertVariant(blockstate, id, "type=double");
    return;
  }
  if (factory === "legacyFence" || factory === "legacyPane") {
    assertMultipart(blockstate, id);
    return;
  }
  if (factory === "legacyWall") {
    assertMultipart(blockstate, id);
    return;
  }
  if (factory === "legacyPillar") {
    assertVariant(blockstate, id, "axis=x");
    assertVariant(blockstate, id, "axis=y");
    assertVariant(blockstate, id, "axis=z");
    return;
  }
}

if (blockIds.length !== 263) throw new Error(`Expected 263 legacy blockstates, found ${blockIds.length}`);
for (const marker of [
  "DeferredRegister.createBlocks(SRPMain.MODID)",
  "DeferredRegister.createItems(SRPMain.MODID)",
  "CREATIVE_TAB_BLOCK_ITEMS",
  "legacyProperties()",
  "legacyWoodProperties()",
  "legacyGlassProperties()",
  "legacyFluidProperties()",
  "legacyDoor",
  "legacyTrapDoor",
  "legacyStairs",
  "legacySlab",
  "legacyFence",
  "legacyPane",
  "legacyWall",
  "legacyPillar",
  "blockWithItem",
  "legacyBlock",
  "new DoorBlock(BlockSetType.OAK",
  "new TrapDoorBlock(BlockSetType.OAK",
  "new StairBlock(Blocks.STONE.defaultBlockState()",
  "new SlabBlock(",
  "new FenceBlock(",
  "new IronBarsBlock(",
  "new WallBlock(",
  "new RotatedPillarBlock(",
  "public static final DeferredBlock<LiquidBlock> DEADBLOOD = legacyFluid(\"deadblood\");",
  "public static final DeferredBlock<ParasiteStainBlock> PARASITESTAIN = parasiteStainBlock(\"parasitestain\");",
  "public static final DeferredBlock<ParasiteRubbleBlock> PARASITERUBBLE = parasiteRubbleBlock(\"parasiterubble\");",
  "new DeadBloodFluidBlock(ModFluids.DEADBLOOD_SOURCE.get(), legacyFluidProperties())",
  "new BlockItem(block.get(), new Item.Properties())"
]) {
  if (!source.includes(marker)) throw new Error(`ModBlocks missing marker: ${marker}`);
}
for (const id of blockIds) {
  const blockstate = readJson(`src/main/resources/assets/srparasites/blockstates/${id}.json`);
  const factory = expectedFactory(id, blockstate);
  const blockType = factoryBlockType(factory);
  const constant = constantName(id);

  const expected = id === "deadblood"
    ? `public static final DeferredBlock<LiquidBlock> ${constant} = legacyFluid("${id}");`
    : id === "parasitestain"
      ? `public static final DeferredBlock<ParasiteStainBlock> ${constant} = parasiteStainBlock("${id}");`
      : id === "parasiterubble"
        ? `public static final DeferredBlock<ParasiteRubbleBlock> ${constant} = parasiteRubbleBlock("${id}");`
        : `public static final DeferredBlock<${blockType}> ${constant} = ${factory}("${id}");`;
  if (!source.includes(expected)) {
    throw new Error(`ModBlocks missing registration for ${id} (expected factory ${factory} with type ${blockType})`);
  }
  verifyModernBlockstate(id, blockstate, factory);
}
for (const key of ["alveoli", "parasitebush", "brusewood_door", "bruisewood_fence", "solid_alveoli_block", "trophy_void_orb"]) {
  if (!blockIds.includes(key)) throw new Error(`Missing expected legacy blockstate id: ${key}`);
  const blockstate = readJson(`src/main/resources/assets/srparasites/blockstates/${key}.json`);
  const factory = expectedFactory(key, blockstate);
  if (key === "brusewood_door" && !source.includes(`legacyDoor("${key}")`)) throw new Error(`Missing structural door registration: ${key}`);
  if (key === "bruisewood_fence" && !source.includes(`legacyFence("${key}")`)) throw new Error(`Missing structural fence registration: ${key}`);
}
if (!main.includes("import com.dhanantry.scapeandrunparasites.init.ModBlocks;")) throw new Error("SRPMain does not import ModBlocks");
if (!main.includes("ModBlocks.register(modEventBus);")) throw new Error("SRPMain does not register ModBlocks on the mod event bus");
if (!tabs.includes("ModBlocks.CREATIVE_TAB_BLOCK_ITEMS.forEach(item -> output.accept(item.get()))")) {
  throw new Error("Creative tab does not expose legacy block items");
}
console.log("Legacy block registration verifier passed with standard structural block behavior.");
