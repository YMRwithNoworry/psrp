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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/OrchEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/WebballEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/OrchRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/WebballRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/orch.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/orch.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/orch.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/orchsp1.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/orchh.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/webball.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_orch.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_orch.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Orch port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("monarch"',
  "EntityType.Builder.of(OrchEntity::new, MobCategory.MONSTER)",
  ".sized(OrchEntity.LEGACY_WIDTH, OrchEntity.LEGACY_HEIGHT)",
  ".eyeHeight(OrchEntity.LEGACY_EYE_HEIGHT)",
  'ENTITIES.register("webball"',
  "EntityType.Builder.<WebballEntity>of(WebballEntity::new, MobCategory.MISC)",
  ".sized(WebballEntity.LEGACY_WIDTH, WebballEntity.LEGACY_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Orch/Webball marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["OrchEntity.createAttributes().build()", "ModEntities.MONARCH.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Orch attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_ORCH", '"itemmobspawner_orch"', "DeferredSpawnEggItem", "ModEntities.MONARCH", "0x313517"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Orch spawn egg marker: ${marker}`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.MONARCH.get(), OrchRenderer::new)",
  "event.registerEntityRenderer(ModEntities.WEBBALL.get(), WebballRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Orch/Webball renderer registration: ${marker}`);
}

const orch = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/OrchEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 84",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.orch.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.orch.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 75.0D",
  "LEGACY_ARMOR = 10.0D",
  "LEGACY_ATTACK_DAMAGE = 25.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_MOVEMENT_SPEED = 0.2775D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 1.901F",
  "LEGACY_HEIGHT = 4.1F",
  "LEGACY_EYE_HEIGHT = 3.5F",
  "LEGACY_XP = 75",
  "LEGACY_AOE_INFLATE = 2.0D",
  "LEGACY_PROJECTILE_COOLDOWN = 40",
  "LEGACY_PROJECTILE_INTERVAL = 15",
  "LEGACY_PROJECTILE_COUNT = 4",
  "LEGACY_PROJECTILE_RANGE_SQR = 4225.0D",
  "LEGACY_SKILL_LEAP_POWER = 0.5F",
  "LEGACY_SKILL_LEAP_SPEED = 3.5D",
  "LEGACY_EVADE_INTERVAL = 17",
  "LEGACY_EVADE_SPEED = 3.5D",
  "LEGACY_HIT_VERTICAL_BONUS = 0.5000000059604645D",
  "LEGACY_VARIANT_SKIN_WEAK = 1",
  "LEGACY_VARIANT_SKIN_HEAVY = 7",
  "LEGACY_WEAK_HEALTH_MULTIPLIER = 0.5D",
  "LEGACY_WEAK_DAMAGE_MULTIPLIER = 1.5D",
  "implements GeoEntity",
  "WallClimberNavigation",
  "attackEntityAsMobAOE",
  "WebballVolleyGoal",
  "WaterLeapGoal",
  "SkillLeapGoal",
  "EvadeDashGoal",
  "WebballEntity.LEGACY_TYPE_TWO",
  "applyWeakVariantAttributes(true)",
  "applyWeakVariantAttributes(false)"
]) {
  if (!orch.includes(marker)) throw new Error(`OrchEntity missing legacy behavior marker: ${marker}`);
}

const webball = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/WebballEntity.java");
for (const marker of [
  "LEGACY_ENTITY_ID = 101",
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_BLIND_CHANCE = 0.3F",
  "LEGACY_BLIND_TICKS = 60",
  "LEGACY_TIMEOUT_TICKS = 60",
  "LEGACY_MIN_TIMEOUT_WEBS = 1",
  "LEGACY_RANDOM_TIMEOUT_WEBS = 3",
  "MobEffects.BLINDNESS",
  "Blocks.COBWEB",
  "EventHooks.canEntityGrief",
  "setWebsAround",
  "damageSources().thrown(this, getOwner())"
]) {
  if (!webball.includes(marker)) throw new Error(`WebballEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/OrchRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<OrchEntity>",
  "geo/entity/orch.geo.json",
  "animations/entity/orch.animation.json",
  "orch.png",
  "orchsp1.png",
  "orchh.png",
  "this.shadowRadius = 1.2F",
  "entity.getAttackTimer()"
]) {
  if (!renderer.includes(marker)) throw new Error(`OrchRenderer missing renderer marker: ${marker}`);
}

const webballRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/WebballRenderer.java");
for (const marker of [
  "extends EntityRenderer<WebballEntity>",
  "textures/entity/projectile/webball.png",
  "RenderType.entityCutoutNoCull",
  "cameraOrientation()"
]) {
  if (!webballRenderer.includes(marker)) throw new Error(`WebballRenderer missing renderer marker: ${marker}`);
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/orch.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Orch geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Orch geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 196) throw new Error(`Orch geo must preserve the 196 converted ModelOrch bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/orch.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.orch.func_78087_a", "animation.orch.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Orch animation names changed: ${JSON.stringify(animationNames)}`);
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

console.log("Orch entity port verifier passed.");
