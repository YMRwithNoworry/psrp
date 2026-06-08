const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Living Bow damage port file: ${file}`);
}

const bow = read("src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java");
for (const marker of [
  "extends BowItem",
  "private static final ThreadLocal<Integer> DRAW_TICKS",
  "releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft)",
  "DRAW_TICKS.set(Math.max(0, getUseDuration(stack, entity) - timeLeft))",
  "super.releaseUsing(stack, level, entity, timeLeft)",
  "DRAW_TICKS.remove()",
  "SrpConfig.WEAPON_BOW_SENTIENT_DAMAGE.get()",
  "SrpConfig.WEAPON_BOW_DAMAGE.get()",
  "SrpConfig.WEAPON_BOW_SENTIENT_BONUS.get()",
  "SrpConfig.WEAPON_BOW_BONUS.get()",
  "SrpConfig.WEAPON_BOW_SENTIENT_DAMAGE_CAP.get()",
  "SrpConfig.WEAPON_BOW_DAMAGE_CAP.get()",
  "Math.min((double) damage * cap, drawSeconds() * (double) bonus)",
  "arrow.setBaseDamage(arrow.getBaseDamage() * multiplier + damage)",
  "return drawTicks == null ? 0 : drawTicks / 20"
]) {
  if (!bow.includes(marker)) throw new Error(`LivingBowItem missing legacy bow formula marker: ${marker}`);
}
for (const forbidden of [
  "Math.min(cap, arrow.getBaseDamage() + damage)",
  "arrow.getBaseDamage() + damage)",
  "velocity / 3.0F"
]) {
  if (bow.includes(forbidden)) throw new Error(`LivingBowItem contains non-legacy bow formula marker: ${forbidden}`);
}

const config = read("src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java");
for (const marker of [
  "WEAPON_BOW_DAMAGE",
  "WEAPON_BOW_BONUS",
  "WEAPON_BOW_DAMAGE_CAP",
  "WEAPON_BOW_SENTIENT_DAMAGE",
  "WEAPON_BOW_SENTIENT_BONUS",
  "WEAPON_BOW_SENTIENT_DAMAGE_CAP",
  'damage("weapon_bow_damage", 1)',
  'damage("weapon_bow_bonus", 1)',
  'damage("weapon_bow_damageCap", 2)',
  'damage("weapon_bow_sentient_damage", 1)',
  'damage("weapon_bow_sentient_bonus", 1)',
  'damage("weapon_bow_sentient_damageCap", 2)'
]) {
  if (!config.includes(marker)) throw new Error(`SrpConfig missing legacy bow config marker: ${marker}`);
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "WeaponToolRangeBase",
  "weapon_bow_bonus",
  "weapon_bow_damageCap",
  "weapon_bow_damage",
  "draw seconds * bonus",
  "damage * damageCap",
  "base arrow damage by the capped multiplier",
  "then adds the fixed damage value",
  "Ported the living greatbow damage formula"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Living Bow damage marker: ${marker}`);
}

console.log("Living Bow damage port verifier passed.");
