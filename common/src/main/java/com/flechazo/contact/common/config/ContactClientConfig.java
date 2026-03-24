package com.flechazo.contact.common.config;

import cc.sighs.oelib.config.ConfigManager;
import cc.sighs.oelib.config.ConfigRecordCodecBuilder;
import cc.sighs.oelib.config.ConfigUnit;
import cc.sighs.oelib.config.field.ConfigField;
import cc.sighs.oelib.config.model.ConfigStorageFormat;
import com.flechazo.contact.Contact;
import net.minecraft.resources.ResourceLocation;

public record ContactClientConfig(
        boolean showNewMailToast
) {
    private static final String FILE_NAME = "contact_client";

    public static final ConfigUnit<ContactClientConfig> UNIT = ConfigRecordCodecBuilder.createClient(
            new ResourceLocation(Contact.MOD_ID, "contact_client"),
            instance -> instance.group(
                    ConfigField.bool("showNewMailToast")
                            .defaultValue(true)
                            .tooltip()
                            .comment("是否在收到新邮件时显示提示")
                            .forGetter(ContactClientConfig::showNewMailToast)
            ).apply(instance, ContactClientConfig::new),
            meta -> meta
                    .directory(Contact.MOD_ID)
                    .fileName(FILE_NAME)
                    .format(ConfigStorageFormat.JSON)
    );

    public static ContactClientConfig get() {
        return UNIT.get();
    }

    public static void save() {
        UNIT.save();
    }

    public static void register() {
        ConfigManager.registerClient(UNIT);
    }

    public static boolean isShowNewMailToast() {
        return get().showNewMailToast();
    }
}