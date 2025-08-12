package com.flechazo.contact.common.screenhandler;

import com.flechazo.contact.client.gui.screen.EnvelopeScreen;
import com.flechazo.contact.client.gui.screen.PostboxScreen;
import com.flechazo.contact.client.gui.screen.RedPacketEnvelopeScreen;
import com.flechazo.contact.client.gui.screen.WrappingPaperScreen;
import com.flechazo.contact.common.registry.RegistryManager;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ScreenHandlerTypeRegistry {
    public final static RegistrySupplier<MenuType<WrappingPaperScreenHandler>> WRAPPING_PAPER_CONTAINER =
            RegistryManager.MENU_TYPES.register("wrapping_paper", () -> MenuRegistry.ofExtended((id, inv, buf) -> new WrappingPaperScreenHandler(id, inv, false)));

    public static final RegistrySupplier<MenuType<EnvelopeScreenHandler>> ENVELOPE_CONTAINER =
            RegistryManager.MENU_TYPES.register("envelope",
                    () -> new MenuType<>(EnvelopeScreenHandler::new, FeatureFlags.VANILLA_SET));

    public static final RegistrySupplier<MenuType<RedPacketEnvelopeScreenHandler>> RED_PACKET_ENVELOPE_CONTAINER =
            RegistryManager.MENU_TYPES.register("red_packet_envelope",
                    () -> new MenuType<>(RedPacketEnvelopeScreenHandler::new, FeatureFlags.VANILLA_SET));


    public final static RegistrySupplier<MenuType<PostboxScreenHandler>> RED_POSTBOX_CONTAINER =
            RegistryManager.MENU_TYPES.register("red_postbox", () -> MenuRegistry.ofExtended((id, inv, buf) -> new PostboxScreenHandler(id, inv, true)));

    public final static RegistrySupplier<MenuType<PostboxScreenHandler>> GREEN_POSTBOX_CONTAINER =
            RegistryManager.MENU_TYPES.register("green_postbox", () -> MenuRegistry.ofExtended((id, inv, buf) -> new PostboxScreenHandler(id, inv, false)));

    public static void init() {

    }

    public static void clientInit() {
        MenuRegistry.registerScreenFactory(WRAPPING_PAPER_CONTAINER.get(), WrappingPaperScreen::new);
        MenuRegistry.registerScreenFactory(ENVELOPE_CONTAINER.get(), EnvelopeScreen::new);
        MenuRegistry.registerScreenFactory(RED_PACKET_ENVELOPE_CONTAINER.get(), RedPacketEnvelopeScreen::new);
        MenuRegistry.registerScreenFactory(RED_POSTBOX_CONTAINER.get(), PostboxScreen::new);
        MenuRegistry.registerScreenFactory(GREEN_POSTBOX_CONTAINER.get(), PostboxScreen::new);
    }
}