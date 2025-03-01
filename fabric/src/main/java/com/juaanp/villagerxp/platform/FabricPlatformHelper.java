package com.juaanp.villagerxp.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juaanp.villagerxp.Constants;
import com.juaanp.villagerxp.config.CommonConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FabricPlatformHelper implements IPlatformHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID + ".json").toFile();

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void loadConfig() {
        try {
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    ConfigData config = GSON.fromJson(reader, ConfigData.class);
                    applyConfig(config);
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        try {
            if (!CONFIG_FILE.exists() && !CONFIG_FILE.createNewFile()) {
                return;
            }

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                ConfigData config = createConfigData();
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyConfig(ConfigData config) {
        CommonConfig common = CommonConfig.getInstance();
        common.setXpAmount(config.xpAmount);
        common.setXpBottlesEnabled(config.enableXpBottles);
        common.setXpOrbsEnabled(config.enableXpOrbs);
        common.setRequiresCrouching(config.requiresCrouching);
        common.setBottleXpMultiplier(config.bottleXpMultiplier);
        common.setOrbXpMultiplier(config.orbXpMultiplier);
        common.setOrbRange(config.orbRange);
        common.setShowOrbRanges(config.showOrbRanges);
    }

    private ConfigData createConfigData() {
        ConfigData config = new ConfigData();
        CommonConfig common = CommonConfig.getInstance();
        config.xpAmount = common.getXpAmount();
        config.enableXpBottles = common.isXpBottlesEnabled();
        config.enableXpOrbs = common.isXpOrbsEnabled();
        config.requiresCrouching = common.requiresCrouching();
        config.bottleXpMultiplier = common.getBottleXpMultiplier();
        config.orbXpMultiplier = common.getOrbXpMultiplier();
        config.orbRange = common.getOrbRange();
        config.showOrbRanges = common.getShowOrbRanges();
        return config;
    }

    private static class ConfigData {
        int xpAmount = CommonConfig.getDefaultXpAmount();
        boolean enableXpBottles = CommonConfig.getDefaultXpBottlesEnabled();
        boolean enableXpOrbs = CommonConfig.getDefaultXpOrbsEnabled();
        boolean requiresCrouching = CommonConfig.getDefaultRequiresCrouching();
        float bottleXpMultiplier = CommonConfig.getDefaultBottleXpMultiplier();
        float orbXpMultiplier = CommonConfig.getDefaultOrbXpMultiplier();
        double orbRange = CommonConfig.getDefaultOrbRange();
        boolean showOrbRanges = CommonConfig.getDefaultShowOrbRanges();
    }
}