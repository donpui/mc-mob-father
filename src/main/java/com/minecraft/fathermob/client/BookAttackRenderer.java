package com.minecraft.fathermob.client;

import com.minecraft.fathermob.BookAttack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BookAttackRenderer extends EntityRenderer<BookAttack> {
    private final ItemRenderer itemRenderer;
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/item/book.png");

    public BookAttackRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BookAttack entity, float entityYaw, float partialTicks,
                       PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();

        // Scale and position the book
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        // Rotate the book as it flies
        matrixStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(
                entity.tickCount * 50F));

        // Render the book item
        ItemStack bookStack = new ItemStack(Items.BOOK);
        this.itemRenderer.renderStatic(bookStack, ItemDisplayContext.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer,
                entity.level(), entity.getId());

        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BookAttack entity) {
        return TEXTURE;
    }
}