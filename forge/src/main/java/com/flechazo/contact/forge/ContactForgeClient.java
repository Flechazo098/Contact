package com.flechazo.contact.forge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.ContactClient;
import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.client.color.block.MailboxBlockColor;
import com.flechazo.contact.client.color.item.MailboxItemColor;
import com.flechazo.contact.client.gui.tooltip.PackageTooltipComponent;
import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.client.renderer.MailboxTileEntityRenderer;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.flechazo.contact.common.block.BlockRegistry.*;
import static com.flechazo.contact.common.block.BlockRegistry.BLACK_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.BLUE_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.BROWN_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.CYAN_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.GRAY_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.GREEN_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.LIGHT_GRAY_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.LIME_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.PINK_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.PURPLE_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.RED_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.WHITE_MAILBOX;
import static com.flechazo.contact.common.block.BlockRegistry.YELLOW_MAILBOX;
import static com.flechazo.contact.common.item.ItemRegistry.*;
import static com.flechazo.contact.common.item.ItemRegistry.WHITE_MAILBOX_ITEM;

@Mod.EventBusSubscriber(modid = Contact.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ContactForgeClient {
    public static final BlockColor BLOCK_MAILBOX_COLOR = new MailboxBlockColor();
    public static final ItemColor ITEM_MAILBOX_COLOR = new MailboxItemColor();
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(BlockEntityTypeRegistry.MAILBOX_BLOCK_ENTITY.get(), MailboxTileEntityRenderer::new);
            ContactClient.onInitializeClient();
        });
    }

    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(PackageTooltipData.class, PackageTooltipComponent::new);
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(BLOCK_MAILBOX_COLOR,
                ORANGE_MAILBOX.get(), MAGENTA_MAILBOX.get(), LIGHT_BLUE_MAILBOX.get(), YELLOW_MAILBOX.get(),
                LIME_MAILBOX.get(), PINK_MAILBOX.get(), GRAY_MAILBOX.get(), LIGHT_GRAY_MAILBOX.get(),
                CYAN_MAILBOX.get(), PURPLE_MAILBOX.get(), BLUE_MAILBOX.get(), BROWN_MAILBOX.get(),
                GREEN_MAILBOX.get(), RED_MAILBOX.get(), BLACK_MAILBOX.get(), WHITE_MAILBOX.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(ITEM_MAILBOX_COLOR,
                ORANGE_MAILBOX_ITEM.get(), MAGENTA_MAILBOX_ITEM.get(), LIGHT_BLUE_MAILBOX_ITEM.get(), YELLOW_MAILBOX_ITEM.get(),
                LIME_MAILBOX_ITEM.get(), PINK_MAILBOX_ITEM.get(), GRAY_MAILBOX_ITEM.get(), LIGHT_GRAY_MAILBOX_ITEM.get(),
                CYAN_MAILBOX_ITEM.get(), PURPLE_MAILBOX_ITEM.get(), BLUE_MAILBOX_ITEM.get(), BROWN_MAILBOX_ITEM.get(),
                GREEN_MAILBOX_ITEM.get(), RED_MAILBOX_ITEM.get(), BLACK_MAILBOX_ITEM.get(), WHITE_MAILBOX_ITEM.get());
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(Contact.MOD_ID, "postcard_pin", ""));
        event.register(new ModelResourceLocation(Contact.MOD_ID, "postcard", ""));
    }
}