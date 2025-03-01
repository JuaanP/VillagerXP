package com.juaanp.villagerxp.config;

import com.juaanp.villagerxp.Constants;
import com.juaanp.villagerxp.platform.Services;

public class ConfigHelper {
    private static boolean isInitialized = false;
    
    public static void initialize() {
        if (!isInitialized) {
            Services.PLATFORM.loadConfig();
            isInitialized = true;
        }
    }
    
    public static void save() {
        Services.PLATFORM.saveConfig();
    }
}