package com.flechazo.contact.client.renderer;

import com.flechazo.contact.common.registry.ItemRegistry;
import com.flechazo.contact.common.tileentity.MailboxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import static java.lang.Math.PI;

public class MailboxTileEntityRenderer implements BlockEntityRenderer<MailboxBlockEntity> {
    private final ItemRenderer itemRenderer;


    public MailboxTileEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }


    @Override
    public void render(MailboxBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (tileEntityIn.isOpened()) {
            ItemStack mail = new ItemStack(ItemRegistry.LETTER.get());

            poseStack.pushPose();
            poseStack.translate(0.5, 1 + 0.1 * Mth.sin((float) ((tileEntityIn.getAngel() + partialTicks) / 20.0D * PI)), 0.5);
            poseStack.scale(0.6F, 0.6F, 0.6F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().player.yHeadRot));

            itemRenderer.renderStatic(mail, ItemDisplayContext.FIXED, 15728880, overlay, poseStack, vertexConsumers, tileEntityIn.getLevel(), 0);

            poseStack.popPose();
        }
    }
}
