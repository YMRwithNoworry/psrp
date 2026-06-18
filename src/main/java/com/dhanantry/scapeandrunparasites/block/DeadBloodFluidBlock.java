package com.dhanantry.scapeandrunparasites.block;

import com.dhanantry.scapeandrunparasites.init.ModBlocks;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class DeadBloodFluidBlock extends LiquidBlock {
    public static final float LEGACY_PARASITE_HEAL = 1.0F;
    public static final double LEGACY_HORIZONTAL_DRAG = 0.85D;
    public static final double LEGACY_FALLING_DRAG = 0.92D;
    public static final float LEGACY_BASE_DAMAGE = 0.1F;
    public static final int LEGACY_HEAD_CORROSIVE_DURATION = 100;
    public static final int LEGACY_HEAD_VIRAL_DURATION = 200;
    public static final int LEGACY_REAPPLY_THRESHOLD = 20;

    public DeadBloodFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            convertNeighboringLiquids(level, pos);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState updated = super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        convertLiquid(level, facingPos, facingState.getFluidState());
        return updated;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (entity instanceof SrpParasiteMob parasite) {
            if (!level.isClientSide) {
                parasite.heal(LEGACY_PARASITE_HEAL);
            }
            return;
        }

        Vec3 motion = entity.getDeltaMovement();
        double y = motion.y < 0.0D ? motion.y * LEGACY_FALLING_DRAG : motion.y;
        entity.setDeltaMovement(motion.x * LEGACY_HORIZONTAL_DRAG, y, motion.z * LEGACY_HORIZONTAL_DRAG);
        entity.resetFallDistance();

        if (!level.isClientSide && entity instanceof LivingEntity living) {
            hurtWithViralScaling(living, LEGACY_BASE_DAMAGE);
            if (isHeadInDeadBlood(level, living)) {
                refreshIfLow(living, ModEffects.CORROSIVE, LEGACY_HEAD_CORROSIVE_DURATION, 0);
                refreshIfLow(living, ModEffects.VIRAL, LEGACY_HEAD_VIRAL_DURATION, 1);
            }
        }
    }

    private static void convertNeighboringLiquids(LevelAccessor level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos targetPos = pos.relative(direction);
            convertLiquid(level, targetPos, level.getFluidState(targetPos));
        }
    }

    private static void convertLiquid(LevelAccessor level, BlockPos pos, FluidState fluidState) {
        if (level instanceof Level concreteLevel && concreteLevel.isClientSide) {
            return;
        }
        if (fluidState.is(FluidTags.WATER)) {
            level.setBlock(pos, ModBlocks.PARASITESTAIN.get().mudState(), 3);
        } else if (fluidState.is(FluidTags.LAVA)) {
            level.setBlock(pos, ModBlocks.PARASITERUBBLE.get().obsidianState(), 3);
        }
    }

    private static boolean isHeadInDeadBlood(Level level, Entity entity) {
        BlockPos eyePos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
        FluidState fluidState = level.getFluidState(eyePos);
        return ModFluids.isDeadBlood(fluidState);
    }

    private static void refreshIfLow(LivingEntity living, net.minecraft.core.Holder<MobEffect> effect, int duration, int amplifier) {
        MobEffectInstance current = living.getEffect(effect);
        if (current == null || current.getDuration() < LEGACY_REAPPLY_THRESHOLD) {
            living.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
        }
    }

    private static void hurtWithViralScaling(LivingEntity living, float baseDamage) {
        if (baseDamage <= 0.0F || living.getHealth() <= 0.0F) {
            return;
        }
        if (living instanceof Player player && player.getAbilities().instabuild) {
            return;
        }

        float damage = baseDamage;
        MobEffectInstance viral = living.getEffect(ModEffects.VIRAL);
        if (viral != null) {
            damage += baseDamage * (viral.getAmplifier() + 1.0F);
        }
        living.hurt(living.damageSources().magic(), damage);
    }
}
