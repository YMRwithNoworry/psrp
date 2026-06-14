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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHumanHeadEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfHumanHeadRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_human_head.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_human_head.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanh.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanh1.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanh2.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanhnocturn.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infhumanhead.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infhumanhead.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfHumanHead port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_humanhead"',
  "EntityType.Builder.of(InfHumanHeadEntity::new, MobCategory.MONSTER)",
  ".sized(InfHumanHeadEntity.LEGACY_WIDTH, InfHumanHeadEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfHumanHeadEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfHumanHead marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfHumanHeadEntity.createAttributes().build()", "ModEntities.INFHUMANHEAD.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfHumanHead marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFHUMANHEAD", '"itemmobspawner_infhumanhead"', "DeferredSpawnEggItem", "ModEntities.INFHUMANHEAD", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfHumanHead spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFHUMANHEAD = legacyItem("itemmobspawner_infhumanhead"')) {
  throw new Error("InfHumanHead spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFHUMANHEAD.get(), InfHumanHeadRenderer::new)")) {
  throw new Error("ModClientEvents missing InfHumanHead renderer registration");
}

const head = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHumanHeadEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 46",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_human_head.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_human_head.setRotationAnglesCosmical"',
  "LEGACY_WIDTH = 0.7F",
  "LEGACY_HEIGHT = 0.8F",
  "LEGACY_EYE_HEIGHT = 0.7F",
  "LEGACY_SHADOW_RADIUS = 0.6F",
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
  "LEGACY_VARIANT_SKIN_MIN = 1",
  "LEGACY_VARIANT_SKIN_COUNT = 3",
  "LEGACY_NOCTURN_SKIN = 10",
  "LEGACY_DISLO_SAVE_DATA_ID = 44",
  "LEGACY_DISLO_PHASE_THRESHOLD = 20",
  "LEGACY_CRUDE_PARTICLE_STATUS = 7",
  "LEGACY_FALL_DAMAGE_MULTIPLIER = 0.3F",
  "setSkin(LEGACY_VARIANT_SKIN_MIN + this.random.nextInt(LEGACY_VARIANT_SKIN_COUNT))",
  "tag.putInt(\"SrpSkin\", getSkin())",
  "canSpawnByIDData",
  "InfHumanHeadSkillGoal",
  "InfHumanHeadMeleeGoal",
  "InfHumanHeadAvoidOrAttackGoal",
  "AvoidEntityGoal",
  "LeapAtTargetGoal",
  "ModSounds.INFECTEDHEAD_GROWL",
  "ModSounds.INFECTEDHEAD_HURT",
  "ModSounds.INFECTEDHEAD_DEATH",
  "ModSounds.SMALL_STEPS",
  "hasCrudeInhooMTargetSupport()",
  "hasDislodgementBodyUpgradeSupport()",
  "replaceWithBody()",
  "ModEntities.INFHUMAN"
]) {
  if (!head.includes(marker)) throw new Error(`InfHumanHeadEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfHumanHeadRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfHumanHeadEntity>",
  "geo/entity/inf_human_head.geo.json",
  "animations/entity/inf_human_head.animation.json",
  "textures/entity/monster/humanh.png",
  "textures/entity/monster/humanh1.png",
  "textures/entity/monster/humanh2.png",
  "textures/entity/monster/humanhnocturn.png",
  "case 1 -> TEXTURE_1",
  "case InfHumanHeadEntity.LEGACY_NOCTURN_SKIN -> TEXTURE_NOCTURN",
  "InfHumanHeadEntity.LEGACY_SHADOW_RADIUS"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfHumanHeadRenderer missing marker: ${marker}`);
}

function pngSize(file) {
  const data = fs.readFileSync(path.join(root, file));
  if (data.readUInt32BE(0) !== 0x89504e47) throw new Error(`${file} is not a PNG`);
  return { width: data.readUInt32BE(16), height: data.readUInt32BE(20) };
}
for (const file of [
  "src/main/resources/assets/srparasites/textures/entity/monster/humanh.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanh1.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanh2.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/humanhnocturn.png"
]) {
  const size = pngSize(file);
  if (size.width !== 64 || size.height !== 32) throw new Error(`${file} must preserve 64x32 texture size, found ${size.width}x${size.height}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_humanhead"] !== "Walking Head" || enUs["entity.srparasites.sim_humanhead.name"] !== "Walking Head") {
  throw new Error("en_us must keep the legacy sim_humanhead Walking Head language keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_human_head.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfHumanHead geo format_version: ${geo.format_version}`);
const geometry = geo["minecraft:geometry"]?.[0];
if (!geometry) throw new Error("InfHumanHead geo must contain one minecraft:geometry entry");
if (geometry.description.texture_width !== 64 || geometry.description.texture_height !== 32) {
  throw new Error(`InfHumanHead geo must preserve the 64x32 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 78) throw new Error(`InfHumanHead geo must preserve the 78 converted ModelInfHumanHead bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_human_head.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_human_head.func_78087_a", "animation.inf_human_head.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfHumanHead animation names changed: ${JSON.stringify(animationNames)}`);
}
const vector = (value) => value.vector || value.post.vector;
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length < 1) throw new Error(`${name} must keep the converted animated human-head channels, found ${bonesWithChannels.length}`);
  const times = new Set();
  const modes = new Set();
  for (const channels of Object.values(data.bones || {})) {
    for (const keys of Object.values(channels)) {
      const sorted = Object.keys(keys).sort((a, b) => Number(a) - Number(b));
      const first = vector(keys[sorted[0]]);
      const last = vector(keys[sorted[sorted.length - 1]]);
      const loopDelta = Math.max(...first.map((value, index) => Math.abs(value - last[index])));
      if (loopDelta !== 0) throw new Error(`${name} must close its loop, found delta ${loopDelta}`);
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
  "EntityInfHumanHead",
  "ModelInfHumanHead",
  "RenderInfHumanHead",
  "sim_humanhead",
  "itemmobspawner_infhumanhead",
  "humanh.png",
  "humanh1.png",
  "humanh2.png",
  "humanhnocturn.png",
  "EntityAISkill(40, 100, 3, true, 14)",
  "EntityInhooM",
  "disloGiveBodies"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfHumanHead marker: ${marker}`);
}

console.log("InfHumanHead / Walking Human Head entity port verifier passed.");
