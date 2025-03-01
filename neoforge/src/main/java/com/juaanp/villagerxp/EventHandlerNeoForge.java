package com.juaanp.villagerxp;

import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class EventHandlerNeoForge {
    private static final VillagerEventHandler handler = new VillagerEventHandler(CommonConfig.getInstance());

    @SubscribeEvent
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Villager villager) {
            InteractionResult result = handler.handleVillagerInteraction(
                event.getEntity(),
                event.getLevel(),
                event.getEntity().getItemInHand(event.getHand()),
                villager
            );
            
            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        }
    }
} 