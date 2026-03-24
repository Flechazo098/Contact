package com.flechazo.contact.data;

import cc.sighs.oelib.data.DataManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public final class PostcardDataManager {

    public static Map<ResourceLocation, PostcardStyle> getPostcards() {
        return DataManager.getAllData(PostcardStyle.class);
    }

    public static PostcardStyle getPostcard(ResourceLocation id) {
        var style = DataManager.getData(PostcardStyle.class, id);
        return style != null ? style : PostcardStyle.DEFAULT;
    }

    public static Set<ResourceLocation> getPostcardIds() {
        return getPostcards().keySet();
    }


    public static boolean hasPostcard(ResourceLocation id) {
        return getPostcards().containsKey(id);
    }
}