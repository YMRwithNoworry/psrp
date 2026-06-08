const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/OverheatingMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Overheating port file: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("overheating"',
  "new OverheatingMobEffect(MobEffectCategory.HARMFUL, OverheatingMobEffect.LEGACY_COLOR)",
  "DeferredHolder<MobEffect, OverheatingMobEffect> OVERHEATING"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing Overheating marker: ${marker}`);
}

const overheating = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/OverheatingMobEffect.java");
for (const marker of [
  "LEGACY_COLOR = 0xFF8706",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "return true",
  "applyEffectTick(LivingEntity entity, int amplifier)",
  "!entity.level().isClientSide",
  "entity.tickCount % 20 == 0",
  "entity.igniteForSeconds(2.0F)"
]) {
  if (!overheating.includes(marker)) throw new Error(`OverheatingMobEffect missing tick marker: ${marker}`);
}

const events = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java");
for (const marker of [
  "LivingIncomingDamageEvent",
  "net.minecraft.world.damagesource.DamageTypes",
  "event.getEntity().getEffect(ModEffects.OVERHEATING)",
  "event.getSource().is(DamageTypes.IN_FIRE)",
  "event.getSource().is(DamageTypes.ON_FIRE)",
  "overheating.getAmplifier() + 1.0F",
  "event.setAmount(amount + amount * multiplier)"
]) {
  if (!events.includes(marker)) throw new Error(`SrpEffectEvents missing Overheating damage marker: ${marker}`);
}

const viralStart = events.indexOf("MobEffectInstance viral");
const overheatingStart = events.indexOf("MobEffectInstance overheating");
const muscleOutStart = events.indexOf("Entity attackerEntity");
if (viralStart === -1 || overheatingStart === -1 || muscleOutStart === -1) {
  throw new Error("SrpEffectEvents is missing Viral, Overheating, or Muscle Out damage blocks");
}
if (!(viralStart < overheatingStart && overheatingStart < muscleOutStart)) {
  throw new Error("Overheating damage amplification must preserve old order: Viral, Overheating, then Muscle Out");
}

const overheatingBlock = events.slice(overheatingStart, muscleOutStart);
for (const forbidden of ["DamageTypes.LAVA", "DamageTypes.HOT_FLOOR", "lava()", "hotFloor()"]) {
  if (overheatingBlock.includes(forbidden)) {
    throw new Error(`Overheating fire amplification should not include non-evidenced source: ${forbidden}`);
  }
}
if (overheatingBlock.includes("SrpConfig.")) {
  throw new Error("Legacy Overheating fire amplification has no config gate or multiplier");
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "PotionOverheat",
  "tickCount % 20 == 0",
  "sets the host on fire for",
  "SRPEventHandlerBus",
  "LivingHurtEvent",
  "OVERHEATING_E",
  "inFire",
  "onFire",
  "amount + amount * (amplifier + 1)",
  "Ported Overheating fire damage amplification",
  "DamageTypes.IN_FIRE",
  "DamageTypes.ON_FIRE"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Overheating evidence marker: ${marker}`);
}

console.log("Overheating port verifier passed.");
