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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfWolfEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfWolfRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_wolf.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_wolf.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/wolf.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infwolf.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infwolf.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfWolf / Assimilated Wolf port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_wolf"',
  "EntityType.Builder.of(InfWolfEntity::new, MobCategory.MONSTER)",
  ".sized(InfWolfEntity.LEGACY_WIDTH, InfWolfEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfWolfEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfWolf marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfWolfEntity.createAttributes().build()", "ModEntities.INFWOLF.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfWolf attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFWOLF", '"itemmobspawner_infwolf"', "DeferredSpawnEggItem", "ModEntities.INFWOLF", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfWolf spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFWOLF = legacyItem("itemmobspawner_infwolf"')) {
  throw new Error("InfWolf spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFWOLF.get(), InfWolfRenderer::new)")) {
  throw new Error("ModClientEvents missing InfWolf renderer registration");
}

const infWolf = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfWolfEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 15",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_wolf.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_wolf.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 10.0D",
  "LEGACY_ARMOR = 0.5D",
  "LEGACY_ATTACK_DAMAGE = 10.5D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.2D",
  "LEGACY_MOVEMENT_SPEED = 0.30000001192092896D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.6F",
  "LEGACY_HEIGHT = 0.85F",
  "LEGACY_EYE_HEIGHT = 0.68F",
  "LEGACY_SHADOW_RADIUS = 0.5F",
  "LEGACY_TYPE = 13",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_FUSE_TIME = 40",
  "LEGACY_ATTACK_SPEED_TICKS = 10",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_LEAP_STRENGTH = 0.4F",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 0.85F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 19",
  "LEGACY_LESH_LEGS = 0",
  "LEGACY_HEAD_HEALTH = 0.0D",
  "LEGACY_HEAD_DAMAGE = 0.0D",
  "LEGACY_HEAD_CHANCE = 0.0D",
  "LEGACY_LESH_V = 0",
  "MeleeAttackGoal",
  "LeapAtTargetGoal",
  "new LeapAtTargetGoal(this, LEGACY_LEAP_STRENGTH)",
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDWOLF_GROWL",
  "ModSounds.INFECTEDWOLF_HURT",
  "ModSounds.INFECTEDWOLF_DEATH",
  "SoundEvents.WOLF_STEP"
]) {
  if (!infWolf.includes(marker)) throw new Error(`InfWolfEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfWolfRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfWolfEntity>",
  "geo/entity/inf_wolf.geo.json",
  "animations/entity/inf_wolf.animation.json",
  "textures/entity/monster/wolf.png",
  "InfWolfEntity.LEGACY_SHADOW_RADIUS",
  "entity.getSelfeFlashIntensity2()"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfWolfRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_wolf"] !== "Assimilated Wolf" || enUs["entity.srparasites.sim_wolf.name"] !== "Assimilated Wolf") {
  throw new Error("en_us.json missing Assimilated Wolf entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infwolf"] !== "Spawn Assimilated Wolf") {
  throw new Error("en_us.json missing Spawn Assimilated Wolf item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_wolf.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfWolf geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfWolf geo must contain exactly one minecraft:geometry entry");
}
const geometry = geo["minecraft:geometry"][0];
if (geometry.description.texture_width !== 64 || geometry.description.texture_height !== 36) {
  throw new Error(`InfWolf geo must preserve the 64x36 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 55) throw new Error(`InfWolf geo must preserve the 55 converted ModelInfWolf bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_wolf.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_wolf.func_78087_a", "animation.inf_wolf.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfWolf animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 11) throw new Error(`${name} must keep 11 animated bones, found ${bonesWithChannels.length}`);
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

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
for (const marker of [
  "EntityInfWolf",
  "ModelInfWolf",
  "RenderInfWolf",
  "sim_wolf",
  "itemmobspawner_infwolf",
  "Assimilated Wolf",
  "INFECTEDWOLF_GROWL",
  "EntityAILeapAtTarget",
  "EntityInfWolfHead",
  "infwolfmob",
  "EntityFerWolf"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfWolf marker: ${marker}`);
}

console.log("InfWolf / Assimilated Wolf entity port verifier passed.");
