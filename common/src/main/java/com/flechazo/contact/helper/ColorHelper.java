package com.flechazo.contact.helper;

public final class ColorHelper {
    public static int getRed(int color) {
        return color >> 16 & 255;
    }

    public static int getGreen(int color) {
        return color >> 8 & 255;
    }

    public static int getBlue(int color) {
        return color & 255;
    }

    public static int getAlpha(int color) {
        return color >> 24 & 255;
    }

    public static float getRedF(int color) {
        return getRed(color) / 255.0F;
    }

    public static float getGreenF(int color) {
        return getGreen(color) / 255.0F;
    }

    public static float getBlueF(int color) {
        return getBlue(color) / 255.0F;
    }

    public static float getAlphaF(int color) {
        return getAlpha(color) / 255.0F;
    }
}