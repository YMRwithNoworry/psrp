const fs = require("node:fs");
const path = require("node:path");

const root = path.resolve(__dirname, "..");
const read = (file) => fs.readFileSync(path.join(root, file), "utf8");
const exists = (file) => fs.existsSync(path.join(root, file));
const readJson = (file) => JSON.parse(read(file));

const required = [
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/crude/InhooMEntity.java",
  "src/main/java/com/dhanantry/scapeandrunparasites/client/InhooMRenderer.java",
  "src/main/resources/assets/srparasites/geo/entity/inhoo_m.geo.json",
  "src/main/resources/assets/srparasites/animations/entity/inhoo_m.animation.json",
  "src/main/resources/assets/srparasites/textures/entity/monster/inhoom.png",
  "src/main/resources/assets/srparasites/models/item/itemmobspawner_inhoom.json",
  "src/main/resources/assets/srparasites/textures/items/itemmobspawner_inhoom.png",
  "docs/SRPARASITES_1_10_6_PORT_AUDIT.md"
];
for (const file of required) if (!exists(file)) throw new Error(`Missing InhooM port file/resource: ${file}`);

const entities = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntities.java");
for (const marker of [
  "import com.dhanantry.scapeandrunparasites.entity.monster.crude.InhooMEntity;",
  'INHOOM = ENTITIES.register("incompleteform_medium"',
  "EntityType.Builder.of(InhooMEntity::new, MobCategory.MONSTER)",
  ".sized(InhooMEntity.LEGACY_WIDTH, InhooMEntity.LEGACY_HEIGHT)",
  ".eyeHeight(InhooMEntity.LEGACY_EYE_HEIGHT)",
  '.build(SRPMain.MODID + ":incompleteform_medium")'
]) if (!entities.includes(marker)) throw new Error(`ModEntities missing InhooM marker: ${marker}`);

const entityEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModEntityEvents.java");
for (const marker of ["import com.dhanantry.scapeandrunparasites.entity.monster.crude.InhooMEntity;", "ModEntities.INHOOM.get()", "InhooMEntity.createAttributes().build()"])
  if (!entityEvents.includes(marker)) throw new Error(`ModEntityEvents missing InhooM marker: ${marker}`);

const clientEvents = read("src/main/java/com/dhanantry/scapeandrunparasites/client/ModClientEvents.java");
if (!clientEvents.includes("event.registerEntityRenderer(ModEntities.INHOOM.get(), InhooMRenderer::new)")) {
  throw new Error("ModClientEvents missing InhooM renderer registration");
}

const items = read("src/main/java/com/dhanantry/scapeandrunparasites/init/ModItems.java");
for (const marker of ["ITEMMOBSPAWNER_INHOOM", '"itemmobspawner_inhoom"', "DeferredSpawnEggItem", "ModEntities.INHOOM"])
  if (!items.includes(marker)) throw new Error(`ModItems missing InhooM spawn egg marker: ${marker}`);
if (items.includes('ITEMMOBSPAWNER_INHOOM = legacyItem("itemmobspawner_inhoom"')) {
  throw new Error("InhooM spawn egg must not remain a legacy placeholder item");
}

const entity = read("src/main/java/com/dhanantry/scapeandrunparasites/entity/monster/crude/InhooMEntity.java");
for (const marker of [
  "LEGACY_PARASITE_ID = 43",
  'LEGACY_MODEL_ANIMATION_NAME = "animation.inhoo_m.func_78087_a"',
  "LEGACY_HEALTH = 14.0D",
  "LEGACY_ARMOR = 0.0D",
  "LEGACY_ATTACK_DAMAGE = 11.0D",
  "LEGACY_KNOCKBACK_RESISTANCE = 0.3D",
  "LEGACY_MOVEMENT_SPEED = 0.15D",
  "LEGACY_FOLLOW_RANGE = 32.0D",
  "LEGACY_MELEE_SPEED = 1.3D",
  "LEGACY_WIDTH = 0.6F",
  "LEGACY_HEIGHT = 1.95F",
  "LEGACY_EYE_HEIGHT = 1.2F",
  "LEGACY_TYPE = 11",
  "LEGACY_CAN_MOD_RENDER = 0",
  "LEGACY_DISLODGEMENT_SPAWN_TICK = 10",
  'LEGACY_DISLODGEMENT_NBT = "dsltwenty"',
  "EntityDataSerializers.INT",
  "HurtByTargetGoal",
  "FloatGoal",
  "MeleeAttackGoal",
  "NearestAttackableTargetGoal",
  "!(target instanceof WaterAnimal)",
  "!(target instanceof Animal)",
  "!(target instanceof Villager)",
  "ModSounds.MOB_SILENCE",
  "ModSounds.INHOOM_GROWL",
  "ModSounds.INHOOM_HURT",
  "ModSounds.INHOOM_DEATH",
  "ModSounds.LITE_FLESH_SLIDE",
  "tag.putBoolean(LEGACY_DISLODGEMENT_NBT, this.disloNumberTwenty)",
  "this.disloNumberTwenty = tag.getBoolean(LEGACY_DISLODGEMENT_NBT)",
  "tickLegacyDislodgementBody",
  "ModEntities.FERSHEEP.get()",
  "ModEntities.FERWOLF.get()",
  "ModEntities.FERHORSE.get()",
  "SrpDislodgementBodySpawned"
]) if (!entity.includes(marker)) throw new Error(`InhooMEntity missing legacy behavior marker: ${marker}`);

const renderer = read("src/main/java/com/dhanantry/scapeandrunparasites/client/InhooMRenderer.java");
for (const marker of [
  "extends GeoEntityRenderer<InhooMEntity>",
  "geo/entity/inhoo_m.geo.json",
  "animations/entity/inhoo_m.animation.json",
  "textures/entity/monster/inhoom.png",
  "InhooMEntity.LEGACY_SHADOW_RADIUS",
  "entity.getSelfeFlashIntensity2()"
]) if (!renderer.includes(marker)) throw new Error(`InhooMRenderer missing marker: ${marker}`);
if (renderer.includes("snowvariants/test.png")) throw new Error("InhooM renderer must not reference missing frozen texture snowvariants/test.png");

const enUs = readJson("src/main/resources/assets/srparasites/lang/en_us.json");
if (enUs["entity.srparasites.incompleteform_medium"] !== "Incomplete Form" || enUs["entity.srparasites.incompleteform_medium.name"] !== "Incomplete Form") {
  throw new Error("en_us.json missing InhooM entity translation keys");
}
if (enUs["item.srparasites.itemmobspawner_inhoom"] !== "Spawn Medium Incomplete Form") {
  throw new Error("en_us.json missing InhooM spawn egg translation key");
}

const geo = readJson("src/main/resources/assets/srparasites/geo/entity/inhoo_m.geo.json");
if (geo.format_version !== "1.12.0") throw new Error(`Unexpected InhooM geo format_version: ${geo.format_version}`);
const geometry = geo["minecraft:geometry"] && geo["minecraft:geometry"][0];
if (!geometry) throw new Error("InhooM geo missing minecraft:geometry entry");
if (geometry.description.texture_width !== 128 || geometry.description.texture_height !== 55) {
  throw new Error(`InhooM geo must preserve 128x55 texture size, found ${geometry.description.texture_width}x${geometry.description.texture_height}`);
}
const bones = geometry.bones || [];
if (bones.length !== 157) throw new Error(`InhooM geo must preserve 157 converted ModelInhooM bones, found ${bones.length}`);

const animation = readJson("src/main/resources/assets/srparasites/animations/entity/inhoo_m.animation.json");
const animationNames = Object.keys(animation.animations || {}).sort();
const expectedAnimations = ["animation.inhoo_m.func_78087_a", "animation.inhoo_m.setRotationAnglesCosmical"];
if (JSON.stringify(animationNames) !== JSON.stringify(expectedAnimations)) {
  throw new Error(`InhooM animation names changed: ${JSON.stringify(animationNames)}`);
}
for (const [name, data] of Object.entries(animation.animations)) {
  if (data.loop !== true) throw new Error(`${name} must remain looped like legacy model-code animation`);
  const bonesWithChannels = Object.keys(data.bones || {});
  if (bonesWithChannels.length !== 28) throw new Error(`${name} must keep 28 animated bones, found ${bonesWithChannels.length}`);
  const times = new Set();
  const modes = new Set();
  for (const channels of Object.values(data.bones || {})) {
    for (const keys of Object.values(channels)) {
      for (const [time, value] of Object.entries(keys)) {
        times.add(Number(time));
        if (value && typeof value === "object" && value.lerp_mode) modes.add(value.lerp_mode);
      }
    }
  }
  if (times.size !== 121) throw new Error(`${name} must keep 121 sampled 60fps key times, found ${times.size}`);
  if (!modes.has("catmullrom")) throw new Error(`${name} must keep catmullrom interpolation`);
}

const audit = read("docs/SRPARASITES_1_10_6_PORT_AUDIT.md");
for (const marker of [
  "EntityInhooM",
  "ModelInhooM",
  "RenderInhooM",
  "parasite id `43`",
  "incompleteform_medium",
  "itemmobspawner_inhoom",
  "dsltwenty",
  "INHOOM_GROWL",
  "LITE_FLESH_SLIDE",
  "157` bones",
  "snowvariants/test.png"
]) if (!audit.includes(marker)) throw new Error(`Audit missing InhooM marker: ${marker}`);

console.log("InhooM / Medium Incomplete Form crude entity port verifier passed.");
