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
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModSounds.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHorseHeadEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfHorseHeadRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_horse_head.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_horse_head.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/horseh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infhorsehead.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infhorsehead.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfHorseHead port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_horsehead"',
  "EntityType.Builder.of(InfHorseHeadEntity::new, MobCategory.MONSTER)",
  ".sized(InfHorseHeadEntity.LEGACY_WIDTH, InfHorseHeadEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfHorseHeadEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfHorseHead marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfHorseHeadEntity.createAttributes().build()", "ModEntities.INFHORSEHEAD.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfHorseHead marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFHORSEHEAD", '"itemmobspawner_infhorsehead"', "DeferredSpawnEggItem", "ModEntities.INFHORSEHEAD", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfHorseHead spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFHORSEHEAD = legacyItem("itemmobspawner_infhorsehead"')) {
  throw new Error("InfHorseHead spawn egg must not remain a legacy placeholder item");
}

const sounds = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModSounds.java");
if (!sounds.includes("SMALL_STEPS = SMALL_STEP")) {
  throw new Error("ModSounds must expose the legacy SMALL_STEPS alias through small.step");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFHORSEHEAD.get(), InfHorseHeadRenderer::new)")) {
  throw new Error("ModClientEvents missing InfHorseHead renderer registration");
}

const head = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHorseHeadEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 45",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_horse_head.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_horse_head.setRotationAnglesCosmical"',
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
  "canSpawnByIDData",
  "InfHorseHeadSkillGoal",
  "InfHorseHeadMeleeGoal",
  "InfHorseHeadAvoidOrAttackGoal",
  "AvoidEntityGoal",
  "LeapAtTargetGoal",
  "ModSounds.INFECTEDHEAD_GROWL",
  "ModSounds.INFECTEDHEAD_HURT",
  "ModSounds.INFECTEDHEAD_DEATH",
  "ModSounds.SMALL_STEPS",
  "hasCrudeInhooMTargetSupport()",
  "return false"
]) {
  if (!head.includes(marker)) throw new Error(`InfHorseHeadEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfHorseHeadRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfHorseHeadEntity>",
  "geo/entity/inf_horse_head.geo.json",
  "animations/entity/inf_horse_head.animation.json",
  "textures/entity/monster/horseh.png",
  "InfHorseHeadEntity.LEGACY_SHADOW_RADIUS"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfHorseHeadRenderer missing marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_horsehead"] !== "Walking Head" || enUs["entity.srparasites.sim_horsehead.name"] !== "Walking Head") {
  throw new Error("en_us.json missing Walking Head entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infhorsehead"] !== "Spawn Walking Horse Head") {
  throw new Error("en_us.json missing Spawn Walking Horse Head item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_horse_head.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfHorseHead geo format_version: ${geo.format_version}`);
const geometry = geo["minecraft:geometry"]?.[0];
if (!geometry) throw new Error("InfHorseHead geo must contain one minecraft:geometry entry");
if (geometry.description.texture_width !== 64 || geometry.description.texture_height !== 46) {
  throw new Error(`InfHorseHead geo must preserve the 64x46 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 89) throw new Error(`InfHorseHead geo must preserve the 89 converted ModelInfHorseHead bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_horse_head.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_horse_head.func_78087_a", "animation.inf_horse_head.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfHorseHead animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 1) throw new Error(`${name} must keep 1 animated root channel, found ${bonesWithChannels.length}`);
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
  "EntityInfHorseHead",
  "ModelInfHorseHead",
  "RenderInfHorseHead",
  "sim_horsehead",
  "itemmobspawner_infhorsehead",
  "INFECTEDHEAD_GROWL",
  "EntityAISkill(40, 100, 3, true, 14)",
  "EntityInhooM",
  "current repo has no crude `EntityInhooM`"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfHorseHead marker: ${marker}`);
}

console.log("InfHorseHead / Walking Horse Head entity port verifier passed.");
