package com.juaanp.villagerxp.client;

import com.juaanp.villagerxp.Constants;
import com.juaanp.villagerxp.config.CommonConfig;
import com.juaanp.villagerxp.config.ConfigHelper;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static com.juaanp.villagerxp.Constants.*;

public class ConfigScreenBase extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("villagerxp.config.title");
    private static final Component RESET = Component.translatable("villagerxp.config.reset");
    private static final Component ORBS_CATEGORY = Component.translatable("villagerxp.config.category.orbs");
    private static final Component BOTTLES_CATEGORY = Component.translatable("villagerxp.config.category.bottles");

    protected final Options options;
    protected Button resetButton;
    protected final Button doneButton = Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(Button.SMALL_WIDTH).build();

    public ConfigScreenBase(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
        this.options = options;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        setResetButtonState(isAnyNonDefault());
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void createResetButton() {
        resetButton = Button.builder(RESET, button -> resetToDefaults())
                .width(Button.SMALL_WIDTH)
                .build();
    }

    protected void setResetButtonState(boolean state) {
        if (resetButton != null) {
            resetButton.active = state;
        }
    }

    protected boolean isAnyNonDefault() {
        return getXpAmount() != CommonConfig.getDefaultXpAmount() ||
                getXpBottlesEnabled() != CommonConfig.getDefaultXpBottlesEnabled() ||
                getXpOrbsEnabled() != CommonConfig.getDefaultXpOrbsEnabled() ||
                getRequireCrouching() != CommonConfig.getDefaultRequiresCrouching() ||
                getBottleXpMultiplier() != CommonConfig.getDefaultBottleXpMultiplier() ||
                getOrbXpMultiplier() != CommonConfig.getDefaultOrbXpMultiplier() ||
                getOrbAttractRange() != CommonConfig.getDefaultOrbAttractRange() ||
                getOrbPickupRange() != CommonConfig.getDefaultOrbPickupRange() ||
                getLevelsPerBottle() != CommonConfig.getDefaultLevelsPerBottle();
    }

    private void resetToDefaults() {
        // Primero configuramos pickup range
        setOrbPickupRange(CommonConfig.getDefaultOrbPickupRange());
        // Después configuramos attract range
        setOrbAttractRange(CommonConfig.getDefaultOrbAttractRange());
        
        // El resto de los resets igual
        setXpAmount(CommonConfig.getDefaultXpAmount());
        setXpBottlesEnabled(CommonConfig.getDefaultXpBottlesEnabled());
        setXpOrbsEnabled(CommonConfig.getDefaultXpOrbsEnabled());
        setRequireCrouching(CommonConfig.getDefaultRequiresCrouching());
        setBottleXpMultiplier(CommonConfig.getDefaultBottleXpMultiplier());
        setOrbXpMultiplier(CommonConfig.getDefaultOrbXpMultiplier());
        setLevelsPerBottle(CommonConfig.getDefaultLevelsPerBottle());
        
        saveConfig();
        
        // Recargar la pantalla
        this.minecraft.setScreen(this.lastScreen);
        this.minecraft.setScreen(new ConfigScreenBase(this.lastScreen, this.options));
    }

    // Fields to track the initial values before user changes
    private Integer lastXpAmount = null;
    private Boolean lastBottlesEnabled = null;
    private Boolean lastOrbsEnabled = null;
    private Boolean lastRequireCrouching = null;
    private Float lastBottleMultiplier = null;
    private Float lastOrbMultiplier = null;
    private Double lastOrbAttractRange = null;
    private Double lastOrbPickupRange = null;
    private Integer lastLevelsPerBottle = null;
    private Boolean lastShowOrbRanges = null; // Add this missing field

    @Override
    protected void addOptions() {
        if (resetButton == null) {
            createResetButton();
        }

        // Initialize all tracking fields at the beginning
        initializeTrackingFields();

        // Create option instances
        OptionInstance<Boolean> bottleToggle = OptionInstance.createBoolean(
                "villagerxp.config.enable",
                getXpBottlesEnabled(),
                this::setXpBottlesEnabled
        );

        OptionInstance<Boolean> orbToggle = OptionInstance.createBoolean(
                "villagerxp.config.enable",
                getXpOrbsEnabled(),
                this::setXpOrbsEnabled
        );

        OptionInstance<Boolean> crouchToggle = OptionInstance.createBoolean(
                "villagerxp.config.requireCtrl",
                getRequireCrouching(),
                this::setRequireCrouching
        );

        OptionInstance<Double> bottleMultiplier = new OptionInstance<>(
                "villagerxp.config.xpMultiplier",
                OptionInstance.noTooltip(),
                (prefix, value) -> prefix.copy()
                        .append(": ")
                        .append(Math.abs(value - DEFAULT_BOTTLE_XP_MULTIPLIER) < Constants.FLOAT_COMPARISON_EPSILON
                                ? Component.translatable("villagerxp.config.default")
                                : Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        value -> Constants.MIN_XP_MULTIPLIER_RANGE + value * (Constants.MAX_XP_MULTIPLIER_RANGE - Constants.MIN_XP_MULTIPLIER_RANGE),
                        value -> (value - Constants.MIN_XP_MULTIPLIER_RANGE) / (Constants.MAX_XP_MULTIPLIER_RANGE - Constants.MIN_XP_MULTIPLIER_RANGE)
                ),
                (double)getBottleXpMultiplier(),
                value -> setBottleXpMultiplier(value.floatValue())
        );

        OptionInstance<Double> orbMultiplier = new OptionInstance<>(
                "villagerxp.config.xpMultiplier",
                OptionInstance.noTooltip(),
                (prefix, value) -> prefix.copy()
                        .append(": ")
                        .append(Math.abs(value - DEFAULT_ORBS_XP_MULTIPLIER) < Constants.FLOAT_COMPARISON_EPSILON
                                ? Component.translatable("villagerxp.config.default")
                                : Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        value -> Constants.MIN_XP_MULTIPLIER_RANGE + value * (Constants.MAX_XP_MULTIPLIER_RANGE - Constants.MIN_XP_MULTIPLIER_RANGE),
                        value -> (value - Constants.MIN_XP_MULTIPLIER_RANGE) / (Constants.MAX_XP_MULTIPLIER_RANGE - Constants.MIN_XP_MULTIPLIER_RANGE)
                ),
                (double)getOrbXpMultiplier(),
                value -> setOrbXpMultiplier(value.floatValue())
        );

        OptionInstance<Double> orbAttractRangeOption = new OptionInstance<>(
                "villagerxp.config.orbAttractRange",
                OptionInstance.noTooltip(),
                (component, value) -> component.copy()
                        .append(": ")
                        .append(Math.abs(value - DEFAULT_ORB_ATTRACT_RANGE) < Constants.FLOAT_COMPARISON_EPSILON
                                ? Component.translatable("villagerxp.config.default")
                                : Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        value -> Constants.MIN_ORB_ATTRACT_RANGE + value * (Constants.MAX_ORB_ATTRACT_RANGE - Constants.MIN_ORB_ATTRACT_RANGE),
                        value -> (value - Constants.MIN_ORB_ATTRACT_RANGE) / (Constants.MAX_ORB_ATTRACT_RANGE - Constants.MIN_ORB_ATTRACT_RANGE)
                ),
                getOrbAttractRange(),
                this::setOrbAttractRange
        );

        OptionInstance<Double> orbPickupRangeOption = new OptionInstance<>(
                "villagerxp.config.orbPickupRange",
                OptionInstance.noTooltip(),
                (component, value) -> component.copy()
                        .append(": ")
                        .append(Math.abs(value - DEFAULT_ORB_PICKUP_RANGE) < Constants.FLOAT_COMPARISON_EPSILON
                                ? Component.translatable("villagerxp.config.default")
                                : Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        value -> Constants.MIN_ORB_PICKUP_RANGE + value * (Constants.MAX_ORB_PICKUP_RANGE - Constants.MIN_ORB_PICKUP_RANGE),
                        value -> (value - Constants.MIN_ORB_PICKUP_RANGE) / (Constants.MAX_ORB_PICKUP_RANGE - Constants.MIN_ORB_PICKUP_RANGE)
                ),
                getOrbPickupRange(),
                this::setOrbPickupRange
        );

        OptionInstance<Integer> levelsPerBottleOption = new OptionInstance<>(
                "villagerxp.config.levelsPerBottle",
                OptionInstance.noTooltip(),
                (component, value) -> {
                    Component display;
                    if (value == Constants.MIN_LEVELS_PER_BOTTLE) {
                        display = Component.translatable("villagerxp.config.auto");
                    } else if (value == Constants.MAX_LEVELS_PER_BOTTLE) {
                        display = Component.translatable("villagerxp.config.max");
                    } else {
                        display = Component.literal(String.valueOf(value));
                    }
                    return component.copy().append(": ").append(display);
                },
                new OptionInstance.IntRange(Constants.MIN_LEVELS_PER_BOTTLE, Constants.MAX_LEVELS_PER_BOTTLE),
                getLevelsPerBottle(),
                this::setLevelsPerBottle
        );

        // Create header widgets for categories
        StringWidget orbsHeader = new StringWidget(ORBS_CATEGORY, this.font);
        StringWidget bottlesHeader = new StringWidget(BOTTLES_CATEGORY, this.font);

        // Add spacing before first category
        this.list.addSmall(new EmptyWidget(10, 8), new EmptyWidget(10, 8));

        // Add orbs category header
        this.list.addSmall(orbsHeader, null);
        this.list.addBig(orbToggle);
        this.list.addBig(orbMultiplier);
        this.list.addBig(orbAttractRangeOption);
        this.list.addBig(orbPickupRangeOption);

        // Add spacing between categories
        this.list.addSmall(new EmptyWidget(10, 16), new EmptyWidget(10, 16));

        // Add bottles category header
        this.list.addSmall(bottlesHeader, null);
        this.list.addBig(bottleToggle);
        this.list.addBig(bottleMultiplier);
        this.list.addBig(levelsPerBottleOption);
        this.list.addBig(crouchToggle);
    }

    private void initializeTrackingFields() {
        lastXpAmount = getXpAmount();
        lastBottlesEnabled = getXpBottlesEnabled();
        lastOrbsEnabled = getXpOrbsEnabled();
        lastRequireCrouching = getRequireCrouching();
        lastBottleMultiplier = getBottleXpMultiplier();
        lastOrbMultiplier = getOrbXpMultiplier();
        lastOrbAttractRange = getOrbAttractRange();
        lastOrbPickupRange = getOrbPickupRange();
        lastLevelsPerBottle = getLevelsPerBottle();
    }

    private static class EmptyWidget extends AbstractWidget {
        public EmptyWidget(int width, int height) {
            super(0, 0, width, height, Component.empty());
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {}

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }

    @Override
    protected void addFooter() {
        LinearLayout linearLayout = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        if (resetButton != null) {
            linearLayout.addChild(resetButton);
            linearLayout.addChild(doneButton);
        } else {
            super.addFooter();
        }
    }

    protected int getXpAmount() {
        return CommonConfig.getInstance().getXpAmount();
    }

    protected void setXpAmount(int amount) {
        CommonConfig.getInstance().setXpAmount(amount);
    }

    protected boolean getXpBottlesEnabled() {
        return CommonConfig.getInstance().isXpBottlesEnabled();
    }

    protected void setXpBottlesEnabled(boolean enabled) {
        CommonConfig.getInstance().setXpBottlesEnabled(enabled);
    }

    protected boolean getXpOrbsEnabled() {
        return CommonConfig.getInstance().isXpOrbsEnabled();
    }

    protected void setXpOrbsEnabled(boolean enabled) {
        CommonConfig.getInstance().setXpOrbsEnabled(enabled);
    }

    protected boolean getRequireCrouching() {
        return CommonConfig.getInstance().requiresCrouching();
    }

    protected void setRequireCrouching(boolean require) {
        CommonConfig.getInstance().setRequiresCrouching(require);
    }

    protected float getBottleXpMultiplier() {
        return CommonConfig.getInstance().getBottleXpMultiplier();
    }

    protected void setBottleXpMultiplier(float multiplier) {
        CommonConfig.getInstance().setBottleXpMultiplier(multiplier);
    }

    protected float getOrbXpMultiplier() {
        return CommonConfig.getInstance().getOrbXpMultiplier();
    }

    protected void setOrbXpMultiplier(float multiplier) {
        CommonConfig.getInstance().setOrbXpMultiplier(multiplier);
    }

    protected double getOrbAttractRange() {
        return CommonConfig.getInstance().getOrbAttractRange();
    }

    protected void setOrbAttractRange(double orbAttractRange) {
        CommonConfig.getInstance().setOrbAttractRange(orbAttractRange);
    }

    protected double getOrbPickupRange() {
        return CommonConfig.getInstance().getOrbPickupRange();
    }

    protected void setOrbPickupRange(double orbPickupRange) {
        CommonConfig.getInstance().setOrbPickupRange(orbPickupRange);
    }

    protected int getLevelsPerBottle() {
        return CommonConfig.getInstance().getLevelsPerBottle();
    }

    protected void setLevelsPerBottle(int levels) {
        CommonConfig.getInstance().setLevelsPerBottle(levels);
    }

    protected void saveConfig() {
        ConfigHelper.save();
    }

    @Override
    public void onClose() {
        saveConfig();
        super.onClose();
    }

    @Override
    public void removed() {
        saveConfig();
        super.removed();
    }
}