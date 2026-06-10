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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/GanroEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/GanroRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/ganro.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/ganro.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/ganro.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/ganroh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_ganro.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_ganro.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Ganro/Warden port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("warden"',
  "EntityType.Builder.of(GanroEntity::new, MobCategory.MONSTER)",
  ".sized(GanroEntity.LEGACY_WIDTH, GanroEntity.LEGACY_HEIGHT)",
  ".eyeHeight(GanroEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Ganro marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["GanroEntity.createAttributes().build()", "ModEntities.GANRO.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Ganro attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_GANRO", '"itemmobspawner_ganro"', "DeferredSpawnEggItem", "ModEntities.GANRO"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Ganro spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_GANRO = legacyItem("itemmobspawner_ganro"')) {
  throw new Error("Ganro spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.GANRO.get(), GanroRenderer::new)")) {
  throw new Error("ModClientEvents missing Ganro renderer registration");
}

const ganro = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/GanroEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 33",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.ganro.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.ganro.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 80.0D",
  "LEGACY_ARMOR = 15.0D",
  "LEGACY_ATTACK_DAMAGE = 25.0D",
  "LEGACY_MOVEMENT_SPEED = 0.27D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.901F",
  "LEGACY_HEIGHT = 4.2F",
  "LEGACY_EYE_HEIGHT = 3.5F",
  "LEGACY_AOE_MELEE_SPEED = 1.3D",
  "LEGACY_MELEE_REACH = 8.0D",
  "LEGACY_AOE_INFLATE = 2.0D",
  "LEGACY_WATER_LEAP_POWER = 0.7F",
  "LEGACY_WATER_LEAP_SPEED = 1.5D",
  "LEGACY_SKILL_LEAP_MIN_COOLDOWN = 80",
  "LEGACY_SKILL_LEAP_MAX_COOLDOWN = 100",
  "LEGACY_SKILL_LEAP_POWER = 1.2F",
  "LEGACY_SKILL_LEAP_SPEED = 2.5D",
  "LEGACY_CHARGE_STATUS = 3",
  "LEGACY_CHARGE_COOLDOWN_TICKS = 40",
  "LEGACY_CHARGE_TRIGGER_RANGE = 22",
  "LEGACY_CHARGE_WINDUP_TICKS = 20",
  "LEGACY_CHARGE_STUCK_CANCEL_TICKS = 60",
  "LEGACY_CHARGE_TARGET_DISTANCE = 15.0D",
  "LEGACY_CHARGE_SPEED = 3.0D",
  "LEGACY_CHARGE_DAMAGE_MULTIPLIER = 0.5D",
  "LEGACY_SHOCKWAVE_STATUS = 100",
  "LEGACY_SHOCKWAVE_TRIGGER_RANGE = 32",
  "LEGACY_SHOCKWAVE_DAMAGE_MULTIPLIER = 0.3D",
  "LEGACY_LAUNCH_HORIZONTAL = 0.4D",
  "LEGACY_LAUNCH_Y = 1.05D",
  "LEGACY_PLAYER_LAUNCH_Y = 0.525D",
  "LEGACY_ATTACK_TIMER_UP_STEP = 0.2F",
  "LEGACY_ATTACK_TIMER_DOWN_STEP = 0.1F",
  "LEGACY_HEAVY_SKIN = 7",
  "LEGACY_ATTACK_EVENT = 12",
  "LEGACY_FLAME_EVENT = 100",
  "WallClimberNavigation",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "GanroAoeMeleeGoal",
  "SkillLeapGoal",
  "ChargeGoal",
  "ShockwaveGoal",
  "EvadeDashGoal",
  "attackEntityAsMobAOE",
  "maybeLaunchTarget",
  "damageChargeArea",
  "spawnShock()",
  "ModSounds.MOB_SWIPE",
  "ModSounds.MOB_SILENCE",
  "ModSounds.GANRO_GROWL",
  "ModSounds.GANRO_HURT",
  "ModSounds.GANRO_DEATH",
  "ParticleTypes.FLAME",
  "getSelfeFlashIntensity"
]) {
  if (!ganro.includes(marker)) throw new Error(`GanroEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/GanroRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<GanroEntity>",
  "geo/entity/ganro.geo.json",
  "animations/entity/ganro.animation.json",
  "textures/entity/monster/ganro.png",
  "textures/entity/monster/ganroh.png",
  "this.shadowRadius = 1.2F",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`GanroRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.warden"] !== "Warden" || enUs["entity.srparasites.warden.name"] !== "Warden") {
  throw new Error("en_us.json missing Warden entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_ganro"] !== "Spawn Warden") {
  throw new Error("en_us.json missing Spawn Warden item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/ganro.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Ganro geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Ganro geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 135) throw new Error(`Ganro geo must preserve the 135 converted ModelGanro bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/ganro.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.ganro.func_78087_a", "animation.ganro.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Ganro animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 10) throw new Error(`${name} must keep 10 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Ganro / Warden entity port verifier passed.");
