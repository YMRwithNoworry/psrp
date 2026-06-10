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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/ElviaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/ElviaBallEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ElviaRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ElviaBallRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/elvia.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/elvia.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/elvia.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/elvia.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_elvia.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_elvia.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Elvia / Wraith port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("wraith"',
  "EntityType.Builder.of(ElviaEntity::new, MobCategory.MONSTER)",
  ".sized(ElviaEntity.LEGACY_WIDTH, ElviaEntity.LEGACY_HEIGHT)",
  ".eyeHeight(ElviaEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("balltall"',
  "EntityType.Builder.<ElviaBallEntity>of(ElviaBallEntity::new, MobCategory.MISC)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Elvia marker: ${marker}`);
}
if (entities.includes('ENTITIES.register("ballmall"') && entities.includes("ELVIABALL = ENTITIES.register(\"ballmall\"")) {
  throw new Error("Elvia projectile must keep the legacy balltall id, not Anged's ballmall id");
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["ElviaEntity.createAttributes().build()", "ModEntities.ELVIA.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Elvia attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_ELVIA", '"itemmobspawner_elvia"', "DeferredSpawnEggItem", "ModEntities.ELVIA"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Elvia spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_ELVIA = legacyItem("itemmobspawner_elvia"')) {
  throw new Error("Elvia spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.ELVIA.get(), ElviaRenderer::new)",
  "event.registerEntityRenderer(ModEntities.ELVIABALL.get(), ElviaBallRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Elvia renderer registration: ${marker}`);
}

const elvia = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/ElviaEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 85",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.elvia.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.elvia.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 310.0D",
  "LEGACY_ARMOR = 15.5D",
  "LEGACY_ATTACK_DAMAGE = 70.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.15D",
  "LEGACY_FOLLOW_RANGE = 80.0D",
  "LEGACY_WIDTH = 4.0F",
  "LEGACY_HEIGHT = 4.0F",
  "LEGACY_EYE_HEIGHT = 2.1F",
  "LEGACY_ADAPTATION_CAP = 0.95D",
  "LEGACY_NEEDED_HEALTH_FRACTION = 0.4D",
  "LEGACY_RANDOM_MOVE_CHANCE = 7",
  "LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.6D",
  "LEGACY_RANDOM_MOVE_SPEED_FAR = 0.7D",
  "LEGACY_RANDOM_MOVE_SPEED_CLOSE = 0.75D",
  "LEGACY_CHARGE_CHANCE = 5",
  "LEGACY_CHARGE_TARGET_Y_OFFSET = 20.0D",
  "LEGACY_PROJECTILE_INTERVAL = 20",
  "LEGACY_PROJECTILE_MIN_DISTANCE = 10",
  "LEGACY_PROJECTILE_COUNT = 4",
  "LEGACY_AOE_INTERVAL = 10",
  "LEGACY_AOE_RANGE = 3.0D",
  "LEGACY_INVISIBILITY_TICKS = 25",
  "MobEffects.INVISIBILITY",
  "ChargeAttackGoal",
  "ElviaProjectileGoal",
  "RandomFlyGoal",
  "ElviaMoveControl",
  "ModSounds.ELVIA_GROWL",
  "ModSounds.ELVIA_HURT",
  "ModSounds.ELVIA_DEATH"
]) {
  if (!elvia.includes(marker)) throw new Error(`ElviaEntity missing legacy behavior marker: ${marker}`);
}
if (elvia.includes("NadeEntity") || elvia.includes("EntityProjectileNade")) {
  throw new Error("Elvia should use the registered NadeBallEntity handoff, not instantiate the delayed Nade body directly");
}

const projectile = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/ElviaBallEntity.java");
for (const marker of [
  "LEGACY_DAMAGE = 70.0F",
  "LEGACY_SHOOT_VELOCITY = 1.6F",
  "living instanceof SrpParasiteMob && !(living instanceof NakEntity)",
  "ParticleTypes.EXPLOSION",
  "damageSources().thrown(this, getOwner())"
]) {
  if (!projectile.includes(marker)) throw new Error(`ElviaBallEntity missing projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ElviaRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<ElviaEntity>",
  "geo/entity/elvia.geo.json",
  "animations/entity/elvia.animation.json",
  "textures/entity/monster/elvia.png",
  "ElviaEntity.LEGACY_SHADOW_RADIUS",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`ElviaRenderer missing renderer marker: ${marker}`);
}

const projectileRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ElviaBallRenderer.java");
for (const marker of ["textures/entity/projectile/elvia.png", "RenderType.entityCutoutNoCull", "cameraOrientation()"]) {
  if (!projectileRenderer.includes(marker)) throw new Error(`ElviaBallRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.wraith"] !== "Wraith" || enUs["entity.srparasites.wraith.name"] !== "Wraith") {
  throw new Error("en_us.json missing Wraith entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_elvia"] !== "Spawn Wraith") {
  throw new Error("en_us.json missing Spawn Wraith item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/elvia.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Elvia geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Elvia geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 289) throw new Error(`Elvia geo must preserve the 289 converted ModelElvia bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/elvia.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.elvia.func_78087_a", "animation.elvia.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Elvia animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 46) throw new Error(`${name} must keep 46 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityElvia",
  "wraith",
  "balltall",
  "EntityProjectileNade",
  "explicit future slice",
  "Elvia/Wraith"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Elvia marker: ${marker}`);
}

console.log("Elvia / Wraith entity port verifier passed.");
