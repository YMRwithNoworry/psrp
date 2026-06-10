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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/VestaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/VestaRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/vesta.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/vesta.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/vesta.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/vestare.png",
  "src/main/resources/assets/srparasites/textures/entity/layer/vestasnow.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_vesta.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_vesta.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Vesta / Colony Carrier port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("carrier_colony"',
  "EntityType.Builder.of(VestaEntity::new, MobCategory.MONSTER)",
  ".sized(VestaEntity.LEGACY_WIDTH, VestaEntity.LEGACY_HEIGHT)",
  ".eyeHeight(VestaEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Vesta marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["VestaEntity.createAttributes().build()", "ModEntities.VESTA.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Vesta attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_VESTA", '"itemmobspawner_vesta"', "DeferredSpawnEggItem", "ModEntities.VESTA"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Vesta spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_VESTA = legacyItem("itemmobspawner_vesta"')) {
  throw new Error("Vesta spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.VESTA.get(), VestaRenderer::new)")) {
  throw new Error("ModClientEvents missing Vesta renderer registration");
}

const vesta = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/VestaEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 88",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.vesta.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.vesta.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 390.0D",
  "LEGACY_ARMOR = 15.5D",
  "LEGACY_ATTACK_DAMAGE = 45.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.15D",
  "LEGACY_MOVEMENT_SPEED = 0.242D",
  "LEGACY_FOLLOW_RANGE = 80.0D",
  "LEGACY_WIDTH = 1.75F",
  "LEGACY_HEIGHT = 3.6F",
  "LEGACY_EYE_HEIGHT = 1.5F",
  "LEGACY_TYPE = 31",
  "LEGACY_ATTACK_INTERVAL = 10",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_MELEE_REACH = 8.0D",
  "LEGACY_WATER_LEAP_POWER = 0.7F",
  "LEGACY_WATER_LEAP_SPEED = 1.5D",
  "LEGACY_WATER_LEAP_STATUS = 3",
  "LEGACY_WATER_LEAP_INTERVAL = 20",
  "LEGACY_AREA_BUFF_INITIAL_TICKS = 60",
  "LEGACY_AREA_BUFF_COOLDOWN_TICKS = 600",
  "LEGACY_AREA_BUFF_RANGE = 60",
  "LEGACY_REGENERATION_TICKS = 600",
  "LEGACY_REGENERATION_AMPLIFIER = 3",
  "LEGACY_LINK_TICKS = 200",
  "LEGACY_LINK_AMPLIFIER = 1",
  "LEGACY_COORDINATION_RANGE = 32",
  "LEGACY_COORDINATION_INTERVAL = 25",
  "LEGACY_COORDINATION_TICKS = 6666",
  "LEGACY_VARIANT_SKIN = 1",
  "LEGACY_VARIANT_CHANCE = 0.33D",
  "LEGACY_VARIANT_ARMOR_MULTIPLIER = 1.5D",
  "LEGACY_VARIANT_SPEED = 0.1694D",
  "WaterLeapGoal",
  "VestaMeleeGoal",
  "AreaBuffGoal",
  "ModEffects.LINK",
  "MobEffects.REGENERATION",
  "ModSounds.VESTA_GROWL",
  "ModSounds.VESTA_HURT",
  "ModSounds.VESTA_DEATH"
]) {
  if (!vesta.includes(marker)) throw new Error(`VestaEntity missing legacy behavior marker: ${marker}`);
}
if (vesta.includes("ModEffects.FOSTER")) {
  throw new Error("Vesta must not pretend Foster is implemented before the Foster system is ported");
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/VestaRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<VestaEntity>",
  "geo/entity/vesta.geo.json",
  "animations/entity/vesta.animation.json",
  "textures/entity/monster/vesta.png",
  "textures/entity/monster/vestare.png",
  "VestaEntity.LEGACY_SHADOW_RADIUS",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`VestaRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.carrier_colony"] !== "Colony Carrier" || enUs["entity.srparasites.carrier_colony.name"] !== "Colony Carrier") {
  throw new Error("en_us.json missing Colony Carrier entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_vesta"] !== "Spawn Colony Carrier") {
  throw new Error("en_us.json missing Spawn Colony Carrier item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/vesta.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Vesta geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Vesta geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 226) throw new Error(`Vesta geo must preserve the 226 converted ModelVesta bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/vesta.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.vesta.func_78087_a", "animation.vesta.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Vesta animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 53) throw new Error(`${name} must keep 53 animated bones, found ${bonesWithChannels.length}`);
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
  "EntityVesta",
  "carrier_colony",
  "FOSTER remains blocked",
  "follower-linking behavior"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Vesta marker: ${marker}`);
}

console.log("Vesta / Colony Carrier entity port verifier passed.");
