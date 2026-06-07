# SRParasites 1.10.6 NeoForge 1.21.1 Port Audit

This repository started as a blank NeoForge MDK. The source of truth for this
slice is `杂物/[逃逸：寄生体] SRParasites-1.10.6.jar`.

## Legacy Evidence Inspected

- `com.dhanantry.scapeandrunparasites.SRPMain`: legacy mod entrypoint and
  `srparasites` namespace.
- `com.dhanantry.scapeandrunparasites.init.SRPItems`: item registry names for
  living/sentient weapons, living/sentient armor, hijacked iron gear, drops, and
  basic parasite materials.
- `com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase`: melee
  living weapons store `srpkills`, use configured attack damage, attack speed,
  and optional reach.
- `com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase`: living
  armor stores `srphits` and upgrades to sentient variants after the configured
  damage threshold.
- `com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase`: living
  bows use configurable damage, bonus, and cap values.
- `com.dhanantry.scapeandrunparasites.util.config.SRPConfig`: defaults for
  living weapon durability, living-to-sentient thresholds, weapon damage/range,
  bow damage/cap, and living/sentient armor point settings.
- `com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog`: Grunt /
  Flog entity size, legacy parasite ID `60`, climb flag, swim and water leap
  goals, AOE melee attack, skill leap, evade dash, variant skin selection, and
  pure-tier attributes.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.pure.RenderFlog`:
  Flog texture paths and skin-to-texture mapping.
- `com.dhanantry.scapeandrunparasites.client.model.entity.pure.ModelFlog`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved method-derived animation names
  `animation.flog.func_78087_a` and
  `animation.flog.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Flog
  attributes before multipliers: `20` health, `7` armor, `13` attack damage,
  `0.4` knockback resistance, and movement speed `0.274172325`.

## Implemented In This Slice

- Replaced the MDK example `psrp` namespace with the legacy `srparasites`
  namespace.
- Migrated legacy item models, item textures, and `.lang` files into 1.21.1
  resource locations.
- Registered the first real gameplay-facing item surface:
  - parasite drops and materials used by the legacy gear family,
  - hijacked iron tools and armor,
  - living and sentient melee weapons,
  - living and sentient greatbows,
  - living and sentient armor.
- Added NeoForge config values that mirror the legacy `SRPConfig` gear defaults.
- Added runtime attribute replacement through `ItemAttributeModifierEvent`.
- Added `srpkills` and `srphits` persistence through 1.21 data components.
- Added living-to-sentient upgrade hooks for weapons and armor.
- Added the first evidence-backed parasite entity slice:
  - registered the Grunt/Flog entity under the legacy `grunt` visible entity id,
  - registered the legacy `itemmobspawner_flog` spawn egg,
  - preserved size `0.7666 x 1.95`, eye height `1.73`, parasite ID `60`, base
    health/armor/damage/speed/follow-range/knockback attributes,
  - implemented climbing with a synced flag and wall-climber navigation,
  - implemented water leap, AOE melee around the target, skill leap, evade dash,
    and skin 5/6/7 texture variants,
  - wired a GeckoLib client renderer to the converted legacy `ModelFlog`
    geometry, Java-authored animation resource, and four legacy Flog texture
    resources.

## Explicit Gaps

This is not a complete mod port yet. The following systems still require their
own evidence-backed slices:

- the remaining parasite entities, AI goals, attributes, and animations,
- exact `SRPPotions.VIRA_E` and `SRPPotions.BLEED_E` effect behavior; skin 5/6
  attacks are currently mapped to short vanilla Hunger/Weakness effects so the
  combat hook is live until the custom status system is migrated,
- block registry and legacy block behavior,
- world evolution and phase systems,
- potions/effects and adaptation systems,
- bestiary GUI and networking,
- recipes, loot tables, advancements, and data generation,
- sounds and entity renderers.

Do not treat a build passing as full restoration. Future slices should continue
from legacy bytecode evidence and add verifier markers before broad claims.
