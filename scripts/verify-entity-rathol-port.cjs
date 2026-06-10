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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/RatholEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/RatholRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/rathol.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/rathol.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/rathol.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/ratholone.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_rathol.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_rathol.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Rathol port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/srathol.png")) {
  throw new Error("Verifier expected srathol.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("carrier_heavy"',
  "EntityType.Builder.of(RatholEntity::new, MobCategory.MONSTER)",
  ".sized(RatholEntity.LEGACY_WIDTH, RatholEntity.LEGACY_HEIGHT)",
  ".eyeHeight(RatholEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Rathol marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["RatholEntity.createAttributes().build()", "ModEntities.RATHOL.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Rathol attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_RATHOL", '"itemmobspawner_rathol"', "DeferredSpawnEggItem", "ModEntities.RATHOL"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Rathol spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_RATHOL = legacyItem("itemmobspawner_rathol"')) {
  throw new Error("Rathol spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.RATHOL.get(), RatholRenderer::new)")) {
  throw new Error("ModClientEvents missing Rathol renderer registration");
}

const rathol = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/RatholEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 3",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.rathol.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.rathol.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 30.0D",
  "LEGACY_ATTACK_DAMAGE = 25.0D",
  "LEGACY_ARMOR = 5.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.95D",
  "LEGACY_MOVEMENT_SPEED = 0.2D",
  "LEGACY_WIDTH = 1.3F",
  "LEGACY_HEIGHT = 3.1F",
  "LEGACY_EYE_HEIGHT = 2.6F",
  "LEGACY_XP = 20",
  "LEGACY_TYPE = 41",
  "LEGACY_FUSE_TICKS = 70",
  "LEGACY_VARIANT_SKIN = 1",
  "LEGACY_VARIANT_MOVEMENT_SPEED = 0.3D",
  "LEGACY_EXPLOSION_RADIUS = 4.0F",
  "LEGACY_DEFAULT_EFFECT_RADIUS = 7.0D",
  "LEGACY_VARIANT_EFFECT_RADIUS = 11.0D",
  "LEGACY_DEFAULT_VIRAL_TICKS = 400",
  "LEGACY_DEFAULT_VIRAL_AMPLIFIER = 2",
  "LEGACY_VARIANT_VIRAL_AMPLIFIER = 4",
  "LEGACY_VOMIT_TICKS = 600",
  "LEGACY_CLOUD_WAIT_TICKS = 10",
  "LEGACY_CLOUD_POISON_TICKS = 300",
  "LEGACY_CLOUD_COTH_VIRAL_TICKS = 3600",
  "LEGACY_LOW_HEALTH_SELF_STATE_FRACTION = 0.05F",
  "RatholSwellGoal",
  "MeleeAttackGoal(this, 1.1D, false)",
  "RandomLookAroundGoal",
  "HurtByTargetGoal(this, RatholEntity.class)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
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
  "ModSounds.RATHOL_BOOM"
]) {
  if (!rathol.includes(marker)) throw new Error(`RatholEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/RatholRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<RatholEntity>",
  "geo/entity/rathol.geo.json",
  "animations/entity/rathol.animation.json",
  "textures/entity/monster/rathol.png",
  "textures/entity/monster/ratholone.png",
  "this.shadowRadius = 1.2F"
]) {
  if (!renderer.includes(marker)) throw new Error(`RatholRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("srathol.png")) {
  throw new Error("RatholRenderer must not reference missing legacy srathol.png texture");
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.carrier_heavy"] !== "Heavy Carrier" || enUs["entity.srparasites.carrier_heavy.name"] !== "Heavy Carrier") {
  throw new Error("en_us.json missing Heavy Carrier entity translation keys");
}
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");
if (!zhCn["entity.srparasites.carrier_heavy"] || !zhCn["entity.srparasites.carrier_heavy.name"]) {
  throw new Error("zh_cn.json missing Heavy Carrier entity translation keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/rathol.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Rathol geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Rathol geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 30) throw new Error(`Rathol geo must preserve the 30 converted ModelRathol bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/rathol.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.rathol.func_78087_a", "animation.rathol.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Rathol animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 12) throw new Error(`${name} must keep 12 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Rathol / Heavy Carrier entity port verifier passed.");
