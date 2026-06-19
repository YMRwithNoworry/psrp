package com.dhanantry.scapeandrunparasites.entity.monster.feral;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FerSheepEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 98;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_sheep.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_sheep.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 13D;
    public static final double LEGACY_ARMOR = 1.3D;
    public static final double LEGACY_ATTACK_DAMAGE = 6D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.3D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.23000000417232513D;
    public static final double LEGACY_FOLLOW_RANGE = 32D;
    public static final float LEGACY_WIDTH = 0.9F;
    public static final float LEGACY_HEIGHT = 1.3F;
    public static final float LEGACY_EYE_HEIGHT = 0.8F;
    public static final float LEGACY_SHADOW_RADIUS = 0.5F;
    public static final int LEGACY_TYPE = 11;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final double LEGACY_SWIM_DIVE_SPEED = 0.08D;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final float LEGACY_WATER_LEAP_POWER = 0.7F;
    public static final double LEGACY_WATER_LEAP_SPEED = 1.5D;
    public static final int LEGACY_WATER_LEAP_STATUS = 3;
    public static final int LEGACY_WATER_LEAP_INTERVAL = 20;
    public static final int LEGACY_FOLLOWER_MODE = 1;
    public static final int LEGACY_FOLLOWER_RANGE = 16;

    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public FerSheepEntity(EntityType<? extends FerSheepEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(3, new FerSheepMeleeGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.INFECTEDSHEEP_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDSHEEP_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDSHEEP_DEATH.get();
    }

    @Override
    protected void playStepSound(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    public float getSelfeFlashIntensity2() {
        return 1.0F;
    }

    public float getSelfeFlashIntensity(float partialTick) {
        return 0.0F;
    }

    private static final class FerSheepMeleeGoal extends MeleeAttackGoal {
        private final FerSheepEntity entity;

        private FerSheepMeleeGoal(FerSheepEntity entity) {
            super(entity, LEGACY_MELEE_SPEED, false);
            this.entity = entity;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.entity.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                this.entity.doHurtTarget(target);
            }
        }
    }
}