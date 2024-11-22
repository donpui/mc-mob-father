package com.minecraft.fathermob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FatherMob.MODID);

    public static final RegistryObject<SoundEvent> FATHER_STEP = SOUND_EVENTS.register("entity.father.step",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FatherMob.MODID, "entity.father.step")));

    public static final RegistryObject<SoundEvent> FATHER_AMBIENT = SOUND_EVENTS.register("entity.father.ambient",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FatherMob.MODID, "entity.father.ambient")));

    public static final RegistryObject<SoundEvent> FATHER_HURT = SOUND_EVENTS.register("entity.father.hurt",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FatherMob.MODID, "entity.father.hurt")));

    public static final RegistryObject<SoundEvent> FATHER_DEATH = SOUND_EVENTS.register("entity.father.death",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FatherMob.MODID, "entity.father.death")));
}
