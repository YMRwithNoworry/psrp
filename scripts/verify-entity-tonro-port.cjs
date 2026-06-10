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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/deterrent/TonroEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/TonroRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/tonro.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/tonro.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/tonro.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_tonro.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_tonro.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Tonro port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/snowvariants/kyphosisfrozen.png")) {
  throw new Error("Verifier expected kyphosisfrozen.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("kyphosis"',
  "EntityType.Builder.of(TonroEntity::new, MobCategory.MONSTER)",
  ".sized(TonroEntity.LEGACY_WIDTH, TonroEntity.LEGACY_HEIGHT)",
  ".eyeHeight(TonroEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Tonro marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["TonroEntity.createAttributes().build()", "ModEntities.TONRO.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Tonro attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_TONRO", '"itemmobspawner_tonro"', "DeferredSpawnEggItem", "ModEntities.TONRO"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Tonro spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_TONRO = legacyItem("itemmobspawner_tonro"')) {
  throw new Error("Tonro spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.TONRO.get(), TonroRenderer::new)")) {
  throw new Error("ModClientEvents missing Tonro renderer registration");
}

const tonro = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/deterrent/TonroEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 29",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.tonro.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.tonro.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 50.0D",
  "LEGACY_ARMOR = 15.0D",
  "LEGACY_ATTACK_DAMAGE = 15.0D",
  "LEGACY_SWING_ATTACK_DAMAGE = 35.0D",
  "LEGACY_MOVEMENT_SPEED = 0.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_FOLLOW_RANGE = 20.0D",
  "LEGACY_WIDTH = 0.7F",
  "LEGACY_HEIGHT = 4.5F",
  "LEGACY_EYE_HEIGHT = 3.8F",
  "LEGACY_XP = 110",
  "LEGACY_TYPE = 40",
  "LEGACY_BURIED_TIME = 7.5D",
  "LEGACY_MELEE_REACH = 8.0D",
  "LEGACY_MELEE_LOS_REACH = 7.0D",
  "LEGACY_AOE_INFLATE = 2.0D",
  "LEGACY_TARGET_LAUNCH = 0.5000000059604645D",
  "LEGACY_ATTACK_TIMER_UP_STEP = 0.15F",
  "LEGACY_ATTACK_TIMER_DOWN_STEP = 0.2F",
  "LEGACY_SHOCKWAVE_STATUS = 10",
  "LEGACY_SHOCKWAVE_COOLDOWN_TICKS = 20",
  "LEGACY_SHOCKWAVE_TRIGGER_RANGE = 16",
  "LEGACY_SHOCKWAVE_FIRST_BORDER = 3",
  "LEGACY_SHOCKWAVE_SECOND_BORDER = 5",
  "LEGACY_SHOCKWAVE_FINISH_BORDER = 6",
  "LEGACY_SHOCKWAVE_DAMAGE_MULTIPLIER = 0.3D",
  "LEGACY_MIN_DAMAGE = 2.0F",
  "LEGACY_ATTACK_EVENT = 12",
  "LEGACY_FLAME_EVENT = 100",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "StationaryMeleeAoeGoal",
  "ShockwaveGoal",
  "attackEntityAsMobAOE",
  "ModSounds.MOB_SWIPE",
  "ModSounds.MOB_SILENCE",
  "ModSounds.TONRO_GROWL",
  "ModSounds.TONRO_HURT",
  "ModSounds.TONRO_DEATH",
  "ParticleTypes.FLAME",
  "setParasiteStatus(LEGACY_SHOCKWAVE_STATUS)",
  "spawnShock()",
  "getAttackTimer()",
  "getFinished(byte skill)",
  "doSpecialSkill(byte skill)"
]) {
  if (!tonro.includes(marker)) throw new Error(`TonroEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/TonroRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<TonroEntity>",
  "geo/entity/tonro.geo.json",
  "animations/entity/tonro.animation.json",
  "textures/entity/monster/tonro.png",
  "this.shadowRadius = 0.4F"
]) {
  if (!renderer.includes(marker)) throw new Error(`TonroRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("kyphosisfrozen.png")) {
  throw new Error("TonroRenderer must not reference missing legacy kyphosisfrozen.png texture");
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.kyphosis"] !== "Kyphosis" || enUs["entity.srparasites.kyphosis.name"] !== "Kyphosis") {
  throw new Error("en_us.json missing Kyphosis entity translation keys");
}
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");
if (zhCn["entity.srparasites.kyphosis"] !== "曲击柱" || zhCn["entity.srparasites.kyphosis.name"] !== "曲击柱") {
  throw new Error("zh_cn.json missing Kyphosis entity translation keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/tonro.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Tonro geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Tonro geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 140) throw new Error(`Tonro geo must preserve the 140 converted ModelTonro bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/tonro.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.tonro.func_78087_a", "animation.tonro.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Tonro animation names changed: ${JSON.stringify(animationNames)}`);
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

console.log("Tonro / Kyphosis entity port verifier passed.");
