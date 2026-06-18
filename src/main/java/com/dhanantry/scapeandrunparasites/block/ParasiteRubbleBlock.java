package com.dhanantry.scapeandrunparasites.block;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ParasiteRubbleBlock extends Block {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant", Variant.class);

    public ParasiteRubbleBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(VARIANT, Variant.FLESH));
    }

    public BlockState obsidianState() {
        return defaultBlockState().setValue(VARIANT, Variant.OBSIDIAN);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum Variant implements StringRepresentable {
        FLESH,
        BONE,
        STONE,
        FUNGUS,
        OBSIDIAN,
        BRICKS,
        WOOD,
        METAL,
        STONEDEBRIS,
        WEATHB,
        WEATHBS,
        WEATHBC,
        WEATHBCS,
        WEATHFS,
        WEATHFSS;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
