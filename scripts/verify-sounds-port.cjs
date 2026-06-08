const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const readJson = (file) => JSON.parse(read(file));

const sounds = readJson("src/main/resources/assets/srparasites/sounds.json");
const source = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModSounds.java");
const main = read("src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java");

function constantName(key) {
  let name = key.replace(/[^A-Za-z0-9]+/g, "_").replace(/^_+|_+$/g, "").toUpperCase();
  if (/^[0-9]/.test(name)) name = `SOUND_${name}`;
  return name;
}

const keys = Object.keys(sounds);
if (keys.length !== 413) throw new Error(`Expected 413 legacy sound events, found ${keys.length}`);

for (const marker of [
  "DeferredRegister.create(Registries.SOUND_EVENT, SRPMain.MODID)",
  "SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, name))",
  "public static void register(IEventBus eventBus)"
]) {
  if (!source.includes(marker)) throw new Error(`ModSounds missing registration marker: ${marker}`);
}

for (const key of keys) {
  const constant = constantName(key);
  if (!source.includes(`public static final DeferredHolder<SoundEvent, SoundEvent> ${constant} = register("${key}");`)) {
    throw new Error(`ModSounds missing sound event ${key} as ${constant}`);
  }
}

for (const key of [
  "compendium.unlock.item",
  "orb.attack.summon",
  "orb.attack.idle",
  "kirin.growl",
  "kirin.hurt",
  "kirin.death",
  "monarch.growl",
  "monarch.hurt",
  "monarch.death",
  "kirin.shoot"
]) {
  if (!sounds[key]) throw new Error(`sounds.json missing legacy key: ${key}`);
  if (!source.includes(`register("${key}")`)) throw new Error(`ModSounds missing legacy key: ${key}`);
}

if (!main.includes("import com.dhanantry.scapeandrunparasites.init.ModSounds;")) {
  throw new Error("SRPMain does not import ModSounds");
}
if (!main.includes("ModSounds.register(modEventBus);")) {
  throw new Error("SRPMain does not register ModSounds on the mod event bus");
}

console.log("Sound event port verifier passed.");
