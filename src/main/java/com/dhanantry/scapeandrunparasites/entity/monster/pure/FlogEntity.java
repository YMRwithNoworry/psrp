package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlogEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 60;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.flog.func_78087_a";
    public static final double LEGACY_HEALTH = 20.0D;
    public static final double LEGACY_ARMOR = 7.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 13.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.4D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.274172325D;
    public static final double LEGACY_AOE_INFLATE = 2.0D;
    public static final float LEGACY_WATER_LEAP_POWER = 0.7F;
    public static final double LEGACY_WATER_LEAP_SPEED = 1.5D;
    public static final float LEGACY_SKILL_LEAP_POWER = 1.1F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 3.5D;
    public static final int LEGACY_SKILL_MIN_COOLDOWN = 40;
    public static final int LEGACY_SKILL_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_WINDUP = 10;
    public static final int LEGACY_EVADE_INTERVAL = 20;
    public static final int LEGACY_EVADE_MIN_DISTANCE = 2;
    public static final int LEGACY_EVADE_MAX_DISTANCE = 4;
    public static final double LEGACY_EVADE_SPEED = 1.5D;
    public static final int LEGACY_EVADE_COOLDOWN = 15;
    public static final byte LEGACY_ATTACK_EVENT = 12;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(FlogEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(FlogEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean attackTimerRising;
    private int skillCooldown;
    private int evadeCooldown;

    public FlogEntity(EntityType<? extends FlogEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
            .add(Attributes.MOVEMENT_SPEED, LEGACY_MOVEMENT_SPEED)
            .add(Attributes.KNOCKBACK_RESISTANCE, LEGACY_KNOCKBACK_RESISTANCE)
            .add(Attributes.ATTACK_DAMAGE, LEGACY_ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, LEGACY_FOLLOW_RANGE);
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
        this.goalSelector.addGoal(1, new SkillLeapGoal(this));
        this.goalSelector.addGoal(2, new EvadeDashGoal(this));
        this.goalSelector.addGoal(2, new WaterLeapGoal(this));
        this.goalSelector.addGoal(3, new FlogAoeMeleeGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, (byte) 0);
        builder.define(SKIN, 0);
    }

    @Override
    public void tick() {
        super.tick();
        tickAttackTimer();
        if (!this.level().isClientSide) {
            setBesideClimbableBlock(this.horizontalCollision);
            if (this.skillCooldown > 0) {
                this.skillCooldown--;
            }
            if (this.evadeCooldown > 0) {
                this.evadeCooldown--;
            }
        }
    }

    @Override
    public boolean onClimbable() {
        return isBesideClimbableBlock();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && target instanceof LivingEntity living) {
            applyLegacyVariantEffect(living);
        }
        return hurt;
    }

    public boolean attackEntityAsMobAOE(Entity target) {
        this.attackTimerRising = true;
        this.attackTimer = 0.0F;
        this.level().broadcastEntityEvent(this, LEGACY_ATTACK_EVENT);
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2.0F, 1.0F);

        boolean hurtAny = false;
        AABB area = target.getBoundingBox().inflate(LEGACY_AOE_INFLATE);
        for (LivingEntity candidate : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canAoeHit)) {
            if (this.doHurtTarget(candidate)) {
                hurtAny = true;
            }
        }
        return hurtAny;
    }

    private boolean canAoeHit(LivingEntity candidate) {
        return canHarmLiving(candidate) && this.hasLineOfSight(candidate);
    }

    private void applyLegacyVariantEffect(LivingEntity target) {
        if (getSkin() == 5) {
            SrpMobEffect.applyStackEffect(ModEffects.VIRAL, target, 40, 0);
        } else if (getSkin() == 6) {
            SrpMobEffect.applyStackEffect(ModEffects.BLEED, target, 40, 0);
        }
    }

    private void tickAttackTimer() {
        if (this.attackTimerRising) {
            this.attackTimer += 0.2F;
            if (this.attackTimer > 1.5F) {
                this.attackTimerRising = false;
            }
        } else if (this.attackTimer > 0.0F) {
            this.attackTimer = Math.max(0.0F, this.attackTimer - 0.1F);
        }
    }

    public float getAttackTimer() {
        return this.attackTimer;
    }

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte flags = this.entityData.get(CLIMBING);
        if (climbing) {
            flags = (byte) (flags | 1);
        } else {
            flags = (byte) (flags & -2);
        }
        this.entityData.set(CLIMBING, flags);
    }

    public int getSkin() {
        return this.entityData.get(SKIN);
    }

    public void setSkin(int skin) {
        this.entityData.set(SKIN, skin);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < 0.33D) {
            setSkin(5 + this.random.nextInt(3));
        }
        this.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN + this.random.nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
        return data;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_ATTACK_EVENT) {
            this.attackTimerRising = true;
            this.attackTimer = 0.0F;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SkillCooldown", this.skillCooldown);
        tag.putInt("EvadeCooldown", this.evadeCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
        }
        if (tag.contains("SkillCooldown")) {
            this.skillCooldown = tag.getInt("SkillCooldown");
        }
        if (tag.contains("EvadeCooldown")) {
            this.evadeCooldown = tag.getInt("EvadeCooldown");
        }
    }

    private boolean tryLeapAt(LivingEntity target, float yPower, double speed) {
        Vec3 delta = target.position().subtract(this.position());
        Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            return false;
        }
        Vec3 leap = horizontal.normalize().scale(speed * 0.2D).add(0.0D, yPower, 0.0D);
        this.setDeltaMovement(leap);
        this.hasImpulse = true;
        return true;
    }

    private boolean tryEvadeFrom(LivingEntity target) {
        Vec3 away = this.position().subtract(target.position());
        Vec3 horizontal = new Vec3(away.x, 0.0D, away.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            horizontal = new Vec3(this.random.nextDouble() - 0.5D, 0.0D, this.random.nextDouble() - 0.5D);
        }
        Vec3 dash = horizontal.normalize().scale(LEGACY_EVADE_SPEED * 0.35D).add(0.0D, 0.15D, 0.0D);
        this.setDeltaMovement(dash);
        this.hasImpulse = true;
        this.evadeCooldown = LEGACY_EVADE_COOLDOWN;
        return true;
    }

    private static final class FlogAoeMeleeGoal extends MeleeAttackGoal {
        private final FlogEntity flog;

        private FlogAoeMeleeGoal(FlogEntity flog) {
            super(flog, 1.5D, false);
            this.flog = flog;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.flog.swing(InteractionHand.MAIN_HAND);
                this.flog.attackEntityAsMobAOE(target);
            }
        }
    }

    private static final class WaterLeapGoal extends Goal {
        private final FlogEntity flog;
        private LivingEntity target;

        private WaterLeapGoal(FlogEntity flog) {
            this.flog = flog;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.flog.getTarget();
            return this.target != null
                && this.flog.isInWater()
                && this.flog.distanceToSqr(this.target) < 64.0D
                && this.flog.getRandom().nextInt(20) == 0;
        }

        @Override
        public void start() {
            this.flog.tryLeapAt(this.target, LEGACY_WATER_LEAP_POWER, LEGACY_WATER_LEAP_SPEED);
        }
    }

    private static final class SkillLeapGoal extends Goal {
        private final FlogEntity flog;
        private LivingEntity target;
        private int windup;

        private SkillLeapGoal(FlogEntity flog) {
            this.flog = flog;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.flog.getTarget();
            return this.target != null
                && this.flog.skillCooldown <= 0
                && this.flog.distanceToSqr(this.target) > 9.0D
                && this.flog.distanceToSqr(this.target) < 144.0D
                && this.flog.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_WINDUP;
            this.flog.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.flog.lookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0) {
                this.flog.tryLeapAt(this.target, LEGACY_SKILL_LEAP_POWER, LEGACY_SKILL_LEAP_SPEED);
                this.flog.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN
                    + this.flog.getRandom().nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
            }
        }
    }

    private static final class EvadeDashGoal extends Goal {
        private final FlogEntity flog;
        private LivingEntity target;

        private EvadeDashGoal(FlogEntity flog) {
            this.flog = flog;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.flog.getTarget();
            if (this.target == null || this.flog.evadeCooldown > 0) {
                return false;
            }
            double distance = this.flog.distanceToSqr(this.target);
            return distance >= LEGACY_EVADE_MIN_DISTANCE * LEGACY_EVADE_MIN_DISTANCE
                && distance <= LEGACY_EVADE_MAX_DISTANCE * LEGACY_EVADE_MAX_DISTANCE
                && this.flog.getRandom().nextInt(LEGACY_EVADE_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.flog.tryEvadeFrom(this.target);
        }
    }
}
