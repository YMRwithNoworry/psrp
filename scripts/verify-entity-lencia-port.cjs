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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/LenciaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/LenciaBallEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/LenciaRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/LenciaBallRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/lencia.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/lencia.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/lencia.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/lencia.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_lencia.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_lencia.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Lencia / Bogle port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("bogle"',
  "EntityType.Builder.of(LenciaEntity::new, MobCategory.MONSTER)",
  ".sized(LenciaEntity.LEGACY_WIDTH, LenciaEntity.LEGACY_HEIGHT)",
  ".eyeHeight(LenciaEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("ballmall"',
  "EntityType.Builder.<LenciaBallEntity>of(LenciaBallEntity::new, MobCategory.MISC)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Lencia marker: ${marker}`);
}
if (!entities.includes('ANGEDBALL = ENTITIES.register("ballball"')) {
  throw new Error("Angedball must keep the legacy ballball id so ballmall remains reserved for Lencia");
}
if (entities.includes('ANGEDBALL = ENTITIES.register("ballmall"') || entities.includes('ELVIABALL = ENTITIES.register("ballmall"')) {
  throw new Error("ballmall must not be reused by Angedball or ElviaBall");
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["LenciaEntity.createAttributes().build()", "ModEntities.LENCIA.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Lencia attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_LENCIA", '"itemmobspawner_lencia"', "DeferredSpawnEggItem", "ModEntities.LENCIA"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Lencia spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_LENCIA = legacyItem("itemmobspawner_lencia"')) {
  throw new Error("Lencia spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.LENCIA.get(), LenciaRenderer::new)",
  "event.registerEntityRenderer(ModEntities.LENCIABALL.get(), LenciaBallRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Lencia renderer registration: ${marker}`);
}

const lencia = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/LenciaEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 86",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.lencia.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.lencia.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 310.0D",
  "LEGACY_ARMOR = 15.5D",
  "LEGACY_ATTACK_DAMAGE = 70.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.15D",
  "LEGACY_FOLLOW_RANGE = 80.0D",
  "LEGACY_MOVEMENT_SPEED = 2.0D",
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
  "LEGACY_PROJECTILE_INTERVAL = 60",
  "LEGACY_PROJECTILE_MIN_DISTANCE = 30",
  "LEGACY_PROJECTILE_COUNT = 3",
  "LEGACY_AOE_INTERVAL = 10",
  "LEGACY_AOE_RANGE = 3.0D",
  "LEGACY_INVISIBILITY_TICKS = 25",
  "MobEffects.INVISIBILITY",
  "ChargeAttackGoal",
  "LenciaProjectileGoal",
  "RandomFlyGoal",
  "LenciaMoveControl",
  "new LenciaBallEntity(this.level(), this, xPower, yPower, zPower)",
  "ModSounds.LENCIA_GROWL",
  "ModSounds.LENCIA_HURT",
  "ModSounds.LENCIA_DEATH"
]) {
  if (!lencia.includes(marker)) throw new Error(`LenciaEntity missing legacy behavior marker: ${marker}`);
}
if (lencia.includes("NadeBallEntity") || lencia.includes("projectileCycleCount")) {
  throw new Error("Lencia must not inherit Elvia's alternating Nade projectile cycle");
}

const projectile = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/LenciaBallEntity.java");
for (const marker of [
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_DAMAGE = 70.0F",
  "LEGACY_EXPLOSION_RADIUS = 10.0F",
  "LEGACY_SHOOT_VELOCITY = 1.6F",
  "ModEntities.LENCIABALL",
  "living instanceof SrpParasiteMob && !(living instanceof NakEntity)",
  "damageSources().thrown(this, getOwner())",
  "Level.ExplosionInteraction.NONE",
  "ParticleTypes.EXPLOSION"
]) {
  if (!projectile.includes(marker)) throw new Error(`LenciaBallEntity missing projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/LenciaRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<LenciaEntity>",
  "geo/entity/lencia.geo.json",
  "animations/entity/lencia.animation.json",
  "textures/entity/monster/lencia.png",
  "LenciaEntity.LEGACY_SHADOW_RADIUS",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`LenciaRenderer missing renderer marker: ${marker}`);
}

const projectileRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/LenciaBallRenderer.java");
for (const marker of ["textures/entity/projectile/lencia.png", "RenderType.entityCutoutNoCull", "cameraOrientation()"]) {
  if (!projectileRenderer.includes(marker)) throw new Error(`LenciaBallRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.bogle"] !== "Bogle" || enUs["entity.srparasites.bogle.name"] !== "Bogle") {
  throw new Error("en_us.json missing Bogle entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_lencia"] !== "Spawn Bogle") {
  throw new Error("en_us.json missing Spawn Bogle item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/lencia.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Lencia geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Lencia geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 317) throw new Error(`Lencia geo must preserve the 317 converted ModelLencia bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/lencia.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.lencia.func_78087_a", "animation.lencia.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Lencia animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 62) throw new Error(`${name} must keep 62 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityLencia",
  "bogle",
  "ballmall",
  "EntityProjectileLenciaBall",
  "Lencia/Bogle"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Lencia marker: ${marker}`);
}

console.log("Lencia / Bogle entity port verifier passed.");
