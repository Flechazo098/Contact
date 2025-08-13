package com.flechazo.contact.data;

import com.flechazo.contact.Contact;
import com.flechazo.contact.platform.IDataManagerWrapper;
import com.flechazo.contact.platform.PlatformHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public final class PostcardDataManager {

    private static IDataManagerWrapper<PostcardStyle> manager;

    public static void initialize() {
        manager = PlatformHelper.getPlatformService().getData(PostcardStyle.class);
    }

    public static Map<ResourceLocation, PostcardStyle> getPostcards() {
        if (manager == null) {
            Contact.warn("PostcardDataManager not initialized, returning empty map");
            return Map.of();
        }
        return manager.getAllData();
    }

    public static PostcardStyle getPostcard(ResourceLocation id) {
        if (manager == null) {
            Contact.warn("PostcardDataManager not initialized, returning default style");
            return PostcardStyle.DEFAULT;
        }
        PostcardStyle style = manager.getData(id);
        return style != null ? style : PostcardStyle.DEFAULT;
    }

    public static Set<ResourceLocation> getPostcardIds() {
        return getPostcards().keySet();
    }


    public static boolean hasPostcard(ResourceLocation id) {
        return getPostcards().containsKey(id);
    }

    public static IDataManagerWrapper<PostcardStyle> getManager() {
        return manager;
    }
}