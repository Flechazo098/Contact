package com.flechazo.contact.fabric.client;

import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.client.gui.tooltip.PackageTooltipComponent;
import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.client.renderer.MailboxTileEntityRenderer;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
import com.flechazo.contact.fabric.network.VersionCheckHandler;
import com.flechazo.contact.network.AddresseeDataMessage;
import com.mafuyu404.oelib.fabric.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;

public final class ContactFabricClient implements ClientModInitializer, ModelLoadingPlugin {
    @Override
    public void onInitializeClient() {
        NetworkManager.registerClientPacket(AddresseeDataMessage.class);
        VersionCheckHandler.registerClientMessage();
        BlockEntityRenderers.register(BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY.get(), MailboxTileEntityRenderer::new);

        registerTooltipComponents();

        ClientProxy.onInitializeClient();
        BlockColorsRegistry.init();
        ItemColorsRegistry.init();
    }

    private void registerTooltipComponents() {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof PackageTooltipData packageData) {
                return new PackageTooltipComponent(packageData);
            }
            return null;
        });
    }

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.addModels(
                new ModelResourceLocation("contact", "postcard_pin", ""),
                new ModelResourceLocation("contact", "postcard", "")
        );
    }
}