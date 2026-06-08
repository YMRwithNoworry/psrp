package com.dhanantry.scapeandrunparasites.entity.monster.derived;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class KirinEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 67;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.kirin.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.kirin.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 410.0D;
    public static final double LEGACY_ARMOR = 30.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 155.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.24D;
    public static final double LEGACY_FOLLOW_RANGE = 80.0D;
    public static final float LEGACY_WIDTH = 2.1271334F;
    public static final float LEGACY_HEIGHT = 7.1F;
    public static final float LEGACY_EYE_HEIGHT = 5.7F;
    public static final int LEGACY_BLINK_CHARGE_TICKS = 60;
    public static final int LEGACY_BLINK_COOLDOWN_TICKS = 200;
    public static final double LEGACY_BLINK_MIN_DISTANCE_SQR = 256.0D;
    public static final int LEGACY_BLINK_MAX_TRIES = 64;
    public static final double LEGACY_BLINK_RADIUS_MIN = 1.5D;
    public static final double LEGACY_BLINK_RADIUS_RANDOM = 22.5D;
    public static final double LEGACY_BLINK_LIFESTEAL_RADIUS = 5.0D;
    public static final float LEGACY_BLINK_LIFESTEAL_FRACTION = 0.5F;
    public static final int LEGACY_FLOAT_GROUND_SCAN = 24;
    public static final double LEGACY_FLOAT_HOVER_HEIGHT = 0.35D;
    public static final double LEGACY_FLOAT_BOB_AMPLITUDE = 0.06D;
    public static final double LEGACY_FLOAT_UP_MAX = 0.16D;
    public static final double LEGACY_FLOAT_DOWN_MAX = -0.16D;
    public static final int LEGACY_RECOVERY_BLINK_RADIUS = 48;
    public static final int LEGACY_RECOVERY_BLINK_VERTICAL = 20;
    public static final int LEGACY_RECOVERY_NO_GROUND_TICKS = 40;

    private static final EntityDataAccessor<Integer> BLINK_X = SynchedEntityData.defineId(KirinEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BLINK_Y = SynchedEntityData.defineId(KirinEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BLINK_Z = SynchedEntityData.defineId(KirinEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BLINK_TICKS = SynchedEntityData.defineId(KirinEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int floatBob;
    private int noGroundTicks;

    public KirinEntity(EntityType<? extends KirinEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 350;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
            .add(Attributes.MOVEMENT_SPEED, LEGACY_MOVEMENT_SPEED)
            .add(Attributes.KNOCKBACK_RESISTANCE, LEGACY_KNOCKBACK_RESISTANCE)
            .add(Attributes.ATTACK_DAMAGE, LEGACY_ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, LEGACY_FOLLOW_RANGE)
            .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    public int getParasiteIDRegister() {
        return LEGACY_PARASITE_ID;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "legacy_model", state -> state.setAndContinue(LEGACY_MODEL_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new KirinBlinkGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
    }

    private boolean canTargetLiving(LivingEntity target) {
        if (target == this || !target.isAlive() || isParasiteAlly(target)) {
            return false;
        }
        return !(target instanceof Player player) || !player.isCreative();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BLINK_X, 0);
        builder.define(BLINK_Y, 0);
        builder.define(BLINK_Z, 0);
        builder.define(BLINK_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            spawnLegacyPortalParticles();
        } else {
            updateFloating();
        }
    }

    public void setBlinkCharge(BlockPos pos, int ticks) {
        BlockPos target = pos == null ? BlockPos.ZERO : pos;
        this.entityData.set(BLINK_X, target.getX());
        this.entityData.set(BLINK_Y, target.getY());
        this.entityData.set(BLINK_Z, target.getZ());
        this.entityData.set(BLINK_TICKS, Math.max(0, ticks));
    }

    public void clearBlinkCharge() {
        setBlinkCharge(BlockPos.ZERO, 0);
    }

    public int getBlinkTicks() {
        return this.entityData.get(BLINK_TICKS);
    }

    public BlockPos getBlinkChargePos() {
        return new BlockPos(this.entityData.get(BLINK_X), this.entityData.get(BLINK_Y), this.entityData.get(BLINK_Z));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("FloatBob", this.floatBob);
        tag.putInt("NoGroundTicks", this.noGroundTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("FloatBob")) {
            this.floatBob = tag.getInt("FloatBob");
        }
        if (tag.contains("NoGroundTicks")) {
            this.noGroundTicks = tag.getInt("NoGroundTicks");
        }
    }

    private void spawnLegacyPortalParticles() {
        for (int i = 0; i < 4; i++) {
            this.level().addParticle(
                ParticleTypes.PORTAL,
                this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 3.0D,
                this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 3.0D,
                (this.random.nextDouble() - 0.5D) * 2.0D,
                -this.random.nextDouble(),
                (this.random.nextDouble() - 0.5D) * 2.0D
            );
        }
    }

    private void updateFloating() {
        this.fallDistance = 0.0F;
        this.floatBob++;
        double bob = Math.sin((this.tickCount + this.floatBob) * 0.12D) * LEGACY_FLOAT_BOB_AMPLITUDE;
        BlockPos base = BlockPos.containing(this.getX(), this.getY() + 0.1D, this.getZ());
        BlockPos ground = null;
        for (int offset = 0; offset <= LEGACY_FLOAT_GROUND_SCAN; offset++) {
            BlockPos check = base.below(offset);
            if (this.level().getBlockState(check).isFaceSturdy(this.level(), check, Direction.UP)) {
                ground = check;
                break;
            }
        }

        if (ground == null) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.0D, this.getDeltaMovement().z);
            this.noGroundTicks++;
            if (this.noGroundTicks >= LEGACY_RECOVERY_NO_GROUND_TICKS && tryBlinkToNearbyLand(LEGACY_RECOVERY_BLINK_RADIUS, LEGACY_RECOVERY_BLINK_VERTICAL)) {
                this.noGroundTicks = 0;
            }
            return;
        }

        this.noGroundTicks = 0;
        double targetY = ground.getY() + 1.0D + LEGACY_FLOAT_HOVER_HEIGHT + bob;
        double delta = targetY - this.getY();
        double impulse = delta > 0.0D ? delta * 0.12D : delta * 0.06D;
        Vec3 motion = this.getDeltaMovement();
        double nextY = Math.max(LEGACY_FLOAT_DOWN_MAX, Math.min(LEGACY_FLOAT_UP_MAX, motion.y + impulse));
        this.setDeltaMovement(motion.x, nextY, motion.z);
    }

    public boolean tryBlinkToNearbyLand(int horizontalRadius, int verticalRadius) {
        BlockPos origin = this.blockPosition();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int dx = -horizontalRadius; dx <= horizontalRadius; dx++) {
            for (int dz = -horizontalRadius; dz <= horizontalRadius; dz++) {
                for (int dy = verticalRadius; dy >= -verticalRadius; dy--) {
                    BlockPos candidate = origin.offset(dx, dy, dz);
                    if (!isRecoverySpotValid(candidate)) {
                        continue;
                    }
                    double distance = candidate.distSqr(origin);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        best = candidate;
                    }
                }
            }
        }

        if (best == null) {
            return false;
        }
        this.teleportTo(best.getX() + 0.5D, best.getY(), best.getZ() + 0.5D);
        return true;
    }

    private boolean isRecoverySpotValid(BlockPos pos) {
        return isSpotValid(pos) && this.level().isEmptyBlock(pos.above()) && this.level().canSeeSky(pos.above());
    }

    private boolean isSpotValid(BlockPos pos) {
        if (!this.level().hasChunkAt(pos)) {
            return false;
        }
        AABB space = new AABB(pos).inflate(0.05D);
        if (!this.level().noCollision(this, space)) {
            return false;
        }
        BlockPos below = pos.below();
        BlockState belowState = this.level().getBlockState(below);
        return belowState.isFaceSturdy(this.level(), below, Direction.UP);
    }

    private static final class KirinBlinkGoal extends Goal {
        private static final int[] LEGACY_VERTICAL_OFFSETS = {0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5, 6, -6};

        private final KirinEntity kirin;
        private BlockPos targetPos;
        private int chargeTicks;
        private int nextAllowedTick;

        private KirinBlinkGoal(KirinEntity kirin) {
            this.kirin = kirin;
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.kirin.tickCount < this.nextAllowedTick || this.kirin.getBlinkTicks() > 0) {
                return false;
            }
            LivingEntity target = this.kirin.getTarget();
            if (target == null || !target.isAlive() || !this.kirin.hasLineOfSight(target) || isIndoors(target)) {
                return false;
            }
            if (this.kirin.distanceToSqr(target) <= LEGACY_BLINK_MIN_DISTANCE_SQR) {
                return false;
            }
            BlockPos pos = findBlinkSpotNear(target);
            if (pos == null) {
                return false;
            }
            this.targetPos = pos;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.chargeTicks > 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.chargeTicks = LEGACY_BLINK_CHARGE_TICKS;
            this.kirin.getNavigation().stop();
            this.kirin.setDeltaMovement(Vec3.ZERO);
            this.kirin.setBlinkCharge(this.targetPos, this.chargeTicks);
            this.kirin.playSound(SoundEvents.ENDERMAN_STARE, 1.0F, 0.9F);
        }

        @Override
        public void tick() {
            if (this.targetPos == null) {
                this.chargeTicks = 0;
                return;
            }
            this.kirin.getNavigation().stop();
            this.kirin.setDeltaMovement(Vec3.ZERO);
            LivingEntity target = this.kirin.getTarget();
            if (target != null) {
                this.kirin.lookAt(target, 30.0F, 30.0F);
            }
            if (!this.kirin.level().isClientSide && this.chargeTicks % 10 == 0) {
                this.kirin.playSound(SoundEvents.PORTAL_TRIGGER, 0.9F, 1.25F);
            }
            this.chargeTicks--;
            this.kirin.setBlinkCharge(this.targetPos, Math.max(0, this.chargeTicks));
            if (!this.kirin.level().isClientSide && this.chargeTicks <= 0) {
                this.kirin.teleportTo(this.targetPos.getX() + 0.5D, this.targetPos.getY(), this.targetPos.getZ() + 0.5D);
                this.kirin.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                doBlinkLifeSteal();
                this.nextAllowedTick = this.kirin.tickCount + LEGACY_BLINK_COOLDOWN_TICKS;
                this.kirin.clearBlinkCharge();
                stop();
            }
        }

        @Override
        public void stop() {
            this.targetPos = null;
            this.chargeTicks = 0;
        }

        private boolean isIndoors(LivingEntity target) {
            BlockPos eye = BlockPos.containing(target.getX(), target.getY() + target.getEyeHeight(), target.getZ());
            for (int i = 0; i < 3; i++) {
                if (this.kirin.level().canSeeSky(eye.above(i))) {
                    return false;
                }
            }
            return true;
        }

        private BlockPos findBlinkSpotNear(LivingEntity target) {
            BlockPos targetBase = target.blockPosition();
            for (int i = 0; i < LEGACY_BLINK_MAX_TRIES; i++) {
                double radius = LEGACY_BLINK_RADIUS_MIN + this.kirin.getRandom().nextDouble() * LEGACY_BLINK_RADIUS_RANDOM;
                double angle = this.kirin.getRandom().nextDouble() * Math.PI * 2.0D;
                int x = (int) Math.floor(targetBase.getX() + 0.5D + radius * Math.cos(angle));
                int z = (int) Math.floor(targetBase.getZ() + 0.5D + radius * Math.sin(angle));
                for (int offset : LEGACY_VERTICAL_OFFSETS) {
                    BlockPos candidate = new BlockPos(x, targetBase.getY() + offset, z);
                    if (!this.kirin.isSpotValid(candidate) || !this.kirin.level().canSeeSky(candidate.above()) || !hasLineOfSight(target, candidate)) {
                        continue;
                    }
                    return candidate;
                }
            }
            return null;
        }

        private boolean hasLineOfSight(LivingEntity target, BlockPos pos) {
            Vec3 from = new Vec3(this.kirin.getX(), this.kirin.getEyeY(), this.kirin.getZ());
            Vec3 to = Vec3.atCenterOf(pos);
            HitResult hit = this.kirin.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.kirin));
            return hit.getType() != HitResult.Type.BLOCK && this.kirin.hasLineOfSight(target);
        }

        private void doBlinkLifeSteal() {
            AABB area = this.kirin.getBoundingBox().inflate(LEGACY_BLINK_LIFESTEAL_RADIUS);
            List<LivingEntity> victims = this.kirin.level().getEntitiesOfClass(LivingEntity.class, area, this::canStealFrom);
            if (victims.isEmpty()) {
                this.kirin.playSound(SoundEvents.ENDERMAN_HURT, 0.7F, 0.9F);
                return;
            }

            LivingEntity victim = victims.get(0);
            float health = victim.getHealth();
            if (health <= 0.0F) {
                return;
            }
            float stolen = health * LEGACY_BLINK_LIFESTEAL_FRACTION;
            victim.setHealth(Math.max(0.0F, health - stolen));
            this.kirin.heal(stolen);
            this.kirin.playSound(SoundEvents.WITHER_HURT, 1.0F, 0.8F + this.kirin.getRandom().nextFloat() * 0.4F);
        }

        private boolean canStealFrom(LivingEntity target) {
            return target != null && target != this.kirin && target.isAlive() && !this.kirin.isParasiteAlly(target);
        }
    }
}
