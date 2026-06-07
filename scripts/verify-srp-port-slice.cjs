const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/SRPMain.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
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

const itemData = fs.readFileSync(path.join(root, "src/main/java/com/dhanantry/scapeandrunparasites/item/SrpItemData.java"), "utf8");
for (const marker of ['KILLS = "srpkills"', 'HITS = "srphits"', "DataComponents.CUSTOM_DATA"]) {
  if (!itemData.includes(marker)) throw new Error(`SrpItemData is missing persistence marker ${marker}`);
}

console.log("SRP port slice verifier passed.");
