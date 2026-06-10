package com.dhanantry.scapeandrunparasites.entity.monster.deterrent;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TonroEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 29;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.tonro.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.tonro.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 50.0D;
    public static final double LEGACY_ARMOR = 15.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 15.0D;
    public static final double LEGACY_SWING_ATTACK_DAMAGE = 35.0D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 1.0D;
    public static final double LEGACY_FOLLOW_RANGE = 20.0D;
    public static final float LEGACY_WIDTH = 0.7F;
    public static final float LEGACY_HEIGHT = 4.5F;
    public static final float LEGACY_EYE_HEIGHT = 3.8F;
    public static final int LEGACY_XP = 110;
    public static final int LEGACY_TYPE = 40;
    public static final double LEGACY_BURIED_TIME = 7.5D;
    public static final double LEGACY_MELEE_REACH = 8.0D;
    public static final double LEGACY_MELEE_LOS_REACH = 7.0D;
    public static final double LEGACY_AOE_INFLATE = 2.0D;
    public static final double LEGACY_TARGET_LAUNCH = 0.5000000059604645D;
    public static final float LEGACY_ATTACK_TIMER_UP_STEP = 0.15F;
    public static final float LEGACY_ATTACK_TIMER_DOWN_STEP = 0.2F;
    public static final int LEGACY_SHOCKWAVE_STATUS = 10;
    public static final int LEGACY_SHOCKWAVE_COOLDOWN_TICKS = 20;
    public static final int LEGACY_SHOCKWAVE_TRIGGER_RANGE = 16;
    public static final int LEGACY_SHOCKWAVE_SKILL = 1;
    public static final int LEGACY_SHOCKWAVE_FIRST_BORDER = 3;
    public static final int LEGACY_SHOCKWAVE_SECOND_BORDER = 5;
    public static final int LEGACY_SHOCKWAVE_FINISH_BORDER = 6;
    public static final double LEGACY_SHOCKWAVE_DAMAGE_MULTIPLIER = 0.3D;
    public static final float LEGACY_MIN_DAMAGE = 2.0F;
    public static final byte LEGACY_ATTACK_EVENT = 12;
    public static final byte LEGACY_FLAME_EVENT = 100;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(TonroEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(TonroEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float attackTimer;
    private boolean up;
    private int border;
    private boolean skillshockwave = true;
    private int shockwaveCooldown;

    public TonroEntity(EntityType<? extends TonroEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(2, new ShockwaveGoal(this));
        this.goalSelector.addGoal(3, new StationaryMeleeAoeGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetTonroLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.shockwaveCooldown > 0) {
            this.shockwaveCooldown--;
        }
        if (this.up) {
            this.attackTimer += LEGACY_ATTACK_TIMER_UP_STEP;
            if (this.attackTimer > 1.0F) {
                this.up = false;
            }
        } else {
            this.attackTimer = Math.max(0.0F, this.attackTimer - LEGACY_ATTACK_TIMER_DOWN_STEP);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
        if (hurt) {
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, LEGACY_TARGET_LAUNCH, 0.0D));
            target.hurtMarked = true;
        }
        return hurt;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_ATTACK_EVENT) {
            this.up = true;
            this.attackTimer = 0.0F;
        } else if (id == LEGACY_FLAME_EVENT) {
            spawnFlameParticles();
            spawnFlameParticles();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putFloat("SrpAttackTimer", this.attackTimer);
        tag.putBoolean("SrpAttackUp", this.up);
        tag.putInt("SrpShockwaveBorder", this.border);
        tag.putBoolean("SrpShockwaveFinished", this.skillshockwave);
        tag.putInt("SrpShockwaveCooldown", this.shockwaveCooldown);
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
        if (tag.contains("SrpAttackTimer")) {
            this.attackTimer = tag.getFloat("SrpAttackTimer");
        }
        if (tag.contains("SrpAttackUp")) {
            this.up = tag.getBoolean("SrpAttackUp");
        }
        if (tag.contains("SrpShockwaveBorder")) {
            this.border = tag.getInt("SrpShockwaveBorder");
        }
        if (tag.contains("SrpShockwaveFinished")) {
            this.skillshockwave = tag.getBoolean("SrpShockwaveFinished");
        }
        if (tag.contains("SrpShockwaveCooldown")) {
            this.shockwaveCooldown = tag.getInt("SrpShockwaveCooldown");
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.TONRO_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.TONRO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.TONRO_DEATH.get();
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

    public float getAttackTimer() {
        return this.attackTimer;
    }

    public boolean getFinished(byte skill) {
        return skill == LEGACY_SHOCKWAVE_SKILL ? this.skillshockwave : true;
    }

    public void setFinished(byte skill, boolean finished) {
        if (skill == LEGACY_SHOCKWAVE_SKILL) {
            this.skillshockwave = finished;
        }
    }

    public void doSpecialSkill(byte skill) {
        if (skill == LEGACY_SHOCKWAVE_SKILL) {
            shockwave();
        }
    }

    private boolean canTargetTonroLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private boolean attackEntityAsMobAOE(Entity target) {
        this.up = true;
        this.attackTimer = 0.0F;
        this.level().broadcastEntityEvent(this, LEGACY_ATTACK_EVENT);
        this.playSound(ModSounds.MOB_SWIPE.get(), 3.0F, 1.0F);
        boolean hurtAny = false;
        AABB area = new AABB(
            target.getX(),
            target.getY(),
            target.getZ(),
            target.getX() + 1.0D,
            target.getY() + 1.0D,
            target.getZ() + 1.0D
        ).inflate(LEGACY_AOE_INFLATE);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area)) {
            if (canHarmLiving(living) && hasLineOfSight(living) && doHurtTarget(living)) {
                hurtAny = true;
            }
        }
        return hurtAny;
    }

    private void shockwave() {
        setParasiteStatus(LEGACY_SHOCKWAVE_STATUS);
        this.getNavigation().stop();
        if (this.border == 0) {
            float pitch = (this.random.nextFloat() - this.random.nextFloat()) * 0.4F + 2.0F;
            playSound(getHurtSound(damageSources().generic()), 4.0F, pitch);
            this.border++;
            return;
        }
        if (this.border <= 2) {
            this.level().broadcastEntityEvent(this, LEGACY_FLAME_EVENT);
        }
        if (this.tickCount % 20 != 0) {
            return;
        }
        this.border++;
        LivingEntity target = getTarget();
        if (target == null || !target.isAlive() || target.getY() != getY()) {
            finishShockwave();
            return;
        }
        if (this.border == LEGACY_SHOCKWAVE_FIRST_BORDER || this.border == LEGACY_SHOCKWAVE_SECOND_BORDER) {
            spawnShock();
        }
        if (this.border > LEGACY_SHOCKWAVE_FINISH_BORDER) {
            finishShockwave();
        }
    }

    private void spawnShock() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        double damage = Math.max(LEGACY_MIN_DAMAGE, getAttributeValue(Attributes.ATTACK_DAMAGE) * LEGACY_SHOCKWAVE_DAMAGE_MULTIPLIER);
        double range = this.border == LEGACY_SHOCKWAVE_SECOND_BORDER ? 6.0D : 4.0D;
        double forwardX = -Mth.sin(getYRot() * Mth.DEG_TO_RAD);
        double forwardZ = Mth.cos(getYRot() * Mth.DEG_TO_RAD);
        for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(range, 1.5D, range))) {
            if (!canHarmLiving(living) || !hasLineOfSight(living)) {
                continue;
            }
            double dx = living.getX() - getX();
            double dz = living.getZ() - getZ();
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            if (horizontal <= 0.001D || horizontal > range) {
                continue;
            }
            double dot = (dx / horizontal) * forwardX + (dz / horizontal) * forwardZ;
            if (dot >= 0.15D && living.hurt(damageSources().mobAttack(this), (float) damage)) {
                living.setDeltaMovement(living.getDeltaMovement().add(dx / horizontal * 0.35D, 0.25D, dz / horizontal * 0.35D));
                living.hurtMarked = true;
            }
        }
        for (int i = 0; i < 24; i++) {
            double spread = (this.random.nextDouble() - 0.5D) * range;
            double distance = 1.5D + this.random.nextDouble() * range;
            serverLevel.sendParticles(
                ParticleTypes.FLAME,
                getX() + forwardX * distance + forwardZ * spread,
                getY() + 0.25D + this.random.nextDouble(),
                getZ() + forwardZ * distance - forwardX * spread,
                1,
                0.02D,
                0.03D,
                0.02D,
                0.0D
            );
        }
    }

    private void finishShockwave() {
        this.skillshockwave = true;
        setParasiteStatus(0);
        this.border = 0;
        this.shockwaveCooldown = LEGACY_SHOCKWAVE_COOLDOWN_TICKS;
    }

    private void spawnFlameParticles() {
        for (int i = 0; i < 8; i++) {
            this.level().addParticle(
                ParticleTypes.FLAME,
                getRandomX(0.5D),
                getRandomY(),
                getRandomZ(0.5D),
                (this.random.nextDouble() - 0.5D) * 0.05D,
                this.random.nextDouble() * 0.05D,
                (this.random.nextDouble() - 0.5D) * 0.05D
            );
        }
    }

    private static final class StationaryMeleeAoeGoal extends Goal {
        private final TonroEntity tonro;
        private int attackTime;

        private StationaryMeleeAoeGoal(TonroEntity tonro) {
            this.tonro = tonro;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.tonro.getTarget();
            return target != null && target.isAlive() && this.tonro.distanceToSqr(target) <= LEGACY_MELEE_REACH * LEGACY_MELEE_REACH;
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.tonro.getTarget();
            if (target == null) {
                return;
            }
            this.tonro.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.attackTime > 0) {
                this.attackTime--;
            }
            if (this.attackTime <= 0
                && this.tonro.distanceToSqr(target) <= LEGACY_MELEE_LOS_REACH * LEGACY_MELEE_LOS_REACH
                && this.tonro.hasLineOfSight(target)) {
                this.attackTime = 20;
                this.tonro.attackEntityAsMobAOE(target);
            }
        }
    }

    private static final class ShockwaveGoal extends Goal {
        private final TonroEntity tonro;

        private ShockwaveGoal(TonroEntity tonro) {
            this.tonro = tonro;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.tonro.getTarget();
            return target != null
                && target.isAlive()
                && this.tonro.shockwaveCooldown <= 0
                && this.tonro.distanceToSqr(target) <= LEGACY_SHOCKWAVE_TRIGGER_RANGE * LEGACY_SHOCKWAVE_TRIGGER_RANGE
                && this.tonro.hasLineOfSight(target)
                && this.tonro.getFinished((byte) LEGACY_SHOCKWAVE_SKILL);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.tonro.getFinished((byte) LEGACY_SHOCKWAVE_SKILL);
        }

        @Override
        public void start() {
            this.tonro.setFinished((byte) LEGACY_SHOCKWAVE_SKILL, false);
            this.tonro.border = 0;
        }

        @Override
        public void stop() {
            if (!this.tonro.getFinished((byte) LEGACY_SHOCKWAVE_SKILL)) {
                this.tonro.finishShockwave();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.tonro.getTarget();
            if (target != null) {
                this.tonro.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            this.tonro.doSpecialSkill((byte) LEGACY_SHOCKWAVE_SKILL);
        }
    }
}
