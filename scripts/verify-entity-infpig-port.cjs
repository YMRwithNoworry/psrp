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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfPigEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfPigRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_pig.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_pig.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/pig.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infpig.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infpig.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfPig / Assimilated Pig port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_pig"',
  "EntityType.Builder.of(InfPigEntity::new, MobCategory.MONSTER)",
  ".sized(InfPigEntity.LEGACY_WIDTH, InfPigEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfPigEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfPig marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfPigEntity.createAttributes().build()", "ModEntities.INFPIG.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfPig attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFPIG", '"itemmobspawner_infpig"', "DeferredSpawnEggItem", "ModEntities.INFPIG", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfPig spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFPIG = legacyItem("itemmobspawner_infpig"')) {
  throw new Error("InfPig spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFPIG.get(), InfPigRenderer::new)")) {
  throw new Error("ModClientEvents missing InfPig renderer registration");
}

const infPig = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfPigEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 26",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_pig.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_pig.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 9.0D",
  "LEGACY_ARMOR = 0.1D",
  "LEGACY_ATTACK_DAMAGE = 3.5D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.1D",
  "LEGACY_MOVEMENT_SPEED = 0.25D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.9F",
  "LEGACY_HEIGHT = 0.9F",
  "LEGACY_EYE_HEIGHT = 0.8F",
  "LEGACY_SHADOW_RADIUS = 0.5F",
  "LEGACY_TYPE = 15",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_FUSE_TIME = 40",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 0.9F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 20",
  "LEGACY_LESH_LEGS = 0",
  "LEGACY_HEAD_HEALTH = 0.0D",
  "LEGACY_HEAD_DAMAGE = 0.0D",
  "LEGACY_HEAD_CHANCE = 0.0D",
  "LEGACY_LESH_V = 0",
  "MeleeAttackGoal",
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDPIG_GROWL",
  "ModSounds.INFECTEDPIG_HURT",
  "ModSounds.INFECTEDPIG_DEATH",
  "SoundEvents.PIG_STEP"
]) {
  if (!infPig.includes(marker)) throw new Error(`InfPigEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfPigRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfPigEntity>",
  "geo/entity/inf_pig.geo.json",
  "animations/entity/inf_pig.animation.json",
  "textures/entity/monster/pig.png",
  "InfPigEntity.LEGACY_SHADOW_RADIUS",
  "entity.getSelfeFlashIntensity2()"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfPigRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_pig"] !== "Assimilated Pig" || enUs["entity.srparasites.sim_pig.name"] !== "Assimilated Pig") {
  throw new Error("en_us.json missing Assimilated Pig entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infpig"] !== "Spawn Assimilated Pig") {
  throw new Error("en_us.json missing Spawn Assimilated Pig item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_pig.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfPig geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfPig geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 51) throw new Error(`InfPig geo must preserve the 51 converted ModelInfPig bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_pig.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_pig.func_78087_a", "animation.inf_pig.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfPig animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 7) throw new Error(`${name} must keep 7 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityInfPig",
  "ModelInfPig",
  "RenderInfPig",
  "sim_pig",
  "itemmobspawner_infpig",
  "Assimilated Pig",
  "INFECTEDPIG_GROWL",
  "EntityLesh",
  "infpigmob",
  "EntityInfPigHead"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfPig marker: ${marker}`);
}

console.log("InfPig / Assimilated Pig entity port verifier passed.");
