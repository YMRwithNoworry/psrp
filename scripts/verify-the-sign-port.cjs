const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModPotions.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/TheSignMobEffect.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/SignEffectEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/item/TheSignCharmItem.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/SrpParasiteMob.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/FlogEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/OrchEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/derived/KirinEntity.java",
  "src/main/resources/assets/srparasites/models/item/the_sign_charm.json",
  "src/main/resources/assets/srparasites/textures/items/the_sign.png",
  "src/main/resources/assets/srparasites/textures/gui/potion_the_sign.png",
  "src/main/resources/assets/srparasites/lang/en_us.json",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing The Sign port file/resource: ${file}`);
}

const effects = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEffects.java");
for (const marker of [
  'EFFECTS.register("the_sign"',
  "DeferredHolder<MobEffect, TheSignMobEffect> THE_SIGN",
  "new TheSignMobEffect(MobEffectCategory.BENEFICIAL, TheSignMobEffect.LEGACY_COLOR)"
]) {
  if (!effects.includes(marker)) throw new Error(`ModEffects missing The Sign marker: ${marker}`);
}

const effect = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/TheSignMobEffect.java");
for (const marker of [
  "extends SrpMobEffect",
  "LEGACY_COLOR = 0x88E1FF"
]) {
  if (!effect.includes(marker)) throw new Error(`TheSignMobEffect missing legacy marker: ${marker}`);
}
for (const forbidden of ["applyEffectTick", "shouldApplyEffectTickThisTick"]) {
  if (effect.includes(forbidden)) throw new Error(`TheSignMobEffect should preserve the old no-tick behavior, but contains: ${forbidden}`);
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of [
  'register("the_sign_charm"',
  "DeferredItem<TheSignCharmItem> THE_SIGN_CHARM",
  "new TheSignCharmItem(new Item.Properties().stacksTo(TheSignCharmItem.LEGACY_STACK_SIZE))"
]) {
  if (!items.includes(marker)) throw new Error(`ModItems missing The Sign Charm marker: ${marker}`);
}

const charm = read("src/main/java/com/dhanantry/scapeandrunparasites/item/TheSignCharmItem.java");
for (const marker of [
  "extends Item",
  "LEGACY_STACK_SIZE = 1",
  "appendHoverText",
  'Component.translatable("tooltip.srparasites.the_sign_charm.red").withStyle(ChatFormatting.RED)',
  'Component.translatable("tooltip.srparasites.the_sign_charm.white").withStyle(ChatFormatting.WHITE)',
  'Component.translatable("tooltip.srparasites.the_sign_charm.gray").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)'
]) {
  if (!charm.includes(marker)) throw new Error(`TheSignCharmItem missing legacy marker: ${marker}`);
}

const events = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/SignEffectEvents.java");
for (const marker of [
  "LEGACY_DURATION_TICKS = 40",
  "PlayerTickEvent.Post",
  "Player player = event.getEntity()",
  "player.level().isClientSide",
  "hasSignCharmAnywhere(player)",
  "new MobEffectInstance(ModEffects.THE_SIGN, LEGACY_DURATION_TICKS, 0, false, false)",
  "Inventory inventory = player.getInventory()",
  "containsSignCharm(inventory.items)",
  "containsSignCharm(inventory.offhand)",
  "containsSignCharm(inventory.armor)",
  "stack.is(ModItems.THE_SIGN_CHARM.get())"
]) {
  if (!events.includes(marker)) throw new Error(`SignEffectEvents missing legacy handler marker: ${marker}`);
}

const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");
for (const marker of [
  "import com.dhanantry.scapeandrunparasites.potion.SignEffectEvents;",
  "NeoForge.EVENT_BUS.register(SignEffectEvents.class)"
]) {
  if (!main.includes(marker)) throw new Error(`SRPMain missing The Sign event marker: ${marker}`);
}

const potions = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModPotions.java");
for (const marker of [
  'register("the_sign", "srparasites:the_sign", ModEffects.THE_SIGN, LEGACY_DEFAULT_DURATION)',
  "LEGACY_DEFAULT_DURATION = 2400"
]) {
  if (!potions.includes(marker)) throw new Error(`ModPotions missing THE_SIGN_P marker: ${marker}`);
}

const baseMob = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/SrpParasiteMob.java");
for (const marker of [
  "public void setTarget(LivingEntity target)",
  "super.setTarget(isProtectedByTheSign(target) ? null : target)",
  "protected boolean canTargetLiving(LivingEntity target)",
  "protected boolean canHarmLiving(LivingEntity target)",
  "protected boolean isProtectedByTheSign(LivingEntity target)",
  "target instanceof Player && target.hasEffect(ModEffects.THE_SIGN)"
]) {
  if (!baseMob.includes(marker)) throw new Error(`SrpParasiteMob missing The Sign target-protection marker: ${marker}`);
}

const combatMarkers = [
  ["src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/FlogEntity.java", [
    "NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving)",
    "return canHarmLiving(candidate) && this.hasLineOfSight(candidate)"
  ]],
  ["src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/pure/OrchEntity.java", [
    "NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving)",
    "NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving)",
    "return canHarmLiving(candidate) && this.hasLineOfSight(candidate)"
  ]],
  ["src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/derived/KirinEntity.java", [
    "NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving)",
    "NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving)",
    "return this.kirin.canHarmLiving(target)"
  ]]
];
for (const [file, markers] of combatMarkers) {
  const source = read(file);
  for (const marker of markers) {
    if (!source.includes(marker)) throw new Error(`${file} missing The Sign combat marker: ${marker}`);
  }
}

const lang = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
for (const key of [
  "mob_effect.srparasites:the_sign",
  "effect.srparasites.the_sign",
  "item.srparasites.the_sign_charm",
  "tooltip.srparasites.the_sign_charm.red",
  "tooltip.srparasites.the_sign_charm.white",
  "tooltip.srparasites.the_sign_charm.gray",
  "item.minecraft.splash_potion.effect.srparasites:the_sign",
  "item.minecraft.potion.effect.srparasites:the_sign",
  "item.minecraft.lingering_potion.effect.srparasites:the_sign",
  "item.minecraft.tipped_arrow.effect.srparasites:the_sign"
]) {
  if (!lang[key]) throw new Error(`en_us.json missing The Sign key: ${key}`);
}

const model = readJson("src/main/resources/assets/srparasites/models/item/the_sign_charm.json");
if (model.textures?.layer0 !== "srparasites:items/the_sign") {
  throw new Error("The Sign Charm item model no longer points at the legacy item texture");
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
for (const marker of [
  "PotionTheSign",
  "0x88E1FF",
  "textures/gui/the_sign.png",
  "ItemTheSignCharm",
  "srparasites:the_sign_charm",
  "SignEffectHandler",
  "THE_SIGN_E",
  "THE_SIGN_P",
  "srparasites:the_sign",
  "duration `2400`",
  "`40` ticks",
  "ambient `false`",
  "visible `false`",
  "EntityParasiteBase",
  "setAttackTarget",
  "Flog",
  "Orch",
  "Kirin"
]) {
  if (!audit.includes(marker)) throw new Error(`Port audit missing The Sign marker: ${marker}`);
}

console.log("The Sign port verifier passed.");
