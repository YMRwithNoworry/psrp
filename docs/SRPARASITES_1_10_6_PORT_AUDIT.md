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
  bow damage/cap, living/sentient armor point settings, and the empty default
  `stackablePotionsLimit` list.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers `BLEED_E` as
  `bleed` with color `0x5E0806`, registers `VIRA_E` as `viral` with color
  `0x136334`, and defines the old `applyStackPotion` amplifier/duration
  stacking rules.
- `com.dhanantry.scapeandrunparasites.potion.SRPEffectBase`: clears curative
  items and applies non-bleed effect ticks every `25 >> amplifier` ticks.
- `com.dhanantry.scapeandrunparasites.potion.PotionBleed`: server-side bleeding
  tick behavior, `DAMAGE_INDICATOR` particle, max-health-scaled damage,
  movement multiplier, and `bleedingDamage` / `bleedingDamageCap` config use.
- `com.dhanantry.scapeandrunparasites.event.SRPEventHandlerBus`: applies the
  `viralEnable` / `viralAmount` incoming damage multiplier while a victim has
  `VIRA_E`.
- `com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog`: Grunt /
  Flog entity size, legacy parasite ID `60`, climb flag, swim and water leap
  goals, AOE melee attack, skill leap, evade dash, variant skin selection, and
  pure-tier attributes. Skin `5` applies `VIRA_E` for 40 ticks at amplifier 0;
  skin `6` applies `BLEED_E` for 40 ticks at amplifier 0.
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
- `com.dhanantry.scapeandrunparasites.entity.monster.derived.EntityKirin`:
  Derived Kirin entity size `2.1271334 x 7.1`, eye height `5.7`, step height
  `1.0`, parasite ID `67`, floating movement, portal particles, and special
  summon hook for the legacy void orb.
- `com.dhanantry.scapeandrunparasites.entity.ai.EntityAIKirinBlink`: Kirin
  blink attack evidence: target must be outdoors, visible, and farther than
  distance squared `256`; charge lasts `60` ticks, cooldown is `200` ticks,
  blink spot search tries `64` positions at radius `1.5 + random * 22.5`, and
  post-blink life steal checks a `5` block radius, halves the first valid
  non-SRP victim's current health, and heals Kirin by the stolen amount.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.derived.RenderKirin`:
  legacy renderer uses shadow radius `1.3`, normal texture
  `srparasites:textures/entity/monster/kirin.png`, and cosmical texture
  `srparasites:textures/entity/monster/testb.png`.
- `com.dhanantry.scapeandrunparasites.client.model.entity.derived.ModelKirin`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved method-derived animation names
  `animation.kirin.func_78087_a` and
  `animation.kirin.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Kirin
  attributes before multipliers: `410` health, `30` armor, `155` attack damage,
  `1.0` knockback resistance, and movement speed `0.24`. The derived follow
  range config default is `80`.

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
- Added NeoForge config values for the migrated status-effect defaults:
  `bleedingDamage = 0.06`, `bleedingDamageCap = 100.0`, `viralEnable = true`,
  `viralAmount = 0.5`, and an empty `stackablePotionsLimit` list.
- Added runtime attribute replacement through `ItemAttributeModifierEvent`.
- Added `srpkills` and `srphits` persistence through 1.21 data components.
- Added living-to-sentient upgrade hooks for weapons and armor.
- Registered evidence-backed `srparasites:viral` and `srparasites:bleed` mob
  effects with legacy colors. Their shared base clears NeoForge effect cures and
  ports the old `applyStackPotion` amplifier/duration stacking behavior.
- Implemented core `bleed` behavior: server-only damage indicator particles,
  `25 >> amplifier` tick cadence, max-health-scaled damage, movement scaling,
  and the legacy damage cap.
- Implemented core `viral` behavior: incoming damage amplification based on
  effect amplifier and the `viralAmount` config.
- Added the first evidence-backed parasite entity slice:
  - registered the Grunt/Flog entity under the legacy `grunt` visible entity id,
  - registered the legacy `itemmobspawner_flog` spawn egg,
  - preserved size `0.7666 x 1.95`, eye height `1.73`, parasite ID `60`, base
    health/armor/damage/speed/follow-range/knockback attributes,
  - implemented climbing with a synced flag and wall-climber navigation,
  - implemented water leap, AOE melee around the target, skill leap, evade dash,
    and skin 5/6/7 texture variants,
  - wired skin 5 attacks to the migrated `viral` stack effect and skin 6 attacks
    to the migrated `bleed` stack effect,
  - wired a GeckoLib client renderer to the converted legacy `ModelFlog`
    geometry, Java-authored animation resource, and four legacy Flog texture
    resources.
- Added the first evidence-backed derived parasite entity slice:
  - registered Kirin under the legacy `kirin` visible entity id,
  - registered the legacy `itemmobspawner_kirin` spawn egg,
  - preserved size `2.1271334 x 7.1`, eye height `5.7`, step height `1.0`,
    parasite ID `67`, base health/armor/damage/speed/follow-range/knockback
    attributes,
  - implemented Kirin's always-floating movement over nearby ground and its
    no-ground recovery blink search,
  - implemented the legacy long-range blink attack with 60 tick charge, 200 tick
    cooldown, outdoor/line-of-sight/distance checks, blink-warning sync data,
    and post-blink life steal,
  - wired client portal particles and a GeckoLib renderer to the converted
    legacy `ModelKirin` geometry, Java-authored animation resource, and legacy
    Kirin texture resource.

## Explicit Gaps

This is not a complete mod port yet. The following systems still require their
own evidence-backed slices:

- the remaining parasite entities, AI goals, attributes, and animations,
- broader `EntityPCosmical` systems used by Kirin and other cosmical parasites:
  clone/shadow damage splitting, cosmical render layer behavior, NeuroLock,
  scary/void orb projectile entities, and related sound/particle polish,
- remaining SRP status effects, potion item variants, brewing data, HUD/screen
  overlays, viral transmission systems, and immunity interactions outside this
  Flog combat slice,
- block registry and legacy block behavior,
- world evolution and phase systems,
- adaptation systems,
- bestiary GUI and networking,
- recipes, loot tables, advancements, and data generation,
- sounds and entity renderers.

Do not treat a build passing as full restoration. Future slices should continue
from legacy bytecode evidence and add verifier markers before broad claims.
