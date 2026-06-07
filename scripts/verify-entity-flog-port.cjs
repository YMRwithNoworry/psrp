const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/SrpParasiteMob.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/FlogEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/FlogRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/flog.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/flog.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/flog.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/flogv.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/flogb.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/flogh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_flog.json"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Flog port file/resource: ${file}`);
}

const build = read("build.gradle");
for (const marker of ["geckolib-neoforge-${minecraft_version}:${geckolib_version}", "dl.cloudsmith.io/public/geckolib3/geckolib/maven"]) {
  if (!build.includes(marker)) throw new Error(`build.gradle missing GeckoLib marker: ${marker}`);
}

const properties = read("gradle.properties");
if (!properties.includes("geckolib_version=4.8.4")) throw new Error("gradle.properties missing GeckoLib 4.8.4 version marker");

const modsToml = read("src/main/templates/META-INF/neoforge.mods.toml");
for (const marker of ['modId = "geckolib"', 'versionRange = "[${geckolib_version},)"', 'ordering = "AFTER"']) {
  if (!modsToml.includes(marker)) throw new Error(`neoforge.mods.toml missing GeckoLib dependency marker: ${marker}`);
}

const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");
for (const marker of ["ModEntities.register(modEventBus)", "modEventBus.register(ModEntityEvents.class)"]) {
  if (!main.includes(marker)) throw new Error(`SRPMain missing entity wiring marker: ${marker}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of ['ENTITIES.register("grunt"', ".sized(0.7666F, 1.95F)", ".eyeHeight(1.73F)", "MobCategory.MONSTER"]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Flog legacy marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_FLOG", '"itemmobspawner_flog"', "DeferredSpawnEggItem", "ModEntities.GRUNT"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Flog spawn egg marker: ${marker}`);
}

const flog = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/FlogEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 60",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.flog.func_78087_a"',
  "LEGACY_HEALTH = 20.0D",
  "LEGACY_ARMOR = 7.0D",
  "LEGACY_ATTACK_DAMAGE = 13.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.4D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_MOVEMENT_SPEED = 0.274172325D",
  "LEGACY_AOE_INFLATE = 2.0D",
  "LEGACY_WATER_LEAP_POWER = 0.7F",
  "LEGACY_WATER_LEAP_SPEED = 1.5D",
  "LEGACY_SKILL_LEAP_POWER = 1.1F",
  "LEGACY_SKILL_LEAP_SPEED = 3.5D",
  "LEGACY_SKILL_MIN_COOLDOWN = 40",
  "LEGACY_SKILL_MAX_COOLDOWN = 100",
  "LEGACY_SKILL_WINDUP = 10",
  "LEGACY_EVADE_INTERVAL = 20",
  "LEGACY_EVADE_MIN_DISTANCE = 2",
  "LEGACY_EVADE_MAX_DISTANCE = 4",
  "LEGACY_EVADE_SPEED = 1.5D",
  "LEGACY_EVADE_COOLDOWN = 15",
  "implements GeoEntity",
  "GeckoLibUtil.createInstanceCache(this)",
  "registerControllers(AnimatableManager.ControllerRegistrar controllers)",
  "new AnimationController<>(this, \"legacy_model\"",
  "RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME)",
  "WallClimberNavigation",
  "attackEntityAsMobAOE",
  "WaterLeapGoal",
  "SkillLeapGoal",
  "EvadeDashGoal",
  "setBesideClimbableBlock(this.horizontalCollision)"
]) {
  if (!flog.includes(marker)) throw new Error(`FlogEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/FlogRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<FlogEntity>",
  "new FlogModel()",
  "geo/entity/flog.geo.json",
  "animations/entity/flog.animation.json",
  "flog.png",
  "flogv.png",
  "flogb.png",
  "flogh.png",
  "case 5",
  "case 6",
  "case 7",
  "preRender(",
  "entity.getAttackTimer()"
]) {
  if (!renderer.includes(marker)) throw new Error(`FlogRenderer missing texture marker: ${marker}`);
}
for (const forbidden of ["SpiderModel", "ModelLayers.SPIDER", "MobRenderer<"]) {
  if (renderer.includes(forbidden)) throw new Error(`FlogRenderer still contains temporary renderer marker: ${forbidden}`);
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/flog.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Flog geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Flog geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 77) throw new Error(`Flog geo must preserve the 77 converted ModelFlog bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/flog.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.flog.func_78087_a", "animation.flog.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Flog animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length === 0) throw new Error(`${name} has no animated bones`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of ["@EventBusSubscriber", "Dist.CLIENT", "EntityRenderersEvent.RegisterRenderers", "registerEntityRenderer"]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing client registration marker: ${marker}`);
}

console.log("Flog entity port verifier passed.");
