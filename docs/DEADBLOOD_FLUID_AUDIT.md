# Dead Blood Fluid Port Audit

## External wiki evidence

- Official wiki home identifies the Fandom site as the official Scape and Run: Parasites wiki: https://scape-and-run-parasites.fandom.com/wiki/Scape_and_Run%3A_Parasites_Wiki
- Dead Blood wiki page: https://scape-and-run-parasites.fandom.com/wiki/Dead_Blood
  - Dead Blood flows similarly to water.
  - A glass bottle used on a source block gives bottled Dead Blood Fluid.
  - Contact deals rapid chip damage, applies stacking Viral, and heals parasites instead of harming them.
  - Contact with water/lava transforms those liquids into Visceral Mud/Bleeding Obsidian.
- Dead Blood Fluid wiki page: https://scape-and-run-parasites.fandom.com/wiki/Dead_Blood_Fluid
  - Bottles stack to 64.
  - Drinking applies Viral II for 30 seconds and visibly distorts the screen.
  - Bottles are obtained from Dead Blood source blocks or by brewing a Diseased Sponge with an Awkward Potion.
- Alveoli wiki page: https://scape-and-run-parasites.fandom.com/wiki/Alveoli
  - The wiki item page covers `srparasites:alveoligrowth`, not bottled `srparasites:alveolar_fluid`.
  - Current bottled Alveolar Fluid behavior therefore follows legacy bytecode rather than wiki item text.

## Legacy jar evidence

Inspected from `杂物/[逃逸：寄生体] SRParasites-1.10.6.jar` with `javap -p -c`:

- `com.dhanantry.scapeandrunparasites.init.SRPFluids`
- `com.dhanantry.scapeandrunparasites.fluid.DeadBloodFluid`
- `com.dhanantry.scapeandrunparasites.block.BlockFluid`
- `com.dhanantry.scapeandrunparasites.events.DeadBloodBottleHandler`
- `com.dhanantry.scapeandrunparasites.item.ItemDeadBlood`
- `com.dhanantry.scapeandrunparasites.item.ItemAlveolarFluid`
- `com.dhanantry.scapeandrunparasites.client.DeadBloodFogHandler`
- `com.dhanantry.scapeandrunparasites.client.DeadBloodOverlayHandler`
- `com.dhanantry.scapeandrunparasites.client.DeadBloodSwimHandler`

## Implemented in this slice

- `deadblood` is now a NeoForge fluid with source and flowing variants, legacy density 3000, viscosity 1500, temperature 310, and a `LiquidBlock` registration.
- Empty glass bottles used on Dead Blood create `deadblood_fluid`, consume one empty bottle outside creative mode, and play the legacy bottle-fill sound.
- Bottled Dead Blood stacks to 64, drinks for 32 ticks, returns a glass bottle, and applies Viral II for 600 ticks.
- Dead Blood fluid slows non-parasite movement, reduces falling speed, resets fall distance, applies viral-scaled magic chip damage, and adds Corrosive/Viral when the entity head is submerged.
- Parasite entities are healed by Dead Blood instead of damaged.
- Client behavior restores green fog, close fog distance, water overlay cancellation, a flowing Dead Blood screen overlay, and jump-key swim lift while the player's eyes are in Dead Blood.
- Bottled Alveolar Fluid follows legacy bytecode: stack size 1, 32 tick drink, returns a glass bottle, and applies Speed I, Haste I, and Viral III for 600 ticks.

## Remaining gaps

- Water/lava conversion remains a documented gap. The wiki says Dead Blood turns contacted water into Visceral Mud and lava into Bleeding Obsidian, but those target blocks are still represented in this repo as broader blockstate assets rather than a focused conversion behavior slice.
- Diseased Sponge brewing into Dead Blood Fluid remains a documented gap. The wiki confirms the recipe path, but this slice only restores the direct source-block bottle path and drink behavior.
