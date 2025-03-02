package com.juaanp.villagerxp;

import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandlerForge {
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