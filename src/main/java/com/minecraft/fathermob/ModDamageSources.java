package com.minecraft.fathermob;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageSources {
    public static final ResourceKey<DamageType> BOOK_ATTACK_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE,
            new ResourceLocation(FatherMob.MODID, "book_attack"));

    public static DamageSource bookAttack(Entity attacker) {
        return new DamageSource(
                attacker.level().registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(BOOK_ATTACK_TYPE),
                attacker
        );
    }
}