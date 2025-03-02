package com.juaanp.villagerxp.platform;

import com.juaanp.villagerxp.Constants;
import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgePlatformHelper implements IPlatformHelper {
    // Keep the Forge config spec definition here
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue XP_AMOUNT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_XP_BOTTLES;
    public static final ForgeConfigSpec.BooleanValue ENABLE_XP_ORBS;
    public static final ForgeConfigSpec.BooleanValue REQUIRES_CROUCHING;
    public static final ForgeConfigSpec.DoubleValue BOTTLE_XP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue ORB_XP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue ORB_ATTRACT_RANGE;
    public static final ForgeConfigSpec.DoubleValue ORB_PICKUP_RANGE;
    public static final ForgeConfigSpec.IntValue LEVELS_PER_BOTTLE;
    public static final ForgeConfigSpec SPEC;

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
                .defineInRange("bottleMultiplier", (double)CommonConfig.getDefaultBottleXpMultiplier(),
                                Constants.MIN_XP_MULTIPLIER_RANGE, Constants.MAX_XP_MULTIPLIER_RANGE);

        ORB_XP_MULTIPLIER = BUILDER
                .comment("villagerxp.config.orbMultiplier.tooltip")
                .translation(Constants.MOD_ID + ".config.orbMultiplier")
                .defineInRange("orbMultiplier", (double)CommonConfig.getDefaultOrbXpMultiplier(),
                                Constants.MIN_XP_MULTIPLIER_RANGE, Constants.MAX_XP_MULTIPLIER_RANGE);

        ORB_ATTRACT_RANGE = BUILDER
                .comment("villagerxp.config.orbAttractRange.tooltip")
                .translation(Constants.MOD_ID + ".config.orbAttractRange")
                .defineInRange("orbAttractRange", CommonConfig.getDefaultOrbAttractRange(),
                                Constants.MIN_ORB_ATTRACT_RANGE, Constants.MAX_ORB_ATTRACT_RANGE);

        ORB_PICKUP_RANGE = BUILDER
                .comment("villagerxp.config.orbPickupRange.tooltip")
                .translation(Constants.MOD_ID + ".config.orbPickupRange")
                .defineInRange("orbPickupRange", CommonConfig.getDefaultOrbPickupRange(),
                                Constants.MIN_ORB_PICKUP_RANGE, Constants.MAX_ORB_PICKUP_RANGE);

        LEVELS_PER_BOTTLE = BUILDER
                .comment("villagerxp.config.levelsPerBottle.tooltip")
                .translation(Constants.MOD_ID + ".config.levelsPerBottle")
                .defineInRange("levelsPerBottle", CommonConfig.getDefaultLevelsPerBottle(),
                               Constants.MIN_LEVELS_PER_BOTTLE, Constants.MAX_LEVELS_PER_BOTTLE);

        SPEC = BUILDER.build();
    }

    @Override
    public String getPlatformName() {
        return "Forge";
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
        saveToForgeConfig();
    }

    private void applyToCommonConfig() {
        CommonConfig common = CommonConfig.getInstance();
        common.setXpAmount(XP_AMOUNT.get());
        common.setXpBottlesEnabled(ENABLE_XP_BOTTLES.get());
        common.setXpOrbsEnabled(ENABLE_XP_ORBS.get());
        common.setRequiresCrouching(REQUIRES_CROUCHING.get());
        common.setBottleXpMultiplier(BOTTLE_XP_MULTIPLIER.get().floatValue());
        common.setOrbXpMultiplier(ORB_XP_MULTIPLIER.get().floatValue());
        common.setOrbAttractRange(ORB_ATTRACT_RANGE.get());
        common.setOrbPickupRange(ORB_PICKUP_RANGE.get());
        common.setLevelsPerBottle(LEVELS_PER_BOTTLE.get());
    }

    private void saveToForgeConfig() {
        CommonConfig common = CommonConfig.getInstance();
        XP_AMOUNT.set(common.getXpAmount());
        ENABLE_XP_BOTTLES.set(common.isXpBottlesEnabled());
        ENABLE_XP_ORBS.set(common.isXpOrbsEnabled());
        REQUIRES_CROUCHING.set(common.requiresCrouching());
        BOTTLE_XP_MULTIPLIER.set((double)common.getBottleXpMultiplier());
        ORB_XP_MULTIPLIER.set((double)common.getOrbXpMultiplier());
        ORB_ATTRACT_RANGE.set(common.getOrbAttractRange());
        ORB_PICKUP_RANGE.set(common.getOrbPickupRange());
        LEVELS_PER_BOTTLE.set(common.getLevelsPerBottle());
        // No need to call SPEC.save() as Forge handles this internally
    }
}