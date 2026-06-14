package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Creeper;
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

public class InfHorseHeadEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 45;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_horse_head.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_horse_head.setRotationAnglesCosmical";
    public static final float LEGACY_WIDTH = 0.7F;
    public static final float LEGACY_HEIGHT = 0.9F;
    public static final float LEGACY_EYE_HEIGHT = 0.8F;
    public static final float LEGACY_SHADOW_RADIUS = 0.35F;
    public static final double LEGACY_HEAD_HEALTH = 0.0D;
    public static final double LEGACY_HEAD_DAMAGE = 0.0D;
    public static final double LEGACY_RUNTIME_HEALTH = 12.0D;
    public static final double LEGACY_RUNTIME_DAMAGE = 4.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.3D;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final int LEGACY_ATTACK_SPEED_TICKS = 15;
    public static final int LEGACY_KILLCOUNT = -10;
    public static final int LEGACY_SKILL_MIN_COOLDOWN = 40;
    public static final int LEGACY_SKILL_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_WINDUP = 3;
    public static final int LEGACY_SKILL_ID = 1;
    public static final int LEGACY_SKILL_STATUS = 14;
    public static final float LEGACY_SKILL_LEAP_STRENGTH = 0.7F;
    public static final double LEGACY_SKILL_LEAP_HEIGHT = 2.5D;
    public static final float LEGACY_LEAP_STRENGTH = 0.4F;
    public static final float LEGACY_AVOID_OR_ATTACK_HEALTH_FRACTION = 0.5F;
    public static final int LEGACY_AVOID_OR_ATTACK_RANGE = 10;
    public static final int LEGACY_AVOID_OR_ATTACK_STATUS = 2;
    public static final float LEGACY_AVOID_ENTITY_RANGE = 8.0F;
    public static final double LEGACY_AVOID_ENTITY_SPEED = 1.3D;
    public static final int LEGACY_CAN_SPAWN_BY_ID_DATA = 0;

    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InfHorseHeadEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int skillCooldown;
    private int skillTicks;
    private int killcount = LEGACY_KILLCOUNT;

    public InfHorseHeadEntity(EntityType<? extends InfHorseHeadEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_RUNTIME_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, LEGACY_MOVEMENT_SPEED)
            .add(Attributes.ATTACK_DAMAGE, LEGACY_RUNTIME_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, LEGACY_FOLLOW_RANGE)
            .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    public int getParasiteIDRegister() {
        return LEGACY_PARASITE_ID;
    }

    public int canSpawnByIDData() {
        return LEGACY_CAN_SPAWN_BY_ID_DATA;
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
        this.goalSelector.addGoal(0, new InfHorseHeadSkillGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, LEGACY_LEAP_STRENGTH));
        this.goalSelector.addGoal(3, new InfHorseHeadMeleeGoal(this));
        this.goalSelector.addGoal(4, new InfHorseHeadAvoidOrAttackGoal(this));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, LivingEntity.class, LEGACY_AVOID_ENTITY_RANGE, LEGACY_AVOID_ENTITY_SPEED, LEGACY_AVOID_ENTITY_SPEED, this::shouldAvoidLiving));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.skillCooldown > 0) {
            this.skillCooldown--;
            if (this.skillCooldown == 0 && getParasiteStatus() == LEGACY_SKILL_STATUS) {
                setParasiteStatus(0);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDHEAD_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INFECTEDHEAD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFECTEDHEAD_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(ModSounds.SMALL_STEPS.get(), 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpSkillCooldown", this.skillCooldown);
        tag.putInt("SrpSkillTicks", this.skillTicks);
        tag.putInt("SrpKillcount", this.killcount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpSkillCooldown")) {
            this.skillCooldown = tag.getInt("SrpSkillCooldown");
        }
        if (tag.contains("SrpSkillTicks")) {
            this.skillTicks = tag.getInt("SrpSkillTicks");
        }
        if (tag.contains("SrpKillcount")) {
            this.killcount = tag.getInt("SrpKillcount");
        }
    }

    public int getParasiteStatus() {
        return this.entityData.get(PARASITE_STATUS);
    }

    public void setParasiteStatus(int status) {
        this.entityData.set(PARASITE_STATUS, status);
    }

    public int getLegacyKillcount() {
        return this.killcount;
    }

    public float getSelfeFlashIntensity(float partialTick) {
        return 0.0F;
    }

    public boolean hasCrudeInhooMTargetSupport() {
        return false;
    }

    private boolean shouldAvoidLiving(LivingEntity living) {
        return living != null
            && !(living instanceof WaterAnimal)
            && !(living instanceof Creeper)
            && !isParasiteAlly(living)
            && !(living instanceof Animal);
    }

    private void doSkillLeap() {
        LivingEntity target = getTarget();
        if (target == null || !target.isAlive()) {
            finishSkill(0);
            return;
        }
        getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (++this.skillTicks < LEGACY_SKILL_WINDUP) {
            setParasiteStatus(LEGACY_SKILL_STATUS);
            return;
        }
        double dx = target.getX() - getX();
        double dz = target.getZ() - getZ();
        double horizontal = Math.max(0.001D, Math.sqrt(dx * dx + dz * dz));
        setDeltaMovement(
            getDeltaMovement().add(dx / horizontal * LEGACY_SKILL_LEAP_STRENGTH, LEGACY_SKILL_LEAP_HEIGHT * 0.1D, dz / horizontal * LEGACY_SKILL_LEAP_STRENGTH)
        );
        this.hasImpulse = true;
        finishSkill(LEGACY_SKILL_STATUS);
    }

    private void finishSkill(int status) {
        this.skillTicks = 0;
        this.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN + this.random.nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
        setParasiteStatus(status);
    }

    private static final class InfHorseHeadMeleeGoal extends MeleeAttackGoal {
        private final InfHorseHeadEntity head;

        private InfHorseHeadMeleeGoal(InfHorseHeadEntity head) {
            super(head, LEGACY_MELEE_SPEED, false);
            this.head = head;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.head.swing(InteractionHand.MAIN_HAND);
                this.head.doHurtTarget(target);
            }
        }
    }

    private static final class InfHorseHeadAvoidOrAttackGoal extends MeleeAttackGoal {
        private final InfHorseHeadEntity head;

        private InfHorseHeadAvoidOrAttackGoal(InfHorseHeadEntity head) {
            super(head, LEGACY_MELEE_SPEED, false);
            this.head = head;
        }

        @Override
        public boolean canUse() {
            return this.head.getHealth() <= this.head.getMaxHealth() * LEGACY_AVOID_OR_ATTACK_HEALTH_FRACTION && super.canUse();
        }

        @Override
        public void start() {
            this.head.setParasiteStatus(LEGACY_AVOID_OR_ATTACK_STATUS);
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
            if (this.head.getParasiteStatus() == LEGACY_AVOID_OR_ATTACK_STATUS) {
                this.head.setParasiteStatus(0);
            }
        }
    }

    private static final class InfHorseHeadSkillGoal extends net.minecraft.world.entity.ai.goal.Goal {
        private final InfHorseHeadEntity head;

        private InfHorseHeadSkillGoal(InfHorseHeadEntity head) {
            this.head = head;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.head.getTarget();
            return target != null
                && target.isAlive()
                && this.head.skillCooldown <= 0
                && this.head.distanceToSqr(target) <= LEGACY_AVOID_OR_ATTACK_RANGE * LEGACY_AVOID_OR_ATTACK_RANGE
                && this.head.hasLineOfSight(target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.head.getParasiteStatus() == LEGACY_SKILL_STATUS && this.head.skillTicks > 0 && this.head.getTarget() != null;
        }

        @Override
        public void stop() {
            if (this.head.getParasiteStatus() == LEGACY_SKILL_STATUS && this.head.skillTicks > 0) {
                this.head.finishSkill(0);
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.head.doSkillLeap();
        }
    }
}
