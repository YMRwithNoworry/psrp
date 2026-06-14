package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class InfPigEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 26;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_pig.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_pig.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 9.0D;
    public static final double LEGACY_ARMOR = 0.1D;
    public static final double LEGACY_ATTACK_DAMAGE = 3.5D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.1D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.25D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.9F;
    public static final float LEGACY_HEIGHT = 0.9F;
    public static final float LEGACY_EYE_HEIGHT = 0.8F;
    public static final float LEGACY_SHADOW_RADIUS = 0.5F;
    public static final int LEGACY_TYPE = 15;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final int LEGACY_FUSE_TIME = 40;
    public static final double LEGACY_SWIM_DIVE_SPEED = 0.08D;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final int LEGACY_FOLLOWER_SEARCH_MODE = 1;
    public static final int LEGACY_FOLLOWER_SEARCH_RANGE = 16;
    public static final int LEGACY_MELT_WAIT_TICKS = 1000;
    public static final float LEGACY_MELT_START_HEIGHT = 0.9F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE = 0.17F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F;
    public static final float LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F;
    public static final float LEGACY_MELT_ASIZE_STEP = -0.005F;
    public static final float LEGACY_MELT_HEIGHT_STEP = -0.01F;
    public static final int LEGACY_MELT_SPAWN_TICKS = 20;
    public static final int LEGACY_LESH_LEGS = 0;
    public static final double LEGACY_HEAD_HEALTH = 0.0D;
    public static final double LEGACY_HEAD_DAMAGE = 0.0D;
    public static final double LEGACY_HEAD_CHANCE = 0.0D;
    public static final int LEGACY_LESH_V = 0;

    private static final EntityDataAccessor<Float> MELT_HEIGHT = SynchedEntityData.defineId(InfPigEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> MELTING = SynchedEntityData.defineId(InfPigEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InfPigEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float aSize = 1.0F;
    private int soundTicks;
    private int meltWaitTicks;

    public InfPigEntity(EntityType<? extends InfPigEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(3, new InfPigMeleeGoal(this));
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
    }

    @Override
    public void tick() {
        super.tick();
        tickMelting();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDPIG_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDPIG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDPIG_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("SrpMeltHeight", getTHeigh());
        tag.putBoolean("SrpMelting", isMelting());
        tag.putFloat("SrpASize", this.aSize);
        tag.putInt("SrpMeltSoundTicks", this.soundTicks);
        tag.putInt("SrpMeltWaitTicks", this.meltWaitTicks);
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
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
            this.soundTicks = tag.getInt("SrpMeltSoundTicks");
        }
        if (tag.contains("SrpMeltWaitTicks")) {
            this.meltWaitTicks = tag.getInt("SrpMeltWaitTicks");
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
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
        if (this.soundTicks % 20 == 0) {
            playSound(ModSounds.INFECTED_MELT.get(), 1.0F, 1.0F);
        }
        this.soundTicks++;
        if (getTHeigh() > LEGACY_MELT_THRESHOLD_HEIGHT) {
            setaSize(LEGACY_MELT_ASIZE_STEP);
            setTHeigh(LEGACY_MELT_HEIGHT_STEP);
        }
        if (!this.level().isClientSide && (getTHeigh() <= LEGACY_MELT_THRESHOLD_HEIGHT || this.soundTicks >= LEGACY_MELT_SPAWN_TICKS)) {
            setMelting(false);
        }
    }

    private static final class InfPigMeleeGoal extends MeleeAttackGoal {
        private final InfPigEntity pig;

        private InfPigMeleeGoal(InfPigEntity pig) {
            super(pig, LEGACY_MELEE_SPEED, false);
            this.pig = pig;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.pig.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                this.pig.doHurtTarget(target);
            }
        }
    }
}
