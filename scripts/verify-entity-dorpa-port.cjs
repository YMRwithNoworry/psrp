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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/DorpaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/WebballEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/DorpaRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/WebballRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/dorpa.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/dorpa.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/dorpa.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/dorpa2.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/webball.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_dorpa.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_dorpa.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Dorpa / Assimilated Big Spider port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_bigspider"',
  "EntityType.Builder.of(DorpaEntity::new, MobCategory.MONSTER)",
  ".sized(DorpaEntity.LEGACY_WIDTH, DorpaEntity.LEGACY_HEIGHT)",
  ".eyeHeight(DorpaEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("webball"',
  "EntityType.Builder.<WebballEntity>of(WebballEntity::new, MobCategory.MISC)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Dorpa/Webball marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["DorpaEntity.createAttributes().build()", "ModEntities.DORPA.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Dorpa attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_DORPA", '"itemmobspawner_dorpa"', "DeferredSpawnEggItem", "ModEntities.DORPA", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Dorpa spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_DORPA = legacyItem("itemmobspawner_dorpa"')) {
  throw new Error("Dorpa spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.DORPA.get(), DorpaRenderer::new)",
  "event.registerEntityRenderer(ModEntities.WEBBALL.get(), WebballRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Dorpa/Webball renderer registration: ${marker}`);
}

const dorpa = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/DorpaEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 2",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.dorpa.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.dorpa.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 22.0D",
  "LEGACY_ARMOR = 3.0D",
  "LEGACY_ATTACK_DAMAGE = 9.0D",
  "LEGACY_RANGED_ATTACK_DAMAGE = 4.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.5D",
  "LEGACY_MOVEMENT_SPEED = 0.27D",
  "LEGACY_WIDTH = 1.9F",
  "LEGACY_HEIGHT = 2.1F",
  "LEGACY_EYE_HEIGHT = 1.75F",
  "LEGACY_SHADOW_RADIUS = 1.2F",
  "LEGACY_TYPE = 14",
  "LEGACY_KILLCOUNT = -10",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_VARIANT_SKIN = 1",
  "LEGACY_VARIANT_RANDOM_BOUND = 3",
  "LEGACY_VARIANT_HEALTH_MULTIPLIER = 0.5D",
  "LEGACY_VARIANT_DAMAGE_MULTIPLIER = 1.25D",
  "LEGACY_WITHER_TICKS = 40",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_PROJECTILE_INTERVAL = 60",
  "LEGACY_PROJECTILE_MIN_DISTANCE = 15",
  "LEGACY_PROJECTILE_COUNT = 3",
  "WallClimberNavigation",
  "MeleeAttackGoal",
  "DorpaWebballGoal",
  "new WebballEntity(this.level(), this, xPower, yPower, zPower, LEGACY_WEBBALL_TYPE)",
  "MobEffects.WITHER",
  "ModSounds.INFECTEDSPIDER_GROWL",
  "ModSounds.INFECTEDSPIDER_HURT",
  "ModSounds.INFECTEDSPIDER_DEATH",
  "ModSounds.INFECTEDSPIDER_STEP",
  "ModSounds.DORPA_RANGE"
]) {
  if (!dorpa.includes(marker)) throw new Error(`DorpaEntity missing legacy behavior marker: ${marker}`);
}

const webball = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/WebballEntity.java");
for (const marker of [
  "LEGACY_ENTITY_ID = 101",
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "MobEffects.BLINDNESS",
  "Blocks.COBWEB",
  "EventHooks.canEntityGrief"
]) {
  if (!webball.includes(marker)) throw new Error(`WebballEntity missing shared projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/DorpaRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<DorpaEntity>",
  "geo/entity/dorpa.geo.json",
  "animations/entity/dorpa.animation.json",
  "textures/entity/monster/dorpa.png",
  "textures/entity/monster/dorpa2.png",
  "DorpaEntity.LEGACY_VARIANT_SKIN",
  "poseStack.scale(0.78F, 0.78F, 0.78F)"
]) {
  if (!renderer.includes(marker)) throw new Error(`DorpaRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_bigspider"] !== "Assimilated Big Spider" || enUs["entity.srparasites.sim_bigspider.name"] !== "Assimilated Big Spider") {
  throw new Error("en_us.json missing Assimilated Big Spider entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_dorpa"] !== "Spawn Assimilated Big Spider") {
  throw new Error("en_us.json missing Spawn Assimilated Big Spider item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/dorpa.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Dorpa geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Dorpa geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 118) throw new Error(`Dorpa geo must preserve the 118 converted ModelDorpa bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/dorpa.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.dorpa.func_78087_a", "animation.dorpa.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Dorpa animation names changed: ${JSON.stringify(animationNames)}`);
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
  "EntityDorpa",
  "sim_bigspider",
  "itemmobspawner_dorpa",
  "Assimilated Big Spider",
  "DORPA_RANGE",
  "EntityProjectileWebball"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Dorpa marker: ${marker}`);
}

console.log("Dorpa / Assimilated Big Spider entity port verifier passed.");
