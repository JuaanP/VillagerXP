package com.juaanp.villagerxp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "villagerxp";
	public static final String MOD_NAME = "VillagerXP";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final int DEFAULT_XP_AMOUNT = 10;
	public static final boolean DEFAULT_BOTTLES_ENABLED = true;
	public static final boolean DEFAULT_ORBS_ENABLED = true;
	public static final boolean DEFAULT_REQUIRES_CROUCHING = true;
	public static final float DEFAULT_BOTTLE_XP_MULTIPLIER = 1.0f;
	public static final float DEFAULT_ORBS_XP_MULTIPLIER = 1.0f;
	public static final float DEFAULT_ORB_ATTRACT_RANGE = 4.5f;
	public static final float DEFAULT_ORB_PICKUP_RANGE = 1.5f;

	public static final double MIN_XP_MULTIPLIER_RANGE = 0.1;
	public static final double MAX_XP_MULTIPLIER_RANGE = 5.0;

	public static final double MIN_ORB_ATTRACT_RANGE = 1.0;
	public static final double MAX_ORB_ATTRACT_RANGE = 16.0;

	public static final double MIN_ORB_PICKUP_RANGE = 0.5;
	public static final double MAX_ORB_PICKUP_RANGE = 10.0;

	// Tolerancia para comparaciones de valores de punto flotante
	public static final double FLOAT_COMPARISON_EPSILON = 0.05;
}