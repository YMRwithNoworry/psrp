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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/derived/KirinEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/KirinRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/kirin.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/kirin.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/kirin.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/testb.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_kirin.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_kirin.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Kirin port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("kirin"',
  "EntityType.Builder.of(KirinEntity::new, MobCategory.MONSTER)",
  ".sized(KirinEntity.LEGACY_WIDTH, KirinEntity.LEGACY_HEIGHT)",
  ".eyeHeight(KirinEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Kirin marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["KirinEntity.createAttributes().build()", "ModEntities.KIRIN.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Kirin attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_KIRIN", '"itemmobspawner_kirin"', "DeferredSpawnEggItem", "ModEntities.KIRIN"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Kirin spawn egg marker: ${marker}`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.KIRIN.get(), KirinRenderer::new)")) {
  throw new Error("ModClientEvents missing Kirin renderer registration");
}

const kirin = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/derived/KirinEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 67",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.kirin.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.kirin.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 410.0D",
  "LEGACY_ARMOR = 30.0D",
  "LEGACY_ATTACK_DAMAGE = 155.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_MOVEMENT_SPEED = 0.24D",
  "LEGACY_FOLLOW_RANGE = 80.0D",
  "LEGACY_WIDTH = 2.1271334F",
  "LEGACY_HEIGHT = 7.1F",
  "LEGACY_EYE_HEIGHT = 5.7F",
  "LEGACY_BLINK_CHARGE_TICKS = 60",
  "LEGACY_BLINK_COOLDOWN_TICKS = 200",
  "LEGACY_BLINK_MIN_DISTANCE_SQR = 256.0D",
  "LEGACY_BLINK_MAX_TRIES = 64",
  "LEGACY_BLINK_RADIUS_MIN = 1.5D",
  "LEGACY_BLINK_RADIUS_RANDOM = 22.5D",
  "LEGACY_BLINK_LIFESTEAL_RADIUS = 5.0D",
  "LEGACY_BLINK_LIFESTEAL_FRACTION = 0.5F",
  "LEGACY_FLOAT_GROUND_SCAN = 24",
  "LEGACY_FLOAT_HOVER_HEIGHT = 0.35D",
  "LEGACY_FLOAT_BOB_AMPLITUDE = 0.06D",
  "LEGACY_FLOAT_UP_MAX = 0.16D",
  "LEGACY_FLOAT_DOWN_MAX = -0.16D",
  "LEGACY_RECOVERY_BLINK_RADIUS = 48",
  "LEGACY_RECOVERY_BLINK_VERTICAL = 20",
  "LEGACY_RECOVERY_NO_GROUND_TICKS = 40",
  "Attributes.STEP_HEIGHT, 1.0D",
  "ParticleTypes.PORTAL",
  "updateFloating()",
  "tryBlinkToNearbyLand",
  "KirinBlinkGoal",
  "isIndoors",
  "findBlinkSpotNear",
  "doBlinkLifeSteal",
  "victim.setHealth(Math.max(0.0F, health - stolen))",
  "this.kirin.heal(stolen)"
]) {
  if (!kirin.includes(marker)) throw new Error(`KirinEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/KirinRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<KirinEntity>",
  "geo/entity/kirin.geo.json",
  "animations/entity/kirin.animation.json",
  "textures/entity/monster/kirin.png",
  "this.shadowRadius = 1.3F"
]) {
  if (!renderer.includes(marker)) throw new Error(`KirinRenderer missing renderer marker: ${marker}`);
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/kirin.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Kirin geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Kirin geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 141) throw new Error(`Kirin geo must preserve the 141 converted ModelKirin bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/kirin.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.kirin.func_78087_a", "animation.kirin.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Kirin animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length === 0) throw new Error(`${name} has no animated bones`);
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

console.log("Kirin entity port verifier passed.");
