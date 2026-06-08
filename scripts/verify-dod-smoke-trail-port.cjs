const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/DodSmokeTrailMobEffect.java",
  "src/main/resources/assets/srparasites/textures/gui/potion_dod_smoke_trail.png",
  "src/main/resources/assets/srparasites/lang/en_us.json",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Dod Smoke Trail port file/resource: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("dod_smoke_trail"',
  "DeferredHolder<MobEffect, DodSmokeTrailMobEffect> DOD_SMOKE_TRAIL",
  "new DodSmokeTrailMobEffect(MobEffectCategory.BENEFICIAL, DodSmokeTrailMobEffect.LEGACY_COLOR)"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing Dod Smoke Trail marker: ${marker}`);
}

const effect = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/DodSmokeTrailMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x404040",
  "LEGACY_GROUND_REMOVE_DURATION = 10",
  "LEGACY_PARTICLE_COUNT = 6",
  "LEGACY_PARTICLE_OFFSET = 0.15D",
  "LEGACY_PARTICLE_SPEED = 0.02D",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "return true",
  "applyEffectTick(LivingEntity entity, int amplifier)",
  "entity.level() instanceof ServerLevel serverLevel",
  "entity.getEffect(ModEffects.DOD_SMOKE_TRAIL)",
  "current == null ? 0 : current.getDuration()",
  "entity.onGround() && duration <= LEGACY_GROUND_REMOVE_DURATION",
  "entity.removeEffect(ModEffects.DOD_SMOKE_TRAIL)",
  "serverLevel.sendParticles(",
  "ParticleTypes.SMOKE",
  "entity.getX()",
  "entity.getY() + entity.getEyeHeight()",
  "entity.getZ()",
  "LEGACY_PARTICLE_COUNT",
  "LEGACY_PARTICLE_OFFSET",
  "LEGACY_PARTICLE_SPEED"
]) {
  if (!effect.includes(marker)) throw new Error(`DodSmokeTrailMobEffect missing legacy marker: ${marker}`);
}

for (const forbidden of [
  "ParticleTypes.CAMPFIRE_COSY_SMOKE",
  "ParticleTypes.CAMPFIRE_SIGNAL_SMOKE",
  "ParticleTypes.LARGE_SMOKE",
  "entity.hurt(",
  "entity.addEffect("
]) {
  if (effect.includes(forbidden)) throw new Error(`DodSmokeTrailMobEffect contains non-legacy marker: ${forbidden}`);
}

const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (!lang["mob_effect.srparasites:dod_smoke_trail"]) throw new Error("en_us.json missing Dod Smoke Trail mob effect translation");
if (!String(lang["mob_effect.srparasites:dod_smoke_trail"]).includes("Smoke Trail")) throw new Error("Unexpected Dod Smoke Trail translation");

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "DOD_SMOKE_TRAIL_E",
  "EffectDodSmokeTrail",
  "dod_smoke_trail",
  "0x404040",
  "SMOKE_NORMAL",
  "6",
  "0.15",
  "0.02",
  "srparasites:dod_smoke_trail",
  "grounded",
  "10"
]) {
  if (!audit.includes(marker)) throw new Error(`Port audit missing Dod Smoke Trail marker: ${marker}`);
}

console.log("Dod Smoke Trail port verifier passed.");
