package com.dhanantry.scapeandrunparasites.entity.monster.inborn;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LodoEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 5;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.lodo.func_78087_a";
    public static final double LEGACY_HEALTH = 7.0D;
    public static final double LEGACY_ARMOR = 1.5D;
    public static final double LEGACY_ATTACK_DAMAGE = 3.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.05D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.2D;
    public static final float LEGACY_WIDTH = 0.5F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_EYE_HEIGHT = 0.3F;
    public static final int LEGACY_MIN_GROW_SECONDS = 60;
    public static final int LEGACY_RANDOM_GROW_SECONDS = 60;
    public static final int LEGACY_AVOID_DISTANCE = 8;
    public static final float LEGACY_BURIED_START = 1.0F;
    public static final float LEGACY_BURIED_DECAY = 0.02F;
    public static final byte LEGACY_BURIED_EVENT = 50;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(LodoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> BURIED = SynchedEntityData.defineId(LodoEntity.class, EntityDataSerializers.FLOAT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int totalGrowTime = 10;
    private int actualGrowTime;

    public LodoEntity(EntityType<? extends LodoEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
            .add(Attributes.MOVEMENT_SPEED, LEGACY_MOVEMENT_SPEED)
            .add(Attributes.KNOCKBACK_RESISTANCE, LEGACY_KNOCKBACK_RESISTANCE)
            .add(Attributes.ATTACK_DAMAGE, LEGACY_ATTACK_DAMAGE);
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
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LivingEntity.class, LEGACY_AVOID_DISTANCE, 1.0D, 1.0D, this::shouldAvoidLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(BURIED, -1.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        this.totalGrowTime = LEGACY_MIN_GROW_SECONDS + this.random.nextInt(LEGACY_RANDOM_GROW_SECONDS);
        return data;
    }

    @Override
    public void tick() {
        super.tick();
        growTimer();
        growStage();
        tickBuried();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LEGACY_BURIED_EVENT) {
            setFloorTimer();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ruptergrow", this.actualGrowTime);
        tag.putInt("SrpTotalGrowTime", this.totalGrowTime);
        tag.putInt("SrpSkin", getSkin());
        tag.putFloat("SrpBuried", getFloorTimer());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ruptergrow")) {
            this.actualGrowTime = tag.getInt("ruptergrow");
        }
        if (tag.contains("SrpTotalGrowTime")) {
            this.totalGrowTime = tag.getInt("SrpTotalGrowTime");
        }
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
        }
        if (tag.contains("SrpBuried")) {
            this.entityData.set(BURIED, tag.getFloat("SrpBuried"));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LODO_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.LODO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LODO_DEATH.get();
    }

    public int getSkin() {
        return this.entityData.get(SKIN);
    }

    public void setSkin(int skin) {
        this.entityData.set(SKIN, skin);
    }

    public void setFloorTimer() {
        this.entityData.set(BURIED, LEGACY_BURIED_START);
    }

    public float getFloorTimer() {
        return this.entityData.get(BURIED);
    }

    public int getActualGrowTime() {
        return this.actualGrowTime;
    }

    public int getTotalGrowTime() {
        return this.totalGrowTime;
    }

    private boolean shouldAvoidLiving(@Nullable LivingEntity living) {
        return living != null
            && !(living instanceof WaterAnimal)
            && !(living instanceof Creeper)
            && !(living instanceof SrpParasiteMob)
            && !(living instanceof Animal);
    }

    private void growTimer() {
        if (!this.level().isClientSide && this.tickCount % 20 == 0) {
            this.actualGrowTime++;
        }
    }

    private void growStage() {
        if (!this.level().isClientSide && this.actualGrowTime > this.totalGrowTime) {
            MudoEntity mudo = ModEntities.MUDO.get().create(this.level());
            if (mudo != null) {
                this.playSound(ModSounds.LODO_MUDO.get(), 1.0F, 1.0F);
                mudo.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
                if (isPersistenceRequired()) {
                    mudo.setPersistenceRequired();
                }
                this.level().addFreshEntity(mudo);
                discard();
            }
        }
    }

    private boolean tickBuried() {
        float buried = getFloorTimer();
        if (buried < 0.0F) {
            return false;
        }
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            spawnBuriedParticles(serverLevel);
        }
        this.entityData.set(BURIED, buried - LEGACY_BURIED_DECAY);
        return true;
    }

    private void spawnBuriedParticles(ServerLevel serverLevel) {
        BlockPos below = blockPosition().below();
        BlockState state = serverLevel.getBlockState(below);
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(below);
        for (int i = 0; i < 2; i++) {
            double x = getX() + this.random.nextFloat() * getBbWidth() * 2.0F - getBbWidth();
            double z = getZ() + this.random.nextFloat() * getBbWidth() * 2.0F - getBbWidth();
            serverLevel.sendParticles(
                particle,
                x,
                getY(),
                z,
                1,
                this.random.nextGaussian() * 0.02D,
                this.random.nextGaussian() * 0.02D,
                this.random.nextGaussian() * 0.02D,
                0.0D
            );
        }
    }
}
