package com.dhanantry.scapeandrunparasites.entity.projectile;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.SrpParasiteMob;
import com.dhanantry.scapeandrunparasites.init.ModEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SpineballEntity extends ThrowableItemProjectile {
    public static final float LEGACY_WIDTH = 0.3F;
    public static final float LEGACY_HEIGHT = 0.3F;
    public static final float LEGACY_DAMAGE = 25.0F;
    public static final int LEGACY_POISON_DURATION_SECONDS = 7;
    public static final int LEGACY_POISON_DURATION_TICKS = LEGACY_POISON_DURATION_SECONDS * 20;
    public static final int LEGACY_CONFIG_POISON_AMPLIFIER = 1;
    public static final int LEGACY_POISON_AMPLIFIER = LEGACY_CONFIG_POISON_AMPLIFIER - 1;
    public static final double LEGACY_GEAR_DAMAGE_FRACTION = 0.04D;
    public static final double LEGACY_GEAR_DAMAGE_THRESHOLD = 0.1D;
    public static final float LEGACY_SHOOT_VELOCITY = 1.6F;

    private static final EquipmentSlot[] LEGACY_ARMOR_SLOTS = {
        EquipmentSlot.HEAD,
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS,
        EquipmentSlot.FEET
    };

    private float damage = LEGACY_DAMAGE;
    private int poisonDuration = LEGACY_POISON_DURATION_TICKS;
    private int poisonAmplifier = LEGACY_POISON_AMPLIFIER;
    private double gearDamage = LEGACY_GEAR_DAMAGE_FRACTION;

    public SpineballEntity(EntityType<? extends SpineballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpineballEntity(Level level, LivingEntity owner, double xPower, double yPower, double zPower) {
        super(ModEntities.SPINEBALL.get(), owner, level);
        shoot(xPower, yPower, zPower, LEGACY_SHOOT_VELOCITY, 0.0F);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level().isClientSide) {
            return;
        }
        Entity entity = result.getEntity();
        if (!(entity instanceof LivingEntity living)) {
            return;
        }
        if (living instanceof SrpParasiteMob) {
            discard();
            return;
        }
        living.hurt(this.damageSources().thrown(this, getOwner()), this.damage);
        living.addEffect(new MobEffectInstance(MobEffects.POISON, this.poisonDuration, this.poisonAmplifier), this);
        damageArmor(living);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SLIME_BALL));
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("SrpDamage", this.damage);
        tag.putInt("SrpPoisonDuration", this.poisonDuration);
        tag.putInt("SrpPoisonAmplifier", this.poisonAmplifier);
        tag.putDouble("SrpGearDamage", this.gearDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SrpDamage")) {
            this.damage = tag.getFloat("SrpDamage");
        }
        if (tag.contains("SrpPoisonDuration")) {
            this.poisonDuration = tag.getInt("SrpPoisonDuration");
        }
        if (tag.contains("SrpPoisonAmplifier")) {
            this.poisonAmplifier = tag.getInt("SrpPoisonAmplifier");
        }
        if (tag.contains("SrpGearDamage")) {
            this.gearDamage = tag.getDouble("SrpGearDamage");
        }
    }

    public void setDurationAmplifier(int durationSeconds, int amplifier) {
        this.poisonDuration = durationSeconds * 20;
        this.poisonAmplifier = amplifier - 1;
    }

    public void setGearDamage(double gearDamage) {
        this.gearDamage = gearDamage;
    }

    private void damageArmor(LivingEntity living) {
        for (EquipmentSlot slot : LEGACY_ARMOR_SLOTS) {
            ItemStack stack = living.getItemBySlot(slot);
            if (stack.isEmpty() || !stack.isDamageableItem()) {
                continue;
            }
            int remainingDurability = stack.getMaxDamage() - stack.getDamageValue();
            double threshold = stack.getMaxDamage() * LEGACY_GEAR_DAMAGE_THRESHOLD;
            if (threshold < remainingDurability) {
                int damageAmount = Math.max(1, (int) (stack.getMaxDamage() * this.gearDamage));
                stack.hurtAndBreak(damageAmount, living, slot);
            }
        }
    }
}
