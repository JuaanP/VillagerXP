package com.juaanp.villagerxp.platform;

import com.juaanp.villagerxp.Constants;
import com.juaanp.villagerxp.config.CommonConfig;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgePlatformHelper implements IPlatformHelper {
    // Keep the NeoForge config spec definition here
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.IntValue XP_AMOUNT;
    public static final ModConfigSpec.BooleanValue ENABLE_XP_BOTTLES;
    public static final ModConfigSpec.BooleanValue ENABLE_XP_ORBS;
    public static final ModConfigSpec.BooleanValue REQUIRES_CROUCHING;
    public static final ModConfigSpec.DoubleValue BOTTLE_XP_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue ORB_XP_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue ORB_RANGE;
    public static final ModConfigSpec.BooleanValue SHOW_ORB_RANGES;
    public static final ModConfigSpec SPEC;

    static {
        XP_AMOUNT = BUILDER
                .comment("villagerxp.config.xpAmount.tooltip")
                .translation(Constants.MOD_ID + ".config.xpAmount")
                .defineInRange("xpAmount", CommonConfig.getDefaultXpAmount(), 0, 150);

        ENABLE_XP_BOTTLES = BUILDER
                .comment("villagerxp.config.enableXpBottles.tooltip")
                .translation(Constants.MOD_ID + ".config.enableXpBottles")
                .define("enableXpBottles", CommonConfig.getDefaultXpBottlesEnabled());

        ENABLE_XP_ORBS = BUILDER
                .comment("villagerxp.config.enableXpOrbs.tooltip")
                .translation(Constants.MOD_ID + ".config.enableXpOrbs")
                .define("enableXpOrbs", CommonConfig.getDefaultXpOrbsEnabled());

        REQUIRES_CROUCHING = BUILDER
                .comment("villagerxp.config.requiresCrouching.tooltip")
                .translation(Constants.MOD_ID + ".config.requiresCrouching")
                .define("requiresCrouching", CommonConfig.getDefaultRequiresCrouching());

        BOTTLE_XP_MULTIPLIER = BUILDER
                .comment("villagerxp.config.bottleMultiplier.tooltip")
                .translation(Constants.MOD_ID + ".config.bottleMultiplier")
                .defineInRange("bottleMultiplier", (double)CommonConfig.getDefaultBottleXpMultiplier(), 0.1, 5.0);

        ORB_XP_MULTIPLIER = BUILDER
                .comment("villagerxp.config.orbMultiplier.tooltip")
                .translation(Constants.MOD_ID + ".config.orbMultiplier")
                .defineInRange("orbMultiplier", (double)CommonConfig.getDefaultOrbXpMultiplier(), 0.1, 5.0);

        ORB_RANGE = BUILDER
                .comment("villagerxp.config.orbRange.tooltip")
                .translation(Constants.MOD_ID + ".config.orbRange")
                .defineInRange("orbRange", CommonConfig.getDefaultOrbRange(), 1.0, 32.0);

        SHOW_ORB_RANGES = BUILDER
                .comment("villagerxp.config.showOrbRanges.tooltip")
                .translation(Constants.MOD_ID + ".config.showOrbRanges")
                .define("showOrbRanges", CommonConfig.getDefaultShowOrbRanges());

        SPEC = BUILDER.build();
    }

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void loadConfig() {
        applyToCommonConfig();
    }

    @Override
    public void saveConfig() {
        saveToNeoForgeConfig();
    }

    private void applyToCommonConfig() {
        CommonConfig common = CommonConfig.getInstance();
        common.setXpAmount(XP_AMOUNT.get());
        common.setXpBottlesEnabled(ENABLE_XP_BOTTLES.get());
        common.setXpOrbsEnabled(ENABLE_XP_ORBS.get());
        common.setRequiresCrouching(REQUIRES_CROUCHING.get());
        common.setBottleXpMultiplier(BOTTLE_XP_MULTIPLIER.get().floatValue());
        common.setOrbXpMultiplier(ORB_XP_MULTIPLIER.get().floatValue());
        common.setOrbRange(ORB_RANGE.get());
        common.setShowOrbRanges(SHOW_ORB_RANGES.get());
    }

    private void saveToNeoForgeConfig() {
        CommonConfig common = CommonConfig.getInstance();
        XP_AMOUNT.set(common.getXpAmount());
        ENABLE_XP_BOTTLES.set(common.isXpBottlesEnabled());
        ENABLE_XP_ORBS.set(common.isXpOrbsEnabled());
        REQUIRES_CROUCHING.set(common.requiresCrouching());
        BOTTLE_XP_MULTIPLIER.set((double)common.getBottleXpMultiplier());
        ORB_XP_MULTIPLIER.set((double)common.getOrbXpMultiplier());
        ORB_RANGE.set(common.getOrbRange());
        SHOW_ORB_RANGES.set(common.getShowOrbRanges());
        // No need to call SPEC.save() as NeoForge handles this internally
    }
}