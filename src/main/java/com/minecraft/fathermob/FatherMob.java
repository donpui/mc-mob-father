package com.minecraft.fathermob;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(FatherMob.MODID)
public class FatherMob {
    public static final String MODID = "fathermob";

    public FatherMob() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register entities
        ModEntities.ENTITIES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Setup logic if needed
    }
}
