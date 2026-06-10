package com.dhanantry.scapeandrunparasites.entity.monster.pure;

import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OmbooEntity extends SrpParasiteMob implements GeoEntity {
    public static final int LEGACY_PARASITE_ID = 47;
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.omboo.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.omboo.setRotationAnglesCosmical";
    public static final double LEGACY_HEALTH = 75.0D;
    public static final double LEGACY_ARMOR = 20.0D;
    public static final double LEGACY_ATTACK_DAMAGE = 25.0D;
    public static final double LEGACY_KNOCKBACK_RESISTANCE = 0.15D;
    public static final double LEGACY_FOLLOW_RANGE = 32.0D;
    public static final float LEGACY_WIDTH = 1.7F;
    public static final float LEGACY_HEIGHT = 2.4F;
    public static final float LEGACY_EYE_HEIGHT = 2.4F;
    public static final int LEGACY_XP = 40;
    public static final int LEGACY_TYPE = 47;
    public static final int LEGACY_HEAVY_SKIN = 7;
    public static final double LEGACY_GROUND_LIFT = 5.0D;
    public static final double LEGACY_GROUND_LIFT_SPEED = 0.5D;
    public static final int LEGACY_RANDOM_MOVE_CHANCE = 7;
    public static final double LEGACY_RANDOM_MOVE_SPEED_IDLE = 0.2D;
    public static final double LEGACY_RANDOM_MOVE_SPEED_TARGET = 0.3D;
    public static final double LEGACY_CHARGE_SPEED = 1.0D;
    public static final double LEGACY_CHARGE_MIN_DISTANCE_SQR = 3.0D;
    public static final double LEGACY_CHARGE_RETARGET_DISTANCE_SQR = 9.0D;
    public static final double LEGACY_CHARGE_TARGET_Y_OFFSET = 10.0D;
    public static final int LEGACY_BOMB_INTERVAL = 15;
    public static final int LEGACY_BOMB_FUSE_TICKS = 80;
    public static final float LEGACY_BOMB_STRENGTH = 1.0F;
    public static final float LEGACY_BOMB_DAMAGE = 20.0F;
    public static final double LEGACY_BOMB_RADIUS = 4.0D;
    public static final double LEGACY_BOMB_HORIZONTAL_TRIGGER_RANGE = 25.0D;

    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(OmbooEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARASITE_STATUS = SynchedEntityData.defineId(OmbooEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(OmbooEntity.class, EntityDataSerializers.BYTE);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);
    private static final int FLAG_CHARGING = 1;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private final List<DelayedBomb> delayedBombs = new ArrayList<>();
    private int bombCounter;

    public OmbooEntity(EntityType<? extends OmbooEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new OmbooMoveControl(this);
        this.xpReward = LEGACY_XP;
        setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, LEGACY_HEALTH)
            .add(Attributes.ARMOR, LEGACY_ARMOR)
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
        this.goalSelector.addGoal(1, new ChargeAttackGoal(this));
        this.goalSelector.addGoal(2, new BombGoal(this));
        this.goalSelector.addGoal(6, new RandomFlyGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canTargetLiving));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::canTargetOmbooLiving));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, 0);
        builder.define(PARASITE_STATUS, 0);
        builder.define(FLAGS, (byte) 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.random.nextDouble() < 0.33D) {
            setSkin(LEGACY_HEAVY_SKIN);
        }
        return data;
    }

    @Override
    public void tick() {
        setNoGravity(true);
        if (!this.level().isClientSide) {
            tickDelayedBombs();
            if (this.tickCount == 10 && onGround()) {
                this.moveControl.setWantedPosition(getX(), getY() + LEGACY_GROUND_LIFT, getZ(), LEGACY_GROUND_LIFT_SPEED);
            }
            LivingEntity target = getTarget();
            if (target != null && !this.level().isEmptyBlock(blockPosition().below())) {
                setDeltaMovement(getDeltaMovement().x, 0.5D, getDeltaMovement().z);
                this.hasImpulse = true;
            }
        }
        super.tick();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        setCharging(false);
        return target instanceof LivingEntity living && canHarmLiving(living) && super.doHurtTarget(target);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getParasiteStatus() != 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.OMBOO_GROWL.get();
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource damageSource) {
        return this.random.nextBoolean() && getParasiteStatus() > 0 ? ModSounds.MOB_SILENCE.get() : ModSounds.OMBOO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.OMBOO_DEATH.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SrpSkin", getSkin());
        tag.putInt("SrpParasiteStatus", getParasiteStatus());
        tag.putInt("SrpBombCounter", this.bombCounter);
        ListTag bombs = new ListTag();
        for (DelayedBomb bomb : this.delayedBombs) {
            CompoundTag bombTag = new CompoundTag();
            bombTag.putInt("Fuse", bomb.fuse);
            bombTag.putDouble("X", bomb.x);
            bombTag.putDouble("Y", bomb.y);
            bombTag.putDouble("Z", bomb.z);
            bombs.add(bombTag);
        }
        tag.put("SrpDelayedBombs", bombs);
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
        if (tag.contains("SrpBombCounter")) {
            this.bombCounter = tag.getInt("SrpBombCounter");
        }
        this.delayedBombs.clear();
        if (tag.contains("SrpDelayedBombs", Tag.TAG_LIST)) {
            ListTag bombs = tag.getList("SrpDelayedBombs", Tag.TAG_COMPOUND);
            for (int i = 0; i < bombs.size(); i++) {
                CompoundTag bombTag = bombs.getCompound(i);
                this.delayedBombs.add(new DelayedBomb(
                    bombTag.getInt("Fuse"),
                    bombTag.getDouble("X"),
                    bombTag.getDouble("Y"),
                    bombTag.getDouble("Z")
                ));
            }
        }
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

    public boolean isCharging() {
        return getFlag(FLAG_CHARGING);
    }

    public void setCharging(boolean charging) {
        setFlag(FLAG_CHARGING, charging);
    }

    public float getSelfeFlashIntensity(float partialTicks) {
        return isCharging() ? 0.5F : 0.0F;
    }

    private boolean getFlag(int flag) {
        return (this.entityData.get(FLAGS) & flag) != 0;
    }

    private void setFlag(int flag, boolean value) {
        byte flags = this.entityData.get(FLAGS);
        if (value) {
            flags = (byte) (flags | flag);
        } else {
            flags = (byte) (flags & ~flag);
        }
        this.entityData.set(FLAGS, flags);
    }

    private boolean canTargetOmbooLiving(LivingEntity target) {
        return canTargetLiving(target) && !(target instanceof WaterAnimal) && !(target instanceof Animal);
    }

    private void queueBomb() {
        this.delayedBombs.add(new DelayedBomb(LEGACY_BOMB_FUSE_TICKS, getX(), getY(), getZ()));
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, getX(), getY() + 0.25D, getZ(), 10, 0.2D, 0.2D, 0.2D, 0.02D);
        }
    }

    private void tickDelayedBombs() {
        Iterator<DelayedBomb> iterator = this.delayedBombs.iterator();
        while (iterator.hasNext()) {
            DelayedBomb bomb = iterator.next();
            bomb.fuse--;
            if (bomb.fuse <= 0) {
                detonateBomb(bomb);
                iterator.remove();
            }
        }
    }

    private void detonateBomb(DelayedBomb bomb) {
        if (this.level().isClientSide) {
            return;
        }
        boolean griefing = EventHooks.canEntityGrief(this.level(), this);
        this.level().explode(this, bomb.x, bomb.y, bomb.z, LEGACY_BOMB_STRENGTH, false, griefing ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
        AABB area = new AABB(
            bomb.x - LEGACY_BOMB_RADIUS,
            bomb.y - LEGACY_BOMB_RADIUS,
            bomb.z - LEGACY_BOMB_RADIUS,
            bomb.x + LEGACY_BOMB_RADIUS,
            bomb.y + LEGACY_BOMB_RADIUS,
            bomb.z + LEGACY_BOMB_RADIUS
        );
        Vec3 center = new Vec3(bomb.x, bomb.y, bomb.z);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area, this::canHarmLiving)) {
            double distance = living.position().add(0.0D, living.getBbHeight() * 0.5D, 0.0D).distanceTo(center);
            if (distance <= LEGACY_BOMB_RADIUS && living.hurt(damageSources().mobAttack(this), LEGACY_BOMB_DAMAGE)) {
                Vec3 push = living.position().subtract(center);
                if (push.lengthSqr() > 1.0E-4D) {
                    living.setDeltaMovement(living.getDeltaMovement().add(push.normalize().scale(0.35D)).add(0.0D, 0.25D, 0.0D));
                    living.hasImpulse = true;
                }
            }
        }
    }

    private boolean shouldDropBomb(LivingEntity target) {
        if (target == null || !target.isAlive() || !target.onGround()) {
            return false;
        }
        Vec3 projected = new Vec3(getX(), target.getY(), getZ());
        return target.position().distanceTo(projected) < LEGACY_BOMB_HORIZONTAL_TRIGGER_RANGE;
    }

    private static final class BombGoal extends Goal {
        private final OmbooEntity omboo;
        private LivingEntity target;

        private BombGoal(OmbooEntity omboo) {
            this.omboo = omboo;
        }

        @Override
        public boolean canUse() {
            this.omboo.bombCounter++;
            if (this.omboo.bombCounter < LEGACY_BOMB_INTERVAL) {
                return false;
            }
            this.omboo.bombCounter = 0;
            this.target = this.omboo.getTarget();
            return this.omboo.shouldDropBomb(this.target);
        }

        @Override
        public void start() {
            this.omboo.queueBomb();
        }
    }

    private static final class ChargeAttackGoal extends Goal {
        private final OmbooEntity omboo;
        private LivingEntity target;

        private ChargeAttackGoal(OmbooEntity omboo) {
            this.omboo = omboo;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.omboo.getTarget();
            return this.target != null
                && this.omboo.getRandom().nextInt(LEGACY_RANDOM_MOVE_CHANCE) == 0
                && this.omboo.distanceToSqr(this.target) > LEGACY_CHARGE_MIN_DISTANCE_SQR;
        }

        @Override
        public boolean canContinueToUse() {
            return this.omboo.getMoveControl().hasWanted()
                && this.omboo.isCharging()
                && this.target != null
                && this.target.isAlive();
        }

        @Override
        public void start() {
            Vec3 targetEye = this.target.getEyePosition().add(0.0D, LEGACY_CHARGE_TARGET_Y_OFFSET, 0.0D);
            this.omboo.getMoveControl().setWantedPosition(targetEye.x, targetEye.y, targetEye.z, LEGACY_CHARGE_SPEED);
            this.omboo.setCharging(true);
        }

        @Override
        public void stop() {
            this.omboo.setCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null || !this.target.isAlive()) {
                return;
            }
            if (this.omboo.getBoundingBox().intersects(this.target.getBoundingBox())) {
                this.omboo.doHurtTarget(this.target);
            } else if (this.omboo.distanceToSqr(this.target) < LEGACY_CHARGE_RETARGET_DISTANCE_SQR) {
                Vec3 targetEye = this.target.getEyePosition().add(0.0D, LEGACY_CHARGE_TARGET_Y_OFFSET, 0.0D);
                this.omboo.getMoveControl().setWantedPosition(targetEye.x, targetEye.y, targetEye.z, LEGACY_CHARGE_SPEED);
            }
        }
    }

    private static final class RandomFlyGoal extends Goal {
        private final OmbooEntity omboo;

        private RandomFlyGoal(OmbooEntity omboo) {
            this.omboo = omboo;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.omboo.getMoveControl().hasWanted() && this.omboo.getRandom().nextInt(LEGACY_RANDOM_MOVE_CHANCE) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            LivingEntity target = this.omboo.getTarget();
            BlockPos origin = this.omboo.blockPosition();
            int mode = 1;
            double speed = LEGACY_RANDOM_MOVE_SPEED_IDLE;
            if (target != null && this.omboo.distanceToSqr(target) > 100.0D) {
                origin = target.blockPosition();
                mode = 2;
                speed = LEGACY_RANDOM_MOVE_SPEED_TARGET;
            } else if (target != null && this.omboo.distanceToSqr(target) < 36.0D) {
                origin = target.blockPosition();
                mode = 3;
                speed = LEGACY_RANDOM_MOVE_SPEED_TARGET;
            }
            for (int i = 0; i < 3; i++) {
                BlockPos pos = randomOffset(origin, mode, this.omboo);
                if (this.omboo.level().isEmptyBlock(pos)) {
                    this.omboo.getMoveControl().setWantedPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, speed);
                    if (target == null) {
                        this.omboo.getLookControl().setLookAt(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    return;
                }
            }
        }

        private static BlockPos randomOffset(BlockPos origin, int mode, OmbooEntity omboo) {
            if (mode == 2) {
                return origin.offset(omboo.getRandom().nextInt(6) - 2, omboo.getRandom().nextInt(7) - 2, omboo.getRandom().nextInt(6) - 2);
            }
            if (mode == 3) {
                int x = omboo.getRandom().nextInt(4) + 3;
                int z = omboo.getRandom().nextInt(4) + 3;
                return origin.offset(omboo.getRandom().nextBoolean() ? x : -x, omboo.getRandom().nextInt(5) + 4, omboo.getRandom().nextBoolean() ? z : -z);
            }
            return origin.offset(omboo.getRandom().nextInt(15) - 7, omboo.getRandom().nextInt(11) - 5, omboo.getRandom().nextInt(15) - 7);
        }
    }

    private static final class OmbooMoveControl extends MoveControl {
        private final OmbooEntity omboo;

        private OmbooMoveControl(OmbooEntity omboo) {
            super(omboo);
            this.omboo = omboo;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            Vec3 delta = new Vec3(this.wantedX - this.omboo.getX(), this.wantedY - this.omboo.getY(), this.wantedZ - this.omboo.getZ());
            double distance = delta.length();
            if (distance < this.omboo.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.omboo.setDeltaMovement(this.omboo.getDeltaMovement().scale(0.5D));
                return;
            }
            Vec3 movement = this.omboo.getDeltaMovement().add(delta.scale(0.05D * this.speedModifier / distance));
            this.omboo.setDeltaMovement(movement);
            LivingEntity target = this.omboo.getTarget();
            double xLook = target == null ? movement.x : target.getX() - this.omboo.getX();
            double zLook = target == null ? movement.z : target.getZ() - this.omboo.getZ();
            if (xLook * xLook + zLook * zLook > 1.0E-7D) {
                this.omboo.setYRot(-((float) Mth.atan2(xLook, zLook)) * Mth.RAD_TO_DEG);
                this.omboo.yBodyRot = this.omboo.getYRot();
            }
        }
    }

    private static final class DelayedBomb {
        private int fuse;
        private final double x;
        private final double y;
        private final double z;

        private DelayedBomb(int fuse, double x, double y, double z) {
            this.fuse = fuse;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
