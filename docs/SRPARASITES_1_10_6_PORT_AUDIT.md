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
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers
  `DOD_SMOKE_TRAIL_E` through `EffectDodSmokeTrail` as `dod_smoke_trail` with
  color `0x404040`.
- `com.dhanantry.scapeandrunparasites.potion.EffectDodSmokeTrail`: server-side
  smoke trail behavior. It ticks every tick, removes itself when the host is
  grounded with `<= 10` ticks remaining, and otherwise sends `6` `SMOKE_NORMAL`
  particles at eye height with `0.15` position offsets and `0.02` particle
  speed.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers `CORRO_E` as
  `corrosive` with color `0x7A605A`.
- `com.dhanantry.scapeandrunparasites.potion.PotionCorrosion`: server-side
  armor durability corrosion. Every effect tick it scans armor slots, keeps only
  non-empty damageable stacks, and damages them by `corroValue` while remaining
  durability is above `maxDamage * corrNot`.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers `RAGE_E` as
  `rage` with color `0xF84343`. Legacy Rage is an `SRPEffectBase` with movement
  speed and attack damage attribute modifiers, both using operation `2`
  (`ADD_MULTIPLIED_TOTAL` in modern terms).
- `com.dhanantry.scapeandrunparasites.potion.SRPEffectBase`: clears curative
  items and applies non-bleed effect ticks every `25 >> amplifier` ticks.
- `com.dhanantry.scapeandrunparasites.potion.PotionBleed`: server-side bleeding
  tick behavior, `DAMAGE_INDICATOR` particle, max-health-scaled damage,
  movement multiplier, and `bleedingDamage` / `bleedingDamageCap` config use.
- `com.dhanantry.scapeandrunparasites.event.SRPEventHandlerBus`: applies the
  `viralEnable` / `viralAmount` incoming damage multiplier while a victim has
  `VIRA_E`.
- `com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems`: defines
  Corrosive defaults `corroValue = 3` and `corrNot = 0.1`.
- `com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems`: defines
  Rage defaults `rageEnable = true`, `rageDamage = 0.1`, and `rageSpeed = 0.1`.
  The legacy config text describes `rageEnable` as the switch controlling
  whether parasites spawn with or give the Rage effect.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers `VOMIT_E` as
  `vomit` with color `0x726C41` and a follow-range attribute modifier amount
  `0.9`, and registers `SENS_E` as `senses` with color `0x8E9ED7` and a
  follow-range attribute modifier amount `0.1`. Both old modifiers use
  operation `2` (`ADD_MULTIPLIED_TOTAL` in modern terms).
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers `INDEAF_E` as
  `indeaf` with color `0xFFDD00`, `EFFECTPOS_E` as `effectpos` with color
  `0xB890C8`, and `EFFECTNEG_E` as `effectneg` with color `0x6FACB4`.
- `com.dhanantry.scapeandrunparasites.potion.SRPEffectBase`: implements the
  simple runtime behavior for `INDEAF_E`, `EFFECTPOS_E`, and `EFFECTNEG_E`.
  `indeaf` zeroes horizontal motion and player movement inputs every tick;
  `effectpos` runs every 20 ticks and deals magic damage for each active
  non-bad effect; `effectneg` runs every 20 ticks and reapplies old
  `applyStackPotion` stacking to each active bad effect.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers
  `OVERHEATING_E` as `overheating` with color `0xFF8706` and `CONTA_E` as
  `conta` with color `0x9DF100`. Their potion types both use duration `2400`.
- `com.dhanantry.scapeandrunparasites.potion.PotionOverheat`: server-side tick
  behavior that checks `tickCount % 20 == 0` and sets the host on fire for `2`
  seconds.
- `com.dhanantry.scapeandrunparasites.potion.PotionContamination`: server-side
  behavior gated by the shared `25 >> amplifier` cadence and an additional
  `tickCount % 40 == 0` check. When active it deals `1` damage if the host is
  above `1` health, then spreads `CONTA_E` with the current duration and
  amplifier to all living entities in a `4 x 3 x 4` expanded box around the
  host, using the legacy `applyStackPotion` stacking rules.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers `DLER_E` as
  `needler` with color `0xC7B403`. Current bytecode evidence has no `DLER_P`
  `PotionType` field, so no legacy Needler potion item is registered in this
  slice.
- `com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems`: defines
  Needler defaults `needlerDamage = 0.4`, `needlerTerminal = 7`,
  `needlerMaxDamPlayer = 1.0E9`, `needlerMaxDamMonster = 1.0E9`, empty
  `needlerImmuneList`, and `needlerImmuneListWhite = false`.
- `com.dhanantry.scapeandrunparasites.potion.PotionNeedler`: server-side
  behavior gated by the shared `25 >> amplifier` cadence. Once the terminal
  amplifier is reached, it removes the current Needler effect, checks the
  immune list by entity id substring with whitelist inversion, reapplies
  Needler for `400` ticks at reduced amplifier, subtracts configured
  max-health-scaled damage from current health, broadcasts hurt particles,
  creates a no-block-damage zero-radius explosion, consumes a held totem when
  available, restores the host to `1` health with regeneration and absorption,
  or kills without a totem through the old out-of-world death path
  (`fellOutOfWorld` in the modern port).
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers
  `THORNSHADE_THORNS_E` as `thornshade_thorns` with color `0x421F7E` and a
  potion type duration of `60`.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers marker/basic
  `SRPEffectBase` effects with no dedicated effect subclass in this bytecode:
  `FEAR_E` as `fear` with color `0x111114`, `RES_E` as `antimall` with color
  `0x88626C`, `EPEL_E` as `repel` with color `0x43AC30`, `DEBAR_E` as `debar`
  with color `0x9E134B`, `LINK_E` as `link` with color `0xFF7595`, `PIVOT_E`
  as `pivot` with color `0xFFB1C3`, `JUGG_E` as `jugg` with color `0xBDB885`,
  `PARATE_E` as `parate` with color `0xB35736`, `KILLPRI_E` as `primitive`
  with color `0x8F4C45`, `KILLADA_E` as `adapted` with color `0x7F584E`,
  `KILLPUR_E` as `pure` with color `0x0DA532`, `KILLCRU_E` as `crude` with
  color `0x0DA532`, `KILLFER_E` as `feral` with color `0x993030`,
  `KILLNEX_E` as `nexus` with color `0x487848`, `BRAINING_E` as `braining`
  with color `0x796E85`, `NOVISION_E` as `novision` with color `0x182639`,
  and `MUSCLEOUT_E` as `muscleout` with color `0xEC7F82`. The legacy harmful
  flag is set only for `antimall` and `muscleout` in this marker/basic subset.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers legacy
  `PotionType` fields for the already implemented potion subset:
  `FEAR_P` as `srparasites:fear`, `RES_P` as registry id `srparasites:res`
  with potion name `srparasites:antimall`, `CORRO_P` as registry id
  `srparasites:corro` with potion name `srparasites:corrosive`, `VIRA_P` as
  `srparasites:vira` with `srparasites:viral`, `VOMIT_P`, `RAGE_P`, `EPEL_P`
  as `srparasites:repel`, `SENS_P`, `DEBAR_P`, `LINK_P`, `PIVOT_P`, `JUGG_P`,
  `PARATE_P`, `KILLPRI_P`, `KILLADA_P`, `KILLPUR_P`, `KILLCRU_P`,
  `KILLFER_P`, `KILLNEX_P`, `BRAINING_P`, `NOVISION_P`, `INDEAF_P`,
  `OVERHEATING_P`, `CONTA_P`, `MUSCLEOUT_P`, `EFFECTPOS_P`, `EFFECTNEG_P`,
  and `THE_SIGN_P` using matching registry/name paths unless noted above.
  These use duration `2400`.
  `THORNSHADE_THORNS_P` uses registry/name `srparasites:thornshade_thorns`
  with duration `60`.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: current bytecode
  evidence has no `DOD_SMOKE_TRAIL_P` or `DLER_P` legacy `PotionType` field.
  Although the same legacy class also defines complex `COTH_P`, `FOSTER_P`,
  and `SPOT_P` potion types, their effect behavior depends on separate world
  infection, resistance, conversion, spawning, and mob-cap systems and is left
  to a later behavior slice.
- `com.dhanantry.scapeandrunparasites.potion.PotionTheSign`: registers the
  no-tick `the_sign` effect with color `0x88E1FF` (`8970751`), translation key
  `mob_effect.srparasites.the_sign`, and a legacy custom HUD/inventory icon at
  `textures/gui/the_sign.png`.
- `com.dhanantry.scapeandrunparasites.item.ItemTheSignCharm`: registers
  `srparasites:the_sign_charm` with stack size `1` and appends three tooltip
  lines: `tooltip.srparasites.the_sign_charm.red` in red,
  `tooltip.srparasites.the_sign_charm.white` in white, and
  `tooltip.srparasites.the_sign_charm.gray` in gray italic.
- `com.dhanantry.scapeandrunparasites.events.SignEffectHandler`: on server-side
  player tick END it checks the main inventory, offhand, and armor slots for
  `srparasites:the_sign_charm` and applies `THE_SIGN_E` for `40` ticks at
  amplifier `0` with ambient `false` and visible `false`.
- `com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase`: clears
  current player targets that have `THE_SIGN_E`, stops navigation, and its
  `func_70624_b` / `setAttackTarget` path refuses protected player targets by
  forwarding `null` to the base mob target setter.
- `com.dhanantry.scapeandrunparasites.potion.PotionThornshadeThorns`: defines
  no ticking behavior; `applyEffectTick` returns immediately and
  `shouldApplyEffectTickThisTick` returns `false`.
- `com.dhanantry.scapeandrunparasites.util.handlers.ThornshadeThornsHandler`:
  implements the real Thornshade runtime behavior. It stores state under
  `srp_thornshade_thorns` with `Uses`, `CooldownUntil`, `ExplodeDelay`, and
  `HasExplodedOnce`; rejects parasites, hosts above `120.0` max health, already
  affected hosts, and infinite or `>= 72000` tick effects; reflects melee
  damage at `0.25` or `0.5`; schedules a `20` tick self-destruct after too many
  applications; explodes without block destruction at radius `3.0`; chains
  nearby already affected hosts; and spreads Thornshade for `600` ticks within
  the wider `10.0` search radius.
- `com.dhanantry.scapeandrunparasites.item.ItemThornshadeDecanter`: registers
  `srparasites:thornshade_decanter`, stack size `16`, drink animation, `32` tick
  use duration, and applies `THORNSHADE_THORNS_E` to players for `400` ticks.
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
- `com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch`: Monarch /
  Orch entity size, legacy parasite ID `84`, step height `1.0`, climb flag,
  water leap, AOE melee attack, projectile volley, skill leap, evade dash, skin
  variant selection, and pure-tier attributes. Skin `1` applies half max health
  and 1.5x attack damage; skin `7` uses the heavy texture variant.
- `com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileWebball`:
  legacy `webball` projectile id `101`, size `0.3 x 0.3`, default type `1`,
  Orch-spawned type `2`, 30% player blindness for 60 ticks, mob-griefing-gated
  web placement on impact, and 60 tick timeout web placement.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.pure.RenderOrch`:
  Orch texture paths, skin-to-texture mapping, and shadow radius `1.2`.
- `com.dhanantry.scapeandrunparasites.client.model.entity.pure.ModelOrch`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 196 model bones and method-derived animation names
  `animation.orch.func_78087_a` and
  `animation.orch.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Orch
  attributes before multipliers: `75` health, `10` armor, `25` attack damage,
  `1.0` knockback resistance, and movement speed `0.2775`. The pure follow
  range config default is `32`.

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
  `viralAmount = 0.5`, `corroValue = 3`, `corrNot = 0.1`,
  `rageEnable = true`, `rageDamage = 0.1`, `rageSpeed = 0.1`,
  `needlerDamage = 0.4`, `needlerTerminal = 7`,
  `needlerMaxDamPlayer = 1.0E9`, `needlerMaxDamMonster = 1.0E9`, an empty
  `needlerImmuneList`, `needlerImmuneListWhite = false`, and an empty
  `stackablePotionsLimit` list.
- Added runtime attribute replacement through `ItemAttributeModifierEvent`.
- Added `srpkills` and `srphits` persistence through 1.21 data components.
- Added living-to-sentient upgrade hooks for weapons and armor.
- Registered evidence-backed `srparasites:viral` and `srparasites:bleed` mob
  effects with legacy colors. Their shared base clears NeoForge effect cures and
  ports the old `applyStackPotion` amplifier/duration stacking behavior.
- Implemented core `bleed` behavior: server-only damage indicator particles,
  `25 >> amplifier` tick cadence, max-health-scaled damage, movement scaling,
  and the legacy damage cap.
- Registered evidence-backed `srparasites:dod_smoke_trail` with legacy color
  `0x404040` and ported the old `EffectDodSmokeTrail` runtime surface:
  every-tick server-side smoke trail particles at eye height, legacy particle
  count/offset/speed, and self-removal when grounded with `10` or fewer ticks
  remaining.
- Registered and implemented evidence-backed `srparasites:corrosive` with
  legacy color `0x7A605A`. It ports the old armor-only durability corrosion
  behavior, including `25 >> amplifier` tick cadence, damageable-stack checks,
  `corroValue` durability damage, and the `corrNot` remaining-durability
  threshold.
- Implemented core `viral` behavior: incoming damage amplification based on
  effect amplifier and the `viralAmount` config.
- Registered evidence-backed `srparasites:rage` with legacy color `0xF84343`.
  The effect ports the old movement speed and attack damage attribute modifiers
  using total multiplicative modifiers that scale with amplifier and read the
  modern config values at application time.
- Registered evidence-backed `srparasites:vomit` and `srparasites:senses` with
  legacy colors `0x726C41` and `0x8E9ED7`. Both effects port the old
  follow-range total-multiplier modifiers with amplifier scaling.
- Registered evidence-backed `srparasites:indeaf` with legacy color `0xFFDD00`
  and ported its old every-tick movement lock by clearing horizontal delta
  movement and movement input values server-side.
- Registered evidence-backed `srparasites:effectpos` with legacy color
  `0xB890C8` and ported its old 20 tick cadence, magic damage source, and
  `0.5 * (active non-bad effect amplifier + 1)` damage rule.
- Registered evidence-backed `srparasites:effectneg` with legacy color
  `0x6FACB4` and ported its old 20 tick cadence by copying active effects and
  applying the shared `applyStackPotion` stacking behavior to each active bad
  effect.
- Registered evidence-backed `srparasites:overheating` with legacy color
  `0xFF8706` and ported its old server-side `tickCount % 20 == 0` behavior:
  affected entities are ignited for `2` seconds.
- Registered evidence-backed `srparasites:conta` with legacy color `0x9DF100`
  and ported its old contamination loop: shared `25 >> amplifier` cadence,
  additional 40 tick gate, self-damage while above `1` health, and current
  duration/amplifier spreading through the old `applyStackPotion` stacking rule
  to nearby living entities in the legacy `4 x 3 x 4` expanded area.
- Registered evidence-backed `srparasites:needler` with legacy color
  `0xC7B403` and ported the `PotionNeedler` runtime surface: terminal
  amplifier checks, immune-list handling, `400` tick reapplication at reduced
  amplifier, configured max-health-scaled direct health reduction, hurt event
  byte broadcast, zero-radius no-block-damage explosion, held-totem scan and
  consumption through NeoForge's totem hook, legacy regeneration and absorption
  recovery, and no-totem death through the modern `fellOutOfWorld` damage
  source.
- Registered evidence-backed `srparasites:thornshade_thorns` with legacy color
  `0x421F7E` and no per-effect tick callback, matching
  `PotionThornshadeThorns`.
- Registered evidence-backed marker/basic `SRPEffectBase` effects with legacy
  colors and categories: `srparasites:fear`, `srparasites:antimall`,
  `srparasites:repel`, `srparasites:debar`, `srparasites:link`,
  `srparasites:pivot`, `srparasites:jugg`, `srparasites:parate`,
  `srparasites:primitive`, `srparasites:adapted`, `srparasites:pure`,
  `srparasites:crude`, `srparasites:feral`, `srparasites:nexus`,
  `srparasites:braining`, `srparasites:novision`, and
  `srparasites:muscleout`. This slice intentionally preserves their registry,
  color, category, translation, GUI icon, and potion item surfaces only; their
  external gameplay hooks are tracked as explicit gaps below.
- Registered modern 1.21.1 `Registries.POTION` entries for the implemented
  legacy potion subset: `corro`, `vira`, `vomit`, `rage`, `senses`, `indeaf`,
  `overheating`, `conta`, `effectpos`, `effectneg`, `the_sign`, and
  `thornshade_thorns`. The normal potion durations preserve the legacy `2400`
  ticks, and Thornshade Thorns preserves `60` ticks. Modern potion, splash
  potion, lingering potion, and tipped arrow translation keys were added for
  those registered names.
- Registered modern 1.21.1 `Registries.POTION` entries for the marker/basic
  legacy potion subset: `fear`, `res` (potion name `srparasites:antimall`),
  `repel`, `debar`, `link`, `pivot`, `jugg`, `parate`, `primitive`,
  `adapted`, `pure`, `crude`, `feral`, `nexus`, `braining`, `novision`, and
  `muscleout`, all preserving the legacy `2400` tick duration. Modern potion,
  splash potion, lingering potion, and tipped arrow translation keys were added
  for those registered potion names.
- Registered evidence-backed `srparasites:the_sign` with legacy color
  `0x88E1FF` and no per-effect tick callback, matching `PotionTheSign`.
- Added the legacy `srparasites:the_sign_charm` item with stack size `1`, the
  legacy item texture/model surface, and the three styled tooltip lines from
  `ItemTheSignCharm`.
- Ported the core `SignEffectHandler` runtime surface: server-side
  `PlayerTickEvent.Post` scans main inventory, offhand, and armor slots for the
  charm, then applies The Sign for `40` ticks with amplifier `0`, ambient
  `false`, and visible `false`.
- Ported the first parasite-side target immunity surface from
  `EntityParasiteBase`: shared parasite target selection now rejects players
  with The Sign, direct `setTarget` clears protected player targets, and the
  migrated Flog, Orch, and Kirin AOE / life-steal helper paths use the shared
  protection predicate.
- Added the legacy `srparasites:thornshade_decanter` drink item with stack size
  `16`, `32` tick use duration, drink animation, creative-mode consumption
  bypass, and `400` tick Thornshade application.
- Ported the core `ThornshadeThornsHandler` runtime surface:
  `MobEffectEvent.Applicable` preserves application rejection rules, `Uses` and
  `CooldownUntil` updates, and explosion scheduling; `LivingDamageEvent.Pre`
  reflects thorns damage back to living attackers at legacy `0.25` / `0.5`
  multipliers; `EntityTickEvent.Post` advances the `ExplodeDelay` countdown,
  blood/block-dust particles, no-block-damage self explosion, `HasExplodedOnce`
  persistence, host kill, `thornshade_self_destruct` criterion awarding, nearby
  chained explosions, and `600` tick spread to valid hosts inside the legacy
  `10.0` radius.
- Added a modern `data/srparasites/advancement/thornshade_self_destruct.json`
  with the legacy `exploded` / `minecraft:impossible` criterion so the
  self-destruct award has a loadable 1.21.1 target.
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
- Added the next evidence-backed pure parasite entity slice:
  - registered Orch under the legacy `monarch` visible entity id,
  - registered the legacy `itemmobspawner_orch` spawn egg,
  - preserved size `1.901 x 4.1`, eye height `3.5`, step height `1.0`,
    parasite ID `84`, base health/armor/damage/speed/follow-range/knockback
    attributes, and XP reward `75`,
  - implemented climbing with a synced flag and wall-climber navigation,
  - implemented water leap, AOE melee around the target, skill leap, evade dash,
    webball projectile volley, and skin 1/7 texture variants,
  - wired skin `1` to the legacy weak-variant attribute modifiers: half max
    health and 1.5x attack damage,
  - registered the legacy `webball` projectile with size `0.3 x 0.3`, Orch type
    `2` spawning, 30% player blindness for 60 ticks, mob-griefing-gated web
    placement, 60 tick timeout web placement, and a legacy texture billboard
    renderer,
  - wired a GeckoLib client renderer to the converted legacy `ModelOrch`
    geometry, Java-authored animation resource, and three legacy Orch texture
    resources.

## Explicit Gaps

This is not a complete mod port yet. The following systems still require their
own evidence-backed slices:

- the remaining parasite entities, AI goals, attributes, and animations,
- broader `EntityPCosmical` systems used by Kirin and other cosmical parasites:
  clone/shadow damage splitting, cosmical render layer behavior, NeuroLock,
  scary/void orb projectile entities, and related sound/particle polish,
- remaining SRP status-effect behavior beyond the currently implemented viral,
  bleed, dod_smoke_trail, corrosive, rage, vomit, senses, indeaf, overheating,
  conta, needler, effectpos, effectneg, the_sign, and Thornshade Thorns handler.
  The marker/basic effects `fear`, `antimall`, `repel`, `debar`, `link`,
  `pivot`, `jugg`, `parate`, `primitive`, `adapted`, `pure`, `crude`, `feral`,
  `nexus`, `braining`, `novision`, and `muscleout` currently have their
  registry/potion surface only,
- DEBAR/PIVOT/JUGG/PARATE/EPEL external interactions from old parasite and
  effect systems are not implemented yet: evolution point and XP changes,
  damage redirection, death/dislodgement reactions, attribute-transfer
  interactions, and COTH infection blocking still need dedicated evidence-backed
  slices,
- COTH, FOSTER, PREY, and SPOTTED remain separate complex systems. They require
  infection NBT/immunity, world evolution and aura logic, malleable resistance
  changes, scent entities, origin/mob-cap-aware parasite spawning, and related
  particles before their old potion types should be mirrored in this modern
  port,
- potion item variants for unimplemented effects, brewing data, HUD/screen
  overlays, viral transmission systems, DDP/sign renderer systems, and immunity
  interactions outside the migrated Flog, Orch, and Kirin combat slices. Current
  legacy bytecode evidence has no Needler or Dod Smoke Trail potion type to
  mirror,
- block registry and legacy block behavior,
- SRP Web block variants and type-specific Webball web placement; until the
  block system is migrated, Webball placement is represented by vanilla
  `minecraft:cobweb`,
- world evolution and phase systems,
- adaptation systems,
- bestiary GUI and networking,
- recipes, loot tables, advancements, and data generation,
- sounds and entity renderers.

Do not treat a build passing as full restoration. Future slices should continue
from legacy bytecode evidence and add verifier markers before broad claims.
