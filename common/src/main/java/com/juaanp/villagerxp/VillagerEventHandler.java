package com.juaanp.villagerxp;

import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class VillagerEventHandler {
    private final CommonConfig config;
    private final VillagerUtils villagerUtils;

    public VillagerEventHandler(CommonConfig config) {
        this.config = config;
        this.villagerUtils = new VillagerUtils(config);
    }

    public InteractionResult handleVillagerInteraction(Player player, Level level, ItemStack stack, Villager villager) {
        if (stack.is(Items.EXPERIENCE_BOTTLE) && config.isXpBottlesEnabled()) {
            if (config.requiresCrouching() && !player.isCrouching()) {
                return InteractionResult.PASS;
            }

            if (villagerUtils.canLevelUp(villager) && !villagerUtils.unemployed(villager)) {
                if (!level.isClientSide) {
                    villagerUtils.giveXP(villager, level, 2.2F * config.getBottleXpMultiplier());

                    if (!(player.isCreative() || player.hasInfiniteMaterials()))
                        stack.shrink(1);

                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
} 