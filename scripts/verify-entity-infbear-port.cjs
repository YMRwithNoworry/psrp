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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfBearEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfBearRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_bear.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_bear.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/infbear.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infbear.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infbear.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfBear / Assimilated Bear port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_bear"',
  "EntityType.Builder.of(InfBearEntity::new, MobCategory.MONSTER)",
  ".sized(InfBearEntity.LEGACY_WIDTH, InfBearEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfBearEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfBear marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfBearEntity.createAttributes().build()", "ModEntities.INFBEAR.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfBear attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFBEAR", '"itemmobspawner_infbear"', "DeferredSpawnEggItem", "ModEntities.INFBEAR", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfBear spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFBEAR = legacyItem("itemmobspawner_infbear"')) {
  throw new Error("InfBear spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFBEAR.get(), InfBearRenderer::new)")) {
  throw new Error("ModClientEvents missing InfBear renderer registration");
}

const infBear = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfBearEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 49",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_bear.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_bear.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 40.0D",
  "LEGACY_ARMOR = 5.0D",
  "LEGACY_ATTACK_DAMAGE = 13.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.1D",
  "LEGACY_MOVEMENT_SPEED = 0.25D",
  "LEGACY_WIDTH = 1.3F",
  "LEGACY_HEIGHT = 1.4F",
  "LEGACY_EYE_HEIGHT = 1.3F",
  "LEGACY_SHADOW_RADIUS = 0.7F",
  "LEGACY_RENDER_SCALE = 1.2F",
  "LEGACY_TYPE = 11",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_FUSE_TIME = 40",
  "LEGACY_LESH_LEGS = 0",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 1.6F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 73",
  "MeleeAttackGoal",
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDBEAR_GROWL",
  "ModSounds.INFECTEDBEAR_HURT",
  "ModSounds.INFECTEDBEAR_DEATH"
]) {
  if (!infBear.includes(marker)) throw new Error(`InfBearEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfBearRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfBearEntity>",
  "geo/entity/inf_bear.geo.json",
  "animations/entity/inf_bear.animation.json",
  "textures/entity/monster/infbear.png",
  "InfBearEntity.LEGACY_SHADOW_RADIUS",
  "InfBearEntity.LEGACY_RENDER_SCALE",
  "entity.getSelfeFlashIntensity2()"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfBearRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_bear"] !== "Assimilated Bear" || enUs["entity.srparasites.sim_bear.name"] !== "Assimilated Bear") {
  throw new Error("en_us.json missing Assimilated Bear entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infbear"] !== "Spawn Assimilated Bear") {
  throw new Error("en_us.json missing Spawn Assimilated Bear item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_bear.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfBear geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfBear geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 87) throw new Error(`InfBear geo must preserve the 87 converted ModelInfBear bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_bear.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_bear.func_78087_a", "animation.inf_bear.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfBear animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 6) throw new Error(`${name} must keep 6 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityInfBear",
  "sim_bear",
  "itemmobspawner_infbear",
  "Assimilated Bear",
  "INFECTED_MELT",
  "EntityLesh"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfBear marker: ${marker}`);
}

console.log("InfBear / Assimilated Bear entity port verifier passed.");
