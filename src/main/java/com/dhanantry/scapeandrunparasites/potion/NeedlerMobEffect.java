package com.dhanantry.scapeandrunparasites.potion;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.init.ModEffects;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;

public final class NeedlerMobEffect extends SrpMobEffect {
    public static final int LEGACY_COLOR = 0xC7B403;
    public static final float LEGACY_DEFAULT_DAMAGE_FRACTION = 0.4F;
    public static final int LEGACY_DEFAULT_TERMINAL_AMPLIFIER = 7;
    public static final float LEGACY_DEFAULT_MAX_DAMAGE_PLAYER = 1.0E9F;
    public static final float LEGACY_DEFAULT_MAX_DAMAGE_MONSTER = 1.0E9F;
    public static final int LEGACY_REAPPLY_DURATION = 400;
    public static final int LEGACY_REGENERATION_DURATION = 900;
    public static final int LEGACY_REGENERATION_AMPLIFIER = 1;
    public static final int LEGACY_ABSORPTION_DURATION = 100;
    public static final int LEGACY_ABSORPTION_AMPLIFIER = 1;

    public NeedlerMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide || amplifier < SrpConfig.NEEDLER_TERMINAL_AMPLIFIER.get()) {
            return true;
        }

        int nextAmplifier = amplifier - SrpConfig.NEEDLER_TERMINAL_AMPLIFIER.get();
        entity.removeEffect(ModEffects.NEEDLER);
        String entityName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        if (isNeedlerImmune(entityName)) {
            return true;
        }

        entity.addEffect(new MobEffectInstance(ModEffects.NEEDLER, LEGACY_REAPPLY_DURATION, nextAmplifier, false, false));
        float currentHealth = entity.getHealth();
        if (currentHealth <= 0.0F) {
            return true;
        }

        float damage = entity.getMaxHealth() * SrpConfig.NEEDLER_DAMAGE.get().floatValue();
        damage = Math.min(damage, entity instanceof Player ? SrpConfig.NEEDLER_MAX_DAMAGE_PLAYER.get().floatValue() : SrpConfig.NEEDLER_MAX_DAMAGE_MONSTER.get().floatValue());
        entity.setHealth(currentHealth - damage);
        Level level = entity.level();
        level.broadcastEntityEvent(entity, (byte) 2);
        level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 0.0F, false, Level.ExplosionInteraction.NONE);

        if (entity.getHealth() <= 0.0F) {
            triggerLegacyNeedlerDeath(entity);
        }
        return true;
    }

    private static boolean isNeedlerImmune(String entityName) {
        boolean listed = SrpConfig.NEEDLER_IMMUNE_LIST.get().stream().anyMatch(entityName::contains);
        return listed != SrpConfig.NEEDLER_IMMUNE_LIST_WHITE.get();
    }

    private static void triggerLegacyNeedlerDeath(LivingEntity entity) {
        ItemStack totem = ItemStack.EMPTY;
        DamageSource damageSource = entity.damageSources().fellOutOfWorld();
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = entity.getItemInHand(hand);
            if (held.is(Items.TOTEM_OF_UNDYING) && CommonHooks.onLivingUseTotem(entity, damageSource, held, hand)) {
                totem = held.copy();
                held.shrink(1);
                break;
            }
        }

        if (!totem.isEmpty()) {
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
                CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, totem);
            }
            entity.setHealth(1.0F);
            entity.removeAllEffects();
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, LEGACY_REGENERATION_DURATION, LEGACY_REGENERATION_AMPLIFIER));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, LEGACY_ABSORPTION_DURATION, LEGACY_ABSORPTION_AMPLIFIER));
            entity.level().broadcastEntityEvent(entity, (byte) 35);
            return;
        }

        entity.die(damageSource);
        SRPMain.LOGGER.debug("Needler terminal effect killed {}", entity.getType().builtInRegistryHolder().getRegisteredName());
    }
}
