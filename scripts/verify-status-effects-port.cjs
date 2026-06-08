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
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/ContaminationMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/CorrosiveMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/DodSmokeTrailMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/EffectNegMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/EffectPosMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/IndeafMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/OverheatingMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/RageMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpEffectEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpSensingMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java",
  "src/main/resources/assets/srparasites/textures/gui/potion_conta.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_corrosive.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_dod_smoke_trail.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_effectneg.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_effectpos.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_indeaf.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_overheating.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_rage.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_senses.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_vomit.png",
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
  'EFFECTS.register("dod_smoke_trail"',
  "new DodSmokeTrailMobEffect(MobEffectCategory.BENEFICIAL, DodSmokeTrailMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("corrosive"',
  "new CorrosiveMobEffect(MobEffectCategory.HARMFUL, CorrosiveMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("vomit"',
  "SrpSensingMobEffect.LEGACY_VOMIT_COLOR",
  'EFFECTS.register("rage"',
  "new RageMobEffect(MobEffectCategory.BENEFICIAL, RageMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("senses"',
  "SrpSensingMobEffect.LEGACY_SENSES_COLOR",
  'EFFECTS.register("indeaf"',
  "new IndeafMobEffect(MobEffectCategory.BENEFICIAL, IndeafMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("overheating"',
  "new OverheatingMobEffect(MobEffectCategory.HARMFUL, OverheatingMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("conta"',
  "new ContaminationMobEffect(MobEffectCategory.HARMFUL, ContaminationMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("effectpos"',
  "new EffectPosMobEffect(MobEffectCategory.HARMFUL, EffectPosMobEffect.LEGACY_COLOR)",
  'EFFECTS.register("effectneg"',
  "new EffectNegMobEffect(MobEffectCategory.HARMFUL, EffectNegMobEffect.LEGACY_COLOR)"
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

const dodSmokeTrail = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/DodSmokeTrailMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x404040",
  "LEGACY_GROUND_REMOVE_DURATION = 10",
  "LEGACY_PARTICLE_COUNT = 6",
  "LEGACY_PARTICLE_OFFSET = 0.15D",
  "LEGACY_PARTICLE_SPEED = 0.02D",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "return true",
  "entity.level() instanceof ServerLevel serverLevel",
  "entity.getEffect(ModEffects.DOD_SMOKE_TRAIL)",
  "entity.onGround() && duration <= LEGACY_GROUND_REMOVE_DURATION",
  "entity.removeEffect(ModEffects.DOD_SMOKE_TRAIL)",
  "ParticleTypes.SMOKE",
  "entity.getY() + entity.getEyeHeight()",
  "serverLevel.sendParticles("
]) {
  if (!dodSmokeTrail.includes(marker)) throw new Error(`DodSmokeTrailMobEffect missing legacy smoke-trail marker: ${marker}`);
}

const sensing = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SrpSensingMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_VOMIT_COLOR = 0x726C41",
  "LEGACY_SENSES_COLOR = 0x8E9ED7",
  "LEGACY_VOMIT_FOLLOW_RANGE_MULTIPLIER = 0.9D",
  "LEGACY_SENSES_FOLLOW_RANGE_MULTIPLIER = 0.1D",
  "Attributes.FOLLOW_RANGE",
  "ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, modifierPath)",
  "addAttributeModifiers(AttributeMap attributeMap, int amplifier)",
  "removeAttributeModifiers(AttributeMap attributeMap)",
  "this.multiplier * (amplifier + 1.0D)",
  "AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL",
  "attribute.addPermanentModifier(new AttributeModifier(this.modifierId, this.multiplier * (amplifier + 1.0D), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))",
  "attribute.removeModifier(this.modifierId)"
]) {
  if (!sensing.includes(marker)) throw new Error(`SrpSensingMobEffect missing legacy Vomit/Senses marker: ${marker}`);
}

const indeaf = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/IndeafMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0xFFDD00",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "return true",
  "applyEffectTick(LivingEntity entity, int amplifier)",
  "!entity.level().isClientSide",
  "entity.setDeltaMovement(0.0D, entity.getDeltaMovement().y, 0.0D)",
  "entity.xxa = 0.0F",
  "entity.zza = 0.0F"
]) {
  if (!indeaf.includes(marker)) throw new Error(`IndeafMobEffect missing legacy Indeaf marker: ${marker}`);
}

const effectPos = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/EffectPosMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0xB890C8",
  "duration % 20 == 0",
  "entity.getActiveEffects()",
  "effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL",
  "entity.damageSources().magic()",
  "0.5F * (effect.getAmplifier() + 1.0F)"
]) {
  if (!effectPos.includes(marker)) throw new Error(`EffectPosMobEffect missing legacy EffectPos marker: ${marker}`);
}

const effectNeg = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/EffectNegMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x6FACB4",
  "duration % 20 == 0",
  "new ArrayList<>(entity.getActiveEffects())",
  "effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL",
  "SrpMobEffect.applyStackEffect(effect.getEffect(), entity, 20, amplifier)"
]) {
  if (!effectNeg.includes(marker)) throw new Error(`EffectNegMobEffect missing legacy EffectNeg marker: ${marker}`);
}

const overheating = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/OverheatingMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0xFF8706",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "return true",
  "applyEffectTick(LivingEntity entity, int amplifier)",
  "!entity.level().isClientSide",
  "entity.tickCount % 20 == 0",
  "entity.igniteForSeconds(2.0F)"
]) {
  if (!overheating.includes(marker)) throw new Error(`OverheatingMobEffect missing legacy Overheating marker: ${marker}`);
}

const contamination = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/ContaminationMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x9DF100",
  "LEGACY_SELF_DAMAGE_INTERVAL = 40",
  "LEGACY_SPREAD_XZ_RANGE = 4",
  "LEGACY_SPREAD_Y_RANGE = 3",
  "25 >> amplifier",
  "entity.tickCount % LEGACY_SELF_DAMAGE_INTERVAL == 0",
  "entity.getHealth() > 1.0F",
  "entity.hurt(entity.damageSources().magic(), 1.0F)",
  "entity.getEffect(ModEffects.CONTAMINATION)",
  "current == null ? 0 : current.getDuration()",
  "new AABB(",
  "entity.getX() + 1.0D",
  "entity.getY() + 1.0D",
  "entity.getZ() + 1.0D",
  ".inflate(LEGACY_SPREAD_XZ_RANGE, LEGACY_SPREAD_Y_RANGE, LEGACY_SPREAD_XZ_RANGE)",
  "entity.level().getEntitiesOfClass(LivingEntity.class, spreadBox)",
  "SrpMobEffect.applyStackEffect(ModEffects.CONTAMINATION, nearby, duration, amplifier)"
]) {
  if (!contamination.includes(marker)) throw new Error(`ContaminationMobEffect missing legacy Contamination marker: ${marker}`);
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
for (const [id, expected] of [
  ["dod_smoke_trail", "Smoke Trail"],
  ["vomit", "Vomit"],
  ["senses", "Heightened Senses"],
  ["indeaf", "Indeaf"],
  ["overheating", "Overheating"],
  ["conta", "Conta"],
  ["effectpos", "Positive"],
  ["effectneg", "Negative"]
]) {
  const key = `mob_effect.srparasites:${id}`;
  if (!lang[key]) throw new Error(`en_us.json missing legacy ${id} mob effect translation`);
  if (!String(lang[key]).includes(expected)) throw new Error(`Unexpected ${id} translation`);
}

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
  "movement speed and attack damage",
  "DOD_SMOKE_TRAIL_E",
  "EffectDodSmokeTrail",
  "0x404040",
  "SMOKE_NORMAL",
  "srparasites:dod_smoke_trail",
  "VOMIT_E",
  "0x726C41",
  "SENS_E",
  "0x8E9ED7",
  "INDEAF_E",
  "0xFFDD00",
  "EFFECTPOS_E",
  "0xB890C8",
  "EFFECTNEG_E",
  "0x6FACB4",
  "OVERHEATING_E",
  "0xFF8706",
  "CONTA_E",
  "0x9DF100",
  "srparasites:vomit",
  "srparasites:senses",
  "srparasites:indeaf",
  "srparasites:overheating",
  "srparasites:conta",
  "srparasites:effectpos",
  "srparasites:effectneg"
]) {
  if (!audit.includes(marker)) throw new Error(`Port audit missing status-effect marker: ${marker}`);
}

console.log("Status-effect port verifier passed.");
