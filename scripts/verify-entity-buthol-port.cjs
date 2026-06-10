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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/ButholEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ButholRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/buthol.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/buthol.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/buthol.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/butholone.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_buthol.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_buthol.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Buthol port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/sbuthol.png")) {
  throw new Error("Verifier expected sbuthol.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("carrier_flying"',
  "EntityType.Builder.of(ButholEntity::new, MobCategory.MONSTER)",
  ".sized(ButholEntity.LEGACY_WIDTH, ButholEntity.LEGACY_HEIGHT)",
  ".eyeHeight(ButholEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Buthol marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["ButholEntity.createAttributes().build()", "ModEntities.BUTHOL.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Buthol attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_BUTHOL", '"itemmobspawner_buthol"', "DeferredSpawnEggItem", "ModEntities.BUTHOL"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Buthol spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_BUTHOL = legacyItem("itemmobspawner_buthol"')) {
  throw new Error("Buthol spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.BUTHOL.get(), ButholRenderer::new)")) {
  throw new Error("ModClientEvents missing Buthol renderer registration");
}

const buthol = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/ButholEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 11",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.buthol.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.buthol.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 20.0D",
  "LEGACY_ATTACK_DAMAGE = 10.0D",
  "LEGACY_ARMOR = 2.5D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.15D",
  "LEGACY_WIDTH = 1.4F",
  "LEGACY_HEIGHT = 2.4F",
  "LEGACY_EYE_HEIGHT = 2.4F",
  "LEGACY_XP = 20",
  "LEGACY_TYPE = 31",
  "LEGACY_FUSE_TICKS = 30",
  "LEGACY_VARIANT_SKIN = 1",
  "LEGACY_EXPLOSION_RADIUS = 4.0F",
  "LEGACY_EFFECT_RADIUS = 4.0D",
  "LEGACY_VIRAL_TICKS = 400",
  "LEGACY_VIRAL_AMPLIFIER = 1",
  "LEGACY_VOMIT_TICKS = 400",
  "LEGACY_CLOUD_WAIT_TICKS = 10",
  "LEGACY_CLOUD_POISON_TICKS = 300",
  "LEGACY_CLOUD_COTH_VIRAL_TICKS = 3600",
  "LEGACY_CLOUD_RADIUS_MULTIPLIER = 3.5F",
  "LEGACY_GROUND_LIFT = 5.0D",
  "LEGACY_GROUND_LIFT_SPEED = 0.5D",
  "LEGACY_RANDOM_MOVE_CHANCE = 7",
  "LEGACY_RANDOM_MOVE_SPEED = 0.25D",
  "LEGACY_CHARGE_SPEED = 1.0D",
  "ButholMoveControl",
  "ChargeAttackGoal",
  "RandomFlyGoal",
  "ButholSwellGoal",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "setNoGravity(true)",
  "EventHooks.canEntityGrief",
  "Level.ExplosionInteraction.MOB",
  "AreaEffectCloud",
  "MobEffects.POISON",
  "ModEffects.VIRAL",
  "ModEffects.VOMIT",
  "SrpMobEffect.applyStackEffect",
  "ModSounds.CARRIER_GROWL",
  "ModSounds.CARRIER_HURT",
  "ModSounds.CARRIER_DEATH",
  "ModSounds.BUTHOL_BOOM"
]) {
  if (!buthol.includes(marker)) throw new Error(`ButholEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ButholRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<ButholEntity>",
  "geo/entity/buthol.geo.json",
  "animations/entity/buthol.animation.json",
  "textures/entity/monster/buthol.png",
  "textures/entity/monster/butholone.png",
  "this.shadowRadius = 1.3F"
]) {
  if (!renderer.includes(marker)) throw new Error(`ButholRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("sbuthol.png")) {
  throw new Error("ButholRenderer must not reference missing legacy sbuthol.png texture");
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.carrier_flying"] !== "Flying Carrier" || enUs["entity.srparasites.carrier_flying.name"] !== "Flying Carrier") {
  throw new Error("en_us.json missing Flying Carrier entity translation keys");
}
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");
if (zhCn["entity.srparasites.carrier_flying"] !== "飞行母体" || zhCn["entity.srparasites.carrier_flying.name"] !== "飞行母体") {
  throw new Error("zh_cn.json missing Flying Carrier entity translation keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/buthol.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Buthol geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Buthol geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 43) throw new Error(`Buthol geo must preserve the 43 converted ModelButhol bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/buthol.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.buthol.func_78087_a", "animation.buthol.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Buthol animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 9) throw new Error(`${name} must keep 9 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Buthol / Flying Carrier entity port verifier passed.");
