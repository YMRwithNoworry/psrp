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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/LodoEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/MudoEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/MudoRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/mudo.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/mudo.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/mudo.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/mudov.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/mudob.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_mudo.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_mudo.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Mudo port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/smudo.png")) {
  throw new Error("Verifier expected smudo.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("mudo"',
  "EntityType.Builder.of(MudoEntity::new, MobCategory.MONSTER)",
  ".sized(MudoEntity.LEGACY_WIDTH, MudoEntity.LEGACY_HEIGHT)",
  ".eyeHeight(MudoEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Mudo marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["MudoEntity.createAttributes().build()", "ModEntities.MUDO.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Mudo attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_MUDO", '"itemmobspawner_mudo"', "DeferredSpawnEggItem", "ModEntities.MUDO"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Mudo spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_MUDO = legacyItem("itemmobspawner_mudo"')) {
  throw new Error("Mudo spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.MUDO.get(), MudoRenderer::new)")) {
  throw new Error("ModClientEvents missing Mudo renderer registration");
}

const mudo = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/MudoEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 12",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.mudo.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.mudo.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 10.0D",
  "LEGACY_ARMOR = 5.0D",
  "LEGACY_ATTACK_DAMAGE = 5.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.2D",
  "LEGACY_WIDTH = 0.85F",
  "LEGACY_HEIGHT = 1.0F",
  "LEGACY_EYE_HEIGHT = 0.8F",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_SKILL_LEAP_POWER = 0.7F",
  "LEGACY_SKILL_LEAP_SPEED = 2.5D",
  "LEGACY_SKILL_MIN_COOLDOWN = 40",
  "LEGACY_SKILL_MAX_COOLDOWN = 100",
  "LEGACY_SKILL_WINDUP = 5",
  "LEGACY_SKILL_EVENT = 14",
  "LEGACY_LEAP_AT_TARGET_POWER = 0.4F",
  "LEGACY_FALL_DAMAGE_THRESHOLD = 60.0F",
  "implements GeoEntity",
  "WallClimberNavigation",
  "LeapAtTargetGoal",
  "MeleeAttackGoal",
  "SkillLeapGoal",
  "ModEffects.VIRAL",
  "SrpMobEffect.applyStackEffect",
  "ModSounds.MUDO_GROWL",
  "ModSounds.MUDO_HURT",
  "ModSounds.MUDO_DEATH",
  "ModSounds.SMALL_STEP",
  "WaterAnimal",
  "Villager"
]) {
  if (!mudo.includes(marker)) throw new Error(`MudoEntity missing legacy behavior marker: ${marker}`);
}

const lodo = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/LodoEntity.java");
for (const marker of ["ModEntities.MUDO", "MudoEntity mudo", "addFreshEntity(mudo)", "discard()"]) {
  if (!lodo.includes(marker)) throw new Error(`LodoEntity missing grow-to-Mudo marker: ${marker}`);
}
if (lodo.includes("EntityMudo remains a later migration slice") || lodo.includes("loggedMissingMudo")) {
  throw new Error("LodoEntity still contains the old missing-Mudo placeholder path");
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/MudoRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<MudoEntity>",
  "geo/entity/mudo.geo.json",
  "animations/entity/mudo.animation.json",
  "textures/entity/monster/mudo.png",
  "textures/entity/monster/mudov.png",
  "textures/entity/monster/mudob.png",
  "this.shadowRadius = 0.5F"
]) {
  if (!renderer.includes(marker)) throw new Error(`MudoRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("smudo.png")) {
  throw new Error("MudoRenderer must not reference missing legacy smudo.png texture");
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.mudo"] !== "Rupter" || enUs["entity.srparasites.mudo.name"] !== "Rupter") {
  throw new Error("en_us.json missing Mudo entity translation keys");
}
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");
if (zhCn["entity.srparasites.mudo"] !== "裂兽" || zhCn["entity.srparasites.mudo.name"] !== "裂兽") {
  throw new Error("zh_cn.json missing Mudo entity translation keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/mudo.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Mudo geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Mudo geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 113) throw new Error(`Mudo geo must preserve the 113 converted ModelMudo bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/mudo.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.mudo.func_78087_a", "animation.mudo.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Mudo animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 30) throw new Error(`${name} must keep 30 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Mudo entity port verifier passed.");
