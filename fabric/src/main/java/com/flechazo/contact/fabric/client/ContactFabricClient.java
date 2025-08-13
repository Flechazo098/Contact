package com.flechazo.contact.fabric.client;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.client.gui.tooltip.PackageTooltipComponent;
import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.client.renderer.MailboxTileEntityRenderer;
import com.flechazo.contact.common.block.BlockRegistry;
import com.flechazo.contact.common.config.ContactClientConfig;
import com.flechazo.contact.common.screenhandler.ScreenHandlerTypeRegistry;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
import com.flechazo.contact.network.NetworkHelper;
import com.iafenvoy.jupiter.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;

public final class ContactFabricClient implements ClientModInitializer, ModelLoadingPlugin {
    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(ContactClientConfig.INSTANCE);
        NetworkHelper.initializeClient();
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ScreenHandlerTypeRegistry.registerContainers());
        BlockEntityRenderers.register(BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY.get(), MailboxTileEntityRenderer::new);
        ClientProxy.bindEntityRenderer();
        BlockColorsRegistry.init();
        ItemColorsRegistry.init();
        BlockRegistry.registerRenderLayer();
        registerTooltipComponents();
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
                ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "postcard_pin"),
                ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "postcard")
        );
    }
}