const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/SrpParasiteMob.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/FlogEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/FlogRenderer.java",
  "src/main/resources/assets/srparasites/textures/entity/monster/flog.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/flogv.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/flogb.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/flogh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_flog.json"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Flog port file/resource: ${file}`);
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
for (const marker of ["flog.png", "flogv.png", "flogb.png", "flogh.png", "case 5", "case 6", "case 7"]) {
  if (!renderer.includes(marker)) throw new Error(`FlogRenderer missing texture marker: ${marker}`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of ["@EventBusSubscriber", "Dist.CLIENT", "EntityRenderersEvent.RegisterRenderers", "registerEntityRenderer"]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing client registration marker: ${marker}`);
}

console.log("Flog entity port verifier passed.");
