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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/AngedEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/AngedballEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/AngedRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/AngedballRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/anged.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/anged.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/anged.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/angedh.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/tendrilanged.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/anged.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_anged.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_anged.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Anged / Vigilante port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("vigilante"',
  "EntityType.Builder.of(AngedEntity::new, MobCategory.MONSTER)",
  ".sized(AngedEntity.LEGACY_WIDTH, AngedEntity.LEGACY_HEIGHT)",
  ".eyeHeight(AngedEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("ballmall"',
  "EntityType.Builder.<AngedballEntity>of(AngedballEntity::new, MobCategory.MISC)",
  ".sized(AngedballEntity.LEGACY_WIDTH, AngedballEntity.LEGACY_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Anged marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["AngedEntity.createAttributes().build()", "ModEntities.ANGED.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Anged attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_ANGED", '"itemmobspawner_anged"', "DeferredSpawnEggItem", "ModEntities.ANGED"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Anged spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_ANGED = legacyItem("itemmobspawner_anged"')) {
  throw new Error("Anged spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.ANGED.get(), AngedRenderer::new)",
  "event.registerEntityRenderer(ModEntities.ANGEDBALL.get(), AngedballRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Anged renderer marker: ${marker}`);
}

const anged = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/AngedEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 25",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.anged.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.anged.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 70.0D",
  "LEGACY_ARMOR = 25.0D",
  "LEGACY_ATTACK_DAMAGE = 23.0D",
  "LEGACY_RANGED_ATTACK_DAMAGE = 27.0D",
  "LEGACY_MOVEMENT_SPEED = 0.2D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 1.6F",
  "LEGACY_HEIGHT = 3.1F",
  "LEGACY_EYE_HEIGHT = 3.0F",
  "LEGACY_TYPE = 51",
  "LEGACY_HEAVY_SKIN = 7",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_RANGE_SPEED = 1.5D",
  "LEGACY_RANGE_ATTACK_INTERVAL = 20",
  "LEGACY_RANGE_FACTOR = LEGACY_FOLLOW_RANGE / 2.0D",
  "LEGACY_TENDRIL_HEALTH_FRACTION = 0.5D",
  "LEGACY_LEFT_TENDRIL_DEAD_EVENT = 11",
  "LEGACY_ATTACK_EVENT = 12",
  "LEGACY_RIGHT_TENDRIL_DEAD_EVENT = 22",
  "parasiteleftTendril",
  "parasiterightTendril",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "AngedMeleeGoal",
  "AngedRangedGoal",
  "getProj(double xPower, double yPower, double zPower)",
  "AngedballEntity",
  "ModSounds.EMANA_SHOOTING",
  "ModSounds.MOB_SILENCE",
  "ModSounds.ANGED_GROWL",
  "ModSounds.ANGED_HURT",
  "ModSounds.ANGED_DEATH",
  "ModSounds.STEP_HEAVY",
  "getSelfeFlashIntensity"
]) {
  if (!anged.includes(marker)) throw new Error(`AngedEntity missing legacy behavior marker: ${marker}`);
}

const projectile = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/AngedballEntity.java");
for (const marker of [
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_DAMAGE = 27.0F",
  "LEGACY_CLOUD_RADIUS = 2.5F",
  "LEGACY_CLOUD_RADIUS_ON_USE = -0.5F",
  "LEGACY_CLOUD_WAIT_TICKS = 10",
  "LEGACY_CLOUD_DURATION_TICKS = 100",
  "LEGACY_POISON_TICKS = 300",
  "LEGACY_CORROSIVE_TICKS = 100",
  "LEGACY_SHOOT_VELOCITY = 1.6F",
  "ThrowableItemProjectile",
  "ModEntities.ANGEDBALL",
  "ParticleTypes.ITEM",
  "AreaEffectCloud",
  "MobEffects.POISON",
  "ModEffects.CORROSIVE",
  "SrpParasiteMob",
  "NakEntity"
]) {
  if (!projectile.includes(marker)) throw new Error(`AngedballEntity missing projectile marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/AngedRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<AngedEntity>",
  "geo/entity/anged.geo.json",
  "animations/entity/anged.animation.json",
  "textures/entity/monster/anged.png",
  "textures/entity/monster/angedh.png",
  "this.shadowRadius = 1.2F",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`AngedRenderer missing renderer marker: ${marker}`);
}

const projectileRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/AngedballRenderer.java");
for (const marker of ["EntityRenderer<AngedballEntity>", "textures/entity/projectile/anged.png", "RenderType.entityCutoutNoCull", "poseStack.scale(0.6F, 0.6F, 0.6F)"]) {
  if (!projectileRenderer.includes(marker)) throw new Error(`AngedballRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.vigilante"] !== "Vigilante" || enUs["entity.srparasites.vigilante.name"] !== "Vigilante") {
  throw new Error("en_us.json missing Vigilante entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_anged"] !== "Spawn Vigilante") {
  throw new Error("en_us.json missing Spawn Vigilante item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/anged.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Anged geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Anged geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 130) throw new Error(`Anged geo must preserve the 130 converted ModelAnged bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/anged.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.anged.func_78087_a", "animation.anged.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Anged animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 24) throw new Error(`${name} must keep 24 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Anged / Vigilante entity and Angedball projectile port verifier passed.");
