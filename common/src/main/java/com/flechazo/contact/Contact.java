package com.flechazo.contact;

import cc.sighs.oelib.data.DataRegistry;
import cc.sighs.oelib.network.api.NetworkManager;
import cc.sighs.oelib.registry.extra.CommandRegister;
import com.flechazo.contact.common.command.ContactCommand;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.entity.EntityTypeRegistry;
import com.flechazo.contact.common.registry.BlockRegistry;
import com.flechazo.contact.common.registry.ItemRegistry;
import com.flechazo.contact.common.registry.ModCreativeTabRegistry;
import com.flechazo.contact.common.registry.ScreenHandlerTypeRegistry;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
import com.flechazo.contact.data.PostcardStyle;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Contact {
    public static final String MOD_ID = "contact";
    public static final String NETWORK_VERSION = "1.0";

    public static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MOD_ID, "tab"));

    private static final Logger LOGGER = LogManager.getLogger();

    public static void error(String format, Object... data) {
        Contact.LOGGER.log(Level.ERROR, String.format(format, data));
    }

    public static void warn(String format, Object... data) {
        Contact.LOGGER.log(Level.WARN, String.format(format, data));
    }

    public static void info(String format, Object... data) {
        Contact.LOGGER.log(Level.INFO, String.format(format, data));
    }

    public static ResourceLocation getRL(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static void init() {
        ContactCommonConfig.register();
        DataRegistry.register(PostcardStyle.class, PostcardStyle.CODEC);
        NetworkManager.registerPacketScanPackage("com.flechazo.contact.network");
        BlockRegistry.BLOCKS.register();
        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register();
        EntityTypeRegistry.ENTITY_TYPES.register();
        ModCreativeTabRegistry.CREATIVE_TABS.register();
        ItemRegistry.ITEMS.register();
        ContactDataComponents.DATA_COMPONENTS.register();
        ScreenHandlerTypeRegistry.MENU_TYPES.register();
        CommandRegister.registerServer(ContactCommand::register);
    }
}
