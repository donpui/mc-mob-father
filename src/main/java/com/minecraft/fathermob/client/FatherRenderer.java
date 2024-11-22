package com.minecraft.fathermob.client;

import com.minecraft.fathermob.Father;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class FatherRenderer extends SkeletonRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("fathermob", "textures/entity/father.png");

    public FatherRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        // Ensure the texture is returned for your custom Father entity
        if (entity instanceof Father) {
            return TEXTURE;
        }
        // Fallback to the default texture (optional, if needed)
        return super.getTextureLocation(entity);
    }
}
