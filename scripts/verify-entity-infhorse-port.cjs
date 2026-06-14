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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHorseEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfHorseRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_horse.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_horse.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/infhorse.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infhorse.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infhorse.png",
  "src/main/resources/assets/srparasites/sounds/assimilatedhorseexplode.ogg"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfHorse / Assimilated Horse port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_horse"',
  "EntityType.Builder.of(InfHorseEntity::new, MobCategory.MONSTER)",
  ".sized(InfHorseEntity.LEGACY_WIDTH, InfHorseEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfHorseEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfHorse marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfHorseEntity.createAttributes().build()", "ModEntities.INFHORSE.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfHorse attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFHORSE", '"itemmobspawner_infhorse"', "DeferredSpawnEggItem", "ModEntities.INFHORSE", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfHorse spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFHORSE = legacyItem("itemmobspawner_infhorse"')) {
  throw new Error("InfHorse spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFHORSE.get(), InfHorseRenderer::new)")) {
  throw new Error("ModClientEvents missing InfHorse renderer registration");
}

const infHorse = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfHorseEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 44",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_horse.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_horse.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 24.0D",
  "LEGACY_ARMOR = 0.5D",
  "LEGACY_ATTACK_DAMAGE = 7.5D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.1D",
  "LEGACY_MOVEMENT_SPEED = 0.26999999701976773D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 1.3964844F",
  "LEGACY_HEIGHT = 1.6F",
  "LEGACY_EYE_HEIGHT = 1.3F",
  "LEGACY_SHADOW_RADIUS = 0.75F",
  "LEGACY_TYPE = 11",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_FUSE_TIME = 70",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_SWELL_START_DISTANCE = 5.0D",
  "LEGACY_EXPLOSION_AABB_INFLATE = 3.5D",
  "LEGACY_CLOUD_WAIT_TICKS = 10",
  "LEGACY_CLOUD_POISON_TICKS = 300",
  "LEGACY_CLOUD_COTH_TICKS = 3600",
  "LEGACY_CLOUD_RADIUS_MULTIPLIER = 1.5F",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 1.6F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 73",
  "LEGACY_HEAD_HEALTH = 0.0D",
  "LEGACY_HEAD_DAMAGE = 0.0D",
  "LEGACY_HEAD_CHANCE = 0.0D",
  "InfHorseSwellGoal",
  "setSelfeState",
  "selfExplode",
  "affectNearbyLiving",
  "spawnLingeringCloud",
  "damageSources().magic()",
  "AreaEffectCloud",
  "MobEffects.POISON",
  "ModEffects.VIRAL",
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDHORSE_GROWL",
  "ModSounds.INFECTEDHORSE_HURT",
  "ModSounds.INFECTEDHORSE_DEATH",
  "ModSounds.INFECTEDHORSE_SA1",
  "ModSounds.INFECTEDHORSE_SA2",
  "SoundEvents.HORSE_STEP"
]) {
  if (!infHorse.includes(marker)) throw new Error(`InfHorseEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfHorseRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfHorseEntity>",
  "geo/entity/inf_horse.geo.json",
  "animations/entity/inf_horse.animation.json",
  "textures/entity/monster/infhorse.png",
  "InfHorseEntity.LEGACY_SHADOW_RADIUS",
  "entity.getSelfeFlashIntensity2()"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfHorseRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_horse"] !== "Assimilated Horse" || enUs["entity.srparasites.sim_horse.name"] !== "Assimilated Horse") {
  throw new Error("en_us.json missing Assimilated Horse entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infhorse"] !== "Spawn Assimilated Horse") {
  throw new Error("en_us.json missing Spawn Assimilated Horse item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_horse.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfHorse geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfHorse geo must contain exactly one minecraft:geometry entry");
}
const geometry = geo["minecraft:geometry"][0];
if (geometry.description.texture_width !== 128 || geometry.description.texture_height !== 50) {
  throw new Error(`InfHorse geo must preserve the 128x50 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 71) throw new Error(`InfHorse geo must preserve the 71 converted ModelInfHorse bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_horse.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_horse.func_78087_a", "animation.inf_horse.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfHorse animation names changed: ${JSON.stringify(animationNames)}`);
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

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
for (const marker of [
  "EntityInfHorse",
  "ModelInfHorse",
  "RenderInfHorse",
  "sim_horse",
  "itemmobspawner_infhorse",
  "Assimilated Horse",
  "INFECTEDHORSE_GROWL",
  "EntityAIAttackSwell",
  "EntityInfHorseHead",
  "EntityFerHorse",
  "EntityToxicCloud",
  "infhorseExplotionMult"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfHorse marker: ${marker}`);
}

console.log("InfHorse / Assimilated Horse entity port verifier passed.");
