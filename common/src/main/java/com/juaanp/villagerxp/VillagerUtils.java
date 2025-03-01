package com.juaanp.villagerxp;

import com.juaanp.villagerxp.access.VillagerAccessor;
import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;

public class VillagerUtils {
    private final CommonConfig config;

    public VillagerUtils(CommonConfig config) {
        this.config = config;
    }

    public void giveXP(Villager villager, Level level) {
        giveXP(villager, level, 1.0F);
    }

    public void giveXP(Villager villager, Level level, float multiplier) {
        RandomSource random = villager.getRandom();

        villager.setVillagerXp((int)(villager.getVillagerXp() + config.getXpAmount() * multiplier));
        villager.playSound(
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                0.1F,
                (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F);

        if (level instanceof ServerLevel serverLevel){
            spawnXPParticles(villager, serverLevel, random);
            villager.playCelebrateSound();

            if (shouldLevelUp(villager)) {
                levelUp(villager, serverLevel, random);
            }
        }
    }

    public boolean shouldLevelUp(Villager villager){
        int i = villager.getVillagerData().getLevel();
        return VillagerData.canLevelUp(i) && villager.getVillagerXp() >= VillagerData.getMaxXpPerLevel(i);
    }

    public boolean canLevelUp(Villager villager){
        int i = villager.getVillagerData().getLevel();
        return VillagerData.canLevelUp(i);
    }

    public void levelUp(Villager villager, ServerLevel serverLevel, RandomSource random){
        if (villager instanceof VillagerAccessor accessor) {
            accessor.invokeIncreaseMerchantCareer();
            accessor.invokeResendOffersToTradingPlayer();
            villager.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
        }
    }

    public boolean unemployed(Villager villager){
        return villager.getVillagerData().getProfession() == VillagerProfession.NONE;
    }

    public void spawnXPParticles(Villager villager, ServerLevel serverLevel, RandomSource random){
        for(int i = 0; i < 5; ++i) {
            double dx = random.nextGaussian() * 0.02;
            double dy = random.nextGaussian() * 0.02;
            double dz = random.nextGaussian() * 0.02;
            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    villager.getRandomX(1.0),
                    villager.getRandomY() + 0.5,
                    villager.getRandomZ(1.0),
                    1,
                    dx, dy, dz,
                    0
            );
        }
    }
}
