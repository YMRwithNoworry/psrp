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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfSheepEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InfSheepRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inf_sheep.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inf_sheep.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/sheep.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/sheep_grey.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/sheep_black.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_infsheep.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_infsheep.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing InfSheep / Assimilated Sheep port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("sim_sheep"',
  "EntityType.Builder.of(InfSheepEntity::new, MobCategory.MONSTER)",
  ".sized(InfSheepEntity.LEGACY_WIDTH, InfSheepEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InfSheepEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing InfSheep marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["InfSheepEntity.createAttributes().build()", "ModEntities.INFSHEEP.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InfSheep attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INFSHEEP", '"itemmobspawner_infsheep"', "DeferredSpawnEggItem", "ModEntities.INFSHEEP", "0x835000", "0xFF00DC"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing InfSheep spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_INFSHEEP = legacyItem("itemmobspawner_infsheep"')) {
  throw new Error("InfSheep spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INFSHEEP.get(), InfSheepRenderer::new)")) {
  throw new Error("ModClientEvents missing InfSheep renderer registration");
}

const infSheep = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/infected/InfSheepEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 14",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inf_sheep.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_sheep.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 13.0D",
  "LEGACY_ARMOR = 1.3D",
  "LEGACY_ATTACK_DAMAGE = 6.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.3D",
  "LEGACY_MOVEMENT_SPEED = 0.23000000417232513D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 0.9F",
  "LEGACY_HEIGHT = 1.3F",
  "LEGACY_EYE_HEIGHT = 1.235F",
  "LEGACY_SHADOW_RADIUS = 0.5F",
  "LEGACY_TYPE = 12",
  "LEGACY_CAN_MOD_RENDER = 1",
  "LEGACY_FUSE_TIME = 40",
  "LEGACY_SWIM_DIVE_SPEED = 0.08D",
  "LEGACY_MELEE_SPEED = 1.5D",
  "LEGACY_FOLLOWER_SEARCH_MODE = 1",
  "LEGACY_FOLLOWER_SEARCH_RANGE = 16",
  "LEGACY_TEXTURE_WHITE = 0",
  "LEGACY_TEXTURE_GREY = 1",
  "LEGACY_TEXTURE_BLACK = 2",
  "LEGACY_TEXTURE_BLACK_CHANCE = 0.1F",
  "LEGACY_TEXTURE_GREY_CHANCE = 0.4F",
  "LEGACY_MELT_WAIT_TICKS = 1000",
  "LEGACY_MELT_START_HEIGHT = 1.3F",
  "LEGACY_DEATH_HEIGHT_RESTORE = 0.17F",
  "LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F",
  "LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F",
  "LEGACY_MELT_ASIZE_STEP = -0.005F",
  "LEGACY_MELT_HEIGHT_STEP = -0.01F",
  "LEGACY_MELT_SPAWN_TICKS = 63",
  "LEGACY_LESH_LEGS = 0",
  "LEGACY_HEAD_HEALTH = 0.0D",
  "LEGACY_HEAD_DAMAGE = 0.0D",
  "LEGACY_HEAD_CHANCE = 0.0D",
  "LEGACY_LESH_V = 0",
  "MeleeAttackGoal",
  "public SpawnGroupData finalizeSpawn",
  "rollTextureVariantWeighted()",
  "protected InteractionResult mobInteract",
  "DyeItem",
  "DyeColor",
  "case WHITE -> LEGACY_TEXTURE_WHITE",
  "case GRAY, LIGHT_GRAY -> LEGACY_TEXTURE_GREY",
  "case BLACK -> LEGACY_TEXTURE_BLACK",
  'tag.putInt("TextureVariant", getTextureVariant())',
  'tag.contains("TextureVariant")',
  "public void melt()",
  "setTHeighAbsolute(LEGACY_MELT_START_HEIGHT)",
  "setaSize(LEGACY_MELT_ASIZE_STEP)",
  "setTHeigh(LEGACY_MELT_HEIGHT_STEP)",
  "ModSounds.INFECTED_MELT",
  "ModSounds.INFECTEDSHEEP_GROWL",
  "ModSounds.INFECTEDSHEEP_HURT",
  "ModSounds.INFECTEDSHEEP_DEATH",
  "SoundEvents.SHEEP_STEP"
]) {
  if (!infSheep.includes(marker)) throw new Error(`InfSheepEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InfSheepRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InfSheepEntity>",
  "geo/entity/inf_sheep.geo.json",
  "animations/entity/inf_sheep.animation.json",
  "textures/entity/monster/sheep.png",
  "textures/entity/monster/sheep_grey.png",
  "textures/entity/monster/sheep_black.png",
  "InfSheepEntity.LEGACY_SHADOW_RADIUS",
  "entity.getTextureVariant()",
  "entity.getSelfeFlashIntensity2()"
]) {
  if (!renderer.includes(marker)) throw new Error(`InfSheepRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.sim_sheep"] !== "Assimilated Sheep" || enUs["entity.srparasites.sim_sheep.name"] !== "Assimilated Sheep") {
  throw new Error("en_us.json missing Assimilated Sheep entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_infsheep"] !== "Spawn Assimilated Sheep") {
  throw new Error("en_us.json missing Spawn Assimilated Sheep item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inf_sheep.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InfSheep geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("InfSheep geo must contain exactly one minecraft:geometry entry");
}
const geometry = geo["minecraft:geometry"][0];
if (geometry.description.texture_width !== 128 || geometry.description.texture_height !== 40) {
  throw new Error(`InfSheep geo must preserve the 128x40 legacy model texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 83) throw new Error(`InfSheep geo must preserve the 83 converted ModelInfSheep bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inf_sheep.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inf_sheep.func_78087_a", "animation.inf_sheep.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InfSheep animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 40) throw new Error(`${name} must keep 40 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityInfSheep",
  "ModelInfSheep",
  "RenderInfSheep",
  "sim_sheep",
  "itemmobspawner_infsheep",
  "Assimilated Sheep",
  "INFECTEDSHEEP_GROWL",
  "TextureVariant",
  "EntityInfSheepHead",
  "infsheepmob",
  "EntityFerSheep"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing InfSheep marker: ${marker}`);
}

console.log("InfSheep / Assimilated Sheep entity port verifier passed.");
