package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.entity.projectile.AngedballEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AngedEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 25;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.anged.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.anged.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 70.0D;
    public static final double LEGACY_ARMOR = 25.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 23.0D;
    public static final double LEGACY_RANGED_ATTACK_DAMAGE = 27.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.2D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.6F;
    public static final float LEGACY_HEIGHT = 3.1F;
    public static final float LEGACY_EYE_HEIGHT = 3.0F;
    public static final int LEGACY_TYPE = 51;
    public static final int LEGACY_HEAVY_SKIN = 7;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final double LEGACY_RANGE_SPEED = 1.5D;
    public static final int LEGACY_RANGE_ATTACK_INTERVAL = 20;
    public static final double LEGACY_RANGE_FACTOR = LEGACY_FOLLOW_RANGE / 2.0D;
    public static final float LEGACY_ATTACK_TIMER_UP_STEP = 0.2F;
    public static final float LEGACY_ATTACK_TIMER_DOWN_STEP = 0.1F;
    public static final double LEGACY_TENDRIL_HEALTH_FRACTION = 0.5D;
    public static final byte LEGACY_LEFT_TENDRIL_DEAD_EVENT = 11;
    public static final byte LEGACY_ATTACK_EVENT = 12;
    public static final byte LEGACY_RIGHT_TENDRIL_DEAD_EVENT = 22;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(AngedEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(AngedEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean attackTimerRising;
    private float leftTendrilHealth;
    private float rightTendrilHealth;

    public AngedEntity(EntityType<? extends AngedEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(2, new AngedMeleeGoal(this));
        this.goalSelector.addGoal(4, new AngedRangedGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetAngedLiving));
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
        super.tick();
        tickAttackTimer();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.STEP_HEAVY.get(), 0.15F, 1.0F);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
        if (hurt) {
            ((LivingEntity) target).knockback(1.0D, getX() - target.getX(), getZ() - target.getZ());
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
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ANGED_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.ANGED_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ANGED_DEATH.get();
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

    public AngedballEntity getProj(double xPower, double yPower, double zPower) {
        playSound(ModSounds.EMANA_SHOOTING.get(), 2.0F, 1.0F);
        return new AngedballEntity(this.level(), this, xPower, yPower, zPower);
    }

    private boolean canTargetAngedLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void tickAttackTimer() {
        if (this.attackTimerRising) {
            this.attackTimer += LEGACY_ATTACK_TIMER_UP_STEP;
            if (this.attackTimer > 1.0F) {
                this.attackTimerRising = false;
            }
        } else if (this.attackTimer > 0.0F) {
            this.attackTimer = Math.max(0.0F, this.attackTimer - LEGACY_ATTACK_TIMER_DOWN_STEP);
        }
    }

    private static final class AngedMeleeGoal extends MeleeAttackGoal {
        private final AngedEntity anged;

        private AngedMeleeGoal(AngedEntity anged) {
            super(anged, LEGACY_MELEE_SPEED, false);
            this.anged = anged;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.anged.swing(InteractionHand.MAIN_HAND);
                this.anged.level().broadcastEntityEvent(this.anged, LEGACY_ATTACK_EVENT);
                this.anged.doHurtTarget(target);
            }
        }
    }

    private static final class AngedRangedGoal extends Goal {
        private final AngedEntity anged;
        private int attackTimer;

        private AngedRangedGoal(AngedEntity anged) {
            this.anged = anged;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.anged.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void stop() {
            this.attackTimer = 0;
            this.anged.setParasiteStatus(0);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.anged.getTarget();
            if (target == null || !target.isAlive()) {
                this.attackTimer = 0;
                return;
            }
            this.anged.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double range = LEGACY_RANGE_FACTOR * LEGACY_RANGE_FACTOR;
            if (this.anged.distanceToSqr(target) <= range && this.anged.hasLineOfSight(target)) {
                this.anged.getNavigation().stop();
                this.attackTimer++;
                if (this.attackTimer >= LEGACY_RANGE_ATTACK_INTERVAL) {
                    this.attackTimer = 0;
                    shoot(target);
                }
            } else {
                this.anged.getNavigation().moveTo(target, LEGACY_RANGE_SPEED);
                if (this.attackTimer > 0) {
                    this.attackTimer--;
                }
            }
        }

        private void shoot(LivingEntity target) {
            Vec3 look = this.anged.getLookAngle();
            double xPower = target.getX() - (this.anged.getX() + look.x);
            double yPower = target.getBoundingBox().minY + target.getBbHeight() / 4.0D - (this.anged.getY() + 1.0D + this.anged.getBbHeight() / 2.0D);
            double zPower = target.getZ() - (this.anged.getZ() + look.z);
            AngedballEntity projectile = this.anged.getProj(xPower, yPower, zPower);
            projectile.setPos(this.anged.getX() + look.x, this.anged.getY() + this.anged.getEyeHeight() - 0.2D, this.anged.getZ() + look.z);
            this.anged.level().addFreshEntity(projectile);
            this.anged.level().broadcastEntityEvent(this.anged, LEGACY_ATTACK_EVENT);
        }
    }
}
