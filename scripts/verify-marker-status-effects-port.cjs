const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
const potions = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModPotions.java");
const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");

const expected = [
  {
    field: "FEAR",
    effectId: "fear",
    potionId: "fear",
    potionName: "srparasites:fear",
    category: "BENEFICIAL",
    color: "0x111114",
    legacyEffect: "FEAR_E",
    legacyPotion: "FEAR_P",
    texture: "potion_fear.png"
  },
  {
    field: "ANTIMALL",
    effectId: "antimall",
    potionId: "res",
    potionName: "srparasites:antimall",
    category: "HARMFUL",
    color: "0x88626C",
    legacyEffect: "RES_E",
    legacyPotion: "RES_P",
    texture: "potion_antimall.png"
  },
  {
    field: "REPEL",
    effectId: "repel",
    potionId: "repel",
    potionName: "srparasites:repel",
    category: "BENEFICIAL",
    color: "0x43AC30",
    legacyEffect: "EPEL_E",
    legacyPotion: "EPEL_P",
    texture: "potion_repel.png"
  },
  {
    field: "DEBAR",
    effectId: "debar",
    potionId: "debar",
    potionName: "srparasites:debar",
    category: "BENEFICIAL",
    color: "0x9E134B",
    legacyEffect: "DEBAR_E",
    legacyPotion: "DEBAR_P",
    texture: "potion_debar.png"
  },
  {
    field: "LINK",
    effectId: "link",
    potionId: "link",
    potionName: "srparasites:link",
    category: "BENEFICIAL",
    color: "0xFF7595",
    legacyEffect: "LINK_E",
    legacyPotion: "LINK_P",
    texture: "potion_link.png"
  },
  {
    field: "PIVOT",
    effectId: "pivot",
    potionId: "pivot",
    potionName: "srparasites:pivot",
    category: "BENEFICIAL",
    color: "0xFFB1C3",
    legacyEffect: "PIVOT_E",
    legacyPotion: "PIVOT_P",
    texture: "potion_pivot.png"
  },
  {
    field: "JUGG",
    effectId: "jugg",
    potionId: "jugg",
    potionName: "srparasites:jugg",
    category: "BENEFICIAL",
    color: "0xBDB885",
    legacyEffect: "JUGG_E",
    legacyPotion: "JUGG_P",
    texture: "potion_jugg.png"
  },
  {
    field: "PARATE",
    effectId: "parate",
    potionId: "parate",
    potionName: "srparasites:parate",
    category: "BENEFICIAL",
    color: "0xB35736",
    legacyEffect: "PARATE_E",
    legacyPotion: "PARATE_P",
    texture: "potion_parate.png"
  },
  {
    field: "PRIMITIVE",
    effectId: "primitive",
    potionId: "primitive",
    potionName: "srparasites:primitive",
    category: "BENEFICIAL",
    color: "0x8F4C45",
    legacyEffect: "KILLPRI_E",
    legacyPotion: "KILLPRI_P",
    texture: "potion_primitive.png"
  },
  {
    field: "ADAPTED",
    effectId: "adapted",
    potionId: "adapted",
    potionName: "srparasites:adapted",
    category: "BENEFICIAL",
    color: "0x7F584E",
    legacyEffect: "KILLADA_E",
    legacyPotion: "KILLADA_P",
    texture: "potion_adapted.png"
  },
  {
    field: "PURE",
    effectId: "pure",
    potionId: "pure",
    potionName: "srparasites:pure",
    category: "BENEFICIAL",
    color: "0x0DA532",
    legacyEffect: "KILLPUR_E",
    legacyPotion: "KILLPUR_P",
    texture: "potion_pure.png"
  },
  {
    field: "CRUDE",
    effectId: "crude",
    potionId: "crude",
    potionName: "srparasites:crude",
    category: "BENEFICIAL",
    color: "0x0DA532",
    legacyEffect: "KILLCRU_E",
    legacyPotion: "KILLCRU_P",
    texture: "potion_crude.png"
  },
  {
    field: "FERAL",
    effectId: "feral",
    potionId: "feral",
    potionName: "srparasites:feral",
    category: "BENEFICIAL",
    color: "0x993030",
    legacyEffect: "KILLFER_E",
    legacyPotion: "KILLFER_P",
    texture: "potion_feral.png"
  },
  {
    field: "NEXUS",
    effectId: "nexus",
    potionId: "nexus",
    potionName: "srparasites:nexus",
    category: "BENEFICIAL",
    color: "0x487848",
    legacyEffect: "KILLNEX_E",
    legacyPotion: "KILLNEX_P",
    texture: "potion_nexus.png"
  },
  {
    field: "BRAINING",
    effectId: "braining",
    potionId: "braining",
    potionName: "srparasites:braining",
    category: "BENEFICIAL",
    color: "0x796E85",
    legacyEffect: "BRAINING_E",
    legacyPotion: "BRAINING_P",
    texture: "potion_braining.png"
  },
  {
    field: "NOVISION",
    effectId: "novision",
    potionId: "novision",
    potionName: "srparasites:novision",
    category: "BENEFICIAL",
    color: "0x182639",
    legacyEffect: "NOVISION_E",
    legacyPotion: "NOVISION_P",
    texture: "potion_novision.png"
  },
  {
    field: "MUSCLE_OUT",
    effectId: "muscleout",
    potionId: "muscleout",
    potionName: "srparasites:muscleout",
    category: "HARMFUL",
    color: "0xEC7F82",
    legacyEffect: "MUSCLEOUT_E",
    legacyPotion: "MUSCLEOUT_P",
    texture: "potion_muscleout.png"
  }
];

for (const entry of expected) {
  const effectRegister = `EFFECTS.register("${entry.effectId}"`;
  if (!effects.includes(effectRegister)) throw new Error(`Missing marker effect registration: ${effectRegister}`);

  const effectType = `new SrpMobEffect(MobEffectCategory.${entry.category}, ${entry.color})`;
  if (!effects.includes(effectType)) throw new Error(`Missing marker effect category/color: ${entry.effectId} ${effectType}`);

  const potionRegister = `register("${entry.potionId}", "${entry.potionName}", ModEffects.${entry.field}, LEGACY_DEFAULT_DURATION)`;
  if (!potions.includes(potionRegister)) throw new Error(`Missing marker potion registration: ${potionRegister}`);

  const texture = `src/main/resources/assets/srparasites/textures/gui/${entry.texture}`;
  if (!exists(texture)) throw new Error(`Missing legacy marker effect GUI texture: ${texture}`);

  const oldEffectKey = `mob_effect.srparasites:${entry.effectId}`;
  if (!lang[oldEffectKey]) throw new Error(`Missing old marker effect language key: ${oldEffectKey}`);

  for (const prefix of [
    "potion.effect.",
    "splash_potion.effect.",
    "lingering_potion.effect.",
    "tipped_arrow.effect.",
    "item.minecraft.potion.effect.",
    "item.minecraft.splash_potion.effect.",
    "item.minecraft.lingering_potion.effect.",
    "item.minecraft.tipped_arrow.effect."
  ]) {
    const key = `${prefix}${entry.potionName}`;
    if (!lang[key]) throw new Error(`Missing marker potion language key: ${key}`);
  }

  for (const marker of [entry.legacyEffect, entry.legacyPotion, entry.effectId, entry.potionName, entry.color]) {
    if (!audit.includes(marker)) throw new Error(`Audit missing marker effect evidence: ${marker}`);
  }
}

for (const marker of [
  "Registered evidence-backed marker/basic `SRPEffectBase` effects",
  "Registered modern 1.21.1 `Registries.POTION` entries for the marker/basic legacy potion subset",
  "DEBAR/PIVOT/JUGG/PARATE/EPEL external interactions",
  "COTH, FOSTER, PREY, and SPOTTED remain separate complex systems"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing marker status boundary: ${marker}`);
}

for (const forbidden of ["COTH", "FOSTER", "PREY", "SPOTTED"]) {
  if (effects.includes(` ${forbidden} `) || effects.includes(` ${forbidden} =`)) {
    throw new Error(`ModEffects should not register complex status system in this marker slice: ${forbidden}`);
  }
}

console.log("Marker status-effect port verifier passed.");
