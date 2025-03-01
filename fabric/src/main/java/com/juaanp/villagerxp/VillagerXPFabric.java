package com.juaanp.villagerxp;

import net.fabricmc.api.ModInitializer;

public class VillagerXPFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        EventHandlerFabric.init();
    }
}
