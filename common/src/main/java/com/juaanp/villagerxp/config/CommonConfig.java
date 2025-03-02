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
    private double orbAttractRange;
    private double orbPickupRange;
    private int levelsPerBottle;

    // Private constructor for singleton
    private CommonConfig() {
        // Default values
        this.xpAmount = DEFAULT_XP_AMOUNT;
        this.xpBottlesEnabled = DEFAULT_BOTTLES_ENABLED;
        this.xpOrbsEnabled = DEFAULT_ORBS_ENABLED;
        this.requiresCrouching = DEFAULT_REQUIRES_CROUCHING;
        this.bottleXpMultiplier = DEFAULT_BOTTLE_XP_MULTIPLIER;
        this.orbXpMultiplier = DEFAULT_ORBS_XP_MULTIPLIER;
        this.orbAttractRange = DEFAULT_ORB_ATTRACT_RANGE;
        this.orbPickupRange = DEFAULT_ORB_PICKUP_RANGE;
        this.levelsPerBottle = DEFAULT_LEVELS_PER_BOTTLE;
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

    public static double getDefaultOrbAttractRange() {
        return DEFAULT_ORB_ATTRACT_RANGE;
    }

    public static double getDefaultOrbPickupRange() {
        return DEFAULT_ORB_PICKUP_RANGE;
    }

    public static int getDefaultLevelsPerBottle() {
        return DEFAULT_LEVELS_PER_BOTTLE;
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

    public double getOrbAttractRange() {
        return orbAttractRange;
    }

    public void setOrbAttractRange(double orbAttractRange) {
        this.orbAttractRange = Math.max(orbAttractRange, this.orbPickupRange);
    }

    public double getOrbPickupRange() {
        return orbPickupRange;
    }

    public void setOrbPickupRange(double orbPickupRange) {
        this.orbPickupRange = orbPickupRange;
        if (this.orbAttractRange < orbPickupRange) {
            this.orbAttractRange = orbPickupRange;
        }
    }

    public int getLevelsPerBottle() {
        return levelsPerBottle;
    }

    public void setLevelsPerBottle(int levelsPerBottle) {
        this.levelsPerBottle = levelsPerBottle;
    }
}