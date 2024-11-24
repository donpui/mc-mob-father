package com.minecraft.fathermob.client;

import com.minecraft.fathermob.ModEntities;
import com.minecraft.fathermob.client.model.FatherModel;
import com.minecraft.fathermob.client.BookAttackRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "fathermob", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Register Father renderer
        event.registerEntityRenderer(ModEntities.FATHER.get(), FatherRenderer::new);
        // Register BookAttack renderer
        event.registerEntityRenderer(ModEntities.BOOK_ATTACK.get(), BookAttackRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FatherModel.LAYER_LOCATION, FatherModel::createBodyLayer);
    }
}