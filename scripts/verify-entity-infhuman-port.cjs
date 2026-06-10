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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHumanEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfHumanRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_human.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_human.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/human.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/human1.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humaneaten.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanflood.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanfrozen.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanforge.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infhuman.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infhuman.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfHuman / Assimilated Human port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_human"',
  "EntityType.Builder.of(InfHumanEntity::new, MobCategory.MONSTER)",
  ".sized(InfHumanEntity.LEGACY_WIDTH, InfHumanEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfHumanEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfHuman marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfHumanEntity.createAttributes().build()", "ModEntities.INFHUMAN.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfHuman attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFHUMAN", '"itemmobspawner_infhuman"', "DeferredSpawnEggItem", "ModEntities.INFHUMAN", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfHuman spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFHUMAN = legacyItem("itemmobspawner_infhuman"')) {
  throw new Error("InfHuman spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFHUMAN.get(), InfHumanRenderer::new)")) {
  throw new Error("ModClientEvents missing InfHuman renderer registration");
}

const infHuman = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHumanEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 6",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_human.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_human.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 15.0D",
  "LEGACY_ARMOR = 5.0D",
  "LEGACY_ATTACK_DAMAGE = 9.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.1D",
  "LEGACY_MOVEMENT_SPEED = 0.230000004172325D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_SOUND_EATER_FOLLOW_RANGE = 12.0D",
  "LEGACY_SOUND_EATER_MOVEMENT_SPEED = 0.32D",
  "LEGACY_WIDTH = 0.6F",
  "LEGACY_HEIGHT = 1.95F",
  "LEGACY_EYE_HEIGHT = 1.73F",
  "LEGACY_SHADOW_RADIUS = 0.5F",
  "LEGACY_TYPE = 11",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_LEAP_STRENGTH = 0.7F",
  "LEGACY_LEAP_STATUS = 3",
  "LEGACY_LEAP_COOLDOWN = 20",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_VARIANT_RANDOM_BOUND = 3",
  "LEGACY_VARIANT_SKIN_MIN = 1",
  "LEGACY_VARIANT_SKIN_COUNT = 3",
  "LEGACY_SOUND_EATER_SKIN = 111",
  "LEGACY_SOUND_EATER_CHANCE = 0.009999999776482582D",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 1.95F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 127",
  "LEGACY_LESH_LEGS = 0",
  "LEGACY_BLEED_TICKS = 100",
  "OpenDoorGoal",
  "LeapAtTargetGoal",
  "MeleeAttackGoal",
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDHUMAN_GROWL",
  "ModSounds.INFECTEDHUMAN_HURT",
  "ModSounds.INFECTEDHUMAN_DEATH",
  "ModEffects.BLEED",
  "parasitehost"
]) {
  if (!infHuman.includes(marker)) throw new Error(`InfHumanEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfHumanRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfHumanEntity>",
  "geo/entity/inf_human.geo.json",
  "animations/entity/inf_human.animation.json",
  "textures/entity/monster/human.png",
  "textures/entity/monster/human1.png",
  "textures/entity/monster/humaneaten.png",
  "textures/entity/monster/humanflood.png",
  "textures/entity/monster/humanfrozen.png",
  "textures/entity/monster/humanforge.png",
  "InfHumanEntity.LEGACY_SHADOW_RADIUS",
  "entity.getSelfeFlashIntensity2()",
  "LEGACY_KIM_NAME"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfHumanRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_human"] !== "Assimilated Human" || enUs["entity.srparasites.sim_human.name"] !== "Assimilated Human") {
  throw new Error("en_us.json missing Assimilated Human entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infhuman"] !== "Spawn Assimilated Human") {
  throw new Error("en_us.json missing Spawn Assimilated Human item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_human.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfHuman geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfHuman geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 58) throw new Error(`InfHuman geo must preserve the 58 converted ModelInfHuman bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_human.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_human.func_78087_a", "animation.inf_human.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfHuman animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 5) throw new Error(`${name} must keep 5 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityInfHuman",
  "sim_human",
  "itemmobspawner_infhuman",
  "Assimilated Human",
  "INFECTEDHUMAN_GROWL",
  "EntityInfHumanHead",
  "EntityLesh"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfHuman marker: ${marker}`);
}

console.log("InfHuman / Assimilated Human entity port verifier passed.");
