package com.dhanantry.scapeandrunparasites.entity.monster.derived;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.entity.projectile.AlafhaBallEntity;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
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

public class HebluEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 309;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.heblu.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.heblu.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 525.0D;
    public static final double LEGACY_ARMOR = 30.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 210.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.27D;
    public static final double LEGACY_FOLLOW_RANGE = 80.0D;
    public static final float LEGACY_WIDTH = 2.4F;
    public static final float LEGACY_HEIGHT = 3.8F;
    public static final float LEGACY_EYE_HEIGHT = 2.0F;
    public static final float LEGACY_SHADOW_RADIUS = 1.3F;
    public static final int LEGACY_TYPE = 14;
    public static final int LEGACY_KILLCOUNT = -10;
    public static final int LEGACY_CAN_MOD_RENDER = 0;
    public static final int LEGACY_RANDOM_MOVE_CHANCE = 5;
    public static final double LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.5D;
    public static final double LEGACY_RANDOM_MOVE_SPEED_TARGET = 0.75D;
    public static final int LEGACY_AOE_INTERVAL = 10;
    public static final double LEGACY_AOE_RANGE = 9.0D;
    public static final int LEGACY_FIREBALL_WINDUP_TICKS = 20;
    public static final int LEGACY_FIREBALL_COOLDOWN_TICKS = -45;
    public static final int LEGACY_RAIN_COOLDOWN_TICKS = -60;
    public static final double LEGACY_FIREBALL_MAX_DISTANCE_SQR = 4096.0D;
    public static final byte LEGACY_RAIN_EVENT = 100;

    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(HebluEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(HebluEntity.class, EntityDataSerializers.BYTE);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);
    private static final int FLAG_FLYING = 1;
    private static final int FLAG_ATTACKING = 2;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int attackTimer;
    private int rainTimer;
    private int rainingOrbs;

    public HebluEntity(EntityType<? extends HebluEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new HebluMoveControl(this);
        this.xpReward = 300;
        setNoGravity(true);
        setFlyingState(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
            .add(Attributes.ATTACK_DAMAGE, LEGACY_ATTACK_DAMAGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, LEGACY_KNOCKBACK_RESISTANCE)
            .add(Attributes.MOVEMENT_SPEED, LEGACY_MOVEMENT_SPEED)
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
        this.goalSelector.addGoal(1, new FireballAttackGoal(this));
        this.goalSelector.addGoal(6, new RandomFlyGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetHebluLiving));
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
            tickNearbyDamage();
            tickRainState();
        }
        super.tick();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.HEBLU_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.HEBLU_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HEBLU_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        playSound(ModSounds.HEBLU_STEP.get(), 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putBoolean("SrpFlying", getFlyingState());
        tag.putInt("SrpAttackTimer", this.attackTimer);
        tag.putInt("SrpRainTimer", this.rainTimer);
        tag.putInt("SrpRainingOrbs", this.rainingOrbs);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpFlying")) {
            setFlyingState(tag.getBoolean("SrpFlying"));
        }
        if (tag.contains("SrpAttackTimer")) {
            this.attackTimer = tag.getInt("SrpAttackTimer");
        }
        if (tag.contains("SrpRainTimer")) {
            this.rainTimer = tag.getInt("SrpRainTimer");
        }
        if (tag.contains("SrpRainingOrbs")) {
            this.rainingOrbs = tag.getInt("SrpRainingOrbs");
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_RAIN_EVENT) {
            for (int i = 0; i < 24; i++) {
                this.level().addParticle(ParticleTypes.FLAME, getRandomX(1.0D), getRandomY(), getRandomZ(1.0D), 0.0D, 0.05D, 0.0D);
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

    public boolean getFlyingState() {
        return getFlag(FLAG_FLYING);
    }

    public void setFlyingState(boolean flying) {
        setFlag(FLAG_FLYING, flying);
    }

    public boolean isAttacking() {
        return getFlag(FLAG_ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        setFlag(FLAG_ATTACKING, attacking);
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

    private boolean canTargetHebluLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
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

    private void tickRainState() {
        if (this.rainTimer <= 0) {
            return;
        }
        this.rainTimer--;
        if (this.rainTimer % 10 == 0 && this.rainingOrbs > 0) {
            this.rainingOrbs--;
            LivingEntity target = getTarget();
            if (target != null && target.isAlive()) {
                shootAtTarget(target);
            }
        }
    }

    private void shootAtTarget(LivingEntity target) {
        Vec3 look = getLookAngle();
        double startX = getX() + look.x * 4.0D;
        double startY = getY() + getBbHeight() * 0.5D + 0.5D;
        double startZ = getZ() + look.z * 4.0D;
        double xPower = target.getX() - startX;
        double yPower = target.getBoundingBox().minY + target.getBbHeight() / 2.0D - startY;
        double zPower = target.getZ() - startZ;
        AlafhaBallEntity projectile = new AlafhaBallEntity(this.level(), this, xPower, yPower, zPower);
        projectile.setPos(startX, startY, startZ);
        this.level().addFreshEntity(projectile);
        playSound(ModSounds.HEBLU_SHOOT.get(), 2.0F, 1.0F);
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(ParticleTypes.FLAME, startX, startY, startZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private static final class FireballAttackGoal extends Goal {
        private final HebluEntity heblu;

        private FireballAttackGoal(HebluEntity heblu) {
            this.heblu = heblu;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.heblu.getTarget();
            return target != null
                && target.isAlive()
                && this.heblu.getFlyingState()
                && this.heblu.distanceToSqr(target) < LEGACY_FIREBALL_MAX_DISTANCE_SQR
                && this.heblu.hasLineOfSight(target);
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.heblu.getTarget();
            if (target == null) {
                return;
            }
            this.heblu.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.heblu.attackTimer++;
            if (this.heblu.hasEffect(ModEffects.RAGE)) {
                this.heblu.attackTimer++;
            }
            this.heblu.setAttacking(this.heblu.attackTimer > 10);
            if (this.heblu.attackTimer != LEGACY_FIREBALL_WINDUP_TICKS) {
                return;
            }
            if (target.onGround() && this.heblu.getRandom().nextInt(3) == 0) {
                this.heblu.rainTimer = 40;
                this.heblu.rainingOrbs = 19;
                this.heblu.level().broadcastEntityEvent(this.heblu, LEGACY_RAIN_EVENT);
                this.heblu.playSound(ModSounds.HEBLU_SHOOT.get(), 2.0F, 1.0F);
                this.heblu.attackTimer = LEGACY_RAIN_COOLDOWN_TICKS;
                return;
            }
            this.heblu.shootAtTarget(target);
            this.heblu.attackTimer = LEGACY_FIREBALL_COOLDOWN_TICKS;
        }

        @Override
        public void stop() {
            this.heblu.setAttacking(false);
        }
    }

    private static final class RandomFlyGoal extends Goal {
        private final HebluEntity heblu;

        private RandomFlyGoal(HebluEntity heblu) {
            this.heblu = heblu;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.heblu.getFlyingState()
                && !this.heblu.getMoveControl().hasWanted()
                && this.heblu.getRandom().nextInt(LEGACY_RANDOM_MOVE_CHANCE) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            LivingEntity target = this.heblu.getTarget();
            BlockPos origin = this.heblu.blockPosition();
            int mode = 1;
            double speed = LEGACY_RANDOM_MOVE_SPEED_IDLE;
            if (target != null && this.heblu.distanceToSqr(target) > 100.0D) {
                origin = target.blockPosition();
                mode = 2;
                speed = LEGACY_RANDOM_MOVE_SPEED_TARGET;
            } else if (target != null && this.heblu.distanceToSqr(target) < 36.0D) {
                origin = target.blockPosition();
                mode = 3;
                speed = LEGACY_RANDOM_MOVE_SPEED_TARGET;
            }
            for (int i = 0; i < 3; i++) {
                BlockPos pos = randomOffset(origin, mode, this.heblu);
                if (this.heblu.level().isEmptyBlock(pos)) {
                    this.heblu.getMoveControl().setWantedPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, speed);
                    if (target == null) {
                        this.heblu.getLookControl().setLookAt(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    return;
                }
            }
        }

        private static BlockPos randomOffset(BlockPos origin, int mode, HebluEntity heblu) {
            if (mode == 2) {
                return origin.offset(heblu.getRandom().nextInt(6) - 2, heblu.getRandom().nextInt(7) - 2, heblu.getRandom().nextInt(6) - 2);
            }
            if (mode == 3) {
                int x = heblu.getRandom().nextInt(4) + 3;
                int z = heblu.getRandom().nextInt(4) + 3;
                return origin.offset(heblu.getRandom().nextBoolean() ? x : -x, heblu.getRandom().nextInt(5) + 4, heblu.getRandom().nextBoolean() ? z : -z);
            }
            return origin.offset(heblu.getRandom().nextInt(15) - 7, heblu.getRandom().nextInt(11) - 5, heblu.getRandom().nextInt(15) - 7);
        }
    }

    private static final class HebluMoveControl extends MoveControl {
        private final HebluEntity heblu;

        private HebluMoveControl(HebluEntity heblu) {
            super(heblu);
            this.heblu = heblu;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            Vec3 delta = new Vec3(this.wantedX - this.heblu.getX(), this.wantedY - this.heblu.getY(), this.wantedZ - this.heblu.getZ());
            double distance = delta.length();
            if (distance < this.heblu.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.heblu.setDeltaMovement(this.heblu.getDeltaMovement().scale(0.5D));
                return;
            }
            Vec3 movement = this.heblu.getDeltaMovement().add(delta.scale(0.05D * this.speedModifier / distance));
            this.heblu.setDeltaMovement(movement);
            LivingEntity target = this.heblu.getTarget();
            double xLook = target == null ? movement.x : target.getX() - this.heblu.getX();
            double zLook = target == null ? movement.z : target.getZ() - this.heblu.getZ();
            if (xLook * xLook + zLook * zLook > 1.0E-7D) {
                this.heblu.setYRot(-((float) Mth.atan2(xLook, zLook)) * Mth.RAD_TO_DEG);
                this.heblu.yBodyRot = this.heblu.getYRot();
            }
        }
    }
}
