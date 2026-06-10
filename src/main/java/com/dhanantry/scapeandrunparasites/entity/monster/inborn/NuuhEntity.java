package com.dhanantry.scapeandrunparasites.entity.monster.inborn;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
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

public class NuuhEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 76;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.nuuh.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.nuuh.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 17.0D;
    public static final double LEGACY_ARMOR = 10.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 9.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.6D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.37D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.0F;
    public static final float LEGACY_HEIGHT = 1.0F;
    public static final float LEGACY_EYE_HEIGHT = 0.9F;
    public static final int LEGACY_XP = 75;
    public static final int LEGACY_TYPE = 51;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final int LEGACY_ATTACK_SPEED_TICKS = 6;
    public static final float LEGACY_SKILL_LEAP_POWER = 0.8F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 2.0D;
    public static final int LEGACY_SKILL_MIN_COOLDOWN = 20;
    public static final int LEGACY_SKILL_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_WINDUP = 5;
    public static final byte LEGACY_SKILL_EVENT = 14;
    public static final float LEGACY_LEAP_AT_TARGET_POWER = 0.4F;
    public static final int LEGACY_EVADE_INTERVAL = 10;
    public static final int LEGACY_EVADE_MIN_DISTANCE = 2;
    public static final int LEGACY_EVADE_MAX_DISTANCE = 1;
    public static final double LEGACY_EVADE_SPEED = 1.0D;
    public static final int LEGACY_EVADE_COOLDOWN = 15;
    public static final float LEGACY_FALL_DAMAGE_THRESHOLD = 200.0F;
    public static final int LEGACY_VARIANT_SKIN_VIRAL = 5;
    public static final int LEGACY_VARIANT_SKIN_BLEEDING = 6;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(NuuhEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(NuuhEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int skillCooldown;
    private int evadeCooldown;

    public NuuhEntity(EntityType<? extends NuuhEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SkillLeapGoal(this));
        this.goalSelector.addGoal(2, new EvadeDashGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, LEGACY_LEAP_AT_TARGET_POWER));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, LEGACY_MELEE_SPEED, false));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetNuuhLiving));
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
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            setBesideClimbableBlock(this.horizontalCollision);
            if (this.skillCooldown > 0) {
                this.skillCooldown--;
            }
            if (this.evadeCooldown > 0) {
                this.evadeCooldown--;
            }
        }
    }

    @Override
    public boolean onClimbable() {
        return isBesideClimbableBlock();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.SMALL_STEP.get(), 0.15F, 1.0F);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && getSkin() == LEGACY_VARIANT_SKIN_VIRAL && target instanceof LivingEntity living && !isParasiteAlly(living)) {
            SrpMobEffect.applyStackEffect(ModEffects.VIRAL, living, 100, 0);
        }
        return hurt;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < 0.33D) {
            setSkin(this.random.nextBoolean() ? LEGACY_VARIANT_SKIN_VIRAL : LEGACY_VARIANT_SKIN_BLEEDING);
        }
        this.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN + this.random.nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
        return data;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SkillCooldown", this.skillCooldown);
        tag.putInt("EvadeCooldown", this.evadeCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
        }
        if (tag.contains("SkillCooldown")) {
            this.skillCooldown = tag.getInt("SkillCooldown");
        }
        if (tag.contains("EvadeCooldown")) {
            this.evadeCooldown = tag.getInt("EvadeCooldown");
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return fallDistance >= LEGACY_FALL_DAMAGE_THRESHOLD && super.causeFallDamage(fallDistance, multiplier, source);
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

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        LivingEntity target = getTarget();
        if (climbing && target != null) {
            if ((!hasLineOfSight(target) && distanceToSqr(target) < 100.0D) || target.getY() + 1.0D < getY()) {
                climbing = false;
            }
        }
        byte flags = this.entityData.get(CLIMBING);
        if (climbing) {
            flags = (byte) (flags | 1);
        } else {
            flags = (byte) (flags & -2);
        }
        this.entityData.set(CLIMBING, flags);
    }

    public int getSkin() {
        return this.entityData.get(SKIN);
    }

    public void setSkin(int skin) {
        this.entityData.set(SKIN, skin);
    }

    private boolean canTargetNuuhLiving(LivingEntity target) {
        return canTargetLiving(target)
            && !(target instanceof WaterAnimal)
            && !(target instanceof Animal)
            && !(target instanceof Villager);
    }

    private boolean trySkillLeapAt(LivingEntity target) {
        Vec3 delta = target.position().subtract(this.position());
        Vec3 horizontal = new Vec3(delta.x, 0.0D, delta.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            return false;
        }
        Vec3 leap = horizontal.normalize().scale(LEGACY_SKILL_LEAP_SPEED * 0.35D).add(0.0D, LEGACY_SKILL_LEAP_POWER, 0.0D);
        this.setDeltaMovement(leap);
        this.hasImpulse = true;
        this.level().broadcastEntityEvent(this, LEGACY_SKILL_EVENT);
        return true;
    }

    private boolean tryEvadeFrom(LivingEntity target) {
        Vec3 away = this.position().subtract(target.position());
        Vec3 horizontal = new Vec3(away.x, 0.0D, away.z);
        if (horizontal.horizontalDistanceSqr() < 1.0E-4D) {
            horizontal = new Vec3(this.random.nextDouble() - 0.5D, 0.0D, this.random.nextDouble() - 0.5D);
        }
        Vec3 dash = horizontal.normalize().scale(LEGACY_EVADE_SPEED * 0.35D).add(0.0D, 0.15D, 0.0D);
        this.setDeltaMovement(dash);
        this.hasImpulse = true;
        this.evadeCooldown = LEGACY_EVADE_COOLDOWN;
        return true;
    }

    private static final class SkillLeapGoal extends Goal {
        private final NuuhEntity nuuh;
        private LivingEntity target;
        private int windup;

        private SkillLeapGoal(NuuhEntity nuuh) {
            this.nuuh = nuuh;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.nuuh.getTarget();
            return this.target != null
                && this.nuuh.skillCooldown <= 0
                && this.nuuh.distanceToSqr(this.target) > 9.0D
                && this.nuuh.distanceToSqr(this.target) < 144.0D
                && this.nuuh.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_WINDUP;
            this.nuuh.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.nuuh.lookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0) {
                this.nuuh.trySkillLeapAt(this.target);
                this.nuuh.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN
                    + this.nuuh.getRandom().nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
            }
        }
    }

    private static final class EvadeDashGoal extends Goal {
        private final NuuhEntity nuuh;
        private LivingEntity target;

        private EvadeDashGoal(NuuhEntity nuuh) {
            this.nuuh = nuuh;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.nuuh.getTarget();
            if (this.target == null || this.nuuh.evadeCooldown > 0) {
                return false;
            }
            double distance = this.nuuh.distanceToSqr(this.target);
            double min = Math.min(LEGACY_EVADE_MIN_DISTANCE, LEGACY_EVADE_MAX_DISTANCE);
            double max = Math.max(LEGACY_EVADE_MIN_DISTANCE, LEGACY_EVADE_MAX_DISTANCE);
            return distance >= min * min
                && distance <= max * max
                && this.nuuh.getRandom().nextInt(LEGACY_EVADE_INTERVAL) == 0;
        }

        @Override
        public void start() {
            this.nuuh.tryEvadeFrom(this.target);
        }
    }
}
