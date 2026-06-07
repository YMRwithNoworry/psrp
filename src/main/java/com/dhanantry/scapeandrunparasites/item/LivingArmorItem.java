package com.dhanantry.scapeandrunparasites.item;

import com.dhanantry.scapeandrunparasites.init.ModItems;
import com.dhanantry.scapeandrunparasites.util.config.SrpConfig;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class LivingArmorItem extends ArmorItem {
    private final boolean sentient;

    public LivingArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, boolean sentient) {
        super(material, type, properties);
        this.sentient = sentient;
    }

    public boolean isSentient() {
        return sentient;
    }

    public float pointReduction() {
        return sentient ? SrpConfig.SENTIENT_POINT_REDUCTION.get().floatValue() : SrpConfig.LIVING_POINT_REDUCTION.get().floatValue();
    }

    public int pointCap() {
        return sentient ? SrpConfig.SENTIENT_POINT_CAP.get() : SrpConfig.LIVING_POINT_CAP.get();
    }

    public Item nextItem() {
        if (sentient) {
            return null;
        }
        return switch (getType()) {
            case HELMET -> ModItems.ARMOR_HELM_SENTIENT.get();
            case CHESTPLATE -> ModItems.ARMOR_CHEST_SENTIENT.get();
            case LEGGINGS -> ModItems.ARMOR_PANTS_SENTIENT.get();
            case BOOTS -> ModItems.ARMOR_BOOTS_SENTIENT.get();
            default -> null;
        };
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean selected) {
        if (!level.isClientSide) {
            SrpEquipmentEvents.tryUpgradeArmorStack(entity, stack, this);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.srparasites.srphits", SrpItemData.getInt(stack, SrpItemData.HITS), SrpConfig.WEAPON_LIVING_SENTIENT_DAMAGE_NEEDED.get()));
    }
}
