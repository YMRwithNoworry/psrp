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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/deterrent/UnvoEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/SpineballEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/UnvoRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/SpineballRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/unvo.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/unvo.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/unvo.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/spineball.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_unvo.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_unvo.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Unvo/Sentry port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/snowvariants/sentryfrozen.png")) {
  throw new Error("Verifier expected sentryfrozen.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sentry"',
  "EntityType.Builder.of(UnvoEntity::new, MobCategory.MONSTER)",
  ".sized(UnvoEntity.LEGACY_WIDTH, UnvoEntity.LEGACY_HEIGHT)",
  ".eyeHeight(UnvoEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("spineball"',
  "EntityType.Builder.<SpineballEntity>of(SpineballEntity::new, MobCategory.MISC)",
  ".sized(SpineballEntity.LEGACY_WIDTH, SpineballEntity.LEGACY_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Unvo marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["UnvoEntity.createAttributes().build()", "ModEntities.UNVO.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Unvo attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_UNVO", '"itemmobspawner_unvo"', "DeferredSpawnEggItem", "ModEntities.UNVO"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Unvo spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_UNVO = legacyItem("itemmobspawner_unvo"')) {
  throw new Error("Unvo spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.UNVO.get(), UnvoRenderer::new)",
  "event.registerEntityRenderer(ModEntities.SPINEBALL.get(), SpineballRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing renderer registration: ${marker}`);
}

const unvo = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/deterrent/UnvoEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 30",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.unvo.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.unvo.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 30.0D",
  "LEGACY_ARMOR = 10.0D",
  "LEGACY_ATTACK_DAMAGE = 5.0D",
  "LEGACY_RANGE_ATTACK_DAMAGE = 25.0F",
  "LEGACY_MOVEMENT_SPEED = 0.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.7F",
  "LEGACY_HEIGHT = 4.1F",
  "LEGACY_EYE_HEIGHT = 3.6F",
  "LEGACY_XP = 110",
  "LEGACY_TYPE = 40",
  "LEGACY_BURIED_TIME = 5.1D",
  "LEGACY_PROJECTILE_COOLDOWN = 20",
  "LEGACY_PROJECTILE_SOUND_WINDUP = 10",
  "LEGACY_PROJECTILE_TICK_INTERVAL = 1",
  "LEGACY_PROJECTILE_SHOTS = 3",
  "LEGACY_PROJECTILE_RANGE_SQR = 4225.0D",
  "LEGACY_PROJECTILE_STATUS = 1",
  "LEGACY_POISON_DURATION_SECONDS = 7",
  "LEGACY_POISON_AMPLIFIER_CONFIG = 1",
  "LEGACY_GEAR_DAMAGE = 0.04D",
  "HurtByTargetGoal(this)",
  "MeleeAttackGoal(this, 1.0D, false)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "ProjectileAttackGoal",
  "playProjSound()",
  "getProj(double xPower, double yPower, double zPower)",
  "new SpineballEntity(this.level(), this, xPower, yPower, zPower)",
  "spineball.setDurationAmplifier",
  "spineball.setGearDamage",
  "ModSounds.UNVO_GROWL",
  "ModSounds.UNVO_HURT",
  "ModSounds.UNVO_DEATH",
  "ModSounds.UNVO_SHOOTING",
  "ModSounds.EMANA_SHOOTING",
  "ModSounds.MOB_SILENCE"
]) {
  if (!unvo.includes(marker)) throw new Error(`UnvoEntity missing legacy behavior marker: ${marker}`);
}

const spineball = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/SpineballEntity.java");
for (const marker of [
  "extends ThrowableItemProjectile",
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_DAMAGE = 25.0F",
  "LEGACY_POISON_DURATION_SECONDS = 7",
  "LEGACY_POISON_DURATION_TICKS = LEGACY_POISON_DURATION_SECONDS * 20",
  "LEGACY_CONFIG_POISON_AMPLIFIER = 1",
  "LEGACY_POISON_AMPLIFIER = LEGACY_CONFIG_POISON_AMPLIFIER - 1",
  "LEGACY_GEAR_DAMAGE_FRACTION = 0.04D",
  "LEGACY_GEAR_DAMAGE_THRESHOLD = 0.1D",
  "MobEffects.POISON",
  "living.hurt(this.damageSources().thrown(this, getOwner()), this.damage)",
  "living instanceof SrpParasiteMob",
  "damageArmor(living)",
  "stack.hurtAndBreak(damageAmount, living, slot)",
  "setDurationAmplifier(int durationSeconds, int amplifier)",
  "setGearDamage(double gearDamage)",
  "ParticleTypes.ITEM",
  "Items.SLIME_BALL"
]) {
  if (!spineball.includes(marker)) throw new Error(`SpineballEntity missing legacy behavior marker: ${marker}`);
}

const unvoRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/UnvoRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<UnvoEntity>",
  "geo/entity/unvo.geo.json",
  "animations/entity/unvo.animation.json",
  "textures/entity/monster/unvo.png",
  "this.shadowRadius = 0.5F"
]) {
  if (!unvoRenderer.includes(marker)) throw new Error(`UnvoRenderer missing renderer marker: ${marker}`);
}
if (unvoRenderer.includes("sentryfrozen.png")) {
  throw new Error("UnvoRenderer must not reference missing legacy sentryfrozen.png texture");
}

const spineballRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/SpineballRenderer.java");
for (const marker of [
  "extends EntityRenderer<SpineballEntity>",
  "textures/entity/projectile/spineball.png",
  "RenderType.entityCutoutNoCull(TEXTURE)",
  "this.entityRenderDispatcher.cameraOrientation()"
]) {
  if (!spineballRenderer.includes(marker)) throw new Error(`SpineballRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sentry"] !== "Sentry" || enUs["entity.srparasites.sentry.name"] !== "Sentry") {
  throw new Error("en_us.json missing Sentry entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_unvo"] !== "Spawn Sentry") {
  throw new Error("en_us.json missing Spawn Sentry item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/unvo.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Unvo geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Unvo geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 128) throw new Error(`Unvo geo must preserve the 128 converted ModelUnvo bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/unvo.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.unvo.func_78087_a", "animation.unvo.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Unvo animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 35) throw new Error(`${name} must keep 35 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Unvo / Sentry entity and Spineball projectile port verifier passed.");
