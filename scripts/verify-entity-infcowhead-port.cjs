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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfCowHeadEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfCowHeadRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_cow_head.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_cow_head.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/cowh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infcowhead.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infcowhead.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfCowHead port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_cowhead"',
  "EntityType.Builder.of(InfCowHeadEntity::new, MobCategory.MONSTER)",
  ".sized(InfCowHeadEntity.LEGACY_WIDTH, InfCowHeadEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfCowHeadEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfCowHead marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfCowHeadEntity.createAttributes().build()", "ModEntities.INFCOWHEAD.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfCowHead marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFCOWHEAD", '"itemmobspawner_infcowhead"', "DeferredSpawnEggItem", "ModEntities.INFCOWHEAD", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfCowHead spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFCOWHEAD = legacyItem("itemmobspawner_infcowhead"')) {
  throw new Error("InfCowHead spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFCOWHEAD.get(), InfCowHeadRenderer::new)")) {
  throw new Error("ModClientEvents missing InfCowHead renderer registration");
}

const head = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfCowHeadEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 28",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_cow_head.func_78087_a"',
  "LEGACY_WIDTH = 0.7F",
  "LEGACY_HEIGHT = 0.9F",
  "LEGACY_EYE_HEIGHT = 0.8F",
  "LEGACY_HEAD_HEALTH = 0.0D",
  "LEGACY_HEAD_DAMAGE = 0.0D",
  "LEGACY_RUNTIME_HEALTH = 12.0D",
  "LEGACY_RUNTIME_DAMAGE = 4.0D",
  "LEGACY_MOVEMENT_SPEED = 0.3D",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_ATTACK_SPEED_TICKS = 15",
  "LEGACY_KILLCOUNT = -10",
  "LEGACY_SKILL_MIN_COOLDOWN = 40",
  "LEGACY_SKILL_MAX_COOLDOWN = 100",
  "LEGACY_SKILL_WINDUP = 3",
  "LEGACY_SKILL_ID = 1",
  "LEGACY_SKILL_STATUS = 14",
  "LEGACY_SKILL_LEAP_STRENGTH = 0.7F",
  "LEGACY_SKILL_LEAP_HEIGHT = 2.5D",
  "LEGACY_LEAP_STRENGTH = 0.4F",
  "LEGACY_AVOID_OR_ATTACK_HEALTH_FRACTION = 0.5F",
  "LEGACY_AVOID_OR_ATTACK_RANGE = 10",
  "LEGACY_AVOID_OR_ATTACK_STATUS = 2",
  "LEGACY_AVOID_ENTITY_RANGE = 8.0F",
  "LEGACY_AVOID_ENTITY_SPEED = 1.3D",
  "LEGACY_CAN_SPAWN_BY_ID_DATA = 0",
  "LEGACY_DISLO_SAVE_DATA_ID = 44",
  "LEGACY_DISLO_PHASE_THRESHOLD = 20",
  "LEGACY_CRUDE_PARTICLE_STATUS = 7",
  "canSpawnByIDData",
  "InfCowHeadSkillGoal",
  "InfCowHeadMeleeGoal",
  "InfCowHeadAvoidOrAttackGoal",
  "AvoidEntityGoal",
  "LeapAtTargetGoal",
  "ModSounds.INFECTEDHEAD_GROWL",
  "ModSounds.INFECTEDHEAD_HURT",
  "ModSounds.INFECTEDHEAD_DEATH",
  "ModSounds.SMALL_STEPS",
  "hasCrudeInhooMTargetSupport()",
  "hasDislodgementBodyUpgradeSupport()",
  "replaceWithBody()"
]) {
  if (!head.includes(marker)) throw new Error(`InfCowHeadEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfCowHeadRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfCowHeadEntity>",
  "geo/entity/inf_cow_head.geo.json",
  "animations/entity/inf_cow_head.animation.json",
  "textures/entity/monster/cowh.png",
  "InfCowHeadEntity.LEGACY_SHADOW_RADIUS"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfCowHeadRenderer missing marker: ${marker}`);
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_cow_head.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfCowHead geo format_version: ${geo.format_version}`);
const geometry = geo["minecraft:geometry"]?.[0];
if (!geometry) throw new Error("InfCowHead geo must contain one minecraft:geometry entry");
if (geometry.description.texture_width !== 64 || geometry.description.texture_height !== 46) {
  throw new Error(`InfCowHead geo must preserve the 64x46 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 110) throw new Error(`InfCowHead geo must preserve the 110 converted ModelInfCowHead bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_cow_head.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_cow_head.func_78087_a", "animation.inf_cow_head.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfCowHead animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length < 1) throw new Error(`${name} must keep the converted animated cow-head channels, found ${bonesWithChannels.length}`);
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
  "EntityInfCowHead",
  "ModelInfCowHead",
  "RenderInfCowHead",
  "sim_cowhead",
  "itemmobspawner_infcowhead",
  "cowh.png",
  "EntityAISkill(40, 100, 3, true, 14)",
  "EntityInhooM",
  "disloGiveBodies"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfCowHead marker: ${marker}`);
}

console.log("InfCowHead / Walking Cow Head entity port verifier passed.");
