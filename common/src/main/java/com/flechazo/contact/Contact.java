package com.flechazo.contact;

import com.flechazo.contact.common.block.BlockRegistry;
import com.flechazo.contact.common.command.ContactCommand;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.entity.EntityTypeRegistry;
import com.flechazo.contact.common.handler.AddresseeSignInHandler;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.handler.WanderingTraderSaleHandler;
import com.flechazo.contact.common.item.ItemRegistry;
import com.flechazo.contact.common.registry.ModCreativeTabRegistry;
import com.flechazo.contact.common.registry.RegistryManager;
import com.flechazo.contact.common.screenhandler.ScreenHandlerTypeRegistry;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
import com.flechazo.contact.network.*;
import com.flechazo.contact.resourse.PostcardDataManager;
import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.mafuyu404.oelib.api.net.NetworkManager;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
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

    public static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(MOD_ID, "tab"));

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
        return new ResourceLocation(MOD_ID, id);
    }

    public static void initFabric() {
        CommandRegistrationEvent.EVENT.register(ContactCommand::register);
        TickEvent.SERVER_PRE.register(MailboxManager::onServerTick);
        InteractionEvent.INTERACT_ENTITY.register(WanderingTraderSaleHandler::interact);
        PlayerEvent.PLAYER_JOIN.register(AddresseeSignInHandler::onPlayerLoggedIn);
    }
    public static void initForge() {
        ConfigManager.getInstance().registerConfigHandler(ContactCommonConfig.INSTANCE);
        ConfigManager.getInstance().registerServerConfig(ContactCommonConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);
        RegistryManager.initialize();
        BlockRegistry.initBlocks();
        ItemRegistry.initItems();
        EntityTypeRegistry.init();
        ScreenHandlerTypeRegistry.init();
        BlockEntityTypeRegistry.init();
        PostcardDataManager.initialize();
        ModCreativeTabRegistry.initialize();
        CommandRegistrationEvent.EVENT.register(ContactCommand::register);
        TickEvent.SERVER_PRE.register(MailboxManager::onServerTick);
        InteractionEvent.INTERACT_ENTITY.register(WanderingTraderSaleHandler::interact);
        PlayerEvent.PLAYER_JOIN.register(AddresseeSignInHandler::onPlayerLoggedIn);
    }
}
