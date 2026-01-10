package com.flechazo.contact.client.gui.hud;

public record TexturePos(int textureX, int textureY, int width, int height) {
    public static TexturePos create(int textureX, int textureY, int width, int height) {
        return new TexturePos(textureX, textureY, width, height);
    }

    public int getX() {
        return textureX;
    }

    public int getY() {
        return textureY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
