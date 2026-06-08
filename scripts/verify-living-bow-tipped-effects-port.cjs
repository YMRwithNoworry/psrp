const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Living Bow tipped-arrow port file: ${file}`);
}

const bow = read("src/main/java/com/dhanantry/scapeandrunparasites/item/LivingBowItem.java");
for (const marker of [
  "import com.dhanantry.scapeandrunparasites.init.ModEffects;",
  "import net.minecraft.world.effect.MobEffectInstance;",
  "import net.minecraft.world.entity.projectile.Arrow;",
  "import net.minecraft.world.item.TippedArrowItem;",
  "private static final int LEGACY_TIPPED_ARROW_EFFECT_TICKS = 200",
  "private static final int LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER = 0",
  "public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack)",
  "AbstractArrow customized = super.customArrow(arrow, projectileStack, weaponStack)",
  "projectileStack.getItem() instanceof TippedArrowItem",
  "customized instanceof Arrow tippedArrow",
  "tippedArrow.addEffect(new MobEffectInstance(ModEffects.BLEED, LEGACY_TIPPED_ARROW_EFFECT_TICKS, LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER, false, true))",
  "tippedArrow.addEffect(new MobEffectInstance(ModEffects.DOD_SMOKE_TRAIL, LEGACY_TIPPED_ARROW_EFFECT_TICKS, LEGACY_TIPPED_ARROW_EFFECT_AMPLIFIER, false, true))",
  "return customized"
]) {
  if (!bow.includes(marker)) throw new Error(`LivingBowItem missing tipped-arrow marker: ${marker}`);
}

const customArrowStart = bow.indexOf("public AbstractArrow customArrow");
const shootProjectileStart = bow.indexOf("protected void shootProjectile");
if (customArrowStart === -1 || shootProjectileStart === -1 || customArrowStart > shootProjectileStart) {
  throw new Error("LivingBowItem should add legacy tipped-arrow effects during customArrow before projectile shooting");
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("bleed"',
  'EFFECTS.register("dod_smoke_trail"',
  "DeferredHolder<MobEffect, BleedMobEffect> BLEED",
  "DeferredHolder<MobEffect, DodSmokeTrailMobEffect> DOD_SMOKE_TRAIL"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing tipped-arrow effect registration marker: ${marker}`);
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "EntityTippedArrow",
  "BLEED_E",
  "DOD_SMOKE_TRAIL_E",
  "`200` ticks",
  "amplifier `0`",
  "ambient `false`",
  "visible `true`",
  "Ported the living greatbow legacy tipped-arrow effects",
  "TippedArrowItem"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Living Bow tipped-arrow marker: ${marker}`);
}

console.log("Living Bow tipped-arrow effects port verifier passed.");
