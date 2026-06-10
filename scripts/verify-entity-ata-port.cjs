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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/AtaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/AtaRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/ata.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/ata.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/gnat.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_ata.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_ata.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Ata/Gnat port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("gnat"',
  "EntityType.Builder.of(AtaEntity::new, MobCategory.MONSTER)",
  ".sized(AtaEntity.LEGACY_WIDTH, AtaEntity.LEGACY_HEIGHT)",
  ".eyeHeight(AtaEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Ata marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["AtaEntity.createAttributes().build()", "ModEntities.ATA.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Ata attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_ATA", '"itemmobspawner_ata"', "DeferredSpawnEggItem", "ModEntities.ATA"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Ata spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_ATA = legacyItem("itemmobspawner_ata"')) {
  throw new Error("Ata spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.ATA.get(), AtaRenderer::new)")) {
  throw new Error("ModClientEvents missing Ata renderer registration");
}

const ata = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/inborn/AtaEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 91",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.ata.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.ata.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 5.0D",
  "LEGACY_ARMOR = 2.0D",
  "LEGACY_ATTACK_DAMAGE = 5.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.6D",
  "LEGACY_MOVEMENT_SPEED = 0.34559D",
  "LEGACY_WIDTH = 0.85F",
  "LEGACY_HEIGHT = 1.0F",
  "LEGACY_EYE_HEIGHT = 0.8F",
  "LEGACY_XP = 2",
  "LEGACY_TYPE = 5",
  "LEGACY_LIFESPAN_TICKS = 1200",
  "LEGACY_HIJACK_HEALTH_FRACTION = 0.5F",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_ATTACK_SPEED_TICKS = 6",
  "LEGACY_SKILL_LEAP_POWER = 0.4F",
  "LEGACY_SKILL_LEAP_SPEED = 1.0D",
  "LEGACY_SKILL_MIN_COOLDOWN = 20",
  "LEGACY_SKILL_MAX_COOLDOWN = 100",
  "LEGACY_SKILL_WINDUP = 5",
  "LEGACY_SKILL_EVENT = 14",
  "LEGACY_LEAP_AT_TARGET_POWER = 0.4F",
  "LEGACY_FALL_DAMAGE_THRESHOLD = 60.0F",
  "LEGACY_VIRAL_TICKS = 120",
  "LEGACY_VIRAL_AMPLIFIER = 2",
  "LEGACY_BURST_PARTICLE_EVENT = 10",
  "LEGACY_CONVERT_PARTICLE_EVENT = 11",
  "implements GeoEntity",
  "WallClimberNavigation",
  "LeapAtTargetGoal",
  "MeleeAttackGoal",
  "SkillLeapGoal",
  "public void push(Entity entity)",
  "public boolean doHurtTarget(Entity target)",
  "return false;",
  "SrpMobEffect.applyStackEffect",
  "ModEffects.VIRAL",
  "ModSounds.MOB_SILENCE",
  "ModSounds.SMALL_STEP",
  "ModSounds.BUTHOL_BOOM",
  "WaterAnimal"
]) {
  if (!ata.includes(marker)) throw new Error(`AtaEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/AtaRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<AtaEntity>",
  "geo/entity/ata.geo.json",
  "animations/entity/ata.animation.json",
  "textures/entity/monster/gnat.png",
  "this.shadowRadius = 0.5F"
]) {
  if (!renderer.includes(marker)) throw new Error(`AtaRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.gnat"] !== "Gnat" || enUs["entity.srparasites.gnat.name"] !== "Gnat") {
  throw new Error("en_us.json missing Gnat entity translation keys");
}
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");
if (!zhCn["entity.srparasites.gnat"] || !zhCn["entity.srparasites.gnat.name"]) {
  throw new Error("zh_cn.json missing Gnat entity translation keys");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/ata.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Ata geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Ata geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 100) throw new Error(`Ata geo must preserve the 100 converted ModelAta bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/ata.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.ata.func_78087_a", "animation.ata.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Ata animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 18) throw new Error(`${name} must keep 18 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Ata/Gnat entity port verifier passed.");
