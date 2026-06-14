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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfSheepHeadEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfSheepHeadRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_sheep_head.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_sheep_head.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/sheeph.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infsheephead.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infsheephead.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfSheepHead port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_sheephead"',
  "EntityType.Builder.of(InfSheepHeadEntity::new, MobCategory.MONSTER)",
  ".sized(InfSheepHeadEntity.LEGACY_WIDTH, InfSheepHeadEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfSheepHeadEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfSheepHead marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfSheepHeadEntity.createAttributes().build()", "ModEntities.INFSHEEPHEAD.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfSheepHead marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFSHEEPHEAD", '"itemmobspawner_infsheephead"', "DeferredSpawnEggItem", "ModEntities.INFSHEEPHEAD", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfSheepHead spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFSHEEPHEAD = legacyItem("itemmobspawner_infsheephead"')) {
  throw new Error("InfSheepHead spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFSHEEPHEAD.get(), InfSheepHeadRenderer::new)")) {
  throw new Error("ModClientEvents missing InfSheepHead renderer registration");
}

const head = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfSheepHeadEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 22",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_sheep_head.func_78087_a"',
  "LEGACY_WIDTH = 0.7F",
  "LEGACY_HEIGHT = 0.7F",
  "LEGACY_EYE_HEIGHT = 0.6F",
  "LEGACY_SHADOW_RADIUS = 0.5F",
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
  "LEGACY_FALL_DAMAGE_MULTIPLIER = 0.3F",
  "canSpawnByIDData",
  "InfSheepHeadSkillGoal",
  "InfSheepHeadMeleeGoal",
  "InfSheepHeadAvoidOrAttackGoal",
  "AvoidEntityGoal",
  "LeapAtTargetGoal",
  "ModSounds.INFECTEDHEAD_GROWL",
  "ModSounds.INFECTEDHEAD_HURT",
  "ModSounds.INFECTEDHEAD_DEATH",
  "ModSounds.SMALL_STEPS",
  "hasCrudeInhooMTargetSupport()",
  "hasDislodgementBodyUpgradeSupport()",
  "replaceWithBody()",
  "ModEntities.INFSHEEP"
]) {
  if (!head.includes(marker)) throw new Error(`InfSheepHeadEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfSheepHeadRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfSheepHeadEntity>",
  "geo/entity/inf_sheep_head.geo.json",
  "animations/entity/inf_sheep_head.animation.json",
  "textures/entity/monster/sheeph.png",
  "InfSheepHeadEntity.LEGACY_SHADOW_RADIUS"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfSheepHeadRenderer missing marker: ${marker}`);
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_sheep_head.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfSheepHead geo format_version: ${geo.format_version}`);
const geometry = geo["minecraft:geometry"]?.[0];
if (!geometry) throw new Error("InfSheepHead geo must contain one minecraft:geometry entry");
if (geometry.description.texture_width !== 64 || geometry.description.texture_height !== 46) {
  throw new Error(`InfSheepHead geo must preserve the 64x46 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 108) throw new Error(`InfSheepHead geo must preserve the 108 converted ModelInfSheepHead bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_sheep_head.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_sheep_head.func_78087_a", "animation.inf_sheep_head.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfSheepHead animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length < 1) throw new Error(`${name} must keep the converted animated sheep-head channels, found ${bonesWithChannels.length}`);
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
  "EntityInfSheepHead",
  "ModelInfSheepHead",
  "RenderInfSheepHead",
  "sim_sheephead",
  "itemmobspawner_infsheephead",
  "sheeph.png",
  "EntityAISkill(40, 100, 3, true, 14)",
  "EntityInhooM",
  "disloGiveBodies"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfSheepHead marker: ${marker}`);
}

console.log("InfSheepHead / Walking Sheep Head entity port verifier passed.");
