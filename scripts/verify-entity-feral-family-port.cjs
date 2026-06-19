const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const ferals = [
  {
    name: "FerSheep",
    id: "fer_sheep",
    holder: "FERSHEEP",
    item: "itemmobspawner_fersheep",
    texture: "fersheep.png",
    geo: "inf_sheep.geo.json",
    animation: "inf_sheep.animation.json",
    legacyClass: "EntityFerSheep",
    parasiteId: "98",
    health: "13D",
    armor: "1.3D",
    damage: "6D",
    width: "0.9F",
    height: "1.3F",
    eyeHeight: "0.8F",
    ambient: "INFECTEDSHEEP_GROWL",
    hurt: "INFECTEDSHEEP_HURT",
    death: "INFECTEDSHEEP_DEATH",
    step: "SoundEvents.SHEEP_STEP",
    modelAnimation: "animation.inf_sheep.func_78087_a",
    cosmicalAnimation: "animation.inf_sheep.setRotationAnglesCosmical",
    lang: "Feral Sheep"
  },
  {
    name: "FerWolf",
    id: "fer_wolf",
    holder: "FERWOLF",
    item: "itemmobspawner_ferwolf",
    texture: "ferwolf.png",
    geo: "inf_wolf.geo.json",
    animation: "inf_wolf.animation.json",
    legacyClass: "EntityFerWolf",
    parasiteId: "300",
    health: "10D",
    armor: "0.5D",
    damage: "10.5D",
    width: "0.6F",
    height: "1.95F",
    eyeHeight: "1.4F",
    ambient: "INFECTEDWOLF_GROWL",
    hurt: "INFECTEDWOLF_HURT",
    death: "INFECTEDWOLF_DEATH",
    step: "SoundEvents.WOLF_STEP",
    modelAnimation: "animation.inf_wolf.func_78087_a",
    cosmicalAnimation: "animation.inf_wolf.setRotationAnglesCosmical",
    lang: "Feral Wolf"
  },
  {
    name: "FerHorse",
    id: "fer_horse",
    holder: "FERHORSE",
    item: "itemmobspawner_ferhorse",
    texture: "ferhorse.png",
    geo: "inf_horse.geo.json",
    animation: "inf_horse.animation.json",
    legacyClass: "EntityFerHorse",
    parasiteId: "95",
    health: "24D",
    armor: "0.5D",
    damage: "7.5D",
    width: "1.3964844F",
    height: "1.75F",
    eyeHeight: "1.3F",
    ambient: "INFECTEDHORSE_GROWL",
    hurt: "INFECTEDHORSE_HURT",
    death: "INFECTEDHORSE_DEATH",
    step: "SoundEvents.HORSE_STEP",
    modelAnimation: "animation.inf_horse.func_78087_a",
    cosmicalAnimation: "animation.inf_horse.setRotationAnglesCosmical",
    lang: "Feral Horse"
  }
];

const baseRequired = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];
for (const file of baseRequired) if (!exists(file)) throw new Error(`Missing feral verifier dependency: ${file}`);

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
const zhCn = readJson("src/main/resources/assets/srparasites/lang/zh_cn.json");

for (const feral of ferals) {
  const entityFile = `src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/feral/${feral.name}Entity.java`;
  const rendererFile = `src/main/java/com/dhanantry/scapeandrunparasites/client/${feral.name}Renderer.java`;
  const required = [
    entityFile,
    rendererFile,
    `src/main/resources/assets/srparasites/textures/entity/monster/${feral.texture}`,
    `src/main/resources/assets/srparasites/geo/entity/${feral.geo}`,
    `src/main/resources/assets/srparasites/animations/entity/${feral.animation}`,
    `src/main/resources/assets/srparasites/models/item/${feral.item}.json`,
    `src/main/resources/assets/srparasites/textures/items/${feral.item}.png`
  ];
  for (const file of required) if (!exists(file)) throw new Error(`Missing ${feral.name} resource/code file: ${file}`);

  for (const marker of [
    `import com.dhanantry.scapeandrunparasites.entity.monster.feral.${feral.name}Entity;`,
    `${feral.holder} = ENTITIES.register("${feral.id}"`,
    `EntityType.Builder.of(${feral.name}Entity::new, MobCategory.MONSTER)`,
    `.sized(${feral.name}Entity.LEGACY_WIDTH, ${feral.name}Entity.LEGACY_HEIGHT)`,
    `.eyeHeight(${feral.name}Entity.LEGACY_EYE_HEIGHT)`,
    `.build(SRPMain.MODID + ":${feral.id}")`
  ]) if (!entities.includes(marker)) throw new Error(`ModEntities missing ${feral.name} marker: ${marker}`);

  for (const marker of [`${feral.name}Entity.createAttributes().build()`, `ModEntities.${feral.holder}.get()`]) {
    if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing ${feral.name} attribute marker: ${marker}`);
  }

  for (const marker of [
    `ITEMMOBSPAWNER_${feral.holder.replace("FER", "FER")}`,
    `"${feral.item}"`,
    "DeferredSpawnEggItem",
    `ModEntities.${feral.holder}`
  ]) if (!items.includes(marker)) throw new Error(`ModItems missing ${feral.name} spawn egg marker: ${marker}`);
  if (items.includes(`ITEMMOBSPAWNER_${feral.holder.replace("FER", "FER")} = legacyItem("${feral.item}"`)) {
    throw new Error(`${feral.name} spawn egg must not remain a legacy placeholder item`);
  }

  if (!clientEvents.includes(`event.registerEntityRenderer(ModEntities.${feral.holder}.get(), ${feral.name}Renderer::new)`)) {
    throw new Error(`ModClientEvents missing ${feral.name} renderer registration`);
  }

  const entity = read(entityFile);
  for (const marker of [
    `LEGACY_PARASITE_ID = ${feral.parasiteId}`,
    `LEGACY_MODEL_ANIMATION_NAME = "${feral.modelAnimation}"`,
    `LEGACY_COSMICAL_ANIMATION_NAME = "${feral.cosmicalAnimation}"`,
    `LEGACY_HEALTH = ${feral.health}`,
    `LEGACY_ARMOR = ${feral.armor}`,
    `LEGACY_ATTACK_DAMAGE = ${feral.damage}`,
    `LEGACY_WIDTH = ${feral.width}`,
    `LEGACY_HEIGHT = ${feral.height}`,
    `LEGACY_EYE_HEIGHT = ${feral.eyeHeight}`,
    "LEGACY_TYPE = 11",
    "LEGACY_CAN_MOD_RENDER = 1",
    "LEGACY_SWIM_DIVE_SPEED = 0.08D",
    "LEGACY_MELEE_SPEED = 1.5D",
    "LEGACY_WATER_LEAP_POWER = 0.7F",
    "LEGACY_WATER_LEAP_SPEED = 1.5D",
    "LEGACY_WATER_LEAP_STATUS = 3",
    "LEGACY_WATER_LEAP_INTERVAL = 20",
    "LEGACY_FOLLOWER_MODE = 1",
    "LEGACY_FOLLOWER_RANGE = 16",
    "HurtByTargetGoal",
    "FloatGoal",
    "MeleeAttackGoal",
    "RandomLookAroundGoal",
    "NearestAttackableTargetGoal",
    `ModSounds.${feral.ambient}`,
    `ModSounds.${feral.hurt}`,
    `ModSounds.${feral.death}`,
    feral.step
  ]) if (!entity.includes(marker)) throw new Error(`${feral.name}Entity missing legacy behavior marker: ${marker}`);

  const renderer = read(rendererFile);
  for (const marker of [
    `extends GeoEntityRenderer<${feral.name}Entity>`,
    `geo/entity/${feral.geo}`,
    `animations/entity/${feral.animation}`,
    `textures/entity/monster/${feral.texture}`,
    `${feral.name}Entity.LEGACY_SHADOW_RADIUS`,
    "entity.getSelfeFlashIntensity2()"
  ]) if (!renderer.includes(marker)) throw new Error(`${feral.name}Renderer missing renderer marker: ${marker}`);

  if (enUs[`entity.srparasites.${feral.id}`] !== feral.lang || enUs[`entity.srparasites.${feral.id}.name`] !== feral.lang) {
    throw new Error(`en_us.json missing ${feral.name} entity translation keys for ${feral.id}`);
  }
  if (!zhCn[`entity.srparasites.${feral.id}`] || !zhCn[`entity.srparasites.${feral.id}.name`]) {
    throw new Error(`zh_cn.json missing ${feral.name} entity translation keys for ${feral.id}`);
  }
  if (enUs[`item.srparasites.${feral.item}`] !== `Spawn ${feral.lang}`) {
    throw new Error(`en_us.json missing ${feral.name} spawn egg translation key`);
  }

  for (const marker of [
    feral.legacyClass,
    feral.name,
    feral.id,
    feral.item,
    `parasite id ${feral.parasiteId}`,
    feral.modelAnimation,
    feral.texture
  ]) if (!audit.includes(marker)) throw new Error(`Audit missing ${feral.name} marker: ${marker}`);
}

console.log("Feral Sheep/Wolf/Horse entity family verifier passed.");
