package com.flechazo.contact.client.shader;

import net.minecraft.client.renderer.ShaderInstance;

public final class ContactShaderResources {
    private static ShaderInstance redPacketShader;

    private ContactShaderResources() {
    }

    public static ShaderInstance getRedPacketShader() {
        return redPacketShader;
    }

    public static void setRedPacketShader(ShaderInstance shader) {
        redPacketShader = shader;
    }
}
