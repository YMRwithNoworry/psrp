const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/ElviaEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/NadeBallEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/NadeEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/NadeBallRenderer.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/NadeRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/nade.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/nade.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/nade.png",
  "src/main/resources/assets/srparasites/textures/entity/projectile/nade.png",
  "src/main/resources/assets/srparasites/sounds/misc/nade_s.ogg"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Nade port file/resource: ${file}`);
}

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  'ENTITIES.register("nadeball"',
  "EntityType.Builder.<NadeBallEntity>of(NadeBallEntity::new, MobCategory.MISC)",
  'ENTITIES.register("nade"',
  "EntityType.Builder.<NadeEntity>of(NadeEntity::new, MobCategory.MISC)"
]) {
  if (!entities.includes(marker)) throw new Error(`ModEntities missing Nade marker: ${marker}`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
for (const marker of [
  "event.registerEntityRenderer(ModEntities.NADEBALL.get(), NadeBallRenderer::new)",
  "event.registerEntityRenderer(ModEntities.NADE.get(), NadeRenderer::new)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`ModClientEvents missing Nade renderer marker: ${marker}`);
}

const elvia = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/preeminent/ElviaEntity.java");
for (const marker of [
  "import com.dhanantry.scapeandrunparasites.entity.projectile.NadeBallEntity;",
  "this.projectileCycleCount >= 1",
  "this.projectileCycleCount = 0",
  "new NadeBallEntity(this.level(), this, xPower, yPower, zPower, NadeBallEntity.LEGACY_FUSE_TICKS, NadeBallEntity.LEGACY_DURATION_TICKS)",
  "new ElviaBallEntity(this.level(), this, xPower, yPower, zPower)",
  "playProjSound()"
]) {
  if (!elvia.includes(marker)) throw new Error(`ElviaEntity missing alternating Nade marker: ${marker}`);
}

const nadeBall = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/NadeBallEntity.java");
for (const marker of [
  "LEGACY_WIDTH = 0.3F",
  "LEGACY_HEIGHT = 0.3F",
  "LEGACY_FUSE_TICKS = 4",
  "LEGACY_DURATION_TICKS = 60",
  "LEGACY_SHOOT_VELOCITY = 1.6F",
  "new NadeEntity(this.level(), living, this.fuse, this.duration)",
  "ModEntities.NADEBALL"
]) {
  if (!nadeBall.includes(marker)) throw new Error(`NadeBallEntity missing legacy marker: ${marker}`);
}

const nade = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/projectile/NadeEntity.java");
for (const marker of [
  'LEGACY_MODEL_ANIMATION_NAME = "animation.nade.func_78087_a"',
  'LEGACY_COSMICAL_ANIMATION_NAME = "animation.nade.setRotationAnglesCosmical"',
  "LEGACY_WIDTH = 0.5F",
  "LEGACY_HEIGHT = 0.5F",
  "LEGACY_GROWTH_WIDTH = 0.8F",
  "LEGACY_GROWTH_HEIGHT = 0.32F",
  "LEGACY_DEFAULT_FUSE = 3",
  "LEGACY_DEFAULT_DURATION = 10",
  "LEGACY_ELVIA_FUSE = 4",
  "LEGACY_ELVIA_DURATION = 60",
  "LEGACY_SOUND_TICK = 2",
  "ModSounds.NADE_S",
  "ParticleTypes.SMOKE",
  "ParticleTypes.LARGE_SMOKE",
  "!(living instanceof SrpParasiteMob)",
  "Attributes.ATTACK_DAMAGE"
]) {
  if (!nade.includes(marker)) throw new Error(`NadeEntity missing legacy marker: ${marker}`);
}

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/NadeRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<NadeEntity>",
  "geo/entity/nade.geo.json",
  "animations/entity/nade.animation.json",
  "textures/entity/monster/nade.png",
  "poseStack.scale(horizontal, vertical, horizontal)"
]) {
  if (!renderer.includes(marker)) throw new Error(`NadeRenderer missing renderer marker: ${marker}`);
}

const ballRenderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/NadeBallRenderer.java");
for (const marker of ["textures/entity/projectile/nade.png", "RenderType.entityCutoutNoCull", "cameraOrientation()"]) {
  if (!ballRenderer.includes(marker)) throw new Error(`NadeBallRenderer missing renderer marker: ${marker}`);
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/nade.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected Nade geo format_version: ${geo.format_version}`);
const bones = geo["minecraft:geometry"]?.[0]?.bones || [];
if (bones.length !== 1) throw new Error(`Nade geo must preserve the 1 converted ModelNade bone, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/nade.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.nade.func_78087_a", "animation.nade.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`Nade animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like the converted legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 1) throw new Error(`${name} must keep 1 animated bone, found ${bonesWithChannels.length}`);
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
  "EntityProjectileNade",
  "nadeball",
  "EntityNade",
  "nade.s",
  "modern Nade slice"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Nade marker: ${marker}`);
}
if (audit.includes("EntityProjectileNade remains an explicit future slice")) {
  throw new Error("Audit must not still claim EntityProjectileNade is wholly unported after the modern Nade slice");
}

console.log("Nade projectile and delayed burst port verifier passed.");
