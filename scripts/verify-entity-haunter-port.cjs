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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/HaunterEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/HommingballEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/HaunterRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/HommingballRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/pheon.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/pheon.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/pheon.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/pheonsp1.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/projectileh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_pheon.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_pheon.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Haunter / Pheon port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("haunter"',
  "EntityType.Builder.of(HaunterEntity::new, MobCategory.MONSTER)",
  ".sized(HaunterEntity.LEGACY_WIDTH, HaunterEntity.LEGACY_HEIGHT)",
  ".eyeHeight(HaunterEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("homming"',
  "EntityType.Builder.<HommingballEntity>of(HommingballEntity::new, MobCategory.MISC)",
  ".sized(HommingballEntity.LEGACY_WIDTH, HommingballEntity.LEGACY_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Haunter marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["HaunterEntity.createAttributes().build()", "ModEntities.HAUNTER.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Haunter attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_PHEON", '"itemmobspawner_pheon"', "DeferredSpawnEggItem", "ModEntities.HAUNTER"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Haunter spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_PHEON = legacyItem("itemmobspawner_pheon"')) {
  throw new Error("Haunter spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.HAUNTER.get(), HaunterRenderer::new)",
  "event.registerEntityRenderer(ModEntities.HOMMING.get(), HommingballRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Haunter renderer marker: ${marker}`);
}

const haunter = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/HaunterEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 87",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.pheon.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.pheon.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 360.0D",
  "LEGACY_ARMOR = 15.5D",
  "LEGACY_ATTACK_DAMAGE = 110.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.15D",
  "LEGACY_MOVEMENT_SPEED = 0.283D",
  "LEGACY_FOLLOW_RANGE = 80.0D",
  "LEGACY_WIDTH = 2.0F",
  "LEGACY_HEIGHT = 3.6F",
  "LEGACY_EYE_HEIGHT = 4.7F",
  "LEGACY_TYPE = 63",
  "LEGACY_VARIANT_SKIN = 1",
  "LEGACY_VARIANT_HEALTH_MULTIPLIER = 0.5D",
  "LEGACY_VARIANT_DAMAGE_MULTIPLIER = 1.5D",
  "LEGACY_AOE_RANGE_XZ = 5.0D",
  "LEGACY_AOE_RANGE_Y = 2.0D",
  "LEGACY_CROWDED_AOE_RANGE_Y = 3.0D",
  "LEGACY_CROWDED_AOE_THRESHOLD = 4",
  "LEGACY_CROWDED_AOE_DAMAGE = 40.0D",
  "LEGACY_RANGED_INTERVAL = 40",
  "LEGACY_RANGED_RANGE = 40.0F",
  "LEGACY_HOMMING_DAMAGE = 15.0F",
  "LEGACY_PROJECTILE_INTERVAL = 60",
  "LEGACY_PROJECTILE_MIN_DISTANCE = 10",
  "LEGACY_PROJECTILE_COUNT = 3",
  "HaunterMeleeGoal",
  "HaunterRangedGoal",
  "HaunterProjectileGoal",
  "getProj(double xPower, double yPower, double zPower)",
  "HommingballEntity",
  "ModSounds.DORPA_RANGE",
  "ModSounds.MOB_SILENCE"
]) {
  if (!haunter.includes(marker)) throw new Error(`HaunterEntity missing legacy behavior marker: ${marker}`);
}

const projectile = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/HommingballEntity.java");
for (const marker of [
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_SHOOT_VELOCITY = 1.2F",
  "LEGACY_DAMAGE = 15.0F",
  "LEGACY_HOMING_ACCELERATION = 0.12D",
  "LEGACY_LIFETIME = 120",
  "ThrowableItemProjectile",
  "ModEntities.HOMMING",
  "ParticleTypes.ITEM",
  "SrpParasiteMob"
]) {
  if (!projectile.includes(marker)) throw new Error(`HommingballEntity missing projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/HaunterRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<HaunterEntity>",
  "geo/entity/pheon.geo.json",
  "animations/entity/pheon.animation.json",
  "textures/entity/monster/pheon.png",
  "textures/entity/monster/pheonsp1.png",
  "HaunterEntity.LEGACY_SHADOW_RADIUS",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`HaunterRenderer missing renderer marker: ${marker}`);
}

const projectileRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/HommingballRenderer.java");
for (const marker of ["EntityRenderer<HommingballEntity>", "textures/entity/projectile/projectileh.png", "RenderType.entityCutoutNoCull", "poseStack.scale(0.6F, 0.6F, 0.6F)"]) {
  if (!projectileRenderer.includes(marker)) throw new Error(`HommingballRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.haunter"] !== "Haunter" || enUs["entity.srparasites.haunter.name"] !== "Haunter") {
  throw new Error("en_us.json missing Haunter entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_pheon"] !== "Spawn Haunter") {
  throw new Error("en_us.json missing Spawn Haunter item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/pheon.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Pheon geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Pheon geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 275) throw new Error(`Pheon geo must preserve the 275 converted ModelPheon bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/pheon.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.pheon.func_78087_a", "animation.pheon.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Pheon animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 37) throw new Error(`${name} must keep 37 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Haunter / Pheon entity and Homming projectile port verifier passed.");
