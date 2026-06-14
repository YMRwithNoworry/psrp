package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class InfHorseEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 44;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_horse.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_horse.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 24.0D;
    public static final double LEGACY_ARMOR = 0.5D;
    public static final double LEGACY_ATTACK_DAMAGE = 7.5D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.1D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.26999999701976773D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.3964844F;
    public static final float LEGACY_HEIGHT = 1.6F;
    public static final float LEGACY_EYE_HEIGHT = 1.3F;
    public static final float LEGACY_SHADOW_RADIUS = 0.75F;
    public static final int LEGACY_TYPE = 11;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final int LEGACY_FUSE_TIME = 70;
    public static final double LEGACY_SWIM_DIVE_SPEED = 0.08D;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final int LEGACY_FOLLOWER_SEARCH_MODE = 1;
    public static final int LEGACY_FOLLOWER_SEARCH_RANGE = 16;
    public static final double LEGACY_SWELL_START_DISTANCE = 5.0D;
    public static final double LEGACY_SWELL_STOP_DISTANCE = 7.0D;
    public static final double LEGACY_EXPLOSION_AABB_INFLATE = 3.5D;
    public static final float LEGACY_EXPLOSION_DAMAGE_MULTIPLIER = 1.0F;
    public static final int LEGACY_CLOUD_WAIT_TICKS = 10;
    public static final int LEGACY_CLOUD_POISON_TICKS = 300;
    public static final int LEGACY_CLOUD_COTH_TICKS = 3600;
    public static final float LEGACY_CLOUD_RADIUS_MULTIPLIER = 1.5F;
    public static final float LEGACY_CLOUD_RADIUS_ON_USE = 0.5F;
    public static final double LEGACY_SELF_STATE_HEALTH_FRACTION = 0.5D;
    public static final int LEGACY_MELT_WAIT_TICKS = 1000;
    public static final float LEGACY_MELT_START_HEIGHT = 1.6F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE = 0.17F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F;
    public static final float LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F;
    public static final float LEGACY_MELT_ASIZE_STEP = -0.005F;
    public static final float LEGACY_MELT_HEIGHT_STEP = -0.01F;
    public static final int LEGACY_MELT_SPAWN_TICKS = 73;
    public static final int LEGACY_LESH_LEGS = 0;
    public static final double LEGACY_HEAD_HEALTH = 0.0D;
    public static final double LEGACY_HEAD_DAMAGE = 0.0D;
    public static final double LEGACY_HEAD_CHANCE = 0.0D;
    public static final int LEGACY_LESH_V = 0;

    private static final EntityDataAccessor<Float> MELT_HEIGHT = SynchedEntityData.defineId(InfHorseEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> MELTING = SynchedEntityData.defineId(InfHorseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InfHorseEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SWELL_DIR = SynchedEntityData.defineId(InfHorseEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float aSize = 1.0F;
    private int meltSoundTicks;
    private int meltWaitTicks;
    private int oldSwell;
    private int swell;
    private int maxSwell = LEGACY_FUSE_TIME;

    public InfHorseEntity(EntityType<? extends InfHorseEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(2, new InfHorseSwellGoal(this));
        this.goalSelector.addGoal(3, new InfHorseMeleeGoal(this));
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
        builder.define(SWELL_DIR, -1);
    }

    @Override
    public void tick() {
        if (isAlive()) {
            this.oldSwell = this.swell;
            int swellDir = getSwellDir();
            if (swellDir > 0 && this.swell == 0) {
                playSound(ModSounds.INFECTEDHORSE_SA1.get(), 1.0F, 0.5F);
            }
            this.swell += swellDir;
            if (this.swell < 0) {
                this.swell = 0;
            }
            if (!this.level().isClientSide && this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                selfExplode();
            }
        }
        super.tick();
        tickMelting();
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource damageSource) {
        if (!this.level().isClientSide && getTHeigh() < LEGACY_DEATH_HEIGHT_RESTORE_CAP) {
            setTHeigh(LEGACY_DEATH_HEIGHT_RESTORE);
        }
        super.die(damageSource);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDHORSE_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDHORSE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDHORSE_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.HORSE_STEP, 0.15F, 1.0F);
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
        tag.putShort("Fuse", (short) this.maxSwell);
        tag.putInt("SrpSwell", this.swell);
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
        if (tag.contains("Fuse")) {
            this.maxSwell = tag.getShort("Fuse");
        }
        if (tag.contains("SrpSwell")) {
            this.swell = tag.getInt("SrpSwell");
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
        return ((float) this.oldSwell + (float) (this.swell - this.oldSwell) * partialTick) / (float) (this.maxSwell - 2);
    }

    public int getParasiteStatus() {
        return this.entityData.get(PARASITE_STATUS);
    }

    public void setParasiteStatus(int status) {
        this.entityData.set(PARASITE_STATUS, status);
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

    public void setSelfeState(int state) {
        if (getHealth() <= getMaxHealth() * LEGACY_SELF_STATE_HEALTH_FRACTION) {
            setSwellDir(state);
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

    private void selfExplode() {
        if (this.level().isClientSide || isRemoved()) {
            return;
        }
        affectNearbyLiving();
        spawnLingeringCloud();
        playSound(ModSounds.INFECTEDHORSE_SA2.get(), 2.0F, 1.0F);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + getBbHeight() * 0.5D, getZ(), 12, 0.6D, 0.8D, 0.6D, 0.0D);
        }
        discard();
    }

    private void affectNearbyLiving() {
        AABB area = getBoundingBox().inflate(LEGACY_EXPLOSION_AABB_INFLATE);
        float damage = (float) getAttributeValue(Attributes.ATTACK_DAMAGE) * LEGACY_EXPLOSION_DAMAGE_MULTIPLIER;
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canExplosionHit)) {
            living.hurt(this.damageSources().magic(), damage);
        }
    }

    private boolean canExplosionHit(LivingEntity living) {
        return canHarmLiving(living);
    }

    private void spawnLingeringCloud() {
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), getX(), getY(), getZ());
        float radius = getBbWidth() * LEGACY_CLOUD_RADIUS_MULTIPLIER;
        cloud.setOwner(this);
        cloud.setRadius(radius);
        cloud.setRadiusOnUse(LEGACY_CLOUD_RADIUS_ON_USE);
        cloud.setWaitTime(LEGACY_CLOUD_WAIT_TICKS);
        cloud.setDuration(cloud.getDuration() * 2);
        cloud.setRadiusPerTick(-radius / (float) cloud.getDuration());
        cloud.addEffect(new MobEffectInstance(MobEffects.POISON, LEGACY_CLOUD_POISON_TICKS, 0));
        cloud.addEffect(new MobEffectInstance(ModEffects.VIRAL, LEGACY_CLOUD_COTH_TICKS, 0, false, false));
        this.level().addFreshEntity(cloud);
    }

    private static final class InfHorseMeleeGoal extends MeleeAttackGoal {
        private final InfHorseEntity horse;

        private InfHorseMeleeGoal(InfHorseEntity horse) {
            super(horse, LEGACY_MELEE_SPEED, false);
            this.horse = horse;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.horse.swing(InteractionHand.MAIN_HAND);
                this.horse.doHurtTarget(target);
            }
        }
    }

    private static final class InfHorseSwellGoal extends Goal {
        private final InfHorseEntity horse;
        private LivingEntity target;

        private InfHorseSwellGoal(InfHorseEntity horse) {
            this.horse = horse;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity currentTarget = this.horse.getTarget();
            return this.horse.getSwellDir() > 0
                || currentTarget != null && this.horse.distanceToSqr(currentTarget) < LEGACY_SWELL_START_DISTANCE * LEGACY_SWELL_START_DISTANCE;
        }

        @Override
        public void start() {
            this.horse.getNavigation().stop();
            this.target = this.horse.getTarget();
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
                this.horse.setSwellDir(-1);
            } else if (this.horse.distanceToSqr(this.target) > LEGACY_SWELL_STOP_DISTANCE * LEGACY_SWELL_STOP_DISTANCE) {
                this.horse.setSwellDir(-1);
            } else if (!this.horse.hasLineOfSight(this.target)) {
                this.horse.setSwellDir(-1);
            } else {
                this.horse.setSwellDir(1);
            }
        }
    }
}
