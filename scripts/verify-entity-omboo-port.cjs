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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/OmbooEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/OmbooRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/omboo.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/omboo.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/omboo.png",
  "src/main/resources/assets/srparasites/textures/entity/monster/ombooh.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_omboo.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_omboo.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Omboo / Light Bomber port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("bomber_light"',
  "EntityType.Builder.of(OmbooEntity::new, MobCategory.MONSTER)",
  ".sized(OmbooEntity.LEGACY_WIDTH, OmbooEntity.LEGACY_HEIGHT)",
  ".eyeHeight(OmbooEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Omboo marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["OmbooEntity.createAttributes().build()", "ModEntities.OMBOO.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Omboo attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_OMBOO", '"itemmobspawner_omboo"', "DeferredSpawnEggItem", "ModEntities.OMBOO"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Omboo spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_OMBOO = legacyItem("itemmobspawner_omboo"')) {
  throw new Error("Omboo spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.OMBOO.get(), OmbooRenderer::new)")) {
  throw new Error("ModClientEvents missing Omboo renderer registration");
}

const omboo = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/OmbooEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 47",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.omboo.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.omboo.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 75.0D",
  "LEGACY_ARMOR = 20.0D",
  "LEGACY_ATTACK_DAMAGE = 25.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.15D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_WIDTH = 1.7F",
  "LEGACY_HEIGHT = 2.4F",
  "LEGACY_EYE_HEIGHT = 2.4F",
  "LEGACY_TYPE = 47",
  "LEGACY_HEAVY_SKIN = 7",
  "LEGACY_GROUND_LIFT = 5.0D",
  "LEGACY_GROUND_LIFT_SPEED = 0.5D",
  "LEGACY_RANDOM_MOVE_CHANCE = 7",
  "LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.2D",
  "LEGACY_RANDOM_MOVE_SPEED_TARGET = 0.3D",
  "LEGACY_CHARGE_SPEED = 1.0D",
  "LEGACY_CHARGE_MIN_DISTANCE_SQR = 3.0D",
  "LEGACY_CHARGE_RETARGET_DISTANCE_SQR = 9.0D",
  "LEGACY_CHARGE_TARGET_Y_OFFSET = 10.0D",
  "LEGACY_BOMB_INTERVAL = 15",
  "LEGACY_BOMB_FUSE_TICKS = 80",
  "LEGACY_BOMB_STRENGTH = 1.0F",
  "LEGACY_BOMB_DAMAGE = 20.0F",
  "LEGACY_BOMB_HORIZONTAL_TRIGGER_RANGE = 25.0D",
  "OmbooMoveControl",
  "ChargeAttackGoal",
  "RandomFlyGoal",
  "BombGoal",
  "DelayedBomb",
  "queueBomb()",
  "tickDelayedBombs()",
  "detonateBomb",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "setNoGravity(true)",
  "EventHooks.canEntityGrief",
  "Level.ExplosionInteraction.MOB",
  "ParticleTypes.SMOKE",
  "ModSounds.MOB_SILENCE",
  "ModSounds.OMBOO_GROWL",
  "ModSounds.OMBOO_HURT",
  "ModSounds.OMBOO_DEATH"
]) {
  if (!omboo.includes(marker)) throw new Error(`OmbooEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/OmbooRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<OmbooEntity>",
  "geo/entity/omboo.geo.json",
  "animations/entity/omboo.animation.json",
  "textures/entity/monster/omboo.png",
  "textures/entity/monster/ombooh.png",
  "this.shadowRadius = 1.3F",
  "poseStack.scale(scale, 1.0F / scale, scale)"
]) {
  if (!renderer.includes(marker)) throw new Error(`OmbooRenderer missing renderer marker: ${marker}`);
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.bomber_light"] !== "Light Bomber" || enUs["entity.srparasites.bomber_light.name"] !== "Light Bomber") {
  throw new Error("en_us.json missing Light Bomber entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_omboo"] !== "Spawn Light Bomber") {
  throw new Error("en_us.json missing Spawn Light Bomber item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/omboo.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Omboo geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Omboo geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 84) throw new Error(`Omboo geo must preserve the 84 converted ModelOmboo bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/omboo.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.omboo.func_78087_a", "animation.omboo.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Omboo animation names changed: ${JSON.stringify(animationNames)}`);
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

console.log("Omboo / Light Bomber entity port verifier passed.");
