package com.dhanantry.scapeandrunparasites.entity.monster.infected;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class InfSheepEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 14;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.inf_sheep.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.inf_sheep.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 13.0D;
    public static final double LEGACY_ARMOR = 1.3D;
    public static final double LEGACY_ATTACK_DAMAGE = 6.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.3D;
    public static final double LEGACY_MOVEMENT_SPEED = 0.23000000417232513D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 0.9F;
    public static final float LEGACY_HEIGHT = 1.3F;
    public static final float LEGACY_EYE_HEIGHT = 1.235F;
    public static final float LEGACY_SHADOW_RADIUS = 0.5F;
    public static final int LEGACY_TYPE = 12;
    public static final int LEGACY_CAN_MOD_RENDER = 1;
    public static final int LEGACY_FUSE_TIME = 40;
    public static final double LEGACY_SWIM_DIVE_SPEED = 0.08D;
    public static final double LEGACY_MELEE_SPEED = 1.5D;
    public static final int LEGACY_FOLLOWER_SEARCH_MODE = 1;
    public static final int LEGACY_FOLLOWER_SEARCH_RANGE = 16;
    public static final int LEGACY_TEXTURE_WHITE = 0;
    public static final int LEGACY_TEXTURE_GREY = 1;
    public static final int LEGACY_TEXTURE_BLACK = 2;
    public static final float LEGACY_TEXTURE_BLACK_CHANCE = 0.1F;
    public static final float LEGACY_TEXTURE_GREY_CHANCE = 0.4F;
    public static final int LEGACY_MELT_WAIT_TICKS = 1000;
    public static final float LEGACY_MELT_START_HEIGHT = 1.3F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE = 0.17F;
    public static final float LEGACY_DEATH_HEIGHT_RESTORE_CAP = 1.57F;
    public static final float LEGACY_MELT_THRESHOLD_HEIGHT = 0.7F;
    public static final float LEGACY_MELT_ASIZE_STEP = -0.005F;
    public static final float LEGACY_MELT_HEIGHT_STEP = -0.01F;
    public static final int LEGACY_MELT_SPAWN_TICKS = 63;
    public static final int LEGACY_LESH_LEGS = 0;
    public static final double LEGACY_HEAD_HEALTH = 0.0D;
    public static final double LEGACY_HEAD_DAMAGE = 0.0D;
    public static final double LEGACY_HEAD_CHANCE = 0.0D;
    public static final int LEGACY_LESH_V = 0;

    private static final EntityDataAccessor<Float> MELT_HEIGHT = SynchedEntityData.defineId(InfSheepEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> MELTING = SynchedEntityData.defineId(InfSheepEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TEXTURE_VARIANT = SynchedEntityData.defineId(InfSheepEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(InfSheepEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private float aSize = 1.0F;
    private int meltSoundTicks;
    private int meltWaitTicks;

    public InfSheepEntity(EntityType<? extends InfSheepEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(3, new InfSheepMeleeGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MELT_HEIGHT, 0.0F);
        builder.define(MELTING, false);
        builder.define(TEXTURE_VARIANT, LEGACY_TEXTURE_WHITE);
        builder.define(PARASITE_STATUS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        tickMelting();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        rollTextureVariantWeighted();
        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.INFECTEDSHEEP_GROWL.get();
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
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof DyeItem dyeItem) {
            int variant = variantFromDyeColor(dyeItem.getDyeColor());
            if (variant < 0) {
                return InteractionResult.PASS;
            }
            if (!this.level().isClientSide && getTextureVariant() != variant) {
                setTextureVariant(variant);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("SrpMeltHeight", getTHeigh());
        tag.putBoolean("SrpMelting", isMelting());
        tag.putFloat("SrpASize", this.aSize);
        tag.putInt("SrpMeltSoundTicks", this.meltSoundTicks);
        tag.putInt("SrpMeltWaitTicks", this.meltWaitTicks);
        tag.putInt("TextureVariant", getTextureVariant());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpMeltHeight")) {
            setTHeighAbsolute(tag.getFloat("SrpMeltHeight"));
        }
        if (tag.contains("SrpMelting")) {
            setMelting(tag.getBoolean("SrpMelting"));
        }
        if (tag.contains("SrpASize")) {
            this.aSize = tag.getFloat("SrpASize");
        }
        if (tag.contains("SrpMeltSoundTicks")) {
            this.meltSoundTicks = tag.getInt("SrpMeltSoundTicks");
        }
        if (tag.contains("SrpMeltWaitTicks")) {
            this.meltWaitTicks = tag.getInt("SrpMeltWaitTicks");
        }
        if (tag.contains("TextureVariant")) {
            setTextureVariant(tag.getInt("TextureVariant"));
        }
        if (tag.contains("SrpParasiteStatus")) {
            setParasiteStatus(tag.getInt("SrpParasiteStatus"));
        }
    }

    public void melt() {
        this.meltWaitTicks = LEGACY_MELT_WAIT_TICKS;
        setTHeighAbsolute(LEGACY_MELT_START_HEIGHT);
        setMelting(true);
    }

    public boolean isMelting() {
        return this.entityData.get(MELTING);
    }

    public void setMelting(boolean melting) {
        this.entityData.set(MELTING, melting);
    }

    public float getTHeigh() {
        return this.entityData.get(MELT_HEIGHT);
    }

    public void setTHeigh(float heightDelta) {
        setTHeighAbsolute(getTHeigh() + heightDelta);
    }

    public float getaSize() {
        return this.aSize;
    }

    public void setaSize(float sizeDelta) {
        this.aSize += sizeDelta;
    }

    public float getSelfeFlashIntensity2() {
        return this.aSize;
    }

    public float getSelfeFlashIntensity(float partialTick) {
        return 0.0F;
    }

    public int getTextureVariant() {
        return this.entityData.get(TEXTURE_VARIANT);
    }

    public void setTextureVariant(int variant) {
        this.entityData.set(TEXTURE_VARIANT, Math.max(LEGACY_TEXTURE_WHITE, Math.min(LEGACY_TEXTURE_BLACK, variant)));
    }

    public int getParasiteStatus() {
        return this.entityData.get(PARASITE_STATUS);
    }

    public void setParasiteStatus(int status) {
        this.entityData.set(PARASITE_STATUS, status);
    }

    private void setTHeighAbsolute(float height) {
        this.entityData.set(MELT_HEIGHT, height);
    }

    private void tickMelting() {
        if (!isMelting()) {
            return;
        }
        if (this.meltSoundTicks % 20 == 0) {
            playSound(ModSounds.INFECTED_MELT.get(), 1.0F, 1.0F);
        }
        this.meltSoundTicks++;
        if (getTHeigh() > LEGACY_MELT_THRESHOLD_HEIGHT) {
            setaSize(LEGACY_MELT_ASIZE_STEP);
            setTHeigh(LEGACY_MELT_HEIGHT_STEP);
        }
        if (!this.level().isClientSide && (getTHeigh() <= LEGACY_MELT_THRESHOLD_HEIGHT || this.meltSoundTicks >= LEGACY_MELT_SPAWN_TICKS)) {
            setMelting(false);
        }
    }

    private void rollTextureVariantWeighted() {
        float roll = this.random.nextFloat();
        if (roll < LEGACY_TEXTURE_BLACK_CHANCE) {
            setTextureVariant(LEGACY_TEXTURE_BLACK);
        } else if (roll < LEGACY_TEXTURE_GREY_CHANCE) {
            setTextureVariant(LEGACY_TEXTURE_GREY);
        } else {
            setTextureVariant(LEGACY_TEXTURE_WHITE);
        }
    }

    private static int variantFromDyeColor(DyeColor color) {
        return switch (color) {
            case WHITE -> LEGACY_TEXTURE_WHITE;
            case GRAY, LIGHT_GRAY -> LEGACY_TEXTURE_GREY;
            case BLACK -> LEGACY_TEXTURE_BLACK;
            default -> -1;
        };
    }

    private static final class InfSheepMeleeGoal extends MeleeAttackGoal {
        private final InfSheepEntity sheep;

        private InfSheepMeleeGoal(InfSheepEntity sheep) {
            super(sheep, LEGACY_MELEE_SPEED, false);
            this.sheep = sheep;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (canPerformAttack(target)) {
                resetAttackCooldown();
                this.sheep.swing(InteractionHand.MAIN_HAND);
                this.sheep.doHurtTarget(target);
            }
        }
    }
}
