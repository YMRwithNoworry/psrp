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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/LodoEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/LodoRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/lodo.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/lodo.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/lodo.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_lodo.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_lodo.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Lodo port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("lodo"',
  "EntityType.Builder.of(LodoEntity::new, MobCategory.MONSTER)",
  ".sized(LodoEntity.LEGACY_WIDTH, LodoEntity.LEGACY_HEIGHT)",
  ".eyeHeight(LodoEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Lodo marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["LodoEntity.createAttributes().build()", "ModEntities.LODO.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Lodo attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_LODO", '"itemmobspawner_lodo"', "DeferredSpawnEggItem", "ModEntities.LODO"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Lodo spawn egg marker: ${marker}`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.LODO.get(), LodoRenderer::new)")) {
  throw new Error("ModClientEvents missing Lodo renderer registration");
}

const lodo = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/LodoEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 5",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.lodo.func_78087_a"',
  "LEGACY_HEALTH = 7.0D",
  "LEGACY_ARMOR = 1.5D",
  "LEGACY_ATTACK_DAMAGE = 3.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.05D",
  "LEGACY_MOVEMENT_SPEED = 0.2D",
  "LEGACY_WIDTH = 0.5F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_EYE_HEIGHT = 0.3F",
  "LEGACY_MIN_GROW_SECONDS = 60",
  "LEGACY_RANDOM_GROW_SECONDS = 60",
  "LEGACY_AVOID_DISTANCE = 8",
  "LEGACY_BURIED_START = 1.0F",
  "LEGACY_BURIED_DECAY = 0.02F",
  "LEGACY_BURIED_EVENT = 50",
  "implements GeoEntity",
  "AvoidEntityGoal<>",
  "WaterAnimal",
  "Creeper",
  "SrpParasiteMob",
  "Animal",
  'tag.putInt("ruptergrow"',
  "ModSounds.LODO_GROWL",
  "ModSounds.LODO_HURT",
  "ModSounds.LODO_DEATH",
  "ModSounds.LODO_MUDO",
  "BlockParticleOption",
  "ParticleTypes.BLOCK"
]) {
  if (!lodo.includes(marker)) throw new Error(`LodoEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/LodoRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<LodoEntity>",
  "geo/entity/lodo.geo.json",
  "animations/entity/lodo.animation.json",
  "textures/entity/monster/lodo.png",
  "this.shadowRadius = 0.2F"
]) {
  if (!renderer.includes(marker)) throw new Error(`LodoRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("slodo.png")) {
  throw new Error("LodoRenderer must not reference missing legacy slodo.png texture");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/lodo.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Lodo geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Lodo geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 27) throw new Error(`Lodo geo must preserve the 27 converted ModelLodo bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/lodo.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.lodo.func_78087_a", "animation.lodo.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Lodo animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length === 0) throw new Error(`${name} has no animated bones`);
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

console.log("Lodo entity port verifier passed.");
