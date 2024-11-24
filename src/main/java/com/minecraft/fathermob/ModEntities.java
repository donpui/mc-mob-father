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
    // Register for entities
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FatherMob.MODID);

    // Register the Father entity
    public static final RegistryObject<EntityType<Father>> FATHER = ENTITIES.register("father",
            () -> EntityType.Builder.of(Father::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F) // Size of the entity
                    .build("father"));

    // Register the CustomSnowball entity
    public static final RegistryObject<EntityType<BookAttack>> BOOK_ATTACK = ENTITIES.register("book_attack",
            () -> EntityType.Builder.<BookAttack>of(BookAttack::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F) // Size of the snowball
                    .build("book_attack"));

    // Register attributes for the Father entity
    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(FATHER.get(), Father.createAttributes().build());
    }
}
