const fs = require("node:fs");
const path = require("node:path");
const os = require("node:os");
const childProcess = require("node:child_process");

const root = path.resolve(__dirname, "..");
const jar = process.argv[2] ? path.resolve(process.argv[2]) : path.join(root, "杂物", "[逃逸：寄生体] SRParasites-1.10.6.jar");
const outAssets = path.join(root, "src", "main", "resources", "assets", "srparasites");
const tmp = fs.mkdtempSync(path.join(os.tmpdir(), "srp-assets-"));

if (!fs.existsSync(jar)) {
  throw new Error(`Legacy jar not found: ${jar}`);
}

function run(command, args, cwd) {
  childProcess.execFileSync(command, args, { cwd, stdio: "inherit" });
}

function copyDir(src, dest, transformText) {
  if (!fs.existsSync(src)) return;
  for (const entry of fs.readdirSync(src, { withFileTypes: true })) {
    const from = path.join(src, entry.name);
    const to = path.join(dest, entry.name);
    if (entry.isDirectory()) {
      copyDir(from, to, transformText);
      continue;
    }
    fs.mkdirSync(path.dirname(to), { recursive: true });
    if (transformText && entry.name.endsWith(".json")) {
      let text = fs.readFileSync(from, "utf8");
      text = transformText(text);
      fs.writeFileSync(to, text, "utf8");
    } else {
      fs.copyFileSync(from, to);
    }
  }
}

function readLang(file) {
  const result = {};
  const text = fs.readFileSync(file, "utf8").replace(/^\uFEFF/, "");
  for (const rawLine of text.split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith("#") || !line.includes("=")) continue;
    const split = line.indexOf("=");
    const key = line.slice(0, split).trim();
    const value = line.slice(split + 1).trim();
    result[key] = value;
    if ((key.startsWith("item.srparasites.") || key.startsWith("block.srparasites.") || key.startsWith("entity.srparasites.")) && key.endsWith(".name")) {
      result[key.slice(0, -5)] = value;
    }
  }
  return result;
}

function writeLangs(srcDir) {
  const langOut = path.join(outAssets, "lang");
  fs.mkdirSync(langOut, { recursive: true });
  for (const file of fs.readdirSync(srcDir)) {
    if (!file.endsWith(".lang")) continue;
    const lang = readLang(path.join(srcDir, file));
    lang["itemGroup.srparasites"] = lang["itemGroup.srparasites"] || "Scape and Run: Parasites";
    lang["tooltip.srparasites.srpkills"] = "Assimilated life: %s / %s";
    lang["tooltip.srparasites.srphits"] = "Assimilated damage: %s / %s";
    lang["tooltip.srparasites.reach"] = "Legacy reach: +%s";
    const outName = file.replace(/\.lang$/, ".json").toLowerCase();
    fs.writeFileSync(path.join(langOut, outName), `${JSON.stringify(lang, null, 2)}\n`, "utf8");
  }
}

fs.rmSync(outAssets, { recursive: true, force: true });
fs.mkdirSync(outAssets, { recursive: true });
run("jar", ["xf", jar, "assets/srparasites/lang", "assets/srparasites/models/item", "assets/srparasites/textures"], tmp);

const legacyAssets = path.join(tmp, "assets", "srparasites");
copyDir(path.join(legacyAssets, "models", "item"), path.join(outAssets, "models", "item"), text =>
  text.replace(/SRParasites:/g, "srparasites:").replace(/srparasites:items\//g, "srparasites:items/")
);
copyDir(path.join(legacyAssets, "textures"), path.join(outAssets, "textures"));
const venkrolSv = path.join(outAssets, "textures", "items", "itemmobspawner_venkrolsv.png");
const venkrolSiv = path.join(outAssets, "textures", "items", "itemmobspawner_venkrolsiv.png");
if (!fs.existsSync(venkrolSv) && fs.existsSync(venkrolSiv)) {
  fs.copyFileSync(venkrolSiv, venkrolSv);
}
writeLangs(path.join(legacyAssets, "lang"));
fs.rmSync(tmp, { recursive: true, force: true });

fs.writeFileSync(
  path.join(root, "src", "main", "resources", "pack.mcmeta"),
  JSON.stringify({ pack: { pack_format: 34, description: "Scape and Run: Parasites 1.10.6 assets migrated for NeoForge 1.21.1" } }, null, 2) + "\n",
  "utf8"
);

console.log(`Migrated legacy item models, item textures, and lang files from ${jar}`);
