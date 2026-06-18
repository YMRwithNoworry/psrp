package com.dhanantry.scapeandrunparasites.block;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ParasiteStainBlock extends Block {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant", Variant.class);

    public ParasiteStainBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(VARIANT, Variant.DIRT));
    }

    public BlockState mudState() {
        return defaultBlockState().setValue(VARIANT, Variant.MUD);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum Variant implements StringRepresentable {
        DIRT,
        MUD,
        FLESH,
        FEELER,
        SPORE,
        RED,
        SACKFLESH;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
