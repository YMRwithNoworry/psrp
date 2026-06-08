const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/ThornshadeThornsMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/ThornshadeThornsEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/item/ThornshadeDecanterItem.java",
  "src/main/resources/assets/srparasites/models/item/thornshade_decanter.json",
  "src/main/resources/assets/srparasites/textures/gui/potion_thornshade_thorns.png",
  "src/main/resources/assets/srparasites/lang/en_us.json",
  "src/main/resources/data/srparasites/advancement/thornshade_self_destruct.json"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Thornshade port file/resource: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("thornshade_thorns"',
  "new ThornshadeThornsMobEffect(MobEffectCategory.BENEFICIAL, ThornshadeThornsMobEffect.LEGACY_COLOR)",
  "DeferredHolder<MobEffect, ThornshadeThornsMobEffect> THORNSHADE_THORNS"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing Thornshade marker: ${marker}`);
}

const effect = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/ThornshadeThornsMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x421F7E",
  "LEGACY_POTION_DURATION = 60",
  "shouldApplyEffectTickThisTick(int duration, int amplifier)",
  "return false"
]) {
  if (!effect.includes(marker)) throw new Error(`ThornshadeThornsMobEffect missing legacy marker: ${marker}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of [
  'register("thornshade_decanter"',
  "new ThornshadeDecanterItem(new Item.Properties().stacksTo(ThornshadeDecanterItem.LEGACY_STACK_SIZE))"
]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing Thornshade Decanter marker: ${marker}`);
}

const decanter = read("src/main/java/com/dhanantry/scapeandrunparasites/item/ThornshadeDecanterItem.java");
for (const marker of [
  "extends Item",
  "EFFECT_DURATION_TICKS = 400",
  "USE_DURATION_TICKS = 32",
  "LEGACY_STACK_SIZE = 16",
  "player.startUsingItem(hand)",
  "new MobEffectInstance(ModEffects.THORNSHADE_THORNS, EFFECT_DURATION_TICKS, 0, false, true)",
  "player.getAbilities().instabuild",
  "stack.shrink(1)",
  "UseAnim.DRINK",
  "getUseDuration(ItemStack stack, LivingEntity entity)"
]) {
  if (!decanter.includes(marker)) throw new Error(`ThornshadeDecanterItem missing legacy marker: ${marker}`);
}

const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");
if (!main.includes("NeoForge.EVENT_BUS.register(ThornshadeThornsEvents.class)")) {
  throw new Error("SRPMain does not register ThornshadeThornsEvents on the NeoForge event bus");
}

const events = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/ThornshadeThornsEvents.java");
for (const marker of [
  "MobEffectEvent.Applicable",
  "LivingDamageEvent.Pre",
  "EntityTickEvent.Post",
  'TAG_ROOT = "srp_thornshade_thorns"',
  'TAG_USES = "Uses"',
  'TAG_COOLDOWN_UNTIL = "CooldownUntil"',
  'TAG_EXPLODE_DELAY = "ExplodeDelay"',
  'TAG_HAS_EXPLODED = "HasExplodedOnce"',
  "LEGACY_MAX_HP_ALLOWED = 120.0F",
  "LEGACY_MAX_APPLICATIONS_BEFORE_EXPLOSION = 2",
  "LEGACY_EXPLODE_DELAY_TICKS = 20",
  "LEGACY_INFINITE_DURATION_THRESHOLD = 72000",
  "LEGACY_SINGLE_USE_REFLECT_MULTIPLIER = 0.25F",
  "LEGACY_MULTI_USE_REFLECT_MULTIPLIER = 0.5F",
  "LEGACY_EXPLOSION_RADIUS = 3.0F",
  "LEGACY_SPREAD_RADIUS = 10.0F",
  "LEGACY_SPREAD_DURATION = 600",
  "target.getMaxHealth() > LEGACY_MAX_HP_ALLOWED",
  "target.hasEffect(ModEffects.THORNSHADE_THORNS)",
  "isInfiniteDuration(effect)",
  "data.getInt(TAG_USES)",
  "data.putLong(TAG_COOLDOWN_UNTIL, gameTime + effect.getDuration() / 2L)",
  "MobEffectEvent.Applicable.Result.DO_NOT_APPLY",
  "attacker.damageSources().thorns(victim)",
  "attacker.hurt(",
  "data.putInt(TAG_EXPLODE_DELAY, LEGACY_EXPLODE_DELAY_TICKS)",
  "SoundEvents.WITHER_HURT",
  "Blocks.REDSTONE_BLOCK.defaultBlockState()",
  "ParticleTypes.CLOUD",
  "ParticleTypes.WITCH",
  "Level.ExplosionInteraction.NONE",
  "host.damageSources().genericKill()",
  "Float.MAX_VALUE",
  "new MobEffectInstance(ModEffects.THORNSHADE_THORNS, LEGACY_SPREAD_DURATION, 0, false, true)",
  "Math.max(LEGACY_MAX_APPLICATIONS_BEFORE_EXPLOSION, data.getInt(TAG_USES))",
  "ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, \"thornshade_self_destruct\")",
  "player.server.getAdvancements().get(THORNSHADE_SELF_DESTRUCT)",
  "player.getAdvancements().award(advancement, criterion)"
]) {
  if (!events.includes(marker)) throw new Error(`ThornshadeThornsEvents missing legacy handler marker: ${marker}`);
}

for (const forbidden of [
  "Level.ExplosionInteraction.BLOCK",
  "Level.ExplosionInteraction.TNT",
  "target.addEffect(new MobEffectInstance(ModEffects.THORNSHADE_THORNS, Integer.MAX_VALUE"
]) {
  if (events.includes(forbidden)) throw new Error(`ThornshadeThornsEvents contains non-legacy marker: ${forbidden}`);
}

const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (!lang["mob_effect.srparasites:thornshade_thorns"]) throw new Error("en_us.json missing Thornshade mob effect translation");
if (!lang["item.srparasites.thornshade_decanter"]) throw new Error("en_us.json missing Thornshade Decanter translation");
if (!lang["advancement.srparasites.thornshade_self_destruct.title"]) throw new Error("en_us.json missing Thornshade advancement title");

const advancement = readJson("src/main/resources/data/srparasites/advancement/thornshade_self_destruct.json");
if (advancement.criteria?.exploded?.trigger !== "minecraft:impossible") throw new Error("Thornshade advancement must preserve the legacy exploded impossible criterion");
if (advancement.display?.icon?.id !== "srparasites:thornshade_decanter") throw new Error("Thornshade advancement icon should be the legacy decanter item");
if (advancement.display?.frame !== "challenge") throw new Error("Thornshade advancement should preserve the legacy challenge frame");
if (advancement.display?.hidden !== true) throw new Error("Thornshade advancement should remain hidden");

const model = readJson("src/main/resources/assets/srparasites/models/item/thornshade_decanter.json");
if (model.textures?.layer0 !== "srparasites:items/parasitebush_decanter") {
  throw new Error("Thornshade Decanter model no longer points at the legacy decanter texture");
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
for (const marker of [
  "THORNSHADE_THORNS_E",
  "0x421F7E",
  "PotionThornshadeThorns",
  "ThornshadeThornsHandler",
  "ItemThornshadeDecanter",
  "srparasites:thornshade_thorns",
  "srparasites:thornshade_decanter",
  "srp_thornshade_thorns",
  "Uses",
  "CooldownUntil",
  "ExplodeDelay",
  "HasExplodedOnce",
  "120.0",
  "0.25",
  "0.5",
  "3.0",
  "10.0",
  "600"
]) {
  if (!audit.includes(marker)) throw new Error(`Port audit missing Thornshade marker: ${marker}`);
}

console.log("Thornshade Thorns port verifier passed.");
