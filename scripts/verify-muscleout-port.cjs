const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Muscle Out port file: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("muscleout"',
  "new SrpMobEffect(MobEffectCategory.HARMFUL, 0xEC7F82)",
  "DeferredHolder<MobEffect, MobEffect> MUSCLE_OUT"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing Muscle Out marker: ${marker}`);
}

const config = read("src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java");
for (const marker of [
  "MUSCLE_OUT_DAMAGE_OUT",
  'defineInRange("muscleoutDamageOut", 0.09D, 0.0D, 1.0D)',
  "Legacy SRPConfigSystems: MuscleOut Value"
]) {
  if (!config.includes(marker)) throw new Error(`SrpConfig missing Muscle Out config marker: ${marker}`);
}

const events = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java");
for (const marker of [
  "LivingIncomingDamageEvent",
  "event.getSource().getEntity()",
  "attackerEntity instanceof LivingEntity attacker",
  "attacker.getEffect(ModEffects.MUSCLE_OUT)",
  "muscleOut.getAmplifier() + 1.0F",
  "SrpConfig.MUSCLE_OUT_DAMAGE_OUT.get().floatValue()",
  "event.setAmount(event.getAmount() * multiplier)"
]) {
  if (!events.includes(marker)) throw new Error(`SrpEffectEvents missing Muscle Out damage marker: ${marker}`);
}

const viralBlock = events.slice(events.indexOf("MobEffectInstance viral"), events.indexOf("Entity attackerEntity"));
if (!viralBlock.includes("SrpConfig.VIRAL_ENABLE.get()")) {
  throw new Error("Viral damage amplification should still honor viralEnable");
}
if (events.includes("if (!SrpConfig.VIRAL_ENABLE.get())")) {
  throw new Error("viralEnable must not short-circuit independent Muscle Out outgoing damage");
}
if (events.includes("amount + amount * multiplier") && !events.includes("event.setAmount(event.getAmount() * multiplier)")) {
  throw new Error("Muscle Out must preserve the legacy replacement formula, not Viral's additive formula");
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "muscleoutDamageOut = 0.09",
  "LivingHurtEvent",
  "MUSCLEOUT_E",
  "event source entity",
  "amount * muscleoutDamageOut * (amplifier + 1)",
  "Ported Muscle Out outgoing damage",
  "Muscle Out preserves its legacy replacement formula"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Muscle Out evidence marker: ${marker}`);
}

console.log("Muscle Out port verifier passed.");
