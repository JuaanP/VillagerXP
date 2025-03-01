package com.juaanp.villagerxp.mixin;

import com.juaanp.villagerxp.VillagerUtils;
import com.juaanp.villagerxp.config.CommonConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
    @Unique private ExperienceOrb villagerXP$orb;
    @Unique private Level villagerXP$level;
    @Unique private final CommonConfig villagerXP$config = CommonConfig.getInstance();
    @Unique private final VillagerUtils villagerXP$utils = new VillagerUtils(villagerXP$config);
    @Shadow private int count;

    @Inject(method = "tick", at = @At(value = "HEAD",
            target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void tick(CallbackInfo ci) {
        if (!villagerXP$config.isXpOrbsEnabled()) {return;}

        double orbRange = villagerXP$config.getOrbRange();
        double orbRangeSqr = orbRange * orbRange;

        Villager nearestVillager = villagerXP$getNearestVillager(orbRange);

        if (nearestVillager != null &&
                villagerXP$utils.canLevelUp(nearestVillager) &&
                !villagerXP$utils.unemployed(nearestVillager)) {

            Vec3 directionToVillager = new Vec3(
                    nearestVillager.getX() - this.villagerXP$orb.getX(),
                    nearestVillager.getY() + (double) nearestVillager.getEyeHeight() / 2.0 - this.villagerXP$orb.getY(),
                    nearestVillager.getZ() - this.villagerXP$orb.getZ()
            );

            double distanceToVillagerSqr = directionToVillager.lengthSqr();

            if (distanceToVillagerSqr < orbRangeSqr) {
                double e = 1.0 - Math.sqrt(distanceToVillagerSqr) / orbRange;
                this.villagerXP$orb.setDeltaMovement(
                        this.villagerXP$orb.getDeltaMovement().add(
                                directionToVillager.normalize().scale(e * e * 0.1)
                        )
                );

                if(distanceToVillagerSqr < 9.0 && !villagerXP$level.isClientSide) {
                    nearestVillager.take(this.villagerXP$orb, 1);
                    villagerXP$utils.giveXP(nearestVillager, villagerXP$level, villagerXP$config.getOrbXpMultiplier());

                    --this.count;
                    if (this.count == 0) {
                        this.villagerXP$orb.discard();
                    }
                }
            }
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V",
            at = @At(value = "RETURN"))
    private void init(EntityType entityType, Level level, CallbackInfo ci) {
        this.villagerXP$orb = (ExperienceOrb) (Object) this;
        this.villagerXP$level = level;
    }

    @Unique
    private Villager villagerXP$getNearestVillager(double radius) {
        AABB boundingBox = this.villagerXP$orb.getBoundingBox().inflate(radius);

        List<Villager> nearbyVillagers = this.villagerXP$level.getEntitiesOfClass(
                Villager.class,
                boundingBox,
                villager -> !villager.isSpectator() && !villager.isDeadOrDying()
        );

        if (nearbyVillagers.isEmpty()) {
            return null;
        }

        return nearbyVillagers.stream()
                .min((v1, v2) -> Double.compare(
                        v1.distanceToSqr(this.villagerXP$orb),
                        v2.distanceToSqr(this.villagerXP$orb)
                ))
                .orElse(null);
    }
}