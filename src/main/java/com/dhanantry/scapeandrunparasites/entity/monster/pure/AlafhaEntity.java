package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.entity.projectile.AlafhaBallEntity;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AlafhaEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 9;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.alafha.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.alafha.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 80.0D;
    public static final double LEGACY_ARMOR = 20.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 22.0D;
    public static final double LEGACY_RANGED_ATTACK_DAMAGE = 30.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.4D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.9F;
    public static final float LEGACY_HEIGHT = 2.6F;
    public static final float LEGACY_EYE_HEIGHT = 1.6F;
    public static final int LEGACY_TYPE = 9;
    public static final int LEGACY_HEAVY_SKIN = 7;
    public static final float LEGACY_ADAPTATION_CAP = 0.95F;
    public static final double LEGACY_GROUND_LIFT = 5.0D;
    public static final double LEGACY_GROUND_LIFT_SPEED = 0.5D;
    public static final int LEGACY_PROJECTILE_INTERVAL = 20;
    public static final int LEGACY_PROJECTILE_MIN_DISTANCE = 10;
    public static final int LEGACY_PROJECTILE_COUNT = 4;
    public static final double LEGACY_MELEE_NOT_GROUND_SPEED = 4.5D;
    public static final double LEGACY_MELEE_NOT_GROUND_RANGE = 16.0D;
    public static final double LEGACY_MELEE_NOT_GROUND_VERTICAL = 0.045D;
    public static final double LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.11D;
    public static final double LEGACY_RANDOM_MOVE_SPEED_TARGET = 0.22D;
    public static final double LEGACY_RANDOM_IDLE_RADIUS = 16.0D;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(AlafhaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(AlafhaEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int projectileTimer;

    public AlafhaEntity(EntityType<? extends AlafhaEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new AlafhaMoveControl(this);
        this.xpReward = 40;
        setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
            .add(Attributes.ATTACK_DAMAGE, LEGACY_ATTACK_DAMAGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, LEGACY_KNOCKBACK_RESISTANCE)
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
        this.goalSelector.addGoal(1, new AlafhaProjectileGoal(this));
        this.goalSelector.addGoal(2, new AirMeleeGoal(this));
        this.goalSelector.addGoal(6, new RandomFlyGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetAlafhaLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        setSkin(LEGACY_HEAVY_SKIN);
        return data;
    }

    @Override
    public void tick() {
        setNoGravity(true);
        if (!this.level().isClientSide) {
            if (onGround()) {
                this.moveControl.setWantedPosition(getX(), getY() + LEGACY_GROUND_LIFT, getZ(), LEGACY_GROUND_LIFT_SPEED);
            }
            LivingEntity target = getTarget();
            if (target != null && (!this.level().isEmptyBlock(blockPosition().below()) || !this.level().isEmptyBlock(blockPosition().below(2)))) {
                setDeltaMovement(getDeltaMovement().x, 0.5D, getDeltaMovement().z);
                this.hasImpulse = true;
            }
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
        return ModSounds.ALAFHA_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.ALAFHA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ALAFHA_DEATH.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpProjectileTimer", this.projectileTimer);
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
        if (tag.contains("SrpProjectileTimer")) {
            this.projectileTimer = tag.getInt("SrpProjectileTimer");
        }
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

    public float getSelfeFlashIntensity(float partialTicks) {
        return 0.0F;
    }

    public ThrowableItemProjectile getProj(double xPower, double yPower, double zPower) {
        playSound(ModSounds.ALAFHA_SHOOTING.get(), 2.0F, 1.0F);
        return new AlafhaBallEntity(this.level(), this, xPower, yPower, zPower);
    }

    public void playProjSound() {
        playSound(ModSounds.ALAFHA_SHOOTINGPOST.get(), 2.0F, 1.0F);
    }

    private boolean canTargetAlafhaLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void shootAtTarget(LivingEntity target) {
        Vec3 look = getLookAngle();
        double startX = getX() + look.x;
        double startY = getY() + getEyeHeight() - 0.2D;
        double startZ = getZ() + look.z;
        double xPower = target.getX() - startX;
        double yPower = target.getBoundingBox().minY + target.getBbHeight() / 2.0D - startY;
        double zPower = target.getZ() - startZ;
        ThrowableItemProjectile projectile = getProj(xPower, yPower, zPower);
        projectile.setPos(startX, startY, startZ);
        this.level().addFreshEntity(projectile);
        playProjSound();
    }

    private static final class AlafhaProjectileGoal extends Goal {
        private final AlafhaEntity alafha;

        private AlafhaProjectileGoal(AlafhaEntity alafha) {
            this.alafha = alafha;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.alafha.getTarget();
            this.alafha.projectileTimer++;
            return target != null
                && target.isAlive()
                && this.alafha.projectileTimer >= LEGACY_PROJECTILE_INTERVAL
                && this.alafha.distanceToSqr(target) > LEGACY_PROJECTILE_MIN_DISTANCE * LEGACY_PROJECTILE_MIN_DISTANCE;
        }

        @Override
        public void start() {
            this.alafha.projectileTimer = 0;
            LivingEntity target = this.alafha.getTarget();
            if (target == null) {
                return;
            }
            this.alafha.getLookControl().setLookAt(target, 30.0F, 30.0F);
            for (int i = 0; i < LEGACY_PROJECTILE_COUNT; i++) {
                this.alafha.shootAtTarget(target);
            }
        }
    }

    private static final class AirMeleeGoal extends Goal {
        private final AlafhaEntity alafha;
        private LivingEntity target;

        private AirMeleeGoal(AlafhaEntity alafha) {
            this.alafha = alafha;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.alafha.getTarget();
            return this.target != null && this.target.isAlive() && !this.target.onGround() && this.alafha.distanceToSqr(this.target) < LEGACY_MELEE_NOT_GROUND_RANGE * LEGACY_MELEE_NOT_GROUND_RANGE;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive() && !this.target.onGround() && this.alafha.getMoveControl().hasWanted();
        }

        @Override
        public void start() {
            Vec3 targetPos = this.target.position().add(0.0D, this.target.getBbHeight() * 0.5D, 0.0D);
            this.alafha.getMoveControl().setWantedPosition(targetPos.x, targetPos.y, targetPos.z, LEGACY_MELEE_NOT_GROUND_SPEED);
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
            if (this.alafha.getBoundingBox().intersects(this.target.getBoundingBox())) {
                this.alafha.doHurtTarget(this.target);
                return;
            }
            Vec3 targetPos = this.target.position().add(0.0D, this.target.getBbHeight() * 0.5D + LEGACY_MELEE_NOT_GROUND_VERTICAL, 0.0D);
            this.alafha.getMoveControl().setWantedPosition(targetPos.x, targetPos.y, targetPos.z, LEGACY_MELEE_NOT_GROUND_SPEED);
        }
    }

    private static final class RandomFlyGoal extends Goal {
        private final AlafhaEntity alafha;

        private RandomFlyGoal(AlafhaEntity alafha) {
            this.alafha = alafha;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl moveControl = this.alafha.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            }
            double dx = moveControl.getWantedX() - this.alafha.getX();
            double dy = moveControl.getWantedY() - this.alafha.getY();
            double dz = moveControl.getWantedZ() - this.alafha.getZ();
            double distanceSqr = dx * dx + dy * dy + dz * dz;
            return distanceSqr < 1.0D || distanceSqr > 3600.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            LivingEntity target = this.alafha.getTarget();
            if (target == null) {
                double x = this.alafha.getX() + (this.alafha.getRandom().nextFloat() * 2.0F - 1.0F) * LEGACY_RANDOM_IDLE_RADIUS;
                double y = this.alafha.getY() + (this.alafha.getRandom().nextFloat() * 2.0F - 1.0F) * LEGACY_RANDOM_IDLE_RADIUS;
                double z = this.alafha.getZ() + (this.alafha.getRandom().nextFloat() * 2.0F - 1.0F) * LEGACY_RANDOM_IDLE_RADIUS;
                this.alafha.getMoveControl().setWantedPosition(x, y, z, 0.5D);
                return;
            }

            BlockPos origin = this.alafha.blockPosition();
            int mode = 1;
            double speed = LEGACY_RANDOM_MOVE_SPEED_IDLE;
            if (this.alafha.distanceToSqr(target) > 400.0D) {
                origin = target.blockPosition();
                mode = 2;
                speed = LEGACY_RANDOM_MOVE_SPEED_TARGET;
            } else if (this.alafha.distanceToSqr(target) < 100.0D) {
                origin = target.blockPosition();
                mode = 3;
                speed = LEGACY_RANDOM_MOVE_SPEED_TARGET;
            }
            for (int i = 0; i < 3; i++) {
                BlockPos pos = randomOffset(origin, mode, this.alafha);
                if (this.alafha.level().isEmptyBlock(pos)) {
                    this.alafha.getMoveControl().setWantedPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, speed);
                    return;
                }
            }
        }

        private static BlockPos randomOffset(BlockPos origin, int mode, AlafhaEntity alafha) {
            if (mode == 2) {
                return origin.offset(alafha.getRandom().nextInt(6) - 2, alafha.getRandom().nextInt(7) - 2, alafha.getRandom().nextInt(6) - 2);
            }
            if (mode == 3) {
                return origin.offset(alafha.getRandom().nextInt(4) + 3, alafha.getRandom().nextInt(5) + 4, alafha.getRandom().nextInt(4) + 3);
            }
            return origin.offset(alafha.getRandom().nextInt(15) - 7, alafha.getRandom().nextInt(9) - 5, alafha.getRandom().nextInt(15) - 7);
        }
    }

    private static final class AlafhaMoveControl extends MoveControl {
        private final AlafhaEntity alafha;

        private AlafhaMoveControl(AlafhaEntity alafha) {
            super(alafha);
            this.alafha = alafha;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            Vec3 delta = new Vec3(this.wantedX - this.alafha.getX(), this.wantedY - this.alafha.getY(), this.wantedZ - this.alafha.getZ());
            double distance = delta.length();
            if (distance < this.alafha.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.alafha.setDeltaMovement(this.alafha.getDeltaMovement().scale(0.5D));
                return;
            }
            Vec3 movement = this.alafha.getDeltaMovement().add(delta.scale(0.05D * this.speedModifier / distance));
            this.alafha.setDeltaMovement(movement);
            LivingEntity target = this.alafha.getTarget();
            double xLook = target == null ? movement.x : target.getX() - this.alafha.getX();
            double zLook = target == null ? movement.z : target.getZ() - this.alafha.getZ();
            if (xLook * xLook + zLook * zLook > 1.0E-7D) {
                this.alafha.setYRot(-((float) Mth.atan2(xLook, zLook)) * Mth.RAD_TO_DEG);
                this.alafha.yBodyRot = this.alafha.getYRot();
            }
        }
    }
}
