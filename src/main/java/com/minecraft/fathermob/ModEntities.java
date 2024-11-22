package com.minecraft.fathermob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FatherMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FatherMob.MODID);

    public static final RegistryObject<EntityType<Father>> FATHER = ENTITIES.register("father",
            () -> EntityType.Builder.of(Father::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F) // Size of the entity
                    .build("father"));

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        // Register attributes for the "father" entity
        event.put(FATHER.get(), Father.createAttributes().build());
    }
}
