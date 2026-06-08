const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/item/SrpEquipmentEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java",
  "src/main/resources/assets/srparasites/lang/en_us.json",
  "src/main/resources/assets/srparasites/models/item/weapon_sword.json",
  "src/main/resources/assets/srparasites/textures/items/weapon_sword.png"
];

for (const file of required) {
  if (!fs.existsSync(path.join(root, file))) {
    throw new Error(`Missing required port slice file: ${file}`);
  }
}

const modItems = fs.readFileSync(path.join(root, "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java"), "utf8");
for (const marker of ["weapon_scythe", "weapon_bow_sentient", "armor_helm_sentient", "hijacked_iron_sword"]) {
  if (!modItems.includes(marker)) throw new Error(`ModItems is missing marker ${marker}`);
}

const events = fs.readFileSync(path.join(root, "src/main/java/com/dhanantry/scapeandrunparasites/item/SrpEquipmentEvents.java"), "utf8");
for (const marker of ["ItemAttributeModifierEvent", "LivingDeathEvent", "LivingDamageEvent.Post", "SrpItemData.KILLS", "SrpItemData.HITS"]) {
  if (!events.includes(marker)) throw new Error(`SrpEquipmentEvents is missing behavior marker ${marker}`);
}

const bow = fs.readFileSync(path.join(root, "src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java"), "utf8");
for (const marker of [
  "WEAPON_BOW_BONUS",
  "WEAPON_BOW_SENTIENT_BONUS",
  "WEAPON_BOW_DAMAGE_CAP",
  "WEAPON_BOW_SENTIENT_DAMAGE_CAP",
  "Math.min((double) damage * cap, drawSeconds() * (double) bonus)",
  "arrow.setBaseDamage(arrow.getBaseDamage() * multiplier + damage)",
  "LEGACY_FULL_POWER_ARROW_VELOCITY = 4.4F",
  "LEGACY_ARROW_INACCURACY = 0.0F",
  "projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, legacyVelocity, LEGACY_ARROW_INACCURACY)",
  "projectileStack.getItem() instanceof TippedArrowItem",
  "tippedArrow.addEffect(new MobEffectInstance(ModEffects.BLEED, LEGACY_TIPPED_ARROW_EFFECT_TICKS, LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER, false, true))",
  "tippedArrow.addEffect(new MobEffectInstance(ModEffects.DOD_SMOKE_TRAIL, LEGACY_TIPPED_ARROW_EFFECT_TICKS, LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER, false, true))"
]) {
  if (!bow.includes(marker)) throw new Error(`LivingBowItem is missing legacy bow damage marker ${marker}`);
}

const itemData = fs.readFileSync(path.join(root, "src/main/java/com/dhanantry/scapeandrunparasites/item/SrpItemData.java"), "utf8");
for (const marker of ['KILLS = "srpkills"', 'HITS = "srphits"', "DataComponents.CUSTOM_DATA"]) {
  if (!itemData.includes(marker)) throw new Error(`SrpItemData is missing persistence marker ${marker}`);
}

console.log("SRP port slice verifier passed.");
