package com.minecraft.fathermob;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(FatherMob.MODID)
public class FatherMob {
    public static final String MODID = "fathermob";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public FatherMob() {
        LOGGER.info("Initializing FatherMob");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        modEventBus.addListener(this::setup);

        // Register entities
        ModEntities.ENTITIES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        //ModDamageSources.DAMAGE_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(FatherSpawnController.class);
        LOGGER.info("FatherMob registration complete");
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("FatherMob setup phase beginning");
        event.enqueueWork(() -> {
            // Any setup work that needs to be done after mod loading
            LOGGER.info("FatherMob setup work completed");
        });
    }
}