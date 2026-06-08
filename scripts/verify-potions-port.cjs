const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModPotions.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java",
  "src/main/resources/assets/srparasites/lang/en_us.json",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing potion port file/resource: ${file}`);
}

const potions = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModPotions.java");
for (const marker of [
  "Registries.POTION",
  "DeferredRegister<Potion>",
  "DeferredHolder<Potion, Potion>",
  "MobEffectInstance",
  "LEGACY_DEFAULT_DURATION = 2400",
  "LEGACY_THORNSHADE_THORNS_DURATION = 60",
  "new Potion(legacyName, new MobEffectInstance(effect, duration))"
]) {
  if (!potions.includes(marker)) throw new Error(`ModPotions missing marker: ${marker}`);
}

const expected = [
  ["corro", "srparasites:corrosive", "ModEffects.CORROSIVE", "LEGACY_DEFAULT_DURATION"],
  ["vira", "srparasites:viral", "ModEffects.VIRAL", "LEGACY_DEFAULT_DURATION"],
  ["vomit", "srparasites:vomit", "ModEffects.VOMIT", "LEGACY_DEFAULT_DURATION"],
  ["rage", "srparasites:rage", "ModEffects.RAGE", "LEGACY_DEFAULT_DURATION"],
  ["senses", "srparasites:senses", "ModEffects.SENSES", "LEGACY_DEFAULT_DURATION"],
  ["indeaf", "srparasites:indeaf", "ModEffects.INDEAF", "LEGACY_DEFAULT_DURATION"],
  ["overheating", "srparasites:overheating", "ModEffects.OVERHEATING", "LEGACY_DEFAULT_DURATION"],
  ["conta", "srparasites:conta", "ModEffects.CONTAMINATION", "LEGACY_DEFAULT_DURATION"],
  ["effectpos", "srparasites:effectpos", "ModEffects.EFFECT_POS", "LEGACY_DEFAULT_DURATION"],
  ["effectneg", "srparasites:effectneg", "ModEffects.EFFECT_NEG", "LEGACY_DEFAULT_DURATION"],
  ["the_sign", "srparasites:the_sign", "ModEffects.THE_SIGN", "LEGACY_DEFAULT_DURATION"],
  ["thornshade_thorns", "srparasites:thornshade_thorns", "ModEffects.THORNSHADE_THORNS", "LEGACY_THORNSHADE_THORNS_DURATION"]
];

for (const [id, name, effect, duration] of expected) {
  const marker = `register("${id}", "${name}", ${effect}, ${duration})`;
  if (!potions.includes(marker)) throw new Error(`ModPotions missing legacy potion registration: ${marker}`);
}

const registeredIds = [...potions.matchAll(/register\("([^"]+)"/g)].map((match) => match[1]).sort();
const expectedIds = expected.map(([id]) => id).sort();
if (JSON.stringify(registeredIds) !== JSON.stringify(expectedIds)) {
  throw new Error(`Unexpected potion registrations: ${registeredIds.join(", ")}`);
}

for (const forbidden of [
  "needler",
  "dod_smoke_trail",
  "bleed",
  "coth",
  "fear",
  "jugg",
  "repel",
  "debar",
  "foster",
  "link",
  "pivot",
  "parate",
  "primitive",
  "adapted",
  "pure",
  "crude",
  "feral",
  "nexus",
  "muscleout",
  "spotted",
  "braining",
  "novision"
]) {
  if (potions.includes(`"${forbidden}"`) || potions.includes(`:${forbidden}"`)) {
    throw new Error(`ModPotions registers or names a potion outside this evidence-backed slice: ${forbidden}`);
  }
}

const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");
if (!main.includes("import com.dhanantry.scapeandrunparasites.init.ModPotions;")) {
  throw new Error("SRPMain does not import ModPotions");
}
if (!main.includes("ModPotions.register(modEventBus);")) {
  throw new Error("SRPMain does not register ModPotions on the mod event bus");
}

const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
for (const [, name] of expected) {
  for (const prefix of [
    "item.minecraft.potion.effect.",
    "item.minecraft.splash_potion.effect.",
    "item.minecraft.lingering_potion.effect.",
    "item.minecraft.tipped_arrow.effect."
  ]) {
    const key = `${prefix}${name}`;
    if (!lang[key]) throw new Error(`en_us.json missing modern potion item key: ${key}`);
  }
}

for (const forbidden of ["needler", "dod_smoke_trail"]) {
  for (const prefix of [
    "item.minecraft.potion.effect.",
    "item.minecraft.splash_potion.effect.",
    "item.minecraft.lingering_potion.effect.",
    "item.minecraft.tipped_arrow.effect."
  ]) {
    const key = `${prefix}srparasites:${forbidden}`;
    if (lang[key]) throw new Error(`en_us.json should not add modern item key without legacy PotionType evidence: ${key}`);
  }
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "`PotionType` fields for the already implemented potion subset",
  "CORRO_P",
  "srparasites:corro",
  "srparasites:corrosive",
  "VIRA_P",
  "srparasites:vira",
  "srparasites:viral",
  "VOMIT_P",
  "RAGE_P",
  "SENS_P",
  "INDEAF_P",
  "OVERHEATING_P",
  "CONTA_P",
  "EFFECTPOS_P",
  "EFFECTNEG_P",
  "THE_SIGN_P",
  "srparasites:the_sign",
  "THORNSHADE_THORNS_P",
  "duration `2400`",
  "duration `60`",
  "no `DOD_SMOKE_TRAIL_P` or `DLER_P` legacy `PotionType` field",
  "Registered modern 1.21.1 `Registries.POTION` entries",
  "Needler or Dod Smoke Trail potion type"
]) {
  if (!audit.includes(marker)) throw new Error(`Port audit missing potion evidence marker: ${marker}`);
}

if (audit.includes("needler` with color `0xC7B403` and a potion type duration")) {
  throw new Error("Port audit still claims a legacy Needler potion type duration");
}

console.log("Potion port verifier passed.");
