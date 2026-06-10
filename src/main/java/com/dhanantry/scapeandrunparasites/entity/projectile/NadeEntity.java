package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import com.dhanantry.scapeandrunparasites.init.ModSounds;
import java.util.Optional;
import java.util.UUID;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NadeEntity extends Entity implements GeoEntity {
    public static final String LEGACY_MODEL_ANIMATION_NAME = "animation.nade.func_78087_a";
    public static final String LEGACY_COSMICAL_ANIMATION_NAME = "animation.nade.setRotationAnglesCosmical";
    public static final float LEGACY_WIDTH = 0.5F;
    public static final float LEGACY_HEIGHT = 0.5F;
    public static final float LEGACY_GROWTH_WIDTH = 0.8F;
    public static final float LEGACY_GROWTH_HEIGHT = 0.32F;
    public static final int LEGACY_DEFAULT_FUSE = 3;
    public static final int LEGACY_DEFAULT_DURATION = 10;
    public static final int LEGACY_ELVIA_FUSE = 4;
    public static final int LEGACY_ELVIA_DURATION = 60;
    public static final int LEGACY_SOUND_TICK = 2;
    public static final int LEGACY_CLIENT_SMOKE_COUNT = 5;
    public static final int LEGACY_CLIENT_LARGE_SMOKE_COUNT = 2;

    private static final EntityDataAccessor<Integer> SELFE = SynchedEntityData.defineId(NadeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(NadeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WAITSTART = SynchedEntityData.defineId(NadeEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation LEGACY_MODEL_ANIMATION = RawAnimation.begin().thenLoop(LEGACY_MODEL_ANIMATION_NAME);

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private LivingEntity father;
    private UUID fatherUuid;
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int damageTimer;
    private double lockedX;
    private double lockedZ;

    public NadeEntity(EntityType<? extends NadeEntity> entityType, Level level) {
        super(entityType, level);
        setFuseState(LEGACY_DEFAULT_FUSE);
        setStartState(LEGACY_DEFAULT_DURATION);
        this.blocksBuilding = true;
        this.noPhysics = true;
    }

    public NadeEntity(Level level, LivingEntity father, int fuse, int duration) {
        this(ModEntities.NADE.get(), level);
        setFather(father);
        setFuseState(fuse);
        setStartState(duration);
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SELFE, -1);
        builder.define(FUSE, -1);
        builder.define(WAITSTART, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            spawnSmoke();
            return;
        }
        if (this.tickCount == LEGACY_SOUND_TICK) {
            playSound(ModSounds.NADE_S.get(), 1.0F, 1.0F);
        }
        if (this.tickCount <= 3) {
            this.lockedX = getX();
            this.lockedZ = getZ();
            return;
        }
        setSelfeState(1);
        dyingBurst();
        setPos(this.lockedX, getY(), this.lockedZ);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setSelfeState(tag.getInt("Selfe"));
        setFuseState(tag.getInt("Fuse"));
        setStartState(tag.getInt("WaitStart"));
        this.lastActiveTime = tag.getInt("LastActiveTime");
        this.timeSinceIgnited = tag.getInt("TimeSinceIgnited");
        this.damageTimer = tag.getInt("DamageTimer");
        this.lockedX = tag.getDouble("LockedX");
        this.lockedZ = tag.getDouble("LockedZ");
        if (tag.hasUUID("Father")) {
            this.fatherUuid = tag.getUUID("Father");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Selfe", getSelfeState());
        tag.putInt("Fuse", getFuseState());
        tag.putInt("WaitStart", getStartState());
        tag.putInt("LastActiveTime", this.lastActiveTime);
        tag.putInt("TimeSinceIgnited", this.timeSinceIgnited);
        tag.putInt("DamageTimer", this.damageTimer);
        tag.putDouble("LockedX", this.lockedX);
        tag.putDouble("LockedZ", this.lockedZ);
        if (this.fatherUuid != null) {
            tag.putUUID("Father", this.fatherUuid);
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public int getStartState() {
        return this.entityData.get(WAITSTART);
    }

    public void setStartState(int state) {
        this.entityData.set(WAITSTART, state);
    }

    public int getFuseState() {
        return this.entityData.get(FUSE);
    }

    public void setFuseState(int state) {
        this.entityData.set(FUSE, state);
    }

    public int getSelfeState() {
        return this.entityData.get(SELFE);
    }

    public void setSelfeState(int state) {
        this.entityData.set(SELFE, state);
    }

    public void setFather(LivingEntity father) {
        this.father = father;
        this.fatherUuid = father.getUUID();
    }

    public float getSelfeFlashIntensity(float partialTicks) {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * partialTicks * 5.0F) / Math.max(1.0F, getFuseState() - 2.0F);
    }

    private void dyingBurst() {
        this.lastActiveTime = this.timeSinceIgnited;
        int selfe = getSelfeState();
        if (selfe > 0) {
            this.timeSinceIgnited += selfe;
        }
        if (this.timeSinceIgnited < 0) {
            this.timeSinceIgnited = 0;
        }
        if (this.timeSinceIgnited >= getFuseState()) {
            this.timeSinceIgnited = getFuseState();
            selfExplode();
        } else {
            setBoundingBox(makeBoundingBox(getX(), getY(), getZ(), getBbWidth() + LEGACY_GROWTH_WIDTH, getBbHeight() + LEGACY_GROWTH_HEIGHT));
        }
    }

    private void selfExplode() {
        setSelfeState(2);
        this.damageTimer++;
        LivingEntity owner = resolveFather();
        if (owner != null && owner.isAlive()) {
            float damage = Optional.ofNullable(owner.getAttribute(Attributes.ATTACK_DAMAGE))
                .map(attribute -> attribute.getValue())
                .orElse(0.0D)
                .floatValue();
            AABB area = getBoundingBox();
            for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, area, living -> living.isAlive() && !(living instanceof SrpParasiteMob))) {
                living.hurt(damageSources().mobAttack(owner), damage);
            }
        }
        if (this.damageTimer > getStartState()) {
            discard();
        }
    }

    private LivingEntity resolveFather() {
        if (this.father != null && this.father.isAlive()) {
            return this.father;
        }
        if (this.fatherUuid != null && this.level() instanceof ServerLevel serverLevel && serverLevel.getEntity(this.fatherUuid) instanceof LivingEntity living) {
            this.father = living;
            return living;
        }
        return null;
    }

    private void spawnSmoke() {
        for (int i = 0; i < LEGACY_CLIENT_SMOKE_COUNT; i++) {
            spawnParticle(ParticleTypes.SMOKE);
        }
        for (int i = 0; i < LEGACY_CLIENT_LARGE_SMOKE_COUNT; i++) {
            spawnParticle(ParticleTypes.LARGE_SMOKE);
        }
    }

    private void spawnParticle(net.minecraft.core.particles.ParticleOptions particle) {
        double dx = this.random.nextGaussian() * 0.02D;
        double dy = this.random.nextGaussian() * 0.02D;
        double dz = this.random.nextGaussian() * 0.02D;
        this.level().addParticle(
            particle,
            getX() + this.random.nextFloat() * getBbWidth() * 2.0F - getBbWidth(),
            getY() + 0.5D + this.random.nextFloat() * getBbHeight(),
            getZ() + this.random.nextFloat() * getBbWidth() * 2.0F - getBbWidth(),
            dx,
            dy,
            dz
        );
    }

    private static AABB makeBoundingBox(double x, double y, double z, float width, float height) {
        double radius = width / 2.0D;
        return new AABB(x - radius, y, z - radius, x + radius, y + height, z + radius);
    }
}
