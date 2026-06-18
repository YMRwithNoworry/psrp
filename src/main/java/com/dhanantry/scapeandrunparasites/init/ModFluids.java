package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModFluids {
    private static final DeferredRegister<FluidType> FLUID_TYPES =
        DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, SRPMain.MODID);
    private static final DeferredRegister<Fluid> FLUIDS =
        DeferredRegister.create(Registries.FLUID, SRPMain.MODID);

    public static final DeferredHolder<FluidType, FluidType> DEADBLOOD_TYPE = FLUID_TYPES.register(
        "deadblood",
        () -> new FluidType(FluidType.Properties.create()
            .descriptionId("fluid.deadblood")
            .density(3000)
            .viscosity(1500)
            .temperature(310)
            .canDrown(true)
            .canSwim(true)
            .canPushEntity(true)
            .canExtinguish(false)
            .canHydrate(false)
            .supportsBoating(false)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> DEADBLOOD_SOURCE = FLUIDS.register(
        "deadblood",
        () -> new BaseFlowingFluid.Source(deadbloodProperties()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> DEADBLOOD_FLOWING = FLUIDS.register(
        "flowing_deadblood",
        () -> new BaseFlowingFluid.Flowing(deadbloodProperties()));

    private ModFluids() {
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    public static boolean isDeadBlood(FluidState fluidState) {
        Fluid fluid = fluidState.getType();
        return fluid == DEADBLOOD_SOURCE.get() || fluid == DEADBLOOD_FLOWING.get();
    }

    private static BaseFlowingFluid.Properties deadbloodProperties() {
        return new BaseFlowingFluid.Properties(DEADBLOOD_TYPE, DEADBLOOD_SOURCE, DEADBLOOD_FLOWING)
            .block(() -> ModBlocks.DEADBLOOD.get())
            .bucket(() -> ModItems.DEADBLOOD_FLUID.get())
            .slopeFindDistance(4)
            .levelDecreasePerBlock(1)
            .tickRate(5)
            .explosionResistance(100.0F);
    }
}
