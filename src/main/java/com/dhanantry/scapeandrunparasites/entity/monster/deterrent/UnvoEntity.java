package com.dhanantry.scapeandrunparasites.entity.monster.deterrent;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.entity.projectile.SpineballEntity;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class UnvoEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 30;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.unvo.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.unvo.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 30.0D;
    public static final double LEGACY_ARMOR = 10.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 5.0D;
    public static final float LEGACY_RANGE_ATTACK_DAMAGE = 25.0F;
    public static final double LEGACY_MOVEMENT_SPEED = 0.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.7F;
    public static final float LEGACY_HEIGHT = 4.1F;
    public static final float LEGACY_EYE_HEIGHT = 3.6F;
    public static final int LEGACY_XP = 110;
    public static final int LEGACY_TYPE = 40;
    public static final double LEGACY_BURIED_TIME = 5.1D;
    public static final int LEGACY_PROJECTILE_COOLDOWN = 20;
    public static final int LEGACY_PROJECTILE_SOUND_WINDUP = 10;
    public static final int LEGACY_PROJECTILE_TICK_INTERVAL = 1;
    public static final int LEGACY_PROJECTILE_SHOTS = 3;
    public static final double LEGACY_PROJECTILE_RANGE_SQR = 4225.0D;
    public static final int LEGACY_PROJECTILE_STATUS = 1;
    public static final int LEGACY_POISON_DURATION_SECONDS = 7;
    public static final int LEGACY_POISON_AMPLIFIER_CONFIG = 1;
    public static final double LEGACY_GEAR_DAMAGE = 0.04D;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(UnvoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(UnvoEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public UnvoEntity(EntityType<? extends UnvoEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(2, new ProjectileAttackGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetUnvoLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
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
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.UNVO_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.UNVO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.UNVO_DEATH.get();
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

    public void playProjSound() {
        playSound(ModSounds.UNVO_SHOOTING.get(), 2.0F, 1.0F);
    }

    public SpineballEntity getProj(double xPower, double yPower, double zPower) {
        playSound(ModSounds.EMANA_SHOOTING.get(), 2.0F, 1.0F);
        SpineballEntity spineball = new SpineballEntity(this.level(), this, xPower, yPower, zPower);
        spineball.setDurationAmplifier(LEGACY_POISON_DURATION_SECONDS, LEGACY_POISON_AMPLIFIER_CONFIG);
        spineball.setGearDamage(LEGACY_GEAR_DAMAGE);
        return spineball;
    }

    private boolean canTargetUnvoLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private static final class ProjectileAttackGoal extends Goal {
        private final UnvoEntity unvo;
        private int attackTimer;
        private int shootingUpdate;

        private ProjectileAttackGoal(UnvoEntity unvo) {
            this.unvo = unvo;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.unvo.getTarget() != null;
        }

        @Override
        public void stop() {
            this.shootingUpdate = 0;
            this.attackTimer = 0;
            this.unvo.setParasiteStatus(0);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.unvo.getTarget();
            if (target == null || !target.isAlive()) {
                this.attackTimer = 0;
                return;
            }
            this.unvo.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.unvo.getParasiteStatus() == 0) {
                this.unvo.setParasiteStatus(LEGACY_PROJECTILE_STATUS);
            }
            if (this.unvo.distanceToSqr(target) < LEGACY_PROJECTILE_RANGE_SQR && this.unvo.hasLineOfSight(target)) {
                this.attackTimer++;
                if (this.attackTimer == LEGACY_PROJECTILE_COOLDOWN - LEGACY_PROJECTILE_SOUND_WINDUP) {
                    this.unvo.playProjSound();
                }
                if (this.attackTimer > LEGACY_PROJECTILE_COOLDOWN) {
                    if (this.shootingUpdate < LEGACY_PROJECTILE_SHOTS) {
                        if (this.attackTimer % LEGACY_PROJECTILE_TICK_INTERVAL == 0) {
                            shoot(target);
                            this.shootingUpdate++;
                        }
                    } else {
                        this.attackTimer = 0;
                        this.shootingUpdate = 0;
                    }
                }
            } else if (this.attackTimer > 0) {
                this.attackTimer--;
            }
        }

        private void shoot(LivingEntity target) {
            Vec3 look = this.unvo.getLookAngle();
            double xPower = target.getX() - (this.unvo.getX() + look.x);
            double yPower = target.getBoundingBox().minY + target.getBbHeight() / 2.0D - (this.unvo.getY() + this.unvo.getEyeHeight() - 0.2D);
            double zPower = target.getZ() - (this.unvo.getZ() + look.z);
            SpineballEntity spineball = this.unvo.getProj(xPower, yPower, zPower);
            spineball.setPos(this.unvo.getX() + look.x, this.unvo.getY() + this.unvo.getEyeHeight() - 0.2D, this.unvo.getZ() + look.z);
            this.unvo.level().addFreshEntity(spineball);
        }
    }
}
