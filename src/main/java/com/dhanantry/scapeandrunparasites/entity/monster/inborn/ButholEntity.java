package com.dhanantry.scapeandrunparasites.entity.monster.inborn;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ButholEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 11;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.buthol.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.buthol.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 20.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 10.0D;
    public static final double LEGACY_ARMOR = 2.5D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.15D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.4F;
    public static final float LEGACY_HEIGHT = 2.4F;
    public static final float LEGACY_EYE_HEIGHT = 2.4F;
    public static final int LEGACY_XP = 20;
    public static final int LEGACY_TYPE = 31;
    public static final int LEGACY_FUSE_TICKS = 30;
    public static final int LEGACY_VARIANT_SKIN = 1;
    public static final float LEGACY_EXPLOSION_RADIUS = 4.0F;
    public static final double LEGACY_EFFECT_RADIUS = 4.0D;
    public static final int LEGACY_VIRAL_TICKS = 400;
    public static final int LEGACY_VIRAL_AMPLIFIER = 1;
    public static final int LEGACY_VOMIT_TICKS = 400;
    public static final int LEGACY_VOMIT_AMPLIFIER = 0;
    public static final int LEGACY_CLOUD_WAIT_TICKS = 10;
    public static final int LEGACY_CLOUD_POISON_TICKS = 300;
    public static final int LEGACY_CLOUD_COTH_VIRAL_TICKS = 3600;
    public static final float LEGACY_CLOUD_RADIUS_MULTIPLIER = 3.5F;
    public static final float LEGACY_CLOUD_RADIUS_ON_USE = 0.5F;
    public static final double LEGACY_GROUND_LIFT = 5.0D;
    public static final double LEGACY_GROUND_LIFT_SPEED = 0.5D;
    public static final int LEGACY_RANDOM_MOVE_CHANCE = 7;
    public static final int LEGACY_RANDOM_MOVE_HORIZONTAL_RANGE = 15;
    public static final int LEGACY_RANDOM_MOVE_VERTICAL_RANGE = 11;
    public static final double LEGACY_RANDOM_MOVE_SPEED = 0.25D;
    public static final double LEGACY_CHARGE_SPEED = 1.0D;
    public static final double LEGACY_CHARGE_MIN_DISTANCE_SQR = 4.0D;
    public static final double LEGACY_CHARGE_RETARGET_DISTANCE_SQR = 9.0D;

    private static final EntityDataAccessor<Integer> SWELL_DIR = SynchedEntityData.defineId(ButholEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(ButholEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(ButholEntity.class, EntityDataSerializers.BYTE);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);
    private static final int FLAG_CHARGING = 1;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int oldSwell;
    private int swell;
    private int maxSwell = LEGACY_FUSE_TICKS;

    public ButholEntity(EntityType<? extends ButholEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ButholMoveControl(this);
        this.xpReward = LEGACY_XP;
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
        this.goalSelector.addGoal(2, new ButholSwellGoal(this));
        this.goalSelector.addGoal(3, new ChargeAttackGoal(this));
        this.goalSelector.addGoal(6, new RandomFlyGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetButholLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWELL_DIR, -1);
        builder.define(SKIN, 0);
        builder.define(FLAGS, (byte) 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < 0.33D) {
            setSkin(LEGACY_VARIANT_SKIN);
        }
        return data;
    }

    @Override
    public void tick() {
        setNoGravity(true);
        if (isAlive()) {
            this.oldSwell = this.swell;
            if (!this.level().isClientSide && onGround()) {
                this.moveControl.setWantedPosition(getX(), getY() + LEGACY_GROUND_LIFT, getZ(), LEGACY_GROUND_LIFT_SPEED);
            }
            int swellDir = getSwellDir();
            if (swellDir > 0 && this.swell == 0) {
                playSound(ModSounds.BUTHOL_BOOM.get(), 1.0F, 0.5F);
            }
            this.swell += swellDir;
            if (this.swell < 0) {
                this.swell = 0;
            }
            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                selfExplode();
            }
        }
        super.tick();
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource damageSource) {
        if (!this.level().isClientSide && !isOnFire()) {
            setSwellDir(1);
            selfExplode();
            return;
        }
        super.die(damageSource);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        setCharging(false);
        return target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.CARRIER_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.CARRIER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.CARRIER_DEATH.get();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("Fuse", (short) this.maxSwell);
        tag.putInt("SrpSwell", this.swell);
        tag.putInt("SrpSkin", getSkin());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fuse")) {
            this.maxSwell = tag.getShort("Fuse");
        }
        if (tag.contains("SrpSwell")) {
            this.swell = tag.getInt("SrpSwell");
        }
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
        }
    }

    public boolean isSwell() {
        return getSwellDir() > 0;
    }

    public void setSwellDir(int state) {
        this.entityData.set(SWELL_DIR, state);
    }

    public int getSwellDir() {
        return this.entityData.get(SWELL_DIR);
    }

    public float getSelfeFlashIntensity(float partialTicks) {
        return ((float) this.oldSwell + (float) (this.swell - this.oldSwell) * partialTicks) / (float) (this.maxSwell - 2);
    }

    public int getSkin() {
        return this.entityData.get(SKIN);
    }

    public void setSkin(int skin) {
        this.entityData.set(SKIN, skin);
    }

    public boolean isCharging() {
        return getFlag(FLAG_CHARGING);
    }

    public void setCharging(boolean charging) {
        setFlag(FLAG_CHARGING, charging);
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

    private boolean canTargetButholLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void selfExplode() {
        if (this.level().isClientSide || isRemoved()) {
            return;
        }
        boolean griefing = EventHooks.canEntityGrief(this.level(), this);
        this.level().explode(this, getX(), getY(), getZ(), LEGACY_EXPLOSION_RADIUS, false, griefing ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
        affectNearbyLiving();
        spawnLingeringCloud();
        playSound(ModSounds.BUTHOL_BOOM.get(), 1.0F, 1.0F);
        discard();
    }

    private void affectNearbyLiving() {
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(LEGACY_EFFECT_RADIUS))) {
            if (living == this || living instanceof SrpParasiteMob) {
                continue;
            }
            if (getSkin() == LEGACY_VARIANT_SKIN) {
                living.hurt(this.damageSources().magic(), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
            SrpMobEffect.applyStackEffect(ModEffects.VIRAL, living, LEGACY_VIRAL_TICKS, LEGACY_VIRAL_AMPLIFIER);
            living.addEffect(new MobEffectInstance(ModEffects.VOMIT, LEGACY_VOMIT_TICKS, LEGACY_VOMIT_AMPLIFIER, false, true), this);
        }
    }

    private void spawnLingeringCloud() {
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), getX(), getY(), getZ());
        float radius = getBbWidth() * LEGACY_CLOUD_RADIUS_MULTIPLIER;
        int amplifier = getSkin() == LEGACY_VARIANT_SKIN ? 2 : 0;
        cloud.setOwner(this);
        cloud.setRadius(radius);
        cloud.setRadiusOnUse(LEGACY_CLOUD_RADIUS_ON_USE);
        cloud.setWaitTime(LEGACY_CLOUD_WAIT_TICKS);
        cloud.setDuration(Math.max(1, cloud.getDuration() / 2));
        cloud.setRadiusPerTick(-radius / (float) cloud.getDuration());
        cloud.addEffect(new MobEffectInstance(MobEffects.POISON, LEGACY_CLOUD_POISON_TICKS, amplifier));
        cloud.addEffect(new MobEffectInstance(ModEffects.VIRAL, LEGACY_CLOUD_COTH_VIRAL_TICKS, amplifier, false, false));
        this.level().addFreshEntity(cloud);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + getBbHeight() * 0.5D, getZ(), 12, 0.6D, 0.8D, 0.6D, 0.0D);
        }
    }

    private static final class ButholSwellGoal extends Goal {
        private final ButholEntity buthol;
        private LivingEntity target;

        private ButholSwellGoal(ButholEntity buthol) {
            this.buthol = buthol;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity currentTarget = this.buthol.getTarget();
            return this.buthol.getSwellDir() > 0 || currentTarget != null && this.buthol.distanceToSqr(currentTarget) < 9.0D;
        }

        @Override
        public void start() {
            this.buthol.getNavigation().stop();
            this.target = this.buthol.getTarget();
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.buthol.setSwellDir(-1);
            } else if (this.buthol.distanceToSqr(this.target) > 49.0D) {
                this.buthol.setSwellDir(-1);
            } else if (!this.buthol.hasLineOfSight(this.target)) {
                this.buthol.setSwellDir(-1);
            } else {
                this.buthol.setSwellDir(1);
            }
        }
    }

    private static final class ChargeAttackGoal extends Goal {
        private final ButholEntity buthol;
        private LivingEntity target;

        private ChargeAttackGoal(ButholEntity buthol) {
            this.buthol = buthol;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.buthol.getTarget();
            return this.target != null
                && !this.buthol.getMoveControl().hasWanted()
                && this.buthol.getRandom().nextInt(7) == 0
                && this.buthol.distanceToSqr(this.target) > LEGACY_CHARGE_MIN_DISTANCE_SQR;
        }

        @Override
        public boolean canContinueToUse() {
            return this.buthol.getMoveControl().hasWanted()
                && this.buthol.isCharging()
                && this.target != null
                && this.target.isAlive();
        }

        @Override
        public void start() {
            Vec3 targetEye = this.target.getEyePosition();
            this.buthol.getMoveControl().setWantedPosition(targetEye.x, targetEye.y, targetEye.z, LEGACY_CHARGE_SPEED);
            this.buthol.setCharging(true);
        }

        @Override
        public void stop() {
            this.buthol.setCharging(false);
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
            if (this.buthol.getBoundingBox().intersects(this.target.getBoundingBox())) {
                this.buthol.doHurtTarget(this.target);
            } else if (this.buthol.distanceToSqr(this.target) < LEGACY_CHARGE_RETARGET_DISTANCE_SQR) {
                Vec3 targetEye = this.target.getEyePosition();
                this.buthol.getMoveControl().setWantedPosition(targetEye.x, targetEye.y, targetEye.z, LEGACY_CHARGE_SPEED);
            }
        }
    }

    private static final class RandomFlyGoal extends Goal {
        private final ButholEntity buthol;

        private RandomFlyGoal(ButholEntity buthol) {
            this.buthol = buthol;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.buthol.getMoveControl().hasWanted() && this.buthol.getRandom().nextInt(LEGACY_RANDOM_MOVE_CHANCE) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos origin = this.buthol.blockPosition();
            for (int i = 0; i < 3; i++) {
                BlockPos pos = origin.offset(
                    this.buthol.getRandom().nextInt(LEGACY_RANDOM_MOVE_HORIZONTAL_RANGE) - 7,
                    this.buthol.getRandom().nextInt(LEGACY_RANDOM_MOVE_VERTICAL_RANGE) - 5,
                    this.buthol.getRandom().nextInt(LEGACY_RANDOM_MOVE_HORIZONTAL_RANGE) - 7
                );
                if (this.buthol.level().isEmptyBlock(pos)) {
                    this.buthol.getMoveControl().setWantedPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, LEGACY_RANDOM_MOVE_SPEED);
                    if (this.buthol.getTarget() == null) {
                        this.buthol.getLookControl().setLookAt(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    return;
                }
            }
        }
    }

    private static final class ButholMoveControl extends MoveControl {
        private final ButholEntity buthol;

        private ButholMoveControl(ButholEntity buthol) {
            super(buthol);
            this.buthol = buthol;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            Vec3 delta = new Vec3(this.wantedX - this.buthol.getX(), this.wantedY - this.buthol.getY(), this.wantedZ - this.buthol.getZ());
            double distance = delta.length();
            if (distance < this.buthol.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.buthol.setDeltaMovement(this.buthol.getDeltaMovement().scale(0.5D));
                return;
            }
            Vec3 movement = this.buthol.getDeltaMovement().add(delta.scale(0.05D * this.speedModifier / distance));
            this.buthol.setDeltaMovement(movement);
            LivingEntity target = this.buthol.getTarget();
            double xLook = target == null ? movement.x : target.getX() - this.buthol.getX();
            double zLook = target == null ? movement.z : target.getZ() - this.buthol.getZ();
            if (xLook * xLook + zLook * zLook > 1.0E-7D) {
                this.buthol.setYRot(-((float) Mth.atan2(xLook, zLook)) * Mth.RAD_TO_DEG);
                this.buthol.yBodyRot = this.buthol.getYRot();
            }
        }
    }
}
