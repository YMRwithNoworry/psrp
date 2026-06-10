package com.dhanantry.scapeandrunparasites.entity.monster.inborn;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RatholEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 3;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.rathol.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.rathol.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 30.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 25.0D;
    public static final double LEGACY_ARMOR = 5.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.95D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.2D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.3F;
    public static final float LEGACY_HEIGHT = 3.1F;
    public static final float LEGACY_EYE_HEIGHT = 2.6F;
    public static final int LEGACY_XP = 20;
    public static final int LEGACY_TYPE = 41;
    public static final int LEGACY_FUSE_TICKS = 70;
    public static final int LEGACY_VARIANT_SKIN = 1;
    public static final double LEGACY_VARIANT_MOVEMENT_SPEED = 0.3D;
    public static final float LEGACY_EXPLOSION_RADIUS = 4.0F;
    public static final double LEGACY_DEFAULT_EFFECT_RADIUS = 7.0D;
    public static final double LEGACY_VARIANT_EFFECT_RADIUS = 11.0D;
    public static final int LEGACY_DEFAULT_VIRAL_TICKS = 400;
    public static final int LEGACY_DEFAULT_VIRAL_AMPLIFIER = 2;
    public static final int LEGACY_VARIANT_VIRAL_TICKS = 400;
    public static final int LEGACY_VARIANT_VIRAL_AMPLIFIER = 4;
    public static final int LEGACY_VOMIT_TICKS = 600;
    public static final int LEGACY_VOMIT_AMPLIFIER = 0;
    public static final int LEGACY_CLOUD_WAIT_TICKS = 10;
    public static final int LEGACY_CLOUD_POISON_TICKS = 300;
    public static final int LEGACY_CLOUD_COTH_VIRAL_TICKS = 3600;
    public static final float LEGACY_LOW_HEALTH_SELF_STATE_FRACTION = 0.05F;

    private static final EntityDataAccessor<Integer> SWELL_DIR = SynchedEntityData.defineId(RatholEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int oldSwell;
    private int swell;
    private int maxSwell = LEGACY_FUSE_TICKS;
    private int skin;

    public RatholEntity(EntityType<? extends RatholEntity> entityType, Level level) {
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
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, RatholEntity.class));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RatholSwellGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1D, false));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetRatholLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWELL_DIR, -1);
    }

    @Override
    public SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < 0.33D) {
            setSkin(LEGACY_VARIANT_SKIN);
            if (getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(LEGACY_VARIANT_MOVEMENT_SPEED);
            }
        }
        return data;
    }

    @Override
    public void tick() {
        if (isAlive()) {
            this.oldSwell = this.swell;
            if (!this.level().isClientSide && this.swell == 0 && getHealth() < getMaxHealth() * LEGACY_LOW_HEALTH_SELF_STATE_FRACTION) {
                setSwellDir(1);
            }
            int swellDir = getSwellDir();
            if (swellDir > 0 && this.swell == 0) {
                playSound(ModSounds.RATHOL_BOOM.get(), 1.0F, 0.5F);
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
            selfExplode();
            return;
        }
        super.die(damageSource);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("Fuse", (short) this.maxSwell);
        tag.putInt("SrpSwell", this.swell);
        tag.putInt("SrpSkin", this.skin);
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
        return this.skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    private boolean canTargetRatholLiving(LivingEntity target) {
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
        playSound(ModSounds.RATHOL_BOOM.get(), 2.0F, 1.0F);
        discard();
    }

    private void affectNearbyLiving() {
        double radius = getSkin() == LEGACY_VARIANT_SKIN ? LEGACY_VARIANT_EFFECT_RADIUS : LEGACY_DEFAULT_EFFECT_RADIUS;
        int viralAmplifier = getSkin() == LEGACY_VARIANT_SKIN ? LEGACY_VARIANT_VIRAL_AMPLIFIER : LEGACY_DEFAULT_VIRAL_AMPLIFIER;
        int viralTicks = getSkin() == LEGACY_VARIANT_SKIN ? LEGACY_VARIANT_VIRAL_TICKS : LEGACY_DEFAULT_VIRAL_TICKS;
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius))) {
            if (living == this || living instanceof SrpParasiteMob) {
                continue;
            }
            if (getSkin() == LEGACY_VARIANT_SKIN) {
                living.hurt(this.damageSources().magic(), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
            SrpMobEffect.applyStackEffect(ModEffects.VIRAL, living, viralTicks, viralAmplifier);
            living.addEffect(new MobEffectInstance(ModEffects.VOMIT, LEGACY_VOMIT_TICKS, LEGACY_VOMIT_AMPLIFIER, false, true), this);
        }
    }

    private void spawnLingeringCloud() {
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), getX(), getY(), getZ());
        float radius = getBbWidth() * 3.5F;
        cloud.setOwner(this);
        cloud.setRadius(radius);
        cloud.setRadiusOnUse(0.5F);
        cloud.setWaitTime(LEGACY_CLOUD_WAIT_TICKS);
        cloud.setDuration(cloud.getDuration() * 2);
        cloud.setRadiusPerTick(-radius / (float) cloud.getDuration());
        int amplifier = getSkin() == LEGACY_VARIANT_SKIN ? 2 : 0;
        cloud.addEffect(new MobEffectInstance(MobEffects.POISON, LEGACY_CLOUD_POISON_TICKS, amplifier));
        cloud.addEffect(new MobEffectInstance(ModEffects.VIRAL, LEGACY_CLOUD_COTH_VIRAL_TICKS, amplifier, false, false));
        cloud.addEffect(new MobEffectInstance(ModEffects.VOMIT, LEGACY_CLOUD_COTH_VIRAL_TICKS, amplifier, false, false));
        this.level().addFreshEntity(cloud);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + getBbHeight() * 0.5D, getZ(), 12, 0.6D, 0.8D, 0.6D, 0.0D);
        }
    }

    private static final class RatholSwellGoal extends Goal {
        private final RatholEntity rathol;
        private LivingEntity target;

        private RatholSwellGoal(RatholEntity rathol) {
            this.rathol = rathol;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity currentTarget = this.rathol.getTarget();
            return this.rathol.getSwellDir() > 0 || currentTarget != null && this.rathol.distanceToSqr(currentTarget) < 9.0D;
        }

        @Override
        public void start() {
            this.rathol.getNavigation().stop();
            this.target = this.rathol.getTarget();
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
            if (this.rathol.getHealth() < this.rathol.getMaxHealth() * LEGACY_LOW_HEALTH_SELF_STATE_FRACTION) {
                this.rathol.setSwellDir(1);
            } else if (this.target == null) {
                this.rathol.setSwellDir(-1);
            } else if (this.rathol.distanceToSqr(this.target) > 49.0D) {
                this.rathol.setSwellDir(-1);
            } else if (!this.rathol.hasLineOfSight(this.target)) {
                this.rathol.setSwellDir(-1);
            } else {
                this.rathol.setSwellDir(1);
            }
        }
    }
}
