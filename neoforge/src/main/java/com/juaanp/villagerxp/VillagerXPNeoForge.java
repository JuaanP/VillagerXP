package com.juaanp.villagerxp;

import com.juaanp.villagerxp.client.ConfigScreenBase;
import com.juaanp.villagerxp.platform.NeoForgePlatformHelper;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Constants.MOD_ID)
public class VillagerXPNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public VillagerXPNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        modEventBus.register(new EventHandlerNeoForge());
        modEventBus.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NeoForgePlatformHelper.SPEC);

        CommonClass.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("VillagerXP initializing...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("VillagerXP server starting...");
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("VillagerXP client setup...");

            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, screen) -> new ConfigScreenBase(screen, Minecraft.getInstance().options)
                    )
            );
        }
    }
}