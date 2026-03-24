package com.flechazo.contact.common.config;

import cc.sighs.oelib.config.ConfigManager;
import cc.sighs.oelib.config.ConfigRecordCodecBuilder;
import cc.sighs.oelib.config.ConfigUnit;
import cc.sighs.oelib.config.field.ConfigField;
import cc.sighs.oelib.config.model.ConfigStorageFormat;
import com.flechazo.contact.Contact;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ContactCommonConfig(
        List<String> blacklistID,
        int postalSpeed,
        int ticksToAnotherWorld,
        boolean enableCenterMailbox
) {
    private static final String FILE_NAME = "contact_common";

    private static final List<String> DEFAULT_BLACKLIST = ImmutableList.of(
            "contact:parcel", "contact:letter",
            "minecraft:shulker_box",
            "minecraft:bundle",
            "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box",
            "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box",
            "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:light_gray_shulker_box",
            "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box",
            "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box",
            "minecraft:black_shulker_box"
    );

    public static final ConfigUnit<ContactCommonConfig> UNIT = ConfigRecordCodecBuilder.create(
            new ResourceLocation(Contact.MOD_ID, "contact_common"),
            instance -> instance.group(
                    ConfigField.list("blacklistID", Codec.STRING)
                            .defaultValue(DEFAULT_BLACKLIST)
                            .tooltip()
                            .comment("禁止邮寄的物品 ID 列表")
                            .forGetter(ContactCommonConfig::blacklistID),

                    ConfigField.intRange("postalSpeed", 0, 1200)
                            .defaultValue(4)
                            .tooltip()
                            .text()
                            .comment("邮件投递速度（刻），值越小越快")
                            .forGetter(ContactCommonConfig::postalSpeed),

                    ConfigField.intRange("ticksToAnotherWorld", 0, 12000)
                            .defaultValue(1200)
                            .tooltip()
                            .text()
                            .comment("跨维度邮寄所需时间（刻）")
                            .forGetter(ContactCommonConfig::ticksToAnotherWorld),

                    ConfigField.bool("enableCenterMailbox")
                            .defaultValue(false)
                            .tooltip()
                            .comment("是否启用中心邮箱（全服共享）")
                            .forGetter(ContactCommonConfig::enableCenterMailbox)
            ).apply(instance, ContactCommonConfig::new),
            meta -> meta
                    .directory(Contact.MOD_ID)
                    .fileName(FILE_NAME)
                    .format(ConfigStorageFormat.JSON)
    );

    public static ContactCommonConfig get() {
        return UNIT.get();
    }

    public static void save() {
        UNIT.save();
    }

    public static void register() {
        ConfigManager.registerServer(UNIT, player -> player.hasPermissions(4));
    }

    public static List<String> getBlacklistID() {
        return get().blacklistID();
    }

    public static int getPostalSpeed() {
        return get().postalSpeed();
    }

    public static int getTicksToAnotherWorld() {
        return get().ticksToAnotherWorld();
    }

    public static boolean isEnableCenterMailbox() {
        return get().enableCenterMailbox();
    }
}