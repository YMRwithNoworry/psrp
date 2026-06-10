package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class InfHumanEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 6;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_human.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_human.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 15.0D;
    public static final double LEGACY_ARMOR = 5.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 9.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.1D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.230000004172325D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final double LEGACY_SOUND_EATER_FOLLOW_RANGE = 12.0D;
    public static final double LEGACY_SOUND_EATER_MOVEMENT_SPEED = 0.32D;
    public static final float LEGACY_WIDTH = 0.6F;
    public static final float LEGACY_HEIGHT = 1.95F;
    public static final float LEGACY_EYE_HEIGHT = 1.73F;
    public static final float LEGACY_SHADOW_RADIUS = 0.5F;
    public static final int LEGACY_TYPE = 11;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final double LEGACY_SWIM_DIVE_SPEED = 0.08D;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final float LEGACY_LEAP_STRENGTH = 0.7F;
    public static final int LEGACY_LEAP_STATUS = 3;
    public static final int LEGACY_LEAP_COOLDOWN = 20;
    public static final int LEGACY_FOLLOWER_SEARCH_MODE = 1;
    public static final int LEGACY_FOLLOWER_SEARCH_RANGE = 16;
    public static final int LEGACY_VARIANT_RANDOM_BOUND = 3;
    public static final int LEGACY_VARIANT_SKIN_MIN = 1;
    public static final int LEGACY_VARIANT_SKIN_COUNT = 3;
    public static final int LEGACY_SOUND_EATER_SKIN = 111;
    public static final double LEGACY_SOUND_EATER_CHANCE = 0.009999999776482582D;
    public static final int LEGACY_MELT_WAIT_TICKS = 1000;
    public static final float LEGACY_MELT_START_HEIGHT = 1.95F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE = 0.17F;
    public static final float LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F;
    public static final float LEGACY_MELT_ASIZE_STEP = -0.005F;
    public static final float LEGACY_MELT_HEIGHT_STEP = -0.01F;
    public static final int LEGACY_MELT_SPAWN_TICKS = 127;
    public static final int LEGACY_LESH_LEGS = 0;
    public static final int LEGACY_BLEED_TICKS = 100;
    public static final int LEGACY_BLEED_AMPLIFIER = 0;

    private static final EntityDataAccessor<Float> MELT_HEIGHT = SynchedEntityData.defineId(InfHumanEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> MELTING = SynchedEntityData.defineId(InfHumanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(InfHumanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InfHumanEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float aSize = 1.0F;
    private int meltSoundTicks;
    private int meltWaitTicks;
    private int host = 0;

    public InfHumanEntity(EntityType<? extends InfHumanEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        setPathfindingMalus(PathType.DOOR_WOOD_CLOSED, 0.0F);
        setPathfindingMalus(PathType.DOOR_OPEN, 0.0F);
        setPathfindingMalus(PathType.DOOR_IRON_CLOSED, -1.0F);
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
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, LEGACY_LEAP_STRENGTH));
        this.goalSelector.addGoal(3, new InfHumanMeleeGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MELT_HEIGHT, 0.0F);
        builder.define(MELTING, false);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        tickMelting();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && target instanceof LivingEntity living && !isParasiteAlly(living)) {
            living.addEffect(new MobEffectInstance(ModEffects.BLEED, LEGACY_BLEED_TICKS, LEGACY_BLEED_AMPLIFIER, false, true), this);
        }
        return hurt;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < LEGACY_SOUND_EATER_CHANCE) {
            setSkin(LEGACY_SOUND_EATER_SKIN);
            applySoundEaterAttributes(true);
        } else if (this.random.nextInt(LEGACY_VARIANT_RANDOM_BOUND) == 0) {
            setSkin(LEGACY_VARIANT_SKIN_MIN + this.random.nextInt(LEGACY_VARIANT_SKIN_COUNT));
        } else {
            setSkin(0);
        }
        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDHUMAN_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDHUMAN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDHUMAN_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("SrpMeltHeight", getTHeigh());
        tag.putBoolean("SrpMelting", isMelting());
        tag.putFloat("SrpASize", this.aSize);
        tag.putInt("SrpMeltSoundTicks", this.meltSoundTicks);
        tag.putInt("SrpMeltWaitTicks", this.meltWaitTicks);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("parasitehost", this.host);
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
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
            if (getSkin() == LEGACY_SOUND_EATER_SKIN) {
                applySoundEaterAttributes(false);
            }
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("parasitehost")) {
            this.host = tag.getInt("parasitehost");
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

    private void applySoundEaterAttributes(boolean refillHealth) {
        if (getAttribute(Attributes.FOLLOW_RANGE) != null) {
            getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(LEGACY_SOUND_EATER_FOLLOW_RANGE);
        }
        if (getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(LEGACY_SOUND_EATER_MOVEMENT_SPEED);
        }
        if (refillHealth) {
            setHealth(getMaxHealth());
        }
    }

    private static final class InfHumanMeleeGoal extends MeleeAttackGoal {
        private final InfHumanEntity human;

        private InfHumanMeleeGoal(InfHumanEntity human) {
            super(human, LEGACY_MELEE_SPEED, false);
            this.human = human;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.human.swing(InteractionHand.MAIN_HAND);
                this.human.doHurtTarget(target);
            }
        }
    }
}
