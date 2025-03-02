package com.juaanp.villagerxp;

import com.juaanp.villagerxp.client.ConfigScreenBase;
import com.juaanp.villagerxp.platform.ForgePlatformHelper;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Constants.MOD_ID)
public class VillagerXPForge {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ModContainer modContainer;

    public VillagerXPForge(IEventBus modEventBus, ModContainer modContainer) {
        this.modContainer = modContainer;
        modEventBus.addListener(this::commonSetup);

        // Registramos los eventos de juego en el FORGE bus, no en el MOD bus
        NeoForge.EVENT_BUS.register(new EventHandlerForge());
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, ForgePlatformHelper.SPEC);

        CommonClass.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("VillagerXP initializing...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("VillagerXP server starting...");
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("VillagerXP client setup...");

            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (mc, screen) -> new ConfigScreenBase(screen, Minecraft.getInstance().options)
            );
        }
    }
}