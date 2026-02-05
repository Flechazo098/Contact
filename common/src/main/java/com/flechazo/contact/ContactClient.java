package com.flechazo.contact;

import cc.sighs.oelib.registry.extra.ClientTooltipComponentRegister;
import cc.sighs.oelib.registry.extra.EntityRendererRegister;
import com.flechazo.contact.client.color.block.MailboxBlockColor;
import com.flechazo.contact.client.color.item.MailboxItemColor;
import com.flechazo.contact.client.gui.tooltip.PackageTooltipComponent;
import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.client.renderer.MailboxTileEntityRenderer;
import com.flechazo.contact.client.renderer.PostcardEntityRenderer;
import com.flechazo.contact.common.config.ContactClientConfig;
import com.flechazo.contact.common.entity.EntityTypeRegistry;
import com.flechazo.contact.common.registry.BlockRegistry;
import com.flechazo.contact.common.registry.ItemRegistry;
import com.flechazo.contact.common.registry.ScreenHandlerTypeRegistry;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import cc.sighs.oelib.registry.extra.ShaderRegister;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import cc.sighs.oelib.bless.OverlayRegistry;
import com.flechazo.contact.client.overlay.RedPacketOverlay;
import com.flechazo.contact.client.shader.ContactShaderResources;

@Environment(EnvType.CLIENT)
public class ContactClient {
    public static final BlockColor BLOCK_MAILBOX_COLOR = new MailboxBlockColor();
    public static final ItemColor ITEM_MAILBOX_COLOR = new MailboxItemColor();

    public static void onInitializeClient() {
        ContactClientConfig.register();
        ScreenHandlerTypeRegistry.registerContainers();
        BlockRegistry.registerColors();
        ItemRegistry.registerColors();
        BlockRegistry.registerRenderLayer();
        EntityRendererRegister.register(EntityTypeRegistry.POSTCARD, PostcardEntityRenderer::new);
        BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY.listen(blockEntity -> BlockEntityRenderers.register(blockEntity, MailboxTileEntityRenderer::new));
        ClientTooltipComponentRegister.register(PackageTooltipData.class, PackageTooltipComponent::new);

        ShaderRegister.register(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "red_packet"),
                DefaultVertexFormat.POSITION_TEX, ContactShaderResources::setRedPacketShader);
        OverlayRegistry.register(RedPacketOverlay.INSTANCE);
    }
}
