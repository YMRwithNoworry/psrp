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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/EsorEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/EsorRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/esor.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/esor.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/esor.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/esorh.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/tendrilesor.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_esor.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_esor.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Esor / Heavy Bomber port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("bomber_heavy"',
  "EntityType.Builder.of(EsorEntity::new, MobCategory.MONSTER)",
  ".sized(EsorEntity.LEGACY_WIDTH, EsorEntity.LEGACY_HEIGHT)",
  ".eyeHeight(EsorEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Esor marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["EsorEntity.createAttributes().build()", "ModEntities.ESOR.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Esor attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_ESOR", '"itemmobspawner_esor"', "DeferredSpawnEggItem", "ModEntities.ESOR"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Esor spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_ESOR = legacyItem("itemmobspawner_esor"')) {
  throw new Error("Esor spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.ESOR.get(), EsorRenderer::new)")) {
  throw new Error("ModClientEvents missing Esor renderer registration");
}

const esor = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/EsorEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 50",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.esor.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.esor.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 120.0D",
  "LEGACY_ARMOR = 20.0D",
  "LEGACY_ATTACK_DAMAGE = 40.0D",
  "LEGACY_MOVEMENT_SPEED = 0.255D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.901F",
  "LEGACY_HEIGHT = 4.2F",
  "LEGACY_EYE_HEIGHT = 3.5F",
  "LEGACY_TYPE = 50",
  "LEGACY_AOE_MELEE_SPEED = 1.3D",
  "LEGACY_MELEE_REACH = 8.0D",
  "LEGACY_AOE_INFLATE = 5.0D",
  "LEGACY_WATER_LEAP_POWER = 0.7F",
  "LEGACY_WATER_LEAP_SPEED = 1.5D",
  "LEGACY_WATER_LEAP_STATUS = 7",
  "LEGACY_WATER_LEAP_INTERVAL = 20",
  "LEGACY_SKILL_LEAP_COOLDOWN = 100",
  "LEGACY_SKILL_LEAP_WINDUP = 10",
  "LEGACY_SKILL_LEAP_POWER = 1.2F",
  "LEGACY_SKILL_LEAP_SPEED = 2.5D",
  "LEGACY_SKILL_LEAP_STATUS = 7",
  "LEGACY_SMASH_COOLDOWN = 100",
  "LEGACY_SMASH_TRIGGER_RANGE = 7",
  "LEGACY_SMASH_WARMUP_TICKS = 20",
  "LEGACY_SMASH_FINISH_TICKS = 100",
  "LEGACY_SMASH_WARMUP_STATUS = 25",
  "LEGACY_SMASH_ACTIVE_STATUS = 3",
  "LEGACY_SMASH_DAMAGE_RADIUS = 6.0D",
  "LEGACY_SMASH_DAMAGE_Y_RADIUS = 3.0D",
  "LEGACY_SMASH_RAGE_RADIUS = 24.0D",
  "LEGACY_SMASH_RAGE_Y_RADIUS = 5.0D",
  "LEGACY_SMASH_RAGE_TICKS = 1200",
  "LEGACY_SMASH_RAGE_AMPLIFIER = 1",
  "LEGACY_SMASH_SLOW_TICKS = 110",
  "LEGACY_SMASH_SLOW_AMPLIFIER = 100",
  "LEGACY_EVADE_INTERVAL = 50",
  "LEGACY_EVADE_MAX_DISTANCE = 10",
  "LEGACY_EVADE_SPEED = 4.0D",
  "LEGACY_HIT_VERTICAL_BONUS = 0.5000000059604645D",
  "LEGACY_TENDRIL_HEALTH_FRACTION = 0.5D",
  "LEGACY_LEFT_TENDRIL_DEAD_EVENT = 11",
  "LEGACY_ATTACK_EVENT = 12",
  "LEGACY_RIGHT_TENDRIL_DEAD_EVENT = 22",
  "LEGACY_FLAME_EVENT = 100",
  "parasiteleftTendril",
  "parasiterightTendril",
  "WallClimberNavigation",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "EsorAoeMeleeGoal",
  "SkillLeapGoal",
  "WaterLeapGoal",
  "EvadeDashGoal",
  "SmashGoal",
  "attackEntityAsMobAOE",
  "damageSmashArea",
  "applyRageToNearbyParasites",
  "ModEffects.RAGE",
  "SrpConfig.RAGE_ENABLE",
  "MobEffects.MOVEMENT_SLOWDOWN",
  "ModSounds.MOB_SWIPE",
  "ModSounds.MOB_SILENCE",
  "ModSounds.ESOR_GROWL",
  "ModSounds.ESOR_HURT",
  "ModSounds.ESOR_DEATH",
  "ModSounds.STEP_HEAVY",
  "ParticleTypes.FLAME",
  "getSelfeFlashIntensity"
]) {
  if (!esor.includes(marker)) throw new Error(`EsorEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/EsorRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<EsorEntity>",
  "geo/entity/esor.geo.json",
  "animations/entity/esor.animation.json",
  "textures/entity/monster/esor.png",
  "textures/entity/monster/esorh.png",
  "this.shadowRadius = 1.2F",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`EsorRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.bomber_heavy"] !== "Heavy Bomber" || enUs["entity.srparasites.bomber_heavy.name"] !== "Heavy Bomber") {
  throw new Error("en_us.json missing Heavy Bomber entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_esor"] !== "Spawn Marauder") {
  throw new Error("en_us.json changed the legacy Esor spawn item translation surface");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/esor.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Esor geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Esor geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 175) throw new Error(`Esor geo must preserve the 175 converted ModelEsor bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/esor.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.esor.func_78087_a", "animation.esor.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Esor animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 16) throw new Error(`${name} must keep 16 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Esor / Heavy Bomber entity port verifier passed.");
