const fs = require("node:fs");
const path = require("node:path");
const os = require("node:os");
const childProcess = require("node:child_process");

const root = path.resolve(__dirname, "..");
const jar = process.argv[2] ? path.resolve(process.argv[2]) : path.join(root, "杂物", "[逃逸：寄生体] SRParasites-1.10.6.jar");
const outResources = path.join(root, "src", "main", "resources");
const outAssets = path.join(outResources, "assets", "srparasites");
const outMinecraftAssets = path.join(outResources, "assets", "minecraft");
const outData = path.join(outResources, "data", "srparasites");
const tmp = fs.mkdtempSync(path.join(os.tmpdir(), "srp-assets-"));
const generatedBlockModels = new Map();

if (!fs.existsSync(jar)) {
  throw new Error(`Legacy jar not found: ${jar}`);
}

function run(command, args, cwd) {
  childProcess.execFileSync(command, args, { cwd, stdio: "inherit" });
}

function copyDir(src, dest, transformText) {
  if (!fs.existsSync(src)) return;
  for (const entry of fs.readdirSync(src, { withFileTypes: true })) {
    const from = path.join(src, entry.name);
    const to = path.join(dest, entry.name);
    if (entry.isDirectory()) {
      copyDir(from, to, transformText);
      continue;
    }
    fs.mkdirSync(path.dirname(to), { recursive: true });
    if (transformText && entry.name.endsWith(".json")) {
      let text = fs.readFileSync(from, "utf8");
      text = transformText(text, from, entry.name);
      fs.writeFileSync(to, text, "utf8");
    } else {
      fs.copyFileSync(from, to);
    }
  }
}

function readLang(file) {
  const result = {};
  const text = fs.readFileSync(file, "utf8").replace(/^\uFEFF/, "");
  for (const rawLine of text.split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith("#") || !line.includes("=")) continue;
    const split = line.indexOf("=");
    const key = line.slice(0, split).trim();
    const value = line.slice(split + 1).trim();
    result[key] = value;
    if ((key.startsWith("item.srparasites.") || key.startsWith("block.srparasites.") || key.startsWith("entity.srparasites.")) && key.endsWith(".name")) {
      result[key.slice(0, -5)] = value;
    }
  }
  return result;
}

const MODERN_POTION_NAMES = [
  "srparasites:fear",
  "srparasites:antimall",
  "srparasites:corrosive",
  "srparasites:viral",
  "srparasites:vomit",
  "srparasites:rage",
  "srparasites:repel",
  "srparasites:senses",
  "srparasites:debar",
  "srparasites:link",
  "srparasites:pivot",
  "srparasites:jugg",
  "srparasites:parate",
  "srparasites:primitive",
  "srparasites:adapted",
  "srparasites:pure",
  "srparasites:crude",
  "srparasites:feral",
  "srparasites:nexus",
  "srparasites:braining",
  "srparasites:novision",
  "srparasites:indeaf",
  "srparasites:overheating",
  "srparasites:conta",
  "srparasites:muscleout",
  "srparasites:effectpos",
  "srparasites:effectneg",
  "srparasites:thornshade_thorns",
  "srparasites:the_sign"
];

function stripFormatting(value) {
  return typeof value === "string" ? value.replace(/§[0-9A-FK-OR]/gi, "") : value;
}

function potionDisplayName(lang, potionName) {
  const id = potionName.slice("srparasites:".length);
  return stripFormatting(lang[`mob_effect.srparasites:${id}`])
    || stripFormatting(lang[`effect.srparasites.${id}`])
    || id.split("_").map(part => part.charAt(0).toUpperCase() + part.slice(1)).join(" ");
}

function addModernPotionLanguage(lang) {
  const prefixes = [
    "potion.effect.",
    "splash_potion.effect.",
    "lingering_potion.effect.",
    "tipped_arrow.effect.",
    "item.minecraft.potion.effect.",
    "item.minecraft.splash_potion.effect.",
    "item.minecraft.lingering_potion.effect.",
    "item.minecraft.tipped_arrow.effect."
  ];

  for (const potionName of MODERN_POTION_NAMES) {
    const displayName = potionDisplayName(lang, potionName);
    for (const prefix of prefixes) {
      lang[`${prefix}${potionName}`] = lang[`${prefix}${potionName}`] || displayName;
    }
  }
}

function writeLangs(srcDir) {
  const langOut = path.join(outAssets, "lang");
  fs.mkdirSync(langOut, { recursive: true });
  for (const file of fs.readdirSync(srcDir)) {
    if (!file.endsWith(".lang")) continue;
    const lang = readLang(path.join(srcDir, file));
    lang["itemGroup.srparasites"] = lang["itemGroup.srparasites"] || "Scape and Run: Parasites";
    lang["tooltip.srparasites.srpkills"] = "Assimilated life: %s / %s";
    lang["tooltip.srparasites.srphits"] = "Assimilated damage: %s / %s";
    lang["tooltip.srparasites.reach"] = "Legacy reach: +%s";
    addModernPotionLanguage(lang);
    const outName = file.replace(/\.lang$/, ".json").toLowerCase();
    fs.writeFileSync(path.join(langOut, outName), `${JSON.stringify(lang, null, 2)}\n`, "utf8");
  }
}

function normalizeLegacyJsonText(text) {
  return text
    .replace(/SRParasites:/g, "srparasites:")
    .replace(/\"type\"\s*:\s*\"item\"/g, '"type": "minecraft:item"')
    .replace(/\"function\"\s*:\s*\"set_count\"/g, '"function": "minecraft:set_count"')
    .replace(/\"condition\"\s*:\s*\"killed_by_player\"/g, '"condition": "minecraft:killed_by_player"')
    .replace(/\"condition\"\s*:\s*\"random_chance_with_looting\"/g, '"condition": "minecraft:random_chance_with_enchanted_bonus"');
}

function cloneJson(value) {
  return JSON.parse(JSON.stringify(value));
}

function generatedBlockModelName(name) {
  return name.replace(/[^a-z0-9_./-]+/gi, "_").replace(/[./-]+/g, "_").replace(/^_+|_+$/g, "").toLowerCase();
}

function generatedBlockModelRef(name, model) {
  const safeName = generatedBlockModelName(name);
  generatedBlockModels.set(safeName, model);
  return `srparasites:block/generated/${safeName}`;
}

function modernBlockModelReference(model, blockId) {
  if (typeof model !== "string") return model;
  if (model === "forge:fluid") {
    return generatedBlockModelRef(`${blockId}_fluid_fallback`, {
      "parent": "minecraft:block/cube_all",
      "textures": {
        "all": `srparasites:blocks/${blockId}_still`
      }
    });
  }
  if (/^vine(?:_|$)/.test(model)) {
    return generatedBlockModelRef(`${blockId}_vine_fallback`, {
      "parent": "minecraft:block/cross",
      "textures": {
        "cross": "minecraft:block/vine"
      }
    });
  }

  const split = model.includes(":") ? model.split(":", 2) : ["minecraft", model];
  const namespace = split[0];
  const modelPath = split[1];
  if (modelPath.startsWith("block/") || modelPath.startsWith("item/") || modelPath.startsWith("builtin/")) {
    return `${namespace}:${modelPath}`;
  }
  return `${namespace}:block/${modelPath}`;
}

function normalizeVariantModelReferences(value, blockId) {
  if (Array.isArray(value)) {
    return value.map(entry => normalizeVariantModelReferences(entry, blockId));
  }
  if (!value || typeof value !== "object") {
    return value;
  }
  const out = {};
  for (const [key, raw] of Object.entries(value)) {
    if (key === "model") {
      out[key] = modernBlockModelReference(raw, blockId);
    } else {
      out[key] = normalizeVariantModelReferences(raw, blockId);
    }
  }
  return out;
}

function applyLegacyDefaults(value, defaults) {
  if (Array.isArray(value)) {
    return value.map(entry => applyLegacyDefaults(entry, defaults));
  }
  if (!value || typeof value !== "object") {
    return value;
  }
  if (!Array.isArray(value) && !value.model && defaults && defaults.model) {
    return { ...cloneJson(defaults), ...cloneJson(value) };
  }
  return cloneJson(value);
}

function hasModelReference(value) {
  if (Array.isArray(value)) {
    return value.some(hasModelReference);
  }
  if (!value || typeof value !== "object") {
    return false;
  }
  if (typeof value.model === "string") {
    return true;
  }
  return Object.values(value).some(hasModelReference);
}

function firstTextureOverride(value) {
  if (Array.isArray(value)) {
    for (const entry of value) {
      const found = firstTextureOverride(entry);
      if (found) return found;
    }
    return null;
  }
  if (!value || typeof value !== "object") {
    return null;
  }
  if (value.textures && typeof value.textures === "object") {
    return cloneJson(value.textures);
  }
  for (const child of Object.values(value)) {
    const found = firstTextureOverride(child);
    if (found) return found;
  }
  return null;
}

function normalizedCandidateVariant(value, defaults, blockId) {
  const withDefaults = applyLegacyDefaults(value, defaults);
  if (!hasModelReference(withDefaults)) {
    return null;
  }
  return normalizeVariantModelReferences(withDefaults, blockId);
}

function firstLegacyVariantCandidate(variants, defaults, blockId) {
  if (!variants || typeof variants !== "object") return null;
  const keys = Object.keys(variants);
  const priority = ["", "normal", "fluid", ...keys.filter(key => !["", "normal", "fluid", "inventory"].includes(key)), "inventory"];
  const seen = new Set();
  for (const key of priority) {
    if (seen.has(key) || !Object.prototype.hasOwnProperty.call(variants, key)) continue;
    seen.add(key);
    const candidate = normalizedCandidateVariant(variants[key], defaults, blockId);
    if (candidate) return candidate;
  }
  return null;
}

function firstMultipartCandidate(multipart, defaults, blockId) {
  if (!Array.isArray(multipart)) return null;
  const priority = [
    ...multipart.filter(entry => entry && typeof entry === "object" && !entry.when),
    ...multipart.filter(entry => entry && typeof entry === "object" && entry.when)
  ];
  for (const entry of priority) {
    const candidate = normalizedCandidateVariant(entry.apply, defaults, blockId);
    if (candidate) return candidate;
  }
  return null;
}

function textureOverrideCandidate(data, defaults, blockId) {
  const textures = firstTextureOverride(data.variants) || firstTextureOverride(data.multipart);
  if (!textures) return null;
  const parent = modernBlockModelReference(defaults && defaults.model ? defaults.model : "minecraft:cross", blockId);
  return {
    "model": generatedBlockModelRef(`${blockId}_texture_fallback`, {
      "parent": parent,
      "textures": textures
    })
  };
}

function fallbackBlockstateVariant(data, blockId) {
  const defaults = data.defaults && typeof data.defaults === "object" ? data.defaults : {};
  return textureOverrideCandidate(data, defaults, blockId)
    || firstLegacyVariantCandidate(data.variants, defaults, blockId)
    || firstMultipartCandidate(data.multipart, defaults, blockId)
    || (defaults.model ? normalizeVariantModelReferences({ "model": defaults.model }, blockId) : null)
    || {
      "model": generatedBlockModelRef(`${blockId}_missing_fallback`, {
        "parent": "minecraft:block/cube_all",
        "textures": {
          "all": "minecraft:block/stone"
        }
      })
    };
}

function normalizeBlockstateJsonText(text, from, entryName) {
  const normalized = normalizeLegacyJsonText(text);
  const data = JSON.parse(normalized);
  const blockId = (entryName || path.basename(from)).replace(/\.json$/, "");
  const out = {
    "variants": {
      "": fallbackBlockstateVariant(data, blockId)
    }
  };

  if (data.defaults) out.srparasites_legacy_defaults = data.defaults;
  if (data.variants) out.srparasites_legacy_variants = data.variants;
  if (data.multipart) out.srparasites_legacy_multipart = data.multipart;
  return `${JSON.stringify(out, null, 2)}\n`;
}

function writeGeneratedBlockModels() {
  const generatedDir = path.join(outAssets, "models", "block", "generated");
  fs.mkdirSync(generatedDir, { recursive: true });
  for (const [name, model] of generatedBlockModels.entries()) {
    fs.writeFileSync(path.join(generatedDir, `${name}.json`), `${JSON.stringify(model, null, 2)}\n`, "utf8");
  }
}

function normalizeRecipe(value) {
  if (Array.isArray(value)) {
    return value.map(normalizeRecipe);
  }
  if (!value || typeof value !== "object") {
    return value;
  }

  const out = {};
  for (const [key, raw] of Object.entries(value)) {
    let nextKey = key;
    let next = normalizeRecipe(raw);

    if (key === "type" && typeof raw === "string") {
      next = raw.replace(/^forge:ore_shaped$/, "minecraft:crafting_shaped").replace(/^forge:ore_shapeless$/, "minecraft:crafting_shapeless");
    } else if (key === "ore" && typeof raw === "string") {
      nextKey = "tag";
      next = `c:${raw.replace(/([a-z0-9])([A-Z])/g, "$1_$2").toLowerCase()}`;
    } else if (key === "item" && typeof raw === "string") {
      next = raw.replace(/^SRParasites:/, "srparasites:");
    }

    out[nextKey] = next;
  }
  return out;
}

function normalizeRecipeResult(recipe) {
  if (!recipe || typeof recipe !== "object" || !recipe.result || typeof recipe.result !== "object" || Array.isArray(recipe.result)) {
    return recipe;
  }
  if (typeof recipe.result.item === "string" && typeof recipe.result.id !== "string") {
    recipe.result.id = recipe.result.item;
    delete recipe.result.item;
  }
  return recipe;
}

function writeRecipes(srcDir) {
  if (!fs.existsSync(srcDir)) return 0;
  const recipeOut = path.join(outData, "recipe");
  fs.rmSync(recipeOut, { recursive: true, force: true });
  fs.mkdirSync(recipeOut, { recursive: true });
  let count = 0;
  for (const entry of fs.readdirSync(srcDir, { withFileTypes: true })) {
    if (!entry.isFile() || !entry.name.endsWith(".json")) continue;
    const from = path.join(srcDir, entry.name);
    const recipe = normalizeRecipeResult(normalizeRecipe(JSON.parse(fs.readFileSync(from, "utf8"))));
    fs.writeFileSync(path.join(recipeOut, entry.name), `${JSON.stringify(recipe, null, 2)}\n`, "utf8");
    count += 1;
  }
  return count;
}

function normalizeLoot(value) {
  if (Array.isArray(value)) {
    return value.map(normalizeLoot);
  }
  if (!value || typeof value !== "object") {
    return value;
  }

  const out = {};
  for (const [key, raw] of Object.entries(value)) {
    let next = normalizeLoot(raw);
    if ((key === "type" || key === "function" || key === "condition") && typeof raw === "string" && !raw.includes(":")) {
      next = `minecraft:${raw}`;
    } else if (key === "name" && typeof raw === "string") {
      next = raw.replace(/^SRParasites:/, "srparasites:");
    } else if (key === "condition" && raw === "minecraft:random_chance_with_looting") {
      next = "minecraft:random_chance_with_enchanted_bonus";
    }
    out[key] = next;
  }
  return out;
}

function writeLootTables(srcDir) {
  if (!fs.existsSync(srcDir)) return 0;
  const lootOut = path.join(outData, "loot_table");
  fs.rmSync(lootOut, { recursive: true, force: true });
  fs.mkdirSync(lootOut, { recursive: true });
  let count = 0;
  for (const entry of fs.readdirSync(srcDir, { withFileTypes: true })) {
    if (!entry.isFile() || !entry.name.endsWith(".json")) continue;
    const from = path.join(srcDir, entry.name);
    const loot = normalizeLoot(JSON.parse(fs.readFileSync(from, "utf8")));
    fs.writeFileSync(path.join(lootOut, entry.name), `${JSON.stringify(loot, null, 2)}\n`, "utf8");
    count += 1;
  }
  return count;
}

for (const legacyDerivedDir of [
  path.join(outAssets, "blockstates"),
  path.join(outAssets, "lang"),
  path.join(outAssets, "models", "block"),
  path.join(outAssets, "models", "item"),
  path.join(outAssets, "sounds"),
  path.join(outAssets, "textures")
]) {
  fs.rmSync(legacyDerivedDir, { recursive: true, force: true });
}
fs.rmSync(path.join(outAssets, "sounds.json"), { force: true });
fs.rmSync(outMinecraftAssets, { recursive: true, force: true });
fs.mkdirSync(outAssets, { recursive: true });
run("jar", [
  "xf",
  jar,
  "assets/minecraft",
  "assets/srparasites/blockstates",
  "assets/srparasites/lang",
  "assets/srparasites/models/block",
  "assets/srparasites/models/item",
  "assets/srparasites/recipes",
  "assets/srparasites/loot_tables",
  "assets/srparasites/sounds",
  "assets/srparasites/sounds.json",
  "assets/srparasites/textures"
], tmp);

const legacyAssets = path.join(tmp, "assets", "srparasites");
const legacyMinecraftAssets = path.join(tmp, "assets", "minecraft");
copyDir(path.join(legacyAssets, "blockstates"), path.join(outAssets, "blockstates"), normalizeBlockstateJsonText);
copyDir(path.join(legacyAssets, "models", "block"), path.join(outAssets, "models", "block"), normalizeLegacyJsonText);
copyDir(path.join(legacyAssets, "models", "item"), path.join(outAssets, "models", "item"), normalizeLegacyJsonText);
writeGeneratedBlockModels();
copyDir(path.join(legacyAssets, "textures"), path.join(outAssets, "textures"));
copyDir(path.join(legacyAssets, "sounds"), path.join(outAssets, "sounds"));
copyDir(legacyMinecraftAssets, outMinecraftAssets, normalizeLegacyJsonText);
if (fs.existsSync(path.join(legacyAssets, "sounds.json"))) {
  fs.writeFileSync(path.join(outAssets, "sounds.json"), normalizeLegacyJsonText(fs.readFileSync(path.join(legacyAssets, "sounds.json"), "utf8")), "utf8");
}
const venkrolSv = path.join(outAssets, "textures", "items", "itemmobspawner_venkrolsv.png");
const venkrolSiv = path.join(outAssets, "textures", "items", "itemmobspawner_venkrolsiv.png");
if (!fs.existsSync(venkrolSv) && fs.existsSync(venkrolSiv)) {
  fs.copyFileSync(venkrolSiv, venkrolSv);
}
writeLangs(path.join(legacyAssets, "lang"));
const recipeCount = writeRecipes(path.join(legacyAssets, "recipes"));
const lootTableCount = writeLootTables(path.join(legacyAssets, "loot_tables"));
fs.rmSync(tmp, { recursive: true, force: true });

fs.writeFileSync(
  path.join(outResources, "pack.mcmeta"),
  JSON.stringify({ pack: { pack_format: 34, description: "Scape and Run: Parasites 1.10.6 assets migrated for NeoForge 1.21.1" } }, null, 2) + "\n",
  "utf8"
);

console.log(`Migrated legacy assets from ${jar}`);
console.log(`Converted ${recipeCount} recipes and ${lootTableCount} loot tables to 1.21 resource locations.`);
