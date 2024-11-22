package com.minecraft.fathermob.client;

import com.minecraft.fathermob.Father;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FatherRenderer extends SkeletonRenderer<Father> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("fathermob", "textures/entity/father.png");

    public FatherRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Father entity) {
        return TEXTURE;
    }
}
