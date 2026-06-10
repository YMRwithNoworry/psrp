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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/derived/HebluEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/AlafhaBallEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/HebluRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/AlafhaBallRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/heblu.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/heblu.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/heblu.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/heblumc.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/alafha.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_heblu.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_heblu.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Heblu / Draconite port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("draconite"',
  "EntityType.Builder.of(HebluEntity::new, MobCategory.MONSTER)",
  ".sized(HebluEntity.LEGACY_WIDTH, HebluEntity.LEGACY_HEIGHT)",
  ".eyeHeight(HebluEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("salivaball"',
  "EntityType.Builder.<AlafhaBallEntity>of(AlafhaBallEntity::new, MobCategory.MISC)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Heblu marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["HebluEntity.createAttributes().build()", "ModEntities.HEBLU.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Heblu attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_HEBLU", '"itemmobspawner_heblu"', "DeferredSpawnEggItem", "ModEntities.HEBLU", "0x41307C"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Heblu spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_HEBLU = legacyItem("itemmobspawner_heblu"')) {
  throw new Error("Heblu spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.HEBLU.get(), HebluRenderer::new)",
  "event.registerEntityRenderer(ModEntities.ALAFHABALL.get(), AlafhaBallRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Heblu renderer registration: ${marker}`);
}

const heblu = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/derived/HebluEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 309",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.heblu.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.heblu.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 525.0D",
  "LEGACY_ARMOR = 30.0D",
  "LEGACY_ATTACK_DAMAGE = 210.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_MOVEMENT_SPEED = 0.27D",
  "LEGACY_FOLLOW_RANGE = 80.0D",
  "LEGACY_WIDTH = 2.4F",
  "LEGACY_HEIGHT = 3.8F",
  "LEGACY_EYE_HEIGHT = 2.0F",
  "LEGACY_TYPE = 14",
  "LEGACY_KILLCOUNT = -10",
  "LEGACY_CAN_MOD_RENDER = 0",
  "LEGACY_RANDOM_MOVE_CHANCE = 5",
  "LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.5D",
  "LEGACY_RANDOM_MOVE_SPEED_TARGET = 0.75D",
  "LEGACY_AOE_RANGE = 9.0D",
  "LEGACY_FIREBALL_WINDUP_TICKS = 20",
  "LEGACY_FIREBALL_COOLDOWN_TICKS = -45",
  "LEGACY_RAIN_COOLDOWN_TICKS = -60",
  "LEGACY_FIREBALL_MAX_DISTANCE_SQR = 4096.0D",
  "getFlyingState()",
  "FireballAttackGoal",
  "RandomFlyGoal",
  "HebluMoveControl",
  "new AlafhaBallEntity(this.level(), this, xPower, yPower, zPower)",
  "ModSounds.HEBLU_GROWL",
  "ModSounds.HEBLU_HURT",
  "ModSounds.HEBLU_DEATH",
  "ModSounds.HEBLU_STEP",
  "ModSounds.HEBLU_SHOOT",
  "ModEffects.RAGE"
]) {
  if (!heblu.includes(marker)) throw new Error(`HebluEntity missing legacy behavior marker: ${marker}`);
}

const projectile = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/AlafhaBallEntity.java");
for (const marker of [
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_DAMAGE = 30.0F",
  "LEGACY_HIT_AOE = 3.0D",
  "LEGACY_DLER_TICKS = 300",
  "LEGACY_DLER_CLOUD_TICKS = 360",
  "LEGACY_COTH_TICKS = 300",
  "LEGACY_CLOUD_RADIUS_HEBLU = 5.0F",
  "LEGACY_CLOUD_RADIUS = 2.0F",
  "LEGACY_CLOUD_WAIT_TICKS = 30",
  "LEGACY_CLOUD_DURATION_TICKS = 60",
  "ModEntities.ALAFHABALL",
  "living instanceof SrpParasiteMob",
  "ModEffects.NEEDLER",
  "ModEffects.VOMIT",
  "ModEffects.VIRAL",
  "SoundEvents.GLASS_BREAK",
  "ParticleTypes.EXPLOSION"
]) {
  if (!projectile.includes(marker)) throw new Error(`AlafhaBallEntity missing projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/HebluRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<HebluEntity>",
  "geo/entity/heblu.geo.json",
  "animations/entity/heblu.animation.json",
  "textures/entity/monster/heblu.png",
  "HebluEntity.LEGACY_SHADOW_RADIUS"
]) {
  if (!renderer.includes(marker)) throw new Error(`HebluRenderer missing renderer marker: ${marker}`);
}

const projectileRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/AlafhaBallRenderer.java");
for (const marker of ["textures/entity/projectile/alafha.png", "RenderType.entityCutoutNoCull", "cameraOrientation()"]) {
  if (!projectileRenderer.includes(marker)) throw new Error(`AlafhaBallRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (!String(enUs["entity.srparasites.draconite"]).includes("Draconite") || !String(enUs["entity.srparasites.draconite.name"]).includes("Draconite")) {
  throw new Error("en_us.json missing Draconite entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_heblu"] !== "Spawn Draconite") {
  throw new Error("en_us.json missing Spawn Draconite item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/heblu.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Heblu geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Heblu geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 356) throw new Error(`Heblu geo must preserve the 356 converted ModelHeblu bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/heblu.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.heblu.func_78087_a", "animation.heblu.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Heblu animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 65) throw new Error(`${name} must keep 65 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityHeblu",
  "draconite",
  "salivaball",
  "EntityProjectileAlafhaBall",
  "Heblu/Draconite",
  "DLER/COTH"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Heblu marker: ${marker}`);
}

console.log("Heblu / Draconite entity port verifier passed.");
