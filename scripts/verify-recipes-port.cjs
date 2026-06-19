const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const recipeDir = path.join(root, "src/main/resources/data/srparasites/recipe");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const parse = (file) => JSON.parse(fs.readFileSync(file, "utf8"));

const itemsSource = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
const blocksSource = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModBlocks.java");
const migrationScript = read("scripts/migrate-legacy-assets.cjs");
const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");

const recipeFiles = fs.readdirSync(recipeDir).filter((name) => name.endsWith(".json")).sort();
if (recipeFiles.length !== 248) {
  throw new Error(`Expected 248 migrated legacy recipe files, found ${recipeFiles.length}`);
}

for (const marker of [
  "normalizeRecipeResult",
  "recipe.result.id = recipe.result.item",
  "delete recipe.result.item"
]) {
  if (!migrationScript.includes(marker)) throw new Error(`migrate-legacy-assets.cjs missing 1.21 recipe result marker: ${marker}`);
}

const knownSrpIds = new Set();
for (const match of itemsSource.matchAll(/(?:basic|legacyItem|register|livingWeapon|livingArmor)\(\"([a-z0-9_]+)\"/g)) {
  knownSrpIds.add(match[1]);
}
for (const match of blocksSource.matchAll(/(?:legacy(?:Block|Fluid|Door|TrapDoor|Stairs|Slab|Fence|Pane|Wall|Pillar)|parasiteStainBlock|parasiteRubbleBlock)\(\"([a-z0-9_]+)\"/g)) {
  knownSrpIds.add(match[1]);
}

function walk(value, visit) {
  if (Array.isArray(value)) {
    for (const entry of value) walk(entry, visit);
    return;
  }
  if (!value || typeof value !== "object") return;
  visit(value);
  for (const child of Object.values(value)) walk(child, visit);
}

const unresolved = new Set();
for (const name of recipeFiles) {
  const recipe = parse(path.join(recipeDir, name));
  if (!recipe.type || !recipe.type.startsWith("minecraft:crafting_")) {
    throw new Error(`${name} has an unsupported migrated recipe type: ${recipe.type}`);
  }
  if (!recipe.result || typeof recipe.result.id !== "string") {
    throw new Error(`${name} result must use the 1.21 ItemStack id field`);
  }
  if (Object.prototype.hasOwnProperty.call(recipe.result, "item")) {
    throw new Error(`${name} still uses legacy result.item`);
  }

  walk(recipe, (node) => {
    for (const key of ["item", "id"]) {
      if (typeof node[key] === "string" && node[key].startsWith("srparasites:")) {
        const id = node[key].slice("srparasites:".length);
        if (!knownSrpIds.has(id)) unresolved.add(`${name}: ${node[key]}`);
      }
    }
  });
}

if (unresolved.size) {
  throw new Error(`Recipe references are not registered in ModItems/ModBlocks:\n${[...unresolved].slice(0, 20).join("\n")}`);
}

const livingCore = parse(path.join(recipeDir, "livingcore.json"));
if (livingCore.result.id !== "srparasites:living_core" || livingCore.result.count !== 1) {
  throw new Error("livingcore.json no longer outputs one srparasites:living_core");
}

const livingSword = parse(path.join(recipeDir, "wsword.json"));
if (livingSword.result.id !== "srparasites:weapon_sword") {
  throw new Error("wsword.json no longer outputs the legacy living sword item");
}

const hijackedSword = parse(path.join(recipeDir, "hijacked_iron_sword.json"));
if (hijackedSword.result.id !== "srparasites:hijacked_iron_sword") {
  throw new Error("hijacked_iron_sword.json no longer outputs the hijacked iron sword");
}

if (!audit.includes("248 migrated legacy recipe JSON files")) {
  throw new Error("Audit missing recipe migration marker: 248 migrated legacy recipe JSON files");
}
if (!/1\.21[\s\S]*`result\.id`/.test(audit)) {
  throw new Error("Audit missing recipe migration marker: 1.21 `result.id`");
}
if (!audit.includes("Living Core and living weapon recipes")) {
  throw new Error("Audit missing recipe migration marker: Living Core and living weapon recipes");
}

console.log("Recipe data verifier passed with 248 migrated 1.21 recipe JSON files.");
