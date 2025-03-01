package com.juaanp.villagerxp.config;

import com.juaanp.villagerxp.Constants;

import static com.juaanp.villagerxp.Constants.*;

public class CommonConfig {
    private static CommonConfig instance;

    private int xpAmount;
    private boolean xpBottlesEnabled;
    private boolean xpOrbsEnabled;
    private boolean requiresCrouching;
    private float bottleXpMultiplier;
    private float orbXpMultiplier;
    private double orbRange;
    private boolean showOrbRanges; // New field for debug rendering

    // Private constructor for singleton
    private CommonConfig() {
        // Default values
        this.xpAmount = DEFAULT_XP_AMOUNT;
        this.xpBottlesEnabled = DEFAULT_BOTTLES_ENABLED;
        this.xpOrbsEnabled = DEFAULT_ORBS_ENABLED;
        this.requiresCrouching = DEFAULT_REQUIRES_CROUCHING;
        this.bottleXpMultiplier = DEFAULT_BOTTLE_XP_MULTIPLIER;
        this.orbXpMultiplier = DEFAULT_ORBS_XP_MULTIPLIER;
        this.orbRange = DEFAULT_ORB_RANGE;
        this.showOrbRanges = DEFAULT_SHOW_ORB_RANGES;
    }

    public static CommonConfig getInstance() {
        if (instance == null) {
            instance = new CommonConfig();
        }
        return instance;
    }

    // Default getters
    public static int getDefaultXpAmount() {
        return DEFAULT_XP_AMOUNT;
    }

    public static boolean getDefaultXpBottlesEnabled() {
        return DEFAULT_BOTTLES_ENABLED;
    }

    public static boolean getDefaultXpOrbsEnabled() {
        return DEFAULT_ORBS_ENABLED;
    }

    public static boolean getDefaultRequiresCrouching() {
        return DEFAULT_REQUIRES_CROUCHING;
    }

    public static float getDefaultBottleXpMultiplier() {
        return DEFAULT_BOTTLE_XP_MULTIPLIER;
    }

    public static float getDefaultOrbXpMultiplier() {
        return DEFAULT_ORBS_XP_MULTIPLIER;
    }

    public static double getDefaultOrbRange() {
        return DEFAULT_ORB_RANGE;
    }

    public static boolean getDefaultShowOrbRanges() {
        return DEFAULT_SHOW_ORB_RANGES;
    }

    public int getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(int xpAmount) {
        this.xpAmount = xpAmount;
    }

    public boolean isXpBottlesEnabled() {
        return xpBottlesEnabled;
    }

    public void setXpBottlesEnabled(boolean xpBottlesEnabled) {
        this.xpBottlesEnabled = xpBottlesEnabled;
    }

    public boolean isXpOrbsEnabled() {
        return xpOrbsEnabled;
    }

    public void setXpOrbsEnabled(boolean xpOrbsEnabled) {
        this.xpOrbsEnabled = xpOrbsEnabled;
    }

    public boolean requiresCrouching() {
        return requiresCrouching;
    }

    public void setRequiresCrouching(boolean requiresCrouching) {
        this.requiresCrouching = requiresCrouching;
    }

    public float getBottleXpMultiplier() {
        return bottleXpMultiplier;
    }

    public void setBottleXpMultiplier(float bottleXpMultiplier) {
        this.bottleXpMultiplier = bottleXpMultiplier;
    }

    public float getOrbXpMultiplier() {
        return orbXpMultiplier;
    }

    public void setOrbXpMultiplier(float orbXpMultiplier) {
        this.orbXpMultiplier = orbXpMultiplier;
    }

    public double getOrbRange() {
        return orbRange;
    }

    public void setOrbRange(double orbRange) {
        this.orbRange = orbRange;
    }

    public boolean getShowOrbRanges() {
        return showOrbRanges;
    }

    public void setShowOrbRanges(boolean showOrbRanges) {
        this.showOrbRanges = showOrbRanges;
        // Toggle debug rendering when this is changed
        try {
            Class<?> rendererMixinClass = Class.forName("com.juaanp.villagerxp.mixin.ExperienceOrbRendererMixin");
            java.lang.reflect.Method toggleMethod = rendererMixinClass.getMethod("toggleDebugRendering");
            if (showOrbRanges) {
                toggleMethod.invoke(null);
            }
        } catch (Exception e) {
            // Silently fail, debug rendering will still work through the static field
        }
    }
}