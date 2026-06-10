package com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VestaEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 88;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.vesta.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.vesta.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 390.0D;
    public static final double LEGACY_ARMOR = 15.5D;
    public static final double LEGACY_ATTACK_DAMAGE = 45.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.15D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.242D;
    public static final double LEGACY_FOLLOW_RANGE = 80.0D;
    public static final float LEGACY_WIDTH = 1.75F;
    public static final float LEGACY_HEIGHT = 3.6F;
    public static final float LEGACY_EYE_HEIGHT = 1.5F;
    public static final float LEGACY_SHADOW_RADIUS = 1.3F;
    public static final int LEGACY_TYPE = 31;
    public static final int LEGACY_ATTACK_INTERVAL = 10;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final double LEGACY_MELEE_REACH = 8.0D;
    public static final float LEGACY_WATER_LEAP_POWER = 0.7F;
    public static final double LEGACY_WATER_LEAP_SPEED = 1.5D;
    public static final int LEGACY_WATER_LEAP_STATUS = 3;
    public static final int LEGACY_WATER_LEAP_INTERVAL = 20;
    public static final int LEGACY_GET_FOLLOWERS_COUNT = 2;
    public static final int LEGACY_GET_FOLLOWERS_RANGE = 16;
    public static final int LEGACY_AREA_BUFF_INITIAL_TICKS = 60;
    public static final int LEGACY_AREA_BUFF_COOLDOWN_TICKS = 600;
    public static final int LEGACY_AREA_BUFF_RANGE = 60;
    public static final int LEGACY_REGENERATION_TICKS = 600;
    public static final int LEGACY_REGENERATION_AMPLIFIER = 3;
    public static final int LEGACY_LINK_TICKS = 200;
    public static final int LEGACY_LINK_AMPLIFIER = 1;
    public static final int LEGACY_COORDINATION_RANGE = 32;
    public static final int LEGACY_COORDINATION_INTERVAL = 25;
    public static final int LEGACY_COORDINATION_TICKS = 6666;
    public static final int LEGACY_COORDINATION_AMPLIFIER = 0;
    public static final int LEGACY_VARIANT_SKIN = 1;
    public static final double LEGACY_VARIANT_CHANCE = 0.33D;
    public static final double LEGACY_VARIANT_ARMOR_MULTIPLIER = 1.5D;
    public static final double LEGACY_VARIANT_SPEED = 0.1694D;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(VestaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(VestaEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int areaBuffTimer;
    private int waterLeapTimer;

    public VestaEntity(EntityType<? extends VestaEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 150;
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
        this.goalSelector.addGoal(2, new WaterLeapGoal(this));
        this.goalSelector.addGoal(3, new VestaMeleeGoal(this));
        this.goalSelector.addGoal(4, new AreaBuffGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetVestaLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < LEGACY_VARIANT_CHANCE) {
            setSkin(LEGACY_VARIANT_SKIN);
            applyVariantAttributes();
        }
        return data;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.waterLeapTimer > 0) {
                this.waterLeapTimer--;
            }
            if (this.tickCount % LEGACY_COORDINATION_INTERVAL == 0) {
                coordinateNearbyParasites();
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.STEP_HEAVY.get(), 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.VESTA_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.VESTA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.VESTA_DEATH.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpAreaBuffTimer", this.areaBuffTimer);
        tag.putInt("SrpWaterLeapTimer", this.waterLeapTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
            if (getSkin() == LEGACY_VARIANT_SKIN) {
                applyVariantAttributes();
            }
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpAreaBuffTimer")) {
            this.areaBuffTimer = tag.getInt("SrpAreaBuffTimer");
        }
        if (tag.contains("SrpWaterLeapTimer")) {
            this.waterLeapTimer = tag.getInt("SrpWaterLeapTimer");
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
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

    public float getSelfeFlashIntensity(float partialTicks) {
        return 0.0F;
    }

    private boolean canTargetVestaLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void applyVariantAttributes() {
        if (getAttribute(Attributes.ARMOR) != null) {
            getAttribute(Attributes.ARMOR).setBaseValue(LEGACY_ARMOR * LEGACY_VARIANT_ARMOR_MULTIPLIER);
        }
        if (getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(LEGACY_VARIANT_SPEED);
        }
    }

    private void coordinateNearbyParasites() {
        AABB area = getBoundingBox().inflate(LEGACY_COORDINATION_RANGE);
        for (SrpParasiteMob parasite : this.level().getEntitiesOfClass(SrpParasiteMob.class, area, parasite -> parasite != this && parasite.isAlive())) {
            parasite.addEffect(new MobEffectInstance(ModEffects.LINK, LEGACY_COORDINATION_TICKS, LEGACY_COORDINATION_AMPLIFIER, false, false), this);
        }
    }

    private void buffNearbyParasites() {
        AABB area = getBoundingBox().inflate(LEGACY_AREA_BUFF_RANGE);
        for (SrpParasiteMob parasite : this.level().getEntitiesOfClass(SrpParasiteMob.class, area, parasite -> parasite != this && parasite.isAlive())) {
            parasite.addEffect(new MobEffectInstance(MobEffects.REGENERATION, LEGACY_REGENERATION_TICKS, LEGACY_REGENERATION_AMPLIFIER, false, false), this);
            parasite.addEffect(new MobEffectInstance(ModEffects.LINK, LEGACY_LINK_TICKS, LEGACY_LINK_AMPLIFIER, false, false), this);
        }
    }

    private static final class VestaMeleeGoal extends Goal {
        private final VestaEntity vesta;
        private int attackTime;

        private VestaMeleeGoal(VestaEntity vesta) {
            this.vesta = vesta;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.vesta.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void stop() {
            this.attackTime = 0;
            this.vesta.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.vesta.getTarget();
            if (target == null || !target.isAlive()) {
                return;
            }
            this.vesta.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.vesta.getNavigation().moveTo(target, LEGACY_MELEE_SPEED);
            if (this.attackTime > 0) {
                this.attackTime--;
            }
            double reach = Math.max(LEGACY_MELEE_REACH * LEGACY_MELEE_REACH, this.vesta.getBbWidth() * 2.0F * this.vesta.getBbWidth() * 2.0F + target.getBbWidth());
            if (this.vesta.distanceToSqr(target.getX(), target.getY(), target.getZ()) <= reach && this.attackTime <= 0) {
                this.attackTime = LEGACY_ATTACK_INTERVAL;
                this.vesta.swing(InteractionHand.MAIN_HAND);
                this.vesta.doHurtTarget(target);
            }
        }
    }

    private static final class WaterLeapGoal extends Goal {
        private final VestaEntity vesta;

        private WaterLeapGoal(VestaEntity vesta) {
            this.vesta = vesta;
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.vesta.getTarget();
            return target != null && target.isAlive() && this.vesta.isInWater() && this.vesta.waterLeapTimer <= 0;
        }

        @Override
        public void start() {
            LivingEntity target = this.vesta.getTarget();
            if (target == null) {
                return;
            }
            this.vesta.waterLeapTimer = LEGACY_WATER_LEAP_INTERVAL;
            this.vesta.setParasiteStatus(LEGACY_WATER_LEAP_STATUS);
            Vec3 direction = new Vec3(target.getX() - this.vesta.getX(), 0.0D, target.getZ() - this.vesta.getZ());
            if (direction.lengthSqr() > 1.0E-7D) {
                direction = direction.normalize().scale(LEGACY_WATER_LEAP_SPEED);
                this.vesta.setDeltaMovement(direction.x, LEGACY_WATER_LEAP_POWER, direction.z);
                this.vesta.hasImpulse = true;
            }
        }
    }

    private static final class AreaBuffGoal extends Goal {
        private final VestaEntity vesta;

        private AreaBuffGoal(VestaEntity vesta) {
            this.vesta = vesta;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            this.vesta.areaBuffTimer++;
            if (this.vesta.areaBuffTimer >= LEGACY_AREA_BUFF_INITIAL_TICKS) {
                this.vesta.buffNearbyParasites();
                this.vesta.areaBuffTimer -= LEGACY_AREA_BUFF_COOLDOWN_TICKS;
            }
        }
    }
}
