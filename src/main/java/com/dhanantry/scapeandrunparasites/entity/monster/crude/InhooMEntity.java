package com.dhanantry.scapeandrunparasites.entity.monster.crude;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class InhooMEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 43;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inhoo_m.func_78087_a";
    public static final double LEGACY_HEALTH = 14.0D;
    public static final double LEGACY_ARMOR = 0.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 11.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.3D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.15D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final double LEGACY_MELEE_SPEED = 1.3D;
    public static final float LEGACY_WIDTH = 0.6F;
    public static final float LEGACY_HEIGHT = 1.95F;
    public static final float LEGACY_EYE_HEIGHT = 1.2F;
    public static final float LEGACY_SHADOW_RADIUS = 0.5F;
    public static final int LEGACY_TYPE = 11;
    public static final int LEGACY_CAN_MOD_RENDER = 0;
    public static final int LEGACY_DISLODGEMENT_SPAWN_TICK = 10;
    public static final String LEGACY_DISLODGEMENT_NBT = "dsltwenty";
    public static final int LEGACY_FROZEN_SKIN = 120;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(InhooMEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InhooMEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private boolean disloNumberTwenty;
    private boolean spawnedDislodgementBody;

    public InhooMEntity(EntityType<? extends InhooMEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(3, new InhooMMeleeGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetCrudePrey));
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
        tickLegacyDislodgementBody();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(LEGACY_DISLODGEMENT_NBT, this.disloNumberTwenty);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putBoolean("SrpDislodgementBodySpawned", this.spawnedDislodgementBody);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(LEGACY_DISLODGEMENT_NBT)) {
            this.disloNumberTwenty = tag.getBoolean(LEGACY_DISLODGEMENT_NBT);
        }
        if (tag.contains("SrpSkin")) {
            setSkin(tag.getInt("SrpSkin"));
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
        if (tag.contains("SrpDislodgementBodySpawned")) {
            this.spawnedDislodgementBody = tag.getBoolean("SrpDislodgementBodySpawned");
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INHOOM_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return ModSounds.INHOOM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INHOOM_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(ModSounds.LITE_FLESH_SLIDE.get(), 0.3F, 1.0F);
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

    public boolean isDisloNumberTwenty() {
        return this.disloNumberTwenty;
    }

    public void setDisloNumberTwenty(boolean value) {
        this.disloNumberTwenty = value;
    }

    public boolean hasSpawnedDislodgementBody() {
        return this.spawnedDislodgementBody;
    }

    public float getSelfeFlashIntensity2() {
        return 1.0F;
    }

    public float getSelfeFlashIntensity(float partialTick) {
        return 0.0F;
    }

    private boolean canTargetCrudePrey(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal) && !(target instanceof Villager);
    }

    private void tickLegacyDislodgementBody() {
        if (this.level().isClientSide || this.spawnedDislodgementBody || !this.disloNumberTwenty || this.tickCount != LEGACY_DISLODGEMENT_SPAWN_TICK) {
            return;
        }
        SrpParasiteMob body = createRandomAvailableFeralBody();
        if (body == null) {
            return;
        }
        body.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
        if (isPersistenceRequired()) {
            body.setPersistenceRequired();
        }
        ((ServerLevel) this.level()).addFreshEntity(body);
        this.spawnedDislodgementBody = true;
    }

    private SrpParasiteMob createRandomAvailableFeralBody() {
        int pick = this.random.nextInt(3);
        EntityType<? extends SrpParasiteMob> type = switch (pick) {
            case 0 -> ModEntities.FERSHEEP.get();
            case 1 -> ModEntities.FERWOLF.get();
            default -> ModEntities.FERHORSE.get();
        };
        return type.create(this.level());
    }

    private static final class InhooMMeleeGoal extends MeleeAttackGoal {
        private final InhooMEntity entity;

        private InhooMMeleeGoal(InhooMEntity entity) {
            super(entity, LEGACY_MELEE_SPEED, false);
            this.entity = entity;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.entity.swing(InteractionHand.MAIN_HAND);
                this.entity.doHurtTarget(target);
            }
        }
    }
}
