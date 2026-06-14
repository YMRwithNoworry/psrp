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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfCowEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfCowRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_cow.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_cow.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/cow.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infcow.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infcow.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfCow / Assimilated Cow port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_cow"',
  "EntityType.Builder.of(InfCowEntity::new, MobCategory.MONSTER)",
  ".sized(InfCowEntity.LEGACY_WIDTH, InfCowEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfCowEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfCow marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfCowEntity.createAttributes().build()", "ModEntities.INFCOW.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfCow attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFCOW", '"itemmobspawner_infcow"', "DeferredSpawnEggItem", "ModEntities.INFCOW", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfCow spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFCOW = legacyItem("itemmobspawner_infcow"')) {
  throw new Error("InfCow spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFCOW.get(), InfCowRenderer::new)")) {
  throw new Error("ModClientEvents missing InfCow renderer registration");
}

const infCow = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfCowEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 13",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_cow.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_cow.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 18.0D",
  "LEGACY_ARMOR = 5.0D",
  "LEGACY_ATTACK_DAMAGE = 7.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.4D",
  "LEGACY_MOVEMENT_SPEED = 0.20000000298023224D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.9F",
  "LEGACY_HEIGHT = 1.4F",
  "LEGACY_EYE_HEIGHT = 1.3F",
  "LEGACY_SHADOW_RADIUS = 0.5F",
  "LEGACY_TYPE = 11",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_FUSE_TIME = 40",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_SKILL_INTERVAL = 60",
  "LEGACY_SKILL_RANGE = 32",
  "LEGACY_SKILL_UNKNOWN = 8",
  "LEGACY_CHARGE_SKILL_ID = 1",
  "LEGACY_CHARGE_STATUS = 3",
  "LEGACY_CHARGE_END_STATUS = 2",
  "LEGACY_CHARGE_WINDUP_TICKS = 40",
  "LEGACY_CHARGE_STUCK_CANCEL_TICKS = 80",
  "LEGACY_CHARGE_TARGET_DISTANCE = 15.0D",
  "LEGACY_CHARGE_SPEED = 2.0D",
  "LEGACY_CHARGE_AOE_INFLATE_XZ = 1.0D",
  "LEGACY_CHARGE_AOE_INFLATE_Y = 0.0D",
  "LEGACY_CHARGE_AIR_DRAG = 0.7D",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 1.4F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 73",
  "LEGACY_LESH_LEGS = 0",
  "ChargeGoal",
  "doSpecialSkill",
  "damageChargeArea",
  "SrpChargeCooldown",
  "SrpChargeTicks",
  "SrpChargeTargetX",
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDCOW_GROWL",
  "ModSounds.INFECTEDCOW_HURT",
  "ModSounds.INFECTEDCOW_DEATH",
  "SoundEvents.COW_STEP"
]) {
  if (!infCow.includes(marker)) throw new Error(`InfCowEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfCowRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfCowEntity>",
  "geo/entity/inf_cow.geo.json",
  "animations/entity/inf_cow.animation.json",
  "textures/entity/monster/cow.png",
  "InfCowEntity.LEGACY_SHADOW_RADIUS",
  "entity.getSelfeFlashIntensity2()"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfCowRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_cow"] !== "Assimilated Cow" || enUs["entity.srparasites.sim_cow.name"] !== "Assimilated Cow") {
  throw new Error("en_us.json missing Assimilated Cow entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infcow"] !== "Spawn Assimilated Cow") {
  throw new Error("en_us.json missing Spawn Assimilated Cow item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_cow.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfCow geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfCow geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 54) throw new Error(`InfCow geo must preserve the 54 converted ModelInfCow bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_cow.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_cow.func_78087_a", "animation.inf_cow.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfCow animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 8) throw new Error(`${name} must keep 8 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityInfCow",
  "ModelInfCow",
  "RenderInfCow",
  "sim_cow",
  "itemmobspawner_infcow",
  "Assimilated Cow",
  "INFECTEDCOW_GROWL",
  "EntityInfCowHead",
  "EntityLesh",
  "EntityDamage",
  "skillBreakBlocks"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfCow marker: ${marker}`);
}

console.log("InfCow / Assimilated Cow entity port verifier passed.");
