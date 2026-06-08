const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/client/IndeafClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/potion/IndeafMobEffect.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];

for (const file of required) {
  if (!exists(file)) throw new Error(`Missing Indeaf client port file: ${file}`);
}

const serverEffect = read("src/main/java/com/dhanantry/scapeandrunparasites/potion/IndeafMobEffect.java");
for (const marker of [
  "LEGACY_COLOR = 0xFFDD00",
  "entity.setDeltaMovement(0.0D, entity.getDeltaMovement().y, 0.0D)",
  "entity.xxa = 0.0F",
  "entity.zza = 0.0F",
  "return true"
]) {
  if (!serverEffect.includes(marker)) throw new Error(`IndeafMobEffect missing server-side marker: ${marker}`);
}

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/IndeafClientEvents.java");
for (const marker of [
  "@EventBusSubscriber(modid = SRPMain.MODID, value = Dist.CLIENT)",
  "value = Dist.CLIENT",
  "ClientTickEvent.Pre",
  "ClientTickEvent.Post",
  "Minecraft.getInstance()",
  "minecraft.player.hasEffect(ModEffects.INDEAF)",
  "minecraft.options.keyUp",
  "minecraft.options.keyDown",
  "minecraft.options.keyLeft",
  "minecraft.options.keyRight",
  "minecraft.options.keyJump",
  "key.setDown(false)"
]) {
  if (!clientEvents.includes(marker)) throw new Error(`IndeafClientEvents missing client input marker: ${marker}`);
}
for (const forbidden of [
  "bus =",
  "EventBusSubscriber.Bus",
  "minecraft.options.keyShift",
  "minecraft.options.keySprint",
  "minecraft.options.keyAttack",
  "minecraft.options.keyUse",
  "KeyMapping.releaseAll()"
]) {
  if (clientEvents.includes(forbidden)) {
    throw new Error(`Indeaf client lock should not release non-legacy input marker: ${forbidden}`);
  }
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md").replace(/\s+/g, " ");
for (const marker of [
  "SRPEventHandlerBus",
  "onClientTick",
  "INDEAF_E",
  "forward",
  "back",
  "left",
  "right",
  "jump",
  "ClientTickEvent.Pre",
  "ClientTickEvent.Post",
  "keyUp",
  "keyDown",
  "keyLeft",
  "keyRight",
  "keyJump",
  "key.setDown(false)",
  "Ported the old Indeaf client input lock"
]) {
  if (!audit.includes(marker)) throw new Error(`Audit missing Indeaf client marker: ${marker}`);
}

console.log("Indeaf client port verifier passed.");
