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
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/deterrent/NakEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/NakRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/nak.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/nak.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/nak.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_nak.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_nak.png"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Nak/Seizer port file/resource: ${file}`);
}
if (exists("src/main/resources/assets/srparasites/textures/entity/monster/snowvariants/seizerfrozen.png")) {
  throw new Error("Verifier expected seizerfrozen.png to remain absent for the 1.10.6 jar-backed slice");
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("seizer"',
  "EntityType.Builder.of(NakEntity::new, MobCategory.MONSTER)",
  ".sized(NakEntity.LEGACY_WIDTH, NakEntity.LEGACY_HEIGHT)",
  ".eyeHeight(NakEntity.LEGACY_EYE_HEIGHT)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Nak marker: ${marker}`);
}

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["NakEntity.createAttributes().build()", "ModEntities.NAK.get()"]) {
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing Nak attribute marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_NAK", '"itemmobspawner_nak"', "DeferredSpawnEggItem", "ModEntities.NAK"]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Nak spawn egg marker: ${marker}`);
}
if (items.includes('ITEMMOBSPAWNER_NAK = legacyItem("itemmobspawner_nak"')) {
  throw new Error("Nak spawn egg must not remain a legacy placeholder item");
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.NAK.get(), NakRenderer::new)")) {
  throw new Error("ModClientEvents missing Nak renderer registration");
}

const nak = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/deterrent/NakEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 72",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.nak.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.nak.setRotationAnglesCosmical"',
  "LEGACY_HEALTH = 15.0D",
  "LEGACY_ARMOR = 10.0D",
  "LEGACY_ATTACK_DAMAGE = 6.0D",
  "LEGACY_MOVEMENT_SPEED = 0.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 1.0D",
  "LEGACY_FOLLOW_RANGE = 6.0D",
  "LEGACY_WIDTH = 0.7F",
  "LEGACY_HEIGHT = 2.5F",
  "LEGACY_EYE_HEIGHT = 1.8F",
  "LEGACY_XP = 0",
  "LEGACY_TYPE = 40",
  "LEGACY_BURIED_TIME = 4.0D",
  "LEGACY_GRAB_RANGE_SQR = 25.0D",
  "LEGACY_MIN_PULL_RANGE_SQR = 1.0D",
  "LEGACY_SEARCHING_STATUS = 2",
  "LEGACY_GRABBING_STATUS = 3",
  "LEGACY_TARGET_LOST_RETREAT_TICKS = 60",
  "LEGACY_INITIAL_SLOW_TICKS = 80",
  "LEGACY_PULL_SLOW_TICKS = 20",
  "LEGACY_SLOW_AMPLIFIER = 2",
  "LEGACY_PULL_STRENGTH = 0.5D",
  "LEGACY_PULL_DAMPING = 0.5D",
  "LEGACY_ATTACK_EVENT = 12",
  "LEGACY_RETREAT_EVENT = 51",
  "HurtByTargetGoal(this)",
  "NearestAttackableTargetGoal<>(this, Player.class",
  "WaterAnimal",
  "Animal",
  "TARGET_ENTITY",
  "hasTargetedEntity()",
  "getTargetedEntity()",
  "tickLegacyGrab()",
  "pullTargetedEntity()",
  "MobEffects.MOVEMENT_SLOWDOWN",
  "target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LEGACY_INITIAL_SLOW_TICKS, LEGACY_SLOW_AMPLIFIER",
  "target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LEGACY_PULL_SLOW_TICKS, LEGACY_SLOW_AMPLIFIER",
  "Math.signum(getX() - target.getX()) * LEGACY_PULL_STRENGTH",
  "ModSounds.MOB_TENDRIL",
  "ModSounds.MOB_SILENCE",
  "ModSounds.DOD_NAK",
  "direct instanceof Projectile",
  "target.hurt(source, amount * 2.0F)"
]) {
  if (!nak.includes(marker)) throw new Error(`NakEntity missing legacy behavior marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/NakRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<NakEntity>",
  "geo/entity/nak.geo.json",
  "animations/entity/nak.animation.json",
  "textures/entity/monster/nak.png",
  "this.shadowRadius = 0.4F"
]) {
  if (!renderer.includes(marker)) throw new Error(`NakRenderer missing renderer marker: ${marker}`);
}
if (renderer.includes("seizerfrozen.png")) {
  throw new Error("NakRenderer must not reference missing legacy seizerfrozen.png texture");
}

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.seizer"] !== "Seizer" || enUs["entity.srparasites.seizer.name"] !== "Seizer") {
  throw new Error("en_us.json missing Seizer entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_nak"] !== "Spawn Seizer") {
  throw new Error("en_us.json missing Spawn Seizer item translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/nak.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Nak geo format_version: ${geo.format_version}`);
if (!Array.isArray(geo["minecraft:geometry"]) || geo["minecraft:geometry"].length !== 1) {
  throw new Error("Nak geo must contain exactly one minecraft:geometry entry");
}
const bones = geo["minecraft:geometry"][0].bones || [];
if (bones.length !== 66) throw new Error(`Nak geo must preserve the 66 converted ModelNak bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/nak.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.nak.func_78087_a", "animation.nak.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Nak animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 15) throw new Error(`${name} must keep 15 animated bones, found ${bonesWithChannels.length}`);
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

console.log("Nak / Seizer entity port verifier passed.");
