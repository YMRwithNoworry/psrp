package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GanroEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 33;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.ganro.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.ganro.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 80.0D;
    public static final double LEGACY_ARMOR = 15.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 25.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.27D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.901F;
    public static final float LEGACY_HEIGHT = 4.2F;
    public static final float LEGACY_EYE_HEIGHT = 3.5F;
    public static final int LEGACY_XP = 150;
    public static final int LEGACY_TYPE = 40;
    public static final double LEGACY_AOE_MELEE_SPEED = 1.3D;
    public static final double LEGACY_MELEE_REACH = 8.0D;
    public static final double LEGACY_AOE_INFLATE = 2.0D;
    public static final float LEGACY_WATER_LEAP_POWER = 0.7F;
    public static final double LEGACY_WATER_LEAP_SPEED = 1.5D;
    public static final int LEGACY_WATER_LEAP_STATUS = 3;
    public static final int LEGACY_WATER_LEAP_INTERVAL = 20;
    public static final int LEGACY_SKILL_LEAP_MIN_COOLDOWN = 80;
    public static final int LEGACY_SKILL_LEAP_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_LEAP_WINDUP = 10;
    public static final float LEGACY_SKILL_LEAP_POWER = 1.2F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 2.5D;
    public static final int LEGACY_CHARGE_STATUS = 3;
    public static final int LEGACY_CHARGE_COOLDOWN_TICKS = 40;
    public static final int LEGACY_CHARGE_TRIGGER_RANGE = 22;
    public static final int LEGACY_CHARGE_WINDUP_TICKS = 20;
    public static final int LEGACY_CHARGE_STUCK_CANCEL_TICKS = 60;
    public static final double LEGACY_CHARGE_TARGET_DISTANCE = 15.0D;
    public static final double LEGACY_CHARGE_SPEED = 3.0D;
    public static final double LEGACY_CHARGE_AOE_INFLATE = 2.0D;
    public static final double LEGACY_CHARGE_DAMAGE_MULTIPLIER = 0.5D;
    public static final int LEGACY_SHOCKWAVE_STATUS = 100;
    public static final int LEGACY_SHOCKWAVE_COOLDOWN_TICKS = 40;
    public static final int LEGACY_SHOCKWAVE_TRIGGER_RANGE = 32;
    public static final int LEGACY_SHOCKWAVE_FIRST_BORDER = 2;
    public static final int LEGACY_SHOCKWAVE_FINISH_BORDER = 3;
    public static final double LEGACY_SHOCKWAVE_DAMAGE_MULTIPLIER = 0.3D;
    public static final float LEGACY_MIN_DAMAGE = 2.0F;
    public static final int LEGACY_EVADE_INTERVAL = 20;
    public static final int LEGACY_EVADE_MIN_DISTANCE = 2;
    public static final int LEGACY_EVADE_MAX_DISTANCE = 4;
    public static final double LEGACY_EVADE_SPEED = 3.0D;
    public static final int LEGACY_EVADE_COOLDOWN = 15;
    public static final double LEGACY_LAUNCH_HORIZONTAL = 0.4D;
    public static final double LEGACY_LAUNCH_Y = 1.05D;
    public static final double LEGACY_PLAYER_LAUNCH_Y = 0.525D;
    public static final float LEGACY_ATTACK_TIMER_UP_STEP = 0.2F;
    public static final float LEGACY_ATTACK_TIMER_DOWN_STEP = 0.1F;
    public static final int LEGACY_HEAVY_SKIN = 7;
    public static final byte LEGACY_ATTACK_EVENT = 12;
    public static final byte LEGACY_FLAME_EVENT = 100;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(GanroEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(GanroEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(GanroEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean attackTimerRising;
    private int skillLeapCooldown;
    private int chargeCooldown;
    private int shockwaveCooldown;
    private int evadeCooldown;
    private int chargeTicks;
    private int shockwaveBorder;
    private boolean skillCharge = true;
    private boolean skillShockwave = true;
    private Vec3 chargeTarget;

    public GanroEntity(EntityType<? extends GanroEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(1, new ChargeGoal(this));
        this.goalSelector.addGoal(2, new ShockwaveGoal(this));
        this.goalSelector.addGoal(3, new SkillLeapGoal(this));
        this.goalSelector.addGoal(4, new GanroAoeMeleeGoal(this));
        this.goalSelector.addGoal(5, new WaterLeapGoal(this));
        this.goalSelector.addGoal(6, new EvadeDashGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetGanroLiving));
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
        builder.define(PARASITE_STATUS, 0);
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
            if (this.chargeCooldown > 0) {
                this.chargeCooldown--;
            }
            if (this.shockwaveCooldown > 0) {
                this.shockwaveCooldown--;
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
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 0.8F);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
        if (hurt) {
            maybeLaunchTarget(target);
        }
        return hurt;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_ATTACK_EVENT) {
            this.attackTimerRising = true;
            this.attackTimer = 0.0F;
        } else if (id == LEGACY_FLAME_EVENT) {
            spawnFlameParticles();
            spawnFlameParticles();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putFloat("SrpAttackTimer", this.attackTimer);
        tag.putBoolean("SrpAttackUp", this.attackTimerRising);
        tag.putInt("SrpSkillLeapCooldown", this.skillLeapCooldown);
        tag.putInt("SrpChargeCooldown", this.chargeCooldown);
        tag.putInt("SrpShockwaveCooldown", this.shockwaveCooldown);
        tag.putInt("SrpEvadeCooldown", this.evadeCooldown);
        tag.putInt("SrpChargeTicks", this.chargeTicks);
        tag.putInt("SrpShockwaveBorder", this.shockwaveBorder);
        tag.putBoolean("SrpChargeFinished", this.skillCharge);
        tag.putBoolean("SrpShockwaveFinished", this.skillShockwave);
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
        if (tag.contains("SrpSkillLeapCooldown")) {
            this.skillLeapCooldown = tag.getInt("SrpSkillLeapCooldown");
        }
        if (tag.contains("SrpChargeCooldown")) {
            this.chargeCooldown = tag.getInt("SrpChargeCooldown");
        }
        if (tag.contains("SrpShockwaveCooldown")) {
            this.shockwaveCooldown = tag.getInt("SrpShockwaveCooldown");
        }
        if (tag.contains("SrpEvadeCooldown")) {
            this.evadeCooldown = tag.getInt("SrpEvadeCooldown");
        }
        if (tag.contains("SrpChargeTicks")) {
            this.chargeTicks = tag.getInt("SrpChargeTicks");
        }
        if (tag.contains("SrpShockwaveBorder")) {
            this.shockwaveBorder = tag.getInt("SrpShockwaveBorder");
        }
        if (tag.contains("SrpChargeFinished")) {
            this.skillCharge = tag.getBoolean("SrpChargeFinished");
        }
        if (tag.contains("SrpShockwaveFinished")) {
            this.skillShockwave = tag.getBoolean("SrpShockwaveFinished");
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.GANRO_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.GANRO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GANRO_DEATH.get();
    }

    public boolean attackEntityAsMobAOE(Entity target) {
        this.attackTimerRising = true;
        this.attackTimer = 0.0F;
        this.level().broadcastEntityEvent(this, LEGACY_ATTACK_EVENT);
        this.playSound(ModSounds.MOB_SWIPE.get(), 3.0F, 1.0F);
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

    public float getSelfeFlashIntensity(float partialTicks) {
        return Mth.clamp(this.attackTimer / 1.5F, 0.0F, 1.0F);
    }

    public boolean getFinished(byte skill) {
        if (skill == 1) {
            return this.skillShockwave;
        }
        if (skill == 2) {
            return this.skillCharge;
        }
        return true;
    }

    public void setFinished(byte skill, boolean finished) {
        if (skill == 1) {
            this.skillShockwave = finished;
        } else if (skill == 2) {
            this.skillCharge = finished;
        }
    }

    public void doSpecialSkill(byte skill) {
        if (skill == 1) {
            shockwave();
        } else if (skill == 2) {
            charge();
        }
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

    private boolean canAoeHit(LivingEntity candidate) {
        return canHarmLiving(candidate) && hasLineOfSight(candidate);
    }

    private boolean canTargetGanroLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void tickAttackTimer() {
        if (this.attackTimerRising) {
            this.attackTimer += LEGACY_ATTACK_TIMER_UP_STEP;
            if (this.attackTimer > 1.5F) {
                this.attackTimerRising = false;
            }
        } else if (this.attackTimer > 0.0F) {
            this.attackTimer = Math.max(0.0F, this.attackTimer - LEGACY_ATTACK_TIMER_DOWN_STEP);
        }
    }

    private void maybeLaunchTarget(Entity target) {
        if (this.level().isClientSide || !(target instanceof LivingEntity)) {
            return;
        }
        if (this.random.nextFloat() >= 0.1F) {
            return;
        }
        Vec3 delta = target.position().subtract(position());
        Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            horizontal = new Vec3(this.random.nextDouble() - 0.5D, 0.0D, this.random.nextDouble() - 0.5D);
        }
        double y = target instanceof Player ? LEGACY_PLAYER_LAUNCH_Y : LEGACY_LAUNCH_Y;
        Vec3 launch = horizontal.normalize().scale(LEGACY_LAUNCH_HORIZONTAL).add(0.0D, y, 0.0D);
        target.setDeltaMovement(target.getDeltaMovement().add(launch));
        target.hasImpulse = true;
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

    private void charge() {
        this.chargeTicks++;
        setParasiteStatus(LEGACY_CHARGE_STATUS);
        if (this.chargeTicks < LEGACY_CHARGE_WINDUP_TICKS) {
            this.level().broadcastEntityEvent(this, LEGACY_FLAME_EVENT);
            LivingEntity target = getTarget();
            if (target == null || !target.isAlive() || !onGround() || isInWater()) {
                finishCharge();
                return;
            }
            if (this.chargeTicks == 2) {
                playSound(getHurtSound(damageSources().generic()), 4.0F, 2.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
            Vec3 delta = target.position().subtract(position());
            double distance = Math.max(0.001D, delta.length());
            this.chargeTarget = position().add(delta.scale(LEGACY_CHARGE_TARGET_DISTANCE / distance));
            getLookControl().setLookAt(target, 30.0F, 30.0F);
            getNavigation().stop();
            return;
        }
        if (this.chargeTicks == LEGACY_CHARGE_WINDUP_TICKS && this.chargeTarget != null) {
            getNavigation().moveTo(this.chargeTarget.x, this.chargeTarget.y, this.chargeTarget.z, LEGACY_CHARGE_SPEED);
        }
        damageChargeArea();
        if (this.chargeTicks >= LEGACY_CHARGE_STUCK_CANCEL_TICKS && (horizontalCollision || this.chargeTarget == null || position().distanceToSqr(this.chargeTarget) < 2.25D)) {
            finishCharge();
        }
    }

    private void damageChargeArea() {
        double damage = Math.max(LEGACY_MIN_DAMAGE, getAttributeValue(Attributes.ATTACK_DAMAGE) * LEGACY_CHARGE_DAMAGE_MULTIPLIER);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(LEGACY_CHARGE_AOE_INFLATE, 0.0D, LEGACY_CHARGE_AOE_INFLATE), this::canAoeHit)) {
            if (living.hurt(damageSources().mobAttack(this), (float) damage)) {
                Vec3 motion = living.getDeltaMovement();
                if (!living.onGround()) {
                    living.setDeltaMovement(motion.x * 0.7D, motion.y * 0.7D, motion.z * 0.7D);
                    living.hasImpulse = true;
                }
            }
        }
    }

    private void finishCharge() {
        this.skillCharge = true;
        this.chargeTicks = 0;
        this.chargeTarget = null;
        this.chargeCooldown = LEGACY_CHARGE_COOLDOWN_TICKS;
        setParasiteStatus(0);
    }

    private void shockwave() {
        setParasiteStatus(LEGACY_SHOCKWAVE_STATUS);
        getNavigation().stop();
        if (this.shockwaveBorder == 0) {
            playSound(getHurtSound(damageSources().generic()), 4.0F, 2.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            this.shockwaveBorder++;
            return;
        }
        if (this.shockwaveBorder <= 2) {
            this.level().broadcastEntityEvent(this, LEGACY_FLAME_EVENT);
        }
        if (this.tickCount % 20 != 0) {
            return;
        }
        this.shockwaveBorder++;
        LivingEntity target = getTarget();
        if (target == null || !target.isAlive()) {
            finishShockwave();
            return;
        }
        if (this.shockwaveBorder == LEGACY_SHOCKWAVE_FIRST_BORDER) {
            spawnShock();
            this.attackTimerRising = true;
            this.attackTimer = 0.0F;
            this.level().broadcastEntityEvent(this, LEGACY_ATTACK_EVENT);
            this.playSound(ModSounds.MOB_SWIPE.get(), 3.0F, 1.0F);
        }
        if (this.shockwaveBorder > LEGACY_SHOCKWAVE_FINISH_BORDER) {
            finishShockwave();
        }
    }

    private void spawnShock() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        double damage = Math.max(LEGACY_MIN_DAMAGE, getAttributeValue(Attributes.ATTACK_DAMAGE) * LEGACY_SHOCKWAVE_DAMAGE_MULTIPLIER);
        double range = 6.0D;
        for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(range, 1.5D, range), this::canAoeHit)) {
            Vec3 delta = living.position().subtract(position());
            Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
            if (horizontal.horizontalDistanceSqr() > 1.0E-4D && living.hurt(damageSources().mobAttack(this), (float) damage)) {
                Vec3 push = horizontal.normalize().scale(0.35D);
                living.setDeltaMovement(living.getDeltaMovement().add(push.x, 0.25D, push.z));
                living.hasImpulse = true;
            }
        }
        for (int i = 0; i < 40; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            double distance = 1.0D + this.random.nextDouble() * range;
            serverLevel.sendParticles(
                ParticleTypes.FLAME,
                getX() + Math.cos(angle) * distance,
                getY() + 0.25D + this.random.nextDouble(),
                getZ() + Math.sin(angle) * distance,
                1,
                0.02D,
                0.03D,
                0.02D,
                0.0D
            );
        }
    }

    private void finishShockwave() {
        this.skillShockwave = true;
        this.shockwaveBorder = 0;
        this.shockwaveCooldown = LEGACY_SHOCKWAVE_COOLDOWN_TICKS;
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

    private static final class GanroAoeMeleeGoal extends MeleeAttackGoal {
        private final GanroEntity ganro;

        private GanroAoeMeleeGoal(GanroEntity ganro) {
            super(ganro, LEGACY_AOE_MELEE_SPEED, false);
            this.ganro = ganro;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)
                || this.ganro.distanceToSqr(target) <= LEGACY_MELEE_REACH * LEGACY_MELEE_REACH && this.ganro.hasLineOfSight(target)) {
                resetAttackCooldown();
                this.ganro.swing(InteractionHand.MAIN_HAND);
                this.ganro.attackEntityAsMobAOE(target);
            }
        }
    }

    private static final class WaterLeapGoal extends Goal {
        private final GanroEntity ganro;
        private LivingEntity target;

        private WaterLeapGoal(GanroEntity ganro) {
            this.ganro = ganro;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.ganro.getTarget();
            return this.target != null
                && this.ganro.isInWater()
                && this.ganro.distanceToSqr(this.target) < 64.0D
                && this.ganro.getRandom().nextInt(LEGACY_WATER_LEAP_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.ganro.setParasiteStatus(LEGACY_WATER_LEAP_STATUS);
            this.ganro.tryLeapAt(this.target, LEGACY_WATER_LEAP_POWER, LEGACY_WATER_LEAP_SPEED);
        }
    }

    private static final class SkillLeapGoal extends Goal {
        private final GanroEntity ganro;
        private LivingEntity target;
        private int windup;

        private SkillLeapGoal(GanroEntity ganro) {
            this.ganro = ganro;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.ganro.getTarget();
            return this.target != null
                && this.ganro.skillLeapCooldown <= 0
                && this.ganro.distanceToSqr(this.target) > 9.0D
                && this.ganro.distanceToSqr(this.target) < 100.0D
                && this.ganro.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_LEAP_WINDUP;
            this.ganro.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.ganro.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0) {
                this.ganro.tryLeapAt(this.target, LEGACY_SKILL_LEAP_POWER, LEGACY_SKILL_LEAP_SPEED);
                this.ganro.skillLeapCooldown = LEGACY_SKILL_LEAP_MIN_COOLDOWN
                    + this.ganro.getRandom().nextInt(LEGACY_SKILL_LEAP_MAX_COOLDOWN - LEGACY_SKILL_LEAP_MIN_COOLDOWN + 1);
            }
        }
    }

    private static final class ChargeGoal extends Goal {
        private final GanroEntity ganro;

        private ChargeGoal(GanroEntity ganro) {
            this.ganro = ganro;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.ganro.getTarget();
            return target != null
                && target.isAlive()
                && this.ganro.chargeCooldown <= 0
                && this.ganro.onGround()
                && !this.ganro.isInWater()
                && this.ganro.distanceToSqr(target) <= LEGACY_CHARGE_TRIGGER_RANGE * LEGACY_CHARGE_TRIGGER_RANGE
                && this.ganro.hasLineOfSight(target)
                && this.ganro.getFinished((byte) 2);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.ganro.getFinished((byte) 2);
        }

        @Override
        public void start() {
            this.ganro.setFinished((byte) 2, false);
            this.ganro.chargeTicks = 0;
            this.ganro.chargeTarget = null;
        }

        @Override
        public void stop() {
            if (!this.ganro.getFinished((byte) 2)) {
                this.ganro.finishCharge();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.ganro.doSpecialSkill((byte) 2);
        }
    }

    private static final class ShockwaveGoal extends Goal {
        private final GanroEntity ganro;

        private ShockwaveGoal(GanroEntity ganro) {
            this.ganro = ganro;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.ganro.getTarget();
            return target != null
                && target.isAlive()
                && this.ganro.shockwaveCooldown <= 0
                && this.ganro.distanceToSqr(target) <= LEGACY_SHOCKWAVE_TRIGGER_RANGE * LEGACY_SHOCKWAVE_TRIGGER_RANGE
                && this.ganro.hasLineOfSight(target)
                && this.ganro.getFinished((byte) 1);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.ganro.getFinished((byte) 1);
        }

        @Override
        public void start() {
            this.ganro.setFinished((byte) 1, false);
            this.ganro.shockwaveBorder = 0;
        }

        @Override
        public void stop() {
            if (!this.ganro.getFinished((byte) 1)) {
                this.ganro.finishShockwave();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.ganro.getTarget();
            if (target != null) {
                this.ganro.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            this.ganro.doSpecialSkill((byte) 1);
        }
    }

    private static final class EvadeDashGoal extends Goal {
        private final GanroEntity ganro;
        private LivingEntity target;

        private EvadeDashGoal(GanroEntity ganro) {
            this.ganro = ganro;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.ganro.getTarget();
            if (this.target == null || this.ganro.evadeCooldown > 0) {
                return false;
            }
            double distance = this.ganro.distanceToSqr(this.target);
            return distance >= LEGACY_EVADE_MIN_DISTANCE * LEGACY_EVADE_MIN_DISTANCE
                && distance <= LEGACY_EVADE_MAX_DISTANCE * LEGACY_EVADE_MAX_DISTANCE
                && this.ganro.getRandom().nextInt(LEGACY_EVADE_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.ganro.tryEvadeFrom(this.target);
        }
    }
}
