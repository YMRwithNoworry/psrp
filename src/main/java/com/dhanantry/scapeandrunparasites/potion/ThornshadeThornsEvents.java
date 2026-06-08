package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class ThornshadeThornsEvents {
    public static final String TAG_ROOT = "srp_thornshade_thorns";
    public static final String TAG_USES = "Uses";
    public static final String TAG_COOLDOWN_UNTIL = "CooldownUntil";
    public static final String TAG_EXPLODE_DELAY = "ExplodeDelay";
    public static final String TAG_HAS_EXPLODED = "HasExplodedOnce";
    public static final float LEGACY_MAX_HP_ALLOWED = 120.0F;
    public static final int LEGACY_MAX_APPLICATIONS_BEFORE_EXPLOSION = 2;
    public static final int LEGACY_EXPLODE_DELAY_TICKS = 20;
    public static final int LEGACY_INFINITE_DURATION_THRESHOLD = 72000;
    public static final float LEGACY_SINGLE_USE_REFLECT_MULTIPLIER = 0.25F;
    public static final float LEGACY_MULTI_USE_REFLECT_MULTIPLIER = 0.5F;
    public static final float LEGACY_EXPLOSION_RADIUS = 3.0F;
    public static final float LEGACY_SPREAD_RADIUS = 10.0F;
    public static final int LEGACY_SPREAD_DURATION = 600;
    public static final int LEGACY_CHARGE_PARTICLES = 15;
    public static final int LEGACY_EXPLOSION_PARTICLES = 40;
    public static final int LEGACY_SPREAD_PARTICLES = 20;
    private static final ResourceLocation THORNSHADE_SELF_DESTRUCT = ResourceLocation.fromNamespaceAndPath(SRPMain.MODID, "thornshade_self_destruct");
    private static final ParticleOptions BLOOD_PARTICLE = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState());

    private ThornshadeThornsEvents() {
    }

    @SubscribeEvent
    public static void onPotionApplicable(MobEffectEvent.Applicable event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) {
            return;
        }
        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null || !effect.is(ModEffects.THORNSHADE_THORNS)) {
            return;
        }

        if (isParasite(target) || target.getMaxHealth() > LEGACY_MAX_HP_ALLOWED || target.hasEffect(ModEffects.THORNSHADE_THORNS) || isInfiniteDuration(effect)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            return;
        }

        CompoundTag data = getThornshadeData(target);
        int uses = data.getInt(TAG_USES);
        if (uses >= LEGACY_MAX_APPLICATIONS_BEFORE_EXPLOSION) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            if (!data.contains(TAG_EXPLODE_DELAY)) {
                scheduleExplosion(target, data);
                setThornshadeData(target, data);
            }
            return;
        }

        long gameTime = target.level().getGameTime();
        if (data.getLong(TAG_COOLDOWN_UNTIL) > gameTime) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            return;
        }

        uses++;
        data.putInt(TAG_USES, uses);
        data.putLong(TAG_COOLDOWN_UNTIL, gameTime + effect.getDuration() / 2L);
        setThornshadeData(target, data);
        event.setResult(MobEffectEvent.Applicable.Result.DEFAULT);
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) {
            return;
        }
        MobEffectInstance thornshade = victim.getEffect(ModEffects.THORNSHADE_THORNS);
        if (thornshade == null || isInfiniteDuration(thornshade)) {
            return;
        }
        Entity attackerEntity = event.getSource().getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) {
            return;
        }
        float incoming = event.getNewDamage();
        if (incoming <= 0.0F) {
            return;
        }

        int uses = getThornshadeData(victim).getInt(TAG_USES);
        float multiplier = uses <= 1 ? LEGACY_SINGLE_USE_REFLECT_MULTIPLIER : LEGACY_MULTI_USE_REFLECT_MULTIPLIER;
        float reflectedDamage = incoming * multiplier;
        if (reflectedDamage > 0.0F) {
            attacker.hurt(attacker.damageSources().thorns(victim), reflectedDamage);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity) || entity.level().isClientSide) {
            return;
        }
        CompoundTag data = getThornshadeData(entity);
        if (!data.contains(TAG_EXPLODE_DELAY)) {
            return;
        }

        int explodeDelay = data.getInt(TAG_EXPLODE_DELAY);
        if (explodeDelay > 0) {
            spawnBloodParticles(entity, LEGACY_CHARGE_PARTICLES);
            data.putInt(TAG_EXPLODE_DELAY, explodeDelay - 1);
            setThornshadeData(entity, data);
            return;
        }

        data.remove(TAG_EXPLODE_DELAY);
        setThornshadeData(entity, data);
        doExplosion(entity);
    }

    private static void scheduleExplosion(LivingEntity entity, CompoundTag data) {
        if (data.getBoolean(TAG_HAS_EXPLODED)) {
            return;
        }
        data.putInt(TAG_EXPLODE_DELAY, LEGACY_EXPLODE_DELAY_TICKS);
        entity.playSound(SoundEvents.WITHER_HURT, 1.5F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
    }

    private static void doExplosion(LivingEntity host) {
        if (!(host.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        CompoundTag hostData = getThornshadeData(host);
        if (hostData.getBoolean(TAG_HAS_EXPLODED)) {
            return;
        }
        hostData.putBoolean(TAG_HAS_EXPLODED, true);
        setThornshadeData(host, hostData);

        if (host instanceof ServerPlayer serverPlayer) {
            triggerSelfDestructAdvancement(serverPlayer);
        }

        double x = host.getX();
        double y = host.getY();
        double z = host.getZ();
        spawnRadialParticles(serverLevel, x, host.getY() + host.getBbHeight() * 0.5D, z, LEGACY_EXPLOSION_RADIUS, LEGACY_EXPLOSION_PARTICLES, ParticleTypes.CLOUD);
        DamageSource damageSource = host.damageSources().genericKill();
        serverLevel.explode(host, damageSource, null, x, y, z, LEGACY_EXPLOSION_RADIUS, false, Level.ExplosionInteraction.NONE);
        host.hurt(damageSource, Float.MAX_VALUE);

        AABB spreadBox = new AABB(
            x - LEGACY_SPREAD_RADIUS,
            y - LEGACY_SPREAD_RADIUS,
            z - LEGACY_SPREAD_RADIUS,
            x + LEGACY_SPREAD_RADIUS,
            y + LEGACY_SPREAD_RADIUS,
            z + LEGACY_SPREAD_RADIUS
        );

        for (LivingEntity nearby : serverLevel.getEntitiesOfClass(LivingEntity.class, spreadBox)) {
            if (nearby == host || !nearby.isAlive() || isParasite(nearby)) {
                continue;
            }
            double distanceSqr = nearby.distanceToSqr(x, y, z);
            if (distanceSqr <= LEGACY_EXPLOSION_RADIUS * LEGACY_EXPLOSION_RADIUS) {
                scheduleNearbyExplosion(nearby);
            } else if (distanceSqr <= LEGACY_SPREAD_RADIUS * LEGACY_SPREAD_RADIUS) {
                spreadThornshade(nearby, serverLevel);
            }
        }
    }

    private static void scheduleNearbyExplosion(LivingEntity nearby) {
        if (nearby.getMaxHealth() > LEGACY_MAX_HP_ALLOWED || !nearby.hasEffect(ModEffects.THORNSHADE_THORNS)) {
            return;
        }
        CompoundTag data = getThornshadeData(nearby);
        if (data.getBoolean(TAG_HAS_EXPLODED) || data.contains(TAG_EXPLODE_DELAY)) {
            return;
        }
        data.putInt(TAG_USES, Math.max(LEGACY_MAX_APPLICATIONS_BEFORE_EXPLOSION, data.getInt(TAG_USES)));
        scheduleExplosion(nearby, data);
        setThornshadeData(nearby, data);
    }

    private static void spreadThornshade(LivingEntity nearby, ServerLevel serverLevel) {
        if (nearby.getMaxHealth() > LEGACY_MAX_HP_ALLOWED || nearby.hasEffect(ModEffects.THORNSHADE_THORNS)) {
            return;
        }
        nearby.addEffect(new MobEffectInstance(ModEffects.THORNSHADE_THORNS, LEGACY_SPREAD_DURATION, 0, false, true));
        spawnRadialParticles(serverLevel, nearby.getX(), nearby.getY() + nearby.getBbHeight() * 0.5D, nearby.getZ(), 1.0F, LEGACY_SPREAD_PARTICLES, ParticleTypes.WITCH);
    }

    private static void spawnBloodParticles(LivingEntity entity, int count) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        double centerY = entity.getY() + entity.getBbHeight() * 0.5D;
        for (int i = 0; i < count; i++) {
            double xOffset = (entity.getRandom().nextDouble() - 0.5D) * 0.6D;
            double yOffset = entity.getRandom().nextDouble() * 0.8D;
            double zOffset = (entity.getRandom().nextDouble() - 0.5D) * 0.6D;
            double xSpeed = (entity.getRandom().nextDouble() - 0.5D) * 0.3D;
            double ySpeed = entity.getRandom().nextDouble() * 0.4D + 0.1D;
            double zSpeed = (entity.getRandom().nextDouble() - 0.5D) * 0.3D;
            serverLevel.sendParticles(BLOOD_PARTICLE, entity.getX() + xOffset, centerY + yOffset, entity.getZ() + zOffset, 0, xSpeed, ySpeed, zSpeed, 0.0D);
        }
    }

    private static void spawnRadialParticles(ServerLevel serverLevel, double x, double y, double z, float radius, int count, ParticleOptions particle) {
        for (int i = 0; i < count; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2.0D;
            double distance = radius * (0.7D + serverLevel.random.nextDouble() * 0.3D);
            double particleX = x + Math.cos(angle) * distance;
            double particleZ = z + Math.sin(angle) * distance;
            double particleY = y + (serverLevel.random.nextDouble() - 0.5D) * radius * 0.2D;
            double xSpeed = particleX - x;
            double zSpeed = particleZ - z;
            double length = Math.sqrt(xSpeed * xSpeed + zSpeed * zSpeed);
            if (length == 0.0D) {
                xSpeed = 1.0D;
                zSpeed = 0.0D;
                length = 1.0D;
            }
            xSpeed /= length;
            zSpeed /= length;

            if (particle == ParticleTypes.WITCH) {
                serverLevel.sendParticles(BLOOD_PARTICLE, particleX, particleY, particleZ, 0, xSpeed * 0.24D, 0.05D + serverLevel.random.nextDouble() * 0.15D, zSpeed * 0.24D, 0.0D);
            } else {
                serverLevel.sendParticles(particle, particleX, particleY, particleZ, 1, xSpeed * 0.12D, 0.05D + serverLevel.random.nextDouble() * 0.15D, zSpeed * 0.12D, 0.1D);
            }
            serverLevel.sendParticles(BLOOD_PARTICLE, particleX, particleY, particleZ, 0, xSpeed * (0.6D + serverLevel.random.nextDouble() * 0.8D), 0.25D + serverLevel.random.nextDouble() * 0.6D, zSpeed * (0.6D + serverLevel.random.nextDouble() * 0.8D), 0.0D);
        }
    }

    private static CompoundTag getThornshadeData(LivingEntity entity) {
        CompoundTag persistentData = entity.getPersistentData();
        if (!persistentData.contains(TAG_ROOT)) {
            CompoundTag thornshadeData = new CompoundTag();
            persistentData.put(TAG_ROOT, thornshadeData);
            return thornshadeData;
        }
        return persistentData.getCompound(TAG_ROOT);
    }

    private static void setThornshadeData(LivingEntity entity, CompoundTag data) {
        entity.getPersistentData().put(TAG_ROOT, data);
    }

    private static boolean isInfiniteDuration(MobEffectInstance effect) {
        return effect.isInfiniteDuration() || effect.getDuration() >= LEGACY_INFINITE_DURATION_THRESHOLD || effect.getDuration() == Integer.MAX_VALUE;
    }

    private static boolean isParasite(Entity entity) {
        return entity instanceof SrpParasiteMob;
    }

    private static void triggerSelfDestructAdvancement(ServerPlayer player) {
        AdvancementHolder advancement = player.server.getAdvancements().get(THORNSHADE_SELF_DESTRUCT);
        if (advancement != null) {
            for (String criterion : player.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
    }
}
