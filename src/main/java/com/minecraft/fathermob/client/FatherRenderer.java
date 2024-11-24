package com.minecraft.fathermob.client;

import com.minecraft.fathermob.Father;
import com.minecraft.fathermob.client.model.FatherModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FatherRenderer extends MobRenderer<Father, FatherModel<Father>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("fathermob", "textures/entity/father.png");

    public FatherRenderer(EntityRendererProvider.Context context) {
        super(context, new FatherModel<>(context.bakeLayer(FatherModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Father entity) {
        return TEXTURE;
    }
}
