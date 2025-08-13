package com.flechazo.contact.common.screenhandler;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.gui.screen.EnvelopeScreen;
import com.flechazo.contact.client.gui.screen.PostboxScreen;
import com.flechazo.contact.client.gui.screen.RedPacketEnvelopeScreen;
import com.flechazo.contact.client.gui.screen.WrappingPaperScreen;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public final class ScreenHandlerTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Contact.MOD_ID, Registries.MENU);
    public final static RegistrySupplier<MenuType<WrappingPaperScreenHandler>> WRAPPING_PAPER_CONTAINER =
            register("wrapping_paper", () -> MenuRegistry.ofExtended((id, inv, buf) -> new WrappingPaperScreenHandler(id, inv, false)));

    public static final RegistrySupplier<MenuType<EnvelopeScreenHandler>> ENVELOPE_CONTAINER =
            register("envelope",
                    () -> new MenuType<>(EnvelopeScreenHandler::new, FeatureFlags.VANILLA_SET));

    public static final RegistrySupplier<MenuType<RedPacketEnvelopeScreenHandler>> RED_PACKET_ENVELOPE_CONTAINER =
            register("red_packet_envelope",
                    () -> new MenuType<>(RedPacketEnvelopeScreenHandler::new, FeatureFlags.VANILLA_SET));


    public final static RegistrySupplier<MenuType<PostboxScreenHandler>> RED_POSTBOX_CONTAINER =
            register("red_postbox", () -> MenuRegistry.ofExtended((id, inv, buf) -> {
                boolean isRed = buf.readBoolean();
                return new PostboxScreenHandler(id, inv, isRed);
            }));

    public final static RegistrySupplier<MenuType<PostboxScreenHandler>> GREEN_POSTBOX_CONTAINER =
            register("green_postbox", () -> MenuRegistry.ofExtended((id, inv, buf) -> {
                boolean isRed = buf.readBoolean();
                return new PostboxScreenHandler(id, inv, isRed);
            }));

    private static <C extends AbstractContainerMenu> RegistrySupplier<MenuType<C>> register(String name, Supplier<MenuType<C>> menu) {
        return MENU_TYPES.register(name, menu);
    }

    public static void registerContainers() {
        MenuRegistry.registerScreenFactory(WRAPPING_PAPER_CONTAINER.get(), WrappingPaperScreen::new);
        MenuRegistry.registerScreenFactory(ENVELOPE_CONTAINER.get(), EnvelopeScreen::new);
        MenuRegistry.registerScreenFactory(RED_PACKET_ENVELOPE_CONTAINER.get(), RedPacketEnvelopeScreen::new);
        MenuRegistry.registerScreenFactory(RED_POSTBOX_CONTAINER.get(), PostboxScreen::new);
        MenuRegistry.registerScreenFactory(GREEN_POSTBOX_CONTAINER.get(), PostboxScreen::new);
        System.out.println("Screen注册完了");
    }
}