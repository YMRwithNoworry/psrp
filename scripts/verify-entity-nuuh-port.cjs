const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/NuuhEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/NuuhRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/nuuh.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/nuuh.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/nuuh.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/nuuhv.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/nuuhb.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_nuuh.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_nuuh.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Nuuh port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/snuuh.png")) {
  throw new Error("Verifier expected snuuh.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("nuuh"',
  "EntityType.Builder.of(NuuhEntity::new, MobCategory.MONSTER)",
  ".sized(NuuhEntity.LEGACY_WIDTH, NuuhEntity.LEGACY_HEIGHT)",
  ".eyeHeight(NuuhEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Nuuh marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["NuuhEntity.createAttributes().build()", "ModEntities.NUUH.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Nuuh attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_NUUH", '"itemmobspawner_nuuh"', "DeferredSpawnEggItem", "ModEntities.NUUH"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Nuuh spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_NUUH = legacyItem("itemmobspawner_nuuh"')) {
  throw new Error("Nuuh spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.NUUH.get(), NuuhRenderer::new)")) {
  throw new Error("ModClientEvents missing Nuuh renderer registration");
}

const nuuh = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/NuuhEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 76",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.nuuh.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.nuuh.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 17.0D",
  "LEGACY_ARMOR = 10.0D",
  "LEGACY_ATTACK_DAMAGE = 9.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.6D",
  "LEGACY_MOVEMENT_SPEED = 0.37D",
  "LEGACY_WIDTH = 1.0F",
  "LEGACY_HEIGHT = 1.0F",
  "LEGACY_EYE_HEIGHT = 0.9F",
  "LEGACY_XP = 75",
  "LEGACY_TYPE = 51",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_ATTACK_SPEED_TICKS = 6",
  "LEGACY_SKILL_LEAP_POWER = 0.8F",
  "LEGACY_SKILL_LEAP_SPEED = 2.0D",
  "LEGACY_SKILL_MIN_COOLDOWN = 20",
  "LEGACY_SKILL_MAX_COOLDOWN = 100",
  "LEGACY_SKILL_WINDUP = 5",
  "LEGACY_SKILL_EVENT = 14",
  "LEGACY_LEAP_AT_TARGET_POWER = 0.4F",
  "LEGACY_EVADE_INTERVAL = 10",
  "LEGACY_EVADE_COOLDOWN = 15",
  "LEGACY_FALL_DAMAGE_THRESHOLD = 200.0F",
  "implements GeoEntity",
  "WallClimberNavigation",
  "LeapAtTargetGoal",
  "MeleeAttackGoal",
  "SkillLeapGoal",
  "EvadeDashGoal",
  "ModEffects.VIRAL",
  "SrpMobEffect.applyStackEffect",
  "ModSounds.MOB_SILENCE",
  "ModSounds.SMALL_STEP",
  "WaterAnimal",
  "Animal",
  "Villager"
]) {
  if (!nuuh.includes(marker)) throw new Error(`NuuhEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/NuuhRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<NuuhEntity>",
  "geo/entity/nuuh.geo.json",
  "animations/entity/nuuh.animation.json",
  "textures/entity/monster/nuuh.png",
  "textures/entity/monster/nuuhv.png",
  "textures/entity/monster/nuuhb.png",
  "this.shadowRadius = 0.7F"
]) {
  if (!renderer.includes(marker)) throw new Error(`NuuhRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("snuuh.png")) {
  throw new Error("NuuhRenderer must not reference missing legacy snuuh.png texture");
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.nuuh"] !== "Mangler" || enUs["entity.srparasites.nuuh.name"] !== "Mangler") {
  throw new Error("en_us.json missing Nuuh entity translation keys");
}
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");
if (zhCn["entity.srparasites.nuuh"] !== "凶裂兽" || zhCn["entity.srparasites.nuuh.name"] !== "凶裂兽") {
  throw new Error("zh_cn.json missing Nuuh entity translation keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/nuuh.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Nuuh geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Nuuh geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 248) throw new Error(`Nuuh geo must preserve the 248 converted ModelNuuh bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/nuuh.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.nuuh.func_78087_a", "animation.nuuh.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Nuuh animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 54) throw new Error(`${name} must keep 54 animated bones, found ${bonesWithChannels.length}`);
  const times = new Set();
  const modes = new Set();
  for (const channels of Object.values(data.bones || {})) {
    for (const keys of Object.values(channels)) {
      for (const [time, value] of Object.entries(keys)) {
        times.add(Number(time));
        if (value && typeof value === "object" && value.lerp_mode) modes.add(value.lerp_mode);
      }
    }
  }
  if (times.size !== 121) throw new Error(`${name} must keep 121 sampled 60fps key times, found ${times.size}`);
  if (!modes.has("catmullrom")) throw new Error(`${name} must keep catmullrom interpolation`);
}

console.log("Nuuh entity port verifier passed.");
