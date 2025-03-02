package com.juaanp.villagerxp;

import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
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
                if (level instanceof ServerLevel serverLevel) {

                    if(config.getLevelsPerBottle() == 0) {
                        villagerUtils.giveXP(villager, level, 2.2F * config.getBottleXpMultiplier());

                    } else if (config.getLevelsPerBottle() >= 1 && config.getLevelsPerBottle() <= Constants.MAX_LEVELS_PER_BOTTLE) {
                        int currentLevel = villager.getVillagerData().getLevel();
                        int targetLevel = Math.min(VillagerData.MAX_VILLAGER_LEVEL, currentLevel + config.getLevelsPerBottle());

                        while(villagerUtils.canLevelUp(villager) && villager.getVillagerData().getLevel() < targetLevel) {
                            villagerUtils.levelUp(villager);
                        }

                        villagerUtils.spawnLevelUpParticles(villager);
                        villagerUtils.spawnXPParticles(villager, serverLevel, villager.getRandom());
                        villagerUtils.playSound(villager, villager.getRandom());
                    } else {
                        Constants.LOG.error("Wrong levels per bottle, reset settings to default!");
                        config.setLevelsPerBottle(0);
                        return InteractionResult.PASS;
                    }

                    if (!(player.isCreative() || player.hasInfiniteMaterials())) {
                        stack.shrink(1);
                    }

                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
} 