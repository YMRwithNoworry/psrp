package com.dhanantry.scapeandrunparasites.entity.monster.inborn;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import com.dhanantry.scapeandrunparasites.potion.SrpMobEffect;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AtaEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 91;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.ata.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.ata.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 5.0D;
    public static final double LEGACY_ARMOR = 2.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 5.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.6D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.34559D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.85F;
    public static final float LEGACY_HEIGHT = 1.0F;
    public static final float LEGACY_EYE_HEIGHT = 0.8F;
    public static final int LEGACY_XP = 2;
    public static final int LEGACY_TYPE = 5;
    public static final int LEGACY_LIFESPAN_TICKS = 1200;
    public static final float LEGACY_HIJACK_HEALTH_FRACTION = 0.5F;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final int LEGACY_ATTACK_SPEED_TICKS = 6;
    public static final float LEGACY_SKILL_LEAP_POWER = 0.4F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 1.0D;
    public static final int LEGACY_SKILL_MIN_COOLDOWN = 20;
    public static final int LEGACY_SKILL_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_WINDUP = 5;
    public static final byte LEGACY_SKILL_EVENT = 14;
    public static final float LEGACY_LEAP_AT_TARGET_POWER = 0.4F;
    public static final float LEGACY_FALL_DAMAGE_THRESHOLD = 60.0F;
    public static final int LEGACY_VIRAL_TICKS = 120;
    public static final int LEGACY_VIRAL_AMPLIFIER = 2;
    public static final byte LEGACY_BURST_PARTICLE_EVENT = 10;
    public static final byte LEGACY_CONVERT_PARTICLE_EVENT = 11;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(AtaEntity.class, EntityDataSerializers.BYTE);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int skillCooldown;
    private int lifespan;
    private boolean contactConsumed;

    public AtaEntity(EntityType<? extends AtaEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, LEGACY_LEAP_AT_TARGET_POWER));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, LEGACY_MELEE_SPEED, false));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetAtaLiving));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, (byte) 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.lifespan++;
            setBesideClimbableBlock(this.horizontalCollision);
            if (this.skillCooldown > 0) {
                this.skillCooldown--;
            }
            if (this.lifespan > LEGACY_LIFESPAN_TICKS) {
                burstAndDiscard(LEGACY_CONVERT_PARTICLE_EVENT);
            }
        }
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);
        if (!this.level().isClientSide && entity instanceof LivingEntity living) {
            tryContactHijack(living);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return false;
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

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_BURST_PARTICLE_EVENT || id == LEGACY_CONVERT_PARTICLE_EVENT) {
            spawnBurstParticles(id);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpLifeSpan", this.lifespan);
        tag.putInt("SkillCooldown", this.skillCooldown);
        tag.putBoolean("ContactConsumed", this.contactConsumed);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpLifeSpan")) {
            this.lifespan = tag.getInt("SrpLifeSpan");
        }
        if (tag.contains("SkillCooldown")) {
            this.skillCooldown = tag.getInt("SkillCooldown");
        }
        if (tag.contains("ContactConsumed")) {
            this.contactConsumed = tag.getBoolean("ContactConsumed");
        }
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

    private boolean canTargetAtaLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal);
    }

    private void tryContactHijack(LivingEntity target) {
        if (this.contactConsumed || !canHarmLiving(target)) {
            return;
        }
        this.contactConsumed = true;
        SrpMobEffect.applyStackEffect(ModEffects.VIRAL, target, LEGACY_VIRAL_TICKS, LEGACY_VIRAL_AMPLIFIER);
        if (target.getHealth() <= target.getMaxHealth() * LEGACY_HIJACK_HEALTH_FRACTION) {
            burstAndDiscard(LEGACY_CONVERT_PARTICLE_EVENT);
            return;
        }
        target.hurt(this.damageSources().mobAttack(this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
        burstAndDiscard(LEGACY_BURST_PARTICLE_EVENT);
    }

    private void burstAndDiscard(byte particleEvent) {
        this.level().broadcastEntityEvent(this, particleEvent);
        this.playSound(ModSounds.BUTHOL_BOOM.get(), 0.3F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        discard();
    }

    private void spawnBurstParticles(byte particleEvent) {
        if (this.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 12; i++) {
                serverLevel.sendParticles(
                    particleEvent == LEGACY_CONVERT_PARTICLE_EVENT ? ParticleTypes.POOF : ParticleTypes.DAMAGE_INDICATOR,
                    getX(),
                    getY() + getBbHeight() * 0.5D,
                    getZ(),
                    1,
                    this.random.nextGaussian() * 0.15D,
                    this.random.nextGaussian() * 0.15D,
                    this.random.nextGaussian() * 0.15D,
                    0.0D
                );
            }
        } else {
            for (int i = 0; i < 12; i++) {
                this.level().addParticle(
                    particleEvent == LEGACY_CONVERT_PARTICLE_EVENT ? ParticleTypes.POOF : ParticleTypes.DAMAGE_INDICATOR,
                    getX(),
                    getY() + getBbHeight() * 0.5D,
                    getZ(),
                    this.random.nextGaussian() * 0.02D,
                    this.random.nextGaussian() * 0.02D,
                    this.random.nextGaussian() * 0.02D
                );
            }
        }
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

    private static final class SkillLeapGoal extends Goal {
        private final AtaEntity ata;
        private LivingEntity target;
        private int windup;

        private SkillLeapGoal(AtaEntity ata) {
            this.ata = ata;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.ata.getTarget();
            return this.target != null
                && this.ata.skillCooldown <= 0
                && this.ata.distanceToSqr(this.target) > 4.0D
                && this.ata.distanceToSqr(this.target) < 144.0D
                && this.ata.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_WINDUP;
            this.ata.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.ata.lookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0) {
                this.ata.trySkillLeapAt(this.target);
                this.ata.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN
                    + this.ata.getRandom().nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
            }
        }
    }
}
