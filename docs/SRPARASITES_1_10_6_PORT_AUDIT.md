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
  bows use configurable damage, bonus, and cap values. On arrow release, the
  old bow computes `draw seconds * bonus`, caps that multiplier at
  `damage * damageCap`, multiplies the base arrow damage by the capped
  multiplier, then adds the fixed `damage` value.
- `com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase`: when the
  created projectile is an old `EntityTippedArrow`, the living bow appends
  `BLEED_E` and `DOD_SMOKE_TRAIL_E` for `200` ticks at amplifier `0`, with
  ambient `false` and visible `true`.
- `com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase`: the old
  bow fires through `EntityArrow.func_184547_a` with velocity `power * 4.4` and
  zero inaccuracy.
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
- `com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus`:
  `onClientTick` checks the local player for `INDEAF_E` and releases the old
  forward, back, left, right, and jump key bindings. The modern port maps those
  to `keyUp`, `keyDown`, `keyLeft`, `keyRight`, and `keyJump`.
- `com.dhanantry.scapeandrunparasites.init.SRPPotions`: registers
  `OVERHEATING_E` as `overheating` with color `0xFF8706` and `CONTA_E` as
  `conta` with color `0x9DF100`. Their potion types both use duration `2400`.
- `com.dhanantry.scapeandrunparasites.potion.PotionOverheat`: server-side tick
  behavior that checks `tickCount % 20 == 0` and sets the host on fire for `2`
  seconds.
- `com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus`:
  handles `OVERHEATING_E` in `LivingHurtEvent` after the legacy Viral damage
  adjustment and before Muscle Out. If the current damage source is the old
  `inFire` or `onFire` source, it increases the current amount to
  `amount + amount * (amplifier + 1)` with no config gate. The inspected 1.10.6
  bytecode does not include lava or hot-floor damage in this Overheating branch.
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
- `com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems`: defines
  Muscle Out default `muscleoutDamageOut = 0.09`.
- `com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus`:
  handles `MUSCLEOUT_E` in `LivingHurtEvent` after the legacy Viral damage
  adjustment. It reads the event source entity, requires that source to be a
  living attacker with `MUSCLEOUT_E`, and replaces the current damage amount
  with `amount * muscleoutDamageOut * (amplifier + 1)`.
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
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityLodo`: Buglin
  / Lodo entity size `0.5 x 0.3`, eye height `0.3`, parasite ID `5`, little-tier
  XP, random `60..119` second grow timer, `ruptergrow` NBT persistence, `type =
  1`, initial `killcount = -10`, `buried = -1`, avoid-living AI, buried floor
  timer event `50`, block crack particles while buried, and legacy grow-to-Mudo
  threshold behavior.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityLodo$1`: Lodo
  avoidance predicate rejects water mobs, Creepers, SRP parasites, and animals,
  and avoids other living entities within `8` blocks at walk/sprint speed `1.0`.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderLodo`:
  renderer shadow radius `0.2`, normal texture
  `srparasites:textures/entity/monster/lodo.png`, and alternate texture
  `srparasites:textures/entity/monster/slodo.png`. The provided 1.10.6 jar does
  not contain `slodo.png`, so the modern renderer intentionally does not select
  that missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelLodo`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 27 model bones and method-derived animation names
  `animation.lodo.func_78087_a` and
  `animation.lodo.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Lodo
  attributes before multipliers: `7` health, `1.5` armor, `3` attack damage,
  `0.05` knockback resistance, and movement speed `0.2`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo`: Rupter
  / Mudo entity size `0.85 x 1.0`, eye height `0.8`, parasite ID `12`,
  little-tier XP, `type = 5`, climb flag with `PathNavigateClimber`, skin data
  values `5` and `6`, melee status speed `1.3`, `EntityAISkill` cooldown
  `40..100` with windup `5` and status/event `14`, leap values `0.7` and
  `2.5`, `LeapAtTarget` power `0.4`, and fall damage only after fall distance
  reaches `60`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo`: skin
  `5` applies `VIRA_E` for `100` ticks at amplifier `0` when hitting a
  non-parasite living target. Its complex COTH cloud, tunnel generation,
  Nuuh evolution, and death-to-Lodo colony behavior depend on separate legacy
  world/infection/evolution systems and are not mirrored in this slice.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo$1` through
  `$4`: target/avoid predicates exclude water mobs, animals, villagers, SRP
  parasites, and blacklist categories in different legacy targeting phases.
  The modern slice ports the safe baseline hostile targeting surface and
  leaves phase/config-specific targeting to the evolution/world-system work.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderMudo`:
  renderer shadow radius `0.5`, normal texture
  `srparasites:textures/entity/monster/mudo.png`, variant textures
  `mudov.png` and `mudob.png`, and alternate `smudo.png`. The provided 1.10.6
  jar does not contain `smudo.png`, so the modern renderer intentionally does
  not select that missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelMudo`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 113 model bones and method-derived animation names
  `animation.mudo.func_78087_a` and
  `animation.mudo.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Mudo
  attributes before multipliers: `10` health, `5` armor, `5` attack damage,
  and `0.2` knockback resistance. The inspected legacy bytecode did not expose
  a distinct Mudo movement-speed field in `SRPAttributes`; the modern slice uses
  a conservative small-climber movement speed constant and records this evidence
  boundary here.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh`: Mangler
  / Nuuh entity size `1.0 x 1.0`, eye height `0.9`, parasite ID `76`, pure-tier
  XP, `type = 51`, `attackSpeedT = 6`, malleable base fields, climb flag with
  `PathNavigateClimber`, skin data values `5` and `6`, melee status speed `1.3`,
  `EntityAISkill` cooldown `20..100` with windup `5` and status/event `14`,
  leap values `0.8` and `2.0`, `LeapAtTarget` power `0.4`, evade dash arguments
  `(10, 2, 1, 1.0, 15)`, and fall damage only after fall distance reaches `200`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh`: skin
  `5` applies `VIRA_E` for `100` ticks at amplifier `0` when hitting a
  non-parasite living target. The old entity returns `MOBSILENCE` for ambient,
  hurt, and death sounds even though `nuuh.*` sound resources also exist.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh$1`:
  target predicate excludes water mobs, animals, villagers, and configured mob
  blacklist categories.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderNuuh`:
  renderer shadow radius `0.7`, normal texture
  `srparasites:textures/entity/monster/nuuh.png`, variant textures
  `nuuhv.png` and `nuuhb.png`, and alternate `snuuh.png`. The provided 1.10.6
  jar does not contain `snuuh.png`, so the modern renderer intentionally does
  not select that missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelNuuh`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 248 model bones and method-derived animation names
  `animation.nuuh.func_78087_a` and
  `animation.nuuh.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Nuuh
  attributes before multipliers: `17` health, `10` armor, `9` attack damage,
  `0.6` knockback resistance, movement speed `0.37`, and follow range `32`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta`: Gnat
  / Ata entity size `0.85 x 1.0`, eye height `0.8`, parasite ID `91`,
  little-tier XP, `type = 5`, `attackSpeedT = 6`, `lifespan = 1200` ticks,
  climb flag with `PathNavigateClimber`, `EntityAISkill` cooldown `20..100`
  with windup `5` and status/event `14`, leap values `0.4` and `1.0`,
  `LeapAtTarget` power `0.4`, melee status speed `1.3`, no normal
  `doHurtTarget` damage, and fall damage only after fall distance reaches `60`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta`:
  collision/contact with its current target checks the legacy hijack threshold
  `SRPConfigSystems.hijackHealth = 0.5`; the old branch attempts COTH feral
  conversion or hijacking, otherwise deals attack damage. All contact outcomes
  then emit legacy particle events (`10` for damage/burst, `11` for
  conversion/despawn), play `BUTHOL_BOOM` at volume `0.3`, apply `VIRA_E` for
  `120` ticks at amplifier `2`, and remove the Ata.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta$1`:
  non-player target predicate excludes water mobs and configured blacklist
  categories.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderAta`:
  renderer shadow radius `0.5` and texture
  `srparasites:textures/entity/monster/gnat.png`. The jar also contains
  `ata.png`, but the old renderer resolves the visible Gnat texture to
  `gnat.png`, so the modern renderer follows that reference.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelAta`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 100 model bones and method-derived animation names
  `animation.ata.func_78087_a` and
  `animation.ata.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Ata
  attributes before multipliers: `5` health, `2` armor, `5` attack damage,
  `0.6` knockback resistance, movement speed `0.34559`, and follow range `32`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityRathol`:
  Rathol / Heavy Carrier entity size `1.3 x 3.1`, parasite ID `3`, type `41`,
  `killcount = -10`, fuse time `70`, attack speed `1.1`, melee/targeting
  goals, low-health self state below `5%` max health, and death-triggered
  explosion unless on fire. Its explosion uses radius `4.0`, applies Viral and
  Vomit to nearby non-parasites, creates a poison cloud, and has a stronger
  skin `1` variant with movement speed `0.3`, effect radius `11`, and direct
  damage around the blast.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderRathol`:
  legacy renderer uses shadow radius `1.2`, normal texture
  `srparasites:textures/entity/monster/rathol.png`, skin `1` texture
  `srparasites:textures/entity/monster/ratholone.png`, and references
  `srathol.png` for skin `120`. The provided 1.10.6 jar does not contain
  `srathol.png`, so the modern renderer intentionally does not select that
  missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelRathol`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 30 model bones and method-derived animation names
  `animation.rathol.func_78087_a` and
  `animation.rathol.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Rathol
  attributes before multipliers: `30` health, `5` armor, `25` attack damage,
  `0.95` knockback resistance, movement speed `0.2`, and follow range `32`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityGothol`:
  Gothol / Light Carrier entity size `0.85 x 2.3`, parasite ID `304`, type
  `41`, `killcount = -10`, fuse time `70`, attack speed `1.1`,
  melee/targeting goals, low-health self state below `5%` max health, and
  death-triggered explosion unless on fire. Its explosion uses radius `4.0`,
  applies Viral and Vomit to nearby non-parasites, creates a poison cloud with
  radius `width * 2.5`, and has a stronger skin `1` variant with movement
  speed `0.3`, effect radius `11`, direct damage around the blast, and a
  shorter `4` tick cloud wait.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderGothol`:
  legacy renderer uses shadow radius `1.2`, normal texture
  `srparasites:textures/entity/monster/gothol.png`, an unused
  `srparasites:textures/entity/monster/test.png` field, and references
  `sgothol.png` for skin `120`. The provided 1.10.6 jar does not contain
  `sgothol.png`, so the modern renderer intentionally does not select that
  missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelGothol`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 124 model bones and method-derived animation names
  `animation.gothol.func_78087_a` and
  `animation.gothol.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Gothol
  attributes before multipliers: `30` health, `5` armor, `25` attack damage,
  `0.95` knockback resistance, movement speed `0.2`, and follow range `32`.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityButhol`:
  Buthol / Flying Carrier entity id `carrier_flying`, size `1.4 x 2.4`, eye
  height `2.4`, parasite ID `11`, type `31`, infected-tier XP doubled to `20`,
  fuse time `30`, no fall damage, no-gravity flight, ground lift target `+5Y`
  at speed `0.5`, random air movement every `1/7` checks, charge attack speed
  `1.0`, and variant skin `1` selection.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityButhol`:
  explosion evidence uses radius `4.0`, mob-griefing gate, nearby non-parasite
  living scan in the legacy `4.0` inflated box, Viral for `400` ticks at
  amplifier `1`, Vomit for `400` ticks at amplifier `0`, skin `1` direct magic
  damage, and Buthol boom sound. Its old `butholMobs` summon table and custom
  `SRPExplosion`/residue backend require separate world-system slices.
- `com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityButhol`:
  lingering toxic cloud radius is `width * 3.5`, radius-on-use `0.5`, wait
  time `10`, duration halved, poison for `300` ticks at amplifier `0` or `2`,
  and old COTH/Viral cloud effects for `3600` ticks at matching amplifier. The
  modern slice applies poison and Viral, with COTH still tracked as a missing
  backend.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.inborn.RenderButhol`:
  renderer shadow radius `1.3`, normal texture
  `srparasites:textures/entity/monster/buthol.png`, skin `1` texture
  `srparasites:textures/entity/monster/butholone.png`, and skin `120` texture
  `srparasites:textures/entity/monster/sbuthol.png`. The provided 1.10.6 jar
  does not contain `sbuthol.png`, so the modern renderer intentionally does
  not select that missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.inborn.ModelButhol`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 43 model bones and method-derived animation names
  `animation.buthol.func_78087_a` and
  `animation.buthol.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Buthol
  attributes before multipliers: `20` health, `2.5` armor, `10` attack damage,
  `0.15` knockback resistance, and max flight Y `256`.
- `com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityTonro`:
  Tonro / Kyphosis entity id `kyphosis`, size `0.7 x 4.5`, eye height `3.8`,
  parasite ID `29`, type `40`, adapted-tier XP doubled to `110`, stationary
  movement speed `0`, knockback resistance `1`, follow range `20`, and buried
  time `7.5`.
- `com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityTonro`:
  old AI uses HurtByTarget, `EntityAIAttackMeleeStatusAOE(this, 0.0, false,
  8.0, 7.0)`, look-idle, and `EntityAISkill(this, 20, 16, 1, true, 1)`.
  Its direct hit adds `+0.5000000059604645` vertical motion. Its AOE melee
  broadcasts entity event `12`, plays `SRPSounds.SWIPE` at volume `3`, scans a
  target-centered box inflated by `2.0`, and attacks visible non-parasite
  living entities.
- `com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityTonro`:
  old shockwave skill sets parasite status `10`, stops navigation, plays a hurt
  sound at volume `4` with pitch around `2`, broadcasts flame event `100` while
  border is `<= 2`, advances border every `20` ticks, spawns `EntityWave` at
  borders `3` and `5`, and finishes after border `6` or when the target is
  absent / at a different Y. Wave damage is attack damage times `0.3`, with
  deterrent minimum damage default `2`.
- `com.dhanantry.scapeandrunparasites.client.renderer.entity.deterrent.RenderTonro`:
  renderer shadow radius `0.4`, normal texture
  `srparasites:textures/entity/monster/tonro.png`, and skin `120` texture
  `srparasites:textures/entity/monster/snowvariants/kyphosisfrozen.png`. The
  provided 1.10.6 jar does not contain `kyphosisfrozen.png`, so the modern
  renderer intentionally does not select that missing path.
- `com.dhanantry.scapeandrunparasites.client.model.entity.deterrent.ModelTonro`:
  legacy `ModelRenderer` geometry and Java-authored pose methods. GeckoLib
  conversion preserved 140 model bones and method-derived animation names
  `animation.tonro.func_78087_a` and
  `animation.tonro.setRotationAnglesCosmical`.
- `com.dhanantry.scapeandrunparasites.util.SRPAttributes`: default Tonro
  attributes before multipliers: `50` health, `15` armor, `15` attack damage,
  and `35` swing attack damage.

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
  `needlerImmuneList`, `needlerImmuneListWhite = false`,
  `muscleoutDamageOut = 0.09`, and an empty `stackablePotionsLimit` list.
- Added runtime attribute replacement through `ItemAttributeModifierEvent`.
- Added `srpkills` and `srphits` persistence through 1.21 data components.
- Added living-to-sentient upgrade hooks for weapons and armor.
- Ported the living greatbow damage formula from `WeaponToolRangeBase`: normal
  and sentient greatbows now use the legacy `weapon_bow_bonus`,
  `weapon_bow_damageCap`, and `weapon_bow_damage` sequence instead of treating
  `damageCap` as a final total-damage cap. This multiplies the base arrow
  damage by the capped multiplier, then adds the fixed damage value.
- Ported the living greatbow legacy tipped-arrow effects from
  `WeaponToolRangeBase`: when the modern projectile stack is a `TippedArrowItem`,
  the created `Arrow` receives the old `BLEED_E` and `DOD_SMOKE_TRAIL_E`
  additions for `200` ticks at amplifier `0`, with ambient `false` and visible
  `true`.
- Ported the living greatbow legacy arrow ballistics from
  `WeaponToolRangeBase`: modern `BowItem` passes `power * 3.0` into
  `shootProjectile`, so the port converts that value to the legacy `power * 4.4`
  velocity and uses zero inaccuracy before applying the legacy damage formula.
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
- Ported the old Indeaf client input lock: on NeoForge
  `ClientTickEvent.Pre` and `ClientTickEvent.Post`, the physical client checks
  the local player for `INDEAF_E` and calls `key.setDown(false)` on `keyUp`,
  `keyDown`, `keyLeft`, `keyRight`, and `keyJump`.
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
- Ported Overheating fire damage amplification from the old
  `SRPEventHandlerBus` `LivingHurtEvent`: when the affected entity takes
  `DamageTypes.IN_FIRE` or `DamageTypes.ON_FIRE` damage, the current incoming
  damage amount is increased by `amount + amount * (amplifier + 1)`. This keeps
  the legacy order after Viral and before Muscle Out, and intentionally does
  not include modern lava or hot-floor damage without legacy evidence.
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
  `srparasites:muscleout`. This preserves their registry, color, category,
  translation, GUI icon, and potion item surfaces; external gameplay hooks
  beyond Muscle Out are tracked as explicit gaps below.
- Ported Muscle Out outgoing damage from the old `SRPEventHandlerBus`
  `LivingHurtEvent`: when the event source entity is a living attacker with
  `MUSCLEOUT_E`, the current incoming damage amount is replaced by
  `amount * muscleoutDamageOut * (amplifier + 1)`. Muscle Out preserves its
  legacy replacement formula and remains independent from the `viralEnable`
  config gate.
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
- Added the first evidence-backed inborn parasite entity slice:
  - registered Lodo / Buglin under the visible entity id `lodo`,
  - upgraded the legacy `itemmobspawner_lodo` item into a real modern spawn egg,
  - preserved size `0.5 x 0.3`, eye height `0.3`, parasite ID `5`,
    health/armor/damage/speed/knockback attributes, and little-tier XP surface,
  - ported the old avoid-living AI predicate for non-water, non-Creeper,
    non-parasite, non-animal living entities within the legacy `8` block range,
  - preserved the `60..119` second grow timer, `ruptergrow` NBT key, buried
    floor timer event `50`, freeze-while-buried behavior, and block crack
    particle surface,
  - wired legacy Lodo growl/hurt/death and grow-to-Mudo sound events,
  - replaced the temporary grow-to-Mudo placeholder with real Mudo spawning at
    the Lodo position followed by Lodo removal,
  - wired a GeckoLib client renderer to the converted legacy `ModelLodo`
    geometry, Java-authored animation resource, and legacy Lodo texture. The
    old renderer's missing `slodo.png` alternate path is intentionally not
    selected in the modern renderer.
- Added the second evidence-backed inborn parasite entity slice:
  - registered Mudo / Rupter under the visible entity id `mudo`,
  - upgraded the legacy `itemmobspawner_mudo` item into a real modern spawn egg,
  - preserved size `0.85 x 1.0`, eye height `0.8`, parasite ID `12`,
    health/armor/damage/knockback attributes, little-tier XP, climbing
    navigation, climb-state sync, and fall-damage threshold `60`,
  - ported the old melee speed `1.3`, LeapAtTarget power `0.4`, and skill leap
    timing/power surface (`40..100` cooldown, `5` windup, event `14`, `0.7`
    vertical power, and `2.5` leap speed),
  - preserved skin `5` / `6` variant selection and skin `5` viral-on-hit
    behavior for `100` ticks at amplifier `0`,
  - wired legacy Mudo growl/hurt/death and small-step sounds,
  - wired a GeckoLib client renderer to the converted legacy `ModelMudo`
    geometry, Java-authored animation resource, and the three jar-backed Mudo
    texture resources. The old renderer's missing `smudo.png` alternate path is
    intentionally not selected in the modern renderer.
- Added the third evidence-backed inborn parasite entity slice:
  - registered Nuuh / Mangler under the visible entity id `nuuh`,
  - upgraded the legacy `itemmobspawner_nuuh` item into a real modern spawn egg,
  - preserved size `1.0 x 1.0`, eye height `0.9`, parasite ID `76`, type `51`,
    pure-tier XP, health/armor/damage/speed/knockback/follow-range attributes,
    climbing navigation, climb-state sync, and fall-damage threshold `200`,
  - ported the old melee speed `1.3`, LeapAtTarget power `0.4`, skill leap
    timing/power surface (`20..100` cooldown, `5` windup, event `14`, `0.8`
    vertical power, and `2.0` leap speed), and evade dash timing surface,
  - preserved skin `5` / `6` variant selection and skin `5` viral-on-hit
    behavior for `100` ticks at amplifier `0`,
  - preserved the old entity's silent ambient/hurt/death sound surface and
    small-step sound,
  - wired a GeckoLib client renderer to the converted legacy `ModelNuuh`
    geometry, Java-authored animation resource, and the three jar-backed Nuuh
    texture resources. The old renderer's missing `snuuh.png` alternate path is
    intentionally not selected in the modern renderer.
- Added the fourth evidence-backed inborn parasite entity slice:
  - registered Ata / Gnat under the visible entity id `gnat`,
  - upgraded the legacy `itemmobspawner_ata` item into a real modern spawn egg,
  - preserved size `0.85 x 1.0`, eye height `0.8`, parasite ID `91`, type `5`,
    little-tier XP, health/armor/damage/speed/knockback/follow-range
    attributes, climbing navigation, climb-state sync, and fall-damage
    threshold `60`,
  - ported the old melee speed `1.3`, LeapAtTarget power `0.4`, skill leap
    timing/power surface (`20..100` cooldown, `5` windup, event `14`, `0.4`
    vertical power, and `1.0` leap speed), and 1200-tick lifespan despawn,
  - preserved Ata's unusual combat surface where normal melee damage returns
    false and contact/collision drives the old burst behavior instead,
  - preserved the contact viral effect (`120` ticks at amplifier `2`), burst
    sound, burst/despawn particle events, silent ambient/hurt/death sounds, and
    small-step sound,
  - wired a GeckoLib client renderer to the converted legacy `ModelAta`
    geometry, Java-authored animation resource, and the renderer-backed
    `gnat.png` texture.
- Added the fifth evidence-backed inborn parasite entity slice:
  - registered Rathol / Heavy Carrier under the visible entity id
    `carrier_heavy`,
  - upgraded the legacy `itemmobspawner_rathol` item into a real modern spawn
    egg,
  - preserved size `1.3 x 3.1`, eye height `2.6`, parasite ID `3`, type `41`,
    primitive-tier XP surface, health/armor/damage/speed/knockback/follow-range
    attributes, and the `70` tick fuse,
  - ported the old swim, swell, melee, look-idle, HurtByTarget, player target,
    and non-water/non-animal living target surface,
  - preserved the low-health self-state trigger below `5%` max health and the
    death explosion bypass when killed while on fire,
  - ported the explosion radius `4.0`, mob-griefing gate, Viral/Vomit nearby
    effects, variant skin `1` movement/effect-radius/direct-damage behavior,
    and lingering poison cloud surface,
  - wired legacy carrier growl/hurt/death and Rathol boom sounds,
  - wired a GeckoLib client renderer to the converted legacy `ModelRathol`
    geometry, Java-authored animation resource, and the two jar-backed Rathol
    texture resources. The old renderer's missing `srathol.png` alternate path
    is intentionally not selected in the modern renderer.
- Added the sixth evidence-backed inborn parasite entity slice:
  - registered Gothol / Light Carrier under the visible entity id
    `carrier_light`,
  - upgraded the legacy `itemmobspawner_gothol` item into a real modern spawn
    egg,
  - preserved size `0.85 x 2.3`, eye height `1.95`, parasite ID `304`, type
    `41`, primitive-tier XP surface, health/armor/damage/speed/knockback/
    follow-range attributes, and the `70` tick fuse,
  - ported the old swim, swell, melee, look-idle, HurtByTarget, player target,
    and non-water/non-animal living target surface,
  - preserved the low-health self-state trigger below `5%` max health and the
    death explosion bypass when killed while on fire,
  - ported the explosion radius `4.0`, mob-griefing gate, Viral/Vomit nearby
    effects, variant skin `1` movement/effect-radius/direct-damage behavior,
    shorter variant cloud wait, and lingering poison cloud surface,
  - wired legacy carrier growl/hurt/death and Rathol boom sounds,
  - wired a GeckoLib client renderer to the converted legacy `ModelGothol`
    geometry, Java-authored animation resource, and the jar-backed Gothol
    texture resource. The old renderer's missing `sgothol.png` alternate path
    is intentionally not selected in the modern renderer.
- Added the seventh evidence-backed inborn parasite entity slice:
  - registered Buthol / Flying Carrier under the visible entity id
    `carrier_flying`,
  - upgraded the legacy `itemmobspawner_buthol` item into a real modern spawn
    egg,
  - preserved size `1.4 x 2.4`, eye height `2.4`, parasite ID `11`, type `31`,
    infected-tier doubled XP surface, health/armor/damage/knockback/follow-range
    attributes, no-fall-damage flight, and the `30` tick fuse,
  - ported the old no-gravity air movement surface: ground lift toward `+5Y`,
    random empty-air wandering, Vex-style acceleration toward move targets, and
    target-facing charge attacks,
  - ported the old swell, HurtByTarget, player target, and non-water/non-animal
    living target surface,
  - ported the explosion radius `4.0`, mob-griefing gate, Viral/Vomit nearby
    effects, variant skin `1` direct-damage behavior, and lingering poison cloud
    surface,
  - wired legacy carrier growl/hurt/death and Buthol boom sounds,
  - wired a GeckoLib client renderer to the converted legacy `ModelButhol`
    geometry, Java-authored animation resource, and the two jar-backed Buthol
    texture resources. The old renderer's missing `sbuthol.png` alternate path
    is intentionally not selected in the modern renderer.
- Added the first evidence-backed deterrent parasite entity slice:
  - registered Tonro / Kyphosis under the visible entity id `kyphosis`,
  - upgraded the legacy `itemmobspawner_tonro` item into a real modern spawn
    egg,
  - preserved size `0.7 x 4.5`, eye height `3.8`, parasite ID `29`, type `40`,
    adapted-tier doubled XP surface, health/armor/damage/movement/
    knockback/follow-range attributes, and the legacy buried time constant,
  - ported the old stationary target surface with HurtByTarget, player target,
    and non-water/non-animal living target predicates,
  - ported the AOE melee surface: event `12`, attack timer rise/fall,
    target-centered inflated box, line-of-sight filtering, non-parasite
    filtering, swipe sound, and target launch,
  - ported the shockwave skill timing surface: status `10`, event `100` flame
    particles, border progression, triggers at borders `3` and `5`, finish
    after border `6`, and attack-damage `0.3` / minimum `2` damage,
  - wired legacy Tonro growl/hurt/death, mob silence, and mob swipe sounds,
  - wired a GeckoLib client renderer to the converted legacy `ModelTonro`
    geometry, Java-authored animation resource, and the jar-backed Tonro
    texture resource. The old renderer's missing `kyphosisfrozen.png` alternate
    path is intentionally not selected in the modern renderer.

## Explicit Gaps

This is not a complete mod port yet. The following systems still require their
own evidence-backed slices:

- the remaining parasite entities, AI goals, attributes, and animations,
- broader `EntityPCosmical` systems used by Kirin and other cosmical parasites:
  clone/shadow damage splitting, cosmical render layer behavior, NeuroLock,
  scary/void orb projectile entities, and related sound/particle polish,
- remaining SRP status-effect behavior beyond the currently implemented viral,
  bleed, dod_smoke_trail, corrosive, rage, vomit, senses, indeaf, overheating,
  conta, needler, muscleout, effectpos, effectneg, the_sign, and Thornshade
  Thorns handler.
  The marker/basic effects `fear`, `antimall`, `repel`, `debar`, `link`,
  `pivot`, `jugg`, `parate`, `primitive`, `adapted`, `pure`, `crude`, `feral`,
  `nexus`, `braining`, and `novision` currently have their registry/potion
  surface only,
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
- remaining living/sentient greatbow behavior beyond the migrated damage
  formula, tipped-arrow effect additions, and legacy arrow ballistics:
  scent/Prey calling gates, pull/pulling/vinni item predicates, and tooltip text
  still need focused slices,
- block registry and legacy block behavior,
- SRP Web block variants and type-specific Webball web placement; until the
  block system is migrated, Webball placement is represented by vanilla
  `minecraft:cobweb`,
- Lodo's old collision infection path applies `COTH_E` for `100` ticks at
  amplifier `1`; COTH remains a separate infection/world-system migration and
  is not applied by the current Lodo slice,
- Mudo's COTH cloud/infection, tunnel generation, evolution-to-Nuuh trigger,
  death-to-Lodo colony variant rules, and evolution-phase targeting predicates
  depend on missing world infection/evolution systems and remain explicit future
  slices,
- Nuuh's full `EntityPMalleable` adaptation/resistance behavior, colony death
  checks, death-to-Mudo colony variant rules, and config-backed blacklist
  targeting are not complete until the malleable, colony, and world evolution
  systems are migrated,
- Ata's full COTH feral conversion and hijacked-entity replacement tables are
  not complete until the COTH/hijack victim table systems are migrated; the
  modern slice preserves the contact threshold, viral/burst/despawn surface,
  and records the missing conversion/replacement backend here,
- Rathol's full `SRPExplosion`, InfestRemain block placement, `ratholMobs`
  summon table, COTH cloud effect, and exact config-backed blacklist/griefing
  behavior remain explicit future slices. The modern slice preserves the
  evidence-backed fuse, basic explosion, Viral/Vomit/poison cloud, fire death
  bypass, and skin `1` blast variant surfaces,
- Gothol's full `SRPExplosion`, InfestRemain block placement, `gotholMobs`
  summon table, COTH cloud effect, and exact config-backed blacklist/griefing
  behavior remain explicit future slices. The modern slice preserves the
  evidence-backed fuse, basic explosion, Viral/Vomit/poison cloud, fire death
  bypass, and skin `1` blast variant surfaces,
- Buthol's full `SRPExplosion`, InfestRemain block placement, `butholMobs`
  summon table, COTH cloud effect, flight-height config override, and exact
  config-backed blacklist/griefing behavior remain explicit future slices. The
  modern slice preserves the evidence-backed flying movement, charge, fuse,
  basic explosion, Viral/Vomit/poison cloud, fire death bypass, and skin `1`
  blast variant surfaces,
- Tonro's exact `EntityWave` projectile/entity, full `EntityPStationary` bury /
  peek / relocation behavior, and full `EntityPMalleable` adaptation/resistance
  backend remain explicit future slices. The modern slice preserves the
  evidence-backed stationary target surface, AOE melee, shockwave timing,
  damage, sounds, particles, and animation resources,
- world evolution and phase systems,
- adaptation systems,
- bestiary GUI and networking,
- recipes, loot tables, advancements, and data generation,
- sounds and entity renderers.

Do not treat a build passing as full restoration. Future slices should continue
from legacy bytecode evidence and add verifier markers before broad claims.
