package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.block.DeadBloodFluidBlock;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Baseline registrations for every legacy blockstate migrated from SRParasites 1.10.6.
 *
 * <p>These entries intentionally use simple blocks first so the legacy IDs, block
 * items, recipes, and models exist in 1.21.1. Complex behavior such as fluids,
 * multiblocks, growth, infection conversion, doors, panes, and tile entities is
 * restored in later focused slices without changing the registry names.</p>
 */
public final class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SRPMain.MODID);
    private static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(SRPMain.MODID);
    public static final List<DeferredItem<BlockItem>> CREATIVE_TAB_BLOCK_ITEMS = new ArrayList<>();

    public static final DeferredBlock<Block> ALVEOLI = legacyBlock("alveoli");
    public static final DeferredBlock<Block> ALVEOLI_GROWTH = legacyBlock("alveoli_growth");
    public static final DeferredBlock<Block> ASHEN_GLASS = legacyBlock("ashen_glass");
    public static final DeferredBlock<Block> ASHEN_GLASS_PANE = legacyBlock("ashen_glass_pane");
    public static final DeferredBlock<Block> ASSIMILATED_BLOSSOM = legacyBlock("assimilated_blossom");
    public static final DeferredBlock<Block> ASSIMILATED_JACK_O_LANTERN = legacyBlock("assimilated_jack_o_lantern");
    public static final DeferredBlock<Block> ASSIMILATED_PUMPKIN = legacyBlock("assimilated_pumpkin");
    public static final DeferredBlock<Block> ASSIMILATED_REED = legacyBlock("assimilated_reed");
    public static final DeferredBlock<Block> BIOMASS_BLOCK = legacyBlock("biomass_block");
    public static final DeferredBlock<Block> BIOMEHEART = legacyBlock("biomeheart");
    public static final DeferredBlock<Block> BIOMEPURIFIER = legacyBlock("biomepurifier");
    public static final DeferredBlock<Block> BLOODY_GLASS = legacyBlock("bloody_glass");
    public static final DeferredBlock<Block> BLOODY_GLASS_PANE = legacyBlock("bloody_glass_pane");
    public static final DeferredBlock<Block> BLOODYICE = legacyBlock("bloodyice");
    public static final DeferredBlock<Block> BRUISEWOOD_FENCE = legacyBlock("bruisewood_fence");
    public static final DeferredBlock<Block> BRUISEWOOD_PLANK_SLAB = legacyBlock("bruisewood_plank_slab");
    public static final DeferredBlock<Block> BRUISEWOOD_PLANK_SLAB_DOUBLE = legacyBlock("bruisewood_plank_slab_double");
    public static final DeferredBlock<Block> BRUISEWOOD_PLANK_STAIRS = legacyBlock("bruisewood_plank_stairs");
    public static final DeferredBlock<Block> BRUISEWOOD_PLANK_WALL = legacyBlock("bruisewood_plank_wall");
    public static final DeferredBlock<Block> BRUSEWOOD_DOOR = legacyBlock("brusewood_door");
    public static final DeferredBlock<Block> BRUSEWOOD_PLANKS = legacyBlock("brusewood_planks");
    public static final DeferredBlock<Block> BRUSEWOOD_TRAPDOOR = legacyBlock("brusewood_trapdoor");
    public static final DeferredBlock<Block> CANISTERACTIVE = legacyBlock("canisteractive");
    public static final DeferredBlock<Block> COLONYHEART = legacyBlock("colonyheart");
    public static final DeferredBlock<Block> COLONYOUTPOST = legacyBlock("colonyoutpost");
    public static final DeferredBlock<Block> CONSUMED_DOOR = legacyBlock("consumed_door");
    public static final DeferredBlock<Block> CONSUMED_FENCE = legacyBlock("consumed_fence");
    public static final DeferredBlock<Block> CONSUMED_PLANK_SLAB = legacyBlock("consumed_plank_slab");
    public static final DeferredBlock<Block> CONSUMED_PLANK_SLAB_DOUBLE = legacyBlock("consumed_plank_slab_double");
    public static final DeferredBlock<Block> CONSUMED_PLANK_WALL = legacyBlock("consumed_plank_wall");
    public static final DeferredBlock<Block> CONSUMED_PLANKS = legacyBlock("consumed_planks");
    public static final DeferredBlock<Block> CONSUMED_PLANKS_STAIRS = legacyBlock("consumed_planks_stairs");
    public static final DeferredBlock<Block> CONSUMED_POT = legacyBlock("consumed_pot");
    public static final DeferredBlock<Block> CONSUMED_TRAPDOOR = legacyBlock("consumed_trapdoor");
    public static final DeferredBlock<Block> CONSUMED_WORKBENCH = legacyBlock("consumed_workbench");
    public static final DeferredBlock<Block> COOKED_FLESH = legacyBlock("cooked_flesh");
    public static final DeferredBlock<Block> COOKED_FLESH_FENCE = legacyBlock("cooked_flesh_fence");
    public static final DeferredBlock<Block> COOKED_FLESH_PLANKS = legacyBlock("cooked_flesh_planks");
    public static final DeferredBlock<Block> COOKED_FLESH_SLAB = legacyBlock("cooked_flesh_slab");
    public static final DeferredBlock<Block> COOKED_FLESH_SLAB_DOUBLE = legacyBlock("cooked_flesh_slab_double");
    public static final DeferredBlock<Block> COOKED_FLESH_STAIRS = legacyBlock("cooked_flesh_stairs");
    public static final DeferredBlock<Block> DEAD_HEAD_PLANK_SLAB = legacyBlock("dead_head_plank_slab");
    public static final DeferredBlock<Block> DEAD_HEAD_PLANK_SLAB_DOUBLE = legacyBlock("dead_head_plank_slab_double");
    public static final DeferredBlock<LiquidBlock> DEADBLOOD = legacyFluid("deadblood");
    public static final DeferredBlock<Block> DEADHEAD_FENCE = legacyBlock("deadhead_fence");
    public static final DeferredBlock<Block> DEADHEAD_PLANK_STAIRS = legacyBlock("deadhead_plank_stairs");
    public static final DeferredBlock<Block> DERMOID_CYST = legacyBlock("dermoid_cyst");
    public static final DeferredBlock<Block> DISEASED_SPONGE = legacyBlock("diseased_sponge");
    public static final DeferredBlock<Block> DISPATCHERN = legacyBlock("dispatchern");
    public static final DeferredBlock<Block> ESCA_BULB = legacyBlock("esca_bulb");
    public static final DeferredBlock<Block> ESCA_BULB_BLACK = legacyBlock("esca_bulb_black");
    public static final DeferredBlock<Block> ESCA_BULB_BLUE = legacyBlock("esca_bulb_blue");
    public static final DeferredBlock<Block> ESCA_BULB_BROWN = legacyBlock("esca_bulb_brown");
    public static final DeferredBlock<Block> ESCA_BULB_CYAN = legacyBlock("esca_bulb_cyan");
    public static final DeferredBlock<Block> ESCA_BULB_GRAY = legacyBlock("esca_bulb_gray");
    public static final DeferredBlock<Block> ESCA_BULB_GREEN = legacyBlock("esca_bulb_green");
    public static final DeferredBlock<Block> ESCA_BULB_LIGHT_BLUE = legacyBlock("esca_bulb_light_blue");
    public static final DeferredBlock<Block> ESCA_BULB_LIGHT_GRAY = legacyBlock("esca_bulb_light_gray");
    public static final DeferredBlock<Block> ESCA_BULB_LIME = legacyBlock("esca_bulb_lime");
    public static final DeferredBlock<Block> ESCA_BULB_MAGENTA = legacyBlock("esca_bulb_magenta");
    public static final DeferredBlock<Block> ESCA_BULB_ORANGE = legacyBlock("esca_bulb_orange");
    public static final DeferredBlock<Block> ESCA_BULB_PINK = legacyBlock("esca_bulb_pink");
    public static final DeferredBlock<Block> ESCA_BULB_PURPLE = legacyBlock("esca_bulb_purple");
    public static final DeferredBlock<Block> ESCA_BULB_RED = legacyBlock("esca_bulb_red");
    public static final DeferredBlock<Block> ESCA_BULB_WHITE = legacyBlock("esca_bulb_white");
    public static final DeferredBlock<Block> ESCA_BULB_YELLOW = legacyBlock("esca_bulb_yellow");
    public static final DeferredBlock<Block> EVOLUTIONLURE = legacyBlock("evolutionlure");
    public static final DeferredBlock<Block> FLESH_FENCE = legacyBlock("flesh_fence");
    public static final DeferredBlock<Block> FLESH_PLANKS = legacyBlock("flesh_planks");
    public static final DeferredBlock<Block> FLESH_SLAB = legacyBlock("flesh_slab");
    public static final DeferredBlock<Block> FLESH_SLAB_DOUBLE = legacyBlock("flesh_slab_double");
    public static final DeferredBlock<Block> FLESH_STAIRS = legacyBlock("flesh_stairs");
    public static final DeferredBlock<Block> FOG_NULLIFIER = legacyBlock("fog_nullifier");
    public static final DeferredBlock<Block> FROST_WEATHERED_STONE_SLAB = legacyBlock("frost_weathered_stone_slab");
    public static final DeferredBlock<Block> FROST_WEATHERED_STONE_SLAB_DOUBLE = legacyBlock("frost_weathered_stone_slab_double");
    public static final DeferredBlock<Block> FROST_WEATHERED_STONE_STAIRS = legacyBlock("frost_weathered_stone_stairs");
    public static final DeferredBlock<Block> GOREADA = legacyBlock("goreada");
    public static final DeferredBlock<Block> GOREFER = legacyBlock("gorefer");
    public static final DeferredBlock<Block> GOREMAR = legacyBlock("goremar");
    public static final DeferredBlock<Block> GOREPRI = legacyBlock("gorepri");
    public static final DeferredBlock<Block> GOREPUR = legacyBlock("gorepur");
    public static final DeferredBlock<Block> GORESIM = legacyBlock("goresim");
    public static final DeferredBlock<Block> GOTH_DOOR = legacyBlock("goth_door");
    public static final DeferredBlock<Block> GOTH_FENCE = legacyBlock("goth_fence");
    public static final DeferredBlock<Block> GOTH_PLANK_SLAB = legacyBlock("goth_plank_slab");
    public static final DeferredBlock<Block> GOTH_PLANK_SLAB_DOUBLE = legacyBlock("goth_plank_slab_double");
    public static final DeferredBlock<Block> GOTH_PLANK_WALL = legacyBlock("goth_plank_wall");
    public static final DeferredBlock<Block> GOTH_PLANKS = legacyBlock("goth_planks");
    public static final DeferredBlock<Block> GOTH_PLANKS_STAIRS = legacyBlock("goth_planks_stairs");
    public static final DeferredBlock<Block> GOTH_STEM = legacyBlock("goth_stem");
    public static final DeferredBlock<Block> GOTHSHROOM = legacyBlock("gothshroom");
    public static final DeferredBlock<Block> HAIR_FOLLICLE_BLOCK = legacyBlock("hair_follicle_block");
    public static final DeferredBlock<Block> HARLEQUINN_GLASS = legacyBlock("harlequinn_glass");
    public static final DeferredBlock<Block> HARLEQUINN_GLASS_PANE = legacyBlock("harlequinn_glass_pane");
    public static final DeferredBlock<Block> HARLEQUINN_GRASS = legacyBlock("harlequinn_grass");
    public static final DeferredBlock<Block> HARLESKINN_BLOCK = legacyBlock("harleskinn_block");
    public static final DeferredBlock<Block> HARLESKINN_FENCE = legacyBlock("harleskinn_fence");
    public static final DeferredBlock<Block> HARLESKINN_SLAB = legacyBlock("harleskinn_slab");
    public static final DeferredBlock<Block> HARLESKINN_SLAB_DOUBLE = legacyBlock("harleskinn_slab_double");
    public static final DeferredBlock<Block> HARLESKINN_STAIRS = legacyBlock("harleskinn_stairs");
    public static final DeferredBlock<Block> HIRSUTE_HAIR = legacyBlock("hirsute_hair");
    public static final DeferredBlock<Block> INF_SS = legacyBlock("inf_ss");
    public static final DeferredBlock<Block> INF_SS_CHISELED = legacyBlock("inf_ss_chiseled");
    public static final DeferredBlock<Block> INF_SS_CUT = legacyBlock("inf_ss_cut");
    public static final DeferredBlock<Block> INFESTATION_PURIFIER = legacyBlock("infestation_purifier");
    public static final DeferredBlock<Block> INFESTED_CACTUS = legacyBlock("infested_cactus");
    public static final DeferredBlock<Block> INFESTED_COBBLESTONE = legacyBlock("infested_cobblestone");
    public static final DeferredBlock<Block> INFESTED_COBBLESTONE_SLAB = legacyBlock("infested_cobblestone_slab");
    public static final DeferredBlock<Block> INFESTED_COBBLESTONE_SLAB_DOUBLE = legacyBlock("infested_cobblestone_slab_double");
    public static final DeferredBlock<Block> INFESTED_COLUMN = legacyBlock("infested_column");
    public static final DeferredBlock<Block> INFESTED_DIRT_SLAB = legacyBlock("infested_dirt_slab");
    public static final DeferredBlock<Block> INFESTED_DIRT_SLAB_DOUBLE = legacyBlock("infested_dirt_slab_double");
    public static final DeferredBlock<Block> INFESTED_FENCE = legacyBlock("infested_fence");
    public static final DeferredBlock<Block> INFESTED_FURNACE = legacyBlock("infested_furnace");
    public static final DeferredBlock<Block> INFESTED_FURNACE_LIT = legacyBlock("infested_furnace_lit");
    public static final DeferredBlock<Block> INFESTED_GLASS = legacyBlock("infested_glass");
    public static final DeferredBlock<Block> INFESTED_GLASS_PANE = legacyBlock("infested_glass_pane");
    public static final DeferredBlock<Block> INFESTED_LEAVES = legacyBlock("infested_leaves");
    public static final DeferredBlock<Block> INFESTED_LEAVES_FAST = legacyBlock("infested_leaves_fast");
    public static final DeferredBlock<Block> INFESTED_PLANK_SLAB = legacyBlock("infested_plank_slab");
    public static final DeferredBlock<Block> INFESTED_PLANK_SLAB_DOUBLE = legacyBlock("infested_plank_slab_double");
    public static final DeferredBlock<Block> INFESTED_PLANK_WALL = legacyBlock("infested_plank_wall");
    public static final DeferredBlock<Block> INFESTED_PLANKS = legacyBlock("infested_planks");
    public static final DeferredBlock<Block> INFESTED_PLANKS_STAIRS = legacyBlock("infested_planks_stairs");
    public static final DeferredBlock<Block> INFESTED_POLISHED_STONE_BRICKS_STAIRS = legacyBlock("infested_polished_stone_bricks_stairs");
    public static final DeferredBlock<Block> INFESTED_POT = legacyBlock("infested_pot");
    public static final DeferredBlock<Block> INFESTED_SANDSTONE_SLAB = legacyBlock("infested_sandstone_slab");
    public static final DeferredBlock<Block> INFESTED_SANDSTONE_SLAB_DOUBLE = legacyBlock("infested_sandstone_slab_double");
    public static final DeferredBlock<Block> INFESTED_SANDSTONE_STAIRS = legacyBlock("infested_sandstone_stairs");
    public static final DeferredBlock<Block> INFESTED_SANDSTONE_WALL = legacyBlock("infested_sandstone_wall");
    public static final DeferredBlock<Block> INFESTED_STONE_BRICK_SLAB = legacyBlock("infested_stone_brick_slab");
    public static final DeferredBlock<Block> INFESTED_STONE_BRICK_SLAB_DOUBLE = legacyBlock("infested_stone_brick_slab_double");
    public static final DeferredBlock<Block> INFESTED_STONE_BRICK_WALL = legacyBlock("infested_stone_brick_wall");
    public static final DeferredBlock<Block> INFESTED_STONE_BRICKS = legacyBlock("infested_stone_bricks");
    public static final DeferredBlock<Block> INFESTED_STONE_BRICKS_STAIRS = legacyBlock("infested_stone_bricks_stairs");
    public static final DeferredBlock<Block> INFESTED_STONE_POLISHED = legacyBlock("infested_stone_polished");
    public static final DeferredBlock<Block> INFESTED_STONE_SLAB = legacyBlock("infested_stone_slab");
    public static final DeferredBlock<Block> INFESTED_STONE_SLAB_DOUBLE = legacyBlock("infested_stone_slab_double");
    public static final DeferredBlock<Block> INFESTED_STONE_STAIRS = legacyBlock("infested_stone_stairs");
    public static final DeferredBlock<Block> INFESTED_TERRACOTTA = legacyBlock("infested_terracotta");
    public static final DeferredBlock<Block> INFESTED_TERRACOTTA_SLAB = legacyBlock("infested_terracotta_slab");
    public static final DeferredBlock<Block> INFESTED_TERRACOTTA_SLAB_DOUBLE = legacyBlock("infested_terracotta_slab_double");
    public static final DeferredBlock<Block> INFESTED_WORKBENCH = legacyBlock("infested_workbench");
    public static final DeferredBlock<Block> INFESTEDBUSH = legacyBlock("infestedbush");
    public static final DeferredBlock<Block> INFESTEDORE = legacyBlock("infestedore");
    public static final DeferredBlock<Block> INFESTEDREMAIN = legacyBlock("infestedremain");
    public static final DeferredBlock<Block> INFESTEDRUBBLE = legacyBlock("infestedrubble");
    public static final DeferredBlock<Block> INFESTEDRUBBLE_WALL = legacyBlock("infestedrubble_wall");
    public static final DeferredBlock<Block> INFESTEDRUBBLESTAIRS = legacyBlock("infestedrubblestairs");
    public static final DeferredBlock<Block> INFESTEDSAND = legacyBlock("infestedsand");
    public static final DeferredBlock<Block> INFESTEDSTAIN = legacyBlock("infestedstain");
    public static final DeferredBlock<Block> INFESTEDSTAIN_WALL = legacyBlock("infestedstain_wall");
    public static final DeferredBlock<Block> INFESTEDSTAINSTAIRS = legacyBlock("infestedstainstairs");
    public static final DeferredBlock<Block> INFESTEDTRUNK = legacyBlock("infestedtrunk");
    public static final DeferredBlock<Block> INFESTEDTRUNKSTAIRS = legacyBlock("infestedtrunkstairs");
    public static final DeferredBlock<Block> INFESTREMAIN = legacyBlock("infestremain");
    public static final DeferredBlock<Block> INFUSER_FURNACE = legacyBlock("infuser_furnace");
    public static final DeferredBlock<Block> LIPOMA_MASS = legacyBlock("lipoma_mass");
    public static final DeferredBlock<Block> LOCS_BLOCK = legacyBlock("locs_block");
    public static final DeferredBlock<Block> LOCS_BLOCK_SLAB = legacyBlock("locs_block_slab");
    public static final DeferredBlock<Block> LOCS_BLOCK_SLAB_DOUBLE = legacyBlock("locs_block_slab_double");
    public static final DeferredBlock<Block> MOODY_GLASS = legacyBlock("moody_glass");
    public static final DeferredBlock<Block> MOODY_GLASS_PANE = legacyBlock("moody_glass_pane");
    public static final DeferredBlock<Block> NODE_REDSTONE_LAMP = legacyBlock("node_redstone_lamp");
    public static final DeferredBlock<Block> NODERELAY = legacyBlock("noderelay");
    public static final DeferredBlock<Block> PARASITE_BARRIER = legacyBlock("parasite_barrier");
    public static final DeferredBlock<Block> PARASITEBUSH = legacyBlock("parasitebush");
    public static final DeferredBlock<Block> PARASITECANISTER = legacyBlock("parasitecanister");
    public static final DeferredBlock<Block> PARASITECANISTER_BAG_WALL = legacyBlock("parasitecanister_bag_wall");
    public static final DeferredBlock<Block> PARASITEFOG = legacyBlock("parasitefog");
    public static final DeferredBlock<Block> PARASITELOOT = legacyBlock("parasiteloot");
    public static final DeferredBlock<Block> PARASITEMOUTH = legacyBlock("parasitemouth");
    public static final DeferredBlock<Block> PARASITEPLANK = legacyBlock("parasiteplank");
    public static final DeferredBlock<Block> PARASITEPLANK_DEADHEAD_WALL = legacyBlock("parasiteplank_deadhead_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE = legacyBlock("parasiterubble");
    public static final DeferredBlock<Block> PARASITERUBBLE_BONESTAIRS = legacyBlock("parasiterubble_bonestairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_BRICKS_WALL = legacyBlock("parasiterubble_bricks_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE_BRICKSSTAIRS = legacyBlock("parasiterubble_bricksstairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_FLESH_WALL = legacyBlock("parasiterubble_flesh_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE_FLESHSTAIRS = legacyBlock("parasiterubble_fleshstairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_FUNGUSSTAIRS = legacyBlock("parasiterubble_fungusstairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_METAL_WALL = legacyBlock("parasiterubble_metal_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE_METALSTAIRS = legacyBlock("parasiterubble_metalstairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_OBSIDIANSTAIRS = legacyBlock("parasiterubble_obsidianstairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_STONEDEBRISSTAIRS = legacyBlock("parasiterubble_stonedebrisstairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_STONESTAIRS = legacyBlock("parasiterubble_stonestairs");
    public static final DeferredBlock<Block> PARASITERUBBLE_WEATHB_WALL = legacyBlock("parasiterubble_weathb_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE_WEATHBC_WALL = legacyBlock("parasiterubble_weathbc_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE_WEATHFS_WALL = legacyBlock("parasiterubble_weathfs_wall");
    public static final DeferredBlock<Block> PARASITERUBBLE_WOODSTAIRS = legacyBlock("parasiterubble_woodstairs");
    public static final DeferredBlock<Block> PARASITERUBBLEDENSE = legacyBlock("parasiterubbledense");
    public static final DeferredBlock<Block> PARASITERUBBLEDENSE_BIOME_WALL = legacyBlock("parasiterubbledense_biome_wall");
    public static final DeferredBlock<Block> PARASITERUBBLEDENSE_BIOMESTAIRS = legacyBlock("parasiterubbledense_biomestairs");
    public static final DeferredBlock<Block> PARASITERUBBLEDENSE_COLONY_WALL = legacyBlock("parasiterubbledense_colony_wall");
    public static final DeferredBlock<Block> PARASITERUBBLEDENSE_COLONYSTAIRS = legacyBlock("parasiterubbledense_colonystairs");
    public static final DeferredBlock<Block> PARASITERUBBLEDENSE_WALLSTAIRS = legacyBlock("parasiterubbledense_wallstairs");
    public static final DeferredBlock<Block> PARASITERUBBLESLABDOUBLE = legacyBlock("parasiterubbleslabdouble");
    public static final DeferredBlock<Block> PARASITERUBBLESLABHALF = legacyBlock("parasiterubbleslabhalf");
    public static final DeferredBlock<Block> PARASITESAPLING = legacyBlock("parasitesapling");
    public static final DeferredBlock<Block> PARASITESTAIN = legacyBlock("parasitestain");
    public static final DeferredBlock<Block> PARASITESTAIN_DIRTSTAIRS = legacyBlock("parasitestain_dirtstairs");
    public static final DeferredBlock<Block> PARASITESTAIN_FEELERSTAIRS = legacyBlock("parasitestain_feelerstairs");
    public static final DeferredBlock<Block> PARASITESTAIN_FLESH_WALL = legacyBlock("parasitestain_flesh_wall");
    public static final DeferredBlock<Block> PARASITESTAIN_FLESHSTAIRS = legacyBlock("parasitestain_fleshstairs");
    public static final DeferredBlock<Block> PARASITESTAIN_MUDSTAIRS = legacyBlock("parasitestain_mudstairs");
    public static final DeferredBlock<Block> PARASITESTAINSLABDOUBLE = legacyBlock("parasitestainslabdouble");
    public static final DeferredBlock<Block> PARASITESTAINSLABHALF = legacyBlock("parasitestainslabhalf");
    public static final DeferredBlock<Block> PARASITESTRUCTURE = legacyBlock("parasitestructure");
    public static final DeferredBlock<Block> PARASITETENDRIL = legacyBlock("parasitetendril");
    public static final DeferredBlock<Block> PARASITETHIN = legacyBlock("parasitethin");
    public static final DeferredBlock<Block> PARASITETRUNK = legacyBlock("parasitetrunk");
    public static final DeferredBlock<Block> PARASITETRUNK_BALLSTAIRS = legacyBlock("parasitetrunk_ballstairs");
    public static final DeferredBlock<Block> PARASITETRUNK_PLANTSTAIRS = legacyBlock("parasitetrunk_plantstairs");
    public static final DeferredBlock<Block> PARASITETRUNK_TREESTAIRS = legacyBlock("parasitetrunk_treestairs");
    public static final DeferredBlock<Block> PARASITIC_COLONY_CORE_SLAB = legacyBlock("parasitic_colony_core_slab");
    public static final DeferredBlock<Block> PARASITIC_COLONY_CORE_SLAB_DOUBLE = legacyBlock("parasitic_colony_core_slab_double");
    public static final DeferredBlock<Block> PARASITIC_COMPRESSED_COLONY_STONE_SLAB = legacyBlock("parasitic_compressed_colony_stone_slab");
    public static final DeferredBlock<Block> PARASITIC_COMPRESSED_COLONY_STONE_SLAB_DOUBLE = legacyBlock("parasitic_compressed_colony_stone_slab_double");
    public static final DeferredBlock<Block> POLAND_SKIN_BLOCK = legacyBlock("poland_skin_block");
    public static final DeferredBlock<Block> POLAND_SKIN_SLAB = legacyBlock("poland_skin_slab");
    public static final DeferredBlock<Block> POLAND_SKIN_SLAB_DOUBLE = legacyBlock("poland_skin_slab_double");
    public static final DeferredBlock<Block> POLISHED_INFESTED_STONE_SLAB = legacyBlock("polished_infested_stone_slab");
    public static final DeferredBlock<Block> POLISHED_INFESTED_STONE_SLAB_DOUBLE = legacyBlock("polished_infested_stone_slab_double");
    public static final DeferredBlock<Block> POLISHED_INFESTED_STONE_WALL = legacyBlock("polished_infested_stone_wall");
    public static final DeferredBlock<Block> POTTED_ASSIMILATED_BLOSSOM = legacyBlock("potted_assimilated_blossom");
    public static final DeferredBlock<Block> POTTED_CONSUMED_ASSIMILATED_BLOSSOM = legacyBlock("potted_consumed_assimilated_blossom");
    public static final DeferredBlock<Block> REINFORCED_HIVESTONE_SLAB = legacyBlock("reinforced_hivestone_slab");
    public static final DeferredBlock<Block> REINFORCED_HIVESTONE_SLAB_DOUBLE = legacyBlock("reinforced_hivestone_slab_double");
    public static final DeferredBlock<Block> RELAY_BASE = legacyBlock("relay_base");
    public static final DeferredBlock<Block> RELAY_CONTROLLER_DUMMY = legacyBlock("relay_controller_dummy");
    public static final DeferredBlock<Block> RELAY_MIDDLE = legacyBlock("relay_middle");
    public static final DeferredBlock<Block> RELAY_ROOF = legacyBlock("relay_roof");
    public static final DeferredBlock<Block> RELAYCONTROLLER = legacyBlock("relaycontroller");
    public static final DeferredBlock<Block> RESIDUE_BLOCK = legacyBlock("residue_block");
    public static final DeferredBlock<Block> RESIDUE_BRICK_SLAB = legacyBlock("residue_brick_slab");
    public static final DeferredBlock<Block> RESIDUE_BRICK_SLAB_DOUBLE = legacyBlock("residue_brick_slab_double");
    public static final DeferredBlock<Block> RESIDUE_BRICKS = legacyBlock("residue_bricks");
    public static final DeferredBlock<Block> RESIDUE_PLANTS = legacyBlock("residue_plants");
    public static final DeferredBlock<Block> RESIDUE_STAIRS = legacyBlock("residue_stairs");
    public static final DeferredBlock<Block> RESIDUE_WALL = legacyBlock("residue_wall");
    public static final DeferredBlock<Block> SAC_OF_FLESH_SLAB = legacyBlock("sac_of_flesh_slab");
    public static final DeferredBlock<Block> SAC_OF_FLESH_SLAB_DOUBLE = legacyBlock("sac_of_flesh_slab_double");
    public static final DeferredBlock<Block> SEMIORGANIC_BLOCK = legacyBlock("semiorganic_block");
    public static final DeferredBlock<Block> SEPIA_GLASS = legacyBlock("sepia_glass");
    public static final DeferredBlock<Block> SEPIA_GLASS_PANE = legacyBlock("sepia_glass_pane");
    public static final DeferredBlock<Block> SHADE_GLASS = legacyBlock("shade_glass");
    public static final DeferredBlock<Block> SHADE_GLASS_PANE = legacyBlock("shade_glass_pane");
    public static final DeferredBlock<Block> SHROUDED_GLASS = legacyBlock("shrouded_glass");
    public static final DeferredBlock<Block> SHROUDED_GLASS_PANE = legacyBlock("shrouded_glass_pane");
    public static final DeferredBlock<Block> SICK_ALVEOLI = legacyBlock("sick_alveoli");
    public static final DeferredBlock<Block> SOLID_ALVEOLI_BLOCK = legacyBlock("solid_alveoli_block");
    public static final DeferredBlock<Block> SRPWEB = legacyBlock("srpweb");
    public static final DeferredBlock<Block> THORNSHADE = legacyBlock("thornshade");
    public static final DeferredBlock<Block> TRESSES_HAIR = legacyBlock("tresses_hair");
    public static final DeferredBlock<Block> TROPHY_BOOM_ORB = legacyBlock("trophy_boom_orb");
    public static final DeferredBlock<Block> TROPHY_VOID_ORB = legacyBlock("trophy_void_orb");
    public static final DeferredBlock<Block> TUNNEL = legacyBlock("tunnel");
    public static final DeferredBlock<Block> WEATHERED_BRICKS_SLAB = legacyBlock("weathered_bricks_slab");
    public static final DeferredBlock<Block> WEATHERED_BRICKS_SLAB_DOUBLE = legacyBlock("weathered_bricks_slab_double");
    public static final DeferredBlock<Block> WEATHERED_COBBLESTONE_SLAB = legacyBlock("weathered_cobblestone_slab");
    public static final DeferredBlock<Block> WEATHERED_COBBLESTONE_SLAB_DOUBLE = legacyBlock("weathered_cobblestone_slab_double");
    public static final DeferredBlock<Block> WHEATHERED_BRICKS_STAIRS = legacyBlock("wheathered_bricks_stairs");
    public static final DeferredBlock<Block> WHEATHERED_COBBLESTONE_STAIRS = legacyBlock("wheathered_cobblestone_stairs");

    private ModBlocks() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }

    private static DeferredBlock<Block> legacyBlock(String name) {
        DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.of().strength(1.0F, 3.0F));
        DeferredItem<BlockItem> item = BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        CREATIVE_TAB_BLOCK_ITEMS.add(item);
        return block;
    }

    private static DeferredBlock<LiquidBlock> legacyFluid(String name) {
        return BLOCKS.register(name, () -> new DeadBloodFluidBlock(ModFluids.DEADBLOOD_SOURCE.get(), legacyFluidProperties()));
    }

    private static BlockBehaviour.Properties legacyFluidProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .replaceable()
            .noCollission()
            .strength(100.0F)
            .pushReaction(PushReaction.DESTROY)
            .noLootTable()
            .liquid();
    }
}
