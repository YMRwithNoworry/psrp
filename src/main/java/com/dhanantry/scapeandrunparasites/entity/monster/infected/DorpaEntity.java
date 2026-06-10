package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.entity.projectile.WebballEntity;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DorpaEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 2;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.dorpa.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.dorpa.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 22.0D;
    public static final double LEGACY_ARMOR = 3.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 9.0D;
    public static final double LEGACY_RANGED_ATTACK_DAMAGE = 4.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.5D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.27D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.9F;
    public static final float LEGACY_HEIGHT = 2.1F;
    public static final float LEGACY_EYE_HEIGHT = 1.75F;
    public static final float LEGACY_SHADOW_RADIUS = 1.2F;
    public static final int LEGACY_TYPE = 14;
    public static final int LEGACY_KILLCOUNT = -10;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final int LEGACY_VARIANT_SKIN = 1;
    public static final int LEGACY_VARIANT_RANDOM_BOUND = 3;
    public static final double LEGACY_VARIANT_HEALTH_MULTIPLIER = 0.5D;
    public static final double LEGACY_VARIANT_DAMAGE_MULTIPLIER = 1.25D;
    public static final int LEGACY_WITHER_TICKS = 40;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final int LEGACY_PROJECTILE_INTERVAL = 60;
    public static final int LEGACY_PROJECTILE_MIN_DISTANCE = 15;
    public static final int LEGACY_PROJECTILE_COUNT = 3;
    public static final byte LEGACY_WEBBALL_TYPE = WebballEntity.LEGACY_TYPE_ONE;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(DorpaEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(DorpaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(DorpaEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int projectileTimer;

    public DorpaEntity(EntityType<? extends DorpaEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
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
        this.goalSelector.addGoal(3, new DorpaMeleeGoal(this));
        this.goalSelector.addGoal(6, new DorpaWebballGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
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
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    public boolean onClimbable() {
        return isBesideClimbableBlock();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && getSkin() == LEGACY_VARIANT_SKIN && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, LEGACY_WITHER_TICKS), this);
        }
        return hurt;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextInt(LEGACY_VARIANT_RANDOM_BOUND) == 0) {
            setSkin(LEGACY_VARIANT_SKIN);
            applyVariantAttributes(true);
        }
        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDSPIDER_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDSPIDER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDSPIDER_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(ModSounds.INFECTEDSPIDER_STEP.get(), 0.15F, 1.0F);
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
            if (getSkin() == LEGACY_VARIANT_SKIN) {
                applyVariantAttributes(false);
            }
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

    public WebballEntity getProj(double xPower, double yPower, double zPower) {
        playSound(ModSounds.DORPA_RANGE.get(), 2.0F, 1.0F);
        return new WebballEntity(this.level(), this, xPower, yPower, zPower, LEGACY_WEBBALL_TYPE);
    }

    private void applyVariantAttributes(boolean refillHealth) {
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(LEGACY_HEALTH * LEGACY_VARIANT_HEALTH_MULTIPLIER);
            if (refillHealth) {
                setHealth(getMaxHealth());
            }
        }
        if (getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(LEGACY_ATTACK_DAMAGE * LEGACY_VARIANT_DAMAGE_MULTIPLIER);
        }
    }

    private void shootAtTarget(LivingEntity target) {
        Vec3 look = getLookAngle();
        double startX = getX() + look.x;
        double startY = getY() + getEyeHeight() - 0.2D;
        double startZ = getZ() + look.z;
        double xPower = target.getX() - startX;
        double yPower = target.getBoundingBox().minY + target.getBbHeight() / 3.0D - (getY() + 1.0D + getBbHeight() / 2.0D);
        double zPower = target.getZ() - startZ;
        WebballEntity webball = getProj(xPower, yPower, zPower);
        webball.setPos(startX, startY, startZ);
        this.level().addFreshEntity(webball);
    }

    private static final class DorpaMeleeGoal extends MeleeAttackGoal {
        private final DorpaEntity dorpa;

        private DorpaMeleeGoal(DorpaEntity dorpa) {
            super(dorpa, LEGACY_MELEE_SPEED, false);
            this.dorpa = dorpa;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.dorpa.swing(InteractionHand.MAIN_HAND);
                this.dorpa.doHurtTarget(target);
            }
        }
    }

    private static final class DorpaWebballGoal extends Goal {
        private final DorpaEntity dorpa;

        private DorpaWebballGoal(DorpaEntity dorpa) {
            this.dorpa = dorpa;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.dorpa.getTarget();
            this.dorpa.projectileTimer++;
            return target != null
                && target.isAlive()
                && this.dorpa.projectileTimer >= LEGACY_PROJECTILE_INTERVAL
                && this.dorpa.distanceToSqr(target) > LEGACY_PROJECTILE_MIN_DISTANCE * LEGACY_PROJECTILE_MIN_DISTANCE
                && this.dorpa.hasLineOfSight(target);
        }

        @Override
        public void start() {
            this.dorpa.projectileTimer = 0;
            LivingEntity target = this.dorpa.getTarget();
            if (target == null) {
                return;
            }
            this.dorpa.getLookControl().setLookAt(target, 30.0F, 30.0F);
            for (int i = 0; i < LEGACY_PROJECTILE_COUNT; i++) {
                this.dorpa.shootAtTarget(target);
            }
        }
    }
}
