package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
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

public class EsorEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 50;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.esor.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.esor.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 120.0D;
    public static final double LEGACY_ARMOR = 20.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 40.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.255D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.901F;
    public static final float LEGACY_HEIGHT = 4.2F;
    public static final float LEGACY_EYE_HEIGHT = 3.5F;
    public static final int LEGACY_TYPE = 50;
    public static final double LEGACY_AOE_MELEE_SPEED = 1.3D;
    public static final double LEGACY_MELEE_REACH = 8.0D;
    public static final double LEGACY_AOE_INFLATE = 5.0D;
    public static final float LEGACY_WATER_LEAP_POWER = 0.7F;
    public static final double LEGACY_WATER_LEAP_SPEED = 1.5D;
    public static final int LEGACY_WATER_LEAP_STATUS = 7;
    public static final int LEGACY_WATER_LEAP_INTERVAL = 20;
    public static final int LEGACY_SKILL_LEAP_COOLDOWN = 100;
    public static final int LEGACY_SKILL_LEAP_WINDUP = 10;
    public static final float LEGACY_SKILL_LEAP_POWER = 1.2F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 2.5D;
    public static final int LEGACY_SKILL_LEAP_STATUS = 7;
    public static final int LEGACY_SMASH_COOLDOWN = 100;
    public static final int LEGACY_SMASH_TRIGGER_RANGE = 7;
    public static final int LEGACY_SMASH_WARMUP_TICKS = 20;
    public static final int LEGACY_SMASH_FINISH_TICKS = 100;
    public static final int LEGACY_SMASH_WARMUP_STATUS = 25;
    public static final int LEGACY_SMASH_ACTIVE_STATUS = 3;
    public static final double LEGACY_SMASH_DAMAGE_RADIUS = 6.0D;
    public static final double LEGACY_SMASH_DAMAGE_Y_RADIUS = 3.0D;
    public static final double LEGACY_SMASH_RAGE_RADIUS = 24.0D;
    public static final double LEGACY_SMASH_RAGE_Y_RADIUS = 5.0D;
    public static final int LEGACY_SMASH_RAGE_TICKS = 1200;
    public static final int LEGACY_SMASH_RAGE_AMPLIFIER = 1;
    public static final int LEGACY_SMASH_SLOW_TICKS = 110;
    public static final int LEGACY_SMASH_SLOW_AMPLIFIER = 100;
    public static final int LEGACY_EVADE_INTERVAL = 50;
    public static final int LEGACY_EVADE_MAX_DISTANCE = 10;
    public static final double LEGACY_EVADE_SPEED = 4.0D;
    public static final int LEGACY_EVADE_COOLDOWN = 15;
    public static final double LEGACY_HIT_VERTICAL_BONUS = 0.5000000059604645D;
    public static final double LEGACY_TENDRIL_HEALTH_FRACTION = 0.5D;
    public static final int LEGACY_HEAVY_SKIN = 7;
    public static final byte LEGACY_LEFT_TENDRIL_DEAD_EVENT = 11;
    public static final byte LEGACY_ATTACK_EVENT = 12;
    public static final byte LEGACY_RIGHT_TENDRIL_DEAD_EVENT = 22;
    public static final byte LEGACY_FLAME_EVENT = 100;

    private static final net.minecraft.network.syncher.EntityDataAccessor<Byte> CLIMBING =
        net.minecraft.network.syncher.SynchedEntityData.defineId(EsorEntity.class, net.minecraft.network.syncher.EntityDataSerializers.BYTE);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> SKIN =
        net.minecraft.network.syncher.SynchedEntityData.defineId(EsorEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> PARASITE_STATUS =
        net.minecraft.network.syncher.SynchedEntityData.defineId(EsorEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean attackTimerRising;
    private float leftTendrilHealth;
    private float rightTendrilHealth;
    private int skillLeapCooldown;
    private int smashCooldown;
    private int evadeCooldown;
    private int smashBorder;
    private boolean smashFinished = true;

    public EsorEntity(EntityType<? extends EsorEntity> entityType, Level level) {
        super(entityType, level);
        this.leftTendrilHealth = (float) (LEGACY_HEALTH * LEGACY_TENDRIL_HEALTH_FRACTION);
        this.rightTendrilHealth = (float) (LEGACY_HEALTH * LEGACY_TENDRIL_HEALTH_FRACTION);
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
        this.goalSelector.addGoal(1, new SmashGoal(this));
        this.goalSelector.addGoal(2, new SkillLeapGoal(this));
        this.goalSelector.addGoal(3, new WaterLeapGoal(this));
        this.goalSelector.addGoal(4, new EsorAoeMeleeGoal(this));
        this.goalSelector.addGoal(5, new EvadeDashGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetEsorLiving));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, (byte) 0);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < 0.33D) {
            setSkin(LEGACY_HEAVY_SKIN);
        }
        return data;
    }

    @Override
    public void tick() {
        super.tick();
        tickAttackTimer();
        if (!this.level().isClientSide) {
            setBesideClimbableBlock(this.horizontalCollision);
            if (this.skillLeapCooldown > 0) {
                this.skillLeapCooldown--;
            }
            if (this.smashCooldown > 0) {
                this.smashCooldown--;
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
        this.playSound(ModSounds.STEP_HEAVY.get(), 0.15F, 1.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
        if (hurt) {
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, LEGACY_HIT_VERTICAL_BONUS, 0.0D));
            target.hasImpulse = true;
        }
        return hurt;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_LEFT_TENDRIL_DEAD_EVENT) {
            this.leftTendrilHealth = 0.0F;
        } else if (id == LEGACY_ATTACK_EVENT) {
            this.attackTimerRising = true;
            this.attackTimer = 0.0F;
        } else if (id == LEGACY_RIGHT_TENDRIL_DEAD_EVENT) {
            this.rightTendrilHealth = 0.0F;
        } else if (id == LEGACY_FLAME_EVENT) {
            spawnFlameParticles();
            spawnFlameParticles();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.ESOR_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.ESOR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ESOR_DEATH.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putFloat("SrpAttackTimer", this.attackTimer);
        tag.putBoolean("SrpAttackUp", this.attackTimerRising);
        tag.putFloat("parasiteleftTendril", this.leftTendrilHealth);
        tag.putFloat("parasiterightTendril", this.rightTendrilHealth);
        tag.putInt("SrpSkillLeapCooldown", this.skillLeapCooldown);
        tag.putInt("SrpSmashCooldown", this.smashCooldown);
        tag.putInt("SrpEvadeCooldown", this.evadeCooldown);
        tag.putInt("SrpSmashBorder", this.smashBorder);
        tag.putBoolean("SrpSmashFinished", this.smashFinished);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpAttackTimer")) {
            this.attackTimer = tag.getFloat("SrpAttackTimer");
        }
        if (tag.contains("SrpAttackUp")) {
            this.attackTimerRising = tag.getBoolean("SrpAttackUp");
        }
        if (tag.contains("parasiteleftTendril")) {
            this.leftTendrilHealth = tag.getFloat("parasiteleftTendril");
            if (this.leftTendrilHealth <= 0.0F) {
                this.level().broadcastEntityEvent(this, LEGACY_LEFT_TENDRIL_DEAD_EVENT);
            }
        }
        if (tag.contains("parasiterightTendril")) {
            this.rightTendrilHealth = tag.getFloat("parasiterightTendril");
            if (this.rightTendrilHealth <= 0.0F) {
                this.level().broadcastEntityEvent(this, LEGACY_RIGHT_TENDRIL_DEAD_EVENT);
            }
        }
        if (tag.contains("SrpSkillLeapCooldown")) {
            this.skillLeapCooldown = tag.getInt("SrpSkillLeapCooldown");
        }
        if (tag.contains("SrpSmashCooldown")) {
            this.smashCooldown = tag.getInt("SrpSmashCooldown");
        }
        if (tag.contains("SrpEvadeCooldown")) {
            this.evadeCooldown = tag.getInt("SrpEvadeCooldown");
        }
        if (tag.contains("SrpSmashBorder")) {
            this.smashBorder = tag.getInt("SrpSmashBorder");
        }
        if (tag.contains("SrpSmashFinished")) {
            this.smashFinished = tag.getBoolean("SrpSmashFinished");
        }
    }

    public boolean attackEntityAsMobAOE(Entity target) {
        this.attackTimerRising = true;
        this.attackTimer = 0.0F;
        this.level().broadcastEntityEvent(this, LEGACY_ATTACK_EVENT);
        this.playSound(ModSounds.MOB_SWIPE.get(), 2.0F, 1.0F);
        boolean hurtAny = false;
        AABB area = target.getBoundingBox().inflate(LEGACY_AOE_INFLATE);
        for (LivingEntity candidate : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canAoeHit)) {
            if (doHurtTarget(candidate)) {
                hurtAny = true;
            }
        }
        return hurtAny;
    }

    public int getSkin() {
        return this.entityData.get(SKIN);
    }

    public void setSkin(int skin) {
        this.entityData.set(SKIN, skin);
    }

    public int getParasiteStatus() {
        return this.entityData.get(PARASITE_STATUS);
    }

    public void setParasiteStatus(int status) {
        this.entityData.set(PARASITE_STATUS, status);
    }

    public float getAttackTimer() {
        return this.attackTimer;
    }

    public float getLeft() {
        return this.leftTendrilHealth;
    }

    public float getRight() {
        return this.rightTendrilHealth;
    }

    public float getSelfeFlashIntensity(float partialTicks) {
        return Mth.clamp(this.attackTimer / 1.5F, 0.0F, 1.0F);
    }

    public boolean getFinished(byte skill) {
        return skill == 1 ? this.smashFinished : true;
    }

    public void setFinished(byte skill, boolean finished) {
        if (skill == 1) {
            this.smashFinished = finished;
        }
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

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    private boolean canAoeHit(LivingEntity candidate) {
        return canHarmLiving(candidate) && hasLineOfSight(candidate);
    }

    private boolean canTargetEsorLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
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

    private boolean tryLeapAt(LivingEntity target, float yPower, double speed) {
        Vec3 delta = target.position().subtract(position());
        Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            return false;
        }
        setDeltaMovement(horizontal.normalize().scale(speed * 0.35D).add(0.0D, yPower, 0.0D));
        this.hasImpulse = true;
        return true;
    }

    private boolean tryEvadeFrom(LivingEntity target) {
        Vec3 away = position().subtract(target.position());
        Vec3 horizontal = new Vec3(away.x, 0.0D, away.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            horizontal = new Vec3(this.random.nextDouble() - 0.5D, 0.0D, this.random.nextDouble() - 0.5D);
        }
        setDeltaMovement(horizontal.normalize().scale(LEGACY_EVADE_SPEED * 0.35D).add(0.0D, 0.15D, 0.0D));
        this.hasImpulse = true;
        this.evadeCooldown = LEGACY_EVADE_COOLDOWN;
        return true;
    }

    private void smash() {
        if (!onGround()) {
            finishSmash();
            return;
        }
        if (this.smashBorder == 2) {
            playSound(getHurtSound(damageSources().generic()), 4.0F, 2.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        }
        if (this.smashBorder < LEGACY_SMASH_WARMUP_TICKS) {
            this.level().broadcastEntityEvent(this, LEGACY_FLAME_EVENT);
            setParasiteStatus(LEGACY_SMASH_WARMUP_STATUS);
            getNavigation().stop();
            addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LEGACY_SMASH_SLOW_TICKS, LEGACY_SMASH_SLOW_AMPLIFIER, false, false));
        }
        this.smashBorder++;
        if (this.smashBorder >= LEGACY_SMASH_WARMUP_TICKS) {
            setParasiteStatus(LEGACY_SMASH_ACTIVE_STATUS);
            if (this.smashBorder % 7 == 0) {
                this.playSound(ModSounds.MOB_SWIPE.get(), 5.0F, 1.0F);
            }
            damageSmashArea();
        }
        if (this.smashBorder > LEGACY_SMASH_FINISH_TICKS) {
            applyRageToNearbyParasites();
            finishSmash();
        }
    }

    private void damageSmashArea() {
        for (LivingEntity living : this.level().getEntitiesOfClass(
            LivingEntity.class,
            getBoundingBox().inflate(LEGACY_SMASH_DAMAGE_RADIUS, LEGACY_SMASH_DAMAGE_Y_RADIUS, LEGACY_SMASH_DAMAGE_RADIUS),
            this::canAoeHit
        )) {
            if (doHurtTarget(living)) {
                Vec3 delta = living.position().subtract(position());
                Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
                if (horizontal.horizontalDistanceSqr() > 1.0E-4D) {
                    Vec3 push = horizontal.normalize().scale(0.6D);
                    living.setDeltaMovement(living.getDeltaMovement().add(push.x, 0.35D, push.z));
                    living.hasImpulse = true;
                }
            }
        }
    }

    private void applyRageToNearbyParasites() {
        if (!SrpConfig.RAGE_ENABLE.get()) {
            return;
        }
        for (SrpParasiteMob parasite : this.level().getEntitiesOfClass(
            SrpParasiteMob.class,
            getBoundingBox().inflate(LEGACY_SMASH_RAGE_RADIUS, LEGACY_SMASH_RAGE_Y_RADIUS, LEGACY_SMASH_RAGE_RADIUS),
            parasite -> parasite != this && hasLineOfSight(parasite)
        )) {
            parasite.addEffect(new MobEffectInstance(ModEffects.RAGE, LEGACY_SMASH_RAGE_TICKS, LEGACY_SMASH_RAGE_AMPLIFIER, false, false), this);
        }
    }

    private void finishSmash() {
        this.smashFinished = true;
        this.smashBorder = 0;
        this.smashCooldown = LEGACY_SMASH_COOLDOWN;
        setParasiteStatus(0);
    }

    private void spawnFlameParticles() {
        for (int i = 0; i < 8; i++) {
            this.level().addParticle(
                ParticleTypes.FLAME,
                getRandomX(0.5D),
                getRandomY(),
                getRandomZ(0.5D),
                (this.random.nextDouble() - 0.5D) * 0.05D,
                this.random.nextDouble() * 0.05D,
                (this.random.nextDouble() - 0.5D) * 0.05D
            );
        }
    }

    private static final class EsorAoeMeleeGoal extends MeleeAttackGoal {
        private final EsorEntity esor;

        private EsorAoeMeleeGoal(EsorEntity esor) {
            super(esor, LEGACY_AOE_MELEE_SPEED, false);
            this.esor = esor;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)
                || this.esor.distanceToSqr(target) <= LEGACY_MELEE_REACH * LEGACY_MELEE_REACH && this.esor.hasLineOfSight(target)) {
                resetAttackCooldown();
                this.esor.swing(InteractionHand.MAIN_HAND);
                this.esor.attackEntityAsMobAOE(target);
            }
        }
    }

    private static final class SkillLeapGoal extends Goal {
        private final EsorEntity esor;
        private LivingEntity target;
        private int windup;

        private SkillLeapGoal(EsorEntity esor) {
            this.esor = esor;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.esor.getTarget();
            return this.target != null
                && this.esor.skillLeapCooldown <= 0
                && this.esor.distanceToSqr(this.target) > 9.0D
                && this.esor.distanceToSqr(this.target) < 100.0D
                && this.esor.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_LEAP_WINDUP;
            this.esor.getNavigation().stop();
            this.esor.setParasiteStatus(LEGACY_SKILL_LEAP_STATUS);
        }

        @Override
        public void tick() {
            this.esor.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0) {
                this.esor.tryLeapAt(this.target, LEGACY_SKILL_LEAP_POWER, LEGACY_SKILL_LEAP_SPEED);
                this.esor.skillLeapCooldown = LEGACY_SKILL_LEAP_COOLDOWN;
                this.esor.setParasiteStatus(0);
            }
        }
    }

    private static final class WaterLeapGoal extends Goal {
        private final EsorEntity esor;
        private LivingEntity target;

        private WaterLeapGoal(EsorEntity esor) {
            this.esor = esor;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.esor.getTarget();
            return this.target != null
                && this.esor.isInWater()
                && this.esor.distanceToSqr(this.target) < 64.0D
                && this.esor.getRandom().nextInt(LEGACY_WATER_LEAP_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.esor.setParasiteStatus(LEGACY_WATER_LEAP_STATUS);
            this.esor.tryLeapAt(this.target, LEGACY_WATER_LEAP_POWER, LEGACY_WATER_LEAP_SPEED);
        }
    }

    private static final class EvadeDashGoal extends Goal {
        private final EsorEntity esor;
        private LivingEntity target;

        private EvadeDashGoal(EsorEntity esor) {
            this.esor = esor;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.esor.getTarget();
            return this.target != null
                && this.esor.evadeCooldown <= 0
                && this.esor.distanceToSqr(this.target) <= LEGACY_EVADE_MAX_DISTANCE * LEGACY_EVADE_MAX_DISTANCE
                && this.esor.getRandom().nextInt(LEGACY_EVADE_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.esor.tryEvadeFrom(this.target);
        }
    }

    private static final class SmashGoal extends Goal {
        private final EsorEntity esor;

        private SmashGoal(EsorEntity esor) {
            this.esor = esor;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.esor.getTarget();
            return target != null
                && target.isAlive()
                && this.esor.smashCooldown <= 0
                && this.esor.onGround()
                && this.esor.distanceToSqr(target) <= LEGACY_SMASH_TRIGGER_RANGE * LEGACY_SMASH_TRIGGER_RANGE
                && this.esor.hasLineOfSight(target)
                && this.esor.getFinished((byte) 1);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.esor.getFinished((byte) 1);
        }

        @Override
        public void start() {
            this.esor.setFinished((byte) 1, false);
            this.esor.smashBorder = 0;
        }

        @Override
        public void stop() {
            if (!this.esor.getFinished((byte) 1)) {
                this.esor.finishSmash();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.esor.getTarget();
            if (target != null) {
                this.esor.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            this.esor.smash();
        }
    }
}
