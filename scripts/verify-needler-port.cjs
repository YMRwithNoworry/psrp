const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/NeedlerMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java",
  "src/main/resources/assets/srparasites/textures/gui/potion_needler.png",
  "src/main/resources/assets/srparasites/lang/en_us.json",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Needler port file/resource: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("needler"',
  "DeferredHolder<MobEffect, NeedlerMobEffect> NEEDLER",
  "new NeedlerMobEffect(MobEffectCategory.BENEFICIAL, NeedlerMobEffect.LEGACY_COLOR)"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing Needler marker: ${marker}`);
}

const needler = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/NeedlerMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0xC7B403",
  "LEGACY_DEFAULT_DAMAGE_FRACTION = 0.4F",
  "LEGACY_DEFAULT_TERMINAL_AMPLIFIER = 7",
  "LEGACY_DEFAULT_MAX_DAMAGE_PLAYER = 1.0E9F",
  "LEGACY_DEFAULT_MAX_DAMAGE_MONSTER = 1.0E9F",
  "LEGACY_REAPPLY_DURATION = 400",
  "LEGACY_REGENERATION_DURATION = 900",
  "LEGACY_REGENERATION_AMPLIFIER = 1",
  "LEGACY_ABSORPTION_DURATION = 100",
  "LEGACY_ABSORPTION_AMPLIFIER = 1",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "25 >> amplifier",
  "duration % interval == 0",
  "applyEffectTick(LivingEntity entity, int amplifier)",
  "entity.level().isClientSide",
  "amplifier < SrpConfig.NEEDLER_TERMINAL_AMPLIFIER.get()",
  "amplifier - SrpConfig.NEEDLER_TERMINAL_AMPLIFIER.get()",
  "entity.removeEffect(ModEffects.NEEDLER)",
  "BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()",
  "listed != SrpConfig.NEEDLER_IMMUNE_LIST_WHITE.get()",
  "entity.addEffect(new MobEffectInstance(ModEffects.NEEDLER, LEGACY_REAPPLY_DURATION, nextAmplifier, false, false))",
  "entity.getMaxHealth() * SrpConfig.NEEDLER_DAMAGE.get().floatValue()",
  "entity instanceof Player ? SrpConfig.NEEDLER_MAX_DAMAGE_PLAYER.get().floatValue() : SrpConfig.NEEDLER_MAX_DAMAGE_MONSTER.get().floatValue()",
  "entity.setHealth(currentHealth - damage)",
  "level.broadcastEntityEvent(entity, (byte) 2)",
  "level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 0.0F, false, Level.ExplosionInteraction.NONE)",
  "triggerLegacyNeedlerDeath(entity)",
  "entity.damageSources().fellOutOfWorld()",
  "for (InteractionHand hand : InteractionHand.values())",
  "held.is(Items.TOTEM_OF_UNDYING)",
  "CommonHooks.onLivingUseTotem(entity, damageSource, held, hand)",
  "held.shrink(1)",
  "serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1)",
  "CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, totem)",
  "entity.setHealth(1.0F)",
  "entity.removeAllEffects()",
  "new MobEffectInstance(MobEffects.REGENERATION, LEGACY_REGENERATION_DURATION, LEGACY_REGENERATION_AMPLIFIER)",
  "new MobEffectInstance(MobEffects.ABSORPTION, LEGACY_ABSORPTION_DURATION, LEGACY_ABSORPTION_AMPLIFIER)",
  "entity.level().broadcastEntityEvent(entity, (byte) 35)",
  "entity.die(damageSource)"
]) {
  if (!needler.includes(marker)) throw new Error(`NeedlerMobEffect missing legacy marker: ${marker}`);
}

for (const forbidden of [
  "MobEffects.FIRE_RESISTANCE",
  "entity.hurt(",
  "Level.ExplosionInteraction.BLOCK",
  "Level.ExplosionInteraction.TNT"
]) {
  if (needler.includes(forbidden)) throw new Error(`NeedlerMobEffect contains non-legacy marker: ${forbidden}`);
}

const config = read("src/main/java/com/dhanantry/scapeandrunparasites/util/config/SrpConfig.java");
for (const marker of [
  "NEEDLER_DAMAGE",
  'defineInRange("needlerDamage", 0.4D, 0.0D, 1.0D)',
  "NEEDLER_TERMINAL_AMPLIFIER",
  'defineInRange("needlerTerminal", 7, 0, 100)',
  "NEEDLER_MAX_DAMAGE_PLAYER",
  'defineInRange("needlerMaxDamPlayer", 1.0E9D, 0.0D, 2.0E9D)',
  "NEEDLER_MAX_DAMAGE_MONSTER",
  'defineInRange("needlerMaxDamMonster", 1.0E9D, 0.0D, 2.0E9D)',
  "NEEDLER_IMMUNE_LIST",
  'defineListAllowEmpty("needlerImmuneList", List.of(), value -> value instanceof String)',
  "NEEDLER_IMMUNE_LIST_WHITE",
  'define("needlerImmuneListWhite", false)'
]) {
  if (!config.includes(marker)) throw new Error(`SrpConfig missing legacy Needler config marker: ${marker}`);
}

const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (!lang["mob_effect.srparasites:needler"]) throw new Error("en_us.json missing Needler mob effect translation");
if (!String(lang["mob_effect.srparasites:needler"]).includes("Needler")) throw new Error("Unexpected Needler translation");

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
const auditFlat = audit.replace(/\s+/g, " ");
for (const marker of [
  "DLER_E",
  "0xC7B403",
  "PotionNeedler",
  "needlerDamage = 0.4",
  "needlerTerminal = 7",
  "needlerMaxDamPlayer = 1.0E9",
  "needlerMaxDamMonster = 1.0E9",
  "needlerImmuneList",
  "srparasites:needler",
  "terminal amplifier",
  "400",
  "totem",
  "fellOutOfWorld"
]) {
  if (!auditFlat.includes(marker)) throw new Error(`Port audit missing Needler marker: ${marker}`);
}

console.log("Needler port verifier passed.");
