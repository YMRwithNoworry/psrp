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

public class MudoEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 12;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.mudo.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.mudo.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 10.0D;
    public static final double LEGACY_ARMOR = 5.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 5.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.2D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.25D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.85F;
    public static final float LEGACY_HEIGHT = 1.0F;
    public static final float LEGACY_EYE_HEIGHT = 0.8F;
    public static final int LEGACY_XP = 2;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final float LEGACY_SKILL_LEAP_POWER = 0.7F;
    public static final double LEGACY_SKILL_LEAP_SPEED = 2.5D;
    public static final int LEGACY_SKILL_MIN_COOLDOWN = 40;
    public static final int LEGACY_SKILL_MAX_COOLDOWN = 100;
    public static final int LEGACY_SKILL_WINDUP = 5;
    public static final byte LEGACY_SKILL_EVENT = 14;
    public static final float LEGACY_LEAP_AT_TARGET_POWER = 0.4F;
    public static final float LEGACY_FALL_DAMAGE_THRESHOLD = 60.0F;
    public static final int LEGACY_VARIANT_SKIN_VIRAL = 5;
    public static final int LEGACY_VARIANT_SKIN_BLEEDING = 6;

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(MudoEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(MudoEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int skillCooldown;

    public MudoEntity(EntityType<? extends MudoEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, LEGACY_LEAP_AT_TARGET_POWER));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, LEGACY_MELEE_SPEED, false));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetMudoLiving));
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
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return fallDistance >= LEGACY_FALL_DAMAGE_THRESHOLD && super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MUDO_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.MUDO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MUDO_DEATH.get();
    }

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        LivingEntity target = getTarget();
        if (climbing && target != null && (!hasLineOfSight(target) || distanceToSqr(target) > 256.0D || target.getY() + target.getBbHeight() < getY())) {
            climbing = false;
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

    private boolean canTargetMudoLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Villager);
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
        private final MudoEntity mudo;
        private LivingEntity target;
        private int windup;

        private SkillLeapGoal(MudoEntity mudo) {
            this.mudo = mudo;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.mudo.getTarget();
            return this.target != null
                && this.mudo.skillCooldown <= 0
                && this.mudo.distanceToSqr(this.target) > 9.0D
                && this.mudo.distanceToSqr(this.target) < 144.0D
                && this.mudo.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.windup > 0 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            this.windup = LEGACY_SKILL_WINDUP;
            this.mudo.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.mudo.lookAt(this.target, 30.0F, 30.0F);
            this.windup--;
            if (this.windup == 0) {
                this.mudo.trySkillLeapAt(this.target);
                this.mudo.skillCooldown = LEGACY_SKILL_MIN_COOLDOWN
                    + this.mudo.getRandom().nextInt(LEGACY_SKILL_MAX_COOLDOWN - LEGACY_SKILL_MIN_COOLDOWN + 1);
            }
        }
    }
}
