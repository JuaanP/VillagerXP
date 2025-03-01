package com.juaanp.villagerxp.mixin;

import com.juaanp.villagerxp.access.VillagerAccessor;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Villager.class)
public abstract class VillagerMixin implements VillagerAccessor {

    @Invoker(value = "increaseMerchantCareer")
    public abstract void invokeIncreaseMerchantCareer();

    @Invoker(value = "resendOffersToTradingPlayer")
    public abstract void invokeResendOffersToTradingPlayer();

//    @ModifyConstant(method = "rewardTradeXp",
//    constant = @Constant(intValue = 40))
//    private int zeroMerchantTimer(int constant) {
//        return 0;
//    }

//    @Redirect(
//        method = "customServerAiStep",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/world/entity/npc/Villager;isTrading()Z",
//                ordinal = 0
//        )
//    )
//    private boolean ignoreIsTrading(Villager instance) {
//        return false;
//    }
}