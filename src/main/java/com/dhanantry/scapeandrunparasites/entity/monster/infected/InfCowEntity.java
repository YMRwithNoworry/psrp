package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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

public class InfCowEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 13;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_cow.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_cow.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 18.0D;
    public static final double LEGACY_ARMOR = 5.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 7.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.4D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.20000000298023224D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.9F;
    public static final float LEGACY_HEIGHT = 1.4F;
    public static final float LEGACY_EYE_HEIGHT = 1.3F;
    public static final float LEGACY_SHADOW_RADIUS = 0.5F;
    public static final int LEGACY_TYPE = 11;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final int LEGACY_FUSE_TIME = 40;
    public static final double LEGACY_SWIM_DIVE_SPEED = 0.08D;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final int LEGACY_FOLLOWER_SEARCH_MODE = 1;
    public static final int LEGACY_FOLLOWER_SEARCH_RANGE = 16;
    public static final int LEGACY_SKILL_INTERVAL = 60;
    public static final int LEGACY_SKILL_RANGE = 32;
    public static final int LEGACY_SKILL_UNKNOWN = 8;
    public static final int LEGACY_CHARGE_SKILL_ID = 1;
    public static final int LEGACY_CHARGE_STATUS = 3;
    public static final int LEGACY_CHARGE_END_STATUS = 2;
    public static final int LEGACY_CHARGE_WINDUP_TICKS = 40;
    public static final int LEGACY_CHARGE_STUCK_CANCEL_TICKS = 80;
    public static final double LEGACY_CHARGE_TARGET_DISTANCE = 15.0D;
    public static final double LEGACY_CHARGE_SPEED = 2.0D;
    public static final double LEGACY_CHARGE_AOE_INFLATE_XZ = 1.0D;
    public static final double LEGACY_CHARGE_AOE_INFLATE_Y = 0.0D;
    public static final double LEGACY_CHARGE_AIR_DRAG = 0.7D;
    public static final double LEGACY_CHARGE_DAMAGE_MULTIPLIER = 1.0D;
    public static final int LEGACY_MELT_WAIT_TICKS = 1000;
    public static final float LEGACY_MELT_START_HEIGHT = 1.4F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE = 0.17F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F;
    public static final float LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F;
    public static final float LEGACY_MELT_ASIZE_STEP = -0.005F;
    public static final float LEGACY_MELT_HEIGHT_STEP = -0.01F;
    public static final int LEGACY_MELT_SPAWN_TICKS = 73;
    public static final int LEGACY_LESH_LEGS = 0;

    private static final EntityDataAccessor<Float> MELT_HEIGHT = SynchedEntityData.defineId(InfCowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> MELTING = SynchedEntityData.defineId(InfCowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InfCowEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float aSize = 1.0F;
    private int meltSoundTicks;
    private int meltWaitTicks;
    private int chargeCooldown;
    private int chargeTicks;
    private boolean skillCharge = true;
    private Vec3 chargeTarget;
    private Vec3 previousChargePosition = Vec3.ZERO;

    public InfCowEntity(EntityType<? extends InfCowEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
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
        this.goalSelector.addGoal(3, new InfCowMeleeGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MELT_HEIGHT, 0.0F);
        builder.define(MELTING, false);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        tickMelting();
        if (!this.level().isClientSide && this.chargeCooldown > 0) {
            this.chargeCooldown--;
            if (this.chargeCooldown == 0 && getParasiteStatus() == LEGACY_CHARGE_END_STATUS) {
                setParasiteStatus(0);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDCOW_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDCOW_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDCOW_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("SrpMeltHeight", getTHeigh());
        tag.putBoolean("SrpMelting", isMelting());
        tag.putFloat("SrpASize", this.aSize);
        tag.putInt("SrpMeltSoundTicks", this.meltSoundTicks);
        tag.putInt("SrpMeltWaitTicks", this.meltWaitTicks);
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpChargeCooldown", this.chargeCooldown);
        tag.putInt("SrpChargeTicks", this.chargeTicks);
        tag.putBoolean("SrpChargeFinished", this.skillCharge);
        if (this.chargeTarget != null) {
            tag.putDouble("SrpChargeTargetX", this.chargeTarget.x);
            tag.putDouble("SrpChargeTargetY", this.chargeTarget.y);
            tag.putDouble("SrpChargeTargetZ", this.chargeTarget.z);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpMeltHeight")) {
            setTHeighAbsolute(tag.getFloat("SrpMeltHeight"));
        }
        if (tag.contains("SrpMelting")) {
            setMelting(tag.getBoolean("SrpMelting"));
        }
        if (tag.contains("SrpASize")) {
            this.aSize = tag.getFloat("SrpASize");
        }
        if (tag.contains("SrpMeltSoundTicks")) {
            this.meltSoundTicks = tag.getInt("SrpMeltSoundTicks");
        }
        if (tag.contains("SrpMeltWaitTicks")) {
            this.meltWaitTicks = tag.getInt("SrpMeltWaitTicks");
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpChargeCooldown")) {
            this.chargeCooldown = tag.getInt("SrpChargeCooldown");
        }
        if (tag.contains("SrpChargeTicks")) {
            this.chargeTicks = tag.getInt("SrpChargeTicks");
        }
        if (tag.contains("SrpChargeFinished")) {
            this.skillCharge = tag.getBoolean("SrpChargeFinished");
        }
        if (tag.contains("SrpChargeTargetX") && tag.contains("SrpChargeTargetY") && tag.contains("SrpChargeTargetZ")) {
            this.chargeTarget = new Vec3(tag.getDouble("SrpChargeTargetX"), tag.getDouble("SrpChargeTargetY"), tag.getDouble("SrpChargeTargetZ"));
        }
    }

    public void melt() {
        this.meltWaitTicks = LEGACY_MELT_WAIT_TICKS;
        setTHeighAbsolute(LEGACY_MELT_START_HEIGHT);
        setMelting(true);
    }

    public boolean isMelting() {
        return this.entityData.get(MELTING);
    }

    public void setMelting(boolean melting) {
        this.entityData.set(MELTING, melting);
    }

    public float getTHeigh() {
        return this.entityData.get(MELT_HEIGHT);
    }

    public void setTHeigh(float heightDelta) {
        setTHeighAbsolute(getTHeigh() + heightDelta);
    }

    public float getaSize() {
        return this.aSize;
    }

    public void setaSize(float sizeDelta) {
        this.aSize += sizeDelta;
    }

    public float getSelfeFlashIntensity2() {
        return this.aSize;
    }

    public float getSelfeFlashIntensity(float partialTick) {
        return 0.0F;
    }

    public int getParasiteStatus() {
        return this.entityData.get(PARASITE_STATUS);
    }

    public void setParasiteStatus(int status) {
        this.entityData.set(PARASITE_STATUS, status);
    }

    public boolean getFinished(byte skill) {
        return skill != LEGACY_CHARGE_SKILL_ID || this.skillCharge;
    }

    public void setFinished(byte skill, boolean finished) {
        if (skill == LEGACY_CHARGE_SKILL_ID) {
            this.skillCharge = finished;
        }
    }

    public void doSpecialSkill(byte skill) {
        if (skill == LEGACY_CHARGE_SKILL_ID) {
            charge();
        }
    }

    private void setTHeighAbsolute(float height) {
        this.entityData.set(MELT_HEIGHT, height);
    }

    private void tickMelting() {
        if (!isMelting()) {
            return;
        }
        if (this.meltSoundTicks % 20 == 0) {
            playSound(ModSounds.INFECTED_MELT.get(), 1.0F, 1.0F);
        }
        this.meltSoundTicks++;
        if (getTHeigh() > LEGACY_MELT_THRESHOLD_HEIGHT) {
            setaSize(LEGACY_MELT_ASIZE_STEP);
            setTHeigh(LEGACY_MELT_HEIGHT_STEP);
        }
        if (!this.level().isClientSide && (getTHeigh() <= LEGACY_MELT_THRESHOLD_HEIGHT || this.meltSoundTicks >= LEGACY_MELT_SPAWN_TICKS)) {
            setMelting(false);
        }
    }

    private void charge() {
        this.chargeTicks++;
        LivingEntity target = getTarget();
        if (this.chargeTicks < LEGACY_CHARGE_WINDUP_TICKS) {
            if (target == null || !target.isAlive() || !onGround() || isInWater() || target.getY() > getY()) {
                finishCharge(0);
                return;
            }
            setParasiteStatus(LEGACY_CHARGE_STATUS);
            getNavigation().stop();
            getLookControl().setLookAt(target, 30.0F, 30.0F);
            Vec3 delta = target.position().subtract(position());
            double distance = Math.max(0.001D, delta.length());
            this.chargeTarget = position().add(delta.scale(LEGACY_CHARGE_TARGET_DISTANCE / distance));
            this.previousChargePosition = position();
            return;
        }
        if (this.chargeTicks == LEGACY_CHARGE_WINDUP_TICKS && this.chargeTarget != null) {
            getNavigation().moveTo(this.chargeTarget.x, this.chargeTarget.y, this.chargeTarget.z, LEGACY_CHARGE_SPEED);
        }
        damageChargeArea();
        if (!onGround()) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x * LEGACY_CHARGE_AIR_DRAG, motion.y, motion.z * LEGACY_CHARGE_AIR_DRAG);
            this.hasImpulse = true;
        }
        if (this.chargeTicks >= LEGACY_CHARGE_STUCK_CANCEL_TICKS) {
            boolean didNotMove = position().distanceToSqr(this.previousChargePosition) < 1.0E-4D;
            this.previousChargePosition = position();
            if (didNotMove || this.chargeTarget == null || horizontalCollision) {
                finishCharge(LEGACY_CHARGE_END_STATUS);
            }
        }
    }

    private void damageChargeArea() {
        AABB area = getBoundingBox().inflate(LEGACY_CHARGE_AOE_INFLATE_XZ, LEGACY_CHARGE_AOE_INFLATE_Y, LEGACY_CHARGE_AOE_INFLATE_XZ);
        float damage = (float) (getAttributeValue(Attributes.ATTACK_DAMAGE) * LEGACY_CHARGE_DAMAGE_MULTIPLIER);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canChargeHit)) {
            living.hurt(damageSources().mobAttack(this), damage);
        }
    }

    private boolean canChargeHit(LivingEntity candidate) {
        return canHarmLiving(candidate) && hasLineOfSight(candidate);
    }

    private void finishCharge(int status) {
        this.skillCharge = true;
        this.chargeTicks = 0;
        this.chargeTarget = null;
        this.chargeCooldown = LEGACY_SKILL_INTERVAL;
        setParasiteStatus(status);
    }

    private static final class InfCowMeleeGoal extends MeleeAttackGoal {
        private final InfCowEntity cow;

        private InfCowMeleeGoal(InfCowEntity cow) {
            super(cow, LEGACY_MELEE_SPEED, false);
            this.cow = cow;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.cow.swing(InteractionHand.MAIN_HAND);
                this.cow.doHurtTarget(target);
            }
        }
    }

    private static final class ChargeGoal extends Goal {
        private final InfCowEntity cow;

        private ChargeGoal(InfCowEntity cow) {
            this.cow = cow;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.cow.getTarget();
            return target != null
                && target.isAlive()
                && this.cow.chargeCooldown <= 0
                && this.cow.onGround()
                && !this.cow.isInWater()
                && this.cow.distanceToSqr(target) <= LEGACY_SKILL_RANGE * LEGACY_SKILL_RANGE
                && this.cow.hasLineOfSight(target)
                && this.cow.getFinished((byte) LEGACY_CHARGE_SKILL_ID);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.cow.getFinished((byte) LEGACY_CHARGE_SKILL_ID);
        }

        @Override
        public void start() {
            this.cow.setFinished((byte) LEGACY_CHARGE_SKILL_ID, false);
            this.cow.chargeTicks = 0;
            this.cow.chargeTarget = null;
            this.cow.previousChargePosition = this.cow.position();
        }

        @Override
        public void stop() {
            if (!this.cow.getFinished((byte) LEGACY_CHARGE_SKILL_ID)) {
                this.cow.finishCharge(0);
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.cow.doSpecialSkill((byte) LEGACY_CHARGE_SKILL_ID);
        }
    }
}
