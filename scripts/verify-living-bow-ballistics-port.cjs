const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Living Bow ballistics port file: ${file}`);
}

const bow = read("src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java");
for (const marker of [
  "private static final float MODERN_FULL_POWER_ARROW_VELOCITY = 3.0F",
  "private static final float LEGACY_FULL_POWER_ARROW_VELOCITY = 4.4F",
  "private static final float LEGACY_ARROW_INACCURACY = 0.0F",
  "float legacyVelocity = velocity * (LEGACY_FULL_POWER_ARROW_VELOCITY / MODERN_FULL_POWER_ARROW_VELOCITY)",
  "projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, legacyVelocity, LEGACY_ARROW_INACCURACY)"
]) {
  if (!bow.includes(marker)) throw new Error(`LivingBowItem missing legacy ballistics marker: ${marker}`);
}

const shootStart = bow.indexOf("protected void shootProjectile");
const shootEnd = bow.indexOf("private static int drawSeconds()");
if (shootStart === -1 || shootEnd === -1 || shootEnd <= shootStart) {
  throw new Error("Could not locate LivingBowItem.shootProjectile body");
}
const shootBody = bow.slice(shootStart, shootEnd);
for (const forbidden of [
  "super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target)",
  "shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy)",
  "LEGACY_ARROW_INACCURACY = 1.0F"
]) {
  if (shootBody.includes(forbidden)) throw new Error(`LivingBowItem still contains non-legacy ballistics marker: ${forbidden}`);
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "power * 4.4",
  "zero inaccuracy",
  "modern `BowItem` passes `power * 3.0`",
  "legacy `power * 4.4`",
  "Ported the living greatbow legacy arrow ballistics"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Living Bow ballistics marker: ${marker}`);
}

console.log("Living Bow ballistics port verifier passed.");
