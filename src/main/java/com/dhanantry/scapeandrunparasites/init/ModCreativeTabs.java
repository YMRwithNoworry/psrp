package com.dhanantry.scapeandrunparasites.init;

import com.dhanantry.scapeandrunparasites.SRPMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SRPMain.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SRP_TAB = TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.srparasites"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> ModItems.ASSIMILATED_FLESH.get().getDefaultInstance())
        .displayItems((parameters, output) -> ModItems.CREATIVE_TAB_ITEMS.forEach(item -> output.accept(item.get())))
        .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
