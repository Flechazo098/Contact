package com.flechazo.contact.common.config;

import com.flechazo.contact.Contact;
import com.google.common.collect.Lists;
import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.config.entry.ListStringEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ContactCommonConfig extends AutoInitConfigContainer {
    public static final ContactCommonConfig INSTANCE = new ContactCommonConfig();

    public final IConfigEntry<List<String>> blacklistID = new ListStringEntry("config.contact.common.contraband.blacklistID",
            Lists.newArrayList("contact:parcel", "contact:letter", "minecraft:shulker_box", "minecraft:bundle",
                    "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box",
                    "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box",
                    "minecraft:light_gray_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box",
                    "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box"))
            .json("blacklistID");

    public final IConfigEntry<Integer> postalSpeed = new IntegerEntry("config.contact.common.mail.postalSpeed", 4, 0, 1200)
            .json("postalSpeed");

    public final IConfigEntry<Integer> ticksToAnotherWorld = new IntegerEntry("config.contact.common.mail.ticksToAnotherWorld", 1200, 0, 12000)
            .json("ticksToAnotherWorld");

    public final IConfigEntry<Boolean> enableCenterMailbox = new BooleanEntry("config.contact.common.mail.enableCenterMailbox", false)
            .json("enableCenterMailbox");

    public ContactCommonConfig() {
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "contact_common_config"), "config.contact.common.title", "./config/contact/contact-common.json");
    }

    @Override
    public void init() {
        this.createTab("contraband", "config.contact.common.category.contraband")
                .add(this.blacklistID);

        this.createTab("mail", "config.contact.common.category.mail")
                .add(this.postalSpeed)
                .add(this.ticksToAnotherWorld)
                .add(this.enableCenterMailbox);
    }

    public static List<String> getBlacklistID() {
        return INSTANCE.blacklistID.getValue();
    }

    public static int getPostalSpeed() {
        return INSTANCE.postalSpeed.getValue();
    }

    public static int getTicksToAnotherWorld() {
        return INSTANCE.ticksToAnotherWorld.getValue();
    }

    public static boolean isEnableCenterMailbox() {
        return INSTANCE.enableCenterMailbox.getValue();
    }
}