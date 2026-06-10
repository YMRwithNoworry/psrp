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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/AlafhaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/AlafhaBallEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/AlafhaRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/AlafhaBallRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/alafha.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/alafha.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/alafha.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/alafhah.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/alafha.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_alafha.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_alafha.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Alafha / Overseer port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("overseer"',
  "EntityType.Builder.of(AlafhaEntity::new, MobCategory.MONSTER)",
  ".sized(AlafhaEntity.LEGACY_WIDTH, AlafhaEntity.LEGACY_HEIGHT)",
  ".eyeHeight(AlafhaEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("salivaball"',
  "EntityType.Builder.<AlafhaBallEntity>of(AlafhaBallEntity::new, MobCategory.MISC)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Alafha marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["AlafhaEntity.createAttributes().build()", "ModEntities.ALAFHA.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Alafha attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_ALAFHA", '"itemmobspawner_alafha"', "DeferredSpawnEggItem", "ModEntities.ALAFHA", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Alafha spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_ALAFHA = legacyItem("itemmobspawner_alafha"')) {
  throw new Error("Alafha spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.ALAFHA.get(), AlafhaRenderer::new)",
  "event.registerEntityRenderer(ModEntities.ALAFHABALL.get(), AlafhaBallRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Alafha renderer registration: ${marker}`);
}

const alafha = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/AlafhaEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 9",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.alafha.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.alafha.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 80.0D",
  "LEGACY_ARMOR = 20.0D",
  "LEGACY_ATTACK_DAMAGE = 22.0D",
  "LEGACY_RANGED_ATTACK_DAMAGE = 30.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.4D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 1.9F",
  "LEGACY_HEIGHT = 2.6F",
  "LEGACY_EYE_HEIGHT = 1.6F",
  "LEGACY_TYPE = 9",
  "LEGACY_HEAVY_SKIN = 7",
  "LEGACY_ADAPTATION_CAP = 0.95F",
  "LEGACY_PROJECTILE_INTERVAL = 20",
  "LEGACY_PROJECTILE_MIN_DISTANCE = 10",
  "LEGACY_PROJECTILE_COUNT = 4",
  "LEGACY_MELEE_NOT_GROUND_SPEED = 4.5D",
  "LEGACY_MELEE_NOT_GROUND_RANGE = 16.0D",
  "LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.11D",
  "LEGACY_RANDOM_MOVE_SPEED_TARGET = 0.22D",
  "LEGACY_RANDOM_IDLE_RADIUS = 16.0D",
  "AlafhaProjectileGoal",
  "AirMeleeGoal",
  "RandomFlyGoal",
  "AlafhaMoveControl",
  "new AlafhaBallEntity(this.level(), this, xPower, yPower, zPower)",
  "ModSounds.ALAFHA_GROWL",
  "ModSounds.ALAFHA_HURT",
  "ModSounds.ALAFHA_DEATH",
  "ModSounds.ALAFHA_SHOOTING",
  "ModSounds.ALAFHA_SHOOTINGPOST"
]) {
  if (!alafha.includes(marker)) throw new Error(`AlafhaEntity missing legacy behavior marker: ${marker}`);
}

const projectile = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/AlafhaBallEntity.java");
for (const marker of [
  "LEGACY_DAMAGE = 30.0F",
  "LEGACY_HIT_AOE = 3.0D",
  "ModEntities.ALAFHABALL",
  "living instanceof SrpParasiteMob",
  "ModEffects.NEEDLER"
]) {
  if (!projectile.includes(marker)) throw new Error(`AlafhaBallEntity missing shared projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/AlafhaRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<AlafhaEntity>",
  "geo/entity/alafha.geo.json",
  "animations/entity/alafha.animation.json",
  "textures/entity/monster/alafha.png",
  "textures/entity/monster/alafhah.png",
  "AlafhaEntity.LEGACY_HEAVY_SKIN"
]) {
  if (!renderer.includes(marker)) throw new Error(`AlafhaRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.overseer"] !== "Overseer" || enUs["entity.srparasites.overseer.name"] !== "Overseer") {
  throw new Error("en_us.json missing Overseer entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_alafha"] !== "Spawn Overseer") {
  throw new Error("en_us.json missing Spawn Overseer item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/alafha.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Alafha geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Alafha geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 165) throw new Error(`Alafha geo must preserve the 165 converted ModelAlafha bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/alafha.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.alafha.func_78087_a", "animation.alafha.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Alafha animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 50) throw new Error(`${name} must keep 50 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityAlafha",
  "overseer",
  "itemmobspawner_alafha",
  "EntityProjectileAlafhaBall",
  "Alafha/Overseer",
  "spawnBiomassFromProjectile"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Alafha marker: ${marker}`);
}

console.log("Alafha / Overseer entity port verifier passed.");
