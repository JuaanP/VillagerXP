package com.juaanp.villagerxp.client;

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
                getOrbRange() != CommonConfig.getDefaultOrbRange() ||
                getShowOrbRanges() != CommonConfig.getDefaultShowOrbRanges();
    }

    private void resetToDefaults() {
        setXpAmount(CommonConfig.getDefaultXpAmount());
        setXpBottlesEnabled(CommonConfig.getDefaultXpBottlesEnabled());
        setXpOrbsEnabled(CommonConfig.getDefaultXpOrbsEnabled());
        setRequireCrouching(CommonConfig.getDefaultRequiresCrouching());
        setBottleXpMultiplier(CommonConfig.getDefaultBottleXpMultiplier());
        setOrbXpMultiplier(CommonConfig.getDefaultOrbXpMultiplier());
        setOrbRange(CommonConfig.getDefaultOrbRange());
        setShowOrbRanges(CommonConfig.getDefaultShowOrbRanges());

        saveConfig();

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
    private Double lastOrbRange = null;
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

        OptionInstance<Boolean> showOrbRangesToggle = OptionInstance.createBoolean(
                "villagerxp.config.showOrbRanges",
                getShowOrbRanges(),
                this::setShowOrbRanges
        );

        OptionInstance<Double> bottleMultiplier = new OptionInstance<>(
                "villagerxp.config.xpMultiplier",
                OptionInstance.noTooltip(),
                (prefix, value) -> prefix.copy()
                        .append(": ")
                        .append(value == 1.0
                                ? Component.translatable("villagerxp.config.default")
                                : Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        d -> d * 4.9 + 0.1,
                        d -> (d - 0.1) / 4.9
                ),
                (double)getBottleXpMultiplier(),
                value -> setBottleXpMultiplier(value.floatValue())
        );

        OptionInstance<Double> orbMultiplier = new OptionInstance<>(
                "villagerxp.config.xpMultiplier",
                OptionInstance.noTooltip(),
                (prefix, value) -> prefix.copy()
                        .append(": ")
                        .append(value == 1.0
                                ? Component.translatable("villagerxp.config.default")
                                : Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        d -> d * 4.9 + 0.1,
                        d -> (d - 0.1) / 4.9
                ),
                (double)getOrbXpMultiplier(),
                value -> setOrbXpMultiplier(value.floatValue())
        );

        OptionInstance<Double> orbRangeOption = new OptionInstance<>(
                "villagerxp.config.orbRange",
                OptionInstance.noTooltip(),
                (component, value) -> component.copy()
                        .append(": ")
                        .append(Component.literal(String.format("%.1f", value))),
                OptionInstance.UnitDouble.INSTANCE.xmap(
                        value -> value * 32.0,
                        value -> value / 32.0
                ),
                getOrbRange(),
                this::setOrbRange
        );

        // Create header widgets for categories
        StringWidget orbsHeader = new StringWidget(ORBS_CATEGORY, this.font);
        StringWidget bottlesHeader = new StringWidget(BOTTLES_CATEGORY, this.font);

        // Add spacing before first category
        this.list.addSmall(new EmptyWidget(10, 8), new EmptyWidget(10, 8));

        // Add orbs category header
        this.list.addSmall(orbsHeader, null);
        this.list.addBig(orbToggle);
        this.list.addSmall(orbRangeOption, orbMultiplier);

        // Add spacing between categories
        this.list.addSmall(new EmptyWidget(10, 16), new EmptyWidget(10, 16));

        // Add bottles category header
        this.list.addSmall(bottlesHeader, null);
        this.list.addBig(bottleToggle);
        this.list.addSmall(crouchToggle, bottleMultiplier);
    }

    private void initializeTrackingFields() {
        lastXpAmount = getXpAmount();
        lastBottlesEnabled = getXpBottlesEnabled();
        lastOrbsEnabled = getXpOrbsEnabled();
        lastRequireCrouching = getRequireCrouching();
        lastBottleMultiplier = getBottleXpMultiplier();
        lastOrbMultiplier = getOrbXpMultiplier();
        lastOrbRange = getOrbRange();
        lastShowOrbRanges = getShowOrbRanges();
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

    protected double getOrbRange() {
        return CommonConfig.getInstance().getOrbRange();
    }

    protected void setOrbRange(double orbRange) {
        CommonConfig.getInstance().setOrbRange(orbRange);
    }

    protected boolean getShowOrbRanges() {
        return CommonConfig.getInstance().getShowOrbRanges();
    }

    protected void setShowOrbRanges(boolean show) {
        CommonConfig.getInstance().setShowOrbRanges(show);
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