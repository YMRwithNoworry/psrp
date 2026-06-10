package com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.entity.projectile.ElviaBallEntity;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ElviaEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 85;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.elvia.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.elvia.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 310.0D;
    public static final double LEGACY_ARMOR = 15.5D;
    public static final double LEGACY_ATTACK_DAMAGE = 70.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.15D;
    public static final double LEGACY_FOLLOW_RANGE = 80.0D;
    public static final float LEGACY_WIDTH = 4.0F;
    public static final float LEGACY_HEIGHT = 4.0F;
    public static final float LEGACY_EYE_HEIGHT = 2.1F;
    public static final float LEGACY_SHADOW_RADIUS = 1.3F;
    public static final double LEGACY_ADAPTATION_CAP = 0.95D;
    public static final double LEGACY_NEEDED_HEALTH_FRACTION = 0.4D;
    public static final double LEGACY_GROUND_LIFT = 5.0D;
    public static final double LEGACY_GROUND_LIFT_SPEED = 0.5D;
    public static final double LEGACY_BLOCK_LIFT_SPEED = 0.5D;
    public static final int LEGACY_RANDOM_MOVE_CHANCE = 7;
    public static final double LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.6D;
    public static final double LEGACY_RANDOM_MOVE_SPEED_FAR = 0.7D;
    public static final double LEGACY_RANDOM_MOVE_SPEED_CLOSE = 0.75D;
    public static final int LEGACY_CHARGE_CHANCE = 5;
    public static final double LEGACY_CHARGE_MIN_DISTANCE_SQR = 4.0D;
    public static final double LEGACY_CHARGE_RETARGET_DISTANCE_SQR = 9.0D;
    public static final double LEGACY_CHARGE_TARGET_Y_OFFSET = 20.0D;
    public static final double LEGACY_CHARGE_SPEED_NEAR_LOS = 0.7D;
    public static final double LEGACY_CHARGE_SPEED_DIRECT = 1.1D;
    public static final int LEGACY_PROJECTILE_INTERVAL = 20;
    public static final int LEGACY_PROJECTILE_MIN_DISTANCE = 10;
    public static final int LEGACY_PROJECTILE_COUNT = 4;
    public static final int LEGACY_AOE_INTERVAL = 10;
    public static final double LEGACY_AOE_RANGE = 3.0D;
    public static final int LEGACY_INVISIBILITY_TICKS = 25;
    public static final int LEGACY_INVISIBILITY_AMPLIFIER = 1;
    public static final byte LEGACY_INVISIBILITY_EVENT = 6;

    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(ElviaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(ElviaEntity.class, EntityDataSerializers.BYTE);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);
    private static final int FLAG_CHARGING = 1;
    private static final int FLAG_LEGACY_INVISIBLE = 2;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int invisibleTimer;
    private int projectileTimer;
    private int projectileCycleCount;

    public ElviaEntity(EntityType<? extends ElviaEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ElviaMoveControl(this);
        this.xpReward = 150;
        setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
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
        this.goalSelector.addGoal(1, new ChargeAttackGoal(this));
        this.goalSelector.addGoal(2, new ElviaProjectileGoal(this));
        this.goalSelector.addGoal(6, new RandomFlyGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetElviaLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PARASITE_STATUS, 0);
        builder.define(FLAGS, (byte) 0);
    }

    @Override
    public void tick() {
        setNoGravity(true);
        if (!this.level().isClientSide) {
            tickFlightSurface();
            tickNearbyDamage();
            tickLegacyInvisibility();
        }
        super.tick();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        setCharging(false);
        return target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide) {
            clearLegacyInvisibility();
        }
        return hurt;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.ELVIA_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.ELVIA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ELVIA_DEATH.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpInvisibleTimer", this.invisibleTimer);
        tag.putBoolean("SrpInvisible", isLegacyInvisible());
        tag.putInt("SrpProjectileTimer", this.projectileTimer);
        tag.putInt("SrpProjectileCycleCount", this.projectileCycleCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpInvisibleTimer")) {
            this.invisibleTimer = tag.getInt("SrpInvisibleTimer");
        }
        if (tag.contains("SrpInvisible")) {
            setLegacyInvisible(tag.getBoolean("SrpInvisible"));
        }
        if (tag.contains("SrpProjectileTimer")) {
            this.projectileTimer = tag.getInt("SrpProjectileTimer");
        }
        if (tag.contains("SrpProjectileCycleCount")) {
            this.projectileCycleCount = tag.getInt("SrpProjectileCycleCount");
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_INVISIBILITY_EVENT) {
            for (int i = 0; i < 12; i++) {
                this.level().addParticle(ParticleTypes.SMOKE, getRandomX(1.0D), getRandomY(), getRandomZ(1.0D), 0.0D, 0.03D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    public int getParasiteStatus() {
        return this.entityData.get(PARASITE_STATUS);
    }

    public void setParasiteStatus(int status) {
        this.entityData.set(PARASITE_STATUS, status);
    }

    public boolean isCharging() {
        return getFlag(FLAG_CHARGING);
    }

    public void setCharging(boolean charging) {
        setFlag(FLAG_CHARGING, charging);
    }

    public boolean isLegacyInvisible() {
        return getFlag(FLAG_LEGACY_INVISIBLE);
    }

    public void setLegacyInvisible(boolean invisible) {
        setFlag(FLAG_LEGACY_INVISIBLE, invisible);
        setInvisible(invisible);
    }

    public float getSelfeFlashIntensity(float partialTicks) {
        return isCharging() ? 0.5F : 0.0F;
    }

    public ElviaBallEntity getProj(double xPower, double yPower, double zPower) {
        playProjSound();
        return new ElviaBallEntity(this.level(), this, xPower, yPower, zPower);
    }

    private void playProjSound() {
        playSound(ModSounds.DORPA_RANGE.get(), 2.0F, 1.0F);
        this.projectileCycleCount++;
        clearLegacyInvisibility();
    }

    private boolean getFlag(int flag) {
        return (this.entityData.get(FLAGS) & flag) != 0;
    }

    private void setFlag(int flag, boolean value) {
        byte flags = this.entityData.get(FLAGS);
        if (value) {
            flags = (byte) (flags | flag);
        } else {
            flags = (byte) (flags & ~flag);
        }
        this.entityData.set(FLAGS, flags);
    }

    private boolean canTargetElviaLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void tickFlightSurface() {
        if (onGround()) {
            this.moveControl.setWantedPosition(getX(), getY() + LEGACY_GROUND_LIFT, getZ(), LEGACY_GROUND_LIFT_SPEED);
        }
        LivingEntity target = getTarget();
        if (target != null && (!this.level().isEmptyBlock(blockPosition().below()) || !this.level().isEmptyBlock(blockPosition().below(2)))) {
            setDeltaMovement(getDeltaMovement().x, LEGACY_BLOCK_LIFT_SPEED, getDeltaMovement().z);
            this.hasImpulse = true;
        }
    }

    private void tickNearbyDamage() {
        if (this.tickCount % LEGACY_AOE_INTERVAL != 0) {
            return;
        }
        AABB area = getBoundingBox().inflate(LEGACY_AOE_RANGE);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canHarmLiving)) {
            living.hurt(damageSources().mobAttack(this), (float) LEGACY_ATTACK_DAMAGE);
        }
    }

    private void tickLegacyInvisibility() {
        if (isLegacyInvisible()) {
            addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, LEGACY_INVISIBILITY_TICKS, LEGACY_INVISIBILITY_AMPLIFIER, false, false), this);
            if (getHealth() / getMaxHealth() < LEGACY_NEEDED_HEALTH_FRACTION) {
                clearLegacyInvisibility();
            }
            return;
        }
        if (getHealth() / getMaxHealth() >= LEGACY_NEEDED_HEALTH_FRACTION) {
            this.invisibleTimer++;
            if (this.invisibleTimer > 2) {
                setLegacyInvisible(true);
                this.level().broadcastEntityEvent(this, LEGACY_INVISIBILITY_EVENT);
            }
        }
    }

    private void clearLegacyInvisibility() {
        this.invisibleTimer = 0;
        if (isLegacyInvisible()) {
            setLegacyInvisible(false);
            removeEffect(MobEffects.INVISIBILITY);
        }
    }

    private void shootAtTarget(LivingEntity target) {
        Vec3 look = getLookAngle();
        double xPower = target.getX() - (getX() + look.x);
        double yPower = target.getBoundingBox().minY + target.getBbHeight() / 2.0D - (getY() + getEyeHeight() - 0.2D);
        double zPower = target.getZ() - (getZ() + look.z);
        ElviaBallEntity projectile = getProj(xPower, yPower, zPower);
        projectile.setPos(getX() + look.x, getY() + getEyeHeight() - 0.2D, getZ() + look.z);
        this.level().addFreshEntity(projectile);
    }

    private static final class ChargeAttackGoal extends Goal {
        private final ElviaEntity elvia;
        private LivingEntity target;

        private ChargeAttackGoal(ElviaEntity elvia) {
            this.elvia = elvia;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.elvia.getTarget();
            return this.target != null
                && this.target.isAlive()
                && this.elvia.getRandom().nextInt(LEGACY_CHARGE_CHANCE) == 0
                && this.elvia.distanceToSqr(this.target) > LEGACY_CHARGE_MIN_DISTANCE_SQR;
        }

        @Override
        public boolean canContinueToUse() {
            return this.elvia.getMoveControl().hasWanted()
                && this.elvia.isCharging()
                && this.target != null
                && this.target.isAlive();
        }

        @Override
        public void start() {
            Vec3 targetEye = this.target.getEyePosition().add(0.0D, LEGACY_CHARGE_TARGET_Y_OFFSET, 0.0D);
            this.elvia.getMoveControl().setWantedPosition(targetEye.x, targetEye.y, targetEye.z, LEGACY_CHARGE_SPEED_NEAR_LOS);
            this.elvia.setCharging(true);
        }

        @Override
        public void stop() {
            this.elvia.setCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null || !this.target.isAlive()) {
                return;
            }
            if (this.elvia.getBoundingBox().intersects(this.target.getBoundingBox())) {
                this.elvia.doHurtTarget(this.target);
                return;
            }
            Vec3 wanted = this.target.getEyePosition().add(0.0D, LEGACY_CHARGE_TARGET_Y_OFFSET, 0.0D);
            double speed = LEGACY_CHARGE_SPEED_DIRECT;
            if (this.elvia.distanceToSqr(this.target) < LEGACY_CHARGE_RETARGET_DISTANCE_SQR) {
                speed = this.elvia.hasLineOfSight(this.target) ? LEGACY_CHARGE_SPEED_NEAR_LOS : LEGACY_CHARGE_SPEED_DIRECT;
                if (!this.elvia.hasLineOfSight(this.target)) {
                    wanted = this.target.position();
                }
            }
            this.elvia.getMoveControl().setWantedPosition(wanted.x, wanted.y, wanted.z, speed);
        }
    }

    private static final class ElviaProjectileGoal extends Goal {
        private final ElviaEntity elvia;

        private ElviaProjectileGoal(ElviaEntity elvia) {
            this.elvia = elvia;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.elvia.getTarget();
            this.elvia.projectileTimer++;
            return target != null
                && target.isAlive()
                && this.elvia.projectileTimer >= LEGACY_PROJECTILE_INTERVAL
                && this.elvia.distanceToSqr(target) > LEGACY_PROJECTILE_MIN_DISTANCE * LEGACY_PROJECTILE_MIN_DISTANCE;
        }

        @Override
        public void start() {
            this.elvia.projectileTimer = 0;
            LivingEntity target = this.elvia.getTarget();
            if (target == null) {
                return;
            }
            this.elvia.getLookControl().setLookAt(target, 30.0F, 30.0F);
            for (int i = 0; i < LEGACY_PROJECTILE_COUNT; i++) {
                this.elvia.shootAtTarget(target);
            }
        }
    }

    private static final class RandomFlyGoal extends Goal {
        private final ElviaEntity elvia;

        private RandomFlyGoal(ElviaEntity elvia) {
            this.elvia = elvia;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.elvia.getMoveControl().hasWanted() && this.elvia.getRandom().nextInt(LEGACY_RANDOM_MOVE_CHANCE) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            LivingEntity target = this.elvia.getTarget();
            BlockPos origin = this.elvia.blockPosition();
            int mode = 1;
            double speed = LEGACY_RANDOM_MOVE_SPEED_IDLE;
            if (target != null && this.elvia.distanceToSqr(target) > 100.0D) {
                origin = target.blockPosition();
                mode = 2;
                speed = LEGACY_RANDOM_MOVE_SPEED_FAR;
            } else if (target != null && this.elvia.distanceToSqr(target) < 36.0D) {
                origin = target.blockPosition();
                mode = 3;
                speed = LEGACY_RANDOM_MOVE_SPEED_CLOSE;
            }
            for (int i = 0; i < 3; i++) {
                BlockPos pos = randomOffset(origin, mode, this.elvia);
                if (this.elvia.level().isEmptyBlock(pos)) {
                    this.elvia.getMoveControl().setWantedPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, speed);
                    if (target == null) {
                        this.elvia.getLookControl().setLookAt(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    return;
                }
            }
        }

        private static BlockPos randomOffset(BlockPos origin, int mode, ElviaEntity elvia) {
            if (mode == 2) {
                return origin.offset(elvia.getRandom().nextInt(6) - 2, elvia.getRandom().nextInt(7) - 2, elvia.getRandom().nextInt(6) - 2);
            }
            if (mode == 3) {
                int x = elvia.getRandom().nextInt(4) + 3;
                int z = elvia.getRandom().nextInt(4) + 3;
                return origin.offset(elvia.getRandom().nextBoolean() ? x : -x, elvia.getRandom().nextInt(5) + 4, elvia.getRandom().nextBoolean() ? z : -z);
            }
            return origin.offset(elvia.getRandom().nextInt(15) - 7, elvia.getRandom().nextInt(11) - 5, elvia.getRandom().nextInt(15) - 7);
        }
    }

    private static final class ElviaMoveControl extends MoveControl {
        private final ElviaEntity elvia;

        private ElviaMoveControl(ElviaEntity elvia) {
            super(elvia);
            this.elvia = elvia;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            Vec3 delta = new Vec3(this.wantedX - this.elvia.getX(), this.wantedY - this.elvia.getY(), this.wantedZ - this.elvia.getZ());
            double distance = delta.length();
            if (distance < this.elvia.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.elvia.setDeltaMovement(this.elvia.getDeltaMovement().scale(0.5D));
                return;
            }
            Vec3 movement = this.elvia.getDeltaMovement().add(delta.scale(0.05D * this.speedModifier / distance));
            this.elvia.setDeltaMovement(movement);
            LivingEntity target = this.elvia.getTarget();
            double xLook = target == null ? movement.x : target.getX() - this.elvia.getX();
            double zLook = target == null ? movement.z : target.getZ() - this.elvia.getZ();
            if (xLook * xLook + zLook * zLook > 1.0E-7D) {
                this.elvia.setYRot(-((float) Mth.atan2(xLook, zLook)) * Mth.RAD_TO_DEG);
                this.elvia.yBodyRot = this.elvia.getYRot();
            }
        }
    }
}
