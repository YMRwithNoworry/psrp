package com.dhanantry.scapeandrunparasites.entity.monster.deterrent;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NakEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 72;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.nak.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.nak.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 15.0D;
    public static final double LEGACY_ARMOR = 10.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 6.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_FOLLOW_RANGE = 6.0D;
    public static final float LEGACY_WIDTH = 0.7F;
    public static final float LEGACY_HEIGHT = 2.5F;
    public static final float LEGACY_EYE_HEIGHT = 1.8F;
    public static final int LEGACY_XP = 0;
    public static final int LEGACY_TYPE = 40;
    public static final double LEGACY_BURIED_TIME = 4.0D;
    public static final double LEGACY_GRAB_RANGE_SQR = 25.0D;
    public static final double LEGACY_MIN_PULL_RANGE_SQR = 1.0D;
    public static final int LEGACY_SEARCHING_STATUS = 2;
    public static final int LEGACY_GRABBING_STATUS = 3;
    public static final int LEGACY_TARGET_LOST_RETREAT_TICKS = 60;
    public static final int LEGACY_INITIAL_SLOW_TICKS = 80;
    public static final int LEGACY_PULL_SLOW_TICKS = 20;
    public static final int LEGACY_SLOW_AMPLIFIER = 2;
    public static final double LEGACY_PULL_STRENGTH = 0.5D;
    public static final double LEGACY_PULL_DAMPING = 0.5D;
    public static final float LEGACY_ATTACK_TIMER_UP_STEP = 0.15F;
    public static final float LEGACY_ATTACK_TIMER_DOWN_STEP = 0.2F;
    public static final byte LEGACY_ATTACK_EVENT = 12;
    public static final byte LEGACY_RETREAT_EVENT = 51;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(NakEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(NakEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_ENTITY = SynchedEntityData.defineId(NakEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean up;
    private int targetLostTicks;

    public NakEntity(EntityType<? extends NakEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetNakLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
        builder.define(TARGET_ENTITY, 0);
    }

    @Override
    public void tick() {
        super.tick();
        tickAttackTimer();
        if (!this.level().isClientSide) {
            tickLegacyGrab();
        }
        pullTargetedEntity();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        LivingEntity target = getTargetedEntity();
        if (direct instanceof Projectile && target != null && target.isAlive()) {
            target.hurt(source, amount * 2.0F);
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_ATTACK_EVENT) {
            this.up = true;
            this.attackTimer = 0.0F;
        } else if (id == LEGACY_RETREAT_EVENT) {
            setParasiteStatus(LEGACY_SEARCHING_STATUS);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpTargetEntity", getTargetedEntityId());
        tag.putFloat("SrpAttackTimer", this.attackTimer);
        tag.putBoolean("SrpAttackUp", this.up);
        tag.putInt("SrpTargetLostTicks", this.targetLostTicks);
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
        if (tag.contains("SrpTargetEntity")) {
            setTargetedEntity(tag.getInt("SrpTargetEntity"));
        }
        if (tag.contains("SrpAttackTimer")) {
            this.attackTimer = tag.getFloat("SrpAttackTimer");
        }
        if (tag.contains("SrpAttackUp")) {
            this.up = tag.getBoolean("SrpAttackUp");
        }
        if (tag.contains("SrpTargetLostTicks")) {
            this.targetLostTicks = tag.getInt("SrpTargetLostTicks");
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.MOB_SILENCE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DOD_NAK.get();
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

    public boolean hasTargetedEntity() {
        return getTargetedEntityId() != 0;
    }

    public LivingEntity getTargetedEntity() {
        Entity entity = this.level().getEntity(getTargetedEntityId());
        return entity instanceof LivingEntity living ? living : null;
    }

    public float getAttackTimer() {
        return this.attackTimer;
    }

    private int getTargetedEntityId() {
        return this.entityData.get(TARGET_ENTITY);
    }

    private void setTargetedEntity(int entityId) {
        this.entityData.set(TARGET_ENTITY, entityId);
    }

    private void tickLegacyGrab() {
        LivingEntity target = getTarget();
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
            setTarget(null);
            clearGrab();
            return;
        }
        if (target == null || !target.isAlive()) {
            this.targetLostTicks++;
            if (this.targetLostTicks >= LEGACY_TARGET_LOST_RETREAT_TICKS) {
                this.level().broadcastEntityEvent(this, LEGACY_RETREAT_EVENT);
                this.up = true;
            }
            clearGrab();
            return;
        }
        this.targetLostTicks = 0;
        if (hasLineOfSight(target) && distanceToSqr(target) < LEGACY_GRAB_RANGE_SQR) {
            setParasiteStatus(LEGACY_GRABBING_STATUS);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LEGACY_INITIAL_SLOW_TICKS, LEGACY_SLOW_AMPLIFIER, false, false), this);
            setTargetedEntity(target.getId());
            this.getNavigation().moveTo(target, 0.0D);
            getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.tickCount % 20 == 0) {
                playSound(ModSounds.MOB_TENDRIL.get(), 1.0F, 1.0F);
            }
        } else {
            setParasiteStatus(LEGACY_SEARCHING_STATUS);
            setTargetedEntity(0);
        }
    }

    private void pullTargetedEntity() {
        LivingEntity target = getTargetedEntity();
        if (target == null || !target.isAlive()) {
            return;
        }
        double distance = distanceToSqr(target);
        if (distance >= LEGACY_GRAB_RANGE_SQR || distance <= LEGACY_MIN_PULL_RANGE_SQR) {
            return;
        }
        target.resetFallDistance();
        Vec3 motion = target.getDeltaMovement();
        double desiredX = Math.signum(getX() - target.getX()) * LEGACY_PULL_STRENGTH;
        double desiredZ = Math.signum(getZ() - target.getZ()) * LEGACY_PULL_STRENGTH;
        target.setDeltaMovement(
            motion.x + (desiredX - motion.x) * LEGACY_PULL_DAMPING,
            motion.y,
            motion.z + (desiredZ - motion.z) * LEGACY_PULL_DAMPING
        );
        target.hurtMarked = true;
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LEGACY_PULL_SLOW_TICKS, LEGACY_SLOW_AMPLIFIER, false, false), this);
    }

    private void clearGrab() {
        setParasiteStatus(0);
        setTargetedEntity(0);
    }

    private void tickAttackTimer() {
        if (this.up) {
            this.attackTimer += LEGACY_ATTACK_TIMER_UP_STEP;
            if (this.attackTimer > 1.0F) {
                this.up = false;
            }
        } else {
            this.attackTimer = Math.max(0.0F, this.attackTimer - LEGACY_ATTACK_TIMER_DOWN_STEP);
        }
    }

    private boolean canTargetNakLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }
}
