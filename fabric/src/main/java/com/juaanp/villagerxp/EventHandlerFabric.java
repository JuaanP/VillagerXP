package com.juaanp.villagerxp;

import com.juaanp.villagerxp.config.CommonConfig;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;

public class EventHandlerFabric {
    private static final VillagerEventHandler handler = new VillagerEventHandler(CommonConfig.getInstance());

    public static void init() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof Villager villager) {
                return handler.handleVillagerInteraction(
                        player,
                        world,
                        player.getItemInHand(hand),
                        villager
                );
            }
            return InteractionResult.PASS;
        });
    }
}