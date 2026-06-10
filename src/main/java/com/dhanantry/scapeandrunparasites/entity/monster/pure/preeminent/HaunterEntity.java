package com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.entity.projectile.HommingballEntity;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HaunterEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 87;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.pheon.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.pheon.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 360.0D;
    public static final double LEGACY_ARMOR = 15.5D;
    public static final double LEGACY_ATTACK_DAMAGE = 110.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.15D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.283D;
    public static final double LEGACY_FOLLOW_RANGE = 80.0D;
    public static final float LEGACY_WIDTH = 2.0F;
    public static final float LEGACY_HEIGHT = 3.6F;
    public static final float LEGACY_EYE_HEIGHT = 4.7F;
    public static final float LEGACY_SHADOW_RADIUS = 1.3F;
    public static final int LEGACY_TYPE = 63;
    public static final int LEGACY_VARIANT_SKIN = 1;
    public static final double LEGACY_VARIANT_HEALTH_MULTIPLIER = 0.5D;
    public static final double LEGACY_VARIANT_DAMAGE_MULTIPLIER = 1.5D;
    public static final double LEGACY_MELEE_SPEED = 1.0D;
    public static final double LEGACY_AOE_RANGE_XZ = 5.0D;
    public static final double LEGACY_AOE_RANGE_Y = 2.0D;
    public static final double LEGACY_CROWDED_AOE_RANGE_Y = 3.0D;
    public static final int LEGACY_CROWDED_AOE_THRESHOLD = 4;
    public static final double LEGACY_CROWDED_AOE_DAMAGE = 40.0D;
    public static final double LEGACY_RANGED_SPEED = 1.0D;
    public static final int LEGACY_RANGED_INTERVAL = 40;
    public static final float LEGACY_RANGED_RANGE = 40.0F;
    public static final float LEGACY_HOMMING_DAMAGE = 15.0F;
    public static final int LEGACY_PROJECTILE_INTERVAL = 60;
    public static final int LEGACY_PROJECTILE_MIN_DISTANCE = 10;
    public static final int LEGACY_PROJECTILE_COUNT = 3;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(HaunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(HaunterEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int projectileTimer;

    public HaunterEntity(EntityType<? extends HaunterEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 150;
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
        this.goalSelector.addGoal(2, new HaunterMeleeGoal(this));
        this.goalSelector.addGoal(4, new HaunterRangedGoal(this));
        this.goalSelector.addGoal(6, new HaunterProjectileGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetHaunterLiving));
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
        setSkin(LEGACY_VARIANT_SKIN);
        applyVariantAttributes();
        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MOB_SILENCE.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.MOB_SILENCE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MOB_SILENCE.get();
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
                applyVariantAttributes();
            }
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpProjectileTimer")) {
            this.projectileTimer = tag.getInt("SrpProjectileTimer");
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = false;
        AABB area = target.getBoundingBox().inflate(LEGACY_AOE_RANGE_XZ, LEGACY_AOE_RANGE_Y, LEGACY_AOE_RANGE_XZ);
        int livingCount = this.level().getEntitiesOfClass(LivingEntity.class, area, this::canHarmLiving).size();
        if (livingCount > LEGACY_CROWDED_AOE_THRESHOLD) {
            AABB crowdedArea = this.getBoundingBox().inflate(LEGACY_AOE_RANGE_XZ, LEGACY_CROWDED_AOE_RANGE_Y, LEGACY_AOE_RANGE_XZ);
            for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, crowdedArea, this::canHarmLiving)) {
                hit |= living.hurt(damageSources().mobAttack(this), (float) LEGACY_CROWDED_AOE_DAMAGE);
            }
            return hit;
        }
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canHarmLiving)) {
            hit |= super.doHurtTarget(living);
        }
        return hit || super.doHurtTarget(target);
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

    public HommingballEntity getProj(double xPower, double yPower, double zPower) {
        playSound(ModSounds.DORPA_RANGE.get(), 2.0F, 1.0F);
        LivingEntity target = getTarget();
        HommingballEntity projectile = new HommingballEntity(this.level(), this, target, LEGACY_HOMMING_DAMAGE);
        Vec3 look = getLookAngle();
        projectile.setPos(getX() + look.x, getY() + getEyeHeight() - 0.2D, getZ() + look.z);
        projectile.shoot(xPower, yPower, zPower, HommingballEntity.LEGACY_SHOOT_VELOCITY, 0.0F);
        return projectile;
    }

    private void applyVariantAttributes() {
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(LEGACY_HEALTH * LEGACY_VARIANT_HEALTH_MULTIPLIER);
            setHealth(getMaxHealth());
        }
        if (getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(LEGACY_ATTACK_DAMAGE * LEGACY_VARIANT_DAMAGE_MULTIPLIER);
        }
    }

    private boolean canTargetHaunterLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void shootAtTarget(LivingEntity target) {
        Vec3 look = getLookAngle();
        double xPower = target.getX() - (getX() + look.x);
        double yPower = target.getBoundingBox().minY + target.getBbHeight() / 2.0D - (getY() + getEyeHeight() - 0.2D);
        double zPower = target.getZ() - (getZ() + look.z);
        this.level().addFreshEntity(getProj(xPower, yPower, zPower));
    }

    private static final class HaunterMeleeGoal extends MeleeAttackGoal {
        private final HaunterEntity haunter;

        private HaunterMeleeGoal(HaunterEntity haunter) {
            super(haunter, LEGACY_MELEE_SPEED, false);
            this.haunter = haunter;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.haunter.swing(InteractionHand.MAIN_HAND);
                this.haunter.doHurtTarget(target);
            }
        }
    }

    private static final class HaunterRangedGoal extends Goal {
        private final HaunterEntity haunter;
        private int attackTimer;

        private HaunterRangedGoal(HaunterEntity haunter) {
            this.haunter = haunter;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.haunter.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void stop() {
            this.attackTimer = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.haunter.getTarget();
            if (target == null || !target.isAlive()) {
                this.attackTimer = 0;
                return;
            }
            this.haunter.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double range = LEGACY_RANGED_RANGE * LEGACY_RANGED_RANGE;
            if (this.haunter.distanceToSqr(target) <= range && this.haunter.hasLineOfSight(target)) {
                this.haunter.getNavigation().stop();
                this.attackTimer++;
                if (this.attackTimer >= LEGACY_RANGED_INTERVAL) {
                    this.attackTimer = 0;
                    this.haunter.shootAtTarget(target);
                }
            } else {
                this.haunter.getNavigation().moveTo(target, LEGACY_RANGED_SPEED);
                if (this.attackTimer > 0) {
                    this.attackTimer--;
                }
            }
        }
    }

    private static final class HaunterProjectileGoal extends Goal {
        private final HaunterEntity haunter;

        private HaunterProjectileGoal(HaunterEntity haunter) {
            this.haunter = haunter;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.haunter.getTarget();
            this.haunter.projectileTimer++;
            return target != null
                && target.isAlive()
                && this.haunter.projectileTimer >= LEGACY_PROJECTILE_INTERVAL
                && this.haunter.distanceToSqr(target) > LEGACY_PROJECTILE_MIN_DISTANCE * LEGACY_PROJECTILE_MIN_DISTANCE;
        }

        @Override
        public void start() {
            this.haunter.projectileTimer = 0;
            LivingEntity target = this.haunter.getTarget();
            if (target == null) {
                return;
            }
            this.haunter.getLookControl().setLookAt(target, 30.0F, 30.0F);
            for (int i = 0; i < LEGACY_PROJECTILE_COUNT; i++) {
                this.haunter.shootAtTarget(target);
            }
        }
    }
}
