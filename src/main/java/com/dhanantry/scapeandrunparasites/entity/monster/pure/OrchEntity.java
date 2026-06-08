package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.entity.projectile.WebballEntity;
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

public class OrchEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 84;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.orch.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.orch.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 75.0D;
    public static final double LEGACY_ARMOR = 10.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 25.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.2775D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.901F;
    public static final float LEGACY_HEIGHT = 4.1F;
    public static final float LEGACY_EYE_HEIGHT = 3.5F;
    public static final int LEGACY_XP = 75;
    public static final double LEGACY_AOE_INFLATE = 2.0D;
    public static final double LEGACY_AOE_MELEE_SPEED = 1.3D;
    public static final float LEGACY_WATER_LEAP_POWER = 0.7F;
    public static final double LEGACY_WATER_LEAP_SPEED = 1.5D;
    public static final int LEGACY_WATER_LEAP_MIN_DISTANCE = 3;
    public static final int LEGACY_WATER_LEAP_INTERVAL = 20;
    public static final int LEGACY_WATER_LEAP_STATUS = 7;
    public static final int LEGACY_PROJECTILE_COOLDOWN = 40;
    public static final int LEGACY_PROJECTILE_INTERVAL = 15;
    public static final int LEGACY_PROJECTILE_COUNT = 4;
    public static final double LEGACY_PROJECTILE_RANGE_SQR = 4225.0D;
    public static final float LEGACY_SKILL_LEAP_POWER = 0.5F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 3.5D;
    public static final int LEGACY_SKILL_RADIUS = 4;
    public static final int LEGACY_SKILL_MIN_COOLDOWN = 40;
    public static final int LEGACY_SKILL_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_WINDUP = 10;
    public static final int LEGACY_EVADE_INTERVAL = 17;
    public static final int LEGACY_EVADE_MIN_DISTANCE = 2;
    public static final int LEGACY_EVADE_MAX_DISTANCE = 5;
    public static final double LEGACY_EVADE_SPEED = 3.5D;
    public static final int LEGACY_EVADE_COOLDOWN = 15;
    public static final double LEGACY_HIT_VERTICAL_BONUS = 0.5000000059604645D;
    public static final int LEGACY_VARIANT_SKIN_WEAK = 1;
    public static final int LEGACY_VARIANT_SKIN_HEAVY = 7;
    public static final double LEGACY_WEAK_HEALTH_MULTIPLIER = 0.5D;
    public static final double LEGACY_WEAK_DAMAGE_MULTIPLIER = 1.5D;
    public static final byte LEGACY_ATTACK_EVENT = 12;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(OrchEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(OrchEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean attackTimerRising;
    private int skillCooldown;
    private int evadeCooldown;

    public OrchEntity(EntityType<? extends OrchEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = LEGACY_XP;
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
        this.goalSelector.addGoal(1, new SkillLeapGoal(this));
        this.goalSelector.addGoal(2, new EvadeDashGoal(this));
        this.goalSelector.addGoal(2, new WaterLeapGoal(this));
        this.goalSelector.addGoal(3, new OrchAoeMeleeGoal(this));
        this.goalSelector.addGoal(4, new WebballVolleyGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
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
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt) {
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, LEGACY_HIT_VERTICAL_BONUS, 0.0D));
            target.hasImpulse = true;
        }
        return hurt;
    }

    public boolean attackEntityAsMobAOE(Entity target) {
        this.attackTimerRising = true;
        this.attackTimer = 0.0F;
        this.level().broadcastEntityEvent(this, LEGACY_ATTACK_EVENT);
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2.0F, 0.85F);

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
        if (candidate == this || !candidate.isAlive() || isParasiteAlly(candidate)) {
            return false;
        }
        return this.hasLineOfSight(candidate);
    }

    private boolean canTargetLiving(LivingEntity target) {
        if (target == this || !target.isAlive() || isParasiteAlly(target)) {
            return false;
        }
        return !(target instanceof Player player) || !player.isCreative();
    }

    private void tickAttackTimer() {
        if (this.attackTimerRising) {
            this.attackTimer += 0.2F;
            if (this.attackTimer > 1.0F) {
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
            if (this.random.nextInt(2) == 0) {
                setSkin(LEGACY_VARIANT_SKIN_WEAK);
                applyWeakVariantAttributes(true);
            } else {
                setSkin(LEGACY_VARIANT_SKIN_HEAVY);
            }
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
            if (getSkin() == LEGACY_VARIANT_SKIN_WEAK) {
                applyWeakVariantAttributes(false);
            }
        }
        if (tag.contains("SkillCooldown")) {
            this.skillCooldown = tag.getInt("SkillCooldown");
        }
        if (tag.contains("EvadeCooldown")) {
            this.evadeCooldown = tag.getInt("EvadeCooldown");
        }
    }

    private void applyWeakVariantAttributes(boolean refillHealth) {
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(LEGACY_HEALTH * LEGACY_WEAK_HEALTH_MULTIPLIER);
            if (refillHealth) {
                this.setHealth(this.getMaxHealth());
            }
        }
        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(LEGACY_ATTACK_DAMAGE * LEGACY_WEAK_DAMAGE_MULTIPLIER);
        }
    }

    private boolean tryLeapAt(LivingEntity target, float yPower, double speed) {
        Vec3 delta = target.position().subtract(this.position());
        Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            return false;
        }
        Vec3 leap = horizontal.normalize().scale(speed * 0.35D).add(0.0D, yPower, 0.0D);
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

    private void shootWebballAt(LivingEntity target) {
        Vec3 look = this.getLookAngle();
        double xPower = target.getX() - (this.getX() + look.x);
        double yPower = target.getBoundingBox().minY + target.getBbHeight() / 3.0D - (this.getY() + 1.0D + this.getBbHeight() / 2.0D);
        double zPower = target.getZ() - (this.getZ() + look.z);
        WebballEntity webball = new WebballEntity(this.level(), this, xPower, yPower, zPower, WebballEntity.LEGACY_TYPE_TWO);
        webball.setPos(this.getX() + look.x, this.getY() + getEyeHeight() - 0.2D, this.getZ() + look.z);
        this.level().addFreshEntity(webball);
        this.playSound(SoundEvents.SPIDER_AMBIENT, 2.0F, 1.0F);
    }

    private static final class OrchAoeMeleeGoal extends MeleeAttackGoal {
        private final OrchEntity orch;

        private OrchAoeMeleeGoal(OrchEntity orch) {
            super(orch, LEGACY_AOE_MELEE_SPEED, false);
            this.orch = orch;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.orch.swing(InteractionHand.MAIN_HAND);
                this.orch.attackEntityAsMobAOE(target);
            }
        }
    }

    private static final class WebballVolleyGoal extends Goal {
        private final OrchEntity orch;
        private int attackTimer;
        private int shotsFired;

        private WebballVolleyGoal(OrchEntity orch) {
            this.orch = orch;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.orch.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.orch.getTarget();
            return target != null && target.isAlive() && this.orch.distanceToSqr(target) < LEGACY_PROJECTILE_RANGE_SQR;
        }

        @Override
        public void stop() {
            this.attackTimer = 0;
            this.shotsFired = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.orch.getTarget();
            if (target == null || !target.isAlive()) {
                this.attackTimer = 0;
                return;
            }
            this.orch.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.orch.distanceToSqr(target) >= LEGACY_PROJECTILE_RANGE_SQR || !this.orch.hasLineOfSight(target)) {
                this.attackTimer = Math.max(0, this.attackTimer - 1);
                return;
            }
            this.attackTimer++;
            if (this.attackTimer == LEGACY_PROJECTILE_COOLDOWN - 10) {
                this.orch.playSound(SoundEvents.SPIDER_AMBIENT, 1.2F, 0.7F);
            }
            if (this.attackTimer > LEGACY_PROJECTILE_COOLDOWN && this.attackTimer % LEGACY_PROJECTILE_INTERVAL == 0) {
                if (this.shotsFired < LEGACY_PROJECTILE_COUNT) {
                    this.orch.shootWebballAt(target);
                    this.shotsFired++;
                } else {
                    this.attackTimer = 0;
                    this.shotsFired = 0;
                }
            }
        }
    }

    private static final class WaterLeapGoal extends Goal {
        private final OrchEntity orch;
        private LivingEntity target;

        private WaterLeapGoal(OrchEntity orch) {
            this.orch = orch;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.orch.getTarget();
            return this.target != null
                && this.orch.isInWater()
                && this.orch.distanceToSqr(this.target) > LEGACY_WATER_LEAP_MIN_DISTANCE * LEGACY_WATER_LEAP_MIN_DISTANCE
                && this.orch.distanceToSqr(this.target) < 64.0D
                && this.orch.getRandom().nextInt(LEGACY_WATER_LEAP_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.orch.tryLeapAt(this.target, LEGACY_WATER_LEAP_POWER, LEGACY_WATER_LEAP_SPEED);
        }
    }

    private static final class SkillLeapGoal extends Goal {
        private final OrchEntity orch;
        private LivingEntity target;
        private Vec3 targetSpot;
        private int windup;

        private SkillLeapGoal(OrchEntity orch) {
            this.orch = orch;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.orch.getTarget();
            return this.target != null
                && this.orch.skillCooldown <= 0
                && this.orch.onGround()
                && !this.orch.isInWater()
                && this.orch.distanceToSqr(this.target) > 9.0D
                && this.orch.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_WINDUP;
            this.targetSpot = this.target.position();
            this.orch.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.orch.lookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0 && this.targetSpot != null) {
                Vec3 delta = this.targetSpot.subtract(this.orch.position());
                Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
                if (horizontal.horizontalDistanceSqr() > 1.0E-4D) {
                    Vec3 leap = horizontal.normalize().scale(LEGACY_SKILL_LEAP_SPEED * 0.9D).add(0.0D, LEGACY_SKILL_LEAP_POWER, 0.0D);
                    this.orch.setDeltaMovement(leap);
                    this.orch.hasImpulse = true;
                }
                this.orch.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN
                    + this.orch.getRandom().nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
            }
        }

        @Override
        public void stop() {
            this.windup = 0;
            this.targetSpot = null;
        }
    }

    private static final class EvadeDashGoal extends Goal {
        private final OrchEntity orch;
        private LivingEntity target;

        private EvadeDashGoal(OrchEntity orch) {
            this.orch = orch;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.orch.getTarget();
            if (this.target == null || this.orch.evadeCooldown > 0) {
                return false;
            }
            double distance = this.orch.distanceToSqr(this.target);
            return distance >= LEGACY_EVADE_MIN_DISTANCE * LEGACY_EVADE_MIN_DISTANCE
                && distance <= LEGACY_EVADE_MAX_DISTANCE * LEGACY_EVADE_MAX_DISTANCE
                && this.orch.getRandom().nextInt(LEGACY_EVADE_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.orch.tryEvadeFrom(this.target);
        }
    }
}
