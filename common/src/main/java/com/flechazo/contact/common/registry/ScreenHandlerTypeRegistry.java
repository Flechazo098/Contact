package com.flechazo.contact.common.registry;

import cc.sighs.oelib.registry.DeferredRegister;
import cc.sighs.oelib.registry.RegisterSupplier;
import cc.sighs.oelib.registry.extra.MenuRegister;
import com.flechazo.contact.Contact;
import com.flechazo.contact.client.gui.screen.EnvelopeScreen;
import com.flechazo.contact.client.gui.screen.PostboxScreen;
import com.flechazo.contact.client.gui.screen.RedPacketEnvelopeScreen;
import com.flechazo.contact.client.gui.screen.WrappingPaperScreen;
import com.flechazo.contact.common.screenhandler.EnvelopeScreenHandler;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import com.flechazo.contact.common.screenhandler.WrappingPaperScreenHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public final class ScreenHandlerTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Contact.MOD_ID);
    public final static RegisterSupplier<MenuType<WrappingPaperScreenHandler>> WRAPPING_PAPER_CONTAINER =
            register("wrapping_paper", () -> MenuRegister.ofExtended((id, inv, buf) -> new WrappingPaperScreenHandler(id, inv, false)));
    public final static RegisterSupplier<MenuType<PostboxScreenHandler>> RED_POSTBOX_CONTAINER =
            register("red_postbox", () -> MenuRegister.ofExtended((id, inv, buf) -> {
                boolean isRed = buf.readBoolean();
                return new PostboxScreenHandler(id, inv, isRed);
            }));
    public final static RegisterSupplier<MenuType<PostboxScreenHandler>> GREEN_POSTBOX_CONTAINER =
            register("green_postbox", () -> MenuRegister.ofExtended((id, inv, buf) -> {
                boolean isRed = buf.readBoolean();
                return new PostboxScreenHandler(id, inv, isRed);
            }));
    public static final RegisterSupplier<MenuType<EnvelopeScreenHandler>> ENVELOPE_CONTAINER =
            register("envelope",
                    () -> new MenuType<>(EnvelopeScreenHandler::new, FeatureFlags.VANILLA_SET));

    private static <C extends AbstractContainerMenu> RegisterSupplier<MenuType<C>> register(String name, Supplier<MenuType<C>> menu) {
        return MENU_TYPES.register(name, menu);
    }

    public static void registerContainers() {
        MenuRegister.registerScreenFactory(WRAPPING_PAPER_CONTAINER, WrappingPaperScreen::new);
        MenuRegister.registerScreenFactory(ENVELOPE_CONTAINER, EnvelopeScreen::new);
        MenuRegister.registerScreenFactory(RED_PACKET_ENVELOPE_CONTAINER, RedPacketEnvelopeScreen::new);
        MenuRegister.registerScreenFactory(RED_POSTBOX_CONTAINER, PostboxScreen::new);
        MenuRegister.registerScreenFactory(GREEN_POSTBOX_CONTAINER, PostboxScreen::new);
    }

    public static final RegisterSupplier<MenuType<RedPacketEnvelopeScreenHandler>> RED_PACKET_ENVELOPE_CONTAINER =
            register("red_packet_envelope",
                    () -> new MenuType<>(RedPacketEnvelopeScreenHandler::new, FeatureFlags.VANILLA_SET));


}