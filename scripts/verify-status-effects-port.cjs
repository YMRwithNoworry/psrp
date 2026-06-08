const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/BleedMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/CorrosiveMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/RageMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java",
  "src/main/resources/assets/srparasites/textures/gui/potion_corrosive.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_rage.png",
  "src/main/resources/assets/srparasites/lang/en_us.json"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing status-effect port file/resource: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  "Registries.MOB_EFFECT",
  'EFFECTS.register("viral"',
  "new SrpMobEffect(MobEffectCategory.HARMFUL, 0x136334)",
  'EFFECTS.register("bleed"',
  "new BleedMobEffect(MobEffectCategory.HARMFUL, 0x5E0806)",
  'EFFECTS.register("corrosive"',
  "new CorrosiveMobEffect(MobEffectCategory.HARMFUL, CorrosiveMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("rage"',
  "new RageMobEffect(MobEffectCategory.BENEFICIAL, RageMobEffect.LEGACY_COLOR)"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing status-effect marker: ${marker}`);
}

const config = read("src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java");
for (const marker of [
  "BLEEDING_DAMAGE",
  'defineInRange("bleedingDamage", 0.06D',
  "BLEEDING_DAMAGE_CAP",
  'defineInRange("bleedingDamageCap", 100.0D',
  "CORROSIVE_DAMAGE_VALUE",
  'defineInRange("corroValue", 3',
  "CORROSIVE_DURABILITY_THRESHOLD",
  'defineInRange("corrNot", 0.1D',
  "VIRAL_ENABLE",
  'define("viralEnable", true)',
  "VIRAL_AMOUNT",
  'defineInRange("viralAmount", 0.5D',
  "RAGE_ENABLE",
  'define("rageEnable", true)',
  "RAGE_DAMAGE",
  'defineInRange("rageDamage", 0.1D',
  "RAGE_SPEED",
  'defineInRange("rageSpeed", 0.1D',
  "STACKABLE_POTIONS_LIMIT",
  "stackablePotionsLimit"
]) {
  if (!config.includes(marker)) throw new Error(`SrpConfig missing legacy status config marker: ${marker}`);
}

const corrosive = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/CorrosiveMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x7A605A",
  "LEGACY_DEFAULT_DAMAGE_VALUE = 3",
  "LEGACY_DEFAULT_DURABILITY_THRESHOLD = 0.1D",
  "LEGACY_ARMOR_SLOTS",
  "EquipmentSlot.HEAD",
  "EquipmentSlot.CHEST",
  "EquipmentSlot.LEGS",
  "EquipmentSlot.FEET",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "25 >> amplifier",
  "applyEffectTick(LivingEntity entity, int amplifier)",
  "!entity.level().isClientSide",
  "stack.isDamageableItem()",
  "stack.getMaxDamage() - stack.getDamageValue()",
  "SrpConfig.CORROSIVE_DURABILITY_THRESHOLD.get()",
  "stack.hurtAndBreak(SrpConfig.CORROSIVE_DAMAGE_VALUE.get(), entity, slot)"
]) {
  if (!corrosive.includes(marker)) throw new Error(`CorrosiveMobEffect missing legacy Corrosive marker: ${marker}`);
}
for (const forbidden of ["entity.hurt(", "getHandSlots()", "getAllSlots()"]) {
  if (corrosive.includes(forbidden)) throw new Error(`CorrosiveMobEffect contains non-legacy behavior marker: ${forbidden}`);
}

const rage = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/RageMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0xF84343",
  "LEGACY_DEFAULT_DAMAGE_MULTIPLIER = 0.1D",
  "LEGACY_DEFAULT_SPEED_MULTIPLIER = 0.1D",
  'ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "effect.rage_speed")',
  'ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "effect.rage_damage")',
  "addAttributeModifiers(AttributeMap attributeMap, int amplifier)",
  "removeAttributeModifiers(AttributeMap attributeMap)",
  "Attributes.MOVEMENT_SPEED",
  "Attributes.ATTACK_DAMAGE",
  "SrpConfig.RAGE_SPEED.get() * (amplifier + 1.0D)",
  "SrpConfig.RAGE_DAMAGE.get() * (amplifier + 1.0D)",
  "AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL",
  "attribute.addPermanentModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))",
  "attribute.removeModifier(id)"
]) {
  if (!rage.includes(marker)) throw new Error(`RageMobEffect missing legacy Rage marker: ${marker}`);
}

const srpEffect = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpMobEffect.java");
for (const marker of [
  "fillEffectCures",
  "cures.clear()",
  "applyStackEffect",
  "STACKABLE_POTIONS_LIMIT"
]) {
  if (!srpEffect.includes(marker)) throw new Error(`SrpMobEffect missing base status marker: ${marker}`);
}

const bleed = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/BleedMobEffect.java");
for (const marker of ["25 >> amplifier", "DAMAGE_INDICATOR", "BLEEDING_DAMAGE", "BLEEDING_DAMAGE_CAP"]) {
  if (!bleed.includes(marker)) throw new Error(`BleedMobEffect missing legacy bleed marker: ${marker}`);
}

const events = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java");
for (const marker of ["LivingIncomingDamageEvent", "VIRAL_ENABLE", "VIRAL_AMOUNT", "setAmount"]) {
  if (!events.includes(marker)) throw new Error(`SrpEffectEvents missing viral event marker: ${marker}`);
}

const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (!lang["mob_effect.srparasites:corrosive"]) throw new Error("en_us.json missing legacy Corrosive mob effect translation");
if (!String(lang["mob_effect.srparasites:corrosive"]).includes("Corrosion")) throw new Error("Unexpected Corrosive translation");
if (!lang["mob_effect.srparasites:rage"]) throw new Error("en_us.json missing legacy Rage mob effect translation");
if (!String(lang["mob_effect.srparasites:rage"]).includes("Rage")) throw new Error("Unexpected Rage translation");

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
for (const marker of [
  "CORRO_E",
  "0x7A605A",
  "corroValue = 3",
  "corrNot = 0.1",
  "srparasites:corrosive",
  "armor-only durability corrosion",
  "RAGE_E",
  "0xF84343",
  "rageEnable = true",
  "rageDamage = 0.1",
  "rageSpeed = 0.1",
  "srparasites:rage",
  "movement speed and attack damage"
]) {
  if (!audit.includes(marker)) throw new Error(`Port audit missing Rage marker: ${marker}`);
}

console.log("Status-effect port verifier passed.");
